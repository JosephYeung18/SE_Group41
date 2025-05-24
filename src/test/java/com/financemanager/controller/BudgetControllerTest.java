package com.financemanager.controller;

import com.financemanager.model.Budget;
import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * BudgetController的单元测试
 * 测试预算管理的核心功能
 */
public class BudgetControllerTest {
    
    private BudgetController budgetController;
    private DataPersistenceService mockDataService;
    private CategoryController mockCategoryController;
    
    @Before
    public void setUp() throws Exception {
        // 重置单例实例
        resetSingleton(BudgetController.class, "instance");
        resetSingleton(DataPersistenceService.class, "instance");
        
        // 创建模拟对象
        mockDataService = Mockito.mock(DataPersistenceService.class);
        mockCategoryController = Mockito.mock(CategoryController.class);
        
        // 设置DataPersistenceService单例为模拟对象
        setMockInstance(DataPersistenceService.class, "instance", mockDataService);
        
        // 准备测试数据
        List<Budget> testBudgets = new ArrayList<>();
        when(mockDataService.loadBudgets()).thenReturn(testBudgets);
        
        // 获取BudgetController实例
        budgetController = BudgetController.getInstance();
        
        // 使用反射注入模拟的CategoryController
        injectMockCategoryController();
    }
    
    /**
     * 测试添加预算功能
     */
    @Test
    public void testAddBudget() {
        // 准备测试数据
        Budget budget = createTestBudget("餐饮", 1000.0, 2025, 5);
        
        // 执行测试
        budgetController.addBudget(budget);
        
        // 验证结果
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertEquals("应该添加了一个预算", 1, allBudgets.size());
        assertEquals("添加的预算应该与原预算相同", budget, allBudgets.get(0));
        
        // 验证与依赖的交互
        verify(mockDataService, times(1)).saveBudgets(anyList());
    }
    
    /**
     * 测试添加重复预算（相同分类、年份、月份）应该更新而不是新增
     */
    @Test
    public void testAddDuplicateBudgetShouldUpdate() {
        // 准备测试数据
        Category category = createTestCategory("餐饮", TransactionType.EXPENSE);
        Budget originalBudget = createTestBudgetWithCategory(category, 1000.0, 2025, 5);
        Budget duplicateBudget = createTestBudgetWithCategory(category, 1500.0, 2025, 5);
        
        // 执行测试
        budgetController.addBudget(originalBudget);
        budgetController.addBudget(duplicateBudget); // 应该更新而不是新增
        
        // 验证结果
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertEquals("应该只有一个预算（更新而不是新增）", 1, allBudgets.size());
        assertEquals("预算金额应该被更新为新值", 1500.0, allBudgets.get(0).getAmount(), 0.001);
        
        // 验证保存操作被调用了两次
        verify(mockDataService, times(2)).saveBudgets(anyList());
    }
    
    /**
     * 测试删除预算功能
     */
    @Test
    public void testDeleteBudget() {
        // 准备测试数据
        Budget budget = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        budgetController.addBudget(budget);
        
        // 执行测试
        budgetController.deleteBudget(budget.getId());
        
        // 验证结果
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertTrue("删除后应该没有预算", allBudgets.isEmpty());
        
        // 验证保存操作被调用了两次（一次添加，一次删除）
        verify(mockDataService, times(2)).saveBudgets(anyList());
    }
    
    /**
     * 测试获取特定月份的预算
     */
    @Test
    public void testGetBudgetsForMonth() {
        // 准备测试数据
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        Budget budget2 = createTestBudget("Shopping", 800.0, 2025, 5);
        Budget budget3 = createTestBudget("Transportation", 500.0, 2025, 6); // 不同月份
        
        budgetController.addBudget(budget1);
        budgetController.addBudget(budget2);
        budgetController.addBudget(budget3);
        
        // 执行测试
        List<Budget> mayBudgets = budgetController.getBudgetsForMonth(2025, 5);
        List<Budget> juneBudgets = budgetController.getBudgetsForMonth(2025, 6);
        
        // 验证结果
        assertEquals("5月应该有2个预算", 2, mayBudgets.size());
        assertEquals("6月应该有1个预算", 1, juneBudgets.size());
        
        assertTrue("5月预算应包含餐饮预算", mayBudgets.contains(budget1));
        assertTrue("5月预算应包含购物预算", mayBudgets.contains(budget2));
        assertTrue("6月预算应包含交通预算", juneBudgets.contains(budget3));
    }
    
    /**
     * 测试更新预算支出功能
     */
    @Test
    public void testUpdateBudgetSpending() {
        // 准备测试数据
        Category category = createTestCategory("Food & Dining", TransactionType.EXPENSE);
        Budget budget = createTestBudgetWithCategory(category, 1000.0, 2025, 5);
        budgetController.addBudget(budget);
        
        // 执行测试
        budgetController.updateBudgetSpending(category, 2025, 5, 250.0);
        
        // 验证结果
        List<Budget> budgets = budgetController.getBudgetsForMonth(2025, 5);
        assertEquals("应该有一个预算", 1, budgets.size());
        assertEquals("已花费金额应该被更新", 250.0, budgets.get(0).getSpent(), 0.001);
        
        // 再次更新支出
        budgetController.updateBudgetSpending(category, 2025, 5, 150.0);
        budgets = budgetController.getBudgetsForMonth(2025, 5);
        assertEquals("已花费金额应该累加", 400.0, budgets.get(0).getSpent(), 0.001);
    }
    
    /**
     * 测试计算月度总预算
     */
    @Test
    public void testCalculateTotalBudgetForMonth() {
        // 准备测试数据
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        Budget budget2 = createTestBudget("Shopping",800.0, 2025, 5);
        Budget budget3 = createTestBudget("Transportation", 500.0, 2025, 6); // 不同月份

        budgetController.addBudget(budget2);
        budgetController.addBudget(budget3);
        
        // 执行测试
        double totalBudgetMay = budgetController.calculateTotalBudgetForMonth(2025, 5);
        double totalBudgetJune = budgetController.calculateTotalBudgetForMonth(2025, 6);
        
        // 验证结果
        assertEquals("5月总预算应为1800.0", 1800.0, totalBudgetMay, 0.001);
        assertEquals("6月总预算应为500.0", 500.0, totalBudgetJune, 0.001);
    }
    
    /**
     * 测试计算月度总支出
     */
    @Test
    public void testCalculateTotalSpentForMonth() {
        // 准备测试数据
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        budget1.setSpent(300.0);
        Budget budget2 = createTestBudget("Shopping", 800.0, 2025, 5);
        budget2.setSpent(150.0);
        
        budgetController.addBudget(budget1);
        budgetController.addBudget(budget2);
        
        // 执行测试
        double totalSpent = budgetController.calculateTotalSpentForMonth(2025, 5);
        
        // 验证结果
        assertEquals("5月总支出应为450.0", 450.0, totalSpent, 0.001);
    }
    
    /**
     * 创建测试用的预算对象
     */
    private Budget createTestBudget(String categoryName, double amount, int year, int month) {
        Category category = createTestCategory(categoryName, TransactionType.EXPENSE);
        return createTestBudgetWithCategory(category, amount, year, month);
    }
    
    /**
     * 创建带指定分类的测试预算对象
     */
    private Budget createTestBudgetWithCategory(Category category, double amount, int year, int month) {
        Budget budget = new Budget();
        budget.setCategory(category);
        budget.setAmount(amount);
        budget.setYear(year);
        budget.setMonth(month);
        budget.setSpent(0.0);
        return budget;
    }
    
    /**
     * 创建测试用的分类对象
     */
    private Category createTestCategory(String name, TransactionType type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        return category;
    }
    
    /**
     * 使用反射重置单例实例
     */
    private void resetSingleton(Class<?> clazz, String fieldName) {
        try {
            java.lang.reflect.Field instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 使用反射设置单例实例为模拟对象
     */
    private void setMockInstance(Class<?> clazz, String fieldName, Object mockInstance) {
        try {
            java.lang.reflect.Field instance = clazz.getDeclaredField(fieldName);
            instance.setAccessible(true);
            instance.set(null, mockInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 使用反射注入模拟的CategoryController
     */
    private void injectMockCategoryController() {
        try {
            java.lang.reflect.Field field = BudgetController.class.getDeclaredField("categoryController");
            field.setAccessible(true);
            field.set(budgetController, mockCategoryController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package com.financemanager.controller;

import com.financemanager.model.Category;
import com.financemanager.model.Transaction;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * TransactionController的单元测试
 * 展示如何使用Mockito进行依赖注入和测试隔离
 */
public class TransactionControllerTest {
    
    private TransactionController transactionController;
    private DataPersistenceService mockDataService;
    private CategoryController mockCategoryController;
    private BudgetController mockBudgetController;
    
    @Before
    public void setUp() throws Exception {
        // 重置单例实例
        resetSingleton(TransactionController.class, "instance");
        resetSingleton(DataPersistenceService.class, "instance");
        resetSingleton(BudgetController.class, "instance");
        
        // 创建模拟对象
        mockDataService = Mockito.mock(DataPersistenceService.class);
        mockCategoryController = Mockito.mock(CategoryController.class);
        mockBudgetController = Mockito.mock(BudgetController.class);
        
        // 设置DataPersistenceService单例为模拟对象
        setMockInstance(DataPersistenceService.class, "instance", mockDataService);
        
        // 设置BudgetController单例为模拟对象
        setMockInstance(BudgetController.class, "instance", mockBudgetController);
        
        // 准备测试数据
        List<Transaction> testTransactions = new ArrayList<>();
        when(mockDataService.loadTransactions()).thenReturn(testTransactions);
        
        // 获取TransactionController实例
        transactionController = TransactionController.getInstance();
        
        // 使用反射注入模拟的CategoryController
        injectMockCategoryController();
    }
    
    /**
     * 测试添加交易功能
     */
    @Test
    public void testAddTransaction() {
        // 准备测试数据
        Transaction transaction = createTestTransaction(TransactionType.EXPENSE, 100.0);
        
        // 执行测试
        transactionController.addTransaction(transaction);
        
        // 验证结果
        List<Transaction> allTransactions = transactionController.getAllTransactions();
        assertEquals("应该添加了一个交易", 1, allTransactions.size());
        assertEquals("添加的交易应该与原交易相同", transaction, allTransactions.get(0));
        
        // 验证与依赖的交互
        verify(mockDataService, times(1)).saveTransactions(anyList());
    }
    
    /**
     * 测试删除交易功能
     */
    @Test
    public void testDeleteTransaction() {
        // 准备测试数据
        Transaction transaction = createTestTransaction(TransactionType.EXPENSE, 100.0);
        transactionController.addTransaction(transaction);
        
        // 执行测试
        transactionController.deleteTransaction(transaction.getId());
        
        // 验证结果
        List<Transaction> allTransactions = transactionController.getAllTransactions();
        assertTrue("删除后应该没有交易", allTransactions.isEmpty());
        
        // 验证与依赖的交互
        verify(mockDataService, times(2)).saveTransactions(anyList()); // 一次添加，一次删除
    }
    
    /**
     * 测试按类型筛选交易功能
     */
    @Test
    public void testGetTransactionsByType() {
        // 准备测试数据
        Transaction expense1 = createTestTransaction(TransactionType.EXPENSE, 100.0);
        Transaction expense2 = createTestTransaction(TransactionType.EXPENSE, 200.0);
        Transaction income = createTestTransaction(TransactionType.INCOME, 300.0);
        
        transactionController.addTransaction(expense1);
        transactionController.addTransaction(expense2);
        transactionController.addTransaction(income);
        
        // 执行测试
        List<Transaction> expenses = transactionController.getTransactionsByType(TransactionType.EXPENSE);
        List<Transaction> incomes = transactionController.getTransactionsByType(TransactionType.INCOME);
        
        // 验证结果
        assertEquals("应该有2个支出交易", 2, expenses.size());
        assertEquals("应该有1个收入交易", 1, incomes.size());
        
        assertTrue("支出列表应包含expense1", expenses.contains(expense1));
        assertTrue("支出列表应包含expense2", expenses.contains(expense2));
        assertTrue("收入列表应包含income", incomes.contains(income));
    }
    
    /**
     * 测试计算月度总支出功能
     */
    @Test
    public void testCalculateTotalExpenseForMonth() {
        // 准备测试数据 - 当前月份的交易
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        
        // 创建当前月份的交易
        Transaction expense1 = createTestTransactionWithDate(
                TransactionType.EXPENSE, 100.0, createDate(currentYear, currentMonth, 15));
        Transaction expense2 = createTestTransactionWithDate(
                TransactionType.EXPENSE, 200.0, createDate(currentYear, currentMonth, 20));
        Transaction income = createTestTransactionWithDate(
                TransactionType.INCOME, 300.0, createDate(currentYear, currentMonth, 10));
        
        // 创建不同月份的交易
        Transaction otherMonthExpense = createTestTransactionWithDate(
                TransactionType.EXPENSE, 500.0, createDate(currentYear, currentMonth + 1, 5));
        
        transactionController.addTransaction(expense1);
        transactionController.addTransaction(expense2);
        transactionController.addTransaction(income);
        transactionController.addTransaction(otherMonthExpense);
        
        // 执行测试
        double totalExpense = transactionController.calculateTotalExpenseForMonth(currentYear, currentMonth);
        
        // 验证结果
        assertEquals("当月总支出应为300.0", 300.0, totalExpense, 0.001);
    }
    
    /**
     * 测试计算月度总收入功能
     */
    @Test
    public void testCalculateTotalIncomeForMonth() {
        // 准备测试数据 - 当前月份的交易
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        
        // 创建当前月份的交易
        Transaction expense = createTestTransactionWithDate(
                TransactionType.EXPENSE, 100.0, createDate(currentYear, currentMonth, 15));
        Transaction income1 = createTestTransactionWithDate(
                TransactionType.INCOME, 200.0, createDate(currentYear, currentMonth, 10));
        Transaction income2 = createTestTransactionWithDate(
                TransactionType.INCOME, 300.0, createDate(currentYear, currentMonth, 20));
        
        // 创建不同月份的交易
        Transaction otherMonthIncome = createTestTransactionWithDate(
                TransactionType.INCOME, 500.0, createDate(currentYear, currentMonth + 1, 5));
        
        transactionController.addTransaction(expense);
        transactionController.addTransaction(income1);
        transactionController.addTransaction(income2);
        transactionController.addTransaction(otherMonthIncome);
        
        // 执行测试
        double totalIncome = transactionController.calculateTotalIncomeForMonth(currentYear, currentMonth);
        
        // 验证结果
        assertEquals("当月总收入应为500.0", 500.0, totalIncome, 0.001);
    }
    
    /**
     * 创建测试用的交易对象
     */
    private Transaction createTestTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDate(new Date());
        
        Category category = new Category();
        category.setName(type == TransactionType.INCOME ? "测试收入" : "测试支出");
        category.setType(type);
        transaction.setCategory(category);
        
        return transaction;
    }
    
    /**
     * 创建带指定日期的测试交易对象
     */
    private Transaction createTestTransactionWithDate(TransactionType type, double amount, Date date) {
        Transaction transaction = createTestTransaction(type, amount);
        transaction.setDate(date);
        return transaction;
    }
    
    /**
     * 创建指定年月日的日期对象
     */
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
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
            java.lang.reflect.Field field = TransactionController.class.getDeclaredField("categoryController");
            field.setAccessible(true);
            field.set(transactionController, mockCategoryController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

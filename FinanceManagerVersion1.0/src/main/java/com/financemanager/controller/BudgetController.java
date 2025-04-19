package com.financemanager.controller;

import com.financemanager.model.Budget;
import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * 预算控制器，负责管理预算数据
 */
public class BudgetController {
    
    private List<Budget> budgets;
    private DataPersistenceService dataPersistenceService;
    private CategoryController categoryController;
    
    // 添加单例实例
    private static BudgetController instance;
    
    // 将构造函数改为私有
    private BudgetController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        this.categoryController = new CategoryController();
        this.budgets = dataPersistenceService.loadBudgets();
    }
    
    /**
     * 获取BudgetController的单例实例
     */
    public static synchronized BudgetController getInstance() {
        if (instance == null) {
            instance = new BudgetController();
        }
        return instance;
    }
    
    /**
     * 添加预算
     */
    public void addBudget(Budget budget) {
        // 检查是否已存在相同分类、年份和月份的预算
        for (Budget existingBudget : budgets) {
            if (existingBudget.getCategory().getId().equals(budget.getCategory().getId()) &&
                existingBudget.getYear() == budget.getYear() &&
                existingBudget.getMonth() == budget.getMonth()) {
                
                // 已存在，更新预算金额
                existingBudget.setAmount(budget.getAmount());
                dataPersistenceService.saveBudgets(budgets);
                return;
            }
        }
        
        // 不存在，添加新预算
        budgets.add(budget);
        dataPersistenceService.saveBudgets(budgets);
    }
    
    /**
     * 更新预算
     */
    public void updateBudget(Budget budget) {
        for (int i = 0; i < budgets.size(); i++) {
            if (budgets.get(i).getId().equals(budget.getId())) {
                budgets.set(i, budget);
                break;
            }
        }
        
        dataPersistenceService.saveBudgets(budgets);
    }
    
    /**
     * 删除预算
     */
    public void deleteBudget(String budgetId) {
        budgets.removeIf(budget -> budget.getId().equals(budgetId));
        dataPersistenceService.saveBudgets(budgets);
    }
    
    /**
     * 获取所有预算
     */
    public List<Budget> getAllBudgets() {
        return new ArrayList<>(budgets);
    }
    
    /**
     * 获取特定月份的预算
     */
    public List<Budget> getBudgetsForMonth(int year, int month) {
        List<Budget> result = new ArrayList<>();
        
        for (Budget budget : budgets) {
            if (budget.getYear() == year && budget.getMonth() == month) {
                result.add(budget);
            }
        }
        
        return result;
    }
    
    /**
     * 更新预算支出
     */
    public void updateBudgetSpending(Category category, int year, int month, double amount) {
        // 查找匹配的预算
        for (Budget budget : budgets) {
            if (budget.getCategory().getId().equals(category.getId()) &&
                budget.getYear() == year && budget.getMonth() == month) {
                
                // 更新已花费金额
                budget.setSpent(budget.getSpent() + amount);
                // 保存预算
                dataPersistenceService.saveBudgets(budgets);
                return;
            }
        }
        
        // 如果没有找到匹配的预算，创建一个新的预算
        Budget newBudget = new Budget();
        newBudget.setCategory(category);
        newBudget.setYear(year);
        newBudget.setMonth(month);
        newBudget.setAmount(0); // 默认预算为0
        newBudget.setSpent(amount > 0 ? amount : 0); // 设置已花费金额
        
        budgets.add(newBudget);
        dataPersistenceService.saveBudgets(budgets);
    }
    
    /**
     * 获取支出分类
     */
    public List<Category> getExpenseCategories() {
        return categoryController.getCategoriesByType(TransactionType.EXPENSE);
    }
    
    /**
     * 计算特定月份的总预算
     */
    public double calculateTotalBudgetForMonth(int year, int month) {
        double total = 0;
        
        for (Budget budget : getBudgetsForMonth(year, month)) {
            total += budget.getAmount();
        }
        
        return total;
    }
    
    /**
     * 计算特定月份的总已花费
     */
    public double calculateTotalSpentForMonth(int year, int month) {
        double total = 0;
        
        for (Budget budget : getBudgetsForMonth(year, month)) {
            total += budget.getSpent();
        }
        
        return total;
    }
}
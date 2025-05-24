package com.financemanager.util;

import com.financemanager.controller.TransactionController;
import com.financemanager.controller.CategoryController;
import com.financemanager.controller.BudgetController;
import com.financemanager.model.Transaction;
import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.model.Budget;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataAdapter {

    private static TransactionController transactionController = TransactionController.getInstance();
    private static CategoryController categoryController = new CategoryController();
    private static BudgetController budgetController = BudgetController.getInstance();

    /**
     * 获取指定月份的分类支出数据，修正为使用已加载的JSON数据
     */
    public static Map<String, Double> getCategoryExpensesForMonth(int year, int month) {
        List<Transaction> transactions = transactionController.getTransactionsForMonth(year, month);
        Map<String, Double> result = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.EXPENSE && transaction.getCategory() != null) {
                String category = transaction.getCategory().getName();
                double amount = transaction.getAmount();

                result.put(category, result.getOrDefault(category, 0.0) + amount);
            }
        }

        return result;
    }

    /**
     * 获取预算完成情况数据
     */
    public static Map<String, Map<String, Double>> getBudgetCompletionData(int year, int month) {
        List<Budget> budgets = budgetController.getBudgetsForMonth(year, month);
        Map<String, Map<String, Double>> result = new HashMap<>();

        for (Budget budget : budgets) {
            if (budget.getCategory() != null) {
                String categoryName = budget.getCategory().getName();
                Map<String, Double> budgetData = new HashMap<>();
                budgetData.put("amount", budget.getAmount());
                budgetData.put("spent", budget.getSpent());
                result.put(categoryName, budgetData);
            }
        }

        return result;
    }

    /**
     * 获取指定月份的财务摘要
     */
    public static Map<String, Double> getMonthSummary(int year, int month) {
        double income = transactionController.calculateTotalIncomeForMonth(year, month);
        double expenses = transactionController.calculateTotalExpenseForMonth(year, month);
        double balance = income - expenses;

        Map<String, Double> summary = new HashMap<>();
        summary.put("income", income);
        summary.put("expenses", expenses);
        summary.put("balance", balance);

        return summary;
    }

    /**
     * 获取多个月份的财务数据
     */
    public static List<Map<String, Object>> getMultiMonthData(int monthCount) {
        List<Map<String, Object>> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        // 从当前月份向前获取数据
        for (int i = 0; i < monthCount; i++) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            Map<String, Object> monthData = new HashMap<>();
            monthData.put("year", year);
            monthData.put("month", month);
            monthData.put("monthName", String.format("%02d月", month));
            monthData.put("summary", getMonthSummary(year, month));
            monthData.put("categories", getCategoryExpensesForMonth(year, month));

            result.add(monthData);
            cal.add(Calendar.MONTH, -1);
        }

        return result;
    }

    /**
     * 预测下个月的支出
     */
    public static Map<String, Double> predictNextMonthExpenses(int year, int month) {
        Map<String, Double> currentExpenses = getCategoryExpensesForMonth(year, month);
        Map<String, Double> prediction = new HashMap<>();

        // 基于当前月支出，预测下月支出（简单增加5%）
        for (Map.Entry<String, Double> entry : currentExpenses.entrySet()) {
            prediction.put(entry.getKey(), entry.getValue() * 1.05);
        }

        return prediction;
    }
}
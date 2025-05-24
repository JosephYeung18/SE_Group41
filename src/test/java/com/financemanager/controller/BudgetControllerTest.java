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
 * BudgetController Unit Tests
 * Testing core budget management functionality
 */
public class BudgetControllerTest {

    private BudgetController budgetController;
    private DataPersistenceService mockDataService;
    private CategoryController mockCategoryController;

    @Before
    public void setUp() throws Exception {
        // Reset singleton instances
        resetSingleton(BudgetController.class, "instance");
        resetSingleton(DataPersistenceService.class, "instance");

        // Create mock objects
        mockDataService = Mockito.mock(DataPersistenceService.class);
        mockCategoryController = Mockito.mock(CategoryController.class);

        // Set DataPersistenceService singleton to mock object
        setMockInstance(DataPersistenceService.class, "instance", mockDataService);

        // Prepare test data
        List<Budget> testBudgets = new ArrayList<>();
        when(mockDataService.loadBudgets()).thenReturn(testBudgets);

        // Get BudgetController instance
        budgetController = BudgetController.getInstance();

        // Inject mock CategoryController using reflection
        injectMockCategoryController();
    }

    /**
     * Test add budget functionality
     */
    @Test
    public void testAddBudget() {
        // Prepare test data
        Budget budget = createTestBudget("Food & Dining", 1000.0, 2025, 5);

        // Execute test
        budgetController.addBudget(budget);

        // Verify results
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertEquals("Should have added one budget", 1, allBudgets.size());
        assertEquals("Added budget should match original budget", budget, allBudgets.get(0));

        // Verify interactions with dependencies
        verify(mockDataService, times(1)).saveBudgets(anyList());
    }

    /**
     * Test adding duplicate budget (same category, year, month) should update instead of adding new
     */
    @Test
    public void testAddDuplicateBudgetShouldUpdate() {
        // Prepare test data
        Category category = createTestCategory("Food & Dining", TransactionType.EXPENSE);
        Budget originalBudget = createTestBudgetWithCategory(category, 1000.0, 2025, 5);
        Budget duplicateBudget = createTestBudgetWithCategory(category, 1500.0, 2025, 5);

        // Execute test
        budgetController.addBudget(originalBudget);
        budgetController.addBudget(duplicateBudget); // Should update instead of adding new

        // Verify results
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertEquals("Should have only one budget (updated instead of added)", 1, allBudgets.size());
        assertEquals("Budget amount should be updated to new value", 1500.0, allBudgets.get(0).getAmount(), 0.001);

        // Verify save operation was called twice
        verify(mockDataService, times(2)).saveBudgets(anyList());
    }

    /**
     * Test delete budget functionality
     */
    @Test
    public void testDeleteBudget() {
        // Prepare test data
        Budget budget = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        budgetController.addBudget(budget);

        // Execute test
        budgetController.deleteBudget(budget.getId());

        // Verify results
        List<Budget> allBudgets = budgetController.getAllBudgets();
        assertTrue("Should have no budgets after deletion", allBudgets.isEmpty());

        // Verify save operation was called twice (once for add, once for delete)
        verify(mockDataService, times(2)).saveBudgets(anyList());
    }

    /**
     * Test get budgets for specific month
     */
    @Test
    public void testGetBudgetsForMonth() {
        // Prepare test data
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        Budget budget2 = createTestBudget("Shopping", 800.0, 2025, 5);
        Budget budget3 = createTestBudget("Transportation", 500.0, 2025, 6); // Different month

        budgetController.addBudget(budget1);
        budgetController.addBudget(budget2);
        budgetController.addBudget(budget3);

        // Execute test
        List<Budget> mayBudgets = budgetController.getBudgetsForMonth(2025, 5);
        List<Budget> juneBudgets = budgetController.getBudgetsForMonth(2025, 6);

        // Verify results
        assertEquals("May should have 2 budgets", 2, mayBudgets.size());
        assertEquals("June should have 1 budget", 1, juneBudgets.size());

        assertTrue("May budgets should contain food budget", mayBudgets.contains(budget1));
        assertTrue("May budgets should contain shopping budget", mayBudgets.contains(budget2));
        assertTrue("June budgets should contain transportation budget", juneBudgets.contains(budget3));
    }

    /**
     * Test update budget spending functionality
     */
    @Test
    public void testUpdateBudgetSpending() {
        // Prepare test data
        Category category = createTestCategory("Food & Dining", TransactionType.EXPENSE);
        Budget budget = createTestBudgetWithCategory(category, 1000.0, 2025, 5);
        budgetController.addBudget(budget);

        // Execute test
        budgetController.updateBudgetSpending(category, 2025, 5, 250.0);

        // Verify results
        List<Budget> budgets = budgetController.getBudgetsForMonth(2025, 5);
        assertEquals("Should have one budget", 1, budgets.size());
        assertEquals("Spent amount should be updated", 250.0, budgets.get(0).getSpent(), 0.001);

        // Update spending again
        budgetController.updateBudgetSpending(category, 2025, 5, 150.0);
        budgets = budgetController.getBudgetsForMonth(2025, 5);
        assertEquals("Spent amount should be accumulated", 400.0, budgets.get(0).getSpent(), 0.001);
    }

    /**
     * Test calculate total budget for month
     */
    @Test
    public void testCalculateTotalBudgetForMonth() {
        // Prepare test data
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        Budget budget2 = createTestBudget("Shopping", 800.0, 2025, 5);
        Budget budget3 = createTestBudget("Transportation", 500.0, 2025, 6); // Different month

        budgetController.addBudget(budget1);
        budgetController.addBudget(budget2);
        budgetController.addBudget(budget3);

        // Execute test
        double totalBudgetMay = budgetController.calculateTotalBudgetForMonth(2025, 5);
        double totalBudgetJune = budgetController.calculateTotalBudgetForMonth(2025, 6);

        // Verify results
        assertEquals("May total budget should be 1800.0", 1800.0, totalBudgetMay, 0.001);
        assertEquals("June total budget should be 500.0", 500.0, totalBudgetJune, 0.001);
    }

    /**
     * Test calculate total spent for month
     */
    @Test
    public void testCalculateTotalSpentForMonth() {
        // Prepare test data
        Budget budget1 = createTestBudget("Food & Dining", 1000.0, 2025, 5);
        budget1.setSpent(300.0);
        Budget budget2 = createTestBudget("Shopping", 800.0, 2025, 5);
        budget2.setSpent(150.0);

        budgetController.addBudget(budget1);
        budgetController.addBudget(budget2);

        // Execute test
        double totalSpent = budgetController.calculateTotalSpentForMonth(2025, 5);

        // Verify results
        assertEquals("May total spent should be 450.0", 450.0, totalSpent, 0.001);
    }

    /**
     * Create test budget object
     */
    private Budget createTestBudget(String categoryName, double amount, int year, int month) {
        Category category = createTestCategory(categoryName, TransactionType.EXPENSE);
        return createTestBudgetWithCategory(category, amount, year, month);
    }

    /**
     * Create test budget object with specified category
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
     * Create test category object
     */
    private Category createTestCategory(String name, TransactionType type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        return category;
    }

    /**
     * Reset singleton instance using reflection
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
     * Set singleton instance to mock object using reflection
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
     * Inject mock CategoryController using reflection
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
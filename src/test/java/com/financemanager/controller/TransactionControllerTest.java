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
 * TransactionController Unit Tests
 * Demonstrates how to use Mockito for dependency injection and test isolation
 */
public class TransactionControllerTest {

    private TransactionController transactionController;
    private DataPersistenceService mockDataService;
    private CategoryController mockCategoryController;
    private BudgetController mockBudgetController;

    @Before
    public void setUp() throws Exception {
        // Reset singleton instances
        resetSingleton(TransactionController.class, "instance");
        resetSingleton(DataPersistenceService.class, "instance");
        resetSingleton(BudgetController.class, "instance");

        // Create mock objects
        mockDataService = Mockito.mock(DataPersistenceService.class);
        mockCategoryController = Mockito.mock(CategoryController.class);
        mockBudgetController = Mockito.mock(BudgetController.class);

        // Set DataPersistenceService singleton to mock object
        setMockInstance(DataPersistenceService.class, "instance", mockDataService);

        // Set BudgetController singleton to mock object
        setMockInstance(BudgetController.class, "instance", mockBudgetController);

        // Prepare test data
        List<Transaction> testTransactions = new ArrayList<>();
        when(mockDataService.loadTransactions()).thenReturn(testTransactions);

        // Get TransactionController instance
        transactionController = TransactionController.getInstance();

        // Inject mock CategoryController using reflection
        injectMockCategoryController();
    }

    /**
     * Test add transaction functionality
     */
    @Test
    public void testAddTransaction() {
        // Prepare test data
        Transaction transaction = createTestTransaction(TransactionType.EXPENSE, 100.0);

        // Execute test
        transactionController.addTransaction(transaction);

        // Verify results
        List<Transaction> allTransactions = transactionController.getAllTransactions();
        assertEquals("Should have added one transaction", 1, allTransactions.size());
        assertEquals("Added transaction should match original transaction", transaction, allTransactions.get(0));

        // Verify interactions with dependencies
        verify(mockDataService, times(1)).saveTransactions(anyList());
    }

    /**
     * Test delete transaction functionality
     */
    @Test
    public void testDeleteTransaction() {
        // Prepare test data
        Transaction transaction = createTestTransaction(TransactionType.EXPENSE, 100.0);
        transactionController.addTransaction(transaction);

        // Execute test
        transactionController.deleteTransaction(transaction.getId());

        // Verify results
        List<Transaction> allTransactions = transactionController.getAllTransactions();
        assertTrue("Should have no transactions after deletion", allTransactions.isEmpty());

        // Verify interactions with dependencies
        verify(mockDataService, times(2)).saveTransactions(anyList()); // Once for add, once for delete
    }

    /**
     * Test filter transactions by type functionality
     */
    @Test
    public void testGetTransactionsByType() {
        // Prepare test data
        Transaction expense1 = createTestTransaction(TransactionType.EXPENSE, 100.0);
        Transaction expense2 = createTestTransaction(TransactionType.EXPENSE, 200.0);
        Transaction income = createTestTransaction(TransactionType.INCOME, 300.0);

        transactionController.addTransaction(expense1);
        transactionController.addTransaction(expense2);
        transactionController.addTransaction(income);

        // Execute test
        List<Transaction> expenses = transactionController.getTransactionsByType(TransactionType.EXPENSE);
        List<Transaction> incomes = transactionController.getTransactionsByType(TransactionType.INCOME);

        // Verify results
        assertEquals("Should have 2 expense transactions", 2, expenses.size());
        assertEquals("Should have 1 income transaction", 1, incomes.size());

        assertTrue("Expense list should contain expense1", expenses.contains(expense1));
        assertTrue("Expense list should contain expense2", expenses.contains(expense2));
        assertTrue("Income list should contain income", incomes.contains(income));
    }

    /**
     * Test calculate total expense for month functionality
     */
    @Test
    public void testCalculateTotalExpenseForMonth() {
        // Prepare test data - current month transactions
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        // Create current month transactions
        Transaction expense1 = createTestTransactionWithDate(
                TransactionType.EXPENSE, 100.0, createDate(currentYear, currentMonth, 15));
        Transaction expense2 = createTestTransactionWithDate(
                TransactionType.EXPENSE, 200.0, createDate(currentYear, currentMonth, 20));
        Transaction income = createTestTransactionWithDate(
                TransactionType.INCOME, 300.0, createDate(currentYear, currentMonth, 10));

        // Create different month transaction
        Transaction otherMonthExpense = createTestTransactionWithDate(
                TransactionType.EXPENSE, 500.0, createDate(currentYear, currentMonth + 1, 5));

        transactionController.addTransaction(expense1);
        transactionController.addTransaction(expense2);
        transactionController.addTransaction(income);
        transactionController.addTransaction(otherMonthExpense);

        // Execute test
        double totalExpense = transactionController.calculateTotalExpenseForMonth(currentYear, currentMonth);

        // Verify results
        assertEquals("Current month total expense should be 300.0", 300.0, totalExpense, 0.001);
    }

    /**
     * Test calculate total income for month functionality
     */
    @Test
    public void testCalculateTotalIncomeForMonth() {
        // Prepare test data - current month transactions
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        // Create current month transactions
        Transaction expense = createTestTransactionWithDate(
                TransactionType.EXPENSE, 100.0, createDate(currentYear, currentMonth, 15));
        Transaction income1 = createTestTransactionWithDate(
                TransactionType.INCOME, 200.0, createDate(currentYear, currentMonth, 10));
        Transaction income2 = createTestTransactionWithDate(
                TransactionType.INCOME, 300.0, createDate(currentYear, currentMonth, 20));

        // Create different month transaction
        Transaction otherMonthIncome = createTestTransactionWithDate(
                TransactionType.INCOME, 500.0, createDate(currentYear, currentMonth + 1, 5));

        transactionController.addTransaction(expense);
        transactionController.addTransaction(income1);
        transactionController.addTransaction(income2);
        transactionController.addTransaction(otherMonthIncome);

        // Execute test
        double totalIncome = transactionController.calculateTotalIncomeForMonth(currentYear, currentMonth);

        // Verify results
        assertEquals("Current month total income should be 500.0", 500.0, totalIncome, 0.001);
    }

    /**
     * Create test transaction object
     */
    private Transaction createTestTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDate(new Date());

        Category category = new Category();
        category.setName(type == TransactionType.INCOME ? "Test Income" : "Test Expense");
        category.setType(type);
        transaction.setCategory(category);

        return transaction;
    }

    /**
     * Create test transaction object with specified date
     */
    private Transaction createTestTransactionWithDate(TransactionType type, double amount, Date date) {
        Transaction transaction = createTestTransaction(type, amount);
        transaction.setDate(date);
        return transaction;
    }

    /**
     * Create date object for specified year, month, day
     */
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
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
            java.lang.reflect.Field field = TransactionController.class.getDeclaredField("categoryController");
            field.setAccessible(true);
            field.set(transactionController, mockCategoryController);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

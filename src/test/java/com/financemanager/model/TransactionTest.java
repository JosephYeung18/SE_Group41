package com.financemanager.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

/**
 * Transaction class unit tests
 */
public class TransactionTest {

    private Transaction transaction;
    private Category category;

    @Before
    public void setUp() {
        // Initialize test objects before each test method execution
        transaction = new Transaction();
        category = new Category();
        category.setName("Food & Dining");
    }

    @Test
    public void testNewTransactionHasId() {
        // Test that newly created Transaction object should have a non-null ID
        assertNotNull("New transaction should have ID", transaction.getId());
        assertFalse("Transaction ID should not be empty", transaction.getId().isEmpty());
    }

    @Test
    public void testNewTransactionHasCurrentDate() {
        // Test that newly created Transaction object should have current date
        assertNotNull("New transaction should have date", transaction.getDate());

        // Check if date is close to current time (allow 1 second margin)
        long currentTime = System.currentTimeMillis();
        long transactionTime = transaction.getDate().getTime();
        assertTrue("Transaction date should be current date",
                Math.abs(currentTime - transactionTime) < 1000);
    }

    @Test
    public void testSetAndGetCategory() {
        // Test set and get Category
        transaction.setCategory(category);
        assertEquals("Should be able to get the set category correctly", category, transaction.getCategory());
    }

    @Test
    public void testSetAndGetAmount() {
        // Test set and get amount
        double amount = 99.99;
        transaction.setAmount(amount);
        assertEquals("Should be able to get the set amount correctly", amount, transaction.getAmount(), 0.001);
    }

    @Test
    public void testSetAndGetComment() {
        // Test set and get comment
        String comment = "Lunch expense";
        transaction.setComment(comment);
        assertEquals("Should be able to get the set comment correctly", comment, transaction.getComment());
    }

    @Test
    public void testSetAndGetType() {
        // Test set and get transaction type
        TransactionType type = TransactionType.EXPENSE;
        transaction.setType(type);
        assertEquals("Should be able to get the set transaction type correctly", type, transaction.getType());
    }

    @Test
    public void testToString() {
        // Test toString method contains all necessary information
        transaction.setCategory(category);
        transaction.setAmount(50.0);
        transaction.setComment("Test comment");
        transaction.setType(TransactionType.EXPENSE);

        String toString = transaction.toString();
        assertTrue("toString should contain ID", toString.contains(transaction.getId()));
        assertTrue("toString should contain category name", toString.contains(category.getName()));
        assertTrue("toString should contain amount", toString.contains("50.0"));
        assertTrue("toString should contain comment", toString.contains("Test comment"));
        assertTrue("toString should contain transaction type", toString.contains(TransactionType.EXPENSE.toString()));
    }

    @Test
    public void testIsExpense() {
        // Test method to determine if transaction is expense
        transaction.setType(TransactionType.EXPENSE);
        assertTrue("EXPENSE type should be identified as expense", transaction.isExpense());

        transaction.setType(TransactionType.INCOME);
        assertFalse("INCOME type should not be identified as expense", transaction.isExpense());
    }

    @Test
    public void testIsIncome() {
        // Test method to determine if transaction is income
        transaction.setType(TransactionType.INCOME);
        assertTrue("INCOME type should be identified as income", transaction.isIncome());

        transaction.setType(TransactionType.EXPENSE);
        assertFalse("EXPENSE type should not be identified as income", transaction.isIncome());
    }

    @Test
    public void testGetSignedAmount() {
        // Test get signed amount (expense as negative, income as positive)
        double amount = 100.0;
        transaction.setAmount(amount);

        transaction.setType(TransactionType.INCOME);
        assertEquals("Income signed amount should be positive", amount, transaction.getSignedAmount(), 0.001);

        transaction.setType(TransactionType.EXPENSE);
        assertEquals("Expense signed amount should be negative", -amount, transaction.getSignedAmount(), 0.001);
    }
}

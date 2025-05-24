package com.financemanager.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Category class unit tests
 * Testing basic functionality of category model
 */
public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        // Initialize test object before each test method execution
        category = new Category();
    }

    /**
     * Test default constructor
     */
    @Test
    public void testDefaultConstructor() {
        // Test that newly created Category object should have a non-null ID
        assertNotNull("New category should have ID", category.getId());
        assertFalse("Category ID should not be empty", category.getId().isEmpty());

        // Test default values of other fields
        assertNull("Category name should initially be null", category.getName());
        assertNull("Category type should initially be null", category.getType());
        assertNull("Icon name should initially be null", category.getIconName());
        assertNull("Color should initially be null", category.getColor());
    }

    /**
     * Test parameterized constructor
     */
    @Test
    public void testParameterizedConstructor() {
        // Create category using parameterized constructor
        String name = "Food & Dining";
        TransactionType type = TransactionType.EXPENSE;
        Category paramCategory = new Category(name, type);

        // Verify results
        assertNotNull("Category should have ID", paramCategory.getId());
        assertFalse("Category ID should not be empty", paramCategory.getId().isEmpty());
        assertEquals("Category name should be set correctly", name, paramCategory.getName());
        assertEquals("Category type should be set correctly", type, paramCategory.getType());
    }

    /**
     * Test set and get category name
     */
    @Test
    public void testSetAndGetName() {
        String name = "Shopping";
        category.setName(name);
        assertEquals("Should be able to get the set category name correctly", name, category.getName());

        // Test setting null value
        category.setName(null);
        assertNull("Should be able to set null value", category.getName());

        // Test setting empty string
        category.setName("");
        assertEquals("Should be able to set empty string", "", category.getName());
    }

    /**
     * Test set and get category type
     */
    @Test
    public void testSetAndGetType() {
        // Test expense type
        TransactionType expenseType = TransactionType.EXPENSE;
        category.setType(expenseType);
        assertEquals("Should be able to get the set expense type correctly", expenseType, category.getType());

        // Test income type
        TransactionType incomeType = TransactionType.INCOME;
        category.setType(incomeType);
        assertEquals("Should be able to get the set income type correctly", incomeType, category.getType());

        // Test setting null value
        category.setType(null);
        assertNull("Should be able to set null value", category.getType());
    }

    /**
     * Test set and get icon name
     */
    @Test
    public void testSetAndGetIconName() {
        String iconName = "food-icon";
        category.setIconName(iconName);
        assertEquals("Should be able to get the set icon name correctly", iconName, category.getIconName());

        // Test setting null value
        category.setIconName(null);
        assertNull("Should be able to set null value", category.getIconName());
    }

    /**
     * Test set and get color
     */
    @Test
    public void testSetAndGetColor() {
        String color = "#FF5733";
        category.setColor(color);
        assertEquals("Should be able to get the set color correctly", color, category.getColor());

        // Test setting null value
        category.setColor(null);
        assertNull("Should be able to set null value", category.getColor());
    }

    /**
     * Test set and get ID
     */
    @Test
    public void testSetAndGetId() {
        String customId = "custom-category-id";
        category.setId(customId);
        assertEquals("Should be able to get the set ID correctly", customId, category.getId());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        // Test toString when name is not set
        String result = category.toString();
        assertNull("toString should return null when name is not set", result);

        // Test toString after setting name
        String name = "Transportation";
        category.setName(name);
        result = category.toString();
        assertEquals("toString should return category name", name, result);

        // Test toString when name is set to empty string
        category.setName("");
        result = category.toString();
        assertEquals("toString should return empty string", "", result);
    }

    /**
     * Test equals method (based on ID comparison)
     */
    @Test
    public void testEquals() {
        // Create two categories with same ID
        Category category1 = new Category();
        Category category2 = new Category();
        category2.setId(category1.getId());

        // Set different other attributes
        category1.setName("Category1");
        category1.setType(TransactionType.EXPENSE);
        category2.setName("Category2");
        category2.setType(TransactionType.INCOME);

        // Since Category class doesn't override equals method, it uses default object reference comparison
        // Testing default behavior here
        assertFalse("Different object references should not be equal", category1.equals(category2));
        assertTrue("Same object reference should be equal", category1.equals(category1));
        assertFalse("Comparison with null should not be equal", category1.equals(null));
    }

    /**
     * Test complete category object creation and setup
     */
    @Test
    public void testCompleteCategory() {
        // Create a complete category object
        String name = "Entertainment";
        TransactionType type = TransactionType.EXPENSE;
        String iconName = "entertainment-icon";
        String color = "#9B59B6";

        category.setName(name);
        category.setType(type);
        category.setIconName(iconName);
        category.setColor(color);

        // Verify all attributes are set correctly
        assertNotNull("ID should not be null", category.getId());
        assertEquals("Name should be correct", name, category.getName());
        assertEquals("Type should be correct", type, category.getType());
        assertEquals("Icon name should be correct", iconName, category.getIconName());
        assertEquals("Color should be correct", color, category.getColor());
        assertEquals("toString should return name", name, category.toString());
    }

    /**
     * Test validity of transaction types
     */
    @Test
    public void testValidTransactionTypes() {
        // Test all valid transaction types
        TransactionType[] validTypes = TransactionType.values();

        for (TransactionType type : validTypes) {
            category.setType(type);
            assertEquals("Should be able to set transaction type correctly: " + type, type, category.getType());
        }

        // Verify number of transaction types
        assertEquals("Should have 2 transaction types", 2, validTypes.length);

        // Verify specific transaction types
        boolean hasIncome = false;
        boolean hasExpense = false;

        for (TransactionType type : validTypes) {
            if (type == TransactionType.INCOME) {
                hasIncome = true;
            } else if (type == TransactionType.EXPENSE) {
                hasExpense = true;
            }
        }

        assertTrue("Should include income type", hasIncome);
        assertTrue("Should include expense type", hasExpense);
    }

    /**
     * Test category name boundary conditions
     */
    @Test
    public void testCategoryNameBoundaryConditions() {
        // Test very long name
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("a");
        }

        category.setName(longName.toString());
        assertEquals("Should be able to handle long name", longName.toString(), category.getName());

        // Test name with special characters
        String specialName = "Category@#$%^&*()[]{}";
        category.setName(specialName);
        assertEquals("Should be able to handle special characters", specialName, category.getName());

        // Test name with spaces
        String nameWithSpaces = "  Category Name  ";
        category.setName(nameWithSpaces);
        assertEquals("Should preserve spaces", nameWithSpaces, category.getName());
    }
}
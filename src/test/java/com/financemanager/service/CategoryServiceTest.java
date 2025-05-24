package com.financemanager.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * CategoryService unit tests
 * Demonstrates how to use Mockito for test isolation
 */
public class CategoryServiceTest {

    @Before
    public void setUp() throws Exception {
        // Reset singleton instance
        resetSingleton(CategoryService.class, "instance");
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test if getInstance method returns singleton instance
     */
    @Test
    public void testGetInstance() {
        CategoryService instance1 = CategoryService.getInstance();
        CategoryService instance2 = CategoryService.getInstance();

        // Verify that the same instance is returned
        assertNotNull("getInstance should return non-null instance", instance1);
        assertSame("getInstance should always return the same instance", instance1, instance2);
    }

    /**
     * Test initializeDefaultCategories method behavior
     * Since CategoryService directly creates CategoryController instance within the method,
     * we cannot use standard Mockito methods to mock it.
     * Therefore, this test mainly verifies that the method doesn't throw exceptions.
     */
    @Test
    public void testInitializeDefaultCategories() {
        // Get CategoryService instance
        CategoryService categoryService = CategoryService.getInstance();

        // Execute test - verify method doesn't throw exceptions
        try {
            categoryService.initializeDefaultCategories();
            // If no exception, test passes
            assertTrue(true);
        } catch (Exception e) {
            fail("initializeDefaultCategories method should not throw exceptions: " + e.getMessage());
        }
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
}
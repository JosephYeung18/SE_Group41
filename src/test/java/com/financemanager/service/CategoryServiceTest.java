package com.financemanager.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * CategoryService的单元测试
 * 展示如何使用Mockito进行测试隔离
 */
public class CategoryServiceTest {
    
    @Before
    public void setUp() throws Exception {
        // 重置单例实例
        resetSingleton(CategoryService.class, "instance");
        MockitoAnnotations.initMocks(this);
    }
    
    /**
     * 测试getInstance方法是否返回单例实例
     */
    @Test
    public void testGetInstance() {
        CategoryService instance1 = CategoryService.getInstance();
        CategoryService instance2 = CategoryService.getInstance();
        
        // 验证返回的是同一个实例
        assertNotNull("getInstance应该返回非空实例", instance1);
        assertSame("getInstance应该总是返回相同的实例", instance1, instance2);
    }
    
    /**
     * 测试initializeDefaultCategories方法的行为
     * 由于CategoryService直接在方法内部创建了CategoryController实例，
     * 我们无法使用标准的Mockito方法来模拟它。
     * 因此，这个测试主要验证方法不会抛出异常。
     */
    @Test
    public void testInitializeDefaultCategories() {
        // 获取CategoryService实例
        CategoryService categoryService = CategoryService.getInstance();
        
        // 执行测试 - 验证方法不会抛出异常
        try {
            categoryService.initializeDefaultCategories();
            // 如果没有异常，测试通过
            assertTrue(true);
        } catch (Exception e) {
            fail("initializeDefaultCategories方法不应抛出异常: " + e.getMessage());
        }
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
}

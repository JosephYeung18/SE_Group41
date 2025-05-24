package com.financemanager.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Category类的单元测试
 * 测试分类模型的基本功能
 */
public class CategoryTest {
    
    private Category category;
    
    @Before
    public void setUp() {
        // 在每个测试方法执行前初始化测试对象
        category = new Category();
    }
    
    /**
     * 测试默认构造函数
     */
    @Test
    public void testDefaultConstructor() {
        // 测试新创建的Category对象应该有一个非空的ID
        assertNotNull("新分类应该有ID", category.getId());
        assertFalse("分类ID不应为空", category.getId().isEmpty());
        
        // 测试其他字段的默认值
        assertNull("分类名称初始应为null", category.getName());
        assertNull("分类类型初始应为null", category.getType());
        assertNull("图标名称初始应为null", category.getIconName());
        assertNull("颜色初始应为null", category.getColor());
    }
    
    /**
     * 测试带参数的构造函数
     */
    @Test
    public void testParameterizedConstructor() {
        // 使用带参数的构造函数创建分类
        String name = "Food & Dining";
        TransactionType type = TransactionType.EXPENSE;
        Category paramCategory = new Category(name, type);
        
        // 验证结果
        assertNotNull("分类应该有ID", paramCategory.getId());
        assertFalse("分类ID不应为空", paramCategory.getId().isEmpty());
        assertEquals("分类名称应该正确设置", name, paramCategory.getName());
        assertEquals("分类类型应该正确设置", type, paramCategory.getType());
    }
    
    /**
     * 测试设置和获取分类名称
     */
    @Test
    public void testSetAndGetName() {
        String name = "购物";
        category.setName(name);
        assertEquals("应该能正确获取设置的分类名称", name, category.getName());
        
        // 测试设置null值
        category.setName(null);
        assertNull("应该能设置null值", category.getName());
        
        // 测试设置空字符串
        category.setName("");
        assertEquals("应该能设置空字符串", "", category.getName());
    }
    
    /**
     * 测试设置和获取分类类型
     */
    @Test
    public void testSetAndGetType() {
        // 测试支出类型
        TransactionType expenseType = TransactionType.EXPENSE;
        category.setType(expenseType);
        assertEquals("应该能正确获取设置的支出类型", expenseType, category.getType());
        
        // 测试收入类型
        TransactionType incomeType = TransactionType.INCOME;
        category.setType(incomeType);
        assertEquals("应该能正确获取设置的收入类型", incomeType, category.getType());
        
        // 测试设置null值
        category.setType(null);
        assertNull("应该能设置null值", category.getType());
    }
    
    /**
     * 测试设置和获取图标名称
     */
    @Test
    public void testSetAndGetIconName() {
        String iconName = "food-icon";
        category.setIconName(iconName);
        assertEquals("应该能正确获取设置的图标名称", iconName, category.getIconName());
        
        // 测试设置null值
        category.setIconName(null);
        assertNull("应该能设置null值", category.getIconName());
    }
    
    /**
     * 测试设置和获取颜色
     */
    @Test
    public void testSetAndGetColor() {
        String color = "#FF5733";
        category.setColor(color);
        assertEquals("应该能正确获取设置的颜色", color, category.getColor());
        
        // 测试设置null值
        category.setColor(null);
        assertNull("应该能设置null值", category.getColor());
    }
    
    /**
     * 测试设置和获取ID
     */
    @Test
    public void testSetAndGetId() {
        String customId = "custom-category-id";
        category.setId(customId);
        assertEquals("应该能正确获取设置的ID", customId, category.getId());
    }
    
    /**
     * 测试toString方法
     */
    @Test
    public void testToString() {
        // 测试未设置名称时的toString
        String result = category.toString();
        assertNull("未设置名称时toString应返回null", result);
        
        // 测试设置名称后的toString
        String name = "交通";
        category.setName(name);
        result = category.toString();
        assertEquals("toString应该返回分类名称", name, result);
        
        // 测试设置空字符串名称时的toString
        category.setName("");
        result = category.toString();
        assertEquals("toString应该返回空字符串", "", result);
    }
    
    /**
     * 测试equals方法（基于ID比较）
     */
    @Test
    public void testEquals() {
        // 创建两个具有相同ID的分类
        Category category1 = new Category();
        Category category2 = new Category();
        category2.setId(category1.getId());
        
        // 设置不同的其他属性
        category1.setName("分类1");
        category1.setType(TransactionType.EXPENSE);
        category2.setName("分类2");
        category2.setType(TransactionType.INCOME);
        
        // 由于Category类没有重写equals方法，默认使用对象引用比较
        // 这里测试的是默认行为
        assertFalse("不同对象引用应该不相等", category1.equals(category2));
        assertTrue("同一对象引用应该相等", category1.equals(category1));
        assertFalse("与null比较应该不相等", category1.equals(null));
    }
    
    /**
     * 测试完整的分类对象创建和设置
     */
    @Test
    public void testCompleteCategory() {
        // 创建一个完整的分类对象
        String name = "娱乐";
        TransactionType type = TransactionType.EXPENSE;
        String iconName = "entertainment-icon";
        String color = "#9B59B6";
        
        category.setName(name);
        category.setType(type);
        category.setIconName(iconName);
        category.setColor(color);
        
        // 验证所有属性都正确设置
        assertNotNull("ID不应为null", category.getId());
        assertEquals("名称应该正确", name, category.getName());
        assertEquals("类型应该正确", type, category.getType());
        assertEquals("图标名称应该正确", iconName, category.getIconName());
        assertEquals("颜色应该正确", color, category.getColor());
        assertEquals("toString应该返回名称", name, category.toString());
    }
    
    /**
     * 测试分类类型的有效性
     */
    @Test
    public void testValidTransactionTypes() {
        // 测试所有有效的交易类型
        TransactionType[] validTypes = TransactionType.values();
        
        for (TransactionType type : validTypes) {
            category.setType(type);
            assertEquals("应该能正确设置交易类型: " + type, type, category.getType());
        }
        
        // 验证交易类型的数量
        assertEquals("应该有2种交易类型", 2, validTypes.length);
        
        // 验证具体的交易类型
        boolean hasIncome = false;
        boolean hasExpense = false;
        
        for (TransactionType type : validTypes) {
            if (type == TransactionType.INCOME) {
                hasIncome = true;
            } else if (type == TransactionType.EXPENSE) {
                hasExpense = true;
            }
        }
        
        assertTrue("应该包含收入类型", hasIncome);
        assertTrue("应该包含支出类型", hasExpense);
    }
    
    /**
     * 测试分类名称的边界情况
     */
    @Test
    public void testCategoryNameBoundaryConditions() {
        // 测试非常长的名称
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("a");
        }
        
        category.setName(longName.toString());
        assertEquals("应该能处理长名称", longName.toString(), category.getName());
        
        // 测试包含特殊字符的名称
        String specialName = "分类@#$%^&*()[]{}";
        category.setName(specialName);
        assertEquals("应该能处理特殊字符", specialName, category.getName());
        
        // 测试包含空格的名称
        String nameWithSpaces = "  分类名称  ";
        category.setName(nameWithSpaces);
        assertEquals("应该保留空格", nameWithSpaces, category.getName());
    }
}
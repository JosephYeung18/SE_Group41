package com.financemanager.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

/**
 * Transaction类的单元测试
 * 
 */
public class TransactionTest {
    
    private Transaction transaction;
    private Category category;
    
    @Before
    public void setUp() {
        // 在每个测试方法执行前初始化测试对象
        transaction = new Transaction();
        category = new Category();
        category.setName("餐饮");
    }
    
    @Test
    public void testNewTransactionHasId() {
        // 测试新创建的Transaction对象应该有一个非空的ID
        assertNotNull("新交易应该有ID", transaction.getId());
        assertFalse("交易ID不应为空", transaction.getId().isEmpty());
    }
    
    @Test
    public void testNewTransactionHasCurrentDate() {
        // 测试新创建的Transaction对象应该有当前日期
        assertNotNull("新交易应该有日期", transaction.getDate());
        
        // 检查日期是否接近当前时间（允许1秒的误差）
        long currentTime = System.currentTimeMillis();
        long transactionTime = transaction.getDate().getTime();
        assertTrue("交易日期应该是当前日期", 
                Math.abs(currentTime - transactionTime) < 1000);
    }
    
    @Test
    public void testSetAndGetCategory() {
        // 测试设置和获取Category
        transaction.setCategory(category);
        assertEquals("应该能正确获取设置的类别", category, transaction.getCategory());
    }
    
    @Test
    public void testSetAndGetAmount() {
        // 测试设置和获取金额
        double amount = 99.99;
        transaction.setAmount(amount);
        assertEquals("应该能正确获取设置的金额", amount, transaction.getAmount(), 0.001);
    }
    
    @Test
    public void testSetAndGetComment() {
        // 测试设置和获取备注
        String comment = "午餐费用";
        transaction.setComment(comment);
        assertEquals("应该能正确获取设置的备注", comment, transaction.getComment());
    }
    
    @Test
    public void testSetAndGetType() {
        // 测试设置和获取交易类型
        TransactionType type = TransactionType.EXPENSE;
        transaction.setType(type);
        assertEquals("应该能正确获取设置的交易类型", type, transaction.getType());
    }
    
    @Test
    public void testToString() {
        // 测试toString方法包含所有必要的信息
        transaction.setCategory(category);
        transaction.setAmount(50.0);
        transaction.setComment("测试备注");
        transaction.setType(TransactionType.EXPENSE);
        
        String toString = transaction.toString();
        assertTrue("toString应包含ID", toString.contains(transaction.getId()));
        assertTrue("toString应包含类别名称", toString.contains(category.getName()));
        assertTrue("toString应包含金额", toString.contains("50.0"));
        assertTrue("toString应包含备注", toString.contains("测试备注"));
        assertTrue("toString应包含交易类型", toString.contains(TransactionType.EXPENSE.toString()));
    }
    

    
    @Test
    public void testIsExpense() {
        // 测试判断交易是否为支出的方法
        transaction.setType(TransactionType.EXPENSE);
        assertTrue("EXPENSE类型应该被识别为支出", transaction.isExpense());
        
        transaction.setType(TransactionType.INCOME);
        assertFalse("INCOME类型不应该被识别为支出", transaction.isExpense());
    }
    
    @Test
    public void testIsIncome() {
        // 测试判断交易是否为收入的方法
        transaction.setType(TransactionType.INCOME);
        assertTrue("INCOME类型应该被识别为收入", transaction.isIncome());
        
        transaction.setType(TransactionType.EXPENSE);
        assertFalse("EXPENSE类型不应该被识别为收入", transaction.isIncome());
    }
    
    @Test
    public void testGetSignedAmount() {
        // 测试获取带符号的金额（支出为负，收入为正）
        double amount = 100.0;
        transaction.setAmount(amount);
        
        transaction.setType(TransactionType.INCOME);
        assertEquals("收入的带符号金额应为正", amount, transaction.getSignedAmount(), 0.001);
        
        transaction.setType(TransactionType.EXPENSE);
        assertEquals("支出的带符号金额应为负", -amount, transaction.getSignedAmount(), 0.001);
    }
}

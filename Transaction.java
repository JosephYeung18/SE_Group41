package com.financemanager.model;

import java.util.Date;
import java.util.UUID;

/**
 * 表示用户的一笔交易
 */
public class Transaction {
    private String id;
    private Category category;
    private double amount;
    private Date date;
    private String comment;
    private TransactionType type;
    
    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    /**
     * 判断交易是否为支出
     * @return 如果是支出返回true，否则返回false
     */
    public boolean isExpense() {
        return type == TransactionType.EXPENSE;
    }
    
    /**
     * 判断交易是否为收入
     * @return 如果是收入返回true，否则返回false
     */
    public boolean isIncome() {
        return type == TransactionType.INCOME;
    }
    
    /**
     * 获取带符号的金额，支出为负，收入为正
     * @return 带符号的金额
     */
    public double getSignedAmount() {
        return isExpense() ? -amount : amount;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", category=" + (category != null ? category.getName() : "null") +
                ", amount=" + amount +
                ", date=" + date +
                ", comment='" + comment + '\'' +
                ", type=" + type +
                '}';
    }
}
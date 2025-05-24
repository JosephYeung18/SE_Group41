package com.financemanager.model;

import java.util.UUID;

/**
 * 表示用户的预算
 */
public class Budget {
    private String id;
    private Category category;
    private double amount;
    private double spent;
    private int year;
    private int month;
    
    public Budget() {
        this.id = UUID.randomUUID().toString();
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
    
    public double getSpent() {
        return spent;
    }
    
    public void setSpent(double spent) {
        this.spent = spent;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    @Override
    public String toString() {
        return "Budget{" +
                "id='" + id + '\'' +
                ", category=" + (category != null ? category.getName() : "null") +
                ", amount=" + amount +
                ", spent=" + spent +
                ", year=" + year +
                ", month=" + month +
                '}';
    }
}
package com.financemanager.model;

import java.util.UUID;

/**
 * 表示交易的分类
 */
public class Category {
    private String id;
    private String name;
    private TransactionType type;
    private String iconName;
    private String color;
    
    public Category() {
        this.id = UUID.randomUUID().toString();
    }
    
    public Category(String name, TransactionType type) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.type = type;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public String getIconName() {
        return iconName;
    }
    
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
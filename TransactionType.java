package com.financemanager.model;

/**
 * 交易类型枚举
 */
public enum TransactionType {
    INCOME("收入"),
    EXPENSE("支出");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

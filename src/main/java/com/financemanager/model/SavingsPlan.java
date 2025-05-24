package com.financemanager.model;

import java.util.Date;
import java.util.UUID;

/**
 * 表示用户的储蓄计划
 */
public class SavingsPlan {
    private String id;
    private String name;
    private double targetAmount;
    private double depositedAmount;
    private Date startDate;
    private Date endDate;

    public SavingsPlan() {
        this.id = UUID.randomUUID().toString();
        this.startDate = new Date();
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

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getDepositedAmount() {
        return depositedAmount;
    }

    public void setDepositedAmount(double depositedAmount) {
        this.depositedAmount = depositedAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 计算完成百分比
     */
    public int getCompletionPercentage() {
        if (targetAmount <= 0) {
            return 0;
        }
        return (int) ((depositedAmount / targetAmount) * 100);
    }

    /**
     * 判断计划是否已完成
     */
    public boolean isCompleted() {
        return depositedAmount >= targetAmount;
    }

    /**
     * 存款操作
     */
    public void deposit(double amount) {
        if (amount > 0) {
            this.depositedAmount += amount;
        }
    }

    /**
     * 取款操作
     */
    public void withdraw(double amount) {
        if (amount > 0 && amount <= depositedAmount) {
            this.depositedAmount -= amount;
        }
    }

    @Override
    public String toString() {
        return name + " - ¥" + targetAmount;
    }
}
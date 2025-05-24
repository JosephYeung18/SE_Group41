package com.financemanager.controller;

import com.financemanager.model.SavingsPlan;
import com.financemanager.service.DataPersistenceService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 储蓄计划控制器，负责管理储蓄计划数据
 */
public class SavingsPlanController {

    private List<SavingsPlan> savingsPlans;
    private DataPersistenceService dataPersistenceService;

    public SavingsPlanController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        try {
            this.savingsPlans = dataPersistenceService.loadSavingsPlans();
        } catch (Exception e) {
            System.err.println("加载储蓄计划失败: " + e.getMessage());
            this.savingsPlans = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "无法加载储蓄计划数据，请检查文件权限或格式。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 添加储蓄计划
     */
    public void addPlan(SavingsPlan plan) {
        savingsPlans.add(plan);
        try {
            dataPersistenceService.saveSavingsPlans(savingsPlans);
        } catch (Exception e) {
            System.err.println("保存储蓄计划失败: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "无法保存储蓄计划数据，请检查文件权限。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 更新储蓄计划
     */
    public void updatePlan(SavingsPlan plan) {
        for (int i = 0; i < savingsPlans.size(); i++) {
            if (savingsPlans.get(i).getId().equals(plan.getId())) {
                savingsPlans.set(i, plan);
                break;
            }
        }
        try {
            dataPersistenceService.saveSavingsPlans(savingsPlans);
        } catch (Exception e) {
            System.err.println("保存储蓄计划失败: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "无法保存储蓄计划数据，请检查文件权限。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除储蓄计划
     */
    public void deletePlan(String planId) {
        savingsPlans.removeIf(plan -> plan.getId().equals(planId));
        try {
            dataPersistenceService.saveSavingsPlans(savingsPlans);
        } catch (Exception e) {
            System.err.println("保存储蓄计划失败: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "无法保存储蓄计划数据，请检查文件权限。", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 获取所有储蓄计划
     */
    public List<SavingsPlan> getAllPlans() {
        return new ArrayList<>(savingsPlans);
    }

    /**
     * 通过ID获取储蓄计划
     */
    public SavingsPlan getPlanById(String planId) {
        for (SavingsPlan plan : savingsPlans) {
            if (plan.getId().equals(planId)) {
                return plan;
            }
        }
        return null;
    }

    /**
     * 执行存款操作
     */
    public void deposit(String planId, double amount, String comment) {
        SavingsPlan plan = getPlanById(planId);
        if (plan != null) {
            plan.deposit(amount);
            updatePlan(plan);
        }
    }

    /**
     * 执行取款操作
     */
    public void withdraw(String planId, double amount, String comment) {
        SavingsPlan plan = getPlanById(planId);
        if (plan != null) {
            plan.withdraw(amount);
            updatePlan(plan);
        }
    }

    /**
     * 获取已完成的储蓄计划
     */
    public List<SavingsPlan> getCompletedPlans() {
        List<SavingsPlan> completedPlans = new ArrayList<>();
        for (SavingsPlan plan : savingsPlans) {
            if (plan.isCompleted()) {
                completedPlans.add(plan);
            }
        }
        return completedPlans;
    }

    /**
     * 获取进行中的储蓄计划
     */
    public List<SavingsPlan> getActivePlans() {
        List<SavingsPlan> activePlans = new ArrayList<>();
        for (SavingsPlan plan : savingsPlans) {
            if (!plan.isCompleted()) {
                activePlans.add(plan);
            }
        }
        return activePlans;
    }
}
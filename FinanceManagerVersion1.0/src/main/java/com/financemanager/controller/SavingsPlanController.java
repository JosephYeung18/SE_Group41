package com.financemanager.controller;

import com.financemanager.model.SavingsPlan;
import com.financemanager.service.DataPersistenceService;

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
        this.savingsPlans = new ArrayList<>(); // 暂时使用空列表，实际项目中需要从持久化服务加载
    }
    
    /**
     * 添加储蓄计划
     */
    public void addPlan(SavingsPlan plan) {
        savingsPlans.add(plan);
        // 实际项目中应保存到持久化服务
        // dataPersistenceService.saveSavingsPlans(savingsPlans);
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
        
        // 实际项目中应保存到持久化服务
        // dataPersistenceService.saveSavingsPlans(savingsPlans);
    }
    
    /**
     * 删除储蓄计划
     */
    public void deletePlan(String planId) {
        savingsPlans.removeIf(plan -> plan.getId().equals(planId));
        
        // 实际项目中应保存到持久化服务
        // dataPersistenceService.saveSavingsPlans(savingsPlans);
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
            
            // 实际项目中，可以记录存款操作到交易历史
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
            
            // 实际项目中，可以记录取款操作到交易历史
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
package com.financemanager.controller;

import com.financemanager.model.Account;
import com.financemanager.service.DataPersistenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户控制器，负责管理账户数据
 */
public class AccountController {
    
    private List<Account> accounts;
    private DataPersistenceService dataPersistenceService;
    
    public AccountController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        this.accounts = dataPersistenceService.loadAccounts();
    }
    
    /**
     * 添加账户
     */
    public void addAccount(Account account) {
        accounts.add(account);
        dataPersistenceService.saveAccounts(accounts);
    }
    
    /**
     * 更新账户
     */
    public void updateAccount(Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(account.getId())) {
                accounts.set(i, account);
                break;
            }
        }
        
        dataPersistenceService.saveAccounts(accounts);
    }
    
    /**
     * 删除账户
     */
    public void deleteAccount(String accountId) {
        accounts.removeIf(account -> account.getId().equals(accountId));
        dataPersistenceService.saveAccounts(accounts);
    }
    
    /**
     * 获取所有账户
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }
    
    /**
     * 通过ID获取账户
     */
    public Account getAccountById(String accountId) {
        for (Account account : accounts) {
            if (account.getId().equals(accountId)) {
                return account;
            }
        }
        
        return null;
    }
    
    /**
     * 计算总资产
     */
    public double calculateTotalAssets() {
        double total = 0;
        
        for (Account account : accounts) {
            if (account.getBalance() > 0) {
                total += account.getBalance();
            }
        }
        
        return total;
    }
    
    /**
     * 计算总负债
     */
    public double calculateTotalLiabilities() {
        double total = 0;
        
        for (Account account : accounts) {
            if (account.getBalance() < 0) {
                total += Math.abs(account.getBalance());
            }
        }
        
        return total;
    }
    
    /**
     * 计算净资产
     */
    public double calculateNetWorth() {
        return calculateTotalAssets() - calculateTotalLiabilities();
    }
}
package com.financemanager.controller;

import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类控制器，负责管理分类数据
 */
public class CategoryController {
    
    private List<Category> categories;
    private DataPersistenceService dataPersistenceService;
    
    public CategoryController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        this.categories = dataPersistenceService.loadCategories();
        
        // 如果没有分类，则初始化默认分类
        if (categories.isEmpty()) {
            initializeDefaultCategories();
        }
    }
    
    /**
     * 添加分类
     */
    public void addCategory(Category category) {
        categories.add(category);
        dataPersistenceService.saveCategories(categories);
    }
    
    /**
     * 更新分类
     */
    public void updateCategory(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(category.getId())) {
                categories.set(i, category);
                break;
            }
        }
        
        dataPersistenceService.saveCategories(categories);
    }
    
    /**
     * 删除分类
     */
    public void deleteCategory(String categoryId) {
        categories.removeIf(category -> category.getId().equals(categoryId));
        dataPersistenceService.saveCategories(categories);
    }
    
    /**
     * 获取所有分类
     */
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }
    
    /**
     * 获取特定类型的分类
     */
    public List<Category> getCategoriesByType(TransactionType type) {
        List<Category> result = new ArrayList<>();
        
        for (Category category : categories) {
            if (category.getType() == type) {
                result.add(category);
            }
        }
        
        return result;
    }
    
    /**
     * 通过名称和类型获取分类
     */
    public Category getCategoryByName(String name, TransactionType type) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name) && category.getType() == type) {
                return category;
            }
        }
        
        return null;
    }
    
    /**
     * 通过名称获取分类（不考虑类型）
     */
    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        
        return null;
    }
    
    /**
     * 通过ID获取分类
     */
    public Category getCategoryById(String categoryId) {
        for (Category category : categories) {
            if (category.getId().equals(categoryId)) {
                return category;
            }
        }
        
        return null;
    }
    
    /**
     * 初始化默认分类
     */
    public void initializeDefaultCategories() {
        // 收入分类
        addCategory(new Category("工资", TransactionType.INCOME));
        addCategory(new Category("奖金", TransactionType.INCOME));
        addCategory(new Category("利息", TransactionType.INCOME));
        addCategory(new Category("投资收益", TransactionType.INCOME));
        addCategory(new Category("其他收入", TransactionType.INCOME));
        
        // 支出分类
        addCategory(new Category("餐饮", TransactionType.EXPENSE));
        addCategory(new Category("购物", TransactionType.EXPENSE));
        addCategory(new Category("交通", TransactionType.EXPENSE));
        addCategory(new Category("住房", TransactionType.EXPENSE));
        addCategory(new Category("娱乐", TransactionType.EXPENSE));
        addCategory(new Category("教育", TransactionType.EXPENSE));
        addCategory(new Category("医疗", TransactionType.EXPENSE));
        addCategory(new Category("其他", TransactionType.EXPENSE));
    }
}
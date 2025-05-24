package com.financemanager.service;

import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import java.util.List;

/**
 * 分类服务，负责处理分类相关的业务逻辑
 */
public class CategoryService {
    
    private static CategoryService instance;
    
    private CategoryService() {
        // 单例模式
    }
    
    public static synchronized CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
        }
        return instance;
    }
    
    /**
     * 初始化默认分类
     */
    public void initializeDefaultCategories() {
        // 获取控制器
        com.financemanager.controller.CategoryController categoryController = 
            new com.financemanager.controller.CategoryController();
        
        // 检查是否已有分类
        List<Category> existingCategories = categoryController.getAllCategories();
        if (!existingCategories.isEmpty()) {
            return; // 已经有分类，不需要初始化
        }
        
        // 初始化默认分类
        categoryController.initializeDefaultCategories();
    }
}
package com.financemanager.controller;

import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * Category controller, responsible for managing category data
 */
public class CategoryController {

    private List<Category> categories;
    private DataPersistenceService dataPersistenceService;

    public CategoryController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        this.categories = dataPersistenceService.loadCategories();

        // If no categories exist, initialize default categories
        if (categories.isEmpty()) {
            initializeDefaultCategories();
        }
    }

    /**
     * Add category
     */
    public void addCategory(Category category) {
        categories.add(category);
        dataPersistenceService.saveCategories(categories);
    }

    /**
     * Update category
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
     * Delete category
     */
    public void deleteCategory(String categoryId) {
        categories.removeIf(category -> category.getId().equals(categoryId));
        dataPersistenceService.saveCategories(categories);
    }

    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    /**
     * Get categories by specific type
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
     * Get category by name and type
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
     * Get category by name (regardless of type)
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
     * Get category by ID
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
     * Initialize default categories
     */
    public void initializeDefaultCategories() {
        // Income categories
        addCategory(new Category("Salary", TransactionType.INCOME));
        addCategory(new Category("Bonus", TransactionType.INCOME));
        addCategory(new Category("Interest", TransactionType.INCOME));
        addCategory(new Category("Investment Returns", TransactionType.INCOME));
        addCategory(new Category("Other Income", TransactionType.INCOME));

        // Expense categories
        addCategory(new Category("Food & Dining", TransactionType.EXPENSE));
        addCategory(new Category("Shopping", TransactionType.EXPENSE));
        addCategory(new Category("Transportation", TransactionType.EXPENSE));
        addCategory(new Category("Housing", TransactionType.EXPENSE));
        addCategory(new Category("Entertainment", TransactionType.EXPENSE));
        addCategory(new Category("Education", TransactionType.EXPENSE));
        addCategory(new Category("Healthcare", TransactionType.EXPENSE));
        addCategory(new Category("Other", TransactionType.EXPENSE));
    }
}
package com.financemanager.service;

import com.financemanager.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * 数据持久化服务，负责保存和加载应用程序数据
 */
public class DataPersistenceService {

    private static DataPersistenceService instance;

    private static final String DATA_DIR = "data";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.json";
    private static final String CATEGORIES_FILE = DATA_DIR + "/categories.json";
    private static final String ACCOUNTS_FILE = DATA_DIR + "/accounts.json";
    private static final String BUDGETS_FILE = DATA_DIR + "/budgets.json";
    private static final String SAVINGS_PLANS_FILE = DATA_DIR + "/savingsplans.json";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private DataPersistenceService() {
        // 单例模式
    }

    public static synchronized DataPersistenceService getInstance() {
        if (instance == null) {
            instance = new DataPersistenceService();
        }
        return instance;
    }

    /**
     * 初始化数据目录
     */
    public void initialize() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // 使用 mkdirs() 确保父目录存在
        }

        // 创建所有必要的数据文件
        initializeFileIfNotExists(TRANSACTIONS_FILE, "[]");
        initializeFileIfNotExists(CATEGORIES_FILE, "[]");
        initializeFileIfNotExists(ACCOUNTS_FILE, "[]");
        initializeFileIfNotExists(BUDGETS_FILE, "[]");
        initializeFileIfNotExists(SAVINGS_PLANS_FILE, "[]");
    }

    /**
     * 初始化数据文件（如果不存在或内容无效）
     */
    private void initializeFileIfNotExists(String filePath, String defaultContent) {
        File file = new File(filePath);
        try {
            // 如果文件不存在或为空，创建并写入默认内容
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(defaultContent);
                }
            } else {
                // 检查文件内容是否有效 JSON 数组
                String content = readFileAsString(filePath);
                if (content.trim().isEmpty() || !content.trim().startsWith("[")) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write(defaultContent);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("初始化文件失败: " + filePath + ", 错误: " + e.getMessage());
        }
    }

    /**
     * 保存储蓄计划列表
     */
    public void saveSavingsPlans(List<SavingsPlan> savingsPlans) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (SavingsPlan plan : savingsPlans) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", plan.getId());
                jsonObject.put("name", plan.getName());
                jsonObject.put("targetAmount", plan.getTargetAmount());
                jsonObject.put("depositedAmount", plan.getDepositedAmount());
                jsonObject.put("startDate", dateFormat.format(plan.getStartDate()));
                jsonObject.put("endDate", dateFormat.format(plan.getEndDate()));

                jsonArray.put(jsonObject);
            }

            saveJsonToFile(SAVINGS_PLANS_FILE, jsonArray);
        } catch (Exception e) {
            System.err.println("保存储蓄计划失败: " + e.getMessage());
        }
    }

    /**
     * 加载储蓄计划列表
     */
    public List<SavingsPlan> loadSavingsPlans() {
        List<SavingsPlan> savingsPlans = new ArrayList<>();

        try {
            String json = readFileAsString(SAVINGS_PLANS_FILE);
            // 如果文件为空或内容无效，返回空列表
            if (json.trim().isEmpty()) {
                return savingsPlans;
            }

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                SavingsPlan plan = new SavingsPlan();
                plan.setId(jsonObject.getString("id"));
                plan.setName(jsonObject.getString("name"));
                plan.setTargetAmount(jsonObject.getDouble("targetAmount"));
                plan.setDepositedAmount(jsonObject.getDouble("depositedAmount"));

                String startDateStr = jsonObject.getString("startDate");
                Date startDate = dateFormat.parse(startDateStr);
                plan.setStartDate(startDate);

                String endDateStr = jsonObject.getString("endDate");
                Date endDate = dateFormat.parse(endDateStr);
                plan.setEndDate(endDate);

                savingsPlans.add(plan);
            }
        } catch (JSONException e) {
            System.err.println("解析储蓄计划 JSON 失败: " + e.getMessage());
            // 文件内容无效，初始化为空 JSON 数组
            initializeFileIfNotExists(SAVINGS_PLANS_FILE, "[]");
        } catch (Exception e) {
            System.err.println("加载储蓄计划失败: " + e.getMessage());
        }

        return savingsPlans;
    }

    /**
     * 保存交易列表
     */
    public void saveTransactions(List<Transaction> transactions) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Transaction transaction : transactions) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", transaction.getId());

                if (transaction.getCategory() != null) {
                    jsonObject.put("categoryId", transaction.getCategory().getId());
                }

                jsonObject.put("amount", transaction.getAmount());
                jsonObject.put("date", dateFormat.format(transaction.getDate()));
                jsonObject.put("comment", transaction.getComment() != null ? transaction.getComment() : "");
                jsonObject.put("type", transaction.getType().name());

                jsonArray.put(jsonObject);
            }

            saveJsonToFile(TRANSACTIONS_FILE, jsonArray);
        } catch (Exception e) {
            System.err.println("保存交易失败: " + e.getMessage());
        }
    }

    /**
     * 加载交易列表
     */
    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try {
            String json = readFileAsString(TRANSACTIONS_FILE);
            if (json.trim().isEmpty()) {
                return transactions;
            }

            JSONArray jsonArray = new JSONArray(json);

            List<Category> allCategories = loadCategories();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Transaction transaction = new Transaction();
                transaction.setId(jsonObject.getString("id"));
                transaction.setAmount(jsonObject.getDouble("amount"));

                String dateStr = jsonObject.getString("date");
                Date date = dateFormat.parse(dateStr);
                transaction.setDate(date);

                transaction.setComment(jsonObject.getString("comment"));
                transaction.setType(TransactionType.valueOf(jsonObject.getString("type")));

                if (jsonObject.has("categoryId")) {
                    String categoryId = jsonObject.getString("categoryId");
                    for (Category category : allCategories) {
                        if (category.getId().equals(categoryId)) {
                            transaction.setCategory(category);
                            break;
                        }
                    }
                }

                transactions.add(transaction);
            }
        } catch (Exception e) {
            System.err.println("加载交易失败: " + e.getMessage());
        }

        return transactions;
    }

    /**
     * 保存分类列表
     */
    public void saveCategories(List<Category> categories) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Category category : categories) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", category.getId());
                jsonObject.put("name", category.getName());
                jsonObject.put("type", category.getType().name());
                jsonObject.put("iconName", category.getIconName() != null ? category.getIconName() : "");
                jsonObject.put("color", category.getColor() != null ? category.getColor() : "");

                jsonArray.put(jsonObject);
            }

            saveJsonToFile(CATEGORIES_FILE, jsonArray);
        } catch (Exception e) {
            System.err.println("保存分类失败: " + e.getMessage());
        }
    }

    /**
     * 加载分类列表
     */
    public List<Category> loadCategories() {
        List<Category> categories = new ArrayList<>();

        try {
            String json = readFileAsString(CATEGORIES_FILE);
            if (json.trim().isEmpty()) {
                return categories;
            }

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Category category = new Category();
                category.setId(jsonObject.getString("id"));
                category.setName(jsonObject.getString("name"));
                category.setType(TransactionType.valueOf(jsonObject.getString("type")));

                if (jsonObject.has("iconName")) {
                    category.setIconName(jsonObject.getString("iconName"));
                }

                if (jsonObject.has("color")) {
                    category.setColor(jsonObject.getString("color"));
                }

                categories.add(category);
            }
        } catch (Exception e) {
            System.err.println("加载分类失败: " + e.getMessage());
        }

        return categories;
    }

    /**
     * 保存账户列表
     */
    public void saveAccounts(List<Account> accounts) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Account account : accounts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", account.getId());
                jsonObject.put("name", account.getName());
                jsonObject.put("balance", account.getBalance());
                jsonObject.put("description", account.getDescription() != null ? account.getDescription() : "");

                jsonArray.put(jsonObject);
            }

            saveJsonToFile(ACCOUNTS_FILE, jsonArray);
        } catch (Exception e) {
            System.err.println("保存账户失败: " + e.getMessage());
        }
    }

    /**
     * 加载账户列表
     */
    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();

        try {
            String json = readFileAsString(ACCOUNTS_FILE);
            if (json.trim().isEmpty()) {
                return accounts;
            }

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Account account = new Account();
                account.setId(jsonObject.getString("id"));
                account.setName(jsonObject.getString("name"));
                account.setBalance(jsonObject.getDouble("balance"));
                account.setDescription(jsonObject.getString("description"));

                accounts.add(account);
            }
        } catch (Exception e) {
            System.err.println("加载账户失败: " + e.getMessage());
        }

        return accounts;
    }

    /**
     * 保存预算列表
     */
    public void saveBudgets(List<Budget> budgets) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (Budget budget : budgets) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", budget.getId());

                if (budget.getCategory() != null) {
                    jsonObject.put("categoryId", budget.getCategory().getId());
                }

                jsonObject.put("amount", budget.getAmount());
                jsonObject.put("spent", budget.getSpent());
                jsonObject.put("year", budget.getYear());
                jsonObject.put("month", budget.getMonth());

                jsonArray.put(jsonObject);
            }

            saveJsonToFile(BUDGETS_FILE, jsonArray);
        } catch (Exception e) {
            System.err.println("保存预算失败: " + e.getMessage());
        }
    }

    /**
     * 加载预算列表
     */
    public List<Budget> loadBudgets() {
        List<Budget> budgets = new ArrayList<>();

        try {
            String json = readFileAsString(BUDGETS_FILE);
            if (json.trim().isEmpty()) {
                return budgets;
            }

            JSONArray jsonArray = new JSONArray(json);

            List<Category> allCategories = loadCategories();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Budget budget = new Budget();
                budget.setId(jsonObject.getString("id"));
                budget.setAmount(jsonObject.getDouble("amount"));
                budget.setSpent(jsonObject.getDouble("spent"));
                budget.setYear(jsonObject.getInt("year"));
                budget.setMonth(jsonObject.getInt("month"));

                if (jsonObject.has("categoryId")) {
                    String categoryId = jsonObject.getString("categoryId");
                    for (Category category : allCategories) {
                        if (category.getId().equals(categoryId)) {
                            budget.setCategory(category);
                            break;
                        }
                    }
                }

                budgets.add(budget);
            }
        } catch (Exception e) {
            System.err.println("加载预算失败: " + e.getMessage());
        }

        return budgets;
    }

    /**
     * 将JSON对象保存到文件
     */
    private void saveJsonToFile(String filePath, JSONArray jsonArray) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(jsonArray.toString(2)); // 格式化JSON以便于阅读
        }
    }

    /**
     * 从文件读取字符串
     */
    private String readFileAsString(String filePath) throws Exception {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        return content.toString();
    }
}
package com.financemanager.controller;

import com.financemanager.model.Transaction;
import com.financemanager.model.Category;
import com.financemanager.model.TransactionType;
import com.financemanager.service.DataPersistenceService;
import com.financemanager.service.AIService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 交易控制器，负责管理交易数据
 */
public class TransactionController {
    
    private List<Transaction> transactions;
    private DataPersistenceService dataPersistenceService;
    private CategoryController categoryController;
    
    // 添加单例实例
    private static TransactionController instance;
    
    // 将构造函数改为私有
    public TransactionController() {
        this.dataPersistenceService = DataPersistenceService.getInstance();
        this.categoryController = new CategoryController();
        this.transactions = dataPersistenceService.loadTransactions();
    }
    
    /**
     * 获取TransactionController的单例实例
     */
    public static synchronized TransactionController getInstance() {
        if (instance == null) {
            instance = new TransactionController();
        }
        return instance;
    }
    
    /**
     * 添加交易
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        dataPersistenceService.saveTransactions(transactions);
        updateBudgetSpending(transaction);
    }
    
    /**
     * 更新交易
     */
    public void updateTransaction(Transaction transaction) {
        // 找到并更新现有交易
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId().equals(transaction.getId())) {
                // 先更新预算
                removeFromBudget(transactions.get(i));
                transactions.set(i, transaction);
                updateBudgetSpending(transaction);
                break;
            }
        }
        
        dataPersistenceService.saveTransactions(transactions);
    }
    
    /**
     * 删除交易
     */
    public void deleteTransaction(String transactionId) {
        Transaction toRemove = null;
        
        // 找到要删除的交易
        for (Transaction transaction : transactions) {
            if (transaction.getId().equals(transactionId)) {
                toRemove = transaction;
                break;
            }
        }
        
        if (toRemove != null) {
            // 从预算中移除支出
            removeFromBudget(toRemove);
            
            // 从列表中移除交易
            transactions.remove(toRemove);
            dataPersistenceService.saveTransactions(transactions);
        }
    }
    
    /**
     * 获取所有交易
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    /**
     * 获取按日期排序的交易（最新的在前）
     */
    public List<Transaction> getTransactionsSortedByDate() {
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        
        Collections.sort(sortedTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate()); // 降序排列
            }
        });
        
        return sortedTransactions;
    }
    
    /**
     * 获取特定类型的交易
     */
    public List<Transaction> getTransactionsByType(TransactionType type) {
        List<Transaction> result = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            if (transaction.getType() == type) {
                result.add(transaction);
            }
        }
        
        return result;
    }
    
    /**
     * 获取特定分类的交易
     */
    public List<Transaction> getTransactionsByCategory(Category category) {
        List<Transaction> result = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            if (transaction.getCategory() != null && 
                transaction.getCategory().getId().equals(category.getId())) {
                result.add(transaction);
            }
        }
        
        return result;
    }
    
    /**
     * 获取特定日期范围内的交易
     */
    public List<Transaction> getTransactionsByDateRange(Date startDate, Date endDate) {
        List<Transaction> result = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            Date date = transaction.getDate();
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                result.add(transaction);
            }
        }
        
        return result;
    }
    
    /**
     * 获取特定月份的交易
     */
    public List<Transaction> getTransactionsForMonth(int year, int month) {
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month - 1, 1, 0, 0, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = startCal.getTime();
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month - 1, endCal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = endCal.getTime();
        
        return getTransactionsByDateRange(startDate, endDate);
    }
    
    /**
     * 获取最近N天的交易
     */
    public List<Transaction> getRecentTransactions(int days) {
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = endCal.getTime();
        
        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.DAY_OF_MONTH, -days);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = startCal.getTime();
        
        return getTransactionsByDateRange(startDate, endDate);
    }

    /**
     * 从CSV文件导入交易
     */
    public int importTransactionsFromCSV(String filePath) throws Exception {
        List<Transaction> importedTransactions = new ArrayList<>();
        int lineCount = 0;

        // 创建AI服务实例，用于自动分类
        AIService aiService = new AIService();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            // CSV文件格式检测
            boolean hasCategory = false;
            int expectedColumns = 0;

            while ((line = reader.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 跳过表头行
                if (isFirstLine) {
                    isFirstLine = false;

                    // 检测CSV文件格式
                    String[] headers = line.split(",");
                    expectedColumns = headers.length;

                    // 判断是否包含分类列
                    for (String header : headers) {
                        String headerTrim = header.trim().toLowerCase();
                        if (headerTrim.equals("category") || headerTrim.equals("分类")) {
                            hasCategory = true;
                            break;
                        }
                    }

                    continue;
                }

                try {
                    String[] fields = line.split(",", -1); // 使用-1参数保留空字段

                    // 检查字段数量 - 允许最少3个字段
                    if (fields.length < 3) {
                        System.err.println("跳过格式不正确的行（字段不足）: " + line);
                        continue;
                    }

                    // 解析日期 - 使用增强的日期解析方法
                    String dateStr = fields[0].trim();
                    if (dateStr.isEmpty()) {
                        System.err.println("跳过日期为空的行: " + line);
                        continue;
                    }

                    Date date;
                    try {
                        date = parseFlexibleDate(dateStr); // 使用新的日期解析方法
                    } catch (ParseException e) {
                        System.err.println("跳过日期格式不正确的行 [" + dateStr + "]: " + e.getMessage());
                        continue;
                    }

                    // 解析交易类型
                    String typeStr = fields[1].trim();
                    if (typeStr.isEmpty()) {
                        System.err.println("跳过交易类型为空的行: " + line);
                        continue;
                    }

                    TransactionType type;
                    if (typeStr.equalsIgnoreCase("income") || typeStr.equalsIgnoreCase("收入")) {
                        type = TransactionType.INCOME;
                    } else if (typeStr.equalsIgnoreCase("expense") || typeStr.equalsIgnoreCase("支出")) {
                        type = TransactionType.EXPENSE;
                    } else {
                        System.err.println("跳过未知交易类型的行: " + line);
                        continue;
                    }

                    // 初始化变量
                    Category category = null;
                    String comment = ""; // 默认为空字符串
                    double amount = 0.0;

                    if (hasCategory) {
                        // 传统格式: 日期,类型,分类,金额[,备注]
                        if (fields.length < 4) {
                            System.err.println("跳过字段不足的行（需要至少4个字段）: " + line);
                            continue;
                        }

                        String categoryName = fields[2].trim();
                        if (categoryName.isEmpty()) {
                            System.err.println("跳过分类为空的行: " + line);
                            continue;
                        }

                        // 处理分类名称中的特殊字符
                        categoryName = categoryName.replace("&", " & "); // Food&Dining -> Food & Dining

                        category = categoryController.getCategoryByName(categoryName, type);

                        // 如果分类不存在，创建新分类
                        if (category == null) {
                            category = new Category(categoryName, type);
                            categoryController.addCategory(category);
                        }

                        // 解析金额
                        String amountStr = fields[3].trim();
                        if (amountStr.isEmpty()) {
                            System.err.println("跳过金额为空的行: " + line);
                            continue;
                        }
                        amount = Double.parseDouble(amountStr);

                        // 如果有备注列且不为空
                        if (fields.length > 4 && fields[4] != null) {
                            comment = fields[4].trim();
                        }
                    } else {
                        // 新格式: 日期,类型,金额,备注
                        if (fields.length < 3) {
                            System.err.println("跳过字段不足的行（需要至少3个字段）: " + line);
                            continue;
                        }

                        // 解析金额
                        String amountStr = fields[2].trim();
                        if (amountStr.isEmpty()) {
                            System.err.println("跳过金额为空的行: " + line);
                            continue;
                        }
                        amount = Double.parseDouble(amountStr);

                        // 获取备注（如果存在）
                        if (fields.length > 3 && fields[3] != null) {
                            comment = fields[3].trim();
                        }

                        // 使用AI服务根据备注自动分类（如果备注不为空）
                        if (!comment.isEmpty()) {
                            try {
                                String categoryId = aiService.classifyTransaction(comment, type);
                                category = categoryController.getCategoryById(categoryId);
                            } catch (Exception e) {
                                System.err.println("AI分类失败，使用默认分类: " + e.getMessage());
                            }
                        }

                        // 如果AI分类失败或备注为空，使用默认分类
                        if (category == null) {
                            category = getDefaultCategory(type);
                        }
                    }

                    // 验证金额
                    if (amount <= 0) {
                        System.err.println("跳过金额不正确的行（金额必须大于0）: " + line);
                        continue;
                    }

                    // 验证分类
                    if (category == null) {
                        System.err.println("跳过无法确定分类的行: " + line);
                        continue;
                    }

                    // 创建交易对象
                    Transaction transaction = new Transaction();
                    transaction.setDate(date);
                    transaction.setType(type);
                    transaction.setCategory(category);
                    transaction.setAmount(amount);
                    transaction.setComment(comment);

                    importedTransactions.add(transaction);
                    lineCount++;

                } catch (NumberFormatException e) {
                    // 跳过处理不正确的行，但记录错误信息
                    System.err.println("导入行错误 [" + line + "]: 金额格式不正确 - " + e.getMessage());
                } catch (Exception e) {
                    // 跳过处理不正确的行，但记录错误信息
                    System.err.println("导入行错误 [" + line + "]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        // 添加所有成功导入的交易
        for (Transaction transaction : importedTransactions) {
            addTransaction(transaction);
        }

        return lineCount;
    }

    /**
     * 获取默认分类
     * @param type 交易类型
     * @return 默认分类
     */
    private Category getDefaultCategory(TransactionType type) {
        Category defaultCategory = null;

        if (type == TransactionType.INCOME) {
            // 尝试获取"其他收入"分类
            defaultCategory = categoryController.getCategoryByName("其他收入", TransactionType.INCOME);
            if (defaultCategory == null) {
                // 如果不存在，创建一个
                defaultCategory = new Category("其他收入", TransactionType.INCOME);
                categoryController.addCategory(defaultCategory);
            }
        } else {
            // 尝试获取"其他"分类
            defaultCategory = categoryController.getCategoryByName("其他", TransactionType.EXPENSE);
            if (defaultCategory == null) {
                // 如果不存在，创建一个
                defaultCategory = new Category("其他", TransactionType.EXPENSE);
                categoryController.addCategory(defaultCategory);
            }
        }

        return defaultCategory;
    }
    
    /**
     * 计算特定月份的总收入
     */
    public double calculateTotalIncomeForMonth(int year, int month) {
        List<Transaction> monthTransactions = getTransactionsForMonth(year, month);
        double total = 0;
        
        for (Transaction transaction : monthTransactions) {
            if (transaction.getType() == TransactionType.INCOME) {
                total += transaction.getAmount();
            }
        }
        
        return total;
    }
    
    /**
     * 计算特定月份的总支出
     */
    public double calculateTotalExpenseForMonth(int year, int month) {
        List<Transaction> monthTransactions = getTransactionsForMonth(year, month);
        double total = 0;
        
        for (Transaction transaction : monthTransactions) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                total += transaction.getAmount();
            }
        }
        
        return total;
    }
    
    /**
     * 更新预算支出
     */
    private void updateBudgetSpending(Transaction transaction) {
        // 只处理支出类型的交易
        if (transaction.getType() != TransactionType.EXPENSE) {
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        
        // 获取预算控制器
        BudgetController budgetController = BudgetController.getInstance();
        
        // 更新相应分类的预算支出
        if (transaction.getCategory() != null) {
            budgetController.updateBudgetSpending(transaction.getCategory(), year, month, transaction.getAmount());
        }
    }
    
    /**
     * 从预算中移除支出
     */
    private void removeFromBudget(Transaction transaction) {
        // 只处理支出类型的交易
        if (transaction.getType() != TransactionType.EXPENSE) {
            return;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        
        // 获取预算控制器
        BudgetController budgetController = BudgetController.getInstance();
        
        // 从相应分类的预算中减去支出
        if (transaction.getCategory() != null) {
            budgetController.updateBudgetSpending(transaction.getCategory(), year, month, -transaction.getAmount());
        }
    }

    // Add this method to TransactionController.java class

    /**
     * 增强的日期解析方法，支持多种日期格式
     * @param dateStr 日期字符串
     * @return 解析后的Date对象
     * @throws ParseException 如果所有格式都无法解析
     */
    private Date parseFlexibleDate(String dateStr) throws ParseException {
        // 支持的日期格式列表
        String[] dateFormats = {
                "yyyy-MM-dd",      // 2025-05-23
                "yyyy/MM/dd",      // 2025/05/23
                "yyyy/M/d",        // 2025/5/23
                "yyyy-M-d",        // 2025-5-23
                "MM/dd/yyyy",      // 05/23/2025
                "M/d/yyyy",        // 5/23/2025
                "dd/MM/yyyy",      // 23/05/2025
                "d/M/yyyy",        // 23/5/2025
                "dd-MM-yyyy",      // 23-05-2025
                "d-M-yyyy",        // 23-5-2025
                "yyyyMMdd",        // 20250523
                "yyyy.MM.dd",      // 2025.05.23
                "yyyy.M.d"         // 2025.5.23
        };

        // 尝试每种格式
        for (String format : dateFormats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false); // 严格模式
                return sdf.parse(dateStr.trim());
            } catch (ParseException e) {
                // 继续尝试下一种格式
                continue;
            }
        }

        // 如果所有格式都失败，抛出异常
        throw new ParseException("Unable to parse date: " + dateStr +
                ". Supported formats: yyyy-MM-dd, yyyy/MM/dd, yyyy/M/d, MM/dd/yyyy, etc.", 0);
    }
}
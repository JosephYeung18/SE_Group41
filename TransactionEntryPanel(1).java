package com.financemanager.view;

import com.financemanager.controller.CategoryController;
import com.financemanager.controller.TransactionController;
import com.financemanager.model.Category;
import com.financemanager.model.Transaction;
import com.financemanager.model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import javax.swing.border.TitledBorder;

/**
 * 交易录入面板
 */
public class TransactionEntryPanel extends JPanel {
    
    private CategoryController categoryController;
    private TransactionController transactionController;
    
    private JPanel incomePanel;
    private JPanel expensePanel;
    
    private JComboBox<Category> incomeCategoryComboBox;
    private JComboBox<Category> expenseCategoryComboBox;
    
    private JTextField incomeAmountField;
    private JTextField expenseAmountField;
    
    private JTextArea incomeCommentArea;
    private JTextArea expenseCommentArea;
    
    private JButton incomeSubmitButton;
    private JButton expenseSubmitButton;
    
    private JButton importButton;
    
    private JSpinner incomeDateSpinner;
    private JSpinner expenseDateSpinner;
    
    // 添加对主窗口的引用
    private MainFrame mainFrame;
    
    public TransactionEntryPanel() {
        categoryController = new CategoryController();
        transactionController = TransactionController.getInstance();
        
        initComponents();
        setupLayout();
        setupActionListeners();
    }
    
    /**
     * 设置主窗口引用
     * @param mainFrame 主窗口
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    private void initComponents() {
        // 收入面板组件
        incomePanel = new JPanel();
        incomeCategoryComboBox = new JComboBox<>();
        incomeAmountField = new JTextField(10);
        incomeCommentArea = new JTextArea(3, 20);
        incomeSubmitButton = new JButton("添加收入");
        incomeDateSpinner = new JSpinner(new SpinnerDateModel());
        
        // 支出面板组件
        expensePanel = new JPanel();
        expenseCategoryComboBox = new JComboBox<>();
        expenseAmountField = new JTextField(10);
        expenseCommentArea = new JTextArea(3, 20);
        expenseSubmitButton = new JButton("添加支出");
        expenseDateSpinner = new JSpinner(new SpinnerDateModel());
        
        // 导入按钮
        importButton = new JButton("导入交易数据");
        
        // 加载分类数据
        loadCategories();
        
        // 设置日期选择器格式
        JSpinner.DateEditor incomeEditor = new JSpinner.DateEditor(incomeDateSpinner, "yyyy-MM-dd");
        incomeDateSpinner.setEditor(incomeEditor);
        incomeDateSpinner.setValue(new Date());
        
        JSpinner.DateEditor expenseEditor = new JSpinner.DateEditor(expenseDateSpinner, "yyyy-MM-dd");
        expenseDateSpinner.setEditor(expenseEditor);
        expenseDateSpinner.setValue(new Date());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 设置标题
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("交易录入");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // 创建快捷分类按钮面板
        JPanel quickCategoryPanel = createQuickCategoryPanel();
        
        // 设置收入面板
        incomePanel.setLayout(new GridBagLayout());
        incomePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 180, 100)), 
                "收入", TitledBorder.LEFT, TitledBorder.TOP));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        incomePanel.add(new JLabel("分类:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        incomePanel.add(incomeCategoryComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        incomePanel.add(new JLabel("金额:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        incomePanel.add(incomeAmountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        incomePanel.add(new JLabel("日期:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        incomePanel.add(incomeDateSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        incomePanel.add(new JLabel("备注:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JScrollPane incomeScrollPane = new JScrollPane(incomeCommentArea);
        incomePanel.add(incomeScrollPane, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        incomePanel.add(incomeSubmitButton, gbc);
        
        // 设置支出面板
        expensePanel.setLayout(new GridBagLayout());
        expensePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 100)), 
                "支出", TitledBorder.LEFT, TitledBorder.TOP));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        expensePanel.add(new JLabel("分类:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        expensePanel.add(expenseCategoryComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        expensePanel.add(new JLabel("金额:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        expensePanel.add(expenseAmountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        expensePanel.add(new JLabel("日期:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        expensePanel.add(expenseDateSpinner, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        expensePanel.add(new JLabel("备注:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JScrollPane expenseScrollPane = new JScrollPane(expenseCommentArea);
        expensePanel.add(expenseScrollPane, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        expensePanel.add(expenseSubmitButton, gbc);
        
        // 导入数据面板
        JPanel importPanel = new JPanel();
        importPanel.setBorder(BorderFactory.createTitledBorder("导入交易数据"));
        importPanel.add(importButton);
        
        // 主布局
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.add(incomePanel);
        centerPanel.add(expensePanel);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(importPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(quickCategoryPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createQuickCategoryPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("快捷分类"));
        
        // 添加常用分类按钮
        String[] quickCategories = {"餐饮", "购物", "交通", "住房", "娱乐", "教育", "医疗", "其他"};
        Color[] buttonColors = {
            new Color(255, 200, 200), // 餐饮 - 浅红色
            new Color(255, 230, 150), // 购物 - 浅黄色
            new Color(200, 230, 255), // 交通 - 浅蓝色
            new Color(200, 255, 200), // 住房 - 浅绿色
            new Color(230, 200, 255), // 娱乐 - 浅紫色
            new Color(255, 200, 230), // 教育 - 浅粉色
            new Color(200, 255, 230), // 医疗 - 浅青色
            new Color(230, 230, 230)  // 其他 - 浅灰色
        };
        
        for (int i = 0; i < quickCategories.length; i++) {
            JButton button = new JButton(quickCategories[i]);
            button.setBackground(buttonColors[i]);
            button.setFocusPainted(false);
            final int index = i;
            button.addActionListener(e -> {
                // 设置支出分类和焦点到金额字段
                for (int j = 0; j < expenseCategoryComboBox.getItemCount(); j++) {
                    Category category = expenseCategoryComboBox.getItemAt(j);
                    if (category.getName().equals(quickCategories[index])) {
                        expenseCategoryComboBox.setSelectedIndex(j);
                        expenseAmountField.requestFocusInWindow();
                        break;
                    }
                }
            });
            panel.add(button);
        }
        
        return panel;
    }
    
    private void setupActionListeners() {
        incomeSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIncomeTransaction();
            }
        });
        
        expenseSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpenseTransaction();
            }
        });
        
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importTransactions();
            }
        });
    }
    
    private void loadCategories() {
        List<Category> incomeCategories = categoryController.getCategoriesByType(TransactionType.INCOME);
        List<Category> expenseCategories = categoryController.getCategoriesByType(TransactionType.EXPENSE);
        
        incomeCategoryComboBox.removeAllItems();
        for (Category category : incomeCategories) {
            incomeCategoryComboBox.addItem(category);
        }
        
        expenseCategoryComboBox.removeAllItems();
        for (Category category : expenseCategories) {
            expenseCategoryComboBox.addItem(category);
        }
    }
    
    private void addIncomeTransaction() {
        try {
            Category category = (Category) incomeCategoryComboBox.getSelectedItem();
           
            if (category == null) {
                JOptionPane.showMessageDialog(this, "请选择分类", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String amountText = incomeAmountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入金额", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date date = (Date) incomeDateSpinner.getValue();
            String comment = incomeCommentArea.getText().trim();
            
            Transaction transaction = new Transaction();
            transaction.setCategory(category);
            transaction.setAmount(amount);
            transaction.setDate(date);
            transaction.setComment(comment);
            transaction.setType(TransactionType.INCOME);
            
            transactionController.addTransaction(transaction);
            
            // 清空表单
            incomeAmountField.setText("");
            incomeCommentArea.setText("");
            
            JOptionPane.showMessageDialog(this, "收入交易已添加", "成功", JOptionPane.INFORMATION_MESSAGE);
            
            // 刷新交易详情面板
            if (mainFrame != null) {
                mainFrame.refreshTransactionDetailsPanel();
                mainFrame.refreshAssetBudgetPanel(); // 同时刷新资产预算面板
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "添加交易失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addExpenseTransaction() {
        try {
            Category category = (Category) expenseCategoryComboBox.getSelectedItem();
           
            if (category == null) {
                JOptionPane.showMessageDialog(this, "请选择分类", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String amountText = expenseAmountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入金额", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date date = (Date) expenseDateSpinner.getValue();
            String comment = expenseCommentArea.getText().trim();
            
            Transaction transaction = new Transaction();
            transaction.setCategory(category);
            transaction.setAmount(amount);
            transaction.setDate(date);
            transaction.setComment(comment);
            transaction.setType(TransactionType.EXPENSE);
            
            transactionController.addTransaction(transaction);
            
            // 清空表单
            expenseAmountField.setText("");
            expenseCommentArea.setText("");
            
            JOptionPane.showMessageDialog(this, "支出交易已添加", "成功", JOptionPane.INFORMATION_MESSAGE);
            
            // 刷新交易详情面板
            if (mainFrame != null) {
                mainFrame.refreshTransactionDetailsPanel();
                mainFrame.refreshAssetBudgetPanel(); // 同时刷新资产预算面板
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "添加交易失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void importTransactions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择CSV文件");
        
        // 添加CSV文件格式说明
        JPanel accessoryPanel = new JPanel();
        accessoryPanel.setLayout(new BorderLayout());
        
        JTextArea formatInfo = new JTextArea(
            "支持的CSV格式:\n\n" +
            "1. 标准格式: 日期,类型,分类,金额[,备注]\n" +
            "2. 简化格式: 日期,类型,金额,备注\n\n" +
            "对于简化格式，系统将使用AI自动根据备注内容进行分类。\n" +
            "日期格式: yyyy-MM-dd (例如: 2025-04-13)\n" +
            "类型: 收入/支出 或 income/expense"
        );
        formatInfo.setEditable(false);
        formatInfo.setBackground(accessoryPanel.getBackground());
        formatInfo.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        accessoryPanel.add(formatInfo, BorderLayout.CENTER);
        
        fileChooser.setAccessory(accessoryPanel);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                
                // 显示进度对话框
                JDialog progressDialog = new JDialog();
                progressDialog.setTitle("导入进度");
                progressDialog.setSize(300, 100);
                progressDialog.setLocationRelativeTo(this);
                progressDialog.setModal(true);
                
                JPanel progressPanel = new JPanel(new BorderLayout());
                JLabel statusLabel = new JLabel("正在导入交易数据并进行AI分类...", SwingConstants.CENTER);
                progressPanel.add(statusLabel, BorderLayout.CENTER);
                
                progressDialog.add(progressPanel);
                
                // 使用SwingWorker在后台处理导入
                SwingWorker<Integer, Void> worker = new SwingWorker<Integer, Void>() {
                    @Override
                    protected Integer doInBackground() throws Exception {
                        return transactionController.importTransactionsFromCSV(filePath);
                    }
                    
                    @Override
                    protected void done() {
                        progressDialog.dispose();
                        try {
                            int count = get();
                            JOptionPane.showMessageDialog(TransactionEntryPanel.this, 
                                    "成功导入 " + count + " 条交易记录", 
                                    "导入成功", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            
                            // 刷新交易详情面板
                            if (mainFrame != null) {
                                mainFrame.refreshTransactionDetailsPanel();
                                mainFrame.refreshAssetBudgetPanel(); // 同时刷新资产预算面板
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(TransactionEntryPanel.this, 
                                    "导入失败: " + ex.getMessage(), 
                                    "错误", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                
                worker.execute();
                progressDialog.setVisible(true);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                        "导入失败: " + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
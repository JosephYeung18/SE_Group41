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
 * Transaction Entry Panel
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

    // Add reference to main window
    private MainFrame mainFrame;

    public TransactionEntryPanel() {
        categoryController = new CategoryController();
        transactionController = TransactionController.getInstance();

        initComponents();
        setupLayout();
        setupActionListeners();
    }

    /**
     * Set main window reference
     * @param mainFrame main window
     */
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private void initComponents() {
        // Income panel components
        incomePanel = new JPanel();
        incomeCategoryComboBox = new JComboBox<>();
        incomeAmountField = new JTextField(10);
        incomeCommentArea = new JTextArea(3, 20);
        incomeSubmitButton = new JButton("Add Income");
        incomeDateSpinner = new JSpinner(new SpinnerDateModel());

        // Expense panel components
        expensePanel = new JPanel();
        expenseCategoryComboBox = new JComboBox<>();
        expenseAmountField = new JTextField(10);
        expenseCommentArea = new JTextArea(3, 20);
        expenseSubmitButton = new JButton("Add Expense");
        expenseDateSpinner = new JSpinner(new SpinnerDateModel());

        // Import button
        importButton = new JButton("Import Transaction Data");

        // Load category data
        loadCategories();

        // Set date picker format
        JSpinner.DateEditor incomeEditor = new JSpinner.DateEditor(incomeDateSpinner, "yyyy-MM-dd");
        incomeDateSpinner.setEditor(incomeEditor);
        incomeDateSpinner.setValue(new Date());

        JSpinner.DateEditor expenseEditor = new JSpinner.DateEditor(expenseDateSpinner, "yyyy-MM-dd");
        expenseDateSpinner.setEditor(expenseEditor);
        expenseDateSpinner.setValue(new Date());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Transaction Entry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Create quick category button panel
        JPanel quickCategoryPanel = createQuickCategoryPanel();

        // Set income panel
        incomePanel.setLayout(new GridBagLayout());
        incomePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 180, 100)),
                "Income", TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        incomePanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        incomePanel.add(incomeCategoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        incomePanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        incomePanel.add(incomeAmountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        incomePanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        incomePanel.add(incomeDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        incomePanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JScrollPane incomeScrollPane = new JScrollPane(incomeCommentArea);
        incomePanel.add(incomeScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        incomePanel.add(incomeSubmitButton, gbc);

        // Set expense panel
        expensePanel.setLayout(new GridBagLayout());
        expensePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 100, 100)),
                "Expense", TitledBorder.LEFT, TitledBorder.TOP));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        expensePanel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        expensePanel.add(expenseCategoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        expensePanel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        expensePanel.add(expenseAmountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        expensePanel.add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        expensePanel.add(expenseDateSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        expensePanel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JScrollPane expenseScrollPane = new JScrollPane(expenseCommentArea);
        expensePanel.add(expenseScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        expensePanel.add(expenseSubmitButton, gbc);

        // Import data panel
        JPanel importPanel = new JPanel();
        importPanel.setBorder(BorderFactory.createTitledBorder("Import Transaction Data"));
        importPanel.add(importButton);

        // Main layout
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
        panel.setBorder(BorderFactory.createTitledBorder("Quick Categories"));

        // Add common category buttons
        String[] quickCategories = {"Food & Dining", "Shopping", "Transportation", "Housing", "Entertainment", "Education", "Healthcare", "Other"};
        Color[] buttonColors = {
                new Color(255, 200, 200), // Food & Dining - light red
                new Color(255, 230, 150), // Shopping - light yellow
                new Color(200, 230, 255), // Transportation - light blue
                new Color(200, 255, 200), // Housing - light green
                new Color(230, 200, 255), // Entertainment - light purple
                new Color(255, 200, 230), // Education - light pink
                new Color(200, 255, 230), // Healthcare - light cyan
                new Color(230, 230, 230)  // Other - light gray
        };

        for (int i = 0; i < quickCategories.length; i++) {
            JButton button = new JButton(quickCategories[i]);
            button.setBackground(buttonColors[i]);
            button.setFocusPainted(false);
            final int index = i;
            button.addActionListener(e -> {
                // Set expense category and focus to amount field
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
                JOptionPane.showMessageDialog(this, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String amountText = incomeAmountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
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

            // Clear form
            incomeAmountField.setText("");
            incomeCommentArea.setText("");

            JOptionPane.showMessageDialog(this, "Income transaction added", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh transaction details panel
            if (mainFrame != null) {
                mainFrame.refreshTransactionDetailsPanel();
                mainFrame.refreshAssetBudgetPanel(); // Also refresh asset budget panel
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addExpenseTransaction() {
        try {
            Category category = (Category) expenseCategoryComboBox.getSelectedItem();

            if (category == null) {
                JOptionPane.showMessageDialog(this, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String amountText = expenseAmountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
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

            // Clear form
            expenseAmountField.setText("");
            expenseCommentArea.setText("");

            JOptionPane.showMessageDialog(this, "Expense transaction added", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh transaction details panel
            if (mainFrame != null) {
                mainFrame.refreshTransactionDetailsPanel();
                mainFrame.refreshAssetBudgetPanel(); // Also refresh asset budget panel
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importTransactions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");

        // Add CSV format description
        JPanel accessoryPanel = new JPanel();
        accessoryPanel.setLayout(new BorderLayout());

        JTextArea formatInfo = new JTextArea(
                "Supported CSV formats:\n\n" +
                        "1. Standard format: Date,Type,Category,Amount[,Notes]\n" +
                        "2. Simplified format: Date,Type,Amount,Notes\n\n" +
                        "For simplified format, the system will use AI to automatically categorize based on notes content.\n" +
                        "Date format: yyyy-MM-dd (e.g., 2025-04-13)\n" +
                        "Type: Income/Expense or income/expense"
        );
        formatInfo.setEditable(false);
        formatInfo.setBackground(accessoryPanel.getBackground());
        formatInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        accessoryPanel.add(formatInfo, BorderLayout.CENTER);

        fileChooser.setAccessory(accessoryPanel);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // Show progress dialog
                JDialog progressDialog = new JDialog();
                progressDialog.setTitle("Import Progress");
                progressDialog.setSize(300, 100);
                progressDialog.setLocationRelativeTo(this);
                progressDialog.setModal(true);

                JPanel progressPanel = new JPanel(new BorderLayout());
                JLabel statusLabel = new JLabel("Importing transaction data and performing AI classification...", SwingConstants.CENTER);
                progressPanel.add(statusLabel, BorderLayout.CENTER);

                progressDialog.add(progressPanel);

                // Use SwingWorker to handle import in background
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
                                    "Successfully imported " + count + " transaction records",
                                    "Import Successful",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Refresh transaction details panel
                            if (mainFrame != null) {
                                mainFrame.refreshTransactionDetailsPanel();
                                mainFrame.refreshAssetBudgetPanel(); // Also refresh asset budget panel
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(TransactionEntryPanel.this,
                                    "Import failed: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
                progressDialog.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Import failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
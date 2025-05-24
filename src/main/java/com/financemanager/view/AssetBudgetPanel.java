package com.financemanager.view;

import com.financemanager.controller.AccountController;
import com.financemanager.controller.BudgetController;
import com.financemanager.model.Account;
import com.financemanager.model.Budget;
import com.financemanager.model.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import javax.swing.border.TitledBorder;
import java.text.DecimalFormat;

/**
 * Asset Budget Panel
 */
public class AssetBudgetPanel extends JPanel {

    private AccountController accountController;
    private BudgetController budgetController;

    private JPanel assetsPanel;
    private JPanel budgetPanel;

    private JLabel totalAssetsLabel;
    private JLabel liabilitiesLabel;
    private JLabel netWorthLabel;

    private JLabel totalBudgetLabel;
    private JLabel spentLabel;
    private JLabel remainingLabel;

    private JList<Account> accountsList;
    private DefaultListModel<Account> accountsListModel;

    private JPanel categoryBudgetPanel;
    private JPanel categorySpentPanel;

    private JButton addAccountButton;
    private JButton editAccountButton;
    private JButton deleteAccountButton;

    private JButton addBudgetButton;
    private JButton editBudgetButton;

    public AssetBudgetPanel() {
        accountController = new AccountController();
        budgetController = BudgetController.getInstance();

        initComponents();
        setupLayout();
        setupActionListeners();
        refreshData();
    }

    private void initComponents() {
        // Assets panel components
        assetsPanel = new JPanel();
        totalAssetsLabel = new JLabel("$ 0.00");
        liabilitiesLabel = new JLabel("$ 0.00");
        netWorthLabel = new JLabel("$ 0.00");

        accountsListModel = new DefaultListModel<>();
        accountsList = new JList<>(accountsListModel);

        addAccountButton = new JButton("Add Account");
        editAccountButton = new JButton("Edit Account");
        deleteAccountButton = new JButton("Delete Account");

        // Budget panel components
        budgetPanel = new JPanel();
        totalBudgetLabel = new JLabel("$ 0.00");
        spentLabel = new JLabel("$ 0.00");
        remainingLabel = new JLabel("$ 0.00");

        categoryBudgetPanel = new JPanel();
        categorySpentPanel = new JPanel();

        addBudgetButton = new JButton("Add Budget");
        editBudgetButton = new JButton("Edit Budget");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Assets & Budget");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Assets panel
        assetsPanel.setLayout(new BorderLayout());
        assetsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "My Assets", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel assetSummaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        assetSummaryPanel.add(new JLabel("Total Assets:", SwingConstants.RIGHT));
        totalAssetsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        assetSummaryPanel.add(totalAssetsLabel);

        assetSummaryPanel.add(new JLabel("Liabilities:", SwingConstants.RIGHT));
        liabilitiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        assetSummaryPanel.add(liabilitiesLabel);

        assetSummaryPanel.add(new JLabel("Net Worth:", SwingConstants.RIGHT));
        netWorthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        assetSummaryPanel.add(netWorthLabel);

        JPanel accountsPanel = new JPanel(new BorderLayout());
        accountsPanel.setBorder(BorderFactory.createTitledBorder("Asset Accounts"));

        JScrollPane accountsScrollPane = new JScrollPane(accountsList);
        accountsScrollPane.setPreferredSize(new Dimension(300, 150));
        accountsPanel.add(accountsScrollPane, BorderLayout.CENTER);

        JPanel accountButtonsPanel = new JPanel();
        accountButtonsPanel.add(addAccountButton);
        accountButtonsPanel.add(editAccountButton);
        accountButtonsPanel.add(deleteAccountButton);
        accountsPanel.add(accountButtonsPanel, BorderLayout.SOUTH);

        assetsPanel.add(assetSummaryPanel, BorderLayout.NORTH);
        assetsPanel.add(accountsPanel, BorderLayout.CENTER);

        // Budget panel
        budgetPanel.setLayout(new BorderLayout());
        budgetPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "This Month's Budget", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel budgetSummaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        budgetSummaryPanel.add(new JLabel("Total Budget:", SwingConstants.RIGHT));
        totalBudgetLabel.setFont(new Font("Arial", Font.BOLD, 14));
        budgetSummaryPanel.add(totalBudgetLabel);

        budgetSummaryPanel.add(new JLabel("Spent:", SwingConstants.RIGHT));
        spentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        budgetSummaryPanel.add(spentLabel);

        budgetSummaryPanel.add(new JLabel("Remaining:", SwingConstants.RIGHT));
        remainingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        budgetSummaryPanel.add(remainingLabel);

        categoryBudgetPanel.setLayout(new BoxLayout(categoryBudgetPanel, BoxLayout.Y_AXIS));
        categoryBudgetPanel.setBorder(BorderFactory.createTitledBorder("Category Budget"));

        JPanel budgetControlPanel = new JPanel();
        budgetControlPanel.add(addBudgetButton);
        budgetControlPanel.add(editBudgetButton);

        budgetPanel.add(budgetSummaryPanel, BorderLayout.NORTH);
        budgetPanel.add(new JScrollPane(categoryBudgetPanel), BorderLayout.CENTER);
        budgetPanel.add(budgetControlPanel, BorderLayout.SOUTH);

        // Main layout
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(assetsPanel);
        mainPanel.add(budgetPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupActionListeners() {
        addAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAccountDialog();
            }
        });

        editAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selectedAccount = accountsList.getSelectedValue();
                if (selectedAccount != null) {
                    showEditAccountDialog(selectedAccount);
                } else {
                    JOptionPane.showMessageDialog(AssetBudgetPanel.this,
                            "Please select an account to edit",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account selectedAccount = accountsList.getSelectedValue();
                if (selectedAccount != null) {
                    int response = JOptionPane.showConfirmDialog(AssetBudgetPanel.this,
                            "Are you sure you want to delete account '" + selectedAccount.getName() + "'?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        accountController.deleteAccount(selectedAccount.getId());
                        refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(AssetBudgetPanel.this,
                            "Please select an account to delete",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        addBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBudgetDialog();
            }
        });

        editBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditBudgetDialog();
            }
        });
    }

    /**
     * Refresh panel data
     */
    public void refreshData() {
        // Refresh account list and totals
        List<Account> accounts = accountController.getAllAccounts();

        accountsListModel.clear();
        double totalAssets = 0;
        double totalLiabilities = 0;

        for (Account account : accounts) {
            accountsListModel.addElement(account);

            if (account.getBalance() >= 0) {
                totalAssets += account.getBalance();
            } else {
                totalLiabilities += Math.abs(account.getBalance());
            }
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        totalAssetsLabel.setText("$ " + df.format(totalAssets));
        liabilitiesLabel.setText("$ " + df.format(totalLiabilities));
        netWorthLabel.setText("$ " + df.format(totalAssets - totalLiabilities));

        // Refresh budget information
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        List<Budget> budgets = budgetController.getBudgetsForMonth(currentYear, currentMonth);

        double totalBudget = 0;
        double totalSpent = 0;

        for (Budget budget : budgets) {
            totalBudget += budget.getAmount();
            totalSpent += budget.getSpent();
        }

        totalBudgetLabel.setText("$ " + df.format(totalBudget));
        spentLabel.setText("$ " + df.format(totalSpent));
        remainingLabel.setText("$ " + df.format(totalBudget - totalSpent));

        // Update category budget panel
        updateCategoryBudgetPanel(budgets);
    }

    private void updateCategoryBudgetPanel(List<Budget> budgets) {
        categoryBudgetPanel.removeAll();

        // If no budgets
        if (budgets.isEmpty()) {
            JLabel emptyLabel = new JLabel("No budget set yet");
            emptyLabel.setFont(getChineseSupportFont(Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            categoryBudgetPanel.add(emptyLabel);
        } else {
            // Create progress bar for each budget item
            for (Budget budget : budgets) {
                JPanel budgetItemPanel = new JPanel(new BorderLayout(8, 0));
                budgetItemPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                budgetItemPanel.setBackground(Color.WHITE);

                // Category name label - 使用支持中文的字体
                JLabel categoryLabel = new JLabel(budget.getCategory().getName());
                categoryLabel.setFont(getChineseSupportFont(Font.BOLD, 13));
                categoryLabel.setPreferredSize(new Dimension(80, 20));

                // Create styled progress bar
                JProgressBar progressBar = createStyledProgressBar();
                int percentage = (int) ((budget.getSpent() / budget.getAmount()) * 100);
                progressBar.setValue(Math.min(percentage, 100));

                // Set colors based on budget completion
                if (budget.getSpent() > budget.getAmount()) {
                    // Over budget - red gradient
                    progressBar.setForeground(new Color(220, 53, 69));
                    progressBar.setBackground(new Color(248, 215, 218));
                } else if (percentage >= 80) {
                    // Close to budget limit - orange gradient
                    progressBar.setForeground(new Color(255, 133, 27));
                    progressBar.setBackground(new Color(255, 236, 217));
                } else {
                    // Normal range - blue-green gradient
                    progressBar.setForeground(new Color(32, 201, 151));
                    progressBar.setBackground(new Color(208, 244, 234));
                }

                // Amount information label
                DecimalFormat df = new DecimalFormat("#,##0.00");
                JLabel amountLabel = new JLabel(
                        "<html><div style='text-align: right;'>" +
                                "<span style='color: #666; font-size: 11px;'>$" + df.format(budget.getSpent()) + "</span><br>" +
                                "<span style='color: #333; font-size: 10px;'>/ $" + df.format(budget.getAmount()) + "</span>" +
                                "</div></html>"
                );
                amountLabel.setFont(getChineseSupportFont(Font.PLAIN, 11));
                amountLabel.setPreferredSize(new Dimension(100, 32));

                budgetItemPanel.add(categoryLabel, BorderLayout.WEST);
                budgetItemPanel.add(progressBar, BorderLayout.CENTER);
                budgetItemPanel.add(amountLabel, BorderLayout.EAST);

                categoryBudgetPanel.add(budgetItemPanel);

                // Add separator (except for the last one)
                if (budgets.indexOf(budget) < budgets.size() - 1) {
                    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                    separator.setForeground(new Color(240, 240, 240));
                    categoryBudgetPanel.add(separator);
                }
            }
        }

        categoryBudgetPanel.revalidate();
        categoryBudgetPanel.repaint();
    }

    /**
     * Create styled progress bar
     */
    private JProgressBar createStyledProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, 100);

        // Set progress bar size
        progressBar.setPreferredSize(new Dimension(200, 16));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));

        // Set style
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        // Set font - 使用支持中文的字体
        progressBar.setFont(getChineseSupportFont(Font.BOLD, 10));

        // Custom progress bar appearance with Chinese font support
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();

                // Draw background (rounded rectangle)
                g2d.setColor(progressBar.getBackground());
                g2d.fillRoundRect(0, 0, width, height, 8, 8);

                // Draw progress (rounded rectangle)
                if (progressBar.getValue() > 0) {
                    int progressWidth = (int) ((double) progressBar.getValue() / progressBar.getMaximum() * width);
                    g2d.setColor(progressBar.getForeground());
                    g2d.fillRoundRect(0, 0, progressWidth, height, 8, 8);
                }

                // Draw percentage text with Chinese font support
                if (progressBar.isStringPainted()) {
                    String text = progressBar.getValue() + "%";

                    // 使用支持中文的字体
                    Font chineseFont = getChineseSupportFont(Font.BOLD, 10);
                    g2d.setFont(chineseFont);

                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();

                    g2d.setColor(Color.WHITE);
                    g2d.drawString(text, (width - textWidth) / 2, (height + textHeight / 2) / 2 - 1);
                }

                g2d.dispose();
            }
        });

        return progressBar;
    }

    private void showAddAccountDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Account", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(20);
        JTextField balanceField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);

        formPanel.add(new JLabel("Account Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Balance:"));
        formPanel.add(balanceField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter account name", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String balanceText = balanceField.getText().trim();
                    if (balanceText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter balance", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double balance = Double.parseDouble(balanceText);
                    String description = descriptionField.getText().trim();

                    Account account = new Account();
                    account.setName(name);
                    account.setBalance(balance);
                    account.setDescription(description);

                    accountController.addAccount(account);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid balance format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to add account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditAccountDialog(Account account) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Account", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(account.getName(), 20);
        JTextField balanceField = new JTextField(String.valueOf(account.getBalance()), 20);
        JTextField descriptionField = new JTextField(account.getDescription(), 20);

        formPanel.add(new JLabel("Account Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Balance:"));
        formPanel.add(balanceField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter account name", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String balanceText = balanceField.getText().trim();
                    if (balanceText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter balance", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double balance = Double.parseDouble(balanceText);
                    String description = descriptionField.getText().trim();

                    account.setName(name);
                    account.setBalance(balance);
                    account.setDescription(description);

                    accountController.updateAccount(account);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid balance format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to update account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddBudgetDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Budget", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get expense categories
        List<Category> categories = budgetController.getExpenseCategories();
        JComboBox<Category> categoryComboBox = new JComboBox<>();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }

        JTextField amountField = new JTextField(20);

        // Month selection
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(currentMonth);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);
        formPanel.add(new JLabel("Budget Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Month:"));
        formPanel.add(monthComboBox);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Category category = (Category) categoryComboBox.getSelectedItem();
                    if (category == null) {
                        JOptionPane.showMessageDialog(dialog, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter budget amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Budget amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int month = monthComboBox.getSelectedIndex() + 1;

                    Budget budget = new Budget();
                    budget.setCategory(category);
                    budget.setAmount(amount);
                    budget.setYear(currentYear);
                    budget.setMonth(month);
                    budget.setSpent(0); // Initial spent amount is 0

                    budgetController.addBudget(budget);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid budget amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to add budget: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditBudgetDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        List<Budget> budgets = budgetController.getBudgetsForMonth(currentYear, currentMonth);

        if (budgets.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No budget to edit for current month",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Budget", true);
        dialog.setLayout(new BorderLayout());

        // Create budget list
        DefaultListModel<Budget> budgetListModel = new DefaultListModel<>();
        for (Budget budget : budgets) {
            budgetListModel.addElement(budget);
        }

        JList<Budget> budgetList = new JList<>(budgetListModel);
        budgetList.setCellRenderer(new BudgetListCellRenderer());
        budgetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(budgetList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPanel editPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField amountField = new JTextField(20);

        editPanel.add(new JLabel("Budget Amount:"));
        editPanel.add(amountField);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton deleteButton = new JButton("Delete Budget");

        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        // Update amount field when selecting budget
        budgetList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Budget selectedBudget = budgetList.getSelectedValue();
                if (selectedBudget != null) {
                    amountField.setText(String.valueOf(selectedBudget.getAmount()));
                    amountField.setEnabled(true);
                    saveButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    amountField.setText("");
                    amountField.setEnabled(false);
                    saveButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Budget selectedBudget = budgetList.getSelectedValue();
                    if (selectedBudget == null) {
                        JOptionPane.showMessageDialog(dialog, "Please select a budget", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter budget amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Budget amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    selectedBudget.setAmount(amount);

                    budgetController.updateBudget(selectedBudget);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid budget amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to update budget: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Budget selectedBudget = budgetList.getSelectedValue();
                if (selectedBudget != null) {
                    int response = JOptionPane.showConfirmDialog(dialog,
                            "Are you sure you want to delete budget for category '" + selectedBudget.getCategory().getName() + "'?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        budgetController.deleteBudget(selectedBudget.getId());
                        refreshData();
                        dialog.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Please select a budget to delete",
                            "Information",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Initially disable save and delete buttons
        amountField.setEnabled(false);
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(editPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Custom renderer for displaying budgets in list
    private class BudgetListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Budget) {
                Budget budget = (Budget) value;
                DecimalFormat df = new DecimalFormat("#,##0.00");
                setText(budget.getCategory().getName() + " - $" + df.format(budget.getAmount()));
            }

            return this;
        }
    }

    private Font getChineseSupportFont(int style, int size) {
        // 尝试多种支持中文的字体，找到系统支持的第一个
        String[] fontNames = {
                "Microsoft YaHei UI",  // 微软雅黑UI
                "Microsoft YaHei",     // 微软雅黑
                "SimHei",              // 黑体
                "SimSun",              // 宋体
                "NSimSun",             // 新宋体
                "PingFang SC",         // macOS 苹方字体
                "Hiragino Sans GB",    // macOS 冬青黑体
                "WenQuanYi Micro Hei", // Linux 文泉驿微米黑
                "Noto Sans CJK SC",    // Google Noto 字体
                "DejaVu Sans",         // Linux 标准字体
                "SansSerif"            // Java 默认无衬线字体
        };

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();

        for (String fontName : fontNames) {
            for (String availableFont : availableFonts) {
                if (availableFont.equals(fontName)) {
                    Font font = new Font(fontName, style, size);
                    // 测试字体是否能正确显示中文
                    if (font.canDisplay('中') && font.canDisplay('文')) {
                        return font;
                    }
                }
            }
        }

        // 如果没有找到合适的字体，使用Java默认字体并启用Unicode支持
        return new Font(Font.SANS_SERIF, style, size);
    }
}
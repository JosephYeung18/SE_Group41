package com.financemanager.view;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import com.financemanager.controller.TransactionController;
import com.financemanager.model.Transaction;
import com.financemanager.model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

/**
 * Transaction Details Panel - displays transaction history and statistics
 */
public class TransactionDetailsPanel extends JPanel {

    private TransactionController transactionController;

    private JPanel summaryPanel;
    private JPanel transactionsPanel;

    private JLabel balanceLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;

    private JTable transactionsTable;
    private DefaultTableModel tableModel;

    private JButton viewAllButton;
    private JButton filterButton;
    private JButton exportButton;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    public TransactionDetailsPanel() {
        transactionController = TransactionController.getInstance();

        initComponents();
        setupLayout();
        setupActionListeners();
        refreshData();
    }

    private void initComponents() {
        // Summary panel components
        summaryPanel = new JPanel();
        balanceLabel = new JLabel("$ 0.00");
        incomeLabel = new JLabel("$ 0.00");
        expenseLabel = new JLabel("$ 0.00");

        // Transaction table components
        transactionsPanel = new JPanel();

        // Table column names
        String[] columnNames = {"Date", "Type", "Category", "Amount", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table is not editable
            }
        };

        transactionsTable = new JTable(tableModel);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Buttons
        viewAllButton = new JButton("View All");
        filterButton = new JButton("Filter");
        exportButton = new JButton("Export");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Transaction Details & Statistics");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Summary panel
        summaryPanel.setLayout(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Monthly Summary", TitledBorder.LEFT, TitledBorder.TOP));

        JPanel balancePanel = new JPanel(new BorderLayout());
        JLabel balanceTitleLabel = new JLabel("Net Income:");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balancePanel.add(balanceTitleLabel, BorderLayout.NORTH);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        JPanel incomePanel = new JPanel(new BorderLayout());
        JLabel incomeTitleLabel = new JLabel("Total Income:");
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        incomePanel.add(incomeTitleLabel, BorderLayout.NORTH);
        incomePanel.add(incomeLabel, BorderLayout.CENTER);

        JPanel expensePanel = new JPanel(new BorderLayout());
        JLabel expenseTitleLabel = new JLabel("Total Expenses:");
        expenseLabel.setFont(new Font("Arial", Font.BOLD, 14));
        expensePanel.add(expenseTitleLabel, BorderLayout.NORTH);
        expensePanel.add(expenseLabel, BorderLayout.CENTER);

        summaryPanel.add(incomePanel);
        summaryPanel.add(expensePanel);
        summaryPanel.add(balancePanel);

        // Transaction table panel
        transactionsPanel.setLayout(new BorderLayout());
        transactionsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Transaction Records", TitledBorder.LEFT, TitledBorder.TOP));

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewAllButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(exportButton);

        transactionsPanel.add(scrollPane, BorderLayout.CENTER);
        transactionsPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(summaryPanel, BorderLayout.NORTH);
        topPanel.add(transactionsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupActionListeners() {
        viewAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllTransactions();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterDialog();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportTransactions();
            }
        });
    }

    /**
     * Refresh transaction data and statistics
     */
    public void refreshData() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        // Get current month transaction data
        List<Transaction> monthTransactions = transactionController.getTransactionsForMonth(currentYear, currentMonth);

        // Calculate income and expenses
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction transaction : monthTransactions) {
            if (transaction.getType() == TransactionType.INCOME) {
                totalIncome += transaction.getAmount();
            } else {
                totalExpense += transaction.getAmount();
            }
        }

        // Update labels
        incomeLabel.setText("$ " + moneyFormat.format(totalIncome));
        expenseLabel.setText("$ " + moneyFormat.format(totalExpense));
        balanceLabel.setText("$ " + moneyFormat.format(totalIncome - totalExpense));

        // Update table data
        refreshTableData(monthTransactions);
    }

    private void refreshTableData(List<Transaction> transactions) {
        // Clear table
        tableModel.setRowCount(0);

        // Add transaction data to table
        for (Transaction transaction : transactions) {
            Object[] rowData = {
                    dateFormat.format(transaction.getDate()),
                    transaction.getType().getDisplayName(),
                    transaction.getCategory() != null ? transaction.getCategory().getName() : "",
                    "$ " + moneyFormat.format(transaction.getAmount()),
                    transaction.getComment()
            };

            tableModel.addRow(rowData);
        }
    }

    private void showAllTransactions() {
        // Get all transactions sorted by date
        List<Transaction> allTransactions = transactionController.getTransactionsSortedByDate();
        refreshTableData(allTransactions);
    }

    private void showFilterDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Filter Transactions", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Date range selection
        JLabel startDateLabel = new JLabel("Start Date:");
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);

        JLabel endDateLabel = new JLabel("End Date:");
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);

        // Set default date range to current month
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        startDateSpinner.setValue(startDate);
        endDateSpinner.setValue(endDate);

        // Type selection
        JLabel typeLabel = new JLabel("Transaction Type:");
        String[] typeOptions = {"All", "Income", "Expense"};
        JComboBox<String> typeComboBox = new JComboBox<>(typeOptions);

        // Buttons
        JButton applyButton = new JButton("Apply Filter");
        JButton cancelButton = new JButton("Cancel");

        formPanel.add(startDateLabel);
        formPanel.add(startDateSpinner);
        formPanel.add(endDateLabel);
        formPanel.add(endDateSpinner);
        formPanel.add(typeLabel);
        formPanel.add(typeComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date start = (Date) startDateSpinner.getValue();
                Date end = (Date) endDateSpinner.getValue();

                // Ensure end date is the last moment of the day
                Calendar endCal = Calendar.getInstance();
                endCal.setTime(end);
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                end = endCal.getTime();

                List<Transaction> filteredTransactions;

                // Filter by type
                String selectedType = (String) typeComboBox.getSelectedItem();
                if ("Income".equals(selectedType)) {
                    List<Transaction> incomeTransactions = transactionController.getTransactionsByType(TransactionType.INCOME);
                    filteredTransactions = filterByDateRange(incomeTransactions, start, end);
                } else if ("Expense".equals(selectedType)) {
                    List<Transaction> expenseTransactions = transactionController.getTransactionsByType(TransactionType.EXPENSE);
                    filteredTransactions = filterByDateRange(expenseTransactions, start, end);
                } else {
                    filteredTransactions = transactionController.getTransactionsByDateRange(start, end);
                }

                refreshTableData(filteredTransactions);
                dialog.dispose();
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

    private List<Transaction> filterByDateRange(List<Transaction> transactions, Date startDate, Date endDate) {
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Date date = transaction.getDate();
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                filtered.add(transaction);
            }
        }

        return filtered;
    }

    /**
     * Export transaction data
     */
    private void exportTransactions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Transaction Data");
        fileChooser.setSelectedFile(new File("transactions.csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                // Get currently displayed transaction data (based on current filter)
                List<Transaction> transactionsToExport = getCurrentDisplayedTransactions();

                StringBuilder csv = new StringBuilder();

                // Add BOM to support Chinese characters in Excel
                csv.append("\uFEFF");

                // Add header row
                csv.append("Date,Type,Category,Amount,Notes\n");

                // Add data rows
                for (Transaction transaction : transactionsToExport) {
                    csv.append(escapeCSVField(dateFormat.format(transaction.getDate()))).append(",");
                    csv.append(escapeCSVField(transaction.getType().getDisplayName())).append(",");
                    csv.append(escapeCSVField(transaction.getCategory() != null ? transaction.getCategory().getName() : "")).append(",");
                    // Use raw amount data to maintain precision
                    csv.append(String.valueOf(transaction.getAmount())).append(",");
                    csv.append(escapeCSVField(transaction.getComment() != null ? transaction.getComment() : ""));
                    csv.append("\n");
                }

                // Save to file
                try (FileWriter writer = new FileWriter(file, java.nio.charset.StandardCharsets.UTF_8)) {
                    writer.write(csv.toString());
                }

                JOptionPane.showMessageDialog(this,
                        "Transaction data has been successfully exported to: " + file.getAbsolutePath() +
                                "\nTotal " + transactionsToExport.size() + " records exported",
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Export failed: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Escape CSV field, handle fields containing commas, quotes, and newlines
     * @param field Field to escape
     * @return Escaped field
     */
    private String escapeCSVField(String field) {
        if (field == null) {
            return "";
        }

        // If field contains commas, quotes, or newlines, surround with quotes
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            // Escape quotes in field to double quotes
            String escapedField = field.replace("\"", "\"\"");
            return "\"" + escapedField + "\"";
        }

        return field;
    }

    /**
     * Get currently displayed transaction data
     * Determine which transactions to export based on table content
     * @return Currently displayed transaction list
     */
    private List<Transaction> getCurrentDisplayedTransactions() {
        List<Transaction> displayedTransactions = new ArrayList<>();

        // If table is empty, return empty list
        if (tableModel.getRowCount() == 0) {
            return displayedTransactions;
        }

        // Get all transactions
        List<Transaction> allTransactions = transactionController.getTransactionsSortedByDate();

        // Determine displayed transactions based on table rows
        // Since table shows filtered results, we need to match table data
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String dateStr = (String) tableModel.getValueAt(i, 0);
            String typeStr = (String) tableModel.getValueAt(i, 1);
            String categoryStr = (String) tableModel.getValueAt(i, 2);
            String amountStr = (String) tableModel.getValueAt(i, 3);
            String commentStr = (String) tableModel.getValueAt(i, 4);

            // Clean amount string
            String cleanAmountStr = amountStr.replace("$ ", "").replace(",", "");

            try {
                Date tableDate = dateFormat.parse(dateStr);
                double tableAmount = Double.parseDouble(cleanAmountStr);

                // Find matching transaction in all transactions
                for (Transaction transaction : allTransactions) {
                    if (dateFormat.format(transaction.getDate()).equals(dateStr) &&
                            transaction.getType().getDisplayName().equals(typeStr) &&
                            Math.abs(transaction.getAmount() - tableAmount) < 0.01 && // Use small error range for floating point comparison
                            (transaction.getCategory() != null ? transaction.getCategory().getName() : "").equals(categoryStr) &&
                            (transaction.getComment() != null ? transaction.getComment() : "").equals(commentStr)) {

                        displayedTransactions.add(transaction);
                        break; // Break inner loop after finding match
                    }
                }
            } catch (Exception e) {
                System.err.println("Error parsing table data: " + e.getMessage());
            }
        }

        return displayedTransactions;
    }
}
package com.financemanager.view;

import com.financemanager.controller.TransactionController;
import com.financemanager.util.DataAdapter;
import com.financemanager.util.ChartGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AnalysisPanel extends JPanel {

    private TransactionController transactionController;

    private JPanel contentPanel;
    private JPanel timeSelectionPanel;
    private JPanel dataDisplayPanel;
    private CardLayout cardLayout;

    private JComboBox<String> analysisTypeComboBox;
    private JComboBox<MonthYear> monthComboBox;
    private JButton viewButton;

    private static final String[] ANALYSIS_TYPES = {
            "Single Month Analysis", "Three Month Trend", "Quarterly Analysis"
    };

    // 用于存储月份和年份的内部类
    private static class MonthYear {
        private int year;
        private int month;
        private String displayName;

        public MonthYear(int year, int month, String displayName) {
            this.year = year;
            this.month = month;
            this.displayName = displayName;
        }

        public int getYear() { return year; }
        public int getMonth() { return month; }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public AnalysisPanel() {
        transactionController = TransactionController.getInstance();

        initComponents();
        setupLayout();
        setupActionListeners();
    }

    private void initComponents() {
        // Create selection panel components
        timeSelectionPanel = new JPanel();
        analysisTypeComboBox = new JComboBox<>(ANALYSIS_TYPES);

        // 动态生成最近几个月的选项
        MonthYear[] availableMonths = getAvailableMonths();
        monthComboBox = new JComboBox<>(availableMonths);

        viewButton = new JButton("View Analysis");

        // Create content display panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        dataDisplayPanel = new JPanel();
        dataDisplayPanel.setLayout(new BoxLayout(dataDisplayPanel, BoxLayout.Y_AXIS));
    }

    private MonthYear[] getAvailableMonths() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM"); // Changed to English format
        MonthYear[] months = new MonthYear[6]; // 显示最近6个月

        for (int i = 0; i < 6; i++) {
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1; // Calendar月份从0开始
            String displayName = monthFormat.format(cal.getTime());

            months[i] = new MonthYear(year, month, displayName);
            cal.add(Calendar.MONTH, -1);
        }

        return months;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Financial Data Analysis");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Set time selection panel
        timeSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Time & Analysis Type Selection", TitledBorder.LEFT, TitledBorder.TOP));
        timeSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        timeSelectionPanel.add(new JLabel("Analysis Type:"));
        timeSelectionPanel.add(analysisTypeComboBox);
        timeSelectionPanel.add(new JLabel("Month:"));
        timeSelectionPanel.add(monthComboBox);
        timeSelectionPanel.add(viewButton);

        // Set data display panel
        JScrollPane scrollPane = new JScrollPane(dataDisplayPanel);
        contentPanel.add(scrollPane, "DATA_DISPLAY");

        // Add to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(timeSelectionPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void setupActionListeners() {
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDataDisplay();
            }
        });

        // 当分析类型改变时，更新月份选择器的可见性
        analysisTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) analysisTypeComboBox.getSelectedItem();
                // 对于单月分析，显示月份选择器；对于趋势分析，可以隐藏或显示不同的选项
                monthComboBox.setEnabled("Single Month Analysis".equals(selectedType));
            }
        });

        // Auto load data on initialization
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateDataDisplay();
            }
        });
    }

    private void updateDataDisplay() {
        // Clear previous data display
        dataDisplayPanel.removeAll();

        String analysisType = (String) analysisTypeComboBox.getSelectedItem();
        MonthYear selectedMonthYear = (MonthYear) monthComboBox.getSelectedItem();

        // 使用选择的月份和年份
        int year = selectedMonthYear != null ? selectedMonthYear.getYear() : Calendar.getInstance().get(Calendar.YEAR);
        int month = selectedMonthYear != null ? selectedMonthYear.getMonth() : Calendar.getInstance().get(Calendar.MONTH) + 1;

        System.out.println("Analysis Type: " + analysisType + ", Selected Year-Month: " + year + "-" + month); // Debug output

        JPanel chartsPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            switch (analysisType) {
                case "Single Month Analysis":
                    displaySingleMonthAnalysis(year, month, chartsPanel);
                    break;
                case "Three Month Trend":
                    displayThreeMonthTrend(chartsPanel);
                    break;
                case "Quarterly Analysis":
                    displayQuarterlyAnalysis(chartsPanel);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying chart: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        dataDisplayPanel.add(chartsPanel);
        dataDisplayPanel.revalidate();
        dataDisplayPanel.repaint();
    }

    private void displaySingleMonthAnalysis(int year, int month, JPanel chartsPanel) {
        System.out.println("Starting single month analysis: " + year + "-" + month); // Debug output

        // Get current month expense data
        Map<String, Double> expenses = DataAdapter.getCategoryExpensesForMonth(year, month);

        System.out.println("Retrieved expense data: " + expenses); // Debug output

        if (expenses.isEmpty()) {
            showEmptyDataMessage(chartsPanel);
            return;
        }

        Map<String, Double> summary = DataAdapter.getMonthSummary(year, month);
        System.out.println("Monthly summary data: " + summary); // Debug output

        // Predict next month expenses
        Map<String, Double> prediction = DataAdapter.predictNextMonthExpenses(year, month);

        // Create charts
        JPanel pieChartPanel = ChartGenerator.createMonthlyExpensesPieChart(expenses,
                String.format("%d-%02d Expense Distribution", year, month)); // Changed to English
        JPanel predictionChartPanel = ChartGenerator.createExpensePredictionChart(expenses, prediction);

        // Add charts to panel
        chartsPanel.setLayout(new GridLayout(2, 1, 10, 10));
        chartsPanel.add(pieChartPanel);
        chartsPanel.add(predictionChartPanel);

        // Add summary information - Changed to English
        JLabel summaryLabel = new JLabel(String.format(
                "<html><h3>%d-%02d Summary</h3>" +
                        "Income: $%.2f<br>" +
                        "Total Expenses: $%.2f<br>" +
                        "Balance: $%.2f</html>",
                year, month,
                summary.get("income"),
                summary.get("expenses"),
                summary.get("balance")
        ));
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Changed font to Arial
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataDisplayPanel.add(summaryLabel);
    }

    private void displayThreeMonthTrend(JPanel chartsPanel) {
        // Get data for the last three months
        List<Map<String, Object>> monthsData = DataAdapter.getMultiMonthData(3);

        if (monthsData.isEmpty()) {
            showEmptyDataMessage(chartsPanel);
            return;
        }

        // Create trend chart
        JPanel trendChartPanel = ChartGenerator.createFinanceTrendChart(monthsData);
        chartsPanel.add(trendChartPanel);

        // Calculate average expenses
        double avgExpense = 0;
        for (Map<String, Object> monthData : monthsData) {
            Map<String, Double> summary = (Map<String, Double>) monthData.get("summary");
            avgExpense += summary.get("expenses");
        }
        avgExpense /= monthsData.size();

        // Add budget recommendation - Changed to English
        JLabel budgetLabel = new JLabel(String.format(
                "<html><h3>Budget Recommendations</h3>" +
                        "Three-month average expenses: $%.2f<br>" +
                        "Suggested next month budget: $%.2f<br>" +
                        "Consider reducing spending in categories with high expenses this quarter</html>",
                avgExpense,
                avgExpense * 0.95
        ));
        budgetLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Changed font to Arial
        budgetLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataDisplayPanel.add(budgetLabel);
    }

    private void displayQuarterlyAnalysis(JPanel chartsPanel) {
        // Get quarterly data (last 3 months)
        List<Map<String, Object>> monthsData = DataAdapter.getMultiMonthData(3);

        if (monthsData.isEmpty()) {
            showEmptyDataMessage(chartsPanel);
            return;
        }

        // Create quarterly distribution chart
        JPanel distributionChartPanel = ChartGenerator.createQuarterlyDistributionChart(monthsData);
        chartsPanel.add(distributionChartPanel);

        // Calculate quarterly total income and expenses
        double totalIncome = 0;
        double totalExpense = 0;

        for (Map<String, Object> monthData : monthsData) {
            Map<String, Double> summary = (Map<String, Double>) monthData.get("summary");
            totalIncome += summary.get("income");
            totalExpense += summary.get("expenses");
        }

        // Calculate savings goal completion - Changed to English
        double savingsTarget = totalIncome * 0.2; // Assume 20% as savings target
        double actualSavings = totalIncome - totalExpense;
        double savingsPercentage = savingsTarget > 0 ? (actualSavings / savingsTarget) * 100 : 0;

        JLabel savingsLabel = new JLabel(String.format(
                "<html><h3>Savings Performance</h3>" +
                        "Quarterly Total Income: $%.2f<br>" +
                        "Quarterly Total Expenses: $%.2f<br>" +
                        "Savings Target: $%.2f<br>" +
                        "Actual Savings: $%.2f<br>" +
                        "Target Achievement Rate: %.2f%%</html>",
                totalIncome,
                totalExpense,
                savingsTarget,
                actualSavings,
                savingsPercentage
        ));
        savingsLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Changed font to Arial
        savingsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dataDisplayPanel.add(savingsLabel);
    }

    private void showEmptyDataMessage(JPanel panel) {
        JLabel emptyLabel = new JLabel("Insufficient data to generate charts"); // Changed to English
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Changed font to Arial
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new BorderLayout());
        panel.add(emptyLabel, BorderLayout.CENTER);
    }
}
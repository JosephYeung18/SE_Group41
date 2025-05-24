package com.financemanager.view;

import com.financemanager.model.SavingsPlan;
import com.financemanager.controller.SavingsPlanController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.DecimalFormat;

/**
 * Savings Plan Panel
 */
public class SavingsPlanPanel extends JPanel {

    private SavingsPlanController savingsPlanController;

    private JList<SavingsPlan> planList;
    private DefaultListModel<SavingsPlan> planListModel;

    private JLabel planNameLabel;
    private JLabel targetAmountLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel depositedLabel;
    private JLabel remainingLabel;
    private JProgressBar progressBar;

    private JButton addPlanButton;
    private JButton editPlanButton;
    private JButton deletePlanButton;
    private JButton depositButton;
    private JButton withdrawButton;

    private JTabbedPane tabbedPane;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    public SavingsPlanPanel() {
        savingsPlanController = new SavingsPlanController();

        initComponents();
        setupLayout();
        setupActionListeners();
        refreshData();
    }

    private void initComponents() {
        // Plan list
        planListModel = new DefaultListModel<>();
        planList = new JList<>(planListModel);
        planList.setCellRenderer(new SavingsPlanListCellRenderer());
        planList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Plan detail labels
        planNameLabel = new JLabel();
        targetAmountLabel = new JLabel();
        startDateLabel = new JLabel();
        endDateLabel = new JLabel();
        depositedLabel = new JLabel();
        remainingLabel = new JLabel();

        // Create optimized progress bar
        progressBar = createStyledSavingsProgressBar();

        // Buttons
        addPlanButton = new JButton("New Plan");
        editPlanButton = new JButton("Edit Plan");
        deletePlanButton = new JButton("Delete Plan");
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");

        // Disable edit and delete buttons until a plan is selected
        editPlanButton.setEnabled(false);
        deletePlanButton.setEnabled(false);
        depositButton.setEnabled(false);
        withdrawButton.setEnabled(false);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
    }

    /**
     * Create styled savings progress bar
     */
    private JProgressBar createStyledSavingsProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, 100);

        // Set progress bar size - thinner height
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        // Set style
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);

        // Set font
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));

        // Custom progress bar appearance
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = c.getWidth();
                int height = c.getHeight();

                // Draw background (rounded rectangle)
                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRoundRect(0, 0, width, height, 10, 10);

                // Draw progress (rounded rectangle with gradient effect)
                if (progressBar.getValue() > 0) {
                    int progressWidth = (int) ((double) progressBar.getValue() / progressBar.getMaximum() * width);

                    // Create gradient colors
                    Color startColor, endColor;
                    if (progressBar.getValue() >= 100) {
                        // Completed state - gold gradient
                        startColor = new Color(255, 215, 0);
                        endColor = new Color(255, 165, 0);
                    } else if (progressBar.getValue() >= 75) {
                        // Near completion - blue-green gradient
                        startColor = new Color(0, 191, 255);
                        endColor = new Color(30, 144, 255);
                    } else {
                        // In progress - green gradient
                        startColor = new Color(144, 238, 144);
                        endColor = new Color(50, 205, 50);
                    }

                    GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(0, 0, progressWidth, height, 10, 10);

                    // Add gloss effect
                    g2d.setColor(new Color(255, 255, 255, 60));
                    g2d.fillRoundRect(0, 0, progressWidth, height / 2, 10, 10);
                }

                // Draw border
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);

                // Draw percentage text (with shadow effect)
                if (progressBar.isStringPainted()) {
                    String text = progressBar.getValue() + "%";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getHeight();
                    int textX = (width - textWidth) / 2;
                    int textY = (height + textHeight / 2) / 2 - 2;

                    // Draw text shadow
                    g2d.setColor(new Color(0, 0, 0, 80));
                    g2d.drawString(text, textX + 1, textY + 1);

                    // Draw text
                    g2d.setColor(progressBar.getValue() > 50 ? Color.WHITE : new Color(60, 60, 60));
                    g2d.drawString(text, textX, textY);
                }

                g2d.dispose();
            }
        });

        return progressBar;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Savings Plans");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // Plan list panel
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Savings Plan List"));

        JScrollPane scrollPane = new JScrollPane(planList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPanel listButtonPanel = new JPanel();
        listButtonPanel.add(addPlanButton);
        listButtonPanel.add(editPlanButton);
        listButtonPanel.add(deletePlanButton);

        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(listButtonPanel, BorderLayout.SOUTH);

        // Plan details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Plan Details"));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Plan name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameTitle = new JLabel("Plan Name:");
        nameTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(nameTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        planNameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(planNameLabel, gbc);

        // Target amount
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel targetTitle = new JLabel("Target Amount:");
        targetTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(targetTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        targetAmountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        targetAmountLabel.setForeground(new Color(0, 120, 200));
        infoPanel.add(targetAmountLabel, gbc);

        // Date information
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel startTitle = new JLabel("Start Date:");
        startTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(startTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        startDateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(startDateLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel endTitle = new JLabel("End Date:");
        endTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(endTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        endDateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(endDateLabel, gbc);

        // Deposited amount
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel depositedTitle = new JLabel("Deposited Amount:");
        depositedTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(depositedTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        depositedLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        depositedLabel.setForeground(new Color(0, 150, 0));
        infoPanel.add(depositedLabel, gbc);

        // Remaining amount
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel remainingTitle = new JLabel("Remaining Amount:");
        remainingTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(remainingTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        remainingLabel.setForeground(new Color(200, 100, 0));
        infoPanel.add(remainingLabel, gbc);

        // Progress bar
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel progressTitle = new JLabel("Progress:");
        progressTitle.setFont(new Font("Arial", Font.BOLD, 13));
        infoPanel.add(progressTitle, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        infoPanel.add(progressBar, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);

        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        detailsPanel.add(actionPanel, BorderLayout.SOUTH);

        // Create two tabs: Active and Completed
        JPanel activePanel = new JPanel(new BorderLayout(10, 10));
        activePanel.add(listPanel, BorderLayout.WEST);
        activePanel.add(detailsPanel, BorderLayout.CENTER);

        // Completed plans panel
        JPanel completedPanel = createCompletedPlansPanel();

        tabbedPane.addTab("Active", activePanel);
        tabbedPane.addTab("Completed", completedPanel);

        // Add tab change listener
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                // Refresh data when switching to completed tab
                refreshCompletedPlansPanel();
            } else {
                // Refresh data when switching to active tab
                refreshData();
            }
        });

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Create completed plans panel
     */
    private JPanel createCompletedPlansPanel() {
        JPanel completedPanel = new JPanel(new BorderLayout());

        // Completed plans list
        DefaultListModel<SavingsPlan> completedListModel = new DefaultListModel<>();
        JList<SavingsPlan> completedPlansList = new JList<>(completedListModel);
        completedPlansList.setCellRenderer(new CompletedPlanListCellRenderer());
        completedPlansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane completedScrollPane = new JScrollPane(completedPlansList);
        completedScrollPane.setPreferredSize(new Dimension(300, 200));

        // Completed plan details panel
        JPanel completedDetailsPanel = new JPanel(new BorderLayout());
        completedDetailsPanel.setBorder(BorderFactory.createTitledBorder("Completed Plan Details"));

        JPanel completedInfoPanel = new JPanel(new GridBagLayout());
        completedInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Detail labels
        JLabel completedNameLabel = new JLabel();
        JLabel completedTargetLabel = new JLabel();
        JLabel completedStartLabel = new JLabel();
        JLabel completedEndLabel = new JLabel();
        JLabel completedDepositedLabel = new JLabel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Add detail fields
        gbc.gridx = 0; gbc.gridy = 0;
        completedInfoPanel.add(new JLabel("Plan Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        completedInfoPanel.add(completedNameLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        completedInfoPanel.add(new JLabel("Target Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        completedInfoPanel.add(completedTargetLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        completedInfoPanel.add(new JLabel("Actual Savings:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        completedInfoPanel.add(completedDepositedLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        completedInfoPanel.add(new JLabel("Start Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        completedInfoPanel.add(completedStartLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        completedInfoPanel.add(new JLabel("End Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        completedInfoPanel.add(completedEndLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        completedInfoPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JLabel statusLabel = new JLabel("✓ Completed");
        statusLabel.setForeground(new Color(0, 150, 0));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        completedInfoPanel.add(statusLabel, gbc);

        completedDetailsPanel.add(completedInfoPanel, BorderLayout.CENTER);

        // Left-right layout
        JPanel completedLeftPanel = new JPanel(new BorderLayout());
        completedLeftPanel.setBorder(BorderFactory.createTitledBorder("Completed Savings Plans"));
        completedLeftPanel.add(completedScrollPane, BorderLayout.CENTER);

        completedPanel.add(completedLeftPanel, BorderLayout.WEST);
        completedPanel.add(completedDetailsPanel, BorderLayout.CENTER);

        // Add list selection listener
        completedPlansList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SavingsPlan selectedPlan = completedPlansList.getSelectedValue();
                if (selectedPlan != null) {
                    // Update completed plan details
                    completedNameLabel.setText(selectedPlan.getName());
                    completedTargetLabel.setText("$ " + moneyFormat.format(selectedPlan.getTargetAmount()));
                    completedDepositedLabel.setText("$ " + moneyFormat.format(selectedPlan.getDepositedAmount()));
                    completedStartLabel.setText(dateFormat.format(selectedPlan.getStartDate()));
                    completedEndLabel.setText(dateFormat.format(selectedPlan.getEndDate()));
                } else {
                    // Clear details
                    completedNameLabel.setText("");
                    completedTargetLabel.setText("");
                    completedDepositedLabel.setText("");
                    completedStartLabel.setText("");
                    completedEndLabel.setText("");
                }
            }
        });

        // Save references for later refresh
        this.completedPlansList = completedPlansList;
        this.completedListModel = completedListModel;

        return completedPanel;
    }

    /**
     * Refresh completed plans panel
     */
    private void refreshCompletedPlansPanel() {
        try {
            completedListModel.clear();
            List<SavingsPlan> completedPlans = savingsPlanController.getCompletedPlans();

            for (SavingsPlan plan : completedPlans) {
                completedListModel.addElement(plan);
            }

            if (completedPlans.isEmpty()) {
                // If no completed plans, could add a hint
                JLabel emptyLabel = new JLabel("No completed savings plans yet", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                emptyLabel.setForeground(Color.GRAY);
            }
        } catch (Exception e) {
            System.err.println("Failed to refresh completed plans: " + e.getMessage());
        }
    }

    // Need to add these member variables at the beginning of the class
    private JList<SavingsPlan> completedPlansList;
    private DefaultListModel<SavingsPlan> completedListModel;

    /**
     * Custom list renderer for completed plans
     */
    private class CompletedPlanListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof SavingsPlan) {
                SavingsPlan plan = (SavingsPlan) value;
                setText("✓ " + plan.getName() + " - $" + moneyFormat.format(plan.getTargetAmount()));
                setForeground(isSelected ? Color.WHITE : new Color(0, 150, 0)); // Green for completed
            }

            return this;
        }
    }

    private void setupActionListeners() {
        // Plan list selection listener
        planList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    SavingsPlan selectedPlan = planList.getSelectedValue();

                    if (selectedPlan != null) {
                        editPlanButton.setEnabled(true);
                        deletePlanButton.setEnabled(true);
                        depositButton.setEnabled(true);
                        withdrawButton.setEnabled(true);

                        updatePlanDetails(selectedPlan);
                    } else {
                        editPlanButton.setEnabled(false);
                        deletePlanButton.setEnabled(false);
                        depositButton.setEnabled(false);
                        withdrawButton.setEnabled(false);

                        clearPlanDetails();
                    }
                }
            }
        });

        // Add plan button
        addPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPlanDialog();
            }
        });

        // Edit plan button
        editPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    showEditPlanDialog(selectedPlan);
                }
            }
        });

        // Delete plan button
        deletePlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    int response = JOptionPane.showConfirmDialog(
                            SavingsPlanPanel.this,
                            "Are you sure you want to delete '" + selectedPlan.getName() + "' plan?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        savingsPlanController.deletePlan(selectedPlan.getId());
                        refreshData();
                        clearPlanDetails();
                    }
                }
            }
        });

        // Deposit button
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    showDepositDialog(selectedPlan);
                }
            }
        });

        // Withdraw button
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    showWithdrawDialog(selectedPlan);
                }
            }
        });
    }

    private void refreshData() {
        try {
            planListModel.clear();
            List<SavingsPlan> plans = savingsPlanController.getActivePlans(); // Only show active plans
            for (SavingsPlan plan : plans) {
                planListModel.addElement(plan);
            }
        } catch (Exception e) {
            System.err.println("Failed to refresh savings plan list: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load savings plans: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePlanDetails(SavingsPlan plan) {
        planNameLabel.setText(plan.getName());
        targetAmountLabel.setText("$ " + moneyFormat.format(plan.getTargetAmount()));
        startDateLabel.setText(dateFormat.format(plan.getStartDate()));
        endDateLabel.setText(dateFormat.format(plan.getEndDate()));
        depositedLabel.setText("$ " + moneyFormat.format(plan.getDepositedAmount()));

        double remaining = plan.getTargetAmount() - plan.getDepositedAmount();
        remainingLabel.setText("$ " + moneyFormat.format(remaining));

        // Calculate progress percentage
        int progress = (int) ((plan.getDepositedAmount() / plan.getTargetAmount()) * 100);
        progressBar.setValue(Math.min(progress, 100));
        progressBar.setString(progress + "%");

        // Progress bar color will automatically adjust based on progress value in custom UI
    }

    private void clearPlanDetails() {
        planNameLabel.setText("");
        targetAmountLabel.setText("");
        startDateLabel.setText("");
        endDateLabel.setText("");
        depositedLabel.setText("");
        remainingLabel.setText("");
        progressBar.setValue(0);
        progressBar.setString("0%");
    }

    private void showAddPlanDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Savings Plan", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(20);
        JTextField targetAmountField = new JTextField(20);

        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);
        startDateSpinner.setValue(new Date());

        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);

        // Default end date is one year later
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        endDateSpinner.setValue(calendar.getTime());

        formPanel.add(new JLabel("Plan Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Target Amount:"));
        formPanel.add(targetAmountField);

        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateSpinner);

        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateSpinner);

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
                        JOptionPane.showMessageDialog(dialog, "Please enter plan name", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String targetAmountText = targetAmountField.getText().trim();
                    if (targetAmountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter target amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double targetAmount = Double.parseDouble(targetAmountText);
                    if (targetAmount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Target amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Date startDate = (Date) startDateSpinner.getValue();
                    Date endDate = (Date) endDateSpinner.getValue();

                    if (endDate.before(startDate)) {
                        JOptionPane.showMessageDialog(dialog, "End date cannot be earlier than start date", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create new savings plan
                    SavingsPlan plan = new SavingsPlan();
                    plan.setName(name);
                    plan.setTargetAmount(targetAmount);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDepositedAmount(0); // Initial deposited amount is 0

                    savingsPlanController.addPlan(plan);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid target amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to add plan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showEditPlanDialog(SavingsPlan plan) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Savings Plan", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(plan.getName(), 20);
        JTextField targetAmountField = new JTextField(String.valueOf(plan.getTargetAmount()), 20);

        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startDateEditor);
        startDateSpinner.setValue(plan.getStartDate());

        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endDateEditor);
        endDateSpinner.setValue(plan.getEndDate());

        formPanel.add(new JLabel("Plan Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Target Amount:"));
        formPanel.add(targetAmountField);

        formPanel.add(new JLabel("Start Date:"));
        formPanel.add(startDateSpinner);

        formPanel.add(new JLabel("End Date:"));
        formPanel.add(endDateSpinner);

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
                        JOptionPane.showMessageDialog(dialog, "Please enter plan name", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String targetAmountText = targetAmountField.getText().trim();
                    if (targetAmountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter target amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double targetAmount = Double.parseDouble(targetAmountText);
                    if (targetAmount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Target amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Date startDate = (Date) startDateSpinner.getValue();
                    Date endDate = (Date) endDateSpinner.getValue();

                    if (endDate.before(startDate)) {
                        JOptionPane.showMessageDialog(dialog, "End date cannot be earlier than start date", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update savings plan
                    plan.setName(name);
                    plan.setTargetAmount(targetAmount);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);

                    savingsPlanController.updatePlan(plan);
                    refreshData();
                    updatePlanDetails(plan);
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid target amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to update plan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showDepositDialog(SavingsPlan plan) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Deposit", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField amountField = new JTextField(20);
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);

        formPanel.add(new JLabel("Plan Name:"));
        formPanel.add(new JLabel(plan.getName()));

        formPanel.add(new JLabel("Deposit Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Notes:"));
        formPanel.add(commentScrollPane);

        JPanel buttonPanel = new JPanel();
        JButton depositButton = new JButton("Deposit");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(depositButton);
        buttonPanel.add(cancelButton);

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter deposit amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Deposit amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String comment = commentArea.getText().trim();

                    // Execute deposit operation
                    savingsPlanController.deposit(plan.getId(), amount, comment);

                    // Reload data and update details
                    refreshData();

                    // Re-select plan to update details
                    for (int i = 0; i < planListModel.getSize(); i++) {
                        SavingsPlan currentPlan = planListModel.getElementAt(i);
                        if (currentPlan.getId().equals(plan.getId())) {
                            planList.setSelectedIndex(i);
                            break;
                        }
                    }

                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Deposit failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void showWithdrawDialog(SavingsPlan plan) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Withdraw", true);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField amountField = new JTextField(20);
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);

        formPanel.add(new JLabel("Plan Name:"));
        formPanel.add(new JLabel(plan.getName()));

        formPanel.add(new JLabel("Deposited Amount:"));
        formPanel.add(new JLabel("$ " + moneyFormat.format(plan.getDepositedAmount())));

        formPanel.add(new JLabel("Withdraw Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Notes:"));
        formPanel.add(commentScrollPane);

        JPanel buttonPanel = new JPanel();
        JButton withdrawButton = new JButton("Withdraw");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter withdraw amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Withdraw amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (amount > plan.getDepositedAmount()) {
                        JOptionPane.showMessageDialog(dialog, "Withdraw amount cannot exceed deposited amount", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String comment = commentArea.getText().trim();

                    // Execute withdraw operation
                    savingsPlanController.withdraw(plan.getId(), amount, comment);

                    // Reload data and update details
                    refreshData();

                    // Re-select plan to update details
                    for (int i = 0; i < planListModel.getSize(); i++) {
                        SavingsPlan currentPlan = planListModel.getElementAt(i);
                        if (currentPlan.getId().equals(plan.getId())) {
                            planList.setSelectedIndex(i);
                            break;
                        }
                    }

                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Withdraw failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    // Custom list renderer
    private class SavingsPlanListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof SavingsPlan) {
                SavingsPlan plan = (SavingsPlan) value;
                setText(plan.getName() + " - $" + moneyFormat.format(plan.getTargetAmount()));

                // If plan is completed, use different color
                if (plan.getDepositedAmount() >= plan.getTargetAmount()) {
                    setForeground(new Color(255, 165, 0)); // Gold color
                }
            }

            return this;
        }
    }
}
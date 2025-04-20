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
 * 储蓄计划面板
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
        // 计划列表
        planListModel = new DefaultListModel<>();
        planList = new JList<>(planListModel);
        planList.setCellRenderer(new SavingsPlanListCellRenderer());
        planList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 计划详情标签
        planNameLabel = new JLabel();
        targetAmountLabel = new JLabel();
        startDateLabel = new JLabel();
        endDateLabel = new JLabel();
        depositedLabel = new JLabel();
        remainingLabel = new JLabel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        
        // 按钮
        addPlanButton = new JButton("Add Plan");
        editPlanButton = new JButton("Edit Plan");
        deletePlanButton = new JButton("Delete Plan");
        depositButton = new JButton("Save");
        withdrawButton = new JButton("Withdraw");
        
        // 禁用编辑和删除按钮，直到选择一个计划
        editPlanButton.setEnabled(false);
        deletePlanButton.setEnabled(false);
        depositButton.setEnabled(false);
        withdrawButton.setEnabled(false);
        
        // 标签页
        tabbedPane = new JTabbedPane();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 设置标题
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Saving Plan");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // 计划列表面板
      // SavingsPlanPanel.java (续)
        // 计划列表面板
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Saving List"));
        
        JScrollPane scrollPane = new JScrollPane(planList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        
        JPanel listButtonPanel = new JPanel();
        listButtonPanel.add(addPlanButton);
        listButtonPanel.add(editPlanButton);
        listButtonPanel.add(deletePlanButton);
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        listPanel.add(listButtonPanel, BorderLayout.SOUTH);
        
        // 计划详情面板
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        
        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoPanel.add(new JLabel("Saving for:"));
        infoPanel.add(planNameLabel);
        
        infoPanel.add(new JLabel("Money:"));
        infoPanel.add(targetAmountLabel);
        
        infoPanel.add(new JLabel("Start:"));
        infoPanel.add(startDateLabel);
        
        infoPanel.add(new JLabel("End:"));
        infoPanel.add(endDateLabel);
        
        infoPanel.add(new JLabel("Saved:"));
        infoPanel.add(depositedLabel);
        
        infoPanel.add(new JLabel("Left:"));
        infoPanel.add(remainingLabel);
        
        infoPanel.add(new JLabel("Process:"));
        infoPanel.add(progressBar);
        
        JPanel actionPanel = new JPanel();
        actionPanel.add(depositButton);
        actionPanel.add(withdrawButton);
        
        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        detailsPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // 创建两个标签页：进行中和已完成
        JPanel activePanel = new JPanel(new BorderLayout(10, 10));
        activePanel.add(listPanel, BorderLayout.WEST);
        activePanel.add(detailsPanel, BorderLayout.CENTER);
        
        JPanel completedPanel = new JPanel(new BorderLayout());
        completedPanel.add(new JLabel("已完成的储蓄计划将显示在这里", SwingConstants.CENTER), BorderLayout.CENTER);
        
        tabbedPane.addTab("Ongoing", activePanel);
        tabbedPane.addTab("Done", completedPanel);
        
        // 主布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupActionListeners() {
        // 计划列表选择监听器
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
        
        // 添加计划按钮
        addPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPlanDialog();
            }
        });
        
        // 编辑计划按钮
        editPlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    showEditPlanDialog(selectedPlan);
                }
            }
        });
        
        // 删除计划按钮
        deletePlanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    int response = JOptionPane.showConfirmDialog(
                            SavingsPlanPanel.this,
                            "确定要删除 '" + selectedPlan.getName() + "' 计划吗?",
                            "确认删除",
                            JOptionPane.YES_NO_OPTION);
                    
                    if (response == JOptionPane.YES_OPTION) {
                        savingsPlanController.deletePlan(selectedPlan.getId());
                        refreshData();
                        clearPlanDetails();
                    }
                }
            }
        });
        
        // 存款按钮
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SavingsPlan selectedPlan = planList.getSelectedValue();
                if (selectedPlan != null) {
                    showDepositDialog(selectedPlan);
                }
            }
        });
        
        // 取款按钮
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
        // 清空列表并重新加载数据
        planListModel.clear();
        
        List<SavingsPlan> plans = savingsPlanController.getAllPlans();
        for (SavingsPlan plan : plans) {
            planListModel.addElement(plan);
        }
    }
    
    private void updatePlanDetails(SavingsPlan plan) {
        planNameLabel.setText(plan.getName());
        targetAmountLabel.setText("¥ " + moneyFormat.format(plan.getTargetAmount()));
        startDateLabel.setText(dateFormat.format(plan.getStartDate()));
        endDateLabel.setText(dateFormat.format(plan.getEndDate()));
        depositedLabel.setText("¥ " + moneyFormat.format(plan.getDepositedAmount()));
        
        double remaining = plan.getTargetAmount() - plan.getDepositedAmount();
        remainingLabel.setText("¥ " + moneyFormat.format(remaining));
        
        // 计算进度百分比
        int progress = (int) ((plan.getDepositedAmount() / plan.getTargetAmount()) * 100);
        progressBar.setValue(progress);
        progressBar.setString(progress + "%");
        
        // 如果已完成，改变进度条颜色
        if (progress >= 100) {
            progressBar.setForeground(new Color(0, 150, 0)); // 绿色
        } else {
            progressBar.setForeground(new Color(0, 120, 200)); // 蓝色
        }
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "新建储蓄计划", true);
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
        
        // 默认结束日期为一年后
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        endDateSpinner.setValue(calendar.getTime());
        
        formPanel.add(new JLabel("计划名称:"));
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("目标金额:"));
        formPanel.add(targetAmountField);
        
        formPanel.add(new JLabel("开始日期:"));
        formPanel.add(startDateSpinner);
        
        formPanel.add(new JLabel("结束日期:"));
        formPanel.add(endDateSpinner);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入计划名称", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    String targetAmountText = targetAmountField.getText().trim();
                    if (targetAmountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入目标金额", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    double targetAmount = Double.parseDouble(targetAmountText);
                    if (targetAmount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "目标金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Date startDate = (Date) startDateSpinner.getValue();
                    Date endDate = (Date) endDateSpinner.getValue();
                    
                    if (endDate.before(startDate)) {
                        JOptionPane.showMessageDialog(dialog, "结束日期不能早于开始日期", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // 创建新的储蓄计划
                    SavingsPlan plan = new SavingsPlan();
                    plan.setName(name);
                    plan.setTargetAmount(targetAmount);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDepositedAmount(0); // 初始已存金额为0
                    
                    savingsPlanController.addPlan(plan);
                    refreshData();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "目标金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "添加计划失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "编辑储蓄计划", true);
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
        
        formPanel.add(new JLabel("计划名称:"));
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("目标金额:"));
        formPanel.add(targetAmountField);
        
        formPanel.add(new JLabel("开始日期:"));
        formPanel.add(startDateSpinner);
        
        formPanel.add(new JLabel("结束日期:"));
        formPanel.add(endDateSpinner);
        
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText().trim();
                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入计划名称", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    String targetAmountText = targetAmountField.getText().trim();
                    if (targetAmountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入目标金额", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    double targetAmount = Double.parseDouble(targetAmountText);
                    if (targetAmount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "目标金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Date startDate = (Date) startDateSpinner.getValue();
                    Date endDate = (Date) endDateSpinner.getValue();
                    
                    if (endDate.before(startDate)) {
                        JOptionPane.showMessageDialog(dialog, "结束日期不能早于开始日期", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // 更新储蓄计划
                    plan.setName(name);
                    plan.setTargetAmount(targetAmount);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    
                    savingsPlanController.updatePlan(plan);
                    refreshData();
                    updatePlanDetails(plan);
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "目标金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "更新计划失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "存款", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField(20);
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        
        formPanel.add(new JLabel("计划名称:"));
        formPanel.add(new JLabel(plan.getName()));
        
        formPanel.add(new JLabel("存款金额:"));
        formPanel.add(amountField);
        
        formPanel.add(new JLabel("备注:"));
        formPanel.add(commentScrollPane);
        
        JPanel buttonPanel = new JPanel();
        JButton depositButton = new JButton("存款");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(depositButton);
        buttonPanel.add(cancelButton);
        
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入存款金额", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "存款金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    String comment = commentArea.getText().trim();
                    
                    // 执行存款操作
                    savingsPlanController.deposit(plan.getId(), amount, comment);
                    
                    // 重新加载数据并更新详情
                    refreshData();
                    
                    // 重新选择计划以更新详情
                    for (int i = 0; i < planListModel.getSize(); i++) {
                        SavingsPlan currentPlan = planListModel.getElementAt(i);
                        if (currentPlan.getId().equals(plan.getId())) {
                            planList.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "存款失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "取款", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField amountField = new JTextField(20);
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        
        formPanel.add(new JLabel("计划名称:"));
        formPanel.add(new JLabel(plan.getName()));
        
        formPanel.add(new JLabel("已存金额:"));
        formPanel.add(new JLabel("¥ " + moneyFormat.format(plan.getDepositedAmount())));
        
        formPanel.add(new JLabel("取款金额:"));
        formPanel.add(amountField);
        
        formPanel.add(new JLabel("备注:"));
        formPanel.add(commentScrollPane);
        
        JPanel buttonPanel = new JPanel();
        JButton withdrawButton = new JButton("取款");
        JButton cancelButton = new JButton("取消");
        
        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);
        
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String amountText = amountField.getText().trim();
                    if (amountText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "请输入取款金额", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(dialog, "取款金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (amount > plan.getDepositedAmount()) {
                        JOptionPane.showMessageDialog(dialog, "取款金额不能大于已存金额", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    String comment = commentArea.getText().trim();
                    
                    // 执行取款操作
                    savingsPlanController.withdraw(plan.getId(), amount, comment);
                    
                    // 重新加载数据并更新详情
                    refreshData();
                    
                    // 重新选择计划以更新详情
                    for (int i = 0; i < planListModel.getSize(); i++) {
                        SavingsPlan currentPlan = planListModel.getElementAt(i);
                        if (currentPlan.getId().equals(plan.getId())) {
                            planList.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "金额格式不正确", "错误", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "取款失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
    
    // 自定义列表渲染器
    private class SavingsPlanListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof SavingsPlan) {
                SavingsPlan plan = (SavingsPlan) value;
                setText(plan.getName() + " - ¥" + moneyFormat.format(plan.getTargetAmount()));
                
                // 如果计划已经完成，使用不同的颜色
                if (plan.getDepositedAmount() >= plan.getTargetAmount()) {
                    setForeground(new Color(0, 150, 0)); // 绿色
                }
            }
            
            return this;
        }
    }
}
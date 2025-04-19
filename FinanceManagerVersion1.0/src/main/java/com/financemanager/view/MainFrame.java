package com.financemanager.view;

import com.financemanager.view.*;
import com.financemanager.controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * 应用程序主窗口
 */
public class MainFrame extends JFrame {
    
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    
    private AIConversationPanel aiConversationPanel;
    private AssetBudgetPanel assetBudgetPanel;
    private TransactionDetailsPanel transactionDetailsPanel;
    private TransactionEntryPanel transactionEntryPanel;
    private SavingsPlanPanel savingsPlanPanel;
    private UserSettingPanel userSettingPanel;
    
    private CardLayout cardLayout;
    
    public MainFrame() {
        initComponents();
        setupLayout();
        setupActionListeners();
        
        // 为TransactionEntryPanel设置MainFrame引用
        transactionEntryPanel.setMainFrame(this);
        
        setTitle("智能个人理财管理器");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initComponents() {
        contentPanel = new JPanel();
        sidebarPanel = new JPanel();
        mainContentPanel = new JPanel();
        
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);
        
        aiConversationPanel = new AIConversationPanel();
        assetBudgetPanel = new AssetBudgetPanel();
        transactionDetailsPanel = new TransactionDetailsPanel();
        transactionEntryPanel = new TransactionEntryPanel();
        savingsPlanPanel = new SavingsPlanPanel();
        userSettingPanel = new UserSettingPanel();
        
        // 将各面板添加到卡片布局
        mainContentPanel.add(aiConversationPanel, "AI");
        mainContentPanel.add(assetBudgetPanel, "资产");
        mainContentPanel.add(transactionDetailsPanel, "交易");
        mainContentPanel.add(transactionEntryPanel, "记账");
        mainContentPanel.add(savingsPlanPanel, "储蓄");
        mainContentPanel.add(userSettingPanel, "设置");
    }
    
    private void setupLayout() {
        contentPanel.setLayout(new BorderLayout());
        
        // 设置侧边栏
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(new Color(50, 50, 50));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        
        // 添加应用程序图标
        JLabel logoLabel = new JLabel("财务管理");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebarPanel.add(logoLabel);
        
        // 添加侧边栏按钮
        sidebarPanel.add(createSidebarButton("AI对话", "AI"));
        sidebarPanel.add(createSidebarButton("资产预算", "资产"));
        sidebarPanel.add(createSidebarButton("交易详情", "交易"));
        sidebarPanel.add(createSidebarButton("记账", "记账"));
        sidebarPanel.add(createSidebarButton("储蓄计划", "储蓄"));
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(createSidebarButton("设置", "设置"));
        
        contentPanel.add(sidebarPanel, BorderLayout.WEST);
        contentPanel.add(mainContentPanel, BorderLayout.CENTER);
        
        setContentPane(contentPanel);
    }
    
    private JButton createSidebarButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 70));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(180, 45));
        
        return button;
    }
    
    private void setupActionListeners() {
        // 为侧边栏按钮添加监听器
        for (Component component : sidebarPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.addActionListener(e -> {
                    cardLayout.show(mainContentPanel, e.getActionCommand());
                    highlightSelectedButton(button);
                });
            }
        }
    }
    
    private void highlightSelectedButton(JButton selectedButton) {
        // 重置所有按钮颜色
        for (Component component : sidebarPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(new Color(70, 70, 70));
            }
        }
        
        // 高亮选中的按钮
        selectedButton.setBackground(new Color(120, 120, 200));
    }
    
    /**
     * 刷新交易详情面板
     * 当添加或导入新交易后调用此方法
     */
    public void refreshTransactionDetailsPanel() {
        if (transactionDetailsPanel != null) {
            transactionDetailsPanel.refreshData();
        }
    }
    
    /**
     * 刷新资产预算面板
     * 当添加或导入新交易后调用此方法
     */
    public void refreshAssetBudgetPanel() {
        if (assetBudgetPanel != null) {
            assetBudgetPanel.refreshData();
        }
    }
}
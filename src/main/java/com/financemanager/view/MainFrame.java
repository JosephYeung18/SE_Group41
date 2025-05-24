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
 * Main application window
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

    private AnalysisPanel analysisPanel;

    private CardLayout cardLayout;

    public MainFrame() {
        initComponents();
        setupLayout();
        setupActionListeners();

        // Set MainFrame reference for TransactionEntryPanel
        transactionEntryPanel.setMainFrame(this);

        setTitle("Smart Personal Finance Manager");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {

        analysisPanel = new AnalysisPanel();

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

        // Add panels to card layout
        mainContentPanel.add(aiConversationPanel, "AI");
        mainContentPanel.add(assetBudgetPanel, "Assets");
        mainContentPanel.add(transactionDetailsPanel, "Transactions");
        mainContentPanel.add(transactionEntryPanel, "Entry");
        mainContentPanel.add(savingsPlanPanel, "Savings");
        mainContentPanel.add(userSettingPanel, "Settings");

        mainContentPanel.add(analysisPanel, "Analysis");
    }

    private void setupLayout() {
        contentPanel.setLayout(new BorderLayout());

        // Set sidebar
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(new Color(50, 50, 50));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        // Add application logo
        JLabel logoLabel = new JLabel("Finance Manager");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebarPanel.add(logoLabel);

        // Add sidebar buttons
        sidebarPanel.add(createSidebarButton("AI Chat", "AI"));
        sidebarPanel.add(createSidebarButton("Assets & Budget", "Assets"));
        sidebarPanel.add(createSidebarButton("Transaction Details", "Transactions"));
        sidebarPanel.add(createSidebarButton("Add Transaction", "Entry"));
        sidebarPanel.add(createSidebarButton("Savings Plan", "Savings"));
        sidebarPanel.add(createSidebarButton("Data Analysis", "Analysis"));
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(createSidebarButton("Settings", "Settings"));

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
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setPreferredSize(new Dimension(180, 45));

        return button;
    }

    private void setupActionListeners() {
        // Add listeners for sidebar buttons
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
        // Reset all button colors
        for (Component component : sidebarPanel.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(new Color(70, 70, 70));
            }
        }

        // Highlight selected button
        selectedButton.setBackground(new Color(120, 120, 200));
    }

    /**
     * Refresh transaction details panel
     * Call this method after adding or importing new transactions
     */
    public void refreshTransactionDetailsPanel() {
        if (transactionDetailsPanel != null) {
            transactionDetailsPanel.refreshData();
        }
    }

    /**
     * Refresh asset budget panel
     * Call this method after adding or importing new transactions
     */
    public void refreshAssetBudgetPanel() {
        if (assetBudgetPanel != null) {
            assetBudgetPanel.refreshData();
        }
    }
}
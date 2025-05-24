package com.financemanager.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * User Settings Panel
 */
public class UserSettingPanel extends JPanel {

    private JTextField userNameField;
    private JButton saveButton;
    private JButton resetButton;

    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;

    public UserSettingPanel() {
        properties = new Properties();
        loadProperties();

        initComponents();
        setupLayout();
        setupActionListeners();
    }

    private void initComponents() {
        // User information settings
        userNameField = new JTextField(20);
        userNameField.setText(properties.getProperty("user.name", ""));

        // Buttons
        saveButton = new JButton("Save Settings");
        resetButton = new JButton("Reset Settings");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Set title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        // User settings panel
        JPanel userSettingsPanel = new JPanel(new GridBagLayout());
        userSettingsPanel.setBorder(BorderFactory.createTitledBorder("User Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        userSettingsPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        userSettingsPanel.add(userNameField, gbc);

        // Application information panel
        JPanel appInfoPanel = new JPanel(new GridBagLayout());
        appInfoPanel.setBorder(BorderFactory.createTitledBorder("Application Information"));

        GridBagConstraints infoGbc = new GridBagConstraints();
        infoGbc.insets = new Insets(10, 10, 10, 10);
        infoGbc.anchor = GridBagConstraints.WEST;

        infoGbc.gridx = 0;
        infoGbc.gridy = 0;
        appInfoPanel.add(new JLabel("Application Name:"), infoGbc);

        infoGbc.gridx = 1;
        infoGbc.gridy = 0;
        appInfoPanel.add(new JLabel("Smart Personal Finance Manager"), infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 1;
        appInfoPanel.add(new JLabel("Version:"), infoGbc);

        infoGbc.gridx = 1;
        infoGbc.gridy = 1;
        appInfoPanel.add(new JLabel("2.0.0"), infoGbc);

        infoGbc.gridx = 0;
        infoGbc.gridy = 2;
        appInfoPanel.add(new JLabel("Data Storage:"), infoGbc);

        infoGbc.gridx = 1;
        infoGbc.gridy = 2;
        appInfoPanel.add(new JLabel("Local JSON Files"), infoGbc);

        // User guide panel
        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder("User Guide"));

        JTextArea helpText = new JTextArea(
                "1. Add Transaction: Add income and expense records in the transaction entry panel\n" +
                        "2. Transaction Details: View all transaction records and statistics\n" +
                        "3. Assets & Budget: Manage accounts and set monthly budgets\n" +
                        "4. Savings Plan: Create and track savings goals\n" +
                        "5. Data Analysis: View financial charts and trend analysis\n" +
                        "6. AI Chat: Interact with financial assistant for advice\n" +
                        "7. Data Import: Support bulk transaction import in CSV format\n\n" +
                        "Tips:\n" +
                        "• All data is automatically saved to local files\n" +
                        "• Supports AI intelligent transaction categorization\n" +
                        "• Budgets are automatically updated based on expenses"
        );
        helpText.setEditable(false);
        helpText.setBackground(helpPanel.getBackground());
        helpText.setFont(new Font("Arial", Font.PLAIN, 12));
        helpText.setRows(10);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);

        JScrollPane helpScrollPane = new JScrollPane(helpText);
        helpScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        helpScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        helpPanel.add(helpScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        topPanel.add(userSettingsPanel);
        topPanel.add(appInfoPanel);
        topPanel.add(helpPanel);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupActionListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSettings();
            }
        });
    }

    private void loadProperties() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                FileInputStream fis = new FileInputStream(configFile);
                properties.load(fis);
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to load settings: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSettings() {
        try {
            // Save user settings
            String userName = userNameField.getText().trim();
            properties.setProperty("user.name", userName);

            // Save to file
            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            properties.store(fos, "User Settings");
            fos.close();

            JOptionPane.showMessageDialog(this,
                    "Settings saved successfully!",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Failed to save settings: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSettings() {
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset all settings? This will restore default settings.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Restore default settings
            userNameField.setText("");

            // Save default settings
            saveSettings();
        }
    }
}
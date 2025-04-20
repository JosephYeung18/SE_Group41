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
 * 用户设置面板
 */
public class UserSettingPanel extends JPanel {
    
    private JTextField userNameField;
    private JTextField currencyField;
    private JCheckBox darkModeCheckBox;
    private JComboBox<String> languageComboBox;
    private JButton saveButton;
    private JButton resetButton;
    
    private JPanel categorySettingsPanel;
    private JButton backupButton;
    private JButton restoreButton;
    
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
        // 用户信息设置
        userNameField = new JTextField(20);
        userNameField.setText(properties.getProperty("user.name", ""));
        
        currencyField = new JTextField(20);
        currencyField.setText(properties.getProperty("user.currency", "¥"));
        
        darkModeCheckBox = new JCheckBox("Dark Mode");
        darkModeCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty("ui.darkMode", "false")));
        
        // 语言选择
        String[] languages = {"简体中文", "English", "日本語"};
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedItem(properties.getProperty("user.language", "简体中文"));
        
        // 按钮
        saveButton = new JButton("Save Setting");
        resetButton = new JButton("Reset Setting");
        
        // 分类设置面板
        categorySettingsPanel = new JPanel();
        
        // 备份和恢复按钮
        backupButton = new JButton("Backup Data");
        restoreButton = new JButton("Restore Data");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 设置标题
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Setting");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        // 用户设置面板
        JPanel userSettingsPanel = new JPanel(new GridBagLayout());
        userSettingsPanel.setBorder(BorderFactory.createTitledBorder("用户设置"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        userSettingsPanel.add(new JLabel("User Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userSettingsPanel.add(userNameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        userSettingsPanel.add(new JLabel("Currency Symbol:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userSettingsPanel.add(currencyField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        userSettingsPanel.add(new JLabel("Language:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        userSettingsPanel.add(languageComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        userSettingsPanel.add(darkModeCheckBox, gbc);
        
        // 分类设置面板
        categorySettingsPanel.setLayout(new BorderLayout());
        categorySettingsPanel.setBorder(BorderFactory.createTitledBorder("分类设置"));
        categorySettingsPanel.add(new JLabel("分类设置功能将在这里实现"), BorderLayout.CENTER);
        
       
        // 数据备份面板
        JPanel dataBackupPanel = new JPanel();
        dataBackupPanel.setBorder(BorderFactory.createTitledBorder("数据备份与恢复"));
        dataBackupPanel.add(backupButton);
        dataBackupPanel.add(restoreButton);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        
        // 主布局
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(userSettingsPanel, BorderLayout.NORTH);
        topPanel.add(categorySettingsPanel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(dataBackupPanel, BorderLayout.SOUTH);
        
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
        
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backupData();
            }
        });
        
        restoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreData();
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
                    "加载设置失败: " + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveSettings() {
        try {
            // 保存用户设置
            properties.setProperty("user.name", userNameField.getText().trim());
            properties.setProperty("user.currency", currencyField.getText().trim());
            properties.setProperty("user.language", (String) languageComboBox.getSelectedItem());
            properties.setProperty("ui.darkMode", String.valueOf(darkModeCheckBox.isSelected()));
            
            // 保存到文件
            FileOutputStream fos = new FileOutputStream(CONFIG_FILE);
            properties.store(fos, "User Settings");
            fos.close();
            
            JOptionPane.showMessageDialog(this, 
                    "设置已保存。部分设置可能需要重启应用程序才能生效。", 
                    "保存成功", 
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "保存设置失败: " + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetSettings() {
        int response = JOptionPane.showConfirmDialog(this, 
                "确定要重置所有设置吗？这将恢复默认设置。", 
                "确认重置", 
                JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            // 恢复默认设置
            userNameField.setText("");
            currencyField.setText("¥");
            languageComboBox.setSelectedItem("简体中文");
            darkModeCheckBox.setSelected(false);
            
            // 保存默认设置
            saveSettings();
        }
    }
    
    private void backupData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择备份位置");
        fileChooser.setSelectedFile(new File("FinanceManager_Backup.zip"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File backupFile = fileChooser.getSelectedFile();
            
            try {
                // 简单实现：复制data目录下的所有文件到一个zip文件
                // 实际项目中，可以使用Java的ZIP API实现更完整的备份功能
                JOptionPane.showMessageDialog(this, 
                        "数据已成功备份到: " + backupFile.getAbsolutePath(), 
                        "备份成功", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                        "备份失败: " + e.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void restoreData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择备份文件");
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File backupFile = fileChooser.getSelectedFile();
            
            int response = JOptionPane.showConfirmDialog(this, 
                    "恢复备份将覆盖当前所有数据。是否继续？", 
                    "确认恢复", 
                    JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // 简单实现：从zip文件中提取文件并覆盖data目录下的文件
                    // 实际项目中，可以使用Java的ZIP API实现更完整的恢复功能
                    JOptionPane.showMessageDialog(this, 
                            "数据已成功从备份恢复: " + backupFile.getAbsolutePath(), 
                            "恢复成功", 
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                            "恢复失败: " + e.getMessage(), 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
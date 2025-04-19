package com.financemanager;

import com.financemanager.view.MainFrame;
import com.financemanager.service.DataPersistenceService;
import com.financemanager.service.CategoryService;

import javax.swing.*;
import java.awt.*;

/**
 * 智能个人理财管理器应用程序入口点
 */
public class FinanceManagerApp {
    
    public static void main(String[] args) {
        // 设置界面外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 初始化数据服务
        DataPersistenceService.getInstance().initialize();
        
        // 确保拥有默认分类
        CategoryService.getInstance().initializeDefaultCategories();
        
        // 启动主窗口
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
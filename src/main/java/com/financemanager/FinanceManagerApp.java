package com.financemanager;

import com.financemanager.view.MainFrame;
import com.financemanager.service.DataPersistenceService;
import com.financemanager.service.CategoryService;

import javax.swing.*;
import java.awt.*;

/**
 * Smart Personal Finance Manager Application Entry Point
 */
public class FinanceManagerApp {

    public static void main(String[] args) {
        // Set UI look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize data service
        DataPersistenceService.getInstance().initialize();

        // Ensure default categories exist
        CategoryService.getInstance().initializeDefaultCategories();

        // Launch main window
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
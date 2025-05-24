package com.financemanager.view;

import com.financemanager.service.AIService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AI Conversation Panel
 */
public class AIConversationPanel extends JPanel {

    private JTextArea chatHistoryArea;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane scrollPane;

    private AIService aiService;

    public AIConversationPanel() {
        aiService = new AIService();

        initComponents();
        setupLayout();
        setupActionListeners();

        // Display welcome message
        chatHistoryArea.append("Financial Assistant: Hello! I'm your financial management assistant. How can I help you today?\n");
    }

    private void initComponents() {
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setLineWrap(true);
        chatHistoryArea.setWrapStyleWord(true);

        // 设置支持中文的字体
        Font chineseFont = getChinesesFont();
        chatHistoryArea.setFont(chineseFont);
        chatHistoryArea.setBackground(new Color(245, 245, 245));

        scrollPane = new JScrollPane(chatHistoryArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        messageField = new JTextField(20);
        messageField.setFont(chineseFont); // 设置输入框字体

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton.setBackground(new Color(120, 120, 200));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
    }

    /**
     * 获取支持中文的字体
     */
    private Font getChinesesFont() {
        // 尝试多种中文字体，找到系统支持的第一个
        String[] fontNames = {
                "Microsoft YaHei UI",  // 微软雅黑UI
                "Microsoft YaHei",     // 微软雅黑
                "SimHei",              // 黑体
                "SimSun",              // 宋体
                "NSimSun",             // 新宋体
                "Dialog"               // 系统默认对话框字体
        };

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();

        for (String fontName : fontNames) {
            for (String availableFont : availableFonts) {
                if (availableFont.equals(fontName)) {
                    return new Font(fontName, Font.PLAIN, 14);
                }
            }
        }

        // 如果没有找到合适的字体，使用Java默认的支持Unicode的字体
        return new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("AI Conversation", SwingConstants.CENTER), BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupActionListeners() {
        ActionListener sendMessageAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        };

        sendButton.addActionListener(sendMessageAction);
        messageField.addActionListener(sendMessageAction);
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        // Add user message to chat history
        chatHistoryArea.append("User: " + message + "\n");

        // Clear message input field
        messageField.setText("");

        // Get AI response
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return aiService.getResponse(message);
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    chatHistoryArea.append("Financial Assistant: " + response + "\n");

                    // Auto scroll to bottom
                    chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
                } catch (Exception e) {
                    e.printStackTrace();
                    chatHistoryArea.append("Financial Assistant: Sorry, I'm unable to respond right now. Please try again later.\n");
                }
            }
        };

        worker.execute();
    }
}
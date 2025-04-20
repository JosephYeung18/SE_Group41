package com.financemanager.view;

import com.financemanager.service.AIService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AI对话面板
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
        
        // 显示欢迎消息
        chatHistoryArea.append("Financial Assistant :  Hello! I'm your financial management assistant. How can I assist you?\n");
    }
    
    private void initComponents() {
        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setLineWrap(true);
        chatHistoryArea.setWrapStyleWord(true);
        chatHistoryArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        chatHistoryArea.setBackground(new Color(245, 245, 245));
        
        scrollPane = new JScrollPane(chatHistoryArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        messageField = new JTextField(20);
        messageField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        sendButton = new JButton("发送");
        sendButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        sendButton.setBackground(new Color(120, 120, 200));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("AI 对话", SwingConstants.CENTER), BorderLayout.CENTER);
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
        
        // 添加用户消息到聊天历史
        chatHistoryArea.append("User: " + message + "\n");
        
        // 清空消息输入框
        messageField.setText("");
        
        // 获取AI回复
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return aiService.getResponse(message);
            }
            
            @Override
            protected void done() {
                try {
                    String response = get();
                    chatHistoryArea.append("Financial Assistant : " + response + "\n");
                    
                    // 自动滚动到底部
                    chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
                } catch (Exception e) {
                    e.printStackTrace();
                    chatHistoryArea.append("Financial Assistant : Sorry, I can't respond now. Please try again later. \n");
                }
            }
        };
        
        worker.execute();
    }
}
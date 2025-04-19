package com.financemanager.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.io.FileInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * DeepSeek API工具类，负责调用DeepSeek的大语言模型
 */
public class DeepSeekAPI {
    
    private String apiKey;
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    
    public DeepSeekAPI() {
        loadApiKey();
    }
    
    /**
     * 从配置文件加载API密钥
     */
    private void loadApiKey() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            this.apiKey = properties.getProperty("deepseek.api.key");
            
            if (this.apiKey == null || this.apiKey.isEmpty()) {
                System.err.println("警告: DeepSeek API密钥未设置");
                // 使用一个默认的测试密钥
                this.apiKey = "YOUR_DEEPSEEK_API_KEY";
            }
        } catch (Exception e) {
            System.err.println("无法加载API密钥: " + e.getMessage());
            // 使用一个默认的测试密钥
            this.apiKey = "YOUR_DEEPSEEK_API_KEY";
        }
    }
    
    /**
     * 获取AI模型的响应
     * @param message 用户消息
     * @return AI响应
     */
    public String getResponse(String message) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat");
        
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.put(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);
        
        return sendRequest(requestBody);
    }
    
    /**
     * 获取带有上下文的AI模型响应
     * @param message 用户消息
     * @param context 上下文信息
     * @return AI响应
     */
    public String getResponseWithContext(String message, String context) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat");
        
        JSONArray messages = new JSONArray();
        
        // 添加系统消息（上下文）
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一个个人财务顾问，根据用户提供的财务数据给出专业的分析和建议。以下是用户的财务数据：\n\n" + context);
        messages.put(systemMessage);
        
        // 添加用户消息
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.put(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);
        
        return sendRequest(requestBody);
    }
    
    /**
     * 发送HTTP请求到DeepSeek API
     * @param requestBody 请求体
     * @return AI响应文本
     */
    private String sendRequest(JSONObject requestBody) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                 new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                // 解析JSON响应
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject firstChoice = choices.getJSONObject(0);
                    JSONObject message = firstChoice.getJSONObject("message");
                    return message.getString("content");
                }
            }
        } else {
            // 处理错误
            try (BufferedReader br = new BufferedReader(
                 new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.err.println("API错误: " + response.toString());
            }
            
            // 返回一个友好的错误消息
            return "抱歉，我现在无法回应您的问题。请稍后再试。";
        }
        
        return "抱歉，发生了未知错误。请稍后再试。";
    }
}
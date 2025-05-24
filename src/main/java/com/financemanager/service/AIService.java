package com.financemanager.service;

import com.financemanager.model.TransactionType;
import com.financemanager.util.DeepSeekAPI;
import com.financemanager.model.Transaction;
import com.financemanager.controller.TransactionController;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONObject;

/**
 * AI服务类，负责与DeepSeek API交互
 */
public class AIService {
    
    private DeepSeekAPI deepSeekAPI;
    private TransactionController transactionController;
    
    public AIService() {
        this.deepSeekAPI = new DeepSeekAPI();
        this.transactionController = TransactionController.getInstance();
    }
    
    /**
     * 获取AI响应
     * @param message 用户消息
     * @return AI响应
     */
    public String getResponse(String message) {
        try {
            // 如果消息涉及消费分析，添加交易数据上下文
            if (isFinanceRelatedQuery(message)) {
                String context = generateFinancialContext();
                return deepSeekAPI.getResponseWithContext(message, context);
            } else {
                return deepSeekAPI.getResponse(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，我暂时无法回答您的问题。请稍后再试。";
        }
    }
    
    /**
     * 判断消息是否与财务相关
     */
    private boolean isFinanceRelatedQuery(String message) {
        String lowerCaseMessage = message.toLowerCase();
        
        // 关键词检测
        String[] keywords = {
            "消费", "支出", "花费", "开销", "收入", "预算", "储蓄", "钱", "资金", "财务",
            "金钱", "账户", "花销", "费用", "交易", "账单", "花了多少", "赚了多少", "存了多少",
            "分析", "统计", "总结", "建议", "如何节省", "怎么省钱", "怎么花钱"
        };
        
        for (String keyword : keywords) {
            if (lowerCaseMessage.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 生成财务上下文
     */
    private String generateFinancialContext() {
        StringBuilder context = new StringBuilder();
        context.append("以下是用户的财务数据：\n\n");
        
        // 获取最近的交易数据
        List<Transaction> recentTransactions = transactionController.getRecentTransactions(30); // 最近30天的交易
        
        if (recentTransactions.isEmpty()) {
            context.append("用户目前没有记录任何交易数据。\n");
        } else {
            context.append("最近的交易记录：\n");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            for (Transaction transaction : recentTransactions) {
                context.append(dateFormat.format(transaction.getDate()))
                       .append(" | ")
                       .append(transaction.getType().getDisplayName())
                       .append(" | ")
                       .append(transaction.getCategory().getName())
                       .append(" | ¥")
                       .append(transaction.getAmount())
                       .append(" | ")
                       .append(transaction.getComment() != null ? transaction.getComment() : "")
                       .append("\n");
            }
            
            // 添加总支出和收入统计
            double totalIncome = 0;
            double totalExpense = 0;
            
            for (Transaction transaction : recentTransactions) {
                if (transaction.getType() == TransactionType.INCOME) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpense += transaction.getAmount();
                }
            }
            
            context.append("\n总收入：¥").append(totalIncome);
            context.append("\n总支出：¥").append(totalExpense);
            context.append("\n净收入：¥").append(totalIncome - totalExpense);
        }
        
        return context.toString();
    }
    
    /**
     * 获取财务分析和建议
     */
    public String getFinancialAnalysis() {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("请根据以下财务数据，提供详细的消费分析和理财建议。");
            
            String context = generateFinancialContext();
            return deepSeekAPI.getResponseWithContext(prompt.toString(), context);
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，无法生成财务分析。请稍后再试。";
        }
    }
    
    /**
     * 获取智能预算建议
     */
    public String getBudgetSuggestions() {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("请根据以下财务数据，提供下个月的预算建议，包括各个消费类别的合理预算金额。");
            
            String context = generateFinancialContext();
            return deepSeekAPI.getResponseWithContext(prompt.toString(), context);
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，无法生成预算建议。请稍后再试。";
        }
    }
    
    /**
     * 获取储蓄目标建议
     */
    public String getSavingsGoalSuggestions(double monthlyIncome) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("用户的月收入约为 ¥").append(monthlyIncome).append("。");
            prompt.append("请根据这一收入水平，提供合理的储蓄目标建议，包括短期（3-6个月）、中期（1-3年）和长期（3年以上）的储蓄目标。");
            
            String context = generateFinancialContext();
            return deepSeekAPI.getResponseWithContext(prompt.toString(), context);
        } catch (Exception e) {
            e.printStackTrace();
            return "抱歉，无法生成储蓄目标建议。请稍后再试。";
        }
    }
    
    /**
     * 根据交易描述和类型自动分类交易
     * @param description 交易描述或备注
     * @param type 交易类型（收入/支出）
     * @return 分类ID
     */
    public String classifyTransaction(String description, TransactionType type) {
        try {
            // 构建提示信息
            StringBuilder prompt = new StringBuilder();
            prompt.append("请根据以下交易描述，将其归类到最合适的分类中。\n");
            prompt.append("交易描述：").append(description).append("\n");
            prompt.append("交易类型：").append(type == TransactionType.INCOME ? "收入" : "支出").append("\n\n");
            
            // 添加可用的分类选项
            prompt.append("可用的分类选项：\n");
            if (type == TransactionType.INCOME) {
                prompt.append("- 工资 (ID: income-salary)\n");
                prompt.append("- 奖金 (ID: income-bonus)\n");
                prompt.append("- 利息 (ID: income-interest)\n");
                prompt.append("- 投资收益 (ID: income-investment)\n");
                prompt.append("- 其他收入 (ID: income-other)\n");
            } else {
                prompt.append("- 餐饮 (ID: expense-food)\n");
                prompt.append("- 购物 (ID: expense-shopping)\n");
                prompt.append("- 交通 (ID: expense-transport)\n");
                prompt.append("- 住房 (ID: expense-housing)\n");
                prompt.append("- 娱乐 (ID: expense-entertainment)\n");
                prompt.append("- 教育 (ID: expense-education)\n");
                prompt.append("- 医疗 (ID: expense-medical)\n");
                prompt.append("- 其他 (ID: expense-other)\n");
            }
            
            prompt.append("\n请只返回最合适的分类ID，不要有任何其他文字。");
            
            // 调用API获取分类结果
            String response = deepSeekAPI.getResponse(prompt.toString());
            
            // 提取分类ID（清理可能的额外文本）
            String categoryId = response.trim();
            
            // 如果返回的不是有效的ID格式，则尝试从文本中提取
            if (!categoryId.matches("(income|expense)-[a-z]+")) {
                // 尝试匹配ID格式
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                    "(income|expense)-[a-z]+");
                java.util.regex.Matcher matcher = pattern.matcher(response);
                if (matcher.find()) {
                    categoryId = matcher.group();
                } else {
                    // 如果无法提取有效ID，则使用默认分类
                    categoryId = type == TransactionType.INCOME ? 
                        "income-other" : // 其他收入
                        "expense-other";  // 其他支出
                }
            }
            
            return categoryId;
        } catch (Exception e) {
            e.printStackTrace();
            // 发生错误时返回默认分类
            return type == TransactionType.INCOME ? 
                "income-other" : // 其他收入
                "expense-other";  // 其他支出
        }
    }
}
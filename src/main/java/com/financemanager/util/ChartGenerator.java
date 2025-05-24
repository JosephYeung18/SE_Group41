package com.financemanager.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class ChartGenerator {

    static {
        // 创建支持中文的主题
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        // 设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
        // 设置轴标签字体
        standardChartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 15));
        // 设置图例字体
        standardChartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 12));
        // 应用主题
        ChartFactory.setChartTheme(standardChartTheme);
    }

    /**
     * 生成月度支出饼图
     */
    public static JPanel createMonthlyExpensesPieChart(Map<String, Double> expenses, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                true,  // 显示图例
                true,  // 显示工具提示
                false  // 不生成URL
        );

        // 配置饼图
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setLabelFont(new Font("微软雅黑", Font.PLAIN, 12));

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 300));

        return chartPanel;
    }

    /**
     * 生成预测对比柱状图
     */
    public static JPanel createExpensePredictionChart(Map<String, Double> current, Map<String, Double> prediction) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : current.entrySet()) {
            String category = entry.getKey();
            if (prediction.containsKey(category)) {
                dataset.addValue(entry.getValue(), "Current month", category);
                dataset.addValue(prediction.get(category), "Predict next month", category);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Comparison of forecasted expenditures for this month and next month",
                "category",
                "amount of money",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // 显示图例
                true,   // 显示工具提示
                false   // 不生成URL
        );

        // 配置图表
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 350));

        return chartPanel;
    }

    /**
     * 生成财务趋势折线图
     */
    public static JPanel createFinanceTrendChart(List<Map<String, Object>> monthsData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> monthData : monthsData) {
            String monthLabel = monthData.get("monthName").toString();
            Map<String, Double> summary = (Map<String, Double>) monthData.get("summary");

            dataset.addValue(summary.get("income"), "income", monthLabel);
            dataset.addValue(summary.get("expenses"), "expenses", monthLabel);
            dataset.addValue(summary.get("balance"), "balance", monthLabel);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Financial Trend Analysis",
                "month",
                "amount of money",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // 显示图例
                true,   // 显示工具提示
                false   // 不生成URL
        );

        // 配置图表
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 350));

        return chartPanel;
    }

    /**
     * 生成季度类别分布堆积柱状图
     */
    public static JPanel createQuarterlyDistributionChart(List<Map<String, Object>> monthsData) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> monthData : monthsData) {
            String monthLabel = monthData.get("monthName").toString();
            Map<String, Double> categories = (Map<String, Double>) monthData.get("categories");

            for (Map.Entry<String, Double> entry : categories.entrySet()) {
                dataset.addValue(entry.getValue(), entry.getKey(), monthLabel);
            }
        }

        JFreeChart chart = ChartFactory.createStackedBarChart(
                "Distribution of quarterly expenditure categories",
                "month",
                "amount of money",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // 显示图例
                true,   // 显示工具提示
                false   // 不生成URL
        );

        // 配置图表
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 创建图表面板
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 350));

        return chartPanel;
    }
}
# 智能个人理财管理器

一个使用Java开发的智能个人理财管理应用程序，帮助用户记录支出、设定储蓄目标，并分析消费习惯。


## 技术栈

- Java 8
- Swing GUI
- JSON数据存储
- DeepSeek AI API

## 系统要求

- Java 8或更高版本
- 互联网连接（用于AI功能）

## 安装指南

1. 确保已安装Java 8
2. 下载发布包（JAR文件）
3. 创建 `config.properties`文件，配置DeepSeek API密钥
4. 双击JAR文件运行，或使用命令行：`java -jar FinanceManager.jar`

## 配置DeepSeek API

在应用程序根目录创建 `config.properties`文件，添加以下内容：

deepseek.api.key=YOUR_API_KEY_HERE，将 `YOUR_API_KEY_HERE`替换为您的DeepSeek API密钥。

## 使用指南

1. **主界面**：显示财务概览和导航选项
2. **AI对话**：与AI助手进行对话，获取财务建议
3. **资产预算**：管理账户和预算
4. **交易详情**：查看交易历史和统计数据
5. **记账**：添加收入和支出交易
6. **储蓄计划**：设置和跟踪储蓄目标

## CSV导入格式

导入CSV文件应包含以下列（第一行为标题）：

日期,类型,分类,金额,备注
2023-01-01,收入,工资,5000,1月工资
2023-01-02,支出,餐饮,100,午餐

## 项目结构

- `src/` - 源代码
  - `main/java/com/financemanager/` - Java源文件
    - `controller/` - 控制器层
    - `model/` - 模型层
    - `view/` - 视图层
    - `service/` - 服务层
    - `util/` - 工具类
  - `test/` - 测试代码
- `data/` - 数据文件
- `docs/` - 文档

## 开发指南

1. 使用Maven构建项目：`mvn clean package`
2. 运行测试：`mvn test`

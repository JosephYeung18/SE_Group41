#  Personal Financial Manager 

## 🌟 Project Overview  
This system is an application based on Java Swing, designed to help users record and analyze personal financial income and expenditure. It provides functions such as transaction record management, data statistics, and chart visualization, enabling users to track monthly/quarterly financial trends and optimize spending structures.  
### 👥 Team Member：  
- Juejia Yang 
- Wenxiang Guo 
- Site Li   
- Haocheng Zhang   
- Yuxuan Liao   
- Yifei Li  

## ✨ Features  
### 1. 📝 Transaction Management  
- **📝 Record Income/Expense**: Support adding income/expense records with fields including amount, category, date, and notes.  
- **🛠️ Edit & Delete**: Modify existing records or remove invalid data.  
- **🔍 Data Query**: Filter transactions by date range or category.  

### 2. 📊 Data Analysis & Visualization 📈 
- **Single Month Analysis**: Generate monthly expense pie charts and financial summaries (income/expense/balance).  
- **Three-Month Trend**: Compare line charts of income/expense/balance trends over the past three months.  
- **Quarterly Analysis**: Display stacked bar charts of expenditure categories across months in a quarter.  
- **Forecast Comparison**: Bar chart comparison of expenditure categories between the current month and forecasted next month.  

### 3. 🎨 User Interface  
- **🎨 Visual Charts**: Interactive charts generated via JFreeChart, supporting zooming and tooltips.  
- **🎛️ Operation Panel**: Simple controls for selecting analysis type and time range, with one-click chart generation.  
- **📄 Data Display**: Scrollable panel for multi-chart layouts with dynamic refresh.  

## 🛠️ Technical Architecture  
### Core Components  
| Component       | Description                                                                 |  
|-----------------|-----------------------------------------------------------------------------|  
| **⚙️ UI Framework**| Java Swing (JPanel/JComboBox/JButton, etc.)                                 |  
| **📊 Chart Library**| JFreeChart (for generating pie, line, and bar charts)                       |  
| **📝 Data Management**| `TransactionController` singleton for managing transactions; `DataAdapter` for data conversion |  
| **📁 Layout Management**| Mixed layouts (BorderLayout/GridLayout/BoxLayout)                           |  

##  Running Environment  
- **JDK Version**: Java 8 or higher  
- **Dependencies**:  
  - JFreeChart (manually import `jfreechart-1.5.3.jar` and `jcommon-1.0.23.jar`)  
  - Other: Java standard libraries (Swing/AWT/Date, etc.)  

## 📖 Usage Guide  
### 1. 🖱️ Interface Operations  
1. **Select Analysis Type**: Use the dropdown menu to choose "Single Month Analysis", "Three Month Trend", or "Quarterly Analysis".  
2. **Select Month**: Dynamically load the last three months for quick time-range switching.  
3. **Generate Charts**: Click the "View Analysis" button to render charts based on selections, displayed in the scrollable panel below.  

### 2. Chart Interactions  
- **Pie Charts**: Show expenditure category proportions; click legend items to hide/show categories.  
- **Line/Bar Charts**: X-axis = month, Y-axis = amount; support mouse zooming and dragging for details.  
- **ℹTooltips**: Hover over chart elements to see specific values (e.g., "Food: ¥500.00").  

## 📂 Project Structure  
- `src/` - Source code  
  - `main/java/com/financemanager/` - Java source files  
    - `controller/` - Controller layer  
    - `model/` - Model layer  
    - `view/` - View layer  
    - `service/` - Service layer  
    - `util/` - Utility classes  
  - `test/` - Test code  
- `data/` - Data files  
- `docs/` - Documentation  

## 💡 Extension Suggestions  
1. **More Chart Types**: Add radar charts, scatter plots, etc., to analyze spending-income correlations.  
2. **Forecasting**: Enhance expenditure prediction algorithms based on historical data.  

## 🛠️ Development Guide  
1. Build the project using Maven: `mvn clean package`  
2. Run tests: `mvn test`  

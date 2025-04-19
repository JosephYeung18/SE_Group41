
# SE_Group41  
This is Software Engineering Group 41  

## Team Member：  
- Juejia Yang  
- Wenxiang Gu  
- Site Li  
- Haocheng Zhang  
- Yuxuan Liao  
- Yifei Li  

# Smart Personal Finance Management Assistant  
A smart personal finance management application developed in Java to help users record expenses, set savings goals, and analyze spending habits.  


## Technology Stack  
- Java 8  
- Swing GUI  
- JSON data storage  
- DeepSeek AI API  

## System Requirements  
- Java 8 or higher  
- Internet connection (for AI features)  

## Installation Guide  
1. Ensure Java 8 is installed  
2. Download the release package (JAR file)  
3. Create a `config.properties` file and configure the DeepSeek API key  
4. Run by double-clicking the JAR file or using the command line: `java -jar FinanceManager.jar`  

## Configure DeepSeek API  
Create a `config.properties` file in the application root directory and add the following content:  
```properties  
deepseek.api.key=YOUR_API_KEY_HERE  
```  
Replace `YOUR_API_KEY_HERE` with your DeepSeek API key.  

## Usage Guide  
1. **Main Interface**: Displays financial overview and navigation options  
2. **AI Chat**: Chat with the AI assistant to get financial advice  
3. **Asset Budget**: Manage accounts and budgets  
4. **Transaction Details**: View transaction history and statistics  
5. **Expense Recording**: Add income and expense transactions  
6. **Savings Plan**: Set and track savings goals  

## CSV Import Format  
The imported CSV file should contain the following columns (first row as header):  
```csv  
Date,Type,Category,Amount,Note  
2023-01-01,Income,Salary,5000,January salary  
2023-01-02,Expense,Dining,100,Lunch  
```  

## Project Structure  
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

## Development Guide  
1. Build the project using Maven: `mvn clean package`  
2. Run tests: `mvn test`

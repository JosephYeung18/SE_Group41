# Smart Personal Finance Manager

## ✨ Features  
### 1. 💰 Savings Plan Management  
- **📝 Create Plans**: Set savings goals with name, target amount, and timeframe  
- **💰 Deposit/Withdraw**: Track progress with deposit/withdraw operations  
- **📊 Progress Tracking**: Automatic completion percentage calculation  
- **🗂️ Categorization**: Filter plans by active/completed status  

### 2. ⚙️ User Settings  
- **👤 Profile Management**: Set and save username  
- **🔄 Reset Function**: Restore default settings when needed  
- **📂 Local Storage**: Persistent configuration using properties file  

### 3. 📊 Data Persistence  
- **💾 Automatic Saving**: All changes saved to local JSON files  
- **🔄 Data Loading**: Automatic initialization with error handling  
- **🔒 Data Integrity**: Validation for deposit/withdraw operations  

## 🛠️ Technical Architecture  
### Core Components  
| Component               | Description                                                                 |  
|-------------------------|-----------------------------------------------------------------------------|  
| **⚙️ UI Framework**      | Java Swing (JPanel/JButton/JTextField, etc.)                                |  
| **📁 Data Management**   | `SavingsPlanController` for business logic, `DataPersistenceService` for storage |  
| **📊 Model Layer**       | `SavingsPlan` model with deposit/withdraw operations and progress tracking  |  
| **🔧 Utility**           | UUID generation for unique IDs, Date handling for plan timelines            |  

## 📖 Usage Guide  
### 1. Savings Plan Operations  
1. **Create Plan**:  
   - Set name, target amount, and end date  
   - System auto-generates ID and start date  

2. **Track Progress**:  
   - Use `deposit()` to add funds  
   - View completion percentage via `getCompletionPercentage()`  

3. **Manage Plans**:  
   - Filter active plans with `getActivePlans()`  
   - View completed goals with `getCompletedPlans()`  

### 2. User Settings  
- **Edit Profile**:  
  - Update username in settings panel  
  - Changes saved to `config.properties`  

- **Reset Options**:  
  - Restore default configuration  
  - Requires confirmation before executing  

## 📂 Project Structure  

## 🛠️ Development Guide  
1. Build the project using Maven: `mvn clean package`  
2. Run tests: `mvn test`  
src/
├── main/java/com/financemanager/
│ ├── controller/ # Business logic
│ │ └── SavingsPlanController.java
│ ├── model/ # Data models
│ │ └── SavingsPlan.java
│ ├── view/ # UI components
│ │ └── UserSettingPanel.java
│ └── service/ # Services
│ └── DataPersistenceService.java
data/ # JSON data files
config.properties # User configuration


## 🚀 Running Environment  
- **JDK Version**: Java 8+  
- **Dependencies**:  
  - Java Standard Library (Swing/AWT)  
  - No external libraries required  

## 💡 Extension Suggestions  
1. **Enhanced Reporting**: Add savings progress charts and achievement milestones  
2. **Plan Templates**: Predefined savings templates (e.g., "Vacation Fund")  
3. **Reminder System**: Notifications for upcoming plan deadlines  
4. **Data Export**: PDF/Excel reporting for savings history  

## ⚠️ Error Handling  
- File permission errors show user-friendly alerts  
- Data validation prevents negative amounts/over-withdrawals  
- Automatic fallback to empty dataset if loading fails  

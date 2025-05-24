# Personal Contribution - Wenxiang Guo

As the lead developer responsible for the **Asset and Budget** modules of the finance management system, I designed and implemented the core functionalities related to asset management and budgeting. Below is a detailed account of my key contributions:

## Core Module Development

### 1. Architecture Design of Asset and Budget Modules
- Designed and implemented an **MVC architecture** for both Asset and Budget modules, ensuring a clean separation of concerns and allowing for easy maintenance and future expansion.
- Developed the **AssetController** and **BudgetController** classes to handle the logic and data manipulation for assets and budgets respectively. 
- Implemented a **Singleton Design Pattern** for the `BudgetController` to guarantee that there is only one instance managing the budget data throughout the application.

### 2. Budget Management Functionality
- Developed a flexible budgeting system that allows users to create, update, and delete budgets for specific categories, months, and years.
- In the **BudgetController**, I implemented the `addBudget()`, `updateBudget()`, and `deleteBudget()` methods, ensuring that budgets are correctly added, updated, and removed from the data structure.
- Incorporated functionality for calculating total budgets, expenses, and remaining amounts for a given month, allowing users to quickly assess their financial standing.
- Added a feature for visualizing budget progress using **JProgressBar**, where the progress bar dynamically adjusts based on the amount spent in each budget category.

### 3. Asset Management Functionality
- Implemented asset tracking features where users can manage different types of assets, including accounts, liabilities, and net worth.
- Developed the `AssetController` class to handle the logic for adding, editing, and deleting assets, ensuring smooth integration with the **AssetManagementPanel** for real-time data updates.
- Created a comprehensive **AssetPanel** interface where users can view and manage their accounts, liabilities, and overall net worth.

### 4. Data Persistence and Integration
- Integrated the **DataPersistenceService** to ensure that all asset and budget data is saved and restored between sessions.
- Developed methods for saving budget and asset data to a persistent storage medium, and loading this data back when the application starts, ensuring data consistency and reliability.
- Utilized **UUID** for generating unique identifiers for each budget and asset, ensuring that every entry is distinct and easily referenced.

## Key Classes

### 1. BudgetController
- Central to the management of **budget data**. It contains methods for adding, updating, and deleting budgets. It also includes calculation methods to retrieve the total budget, total spending, and remaining balance for a specified time period (month/year).
- Key methods: `addBudget()`, `updateBudget()`, `deleteBudget()`, and `calculateTotalExpenses()`.
- Ensures that the budget data is persistent and updated immediately after any changes are made.

### 2. AssetController
- Manages all operations related to **assets** including adding, removing, and updating different types of assets (accounts, liabilities, etc.).
- Key methods: `addAsset()`, `removeAsset()`, `updateAsset()`.
- Integrates with the **AssetManagementPanel** to update the display whenever changes are made to the asset data.

### 3. AssetManagementPanel
- The **View** component for managing assets. Displays a summary of the user's assets, liabilities, and net worth, allowing users to perform CRUD operations on their assets.
- Real-time updates are triggered whenever the user adds, edits, or removes an asset, providing an up-to-date financial overview.

### 4. BudgetManagementPanel
- The **View** component responsible for displaying and managing the user’s budget.
- Displays budget categories for the current month, showing the total allocated budget, amount spent, and remaining balance.
- Utilizes **JProgressBar** to show the progress of each budget category in real-time, updating dynamically based on user input.
  
## Testing and Quality Assurance

### 1. Unit Testing
- Wrote unit tests to ensure that key functionalities in **BudgetController** and **AssetController** work as expected.
- Test coverage includes adding, updating, and deleting assets and budgets, as well as ensuring the accuracy of budget calculations (total expenses, remaining balance).

### 2. Test Automation
- Automated the testing process using **JUnit 5** to ensure that changes to asset and budget data are correctly reflected in the system, and edge cases are handled properly.
- Created test cases for validating the persistence and retrieval of asset and budget data to ensure that no data is lost between sessions.

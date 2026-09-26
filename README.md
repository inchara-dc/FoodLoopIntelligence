FoodLoop Intelligence

A Java-based food waste management system designed to help manage food donations, connect donors with receivers, and track food waste using a centralized database.

Overview

FoodLoop Intelligence is a desktop application developed to simplify food donation and waste management. It provides a structured platform for managing donors, receivers, food items, donations, pickups, and waste records.

The application uses Java Swing for its graphical user interface and MySQL for data storage, with JDBC connecting the application to the database.

Features

- Dashboard: View an overview of food donation and waste management activities.
- Donor Management: Add, update, delete, and manage donor details.
- Receiver Management: Maintain receiver information.
- Food Category Management: Organize food items by category.
- Food Item Management: Manage food item details.
- Donation Management: Record and manage food donations.
- Pickup Management: Track food pickup records.
- Waste Management: Maintain records of food waste.
- Reports: View reports related to food donation and waste management.

Tech Stack

- Programming Language: Java
- GUI: Java Swing
- Database: MySQL
- Database Connectivity: JDBC
- IDE: Visual Studio Code
- Version Control: Git and GitHub

Project Structure

FoodLoopIntelligence/
├── src/
│   ├── Main.java
│   ├── db/
│   │   └── DBConnection.java
│   └── ui/
│       ├── MainFrame.java
│       └── panels/
│           ├── DashboardPanel.java
│           ├── DonorsPanel.java
│           ├── ReceiversPanel.java
│           ├── FoodCategoryPanel.java
│           ├── FoodItemsPanel.java
│           ├── DonationsPanel.java
│           ├── PickupPanel.java
│           ├── WastePanel.java
│           ├── ReportsPanel.java
│           └── BaseCRUDPanel.java
├── lib/
│   └── mysql-connector-j-9.7.0.jar
├── README.md
└── .gitignore

Prerequisites

- Java JDK 17 or later
- MySQL Server
- MySQL Connector/J

Setup and Installation

1. Clone the repository:
   
   git clone https://github.com/inchara-dc/FoodLoopIntelligence.git

2. Open the project in Visual Studio Code.

3. Create a MySQL database named "food_loop".

4. Configure the database connection in "src/db/DBConnection.java" with your MySQL username and password.

5. Ensure the MySQL Connector/J library is included in the project.

6. Compile and run the application using your Java environment.

Future Enhancements

- Deployment for wider accessibility.
- Improved reporting and data visualization.
- Additional features to support food donation coordination.

Author

Inchara DC

Information Science and Engineering
M. S. Ramaiah Institute of Technology
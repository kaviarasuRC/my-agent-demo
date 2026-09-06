# ATM Mini Project

A simple command-line ATM simulation written in Java. It allows users to register, check balances, deposit, withdraw, and print receipts using a MySQL backend.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Contribution Guidelines](#contribution-guidelines)
- [License](#license)

## Features
- User registration with debit card validation
- Secure PIN authentication
- Balance inquiry, deposit, and withdrawal
- Receipt generation
- Simple console-based UI

## Installation
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Maven (optional, for building)
- MySQL Server

### Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/atm-mini-project.git
   cd atm-mini-project
   ```
2. **Set up the MySQL database**
   ```sql
   CREATE DATABASE atm;
   USE atm;
   CREATE TABLE userDetails (
       id INT AUTO_INCREMENT PRIMARY KEY,
       debitCardNumber VARCHAR(12) NOT NULL UNIQUE,
       pinNumber INT NOT NULL,
       userName VARCHAR(50),
       firstName VARCHAR(50),
       lastName VARCHAR(50),
       availableBalance DOUBLE DEFAULT 5000,
       address VARCHAR(255),
       city VARCHAR(100)
   );
   ```
   Adjust the connection details in `AtmProject.java` if your MySQL credentials differ.
3. **Compile and run**
   ```bash
   javac AtmProject.java
   java AtmProject
   ```
   Or, if you use Maven, create a `pom.xml` and run `mvn compile exec:java`.

## Usage
When you run the program, you will be presented with a menu:
```
1. Perform transaction
2. Register a user
3. Exit
```
- **Register a user**: Follow prompts to enter a 12‑digit debit card number, a 4‑digit PIN (confirmed), and personal details. The account starts with a default balance of 5000.
- **Perform transaction**: After entering a valid debit card number and PIN, you can:
  1. Check balance
  2. Deposit amount
  3. Withdraw amount (ensuring sufficient funds)
  4. Print a receipt
  5. Exit transaction

All interactions are via the console.

## Configuration
Database connection details are hard‑coded in `AtmProject.java`:
```java
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm","root","root");
```
Update the URL, username, and password to match your environment.

## Contribution Guidelines
We welcome contributions! Please follow these steps:
1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** and ensure the code compiles.
4. **Write or update documentation** as needed.
5. **Submit a pull request** with a clear description of your changes.

### Code Style
- Follow standard Java naming conventions.
- Keep methods concise and well‑documented with Javadoc.
- Use meaningful variable names.

### Testing
While this project does not include automated tests, please manually verify that your changes work as expected before submitting a PR.

## License
This project is licensed under the MIT License – see the `LICENSE` file for details.

# ATM Mini Project

A simple command‑line ATM simulation written in Java. It demonstrates basic banking operations such as user registration, balance inquiry, deposits, withdrawals, and receipt generation using a MySQL database for persistence.

---

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Database Schema](#database-schema)
- [Running the Application](#running-the-application)
- [Usage Guide](#usage-guide)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## Features
- **User Registration** – Create a new user with a unique 12‑digit debit card number and a 4‑digit PIN.
- **Secure Login** – Validate debit card number and PIN before allowing transactions.
- **Balance Inquiry** – View the current account balance.
- **Deposit** – Add funds to the account.
- **Withdrawal** – Withdraw funds with sufficient balance checks.
- **Receipt Generation** – Print a simple receipt showing user details and balance.
- **Graceful Error Handling** – Handles invalid inputs, insufficient attempts, and database errors.

---

## Prerequisites
- **Java Development Kit (JDK) 8 or higher**
- **Maven** (optional, if you want to manage dependencies via Maven)
- **MySQL Server** (or any compatible MySQL database)
- **MySQL Connector/J** (JDBC driver) – ensure the JAR is on the classpath when compiling/running.

---

## Setup
1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd <repo-directory>
   ```

2. **Create the MySQL database**
   ```sql
   CREATE DATABASE atm;
   USE atm;
   CREATE TABLE userDetails (
       debitCardNumber VARCHAR(12) PRIMARY KEY,
       pinNumber INT,
       userName VARCHAR(50),
       firstName VARCHAR(50),
       lastName VARCHAR(50),
       availableBalance DOUBLE,
       address VARCHAR(255),
       city VARCHAR(100)
   );
   ```
   The `availableBalance` column is initialized with a default value of **5000.0** during registration.

3. **Add the MySQL JDBC driver to your classpath**
   - If you are using Maven, add the following dependency to `pom.xml`:
   ```xml
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.33</version>
   </dependency>
   ```
   - Otherwise, download the JAR from the official site and include it when compiling:
   ```bash
   javac -cp .:mysql-connector-java-8.0.33.jar AtmProject.java
   ```

---

## Database Schema
| Column            | Type            | Description |
|-------------------|-----------------|-------------|
| `debitCardNumber` | VARCHAR(12) PK  | Unique 12‑digit card number |
| `pinNumber`       | INT             | 4‑digit PIN for authentication |
| `userName`        | VARCHAR(50)     | Username displayed in the UI |
| `firstName`       | VARCHAR(50)     | User's first name |
| `lastName`        | VARCHAR(50)     | User's last name |
| `availableBalance`| DOUBLE          | Current account balance |
| `address`         | VARCHAR(255)    | Residential address |
| `city`            | VARCHAR(100)    | City of residence |

---

## Running the Application
### Compile
```bash
javac -cp .:mysql-connector-java-8.0.33.jar AtmProject.java
```
### Execute
```bash
java -cp .:mysql-connector-java-8.0.33.jar AtmProject
```
The program will launch a text‑based menu. Follow the on‑screen prompts to register a user or perform transactions.

---

## Usage Guide
1. **Main Menu**
   - `1` – Perform transaction (requires existing debit card & PIN)
   - `2` – Register a new user
   - `3` – Exit the application
2. **Transaction Flow**
   - Enter your 12‑digit debit card number.
   - If the card exists, enter the associated 4‑digit PIN.
   - Choose from:
     - `1` – Check balance
     - `2` – Deposit amount
     - `3` – Withdraw amount (checks for sufficient balance)
     - `4` – Print receipt (shows name and balance)
     - `5` – Exit transaction session
3. **Registration Flow**
   - Provide a unique 12‑digit debit card number.
   - Set a 4‑digit PIN (entered twice for confirmation).
   - Fill in personal details: username, first name, last name, address, city.
   - The system creates the record with an initial balance of **5000.0**.

---

## Project Structure
```
├── AtmProject.java      # Main application source file
└── README.md            # Project documentation (this file)
```

---

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests for:
- Bug fixes
- Feature enhancements (e.g., transaction history, password hashing)
- Refactoring and code cleanup

Please ensure that any new code follows standard Java conventions and includes appropriate comments.

---

## License
This project is licensed under the **MIT License** – see the `LICENSE` file for details.

---

*Happy coding!*

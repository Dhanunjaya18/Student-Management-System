# 🎓 Student Management System
### Java · JDBC · MySQL · MVC Architecture

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-4.2-green)
![Maven](https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 📌 Project Overview

A **fully functional console-based Student Management System** built with Core Java, JDBC, and MySQL. Designed with **layered MVC-style architecture**, this project demonstrates real-world backend development skills including database connectivity, CRUD operations, input validation, and clean code organisation.

> **Resume-ready** — suitable for Java Full Stack Developer fresher roles.

---

## ✨ Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | ➕ Add Student | Insert a new student with validation |
| 2 | ✏️ Update Student | Modify existing student details |
| 3 | 🗑️ Delete Student | Remove a student record (with confirmation) |
| 4 | 📋 View All Students | Display all records in a formatted table |
| 5 | 🔍 Search by ID | Find a specific student by primary key |
| 6 | 🔍 Search by Course | Partial, case-insensitive course search |
| 7 | 📊 Sort by Marks | View leaderboard (highest marks first) |
| 8 | 📁 Export to File | Export all records to `students_export.txt` |
| 9 | 🔐 Admin Login | Protected login with 3-attempt lockout |

---

## 🏗️ Project Structure

```
StudentManagementSystem/
├── pom.xml                          ← Maven build & dependencies
├── sql/
│   └── setup.sql                    ← DB creation + seed data
├── app.log                          ← Auto-generated log file
└── src/
    ├── model/
    │   └── Student.java             ← Entity / data class (Encapsulation)
    ├── dao/
    │   ├── StudentDAO.java          ← Interface (Abstraction)
    │   └── StudentDAOImpl.java      ← JDBC CRUD implementation
    ├── service/
    │   └── StudentService.java      ← Business logic + validation
    ├── util/
    │   ├── DBConnection.java        ← Singleton DB connection manager
    │   ├── InputValidator.java      ← Input validation rules
    │   └── Logger.java              ← File-based logger
    └── main/
        └── Main.java                ← Console UI + entry point
```

---

## 🧩 Class Descriptions

### `model/Student.java`
Represents the Student entity. Uses **encapsulation** (private fields + getters/setters). Maps 1:1 to the `students` table. Overrides `toString()` for formatted console display.

### `util/DBConnection.java`
Manages the MySQL connection via JDBC. Implements a **singleton-style** static factory to reuse one connection. Loads the driver, builds the URL, and exposes `getConnection()` / `closeConnection()`.

### `util/InputValidator.java`
Centralises all validation rules (name length, email regex, marks range 0–100). Keeps DAO and Service layers clean. Returns booleans so callers can branch on validity.

### `util/Logger.java`
Writes timestamped `[INFO]`, `[WARN]`, and `[ERROR]` entries to `app.log` using Java's `FileWriter` + `PrintWriter` in append mode. Uses try-with-resources.

### `dao/StudentDAO.java`
**Interface** defining the CRUD contract. Enables swapping database implementations without changing service logic (Open/Closed Principle).

### `dao/StudentDAOImpl.java`
Concrete JDBC implementation. Uses `PreparedStatement` for all queries. Implements `getAllStudents`, `getStudentById`, `addStudent`, `updateStudent`, `deleteStudent`, `searchByCourse`, and `getStudentsSortedByMarks`.

### `service/StudentService.java`
**Business logic layer.** Validates inputs before calling the DAO, adds user-friendly messages, and returns safe defaults (empty list instead of null). Accepts a DAO via constructor injection (testable).

### `main/Main.java`
Console **UI layer**. Displays menus, reads user input safely, calls `StudentService`. Never touches the database directly. Includes admin login, formatted table printing, and file export.

---

## 🗄️ Database Schema

```sql
CREATE TABLE students (
    student_id  INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    course      VARCHAR(100) NOT NULL,
    marks       DECIMAL(5,2) NOT NULL CHECK (marks >= 0 AND marks <= 100),
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (student_id)
);
```

---

## ⚙️ Setup Instructions

### Prerequisites

| Tool | Version | Download |
|------|---------|---------|
| JDK | 17+ | [adoptium.net](https://adoptium.net) |
| MySQL Server | 8.0+ | [mysql.com](https://dev.mysql.com/downloads/) |
| Maven | 3.8+ | [maven.apache.org](https://maven.apache.org) |
| IDE | IntelliJ IDEA / VS Code | See below |

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/yourusername/StudentManagementSystem.git
cd StudentManagementSystem
```

### Step 2 — Set Up the Database

Open MySQL Workbench or the MySQL CLI:

```bash
mysql -u root -p
```

Then run the setup script:

```sql
SOURCE sql/setup.sql;
```

Or paste the contents of `sql/setup.sql` directly.

### Step 3 — Configure DB Credentials

Edit `src/util/DBConnection.java`:

```java
private static final String USER     = "root";       // Your MySQL username
private static final String PASSWORD = "yourpassword"; // Your MySQL password
```

### Step 4 — Build the Project

```bash
mvn clean package
```

This produces `target/StudentManagementSystem-runnable.jar`.

### Step 5 — Run the Application

```bash
java -jar target/StudentManagementSystem-runnable.jar
```

---

## 🖥️ Running in IntelliJ IDEA

1. Open IntelliJ → **File → Open** → select the project folder.
2. IntelliJ detects `pom.xml` and loads Maven automatically.
3. Wait for dependencies to download (MySQL connector).
4. Open `src/main/Main.java`.
5. Right-click → **Run 'Main.main()'**.
6. Interact in the **Run** console at the bottom.

> ⚠️ Make sure the Project SDK is set to JDK 17: **File → Project Structure → SDK**.

---

## 🖥️ Running in VS Code

1. Install extensions: **Extension Pack for Java** + **Maven for Java**.
2. Open the project folder: `File → Open Folder`.
3. VS Code auto-detects the Maven project.
4. Open `src/main/Main.java`.
5. Click the ▶ **Run** button above `main()`.

---

## 🔐 Default Admin Login

```
Username: admin
Password: admin123
```

(Change these in `Main.java` before deploying.)

---

## 📟 Sample Output

```
╔══════════════════════════════════════════════════════╗
║        STUDENT MANAGEMENT SYSTEM  v1.0              ║
║        Built with Java + JDBC + MySQL               ║
╚══════════════════════════════════════════════════════╝

╔══════════════════════════════╗
║         ADMIN LOGIN          ║
╚══════════════════════════════╝
Username: admin
Password: admin123

✔ Login successful! Welcome, admin.

┌─────────────────────────────────┐
│         MAIN MENU               │
├─────────────────────────────────┤
│  1. Add Student                 │
│  2. Update Student              │
│  3. Delete Student              │
│  4. View All Students           │
│  5. Search Student by ID        │
│  6. Search Students by Course   │
│  7. View Students by Marks      │
│  8. Export Records to File      │
│  0. Exit                        │
└─────────────────────────────────┘

--- ALL STUDENTS ---
==========================================================================================
| ID     | Name                 | Email                        | Course             | Marks  |
------------------------------------------------------------------------------------------
| 1      | Arjun Sharma         | arjun.sharma@email.com       | B.Tech CSE         | 92.50  |
| 2      | Priya Patel          | priya.patel@email.com        | B.Tech IT          | 88.00  |
| 4      | Sneha Gupta          | sneha.gupta@email.com        | MCA                | 95.00  |
==========================================================================================
Total records: 10
```

---

## 🧠 OOP Concepts Demonstrated

| Concept | Where Used |
|---------|-----------|
| **Encapsulation** | `Student.java` — private fields + getters/setters |
| **Abstraction** | `StudentDAO.java` — interface hides implementation |
| **Inheritance** | `StudentDAOImpl implements StudentDAO` |
| **Polymorphism** | DAO reference holds `StudentDAOImpl` instance |
| **Single Responsibility** | Each class has one job (Model/DAO/Service/View) |
| **Open/Closed Principle** | New DB impl can be added without changing Service |
| **Dependency Injection** | `StudentService` accepts a DAO via constructor |

---

## 🔄 JDBC Workflow

```
┌──────────┐     ┌────────────────┐     ┌──────────────────┐     ┌─────────┐
│  Java App│────▶│ DBConnection   │────▶│  DriverManager   │────▶│  MySQL  │
│ (Main.java)    │ .getConnection()│     │ .getConnection() │     │   DB    │
└──────────┘     └────────────────┘     └──────────────────┘     └─────────┘
      │
      ▼
 PreparedStatement  →  executeUpdate() / executeQuery()
      │
      ▼
  ResultSet  →  rs.next() / rs.getString() / rs.getInt()
      │
      ▼
  Connection.close()
```

**Steps:**
1. `Class.forName()` — register the JDBC driver
2. `DriverManager.getConnection(url, user, pass)` — open connection
3. `conn.prepareStatement(sql)` — create a pre-compiled statement
4. `ps.setXxx(index, value)` — bind parameters
5. `ps.executeUpdate()` / `ps.executeQuery()` — run the SQL
6. Iterate `ResultSet` with `rs.next()`
7. Close resources (try-with-resources handles this)

---

## ❓ Interview Questions & Answers

**Q1. What is JDBC and how does it work?**
> JDBC (Java Database Connectivity) is a Java API that provides a standard interface for connecting Java applications to relational databases. It works through a driver (e.g., `mysql-connector-j`) that translates JDBC calls into database-specific network protocol.

**Q2. Why use PreparedStatement instead of Statement?**
> `PreparedStatement` pre-compiles the SQL on the database server. Benefits: (1) prevents SQL injection because parameters are escaped, (2) faster for repeated execution since the query plan is cached, (3) handles type conversion automatically. `Statement` concatenates user input directly into SQL strings — a serious security risk.

**Q3. What is the DAO pattern and why use it?**
> DAO (Data Access Object) separates database logic from business logic. It defines an interface for CRUD operations, with one or more concrete implementations. This means you can switch databases (MySQL → PostgreSQL) by writing a new DAO class without changing the rest of the application.

**Q4. Why is MVC useful in this project?**
> MVC separates concerns: Main.java (View) handles UI; StudentService (Controller) handles business logic; Student.java (Model) represents data. This makes each part independently testable, maintainable, and replaceable.

**Q5. What is try-with-resources and why do we use it?**
> A Java 7+ feature that automatically closes resources (`Connection`, `PreparedStatement`, `ResultSet`) when the block exits — even if an exception is thrown. This prevents resource/connection leaks that would eventually crash the application.

**Q6. What is SQL Injection and how is it prevented here?**
> SQL Injection is an attack where a user enters SQL code as input (e.g., `'; DROP TABLE students; --`). Using `PreparedStatement` prevents this because parameter values are always treated as data, never as SQL code.

**Q7. Explain the difference between executeQuery() and executeUpdate().**
> `executeQuery()` is used for `SELECT` statements — returns a `ResultSet`. `executeUpdate()` is used for `INSERT`, `UPDATE`, `DELETE` — returns the number of rows affected.

**Q8. What OOP concepts did you apply in this project?**
> Encapsulation (private fields in Student), Abstraction (StudentDAO interface), Inheritance (StudentDAOImpl implements StudentDAO), Polymorphism (DAO reference holds impl), Single Responsibility (each layer has one job), Dependency Injection (service accepts DAO via constructor).

**Q9. How does the Singleton pattern apply to DBConnection?**
> `DBConnection.getConnection()` checks if a connection already exists and is open. If not, it creates one. This ensures the entire application shares one connection rather than opening a new one per query, which is expensive.

**Q10. What is the difference between DECIMAL and DOUBLE in MySQL?**
> `DECIMAL(5,2)` stores exact values (e.g., 98.75) without floating-point rounding — critical for marks and financial data. `DOUBLE` is a floating-point type that may have small precision errors. Always use `DECIMAL` for marks, prices, etc.

---

## 📄 Resume-Ready Project Description

```
Student Management System | Java, JDBC, MySQL, Maven
• Developed a console-based CRUD application using Core Java 17 and JDBC
  to manage 500+ student records stored in a MySQL database.
• Implemented MVC-style layered architecture (Model/DAO/Service/View)
  ensuring clean separation of concerns and maintainability.
• Used PreparedStatement for all SQL operations, eliminating SQL injection
  vulnerabilities and improving query performance.
• Applied OOP principles: encapsulation, abstraction (DAO interface),
  and dependency injection for testable service layer.
• Built input validation, file-based logging, admin login with lockout,
  data export to file, and result sorting/filtering features.
• Managed dependencies and build lifecycle using Apache Maven.
```

---

## 📦 Dependencies

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

---

## 📃 License

This project is open source under the MIT License. Feel free to use it as a learning reference or portfolio project.

---

## 🙋 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [linkedin.com/in/yourprofile](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

> ⭐ If this project helped you, please give it a star on GitHub!

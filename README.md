# 🎓 Student Management System
### Java · JDBC · MySQL · MVC Architecture


## 📌 Project Overview

A **fully functional console-based Student Management System** built with Core Java, JDBC, and MySQL. Designed with **layered MVC-style architecture**, this project demonstrates real-world backend development skills including database connectivity, CRUD operations, input validation, and clean code organisation.

## 🛠️ Tech Stack

- Java
- JDBC
- MySQL
- Maven
- SQL
- OOPs
- MVC Architecture
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


## 📟 Sample Output

```
╔══════════════════════════════════════════════════════╗
║        STUDENT MANAGEMENT SYSTEM  v1.0              ║
║        Built with Java + JDBC + MySQL               ║
╚══════════════════════════════════════════════════════╝

╔══════════════════════════════╗
║         ADMIN LOGIN          ║
╚══════════════════════════════╝


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
.

---

## 🙋 Author

Dhanunjaya Reddy
- GitHub: https://github.com/Dhanunjaya18
- LinkedIn: https://www.linkedin.com/in/dhanunjaya--reddy/
- Email: dhanunjaya0340@gmail.com

---

> ⭐ If this project helped you, please give it a star on GitHub!

-- ============================================================
-- Student Management System — Database Setup Script
-- MySQL 5.7+ / 8.0+
-- Run this script once before starting the application.
-- ============================================================

-- Step 1: Create the database (if it doesn't already exist)
CREATE DATABASE IF NOT EXISTS student_management
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Step 2: Switch to the database
USE student_management;

-- Step 3: Create the students table
-- DROP TABLE IF EXISTS students;   -- Uncomment to reset the table
CREATE TABLE IF NOT EXISTS students (
    student_id  INT          NOT NULL AUTO_INCREMENT,  -- Primary Key, auto-generated
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,           -- UNIQUE prevents duplicate emails
    course      VARCHAR(100) NOT NULL,
    marks       DECIMAL(5,2) NOT NULL                   -- e.g. 98.75 (max 100.00)
                 CHECK (marks >= 0 AND marks <= 100),
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP, -- Audit: when was the row created?
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
                 ON UPDATE CURRENT_TIMESTAMP,           -- Audit: when was it last changed?
    PRIMARY KEY (student_id)
) ENGINE=InnoDB;

-- ─── Indexes ──────────────────────────────────────────────────────────────────
-- Speeds up course-based searches (used in searchByCourse feature)
CREATE INDEX IF NOT EXISTS idx_course ON students(course);

-- ─── Sample / Seed Data ───────────────────────────────────────────────────────
-- Remove or comment out these lines after initial testing
INSERT INTO students (name, email, course, marks) VALUES
    ('Arjun Sharma',     'arjun.sharma@email.com',     'B.Tech CSE',            92.50),
    ('Priya Patel',      'priya.patel@email.com',      'B.Tech IT',             88.00),
    ('Rohit Verma',      'rohit.verma@email.com',      'BCA',                   75.25),
    ('Sneha Gupta',      'sneha.gupta@email.com',      'MCA',                   95.00),
    ('Karan Singh',      'karan.singh@email.com',      'B.Tech CSE',            67.80),
    ('Anjali Mehta',     'anjali.mehta@email.com',     'B.Sc Computer Science', 81.40),
    ('Vikram Joshi',     'vikram.joshi@email.com',     'MBA Tech',              72.60),
    ('Nisha Reddy',      'nisha.reddy@email.com',      'B.Tech IT',             89.90),
    ('Amit Kumar',       'amit.kumar@email.com',       'BCA',                   55.00),
    ('Pooja Agarwal',    'pooja.agarwal@email.com',    'MCA',                   97.25);

-- ─── Verification Queries (run these to confirm setup) ────────────────────────
-- SELECT * FROM students;
-- SHOW COLUMNS FROM students;
-- SHOW INDEX FROM students;
-- SELECT COUNT(*) AS total_students FROM students;
-- SELECT course, COUNT(*) AS count, AVG(marks) AS avg_marks FROM students GROUP BY course;

-- ─── Useful SQL Commands for Reference ───────────────────────────────────────
-- SELECT * FROM students ORDER BY marks DESC;                           -- Sort by marks
-- SELECT * FROM students WHERE LOWER(course) LIKE LOWER('%cse%');       -- Search by course
-- SELECT * FROM students WHERE student_id = 1;                          -- Search by ID
-- UPDATE students SET marks = 90.0 WHERE student_id = 1;                -- Update marks
-- DELETE FROM students WHERE student_id = 10;                           -- Delete student

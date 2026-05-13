package main;

import model.Student;
import service.StudentService;
import util.DBConnection;
import util.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java — Entry point and Console UI (View in MVC).
 *
 * Responsibilities:
 *  - Display menus and capture user input
 *  - Delegate all logic to StudentService
 *  - Never touch the database directly
 *
 * Architecture note (MVC):
 *  Model   → Student.java          (data)
 *  View    → Main.java             (UI / console)
 *  Controller → StudentService.java (business logic + coordination)
 *  DAO     → StudentDAOImpl.java    (data access, not strictly MVC but layered)
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService service = new StudentService();

    // Admin credentials (in real projects, store hashed in DB)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    // ─── Entry Point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        printBanner();

        // Require login before accessing the system
        if (!adminLogin()) {
            System.out.println("\nAccess denied. Exiting application.");
            Logger.warn("Failed admin login attempt.");
            return;
        }

        Logger.info("Admin logged in successfully.");
        runMainMenu();

        // Cleanup on exit
        DBConnection.closeConnection();
        scanner.close();
        System.out.println("\nThank you for using Student Management System. Goodbye!");
        Logger.info("Application exited.");
    }

    // ─── Admin Login ──────────────────────────────────────────────────────────

    /**
     * Simple credential check. Allows 3 attempts before locking out.
     */
    private static boolean adminLogin() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║         ADMIN LOGIN          ║");
        System.out.println("╚══════════════════════════════╝");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                System.out.println("\n✔ Login successful! Welcome, " + username + ".");
                return true;
            }

            attempts++;
            int remaining = MAX_ATTEMPTS - attempts;
            System.out.println("✘ Invalid credentials. " +
                    (remaining > 0 ? remaining + " attempt(s) remaining." : "No attempts remaining."));
        }
        return false;
    }

    // ─── Main Menu Loop ───────────────────────────────────────────────────────

    /**
     * Runs the menu in a loop until the user chooses to exit.
     */
    private static void runMainMenu() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1  -> addStudent();
                case 2  -> updateStudent();
                case 3  -> deleteStudent();
                case 4  -> viewAllStudents();
                case 5  -> searchById();
                case 6  -> searchByCourse();
                case 7  -> viewSortedByMarks();
                case 8  -> exportToFile();
                case 0  -> running = false;
                default -> System.out.println("\n⚠  Invalid option. Please enter a number from the menu.");
            }
        }
    }

    // ─── Menu Display ─────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║        STUDENT MANAGEMENT SYSTEM  v1.0              ║");
        System.out.println("║        Built with Java + JDBC + MySQL               ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────────┐");
        System.out.println("│         MAIN MENU               │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│  1. Add Student                 │");
        System.out.println("│  2. Update Student              │");
        System.out.println("│  3. Delete Student              │");
        System.out.println("│  4. View All Students           │");
        System.out.println("│  5. Search Student by ID        │");
        System.out.println("│  6. Search Students by Course   │");
        System.out.println("│  7. View Students by Marks      │");
        System.out.println("│  8. Export Records to File      │");
        System.out.println("│  0. Exit                        │");
        System.out.println("└─────────────────────────────────┘");
    }

    // ─── Feature: Add Student ─────────────────────────────────────────────────

    private static void addStudent() {
        System.out.println("\n--- ADD NEW STUDENT ---");
        String name   = readString("Enter Name   : ");
        String email  = readString("Enter Email  : ");
        String course = readString("Enter Course : ");
        double marks  = readDouble("Enter Marks  : ");

        service.addStudent(name, email, course, marks);
    }

    // ─── Feature: Update Student ──────────────────────────────────────────────

    private static void updateStudent() {
        System.out.println("\n--- UPDATE STUDENT ---");
        int id = readInt("Enter Student ID to update: ");

        // Show existing data first so the user knows what they're changing
        Student existing = service.getStudentById(id);
        if (existing == null) return;

        System.out.println("Current data: ");
        printTableHeader();
        System.out.println(existing);
        printTableFooter();
        System.out.println("(Enter new values — press Enter to keep current value)");

        String name   = readStringWithDefault("Enter Name   [" + existing.getName()   + "]: ", existing.getName());
        String email  = readStringWithDefault("Enter Email  [" + existing.getEmail()  + "]: ", existing.getEmail());
        String course = readStringWithDefault("Enter Course [" + existing.getCourse() + "]: ", existing.getCourse());
        double marks  = readDoubleWithDefault ("Enter Marks  [" + existing.getMarks() + "]: ", existing.getMarks());

        service.updateStudent(id, name, email, course, marks);
    }

    // ─── Feature: Delete Student ──────────────────────────────────────────────

    private static void deleteStudent() {
        System.out.println("\n--- DELETE STUDENT ---");
        int id = readInt("Enter Student ID to delete: ");

        System.out.print("Are you sure you want to delete student ID " + id + "? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if ("yes".equalsIgnoreCase(confirm)) {
            service.deleteStudent(id);
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    // ─── Feature: View All Students ───────────────────────────────────────────

    private static void viewAllStudents() {
        System.out.println("\n--- ALL STUDENTS ---");
        List<Student> students = service.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found in the database.");
            return;
        }

        printTableHeader();
        students.forEach(System.out::println);
        printTableFooter();
        System.out.println("Total records: " + students.size());
    }

    // ─── Feature: Search by ID ────────────────────────────────────────────────

    private static void searchById() {
        System.out.println("\n--- SEARCH STUDENT BY ID ---");
        int id = readInt("Enter Student ID: ");

        Student student = service.getStudentById(id);
        if (student != null) {
            printTableHeader();
            System.out.println(student);
            printTableFooter();
        }
    }

    // ─── Feature: Search by Course ────────────────────────────────────────────

    private static void searchByCourse() {
        System.out.println("\n--- SEARCH STUDENTS BY COURSE ---");
        String course = readString("Enter Course name (partial match OK): ");

        List<Student> students = service.searchByCourse(course);
        if (!students.isEmpty()) {
            printTableHeader();
            students.forEach(System.out::println);
            printTableFooter();
            System.out.println("Found: " + students.size() + " student(s).");
        }
    }

    // ─── Feature: Sort by Marks ───────────────────────────────────────────────

    private static void viewSortedByMarks() {
        System.out.println("\n--- STUDENTS SORTED BY MARKS (Highest First) ---");
        List<Student> students = service.getStudentsSortedByMarks();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        printTableHeader();
        int rank = 1;
        for (Student s : students) {
            System.out.printf("  #%-3d %s%n", rank++, s);
        }
        printTableFooter();
    }

    // ─── Feature: Export to File ──────────────────────────────────────────────

    /**
     * Exports all student records to a plain-text file "students_export.txt".
     */
    private static void exportToFile() {
        System.out.println("\n--- EXPORT RECORDS TO FILE ---");
        List<Student> students = service.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No data to export.");
            return;
        }

        String filename = "students_export.txt";
        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
            pw.println("STUDENT MANAGEMENT SYSTEM — DATA EXPORT");
            pw.println("Generated: " + java.time.LocalDateTime.now());
            pw.println("=".repeat(90));
            pw.println(buildTableHeader());
            pw.println("-".repeat(90));
            students.forEach(pw::println);
            pw.println("=".repeat(90));
            pw.println("Total Records: " + students.size());

            System.out.println("✔ Records exported to: " + filename);
            Logger.info("Exported " + students.size() + " records to " + filename);

        } catch (java.io.IOException e) {
            System.err.println("✘ Export failed: " + e.getMessage());
            Logger.error("Export to file failed: " + e.getMessage());
        }
    }

    // ─── Console Table Helpers ────────────────────────────────────────────────

    private static String buildTableHeader() {
        return String.format("| %-6s | %-20s | %-28s | %-18s | %-6s |",
                "ID", "Name", "Email", "Course", "Marks");
    }

    private static void printTableHeader() {
        System.out.println("=".repeat(90));
        System.out.println(buildTableHeader());
        System.out.println("-".repeat(90));
    }

    private static void printTableFooter() {
        System.out.println("=".repeat(90));
    }

    // ─── Input Helpers ────────────────────────────────────────────────────────

    /** Reads a non-empty String from the user. Loops until valid input is given. */
    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("⚠  Input cannot be empty. Please try again.");
        }
    }

    /** Reads a String; returns defaultValue if the user presses Enter without typing. */
    private static String readStringWithDefault(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    /** Reads an integer, handles non-integer input gracefully. */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("⚠  Please enter a valid integer.");
            }
        }
    }

    /** Reads a double, handles non-numeric input gracefully. */
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠  Please enter a valid number (e.g. 85.5).");
            }
        }
    }

    /** Reads a double; returns defaultValue if the user presses Enter. */
    private static double readDoubleWithDefault(String prompt, double defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("⚠  Invalid number entered. Keeping current value: " + defaultValue);
            return defaultValue;
        }
    }
}

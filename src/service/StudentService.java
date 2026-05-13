package service;

import dao.StudentDAO;
import dao.StudentDAOImpl;
import model.Student;
import util.InputValidator;
import util.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Service layer — sits between Main (UI) and DAO (DB).
 *
 * Responsibilities:
 *  1. Input validation before forwarding to DAO
 *  2. Business rules (e.g. no duplicate emails in future)
 *  3. Logging actions at a business level
 *  4. Returning user-friendly error messages
 *
 * This layer means Main.java stays clean (no validation logic)
 * and DAOImpl stays clean (no business logic) — Single Responsibility Principle.
 */
public class StudentService {

    // Depend on the interface, not the concrete class (Dependency Inversion Principle)
    private final StudentDAO studentDAO;

    /** Constructor — creates a default DAO implementation. */
    public StudentService() {
        this.studentDAO = new StudentDAOImpl();
    }

    /** Constructor injection variant — useful for unit testing with mock DAOs. */
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    // ─── ADD ──────────────────────────────────────────────────────────────────

    /**
     * Validates and adds a new student.
     *
     * @return true if student was saved successfully
     */
    public boolean addStudent(String name, String email, String course, double marks) {
        // Validate first — never send invalid data to the DB
        if (!InputValidator.isValidStudent(name, email, course, marks)) {
            Logger.warn("addStudent(): Validation failed for input: " + email);
            return false;
        }

        Student student = new Student(
                name.trim(),
                email.trim().toLowerCase(),
                course.trim(),
                marks
        );

        boolean result = studentDAO.addStudent(student);
        if (result) {
            System.out.println("\n✔ Student added successfully! Assigned ID: " + student.getStudentId());
        } else {
            System.out.println("\n✘ Failed to add student. Please try again.");
        }
        return result;
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Validates and updates an existing student record.
     *
     * @return true if student was updated successfully
     */
    public boolean updateStudent(int id, String name, String email, String course, double marks) {
        if (!InputValidator.isValidId(id)) {
            System.out.println("[Validation] Student ID must be a positive integer.");
            return false;
        }
        if (!InputValidator.isValidStudent(name, email, course, marks)) {
            Logger.warn("updateStudent(): Validation failed for ID: " + id);
            return false;
        }

        Student student = new Student(id, name.trim(), email.trim().toLowerCase(), course.trim(), marks);

        boolean result = studentDAO.updateStudent(student);
        if (result) {
            System.out.println("\n✔ Student ID " + id + " updated successfully!");
        } else {
            System.out.println("\n✘ Update failed. No student found with ID: " + id);
        }
        return result;
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Deletes a student after confirming the record exists.
     *
     * @return true if deletion was successful
     */
    public boolean deleteStudent(int id) {
        if (!InputValidator.isValidId(id)) {
            System.out.println("[Validation] Student ID must be a positive integer.");
            return false;
        }

        // Check existence before deleting — provides a better error message
        Student existing = studentDAO.getStudentById(id);
        if (existing == null) {
            System.out.println("\n✘ No student found with ID: " + id);
            return false;
        }

        boolean result = studentDAO.deleteStudent(id);
        if (result) {
            System.out.println("\n✔ Student \"" + existing.getName() + "\" (ID: " + id + ") deleted successfully!");
        } else {
            System.out.println("\n✘ Delete failed. Please try again.");
        }
        return result;
    }

    // ─── VIEW ALL ─────────────────────────────────────────────────────────────

    /**
     * Returns all students. Returns an empty list (never null) on failure.
     */
    public List<Student> getAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        if (students == null) {
            Logger.warn("getAllStudents() returned null from DAO.");
            return Collections.emptyList();
        }
        return students;
    }

    // ─── SEARCH BY ID ─────────────────────────────────────────────────────────

    /**
     * Fetches a single student by ID with validation.
     *
     * @return Student object or null if not found / invalid ID
     */
    public Student getStudentById(int id) {
        if (!InputValidator.isValidId(id)) {
            System.out.println("[Validation] Student ID must be a positive integer.");
            return null;
        }

        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("\n✘ No student found with ID: " + id);
        }
        return student;
    }

    // ─── SEARCH BY COURSE ─────────────────────────────────────────────────────

    /**
     * Searches students by course name (partial, case-insensitive).
     */
    public List<Student> searchByCourse(String course) {
        if (!InputValidator.isValidCourse(course)) {
            System.out.println("[Validation] Course name cannot be empty.");
            return Collections.emptyList();
        }
        List<Student> students = studentDAO.searchByCourse(course.trim());
        if (students.isEmpty()) {
            System.out.println("\n✘ No students found for course: " + course);
        }
        return students;
    }

    // ─── SORT BY MARKS ────────────────────────────────────────────────────────

    /**
     * Returns all students sorted by marks (highest first).
     */
    public List<Student> getStudentsSortedByMarks() {
        List<Student> students = studentDAO.getStudentsSortedByMarks();
        if (students == null) {
            return Collections.emptyList();
        }
        return students;
    }
}

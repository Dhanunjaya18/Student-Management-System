package dao;

import model.Student;
import util.DBConnection;
import util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of StudentDAO.
 *
 * ALL database operations live here — the rest of the application
 * never touches raw SQL or JDBC objects directly.
 *
 * Key JDBC objects used:
 *  - Connection       : represents the open DB session
 *  - PreparedStatement: pre-compiled SQL with ? placeholders (prevents SQL injection)
 *  - ResultSet        : cursor over rows returned by a SELECT query
 *
 * Why PreparedStatement over Statement?
 *  1. Prevents SQL Injection attacks
 *  2. Pre-compiled → faster for repeated calls
 *  3. Handles type conversion automatically (e.g. int, double, String)
 */
public class StudentDAOImpl implements StudentDAO {

    // ─── SQL Constants ─────────────────────────────────────────────────────────

    private static final String INSERT_SQL =
            "INSERT INTO students (name, email, course, marks) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE students SET name=?, email=?, course=?, marks=? WHERE student_id=?";

    private static final String DELETE_SQL =
            "DELETE FROM students WHERE student_id=?";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM students ORDER BY student_id";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM students WHERE student_id=?";

    private static final String SEARCH_BY_COURSE_SQL =
            "SELECT * FROM students WHERE LOWER(course) LIKE LOWER(?)";

    private static final String SORT_BY_MARKS_SQL =
            "SELECT * FROM students ORDER BY marks DESC";

    // ─── ADD Student ───────────────────────────────────────────────────────────

    /**
     * Inserts a new student. Uses PreparedStatement with auto-generated key retrieval.
     */
    @Override
    public boolean addStudent(Student student) {
        // try-with-resources: PreparedStatement is closed automatically
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Bind parameters — ? placeholders replaced in order
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setDouble(4, student.getMarks());

            int rowsAffected = ps.executeUpdate();

            // Read back the auto-generated primary key
            if (rowsAffected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        student.setStudentId(keys.getInt(1));
                        Logger.info("Student added with ID: " + student.getStudentId());
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            Logger.error("addStudent() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return false;
    }

    // ─── UPDATE Student ────────────────────────────────────────────────────────

    /**
     * Updates all editable fields for the student identified by studentId.
     */
    @Override
    public boolean updateStudent(Student student) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setDouble(4, student.getMarks());
            ps.setInt(5, student.getStudentId());   // WHERE clause

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                Logger.info("Student updated. ID: " + student.getStudentId());
                return true;
            } else {
                Logger.warn("updateStudent(): No record found for ID " + student.getStudentId());
            }

        } catch (SQLException e) {
            Logger.error("updateStudent() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return false;
    }

    // ─── DELETE Student ────────────────────────────────────────────────────────

    /**
     * Removes the student row with the given primary key.
     */
    @Override
    public boolean deleteStudent(int studentId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, studentId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                Logger.info("Student deleted. ID: " + studentId);
                return true;
            } else {
                Logger.warn("deleteStudent(): No record found for ID " + studentId);
            }

        } catch (SQLException e) {
            Logger.error("deleteStudent() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return false;
    }

    // ─── GET ALL Students ──────────────────────────────────────────────────────

    /**
     * Fetches every row from the students table.
     * Uses mapResultSet() helper to avoid code duplication.
     */
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSet(rs));
            }
            Logger.info("getAllStudents(): fetched " + students.size() + " records.");

        } catch (SQLException e) {
            Logger.error("getAllStudents() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return students;
    }

    // ─── GET Student By ID ─────────────────────────────────────────────────────

    /**
     * Returns a single Student matching the given ID, or null if not found.
     */
    @Override
    public Student getStudentById(int studentId) {
        Student student = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = mapResultSet(rs);
                    Logger.info("getStudentById(): found student ID " + studentId);
                } else {
                    Logger.warn("getStudentById(): no student found for ID " + studentId);
                }
            }

        } catch (SQLException e) {
            Logger.error("getStudentById() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return student;
    }

    // ─── SEARCH By Course ─────────────────────────────────────────────────────

    /**
     * Case-insensitive partial match on course name.
     * e.g. searching "java" will match "Core Java", "Java EE", etc.
     */
    @Override
    public List<Student> searchByCourse(String course) {
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_BY_COURSE_SQL)) {

            ps.setString(1, "%" + course + "%");   // Wildcard for LIKE

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSet(rs));
                }
            }
            Logger.info("searchByCourse('" + course + "'): found " + students.size() + " records.");

        } catch (SQLException e) {
            Logger.error("searchByCourse() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return students;
    }

    // ─── SORT By Marks ────────────────────────────────────────────────────────

    /**
     * Returns all students ordered by marks (highest first).
     */
    @Override
    public List<Student> getStudentsSortedByMarks() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SORT_BY_MARKS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSet(rs));
            }
            Logger.info("getStudentsSortedByMarks(): fetched " + students.size() + " records.");

        } catch (SQLException e) {
            Logger.error("getStudentsSortedByMarks() failed: " + e.getMessage());
            System.err.println("[DAO Error] " + e.getMessage());
        }
        return students;
    }

    // ─── Private Helper ───────────────────────────────────────────────────────

    /**
     * Maps the current row of a ResultSet to a Student object.
     * Centralises column-name references to one place.
     *
     * @param rs ResultSet positioned at a valid row
     * @return populated Student object
     * @throws SQLException if a column name is wrong
     */
    private Student mapResultSet(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("student_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("course"),
            rs.getDouble("marks")
        );
    }
}

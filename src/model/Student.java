package model;

/**
 * Model class representing a Student entity.
 * Demonstrates OOP principle: Encapsulation (private fields + getters/setters).
 * Maps directly to the 'students' table in the database.
 */
public class Student {

    // Private fields — Encapsulation
    private int studentId;
    private String name;
    private String email;
    private String course;
    private double marks;

    // ─── Constructors ──────────────────────────────────────────────────────────

    /** Default (no-arg) constructor */
    public Student() {}

    /**
     * Parameterized constructor — used when creating a new student record.
     * studentId is omitted here because MySQL AUTO_INCREMENT generates it.
     */
    public Student(String name, String email, String course, double marks) {
        this.name   = name;
        this.email  = email;
        this.course = course;
        this.marks  = marks;
    }

    /**
     * Full constructor — used when reading records back from the database.
     */
    public Student(int studentId, String name, String email, String course, double marks) {
        this.studentId = studentId;
        this.name      = name;
        this.email     = email;
        this.course    = course;
        this.marks     = marks;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public int getStudentId()             { return studentId; }
    public void setStudentId(int id)      { this.studentId = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getCourse()             { return course; }
    public void setCourse(String course)  { this.course = course; }

    public double getMarks()              { return marks; }
    public void setMarks(double marks)    { this.marks = marks; }

    // ─── toString ──────────────────────────────────────────────────────────────

    /**
     * Neatly formatted string representation used for console display.
     */
    @Override
    public String toString() {
        return String.format(
            "| %-6d | %-20s | %-28s | %-18s | %-6.2f |",
            studentId, name, email, course, marks
        );
    }
}

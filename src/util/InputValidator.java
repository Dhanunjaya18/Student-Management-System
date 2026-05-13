package util;

/**
 * Utility class for validating user input before it reaches the database.
 *
 * Centralising validation here keeps the DAO and Service layers clean.
 * Every method returns a boolean so callers can branch on validity.
 */
public class InputValidator {

    // Simple regex for basic email format checking
    private static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

    /**
     * Checks that a name is non-null, non-empty, and ≤ 100 characters.
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 100;
    }

    /**
     * Validates email format using a simple regex.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    /**
     * Checks that a course name is non-null, non-empty, and ≤ 100 characters.
     */
    public static boolean isValidCourse(String course) {
        return course != null && !course.trim().isEmpty() && course.trim().length() <= 100;
    }

    /**
     * Marks must be between 0 and 100 (inclusive).
     */
    public static boolean isValidMarks(double marks) {
        return marks >= 0.0 && marks <= 100.0;
    }

    /**
     * Student ID must be a positive integer.
     */
    public static boolean isValidId(int id) {
        return id > 0;
    }

    /**
     * Convenience: validates all fields of a Student at once.
     * Prints which field failed so the caller can surface a useful message.
     *
     * @return true only if every field passes validation
     */
    public static boolean isValidStudent(String name, String email, String course, double marks) {
        if (!isValidName(name)) {
            System.out.println("[Validation] Name is invalid (empty or > 100 chars).");
            return false;
        }
        if (!isValidEmail(email)) {
            System.out.println("[Validation] Email format is invalid. Example: user@domain.com");
            return false;
        }
        if (!isValidCourse(course)) {
            System.out.println("[Validation] Course is invalid (empty or > 100 chars).");
            return false;
        }
        if (!isValidMarks(marks)) {
            System.out.println("[Validation] Marks must be between 0.0 and 100.0.");
            return false;
        }
        return true;
    }
}

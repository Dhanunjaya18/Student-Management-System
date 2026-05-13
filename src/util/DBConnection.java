package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing MySQL database connections.
 *
 * Design pattern: Singleton-style static factory method.
 * This ensures we reuse a single connection throughout the app
 * rather than opening a new one for every operation.
 *
 * JDBC Workflow:
 *  Step 1 — Load driver   : Class.forName("com.mysql.cj.jdbc.Driver")
 *  Step 2 — Get connection: DriverManager.getConnection(URL, USER, PASS)
 *  Step 3 — Use connection: create statements, execute queries
 *  Step 4 — Close         : connection.close()
 */
public class DBConnection {

    // ─── Database Configuration ────────────────────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/student_management"
                                         + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER     = "root";        // Change to your MySQL username
    private static final String PASSWORD = "root";        // Change to your MySQL password

    // Holds the single shared connection
    private static Connection connection = null;

    /**
     * Returns a live Connection object.
     * Opens a new connection only if none exists or the existing one is closed.
     *
     * @return java.sql.Connection ready for use
     * @throws SQLException if the database cannot be reached
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Step 1: Register the MySQL JDBC driver (optional in modern JDBC 4+, but good practice)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Step 2: Establish the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Connection established successfully.");
            }
        } catch (ClassNotFoundException e) {
            // Driver JAR is missing from the classpath
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j to your classpath.", e);
        }
        return connection;
    }

    /**
     * Cleanly closes the connection.
     * Call this when the application shuts down.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DB] Error while closing connection: " + e.getMessage());
            }
        }
    }
}

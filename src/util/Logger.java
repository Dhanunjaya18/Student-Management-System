package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple file-based Logger utility.
 *
 * Writes timestamped log entries to "app.log" in the project root.
 * Levels supported: INFO, WARN, ERROR.
 *
 * Resume highlight: demonstrates understanding of I/O streams,
 * file handling, and logging patterns in Java.
 */
public class Logger {

    private static final String LOG_FILE = "app.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ─── Public Logging Methods ────────────────────────────────────────────────

    public static void info(String message) {
        log("INFO ", message);
    }

    public static void warn(String message) {
        log("WARN ", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    // ─── Core Logging Logic ────────────────────────────────────────────────────

    /**
     * Appends a formatted log line to app.log.
     * Uses try-with-resources to guarantee the writer is closed.
     *
     * @param level   log level label (INFO / WARN / ERROR)
     * @param message the message to log
     */
    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry     = String.format("[%s] [%s] %s", timestamp, level, message);

        // try-with-resources — automatically closes the writer
        try (FileWriter fw = new FileWriter(LOG_FILE, true);       // true = append mode
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(entry);
        } catch (IOException e) {
            // Fallback: print to stderr if we can't write the log file
            System.err.println("[Logger] Could not write to log file: " + e.getMessage());
        }
    }
}

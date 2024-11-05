package Views.LogView;

import Models.LogModel.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * The Logger class manages the update of a log file based on notifications
 * received from the LogEntryBuffer.
 * 
 * @author Sumit Sharma
 * @author Muhammad Adnan Asad
 * @author Pranaykumar Chauhan
 * @author Darshansinh Dilipsinh Devda
 * @author Dhruvkumar Parmar
 * @author Dhruv Patel
 * @version 3.0.0
 */
public class Logger extends CustomObserver implements Serializable {
    /**
     * Reference to the LogEntryBuffer observable object.
     */
    LogEntryBuffer d_logEntryBuffer;

    /**
     * Receives and handles notifications for updating log entries from the
     * LogEntryBuffer.
     *
     * @param observable The observable object triggering the update (should be a
     *                   LogEntryBuffer).
     */
    public void update(Object observable) {
        d_logEntryBuffer = (LogEntryBuffer) observable;
        File logFile = new File("LogFile.txt");

        try {
            // Check if the log file exists; if not, create it
            logFile.createNewFile();
            String logMessage = d_logEntryBuffer.getD_logMessage();

            // Clear log file if a specific starting message is encountered
            if (logMessage.equals("STARTING WARZONE.........." + System.lineSeparator() + System.lineSeparator())) {
                Files.newBufferedWriter(Paths.get("LogFile.txt"), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }

            // Append or create log entries in the file
            Files.write(Paths.get("LogFile.txt"), logMessage.getBytes(StandardCharsets.US_ASCII),
                    logMessage.isEmpty() ? StandardOpenOption.CREATE : StandardOpenOption.APPEND);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

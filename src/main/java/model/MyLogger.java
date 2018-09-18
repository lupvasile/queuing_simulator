package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * The logger class.
 *
 * @author Vasile Lup
 */
public class MyLogger {
    private static List<String> allMessages = new LinkedList<>();

    /**
     * Writes to standard output the message and adds it to the list of messages.
     * @param message the message to all to the log
     */
    public synchronized static void write(String message) {
        System.out.println(message);
        allMessages.add(message);
    }

    /**
     * Writes to LOG.txt all messages which were written in the logger.
     */
    public synchronized static void writeToFile() {
        try {
            Path file = Paths.get("LOG.txt");
            Files.write(file, allMessages);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}

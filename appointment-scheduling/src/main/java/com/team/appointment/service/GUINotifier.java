package com.team.appointment.service;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A {@link Notifier} implementation that appends notification messages
 * to a Swing {@link JTextArea} in the GUI notification log panel.
 * All updates are dispatched on the Event Dispatch Thread via
 * {@link SwingUtilities#invokeLater}.
 *
 * @author Team
 * @version 1.0
 */
public class GUINotifier implements Notifier {

    /** The text area widget where notification messages are appended. */
    private final JTextArea logArea;

    /** Formatter for the timestamp prefix on each log entry. */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a GUINotifier that writes to the given text area.
     *
     * @param logArea the {@link JTextArea} to append messages to
     */
    public GUINotifier(JTextArea logArea) {
        this.logArea = logArea;
    }

    /**
     * Appends the notification message to the GUI log area with a timestamp.
     * The update is performed on the Swing Event Dispatch Thread.
     *
     * @param message the notification text to display
     */
    @Override
    public void send(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry = "[" + timestamp + "] " + message + "\n";
        SwingUtilities.invokeLater(() -> logArea.append(entry));
    }
}

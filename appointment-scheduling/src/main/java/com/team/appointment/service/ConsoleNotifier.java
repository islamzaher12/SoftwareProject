package com.team.appointment.service;

/**
 * A {@link Notifier} implementation that prints notification messages
 * to the standard output (console).  Used in non-GUI contexts and testing.
 *
 * @author Team
 * @version 1.0
 */
public class ConsoleNotifier implements Notifier {

    /**
     * Prints the notification message to {@code System.out}.
     *
     * @param message the notification text to print
     */
    @Override
    public void send(String message) {
        System.out.println("[Notification] " + message);
    }
}

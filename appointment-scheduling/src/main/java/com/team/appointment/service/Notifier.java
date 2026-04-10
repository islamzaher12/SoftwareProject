package com.team.appointment.service;

/**
 * Observer interface for sending appointment notifications.
 * Implementations may dispatch messages via email, console output,
 * GUI log panels, or any other channel.
 *
 * <p>This follows the <em>Observer</em> design pattern: the
 * {@link NotificationService} calls {@code send()} on all registered
 * notifiers without knowing the delivery mechanism.
 *
 * @author Team
 * @version 1.0
 * @see ConsoleNotifier
 * @see EmailNotifier
 * @see GUINotifier
 */
public interface Notifier {

    /**
     * Sends the given notification message through this channel.
     *
     * @param message the notification text to deliver
     */
    void send(String message);
}

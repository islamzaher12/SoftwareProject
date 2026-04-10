package com.team.appointment.service;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link Notifier} that fans out each message to multiple
 * underlying notifiers (Composite pattern).
 *
 * @author Team
 * @version 1.0
 */
public class CompositeNotifier implements Notifier {

    private final List<Notifier> notifiers;

    /**
     * Constructs a CompositeNotifier from the given notifiers.
     *
     * @param notifiers one or more notifier implementations
     */
    public CompositeNotifier(Notifier... notifiers) {
        this.notifiers = Arrays.asList(notifiers);
    }

    /**
     * Sends the message to every registered notifier in order.
     *
     * @param message the notification text
     */
    @Override
    public void send(String message) {
        for (Notifier n : notifiers) {
            n.send(message);
        }
    }
}

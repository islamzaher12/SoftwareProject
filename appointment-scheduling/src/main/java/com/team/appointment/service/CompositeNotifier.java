package com.team.appointment.service;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link Notifier} implementation that fans out every message to a list of
 * delegate notifiers (Composite pattern).
 * Use this to send notifications through multiple channels simultaneously
 * (e.g. email + console + GUI) with a single {@code send()} call.
 *
 * @author Team
 * @version 1.0
 */
public class CompositeNotifier implements Notifier {

    /** The list of delegate notifiers that each receive every message. */
    private final List<Notifier> notifiers;

    /**
     * Constructs a CompositeNotifier backed by the given notifiers.
     *
     * @param notifiers one or more {@link Notifier} instances to delegate to
     */
    public CompositeNotifier(Notifier... notifiers) {
        this.notifiers = Arrays.asList(notifiers);
    }

    /**
     * Forwards the message to every registered notifier.
     *
     * @param message the notification text to send
     */
    @Override
    public void send(String message) {
        for (Notifier n : notifiers) {
            n.send(message);
        }
    }
}

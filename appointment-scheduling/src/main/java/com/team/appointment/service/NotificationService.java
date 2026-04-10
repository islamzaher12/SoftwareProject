package com.team.appointment.service;

import java.util.List;
import com.team.appointment.model.AppointmentSlot;

/**
 * Coordinates sending appointment reminders to all registered notifiers.
 * Iterates over all slots and dispatches a reminder for every booked slot.
 *
 * <p>This class acts as the <em>Subject/Publisher</em> in the Observer pattern;
 * the injected {@link Notifier} is the observer/subscriber.
 *
 * @author Team
 * @version 1.0
 */
public class NotificationService {

    /** Source of slot data used to determine which slots need reminders. */
    private final SlotService slotService;

    /** The notification channel (email, console, GUI, etc.). */
    private final Notifier notifier;

    /**
     * Constructs a NotificationService with the given slot source and notifier.
     *
     * @param slotService the service providing all appointment slots
     * @param notifier    the channel through which reminders are dispatched
     */
    public NotificationService(SlotService slotService, Notifier notifier) {
        this.slotService = slotService;
        this.notifier = notifier;
    }

    /**
     * Sends a reminder message for every booked appointment slot.
     * Slots that are still PENDING are skipped.
     */
    public void sendReminders() {
        List<AppointmentSlot> allSlots = slotService.getAllSlots();

        for (AppointmentSlot slot : allSlots) {
            if (slot.isBooked()) {
                String message = "Reminder: You have an appointment on "
                        + slot.getDate() + " at " + slot.getTime()
                        + " | Type: " + slot.getType()
                        + " | Status: " + slot.getStatus();
                notifier.send(message);
            }
        }
    }
}

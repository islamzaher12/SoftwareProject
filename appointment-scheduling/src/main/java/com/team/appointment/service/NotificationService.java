package com.team.appointment.service;

import java.util.List;
import com.team.appointment.model.AppointmentSlot;

public class NotificationService {

    private final SlotService slotService;
    private final Notifier notifier;

    public NotificationService(SlotService slotService, Notifier notifier) {
        this.slotService = slotService;
        this.notifier = notifier;
    }

    public void sendReminders() {
        List<AppointmentSlot> allSlots = slotService.getAllSlots();

        for (AppointmentSlot slot : allSlots) {
            if (slot.isBooked()) {
                String message = "Reminder: You have an appointment on "
                        + slot.getDate() + " at " + slot.getTime()
                        + ". Status: " + slot.getStatus();
                notifier.send(message);
            }
        }
    }
}
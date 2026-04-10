package com.team.appointment.service;

import java.util.ArrayList;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;

/**
 * Manages the collection of appointment slots available in the system.
 * Pre-populates a set of demo slots on construction; administrators can
 * add new slots at runtime via {@link #addSlot(String, String, int)}.
 *
 * @author Team
 * @version 1.0
 */
public class SlotService {

    /** Internal list of all appointment slots. */
    private final List<AppointmentSlot> slots = new ArrayList<>();

    /**
     * Constructs the SlotService and pre-loads demo slots across two dates.
     */
    public SlotService() {
        // Date 1 – 2026-04-10
        slots.add(new AppointmentSlot("2026-04-10", "09:00", 30));
        slots.add(new AppointmentSlot("2026-04-10", "09:30", 30));
        slots.add(new AppointmentSlot("2026-04-10", "10:00", 30));
        slots.add(new AppointmentSlot("2026-04-10", "10:30", 30));
        slots.add(new AppointmentSlot("2026-04-10", "11:00", 30));
        // Date 2 – 2026-04-11
        slots.add(new AppointmentSlot("2026-04-11", "14:00", 30));
        slots.add(new AppointmentSlot("2026-04-11", "14:30", 30));
        slots.add(new AppointmentSlot("2026-04-11", "15:00", 30));
        slots.add(new AppointmentSlot("2026-04-11", "15:30", 30));
        // Legacy test slots kept for backward compatibility
        slots.add(new AppointmentSlot("2026-03-02", "10:00", 30));
        slots.add(new AppointmentSlot("2026-03-02", "11:00", 30));
        slots.add(new AppointmentSlot("2026-03-02", "12:00", 30));
    }

    /**
     * Returns only the slots that have not yet been booked.
     *
     * @return list of available (unbooked) {@link AppointmentSlot} objects
     */
    public List<AppointmentSlot> getAvailableSlots() {
        List<AppointmentSlot> available = new ArrayList<>();
        for (AppointmentSlot slot : slots) {
            if (!slot.isBooked()) {
                available.add(slot);
            }
        }
        return available;
    }

    /**
     * Returns all slots regardless of booking status.
     *
     * @return the full list of {@link AppointmentSlot} objects
     */
    public List<AppointmentSlot> getAllSlots() {
        return slots;
    }

    /**
     * Adds a new appointment slot to the system (admin operation).
     *
     * @param date     the date in YYYY-MM-DD format
     * @param time     the start time in HH:mm format
     * @param duration the duration in minutes
     */
    public void addSlot(String date, String time, int duration) {
        slots.add(new AppointmentSlot(date, time, duration));
    }
}

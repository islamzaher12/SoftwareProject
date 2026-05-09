package com.team.appointment.service;

import java.util.ArrayList;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;

public class SlotService {

    private static final String DATE_1 = "2026-04-10";
    private static final String DATE_2 = "2026-04-11";
    private static final String LEGACY_DATE = "2026-03-02";

    private final List<AppointmentSlot> slots = new ArrayList<>();

    public SlotService() {
        slots.add(new AppointmentSlot(DATE_1, "09:00", 30));
        slots.add(new AppointmentSlot(DATE_1, "09:30", 30));
        slots.add(new AppointmentSlot(DATE_1, "10:00", 30));
        slots.add(new AppointmentSlot(DATE_1, "10:30", 30));
        slots.add(new AppointmentSlot(DATE_1, "11:00", 30));

        slots.add(new AppointmentSlot(DATE_2, "14:00", 30));
        slots.add(new AppointmentSlot(DATE_2, "14:30", 30));
        slots.add(new AppointmentSlot(DATE_2, "15:00", 30));
        slots.add(new AppointmentSlot(DATE_2, "15:30", 30));

        slots.add(new AppointmentSlot(LEGACY_DATE, "10:00", 30));
        slots.add(new AppointmentSlot(LEGACY_DATE, "11:00", 30));
        slots.add(new AppointmentSlot(LEGACY_DATE, "12:00", 30));
    }

    public List<AppointmentSlot> getAvailableSlots() {
        List<AppointmentSlot> available = new ArrayList<>();
        for (AppointmentSlot slot : slots) {
            if (!slot.isBooked()) {
                available.add(slot);
            }
        }
        return available;
    }

    public List<AppointmentSlot> getAllSlots() {
        return slots;
    }

    public void addSlot(String date, String time, int duration) {
        slots.add(new AppointmentSlot(date, time, duration));
    }
}
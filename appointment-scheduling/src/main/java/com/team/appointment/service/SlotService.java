package com.team.appointment.service;

import java.util.ArrayList;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;

public class SlotService {
    private final List<AppointmentSlot> slots = new ArrayList<>();

    public SlotService() {
        slots.add(new AppointmentSlot("2026-03-02", "10:00"));
        slots.add(new AppointmentSlot("2026-03-02", "11:00"));
        slots.add(new AppointmentSlot("2026-03-02", "12:00"));
    }

    public List<AppointmentSlot> getAvailableSlots() {
        List<AppointmentSlot> available = new ArrayList<>();
        for (AppointmentSlot s : slots) {
            if (!s.isBooked()) {
                available.add(s);
            }
        }
        return available;
    }
}
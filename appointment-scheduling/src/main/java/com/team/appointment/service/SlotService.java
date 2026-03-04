package com.team.appointment.service;

import java.util.ArrayList;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;

public class SlotService {

    private final List<AppointmentSlot> slots = new ArrayList<>();
    private static final int REQUIRED_DURATION = 30;
    private static final int MAX_PARTICIPANTS = 3;
    public SlotService() {
        slots.add(new AppointmentSlot("2026-03-02", "10:00", 30));
        slots.add(new AppointmentSlot("2026-03-02", "11:00", 30));
        slots.add(new AppointmentSlot("2026-03-02", "12:00", 30));
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
    public boolean bookSlot(int index, int duration, int participants) {

        List<AppointmentSlot> available = getAvailableSlots();

        if (index < 1 || index > available.size()) {
            return false;
        }

        if (duration != REQUIRED_DURATION) {
            return false;
        }

        if (participants > MAX_PARTICIPANTS) {
            return false;
        }

        AppointmentSlot slot = available.get(index - 1);
        slot.book(participants);

        return true;
    }}

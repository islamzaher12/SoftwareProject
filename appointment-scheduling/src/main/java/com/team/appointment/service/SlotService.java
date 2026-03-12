package com.team.appointment.service;

import java.util.ArrayList;
import java.util.List;
import com.team.appointment.model.AppointmentSlot;

public class SlotService {

    private final List<AppointmentSlot> slots = new ArrayList<>();
    
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
    public List<AppointmentSlot> getAllSlots() {
        return slots;
    }

}
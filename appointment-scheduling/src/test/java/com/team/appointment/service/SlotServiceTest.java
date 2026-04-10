package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SlotService}.
 * Verifies slot initialisation, availability filtering, and dynamic slot addition.
 *
 * @author Team
 * @version 1.0
 */
public class SlotServiceTest {

    private SlotService slotService;

    @BeforeEach
    void setUp() {
        slotService = new SlotService();
    }

    @Test
    void getAllSlots_shouldReturnNonEmptyList() {
        assertFalse(slotService.getAllSlots().isEmpty());
    }

    @Test
    void getAvailableSlots_initiallyAllAvailable() {
        assertEquals(slotService.getAllSlots().size(),
                     slotService.getAvailableSlots().size());
    }

    @Test
    void getAvailableSlots_afterBooking_oneFewerAvailable() {
        int before = slotService.getAvailableSlots().size();
        slotService.getAllSlots().get(0).book(1);
        int after = slotService.getAvailableSlots().size();
        assertEquals(before - 1, after);
    }

    @Test
    void addSlot_increasesTotalCount() {
        int before = slotService.getAllSlots().size();
        slotService.addSlot("2027-01-01", "08:00", 30);
        assertEquals(before + 1, slotService.getAllSlots().size());
    }

    @Test
    void addSlot_newSlotIsAvailable() {
        slotService.addSlot("2027-01-01", "08:00", 30);
        int last = slotService.getAllSlots().size() - 1;
        assertFalse(slotService.getAllSlots().get(last).isBooked());
    }

    @Test
    void addSlot_preservesDateAndTime() {
        slotService.addSlot("2027-06-15", "14:30", 30);
        int last = slotService.getAllSlots().size() - 1;
        assertEquals("2027-06-15", slotService.getAllSlots().get(last).getDate());
        assertEquals("14:30",      slotService.getAllSlots().get(last).getTime());
    }

    @Test
    void getAllSlots_returnsAllRegardlessOfBookingStatus() {
        int total = slotService.getAllSlots().size();
        slotService.getAllSlots().get(0).book(2);
        assertEquals(total, slotService.getAllSlots().size());
    }
}

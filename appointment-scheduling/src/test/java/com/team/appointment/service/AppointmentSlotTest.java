package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;

/**
 * Unit tests for {@link AppointmentSlot} model behaviour.
 *
 * @author Team
 * @version 1.0
 */
public class AppointmentSlotTest {

    private AppointmentSlot slot;

    @BeforeEach
    void setUp() {
        slot = new AppointmentSlot("2026-05-01", "10:00", 30);
    }

    @Test
    void newSlot_isNotBooked() {
        assertFalse(slot.isBooked());
    }

    @Test
    void newSlot_statusIsPending() {
        assertEquals("PENDING", slot.getStatus());
    }

    @Test
    void newSlot_typeIsNull() {
        assertNull(slot.getType());
    }

    @Test
    void book_setsBookedTrue() {
        slot.book(2, AppointmentType.GROUP);
        assertTrue(slot.isBooked());
    }

    @Test
    void book_setsStatusConfirmed() {
        slot.book(1, AppointmentType.URGENT);
        assertEquals("CONFIRMED", slot.getStatus());
    }

    @Test
    void book_storesType() {
        slot.book(1, AppointmentType.VIRTUAL);
        assertEquals(AppointmentType.VIRTUAL, slot.getType());
    }

    @Test
    void cancel_resetsBooked() {
        slot.book(1);
        slot.cancel();
        assertFalse(slot.isBooked());
    }

    @Test
    void cancel_resetsStatusToPending() {
        slot.book(1);
        slot.cancel();
        assertEquals("PENDING", slot.getStatus());
    }

    @Test
    void cancel_resetsTypeToNull() {
        slot.book(1, AppointmentType.FOLLOW_UP);
        slot.cancel();
        assertNull(slot.getType());
    }

    @Test
    void canAcceptParticipants_validRange_true() {
        assertTrue(slot.canAcceptParticipants(1));
        assertTrue(slot.canAcceptParticipants(3));
    }

    @Test
    void canAcceptParticipants_zero_false() {
        assertFalse(slot.canAcceptParticipants(0));
    }

    @Test
    void canAcceptParticipants_overMax_false() {
        assertFalse(slot.canAcceptParticipants(4));
    }

    @Test
    void getDuration_returnsCorrectValue() {
        assertEquals(30, slot.getDuration());
    }

    @Test
    void getDate_returnsCorrectValue() {
        assertEquals("2026-05-01", slot.getDate());
    }

    @Test
    void getTime_returnsCorrectValue() {
        assertEquals("10:00", slot.getTime());
    }

    @Test
    void toString_containsDate() {
        assertTrue(slot.toString().contains("2026-05-01"));
    }
}

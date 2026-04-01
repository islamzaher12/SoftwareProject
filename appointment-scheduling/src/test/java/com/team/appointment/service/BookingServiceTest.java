package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookingServiceTest {

    private SlotService slotService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        slotService = new SlotService();
        bookingService = new BookingService(slotService);
    }

    @Test
    void bookAppointment_valid_shouldSucceed() {
        boolean result = bookingService.bookAppointment(1, 30, 2);
        assertTrue(result);
    }

    @Test
    void bookAppointment_wrongDuration_shouldFail() {
        boolean result = bookingService.bookAppointment(1, 20, 2);
        assertFalse(result);
    }

    @Test
    void bookAppointment_tooManyParticipants_shouldFail() {
        boolean result = bookingService.bookAppointment(1, 30, 10);
        assertFalse(result);
    }

    @Test
    void cancelBooking_shouldWork() {
        bookingService.bookAppointment(1, 30, 2);
        boolean result = bookingService.cancelBooking(1);
        assertTrue(result);
    }
}
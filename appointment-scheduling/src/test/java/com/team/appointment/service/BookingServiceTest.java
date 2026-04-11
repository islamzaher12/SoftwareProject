package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.team.appointment.model.AppointmentType;

/**
 * Unit tests for {@link BookingService}.
 * Covers booking, cancellation, modification, boundary conditions,
 * and appointment-type rules.
 *
 * @author Team
 * @version 1.0
 */
public class BookingServiceTest {

    private SlotService slotService;
    private BookingService bookingService;

    /** Reinitialise services before every test for full isolation. */
    @BeforeEach
    void setUp() {
        slotService    = new SlotService();
        bookingService = new BookingService(slotService);
    }

    // ── Book – happy path ──────────────────────────────────────────────────

    @Test
    void bookAppointment_valid_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 2));
    }

    @Test
    void bookAppointment_withType_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 1, AppointmentType.URGENT));
    }

    @Test
    void bookAppointment_group_maxParticipants_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 3, AppointmentType.GROUP));
    }

    @Test
    void bookedSlot_statusShouldBeConfirmed() {
        bookingService.bookAppointment(1, 30, 2);
        // First available slot maps to allSlots index 0 (since none are pre-booked)
        assertEquals("CONFIRMED", slotService.getAllSlots().get(0).getStatus());
    }

    @Test
    void bookedSlot_typeShouldBeStored() {
        bookingService.bookAppointment(1, 30, 1, AppointmentType.VIRTUAL);
        assertEquals(AppointmentType.VIRTUAL, slotService.getAllSlots().get(0).getType());
    }

    // ── Book – validation failures ──────────────────────────────────────────

    @Test
    void bookAppointment_wrongDuration_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 20, 2));
    }

    @Test
    void bookAppointment_durationZero_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 0, 2));
    }

    @Test
    void bookAppointment_tooManyParticipants_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 10));
    }

    @Test
    void bookAppointment_zeroParticipants_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 0));
    }

    @Test
    void bookAppointment_negativeIndex_shouldFail() {
        assertFalse(bookingService.bookAppointment(-1, 30, 2));
    }

    @Test
    void bookAppointment_indexTooLarge_shouldFail() {
        assertFalse(bookingService.bookAppointment(999, 30, 2));
    }

    @Test
    void bookAppointment_alreadyBooked_shouldFail() {
        // Book all available slots so none remain
        int total = slotService.getAvailableSlots().size();
        for (int i = 0; i < total; i++) {
            bookingService.bookAppointment(1, 30, 1);
        }
        // No slots left – any booking must fail
        assertFalse(bookingService.bookAppointment(1, 30, 1));
    }

    // ── Type-specific capacity rules ─────────────────────────────────────────

    @Test
    void bookUrgent_twoParticipants_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 2, AppointmentType.URGENT));
    }

    @Test
    void bookIndividual_twoParticipants_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 2, AppointmentType.INDIVIDUAL));
    }

    @Test
    void bookAssessment_oneParticipant_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 1, AppointmentType.ASSESSMENT));
    }

    @Test
    void bookFollowUp_twoParticipants_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 2, AppointmentType.FOLLOW_UP));
    }

    @Test
    void bookFollowUp_threeParticipants_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 3, AppointmentType.FOLLOW_UP));
    }

    @Test
    void bookInPerson_twoParticipants_shouldSucceed() {
        assertTrue(bookingService.bookAppointment(1, 30, 2, AppointmentType.IN_PERSON));
    }

    // ── Cancel ───────────────────────────────────────────────────────────────

    @Test
    void cancelBooking_shouldWork() {
        bookingService.bookAppointment(1, 30, 2);
        assertTrue(bookingService.cancelBooking(1));
    }

    @Test
    void cancelBooking_slotShouldBeAvailableAfterCancel() {
        bookingService.bookAppointment(1, 30, 2);
        int availableBefore = slotService.getAvailableSlots().size();
        bookingService.cancelBooking(1);
        int availableAfter = slotService.getAvailableSlots().size();
        assertEquals(availableBefore + 1, availableAfter);
    }

    @Test
    void cancelBooking_notBooked_shouldFail() {
        assertFalse(bookingService.cancelBooking(1));
    }

    @Test
    void cancelBooking_invalidIndex_shouldFail() {
        assertFalse(bookingService.cancelBooking(0));
        assertFalse(bookingService.cancelBooking(999));
    }

    @Test
    void cancelBooking_statusResetsToPending() {
        bookingService.bookAppointment(1, 30, 1);
        bookingService.cancelBooking(1);
        assertEquals("PENDING", slotService.getAllSlots().get(0).getStatus());
    }

    // ── Modify ───────────────────────────────────────────────────────────────

    @Test
    void modifyBooking_shouldSucceed() {
        bookingService.bookAppointment(1, 30, 2);
        // slot 1 (all-slots index 1) is booked; slot 2 (all-slots index 2) is free
        assertTrue(bookingService.modifyBooking(1, 2, 30, 2));
    }

    @Test
    void modifyBooking_withType_shouldSucceed() {
        bookingService.bookAppointment(1, 30, 1, AppointmentType.INDIVIDUAL);
        assertTrue(bookingService.modifyBooking(1, 2, 30, 1, AppointmentType.INDIVIDUAL));
    }

    @Test
    void modifyBooking_wrongDuration_shouldFail() {
        bookingService.bookAppointment(1, 30, 2);
        assertFalse(bookingService.modifyBooking(1, 2, 45, 2));
    }

    @Test
    void modifyBooking_oldSlotNotBooked_shouldFail() {
        assertFalse(bookingService.modifyBooking(1, 2, 30, 2));
    }

    @Test
    void modifyBooking_newSlotAlreadyBooked_shouldFail() {
        bookingService.bookAppointment(1, 30, 1);
        bookingService.bookAppointment(1, 30, 1); // books slot 2 now
        // Both slot 1 and slot 2 are booked; moving 1→2 should fail
        assertFalse(bookingService.modifyBooking(1, 2, 30, 1));
    }

    @Test
    void modifyBooking_invalidOldIndex_shouldFail() {
        assertFalse(bookingService.modifyBooking(0, 2, 30, 1));
    }

    @Test
    void modifyBooking_invalidNewIndex_shouldFail() {
        bookingService.bookAppointment(1, 30, 1);
        assertFalse(bookingService.modifyBooking(1, 999, 30, 1));
    }
    // ── Additional: capacity branches in overloads ─────────────────────────────

    @Test
    void bookAppointment_tooManyParticipants_withType_shouldFail() {
        assertFalse(bookingService.bookAppointment(1, 30, 4, AppointmentType.GROUP));
    }

    @Test
    void modifyBooking_tooManyParticipants_noType_shouldFail() {
        bookingService.bookAppointment(1, 30, 1);
        assertFalse(bookingService.modifyBooking(1, 2, 30, 10));
    }

    @Test
    void modifyBooking_tooManyParticipants_withType_shouldFail() {
        bookingService.bookAppointment(1, 30, 1, AppointmentType.INDIVIDUAL);
        assertFalse(bookingService.modifyBooking(1, 2, 30, 4, AppointmentType.GROUP));
    }

}

package com.team.appointment.service;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.team.appointment.model.AppointmentType;

/**
 * Unit tests for {@link NotificationService} using Mockito to mock
 * the {@link Notifier} dependency so no real email is sent.
 *
 * @author Team
 * @version 1.0
 */
public class NotificationServiceTest {

    private SlotService slotService;
    private BookingService bookingService;
    private Notifier mockNotifier;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        slotService          = new SlotService();
        bookingService       = new BookingService(slotService);
        mockNotifier         = Mockito.mock(Notifier.class);
        notificationService  = new NotificationService(slotService, mockNotifier);
    }

    @Test
    void sendReminders_oneBooked_shouldCallNotifierOnce() {
        bookingService.bookAppointment(1, 30, 2);
        notificationService.sendReminders();
        verify(mockNotifier, times(1)).send(Mockito.contains("Reminder"));
    }

    @Test
    void sendReminders_noBooked_shouldNotCallNotifier() {
        notificationService.sendReminders();
        verify(mockNotifier, never()).send(anyString());
    }

    @Test
    void sendReminders_twoBooked_shouldCallNotifierTwice() {
        bookingService.bookAppointment(1, 30, 1);
        bookingService.bookAppointment(1, 30, 1); // books second slot
        notificationService.sendReminders();
        verify(mockNotifier, times(2)).send(Mockito.contains("Reminder"));
    }

    @Test
    void sendReminders_messageContainsDate() {
        bookingService.bookAppointment(1, 30, 2, AppointmentType.VIRTUAL);
        notificationService.sendReminders();
        verify(mockNotifier, times(1)).send(Mockito.contains("2026"));
    }

    @Test
    void sendReminders_messageContainsStatus() {
        bookingService.bookAppointment(1, 30, 1);
        notificationService.sendReminders();
        verify(mockNotifier, times(1)).send(Mockito.contains("CONFIRMED"));
    }

    @Test
    void sendReminders_afterCancel_shouldNotSendForCancelledSlot() {
        bookingService.bookAppointment(1, 30, 2);
        bookingService.cancelBooking(1);
        notificationService.sendReminders();
        verify(mockNotifier, never()).send(anyString());
    }

    @Test
    void sendReminders_messageContainsType() {
        bookingService.bookAppointment(1, 30, 1, AppointmentType.URGENT);
        notificationService.sendReminders();
        verify(mockNotifier, times(1)).send(Mockito.contains("URGENT"));
    }
}

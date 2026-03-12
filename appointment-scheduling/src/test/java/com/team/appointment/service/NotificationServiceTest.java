package com.team.appointment.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NotificationServiceTest {

    @Test
    void shouldSendReminderForBookedAppointments() {
        SlotService slotService = new SlotService();
        BookingService bookingService = new BookingService(slotService);

        bookingService.bookAppointment(1, 30, 2);

        Notifier mockNotifier = Mockito.mock(Notifier.class);
        NotificationService notificationService =
                new NotificationService(slotService, mockNotifier);

        notificationService.sendReminders();

        verify(mockNotifier, times(1)).send(Mockito.contains("Reminder"));
    }
}
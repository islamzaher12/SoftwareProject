package com.team.appointment.service;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link CompositeNotifier}.
 *
 * @author Team
 * @version 1.0
 */
public class CompositeNotifierTest {

    @Test
    void send_twoNotifiers_bothReceiveMessage() {
        Notifier n1 = Mockito.mock(Notifier.class);
        Notifier n2 = Mockito.mock(Notifier.class);
        new CompositeNotifier(n1, n2).send("Hello");
        verify(n1, times(1)).send("Hello");
        verify(n2, times(1)).send("Hello");
    }

    @Test
    void send_threeNotifiers_allReceiveMessage() {
        Notifier n1 = Mockito.mock(Notifier.class);
        Notifier n2 = Mockito.mock(Notifier.class);
        Notifier n3 = Mockito.mock(Notifier.class);
        new CompositeNotifier(n1, n2, n3).send("Test");
        verify(n1).send("Test");
        verify(n2).send("Test");
        verify(n3).send("Test");
    }

    @Test
    void send_singleNotifier_calledOnce() {
        Notifier n1 = Mockito.mock(Notifier.class);
        new CompositeNotifier(n1).send("Single");
        verify(n1, times(1)).send("Single");
    }

    @Test
    void send_multipleMessages_eachForwardedToAll() {
        Notifier n1 = Mockito.mock(Notifier.class);
        Notifier n2 = Mockito.mock(Notifier.class);
        CompositeNotifier composite = new CompositeNotifier(n1, n2);
        composite.send("Msg1");
        composite.send("Msg2");
        verify(n1, times(1)).send("Msg1");
        verify(n1, times(1)).send("Msg2");
        verify(n2, times(1)).send("Msg1");
        verify(n2, times(1)).send("Msg2");
    }

    @Test
    void send_correctMessageContent_forwarded() {
        Notifier n1 = Mockito.mock(Notifier.class);
        String msg = "Reminder: appointment on 2026-05-01";
        new CompositeNotifier(n1).send(msg);
        verify(n1).send(msg);
    }
}

package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConsoleNotifierTest {

    @Test
    void send_doesNotThrow() {
        assertDoesNotThrow(() -> new ConsoleNotifier().send("Hello"));
    }

    @Test
    void send_emptyMessage_doesNotThrow() {
        assertDoesNotThrow(() -> new ConsoleNotifier().send(""));
    }

    @Test
    void send_multipleMessages_doesNotThrow() {
        ConsoleNotifier n = new ConsoleNotifier();
        assertDoesNotThrow(() -> {
            n.send("First");
            n.send("Second");
        });
    }
}

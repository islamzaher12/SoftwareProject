package com.team.appointment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AppTest {

    @Test
    void testMainRunsWithoutThrowingException() {
        assertDoesNotThrow(() -> App.main(new String[]{}));
    }

    @Test
    void testMainWithArgumentsRunsWithoutThrowingException() {
        assertDoesNotThrow(() -> App.main(new String[]{"arg1", "arg2"}));
    }
}
package com.team.appointment.ui;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.util.Scanner;

/**
 * Unit tests for {@link ConsoleMenu}.
 * Injects a fake Scanner backed by a StringReader to simulate user input
 * without blocking on System.in.
 *
 * @author Team
 * @version 1.0
 */
public class ConsoleMenuTest {

    /** Helper: wraps the given input string into a ConsoleMenu. */
    private ConsoleMenu menu(String input) {
        return new ConsoleMenu(new Scanner(new StringReader(input)));
    }

    // ── Exit ──────────────────────────────────────────────────────────────────

    @Test
    void start_exit_immediately() {
        assertDoesNotThrow(() -> menu("0\n").start());
    }

    // ── Invalid choice ────────────────────────────────────────────────────────

    @Test
    void start_invalid_choice_then_exit() {
        assertDoesNotThrow(() -> menu("99\n0\n").start());
    }

    // ── readInt with non-numeric input ────────────────────────────────────────

    @Test
    void readInt_non_numeric_retries_then_exits() {
        // "abc" is rejected → loops → reads "0" → exits
        assertDoesNotThrow(() -> menu("abc\n0\n").start());
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @Test
    void start_login_valid_then_exit() {
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n0\n").start());
    }

    @Test
    void start_login_wrong_credentials() {
        assertDoesNotThrow(() -> menu("1\nwrong\nwrong\n0\n").start());
    }

    @Test
    void start_login_already_logged_in() {
        // choice 1 twice: second time shows "Already logged in"
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n1\n0\n").start());
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    @Test
    void start_logout_not_logged_in() {
        assertDoesNotThrow(() -> menu("6\n0\n").start());
    }

    @Test
    void start_login_then_logout() {
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n6\n0\n").start());
    }

    // ── View slots ────────────────────────────────────────────────────────────

    @Test
    void start_view_available_slots() {
        assertDoesNotThrow(() -> menu("2\n0\n").start());
    }

    // ── Book ──────────────────────────────────────────────────────────────────

    @Test
    void start_book_not_logged_in() {
        assertDoesNotThrow(() -> menu("3\n0\n").start());
    }

    @Test
    void start_book_invalid_slot_number() {
        // slot 999 doesn't exist → booking fails
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n3\n999\n30\n1\n0\n").start());
    }

    @Test
    void start_book_valid_slot() {
        // slot 1, duration 30, 1 participant → succeeds (email skipped – no EMAIL_PASS)
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n3\n1\n30\n1\n0\n").start());
    }

    // ── Cancel ────────────────────────────────────────────────────────────────

    @Test
    void start_cancel_not_logged_in() {
        assertDoesNotThrow(() -> menu("4\n0\n").start());
    }

    @Test
    void start_cancel_unbooked_slot() {
        // slot 1 is not booked → cancel fails
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n4\n1\n0\n").start());
    }

    @Test
    void start_book_then_cancel() {
        // book slot 1 → cancel slot 1 (uses allSlots index 1)
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n3\n1\n30\n1\n4\n1\n0\n").start());
    }

    // ── Modify ────────────────────────────────────────────────────────────────

    @Test
    void start_modify_not_logged_in() {
        assertDoesNotThrow(() -> menu("5\n0\n").start());
    }

    @Test
    void start_modify_invalid_old_slot() {
        // old=999 (out of range) → fails
        assertDoesNotThrow(() -> menu("1\nadmin\n1234\n5\n999\n1\n30\n1\n0\n").start());
    }

    @Test
    void start_book_then_modify() {
        // book slot 1, then move it to slot 2
        assertDoesNotThrow(() ->
            menu("1\nadmin\n1234\n3\n1\n30\n1\n5\n1\n2\n30\n1\n0\n").start());
    }

    // ── "No available slots" branches ─────────────────────────────────────────

    @Test
    void start_all_slots_booked_covers_empty_branches() {
        // Book all 12 slots, then trigger the "No available slots" paths in
        // viewAvailableSlots, bookAppointment, and modifyAppointment.
        String bookAll =
            "3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n" +
            "3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n" +
            "3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n3\n1\n30\n1\n";
        String input = "1\nadmin\n1234\n" + bookAll
                     + "2\n"       // view   → "No available slots"
                     + "3\n"       // book   → "No available slots" + return
                     + "5\n1\n"    // modify → reads old=1, then "No available slots" + return
                     + "0\n";
        assertDoesNotThrow(() -> menu(input).start());
    }
}

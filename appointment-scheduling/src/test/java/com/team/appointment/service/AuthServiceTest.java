package com.team.appointment.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.team.appointment.model.User;

/**
 * Unit tests for {@link AuthService}.
 * Validates correct credential handling and admin-flag assignment.
 *
 * @author Team
 * @version 1.0
 */
public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void login_validAdminCredentials_shouldReturnUser() {
        User u = authService.login("admin", "1234");
        assertNotNull(u);
    }

    @Test
    void login_validUserCredentials_shouldReturnUser() {
        User u = authService.login("user", "1111");
        assertNotNull(u);
    }

    @Test
    void login_wrongPassword_shouldReturnNull() {
        assertNull(authService.login("admin", "wrong"));
    }

    @Test
    void login_unknownUsername_shouldReturnNull() {
        assertNull(authService.login("nobody", "1234"));
    }

    @Test
    void login_emptyCredentials_shouldReturnNull() {
        assertNull(authService.login("", ""));
    }

    @Test
    void login_adminUser_isAdminFlagTrue() {
        User u = authService.login("admin", "1234");
        assertNotNull(u);
        assertTrue(u.isAdmin());
    }

    @Test
    void login_regularUser_isAdminFlagFalse() {
        User u = authService.login("user", "1111");
        assertNotNull(u);
        assertFalse(u.isAdmin());
    }

    @Test
    void login_caseSensitiveUsername() {
        assertNull(authService.login("Admin", "1234"));
    }

    @Test
    void login_caseSensitivePassword() {
        assertNull(authService.login("admin", "1234 "));
    }

    @Test
    void login_successReturnsCorrectUsername() {
        User u = authService.login("admin", "1234");
        assertNotNull(u);
        assertEquals("admin", u.getUsername());
    }
}

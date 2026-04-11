package com.team.appointment.service;

import com.team.appointment.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testLoginWithValidAdminCredentials() {
        User user = authService.login("admin", "1234");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("1234", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void testLoginWithValidRegularUserCredentials() {
        User user = authService.login("user", "1111");

        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertEquals("1111", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testLoginWithWrongPassword() {
        User user = authService.login("admin", "wrong");

        assertNull(user);
    }

    @Test
    void testLoginWithUnknownUsername() {
        User user = authService.login("unknown", "1234");

        assertNull(user);
    }

    @Test
    void testLoginWithNullUsername() {
        User user = authService.login(null, "1234");

        assertNull(user);
    }

    @Test
    void testLoginWithNullPassword() {
        User user = authService.login("admin", null);

        assertNull(user);
    }

    @Test
    void testAddUserSuccessfullyAsRegularUser() {
        boolean result = authService.addUser("ali", "9999", false);

        assertTrue(result);
        assertTrue(authService.userExists("ali"));

        User user = authService.login("ali", "9999");
        assertNotNull(user);
        assertEquals("ali", user.getUsername());
        assertFalse(user.isAdmin());
    }

    @Test
    void testAddUserSuccessfullyAsAdmin() {
        boolean result = authService.addUser("manager", "abcd", true);

        assertTrue(result);
        assertTrue(authService.userExists("manager"));

        User user = authService.login("manager", "abcd");
        assertNotNull(user);
        assertEquals("manager", user.getUsername());
        assertTrue(user.isAdmin());
    }

    @Test
    void testAddUserFailsWhenUsernameAlreadyExists() {
        boolean result = authService.addUser("admin", "newpass", false);

        assertFalse(result);
    }

    @Test
    void testAddUserFailsWhenUsernameIsNull() {
        boolean result = authService.addUser(null, "1234", false);

        assertFalse(result);
    }

    @Test
    void testAddUserFailsWhenUsernameIsBlank() {
        boolean result = authService.addUser("   ", "1234", false);

        assertFalse(result);
    }

    @Test
    void testAddUserFailsWhenPasswordIsNull() {
        boolean result = authService.addUser("sara", null, false);

        assertFalse(result);
    }

    @Test
    void testAddUserFailsWhenPasswordIsBlank() {
        boolean result = authService.addUser("sara", "   ", false);

        assertFalse(result);
    }

    @Test
    void testUserExistsForDefaultUsers() {
        assertTrue(authService.userExists("admin"));
        assertTrue(authService.userExists("user"));
    }

    @Test
    void testUserExistsForAddedUser() {
        authService.addUser("mohammad", "5555", false);

        assertTrue(authService.userExists("mohammad"));
    }

    @Test
    void testUserExistsForNonExistingUser() {
        assertFalse(authService.userExists("notfound"));
    }

    @Test
    void testDuplicateUserIsNotAddedTwice() {
        boolean firstAdd = authService.addUser("ahmad", "1111", false);
        boolean secondAdd = authService.addUser("ahmad", "2222", true);

        assertTrue(firstAdd);
        assertFalse(secondAdd);

        User user = authService.login("ahmad", "1111");
        assertNotNull(user);
        assertFalse(user.isAdmin());
    }
}
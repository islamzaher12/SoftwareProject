package com.team.appointment.service;

import java.util.HashMap;
import java.util.Map;
import com.team.appointment.model.User;

/**
 * Handles user authentication for the appointment scheduling system.
 * Maintains a registry of known users and validates credentials on login.
 * The "admin" account has administrator privileges.
 *
 * @author Team
 * @version 1.0
 */
public class AuthService {

    /** Internal map of username to {@link User} objects. */
    private final Map<String, User> users = new HashMap<>();

    /**
     * Constructs the AuthService and seeds two default accounts:
     * <ul>
     *   <li>admin / 1234  (administrator)</li>
     *   <li>user  / 1111  (regular user)</li>
     * </ul>
     */
    public AuthService() {
        users.put("admin", new User("admin", "1234", true));
        users.put("user",  new User("user",  "1111", false));
    }

    /**
     * Attempts to authenticate a user with the given credentials.
     *
     * @param username the login name
     * @param password the plaintext password
     * @return the matching {@link User} on success, or {@code null} on failure
     */
    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    /**
     * Registers a new user. Fails if the username is already taken.
     *
     * @param username the desired login name
     * @param password the plaintext password
     * @param isAdmin  whether the new account has admin privileges
     * @return {@code true} if the user was created, {@code false} if username exists
     */
    public boolean addUser(String username, String password, boolean isAdmin) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password, isAdmin));
        return true;
    }

    /**
     * Returns whether a username is already registered.
     *
     * @param username the username to check
     * @return {@code true} if the username exists
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}

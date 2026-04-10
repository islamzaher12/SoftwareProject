package com.team.appointment.model;

/**
 * Represents an authenticated user of the appointment scheduling system.
 * Users can be either regular users or administrators; administrators have
 * elevated privileges such as managing all reservations and adding slots.
 *
 * @author Team
 * @version 1.0
 */
public class User {

    /** The unique username used for authentication. */
    private final String username;

    /** The plaintext password used for authentication. */
    private final String password;

    /** Whether this user has administrator privileges. */
    private final boolean admin;

    /**
     * Constructs a User with the given credentials and admin flag.
     *
     * @param username the unique login name
     * @param password the plaintext password
     * @param admin    {@code true} if this user is an administrator
     */
    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    /**
     * Returns the username.
     *
     * @return username string
     */
    public String getUsername() { return username; }

    /**
     * Returns the password.
     *
     * @return password string
     */
    public String getPassword() { return password; }

    /**
     * Returns whether this user has admin privileges.
     *
     * @return {@code true} if admin
     */
    public boolean isAdmin() { return admin; }
}

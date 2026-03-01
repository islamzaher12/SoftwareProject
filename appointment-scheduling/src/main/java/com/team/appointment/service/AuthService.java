package com.team.appointment.service;

import java.util.HashMap;
import java.util.Map;
import com.team.appointment.model.User;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();

    public AuthService() {
        users.put("admin", new User("admin", "1234"));
        users.put("user", new User("user", "1111"));
    }

    public User login(String username, String password) {
        User u = users.get(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
}
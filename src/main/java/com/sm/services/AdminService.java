package com.sm.services;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final String ADMIN_EMAIL = "admin@gmail.com";
    private final String ADMIN_PASSWORD = "admin123";

    public String login(String email, String password) {
        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            return "Admin Login Successful";
        } else {
            return "Invalid Admin Credentials";
        }
    }
}
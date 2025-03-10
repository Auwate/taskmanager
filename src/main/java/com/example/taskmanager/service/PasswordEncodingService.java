package com.example.taskmanager.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface PasswordEncodingService {
    String encode(String password);
    PasswordEncoder getEncoder();
}

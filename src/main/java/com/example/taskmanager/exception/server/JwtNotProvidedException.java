package com.example.taskmanager.exception.server;

public class JwtNotProvidedException extends RuntimeException {
    public JwtNotProvidedException(String message) {
        super(message);
    }
}
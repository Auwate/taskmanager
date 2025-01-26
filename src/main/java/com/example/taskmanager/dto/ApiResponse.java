package com.example.taskmanager.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApiResponse<T> {

    private final int status;
    private final String timestamp;
    private final String message;
    private final T data;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    // Simple factories

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    // Getters

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

}

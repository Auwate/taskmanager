package com.example.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException exception) {
        return "Invalid request: " + exception.getMessage();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleWrongHttpMethod(HttpRequestMethodNotSupportedException exception) {

        Map<String, String> response = new HashMap<>();

        response.put("response", "Invalid HTTP method provided: " + exception.getMessage());
        response.put("error code", "400");

        return response;

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception exception) {

        Map<String, String> response = new HashMap<>();

        response.put("response", "An error has occurred:" + exception.getMessage());
        response.put("error code", "500");

        return response;

    }

}

package com.example.taskmanager.exception.handler;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.exception.server.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.taskmanager.exception.server.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleWrongHttpMethod(
            HttpRequestMethodNotSupportedException exception
    ) {

        String message = "HTTP method " + exception.getMethod() + " not allowed.";

        ApiResponse<String> response = ApiResponse.of(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                message,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResourceNotFoundException exception) {

        String message = "Resource not found.";

        ApiResponse<String> response = ApiResponse.of(
                HttpStatus.NOT_FOUND.value(),
                message,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception exception) {

        String message = "An internal server error has occurred.";

        ApiResponse<String> response = ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR). body(response);

    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiResponse<String>> handleDatabaseException(DatabaseException exception) {

        String message = "The database could not fulfill your request.";

        ApiResponse<String> response = ApiResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message,
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }

}

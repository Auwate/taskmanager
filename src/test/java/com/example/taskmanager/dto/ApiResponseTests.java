package com.example.taskmanager.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApiResponseTests {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ParameterizedTest
    @CsvSource({"200, Success, Inputted", "201, Created, Item created", "300, Moved, Item is moved."})
    void testStringGeneric_Success(int status, String message, String data) {

        ApiResponse<String> response = ApiResponse.of(status, message, data);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());

    }

    @ParameterizedTest
    @CsvSource({"200, Success", "201, Created", "300, Moved"})
    void testStringGenericEdge_Success(int status, String message) {

        ApiResponse<String> response = ApiResponse.of(status, message, null);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());

    }

    @Test
    void testTimeStamp_Success() {

        ApiResponse<String> response = ApiResponse.of(
                200,
                "Success",
                "Data",
                LocalDateTime.of(2025, 2, 10, 10, 20, 30)
        );

        assertEquals(200, response.getStatus());
        assertEquals("Success", response.getMessage());
        assertEquals("Data", response.getData());
        assertEquals("2025-02-10 10:20:30", response.getTimestamp());

    }

}

package com.example.taskmanager.dto;

import com.example.taskmanager.model.Task;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApiResponseTest {

    static List<ApiResponse<Task>> getTaskResponses() {
        return List.of(
                new ApiResponse<>(200, "Success", new Task("Task 1", "Description 1")),
                new ApiResponse<>(400, "Bad request", new Task("Task 2", "Description 2")),
                new ApiResponse<>(500, "Internal error", new Task("Task 3", "Description 3"))
        );
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ParameterizedTest
    @CsvSource({"200, Success, Inputted", "201, Created, Item created", "300, Moved, Item is moved."})
    void testStringGeneric_Success(int status, String message, String data) {

        ApiResponse<String> response = new ApiResponse<>(status, message, data);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());

    }

    @ParameterizedTest
    @CsvSource({"200, Success", "201, Created", "300, Moved"})
    void testStringGenericEdge_Success(int status, String message) {

        ApiResponse<String> response = new ApiResponse<>(status, message, null);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());

    }

    @ParameterizedTest
    @MethodSource("getTaskResponses")
    void testTaskGeneric_Success(ApiResponse<Task> response) {

        

    }

}

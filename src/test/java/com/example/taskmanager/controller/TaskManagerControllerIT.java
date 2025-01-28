package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Create the entire Spring application context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// Useful for when we have databases -> @ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskManagerControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TaskRepository taskRepository;

    private static final String QUERY_URL = "/tasks";
    private static final String RESPONSE_PATH = "/api/tasks";

    <T> HttpEntity<T> HttpEntityFactory(T data) {
        return new HttpEntity<>(data);
    }

    @Test
    @Order(1)
    void testCreateTask() {

        Map<String, Object> payload = Map.of(
                "name", "Integration test 1",
                "description", "Testing integration test 1"
        );

        ResponseEntity<ApiResponse<URI>> response = testRestTemplate.exchange(
                QUERY_URL,
                HttpMethod.POST,
                HttpEntityFactory(payload),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals( RESPONSE_PATH + "/1", response.getBody().getData().getPath());

        assertEquals(1, taskRepository.findAll().size());
        assertEquals(
                "Integration test 1",
                taskRepository.findById(1L).orElseThrow().getName()
        );
        assertEquals(
                "Testing integration test 1",
                taskRepository.findById(1L).orElseThrow().getDescription()
        );

    }

}

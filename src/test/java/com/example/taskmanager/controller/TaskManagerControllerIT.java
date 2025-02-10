package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.model.Tag;
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
import java.util.List;

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

        Tag testTag = new Tag("Test tag", 0, 0, 0);

        Task payload = new Task(
                "Integration test 1",
                "Testing integration test 1",
                0,
                testTag
        );

        ResponseEntity<ApiResponse<URI>> response = testRestTemplate.exchange(
                QUERY_URL,
                HttpMethod.POST,
                HttpEntityFactory(payload),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
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
        assertEquals(
                0,
                taskRepository.findById(1L).orElseThrow().getPriority()
        );
        assertEquals(
                testTag,
                taskRepository.findById(1L).orElseThrow().getTag()
        );

    }

    @Test
    @Order(2)
    void getTasks() {

        ResponseEntity<ApiResponse<List<Task>>> response = testRestTemplate.exchange(
            QUERY_URL,
            HttpMethod.GET,
            HttpEntityFactory(null),
            new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals("Integration test 1", response.getBody().getData().getFirst().getName());
        assertEquals("Testing integration test 1",
                 response.getBody().getData().getFirst().getDescription());
        assertEquals(
                0,
                response.getBody().getData().getFirst().getPriority()
        );
        assertEquals(
                new Tag("Test tag", 0, 0, 0),
                response.getBody().getData().getFirst().getTag()
        );
        assertEquals(1, response.getBody().getData().size());

    }

    @Test
    @Order(3)
    void getTaskById() {

        ResponseEntity<ApiResponse<Task>> response = testRestTemplate.exchange(
                QUERY_URL + "/1",
                HttpMethod.GET,
                HttpEntityFactory(null),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals("Integration test 1", response.getBody().getData().getName());
        assertEquals("Testing integration test 1", response.getBody().getData().getDescription());
        assertEquals(
                0,
                response.getBody().getData().getPriority()
        );
        assertEquals(
                new Tag("Test tag", 0, 0, 0),
                response.getBody().getData().getTag()
        );

    }

    @Test
    @Order(4)
    void updateTaskById() {

        Tag testTag = new Tag("Test tag", 1, 2, 3);

        Task payload = new Task(
                "Integration test 2",
                "Testing integration test 2",
                1,
                testTag
        );

        ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(
                QUERY_URL + "/1",
                HttpMethod.PUT,
                HttpEntityFactory(payload),
                new ParameterizedTypeReference<ApiResponse<Void>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());

        assertEquals("Integration test 2", taskRepository.findById(1L).orElseThrow().getName());
        assertEquals("Testing integration test 2", taskRepository.findById(1L).orElseThrow().getDescription());
        assertEquals(1, taskRepository.findAll().size());
        assertEquals(
                1,
                taskRepository.findById(1L).orElseThrow().getPriority()
        );
        assertEquals(
                testTag,
                taskRepository.findById(1L).orElseThrow().getTag()
        );

    }

    @Test
    @Order(5)
    void deleteTaskById() {

        ResponseEntity<ApiResponse<Task>> response = testRestTemplate.exchange(
                QUERY_URL + "/1",
                HttpMethod.DELETE,
                HttpEntityFactory(null),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals("Integration test 2", response.getBody().getData().getName());
        assertEquals("Testing integration test 2", response.getBody().getData().getDescription());
        assertEquals(
                1,
                response.getBody().getData().getPriority()
        );
        assertEquals(
                new Tag("Test tag", 1, 2, 3),
                response.getBody().getData().getTag()
        );

        assertEquals(0, taskRepository.findAll().size());

    }

}

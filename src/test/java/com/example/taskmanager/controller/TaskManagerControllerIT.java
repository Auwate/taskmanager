package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.model.Color;
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
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Create the entire Spring application context
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
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

        Color color_payload = new Color();
        color_payload.setRed(0);
        color_payload.setGreen(0);
        color_payload.setBlue(0);

        Tag tag_payload = new Tag();
        tag_payload.setName("Test tag");

        Task payload = new Task();
        payload.setId(null);
        payload.setName("Integration test 1");
        payload.setDescription("Testing integration test 1");
        payload.setPriority(0);

        payload.setTag(tag_payload);
        tag_payload.setTask(payload);
        tag_payload.setColor(color_payload);
        color_payload.setTag(tag_payload);

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
                taskRepository.findById(1L).orElseThrow().getName(),
                payload.getName()
        );
        assertEquals(
                taskRepository.findById(1L).orElseThrow().getDescription(),
                payload.getDescription()
        );
        assertEquals(
                taskRepository.findById(1L).orElseThrow().getPriority(),
                payload.getPriority()
        );
        assertEquals(
                taskRepository.findById(1L).orElseThrow().getTag(),
                payload.getTag()
        );
        assertEquals(
                taskRepository.findById(1L).orElseThrow().getId(),
                1L
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
                new Tag(1L, "Test tag", null, new Color(1L, null, 0, 0, 0)),
                response.getBody().getData().getFirst().getTag()
        );
        assertEquals(1L, response.getBody().getData().getFirst().getId());
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
        assertEquals(1L, response.getBody().getData().getId());
        assertEquals(
                new Tag(1L, "Test tag", null, Color.of(1L, null, 0, 0, 0)),
                response.getBody().getData().getTag()
        );

    }

    @Test
    @Order(4)
    void updateTaskById() {

        Color testColor = new Color(1L, null, 1, 2, 3);
        Tag testTag = new Tag(1L, "Test tag 2", null, testColor);
        Task payload = new Task(
                1L,
                "Integration test 2",
                "Testing integration test 2",
                1,
                testTag
        );
        testColor.setTag(testTag);
        testTag.setTask(payload);

        ResponseEntity<ApiResponse<Void>> response = testRestTemplate.exchange(
                QUERY_URL + "/1",
                HttpMethod.PUT,
                HttpEntityFactory(payload),
                new ParameterizedTypeReference<>() {}
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
                testTag.getName(),
                taskRepository.findById(1L).orElseThrow().getTag().getName()
        );
        assertEquals(
                testColor.getRed(),
                taskRepository.findById(1L).orElseThrow().getTag().getColor().getRed()
        );
        assertEquals(
                testColor.getGreen(),
                taskRepository.findById(1L).orElseThrow().getTag().getColor().getGreen()
        );
        assertEquals(
                testColor.getBlue(),
                taskRepository.findById(1L).orElseThrow().getTag().getColor().getBlue()
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

        assertEquals(0, taskRepository.findAll().size());

    }

}

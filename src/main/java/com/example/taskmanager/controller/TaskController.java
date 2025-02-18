package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ApiResponse;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getTasks() {

        logger.info("GET HTTP request received at /api/tasks/");

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called getAllTasks()");
        }

        ApiResponse<List<Task>> response = ApiResponse.of(
                HttpStatus.OK.value(),
                "Success",
                taskService.getAllTasks()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping
    public ResponseEntity<ApiResponse<URI>> createTask(@RequestBody Task task) {

        logger.info("POST HTTP request received at /api/tasks/");

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called createTask()");
        }

        ApiResponse<URI> response = ApiResponse.of(
                HttpStatus.CREATED.value(),
                "Success",
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(taskService.createTask(task).getId())
                        .toUri()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {

        logger.info("DELETE HTTP request received at /api/tasks/{}", id);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called deleteTask()");
        }

        taskService.deleteTask(id);

        ApiResponse<Void> response = ApiResponse.of(
                HttpStatus.OK.value(),
                "Success",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTask(@PathVariable Long id) {

        logger.info("GET HTTP request received at /api/tasks/{}", id);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called getTaskByID()");
        }

        ApiResponse<Task> response = ApiResponse.of(
                HttpStatus.OK.value(),
                "Success",
                taskService.getTaskById(id)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateTask(@PathVariable Long id, @RequestBody Task task) {

        logger.info("PUT HTTP Request received at /api/tasks/{}", id);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called updateTaskById(id)");
        }

        try {
            taskService.updateTask(id, task);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }

        ApiResponse<Void> response = ApiResponse.of(
                HttpStatus.OK.value(),
                "Success",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}

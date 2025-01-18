package com.example.taskmanager.controller;

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
    public ResponseEntity<List<Task>> getTasks() {

        logger.info("GET HTTP request received at /api/tasks/");

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called getAllTasks()");
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());

    }

    @PostMapping
    public ResponseEntity<URI> createTask(@RequestBody Task task) {

        logger.info("POST HTTP request received at /api/tasks/");

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called createTask()");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(taskService.createTask(task))
                        .toUri());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {

        logger.info("Delete HTTP request received at /api/tasks/{}", id);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called deleteTask()");
        }

        return taskService.deleteTask(id)
                .map((task) -> ResponseEntity.status(HttpStatus.OK).body("Task " + task.getName() + " deleted."))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found."));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {

        logger.info("Get HTTP request received at /api/tasks/{}", id);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called getTaskByID()");
        }

        return taskService.getTaskById(id)
                .map((task) -> ResponseEntity.status(HttpStatus.OK).body(task))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

}

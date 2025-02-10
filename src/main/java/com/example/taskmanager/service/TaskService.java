package com.example.taskmanager.service;

import com.example.taskmanager.exception.server.DatabaseException;
import com.example.taskmanager.exception.server.ResourceNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called findAll()");
        }

        return taskRepository.findAll();

    }

    public Task getTaskById(Long id) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called findById()");
        }

        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The provided resource could not be found in the database."
                ));

    }

    public Long createTask (Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called save().");
        }

        return taskRepository.save(task);

    }

    public Task deleteTask (Long id) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called delete().");
        }

        return taskRepository.delete(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The provided resource could not be found in the database."
                ));

    }

    public void updateTask(Long id, Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called update().");
        }

        Task updatedTask = taskRepository.update(id, task)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The provided resource could not be found in the database."
                ));

        if (!task.equals(updatedTask)) {
            throw new DatabaseException("There was an issue updating the database.");
        }

    }

}

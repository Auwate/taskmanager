package com.example.taskmanager.service;

import com.example.taskmanager.exception.server.ResourceNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

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

    @Transactional
    public Task createTask (Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called save().");
        }

        return taskRepository.saveAndFlush(task);

    }

    @Transactional
    public void deleteTask (Long id) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Attempting to delete task with ID: {}", id);
        }

        taskRepository.delete(taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "The provided resource could not be found in the database."
        )));

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called delete().");
        }

    }

    @Transactional
    public void updateTask(Long id, Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Attempting to call save.");
        }

        Task taskInDatabase = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "The provided resource could not be found in the database."
        ));

        // Only update fields if they are not null

        if (task.getTag() != null && taskInDatabase.getTag() != null) {

            if (task.getTag().getName() != null) {
                taskInDatabase.getTag().setName(task.getTag().getName());
            } else {
                taskInDatabase.getTag().setName(null);
            }

            if (taskInDatabase.getTag().getColor() != null && task.getTag().getColor() != null) {
                taskInDatabase.getTag().getColor().setRed(task.getTag().getColor().getRed());
                taskInDatabase.getTag().getColor().setGreen(task.getTag().getColor().getGreen());
                taskInDatabase.getTag().getColor().setBlue(task.getTag().getColor().getBlue());
            }

        } else if (task.getTag() != null && taskInDatabase.getTag() == null) {
            taskInDatabase.setTag(task.getTag());
        } else {
            taskInDatabase.setTag(null);
        }

        if (task.getDescription() != null) taskInDatabase.setDescription(task.getDescription());
        if (task.getName() != null) taskInDatabase.setName(task.getName());
        if (task.getPriority() != null) taskInDatabase.setPriority(task.getPriority());

        try {
            taskRepository.saveAndFlush(taskInDatabase);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }
        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called save.");
        }

    }

}

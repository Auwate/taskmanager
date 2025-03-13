package com.example.taskmanager.service;

import com.example.taskmanager.exception.server.ResourceNotFoundException;
import com.example.taskmanager.exception.server.UnauthorizedAccessException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(
            TaskRepository taskRepository,
            UserService userService
    ) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Transactional
    public List<Task> getAllTasks() {

        User user = userService.getUserCredentialsFromContext();

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called findAll()");
        }

        return taskRepository.findAllByUserId(user.getId());

    }

    @Transactional
    public Task getTaskById(Long id) {

        User user = userService.getUserCredentialsFromContext();

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called findById()");
        }

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The provided resource could not be found in the database."
                ));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to access this data.");
        }

        return task;

    }

    @Transactional
    public Task createTask (Task task) {

        User user = userService.getUserCredentialsFromContext();

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called save().");
        }

        task.setUser(user);

        return taskRepository.saveAndFlush(task);

    }

    @Transactional
    public void deleteTask (Long id) {

        User user = userService.getUserCredentialsFromContext();

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Attempting to delete task with ID: {}", id);
        }

        Task task = taskRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                "The provided resource could not be found in the database."
        ));

        if (!task.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to access this data.");
        }

        userService.removeAndSave(user, task);

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully called delete().");
        }

    }

    @Transactional
    public void updateTask(Long id, Task task) {

        User user = userService.getUserCredentialsFromContext();

        Task taskInDatabase = taskRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ResourceNotFoundException(
                "The provided resource could not be found in the database."
        ));

        if (!taskInDatabase.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to access this data.");
        }

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

        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to save in updateTask...");
        }

        try {
            taskRepository.saveAndFlush(taskInDatabase);
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
            throw e;
        }

    }

}

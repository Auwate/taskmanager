package com.example.taskmanager.unit.service;

import com.example.taskmanager.exception.server.ResourceNotFoundException;
import com.example.taskmanager.model.Color;
import com.example.taskmanager.model.Tag;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskManagerServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task testTask = new Task();
    private User user;

    @BeforeEach
    void setUp() {

        user = new User(1L, new HashSet<>());

        this.testTask = new Task(
                1L,
                "Test task",
                "N/A",
                0,
                Tag.of(
                        null, "Test tag", null, Color.of(
                                1L, null, 0, 0, 0
                        )
                ),
                user
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        "1",
                        "JWT-Authenticated",
                        List.of()
                ),
                null,
                List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        user.addTask(testTask);

        // We use null because SecurityContextHolder.getContext().getAuthentication()
        //                                          .getPrincipal() returns null if
        //             you haven't already been authenticated through the filters.
        when(userService.getUserCredentialsFromContext()).thenReturn(user);

    }

    @Test
    void testGetAllTasks() {

        when(taskRepository.findAllByUserId(1L)).thenReturn(List.of(this.testTask, this.testTask, this.testTask));

        List<Task> response = this.taskService.getAllTasks();

        assertEquals(response.size(), 3);
        verify(taskRepository, times(1)).findAllByUserId(1L);

    }

    @Test
    void testGetAllTasks_NotFound() {

        when(taskRepository.findAllByUserId(1L)).thenReturn(List.of());

        List<Task> response = taskService.getAllTasks();

        assertTrue(response.isEmpty());
        verify(taskRepository, times(1)).findAllByUserId(1L);

    }

    @Test
    void testGetTaskById_Success() {

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(this.testTask));

        Task response = taskService.getTaskById(1L);

        assertEquals(this.testTask.getName(), response.getName());

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);

    }

    @Test
    void testGetTaskById_Failure() {

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTaskById(1L)
        );

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);

    }

    @Test
    void testCreateTask_Success() {

        when(taskRepository.saveAndFlush(this.testTask)).thenReturn(this.testTask);

        Task response = taskService.createTask(this.testTask);

        assertEquals(this.testTask, response);

        verify(taskRepository, times(1)).saveAndFlush(this.testTask);

    }

    @Test
    void testDeleteTask_Success() {

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(this.testTask));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(userService, times(1)).removeAndSave(user, testTask);

    }

    @Test
    void testDeleteTask_Failure() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L)
        );

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);

    }

    @Test
    void testUpdateTask_Success() {

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(this.testTask));

        taskService.updateTask(1L, testTask);

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);
        verify(taskRepository, times(1)).saveAndFlush(testTask);

    }

    @Test
    void testUpdateTask_Failure_NotFound() {

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, testTask)
        );

        verify(taskRepository, times(1)).findByIdAndUserId(1L, 1L);

    }

}

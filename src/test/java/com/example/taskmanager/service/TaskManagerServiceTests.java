package com.example.taskmanager.service;

import com.example.taskmanager.exception.server.DatabaseException;
import com.example.taskmanager.exception.server.ResourceNotFoundException;
import com.example.taskmanager.model.Color;
import com.example.taskmanager.model.Tag;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskManagerServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task testTask = new Task();

    @BeforeEach
    void setUp() {
        this.testTask = new Task(
                null,
                "Test task",
                "N/A",
                0,
                Tag.of(
                        null, "Test tag", Color.of(
                                1L, 0, 0, 0
                        )
                )
        );
    }

    @Test
    void testGetAllTasks() {

        when(taskRepository.findAll()).thenReturn(List.of(this.testTask, this.testTask, this.testTask));

        List<Task> response = this.taskService.getAllTasks();

        assertEquals(response.size(), 3);
        verify(taskRepository, times(1)).findAll();

    }

    @Test
    void testGetAllTasks_NotFound() {

        when(taskRepository.findAll()).thenReturn(List.of());

        List<Task> response = taskService.getAllTasks();

        assertTrue(response.isEmpty());
        verify(taskRepository, times(1)).findAll();

    }

    @Test
    void testGetTaskById_Success() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(this.testTask));

        Task response = taskService.getTaskById(1L);

        assertEquals(this.testTask.getName(), response.getName());

        verify(taskRepository, times(1)).findById(1L);

    }

    @Test
    void testGetTaskById_Failure() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTaskById(1L)
        );

        verify(taskRepository, times(1)).findById(1L);

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

        when(taskRepository.findById(1L)).thenReturn(Optional.of(this.testTask));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(this.testTask);
        verify(taskRepository, times(1)).findById(1L);

    }

    @Test
    void testDeleteTask_Failure() {

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L)
        );

        verify(taskRepository, times(1)).findById(1L);

    }

    @Test
    void testUpdateTask_Success() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(this.testTask));

        taskService.updateTask(1L, testTask);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).saveAndFlush(testTask);

    }

    @Test
    void testUpdateTask_Failure_NotFound() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, testTask)
        );

        verify(taskRepository, times(1)).findById(1L);

    }

}

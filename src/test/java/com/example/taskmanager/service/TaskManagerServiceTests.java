package com.example.taskmanager.service;

import com.example.taskmanager.exception.server.ResourceNotFoundException;
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

    private Task testTask;

    @BeforeEach
    void setUp() {
        this.testTask = new Task("Test task", "N/A");
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

        when(taskRepository.save(this.testTask)).thenReturn(1L);

        Long response = taskService.createTask(this.testTask);

        assertEquals(1L, response);

        verify(taskRepository, times(1)).save(this.testTask);

    }

    @Test
    void testDeleteTask_Success() {

        when(taskRepository.delete(1L)).thenReturn(Optional.of(this.testTask));

        Task response = taskService.deleteTask(1L);

        assertEquals(this.testTask.getName(), response.getName());

        verify(taskRepository, times(1)).delete(1L);

    }

    @Test
    void testDeleteTask_Failure() {

        when(taskRepository.delete(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L)
        );

        verify(taskRepository, times(1)).delete(1L);

    }

}

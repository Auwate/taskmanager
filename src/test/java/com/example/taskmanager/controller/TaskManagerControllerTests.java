package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskManagerControllerTests {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task task;

    @BeforeEach
    void setUp() {
        this.task = new Task("Test task", "N/A");
    }


}

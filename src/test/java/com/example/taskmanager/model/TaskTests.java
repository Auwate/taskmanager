package com.example.taskmanager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskTests {

    private Task task;

    @BeforeEach
    void setUp () {

        this.task = new Task(
                "Test 1", "Test Desc", 0, Tag.of("Sample", 0, 0, 0
                )
        );

    }

    @Test
    void testTaskEquals_Success() {

        Task testTask = new Task(
                "Test 1", "Test Desc", 0, new Tag("Sample", 0, 0, 0
                )
        );

        assertEquals(this.task, testTask);

    }

    @Test
    void testTaskEquals_Failure() {

        Task wrongTask = new Task(
                "Wrong", "Wrong", 1, new Tag("Wrong", 0, 0, 0)
        );

        assertNotEquals(wrongTask, this.task);

    }

    @Test
    void testTagSet_Success() {

        Tag testTag = new Tag("Test", 1, 2, 3);
        this.task.setTag(testTag);

        assertEquals(this.task.getTag(), testTag);

    }

}

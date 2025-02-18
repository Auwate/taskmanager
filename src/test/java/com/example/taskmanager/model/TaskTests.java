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
                1L, "Test 1", "Test Desc", 0, Tag.of(
                        1L, "Test tag", Color.of(
                                1L,0, 0, 0
                )
            )
        );

    }

    @Test
    void testTaskEquals_Success() {

        Task testTask = new Task(
                1L, "Test 1", "Test Desc", 0, new Tag(
                        1L, "Test tag", Color.of(
                                1L, 0, 0, 0
                        )
                )
        );

        assertEquals(this.task, testTask);

    }

    @Test
    void testTaskEquals_Failure() {

        Task wrongTask = new Task(
                null, "Wrong", "Wrong", 1, new Tag(
                        2L, "Wrong", Color.of(
                                1L, 0, 0, 0
                        )
                )
        );

        assertNotEquals(wrongTask, this.task);

    }

    @Test
    void testTagSet_Success() {

        Tag testTag = new Tag(1L, "Test", Color.of(1L, 1, 2, 3));
        this.task.setTag(testTag);

        assertEquals(this.task.getTag(), testTag);

    }

    @Test
    void testTaskEqualsNullID_Success() {

        Task testTask = new Task(
                null, "Test 1", "Test Desc", 0, new Tag(
                        null, "Test tag", Color.of(null, 0, 0, 0
                        )
                )
        );

        assertEquals(testTask, this.task);

    }

    @Test
    void testTaskEqualsNullID_Failure() {

        Task testTask = new Task(
                null, "Test 1", "Wrong Desc", 1, new Tag(
                        null, "Test tag", Color.of(null, 0, 0, 0
                        )
                )
        );

        assertNotEquals(testTask, this.task);

    }

}

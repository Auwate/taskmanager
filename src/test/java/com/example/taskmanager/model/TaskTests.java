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
                        1L, "Test tag", null, Color.of(
                                1L,null, 0, 0, 0
                )
            )
        );
        this.task.getTag().setTask(task);
        this.task.getTag().getColor().setTag(task.getTag());

    }

    @Test
    void testTaskEquals_Success() {

        Task testTask = new Task(
                1L, "Test 1", "Test Desc", 0, new Tag(
                        1L, "Test tag", null, Color.of(
                                1L, null, 0, 0, 0
                        )
                )
        );
        testTask.getTag().setTask(task);
        testTask.getTag().getColor().setTag(task.getTag());

        assertEquals(this.task, testTask);

    }

    @Test
    void testTaskEquals_Failure() {

        Task wrongTask = new Task(
                null, "Wrong", "Wrong", 1, new Tag(
                        2L, "Wrong", null, Color.of(
                                1L, null, 0, 0, 0
                        )
                )
        );
        wrongTask.getTag().setTask(task);
        wrongTask.getTag().getColor().setTag(task.getTag());

        assertNotEquals(wrongTask, this.task);

    }

    @Test
    void testTagSet_Success() {

        Tag testTag = new Tag(
                1L, "Test", null, Color.of(
                        1L, null, 1, 2, 3));
        this.task.setTag(testTag);

        assertEquals(this.task.getTag(), testTag);

    }

    @Test
    void testTaskEqualsNullID_Success() {

        Task testTask = new Task(
                null, "Test 1", "Test Desc", 0, new Tag(
                        null, "Test tag", null, Color.of(
                                null, null, 0, 0, 0
                        )
                )
        );

        assertEquals(testTask, this.task);

    }

    @Test
    void testTaskEqualsNullID_Failure() {

        Task testTask = new Task(
                null, "Test 1", "Wrong Desc", 1, new Tag(
                        null, "Test tag", null, Color.of(
                                null, null, 0, 0, 0
                        )
                )
        );

        assertNotEquals(testTask, this.task);

    }

}

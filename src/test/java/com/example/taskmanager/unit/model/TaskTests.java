package com.example.taskmanager.unit.model;

import com.example.taskmanager.model.Color;
import com.example.taskmanager.model.Tag;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TaskTests {

    private Task task;

    @BeforeEach
    void setUp () {

        User user = new User(1L, null);

        this.task = new Task(
                1L, "Test 1", "Test Desc", 0, Tag.of(
                        1L, "Test tag", null, Color.of(
                                1L,null, 0, 0, 0
                )
            ), user
        );
        this.task.getTag().setTask(task);
        this.task.getTag().getColor().setTag(task.getTag());
        user.setTasks(Set.of(this.task));

    }

    @Test
    void testTaskEquals_Success() {

        User user = new User(1L, null);

        Task testTask = new Task(
                1L, "Test 1", "Test Desc", 0, new Tag(
                        1L, "Test tag", null, Color.of(
                                1L, null, 0, 0, 0
                        )
                ), user
        );
        testTask.getTag().setTask(testTask);
        testTask.getTag().getColor().setTag(testTask.getTag());
        user.setTasks(Set.of(testTask));

        assertEquals(this.task, testTask);

    }

    @Test
    void testTaskEquals_Failure() {

        User user = new User(1L, null);

        Task wrongTask = new Task(
                null, "Wrong", "Wrong", 1, new Tag(
                        2L, "Wrong", null, Color.of(
                                1L, null, 0, 0, 0
                        )
                ), user
        );
        wrongTask.getTag().setTask(wrongTask);
        wrongTask.getTag().getColor().setTag(wrongTask.getTag());
        user.setTasks(Set.of(wrongTask));

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

        User user = new User(1L, null);

        Task testTask = new Task(
                null, "Test 1", "Test Desc", 0, new Tag(
                        null, "Test tag", null, Color.of(
                                null, null, 0, 0, 0
                        )
                ), user
        );

        assertEquals(testTask, this.task);

    }

    @Test
    void testTaskEqualsNullID_Failure() {

        User user = new User(1L, null);

        Task testTask = new Task(
                null, "Test 1", "Wrong Desc", 1, new Tag(
                        null, "Test tag", null, Color.of(
                                null, null, 0, 0, 0
                        )
                ), user
        );

        assertNotEquals(testTask, this.task);

    }

}

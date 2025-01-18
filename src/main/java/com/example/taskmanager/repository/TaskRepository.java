package com.example.taskmanager.repository;

import com.example.taskmanager.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(TaskRepository.class);

    private final Map<Long, Task> taskMap;
    private Long currId;

    public TaskRepository(Map<Long, Task> taskMap, Long currId) {
        this.taskMap = taskMap;
        this.currId = currId;
    }

    public List<Task> findAll() {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully tried returning all data.");
        }

        return taskMap.values().stream().toList();

    }

    public Optional<Task> findById(Long id) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully tried finding data by ID.");
        }

        return Optional.ofNullable(taskMap.get(id));

    }

    public Long save(Task task) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully tried saving data.");
        }

        taskMap.put(++currId, task);
        return currId;

    }

    public Optional<Task> delete(Long id) {

        if (logger.isEnabledForLevel(Level.DEBUG)) {
            logger.debug("Successfully tried deleting data.");
        }

        return Optional.ofNullable(taskMap.remove(id));

    }

}

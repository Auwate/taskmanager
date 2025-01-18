package com.example.taskmanager.config;

import com.example.taskmanager.model.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public Map<Long, Task> taskMap() {
        return new HashMap<>();
    }

    @Bean
    public Long currId() {
        return 0L;
    }

}

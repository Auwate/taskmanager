package com.example.taskmanager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK")
public class Task {

    @Id
    @SequenceGenerator(name = "task_seq", sequenceName = "TASK_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private Long id;

    private String name;
    private String description;
    private Integer priority;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Tag tag;

    public Task(
            Long id,
            String name,
            String description,
            Integer priority,
            Tag tag
    ) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.tag = tag;
        this.id = id;
    }

    public Task() {}

    public static Task of(
            Long id,
            String name,
            String description,
            Integer priority,
            Tag tag
    ) {
        return new Task(id, name, description, priority, tag);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) { this.description = description; }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task target &&
                this.getId() != null && target.getId() != null &&
                this.getId().equals(target.getId())
        ) {
            return true;
        }
        return obj instanceof Task target &&
                this.getName().equals(target.getName()) &&
                this.getDescription().equals(target.getDescription()) &&
                this.getPriority().equals(target.getPriority()) &&
                this.getTag().equals(target.getTag());
    }

}

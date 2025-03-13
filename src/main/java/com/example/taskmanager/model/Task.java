package com.example.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK")
public class Task {

    @Id
    @SequenceGenerator(name = "task_seq", sequenceName = "TASK_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "task", orphanRemoval = true)
    @JsonManagedReference
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    public Task(
            Long id,
            String name,
            String description,
            Integer priority,
            Tag tag,
            User user
    ) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.tag = tag;
        this.id = id;
        this.user = user;
    }

    public Task() {}

    public static Task of(
            Long id,
            String name,
            String description,
            Integer priority,
            Tag tag,
            User user
    ) {
        return new Task(id, name, description, priority, tag, user);
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

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

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

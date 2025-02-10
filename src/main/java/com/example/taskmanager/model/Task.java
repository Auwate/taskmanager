package com.example.taskmanager.model;

public class Task {

    private String name;
    private String description;
    private int priority;
    private Tag tag;

    public Task(
            String name,
            String description,
            int priority,
            Tag tag
    ) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.tag = tag;
    }

    public static Task of(
            String name,
            String description,
            int priority,
            Tag tag
    ) {
        return new Task(name, description, priority, tag);
    }

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
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
        return obj instanceof Task target &&
                this.getName().equals(target.getName()) &&
                this.getDescription().equals(target.getDescription()) &&
                this.getPriority() == target.getPriority() &&
                this.getTag().equals(target.getTag());
    }

}

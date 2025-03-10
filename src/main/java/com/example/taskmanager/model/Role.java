package com.example.taskmanager.model;

public class Role {

    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public static Role of(String name) {
        return new Role(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

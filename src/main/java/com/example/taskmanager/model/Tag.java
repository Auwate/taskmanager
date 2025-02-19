package com.example.taskmanager.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag {

    @Id
    @SequenceGenerator(name = "tag_seq", sequenceName = "TAG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @JsonBackReference
    private Task task;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tag", orphanRemoval = true)
    @JsonManagedReference
    private Color color;

    public Tag(Long id, String name, Task task, Color color) {
        this.name = name;
        this.task = task;
        this.color = color;
        this.id = id;
    }

    public Tag() {}

    public static Tag of(Long id, String name, Task task, Color color) {
        return new Tag(id, name, task, color);
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag target &&
                this.getId() != null && target.getId() != null &&
                this.getId().equals(target.getId())
        ) {
            return true;
        }
        return obj instanceof Tag target &&
                this.getName().equals(target.getName()) &&
                this.getColor().equals(target.getColor());
    }

}

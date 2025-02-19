package com.example.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "COLOR")
public class Color {

    @Id
    @SequenceGenerator(name = "color_seq", sequenceName = "COLOR_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "color_seq")
    private Long id;

    @OneToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @JsonBackReference
    private Tag tag;

    @Column(name = "red", nullable = false)
    private int red;

    @Column(name = "green", nullable = false)
    private int green;

    @Column(name = "blue", nullable = false)
    private int blue;

    public Color(Long id, Tag tag, int red, int green, int blue) {
        validate(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.id = id;
        this.tag = tag;
    }

    public Color() {}

    public static Color of(Long id, Tag tag, int red, int green, int blue) {
        return new Color(id, tag, red, green, blue);
    }

    private void validate(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0-255.");
        }
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public Tag getTag() { return tag; }

    public void setTag(Tag tag) { this.tag = tag; }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color target &&
                this.getId() != null && target.getId() != null &&
                this.getId().equals(target.getId())
        ) {
            return true;
        }
        return obj instanceof Color target &&
                this.getRed() == target.getRed() &&
                this.getGreen() == target.getGreen() &&
                this.getBlue() == target.getBlue();
    }

}

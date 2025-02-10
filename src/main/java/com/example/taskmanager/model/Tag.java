package com.example.taskmanager.model;

public class Tag {

    private String name;
    private final Color color; // A tag must have a color, even if it's (0, 0, 0)

    public Tag(String name, int red, int green, int blue) {
        this.name = name;
        this.color = Color.of(red, green, blue);
    }

    public static Tag of(String name, int red, int green, int blue) {
        return new Tag(name, red, green, blue);
    }

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
        this.color.setRed(color.getRed());
        this.color.setGreen(color.getGreen());
        this.color.setBlue(color.getBlue());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag target &&
                this.getName().equals(target.getName()) &&
                this.getColor().equals(target.getColor());
    }

}

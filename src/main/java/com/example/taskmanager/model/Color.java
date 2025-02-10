package com.example.taskmanager.model;

public class Color {

    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        validate(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public static Color of(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    private void validate(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0-255.");
        }
    }

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color target &&
                this.getRed() == target.getRed() &&
                this.getGreen() == target.getGreen() &&
                this.getBlue() == target.getBlue();
    }

}

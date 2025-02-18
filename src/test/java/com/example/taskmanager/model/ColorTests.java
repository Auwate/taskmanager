package com.example.taskmanager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ColorTests {

    private Color color;

    @BeforeEach
    void setUp() {
        this.color = new Color(1L, 1, 2, 3);
    }

    @Test
    void testValidColor_Success() {

        Color color = new Color(1L, 1, 2, 3);

        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen());
        assertEquals(3, color.getBlue());

    }

    @Test
    void testValidColor_Failure() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Color(1L,-1, 0, 0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Color(1L,0, 0, 256)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Color(1L,255, -100, 255)
        );

    }

    @Test
    void testColorEquals_Success() {

        Color testColor = new Color(1L,1, 2, 3);

        assertEquals(testColor, this.color);

    }

    @Test
    void testColorEquals_Failure() {

        Color wrongColor = new Color(null,3, 2, 1);

        assertNotEquals(wrongColor, this.color);

    }

    @Test
    void testColorEqualsNullID_Success() {

        Color testColor = new Color(null, 1, 2, 3);

        assertEquals(testColor, this.color);

    }

    @Test
    void testColorEqualsNullId_Failure() {

        Color wrongColor = new Color(null, 2, 2, 1);

        assertNotEquals(wrongColor, this.color);

    }

}

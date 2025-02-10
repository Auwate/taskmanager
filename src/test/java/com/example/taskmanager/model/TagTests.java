package com.example.taskmanager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TagTests {

    private Tag tag;

    @BeforeEach
    void setUp() {
        this.tag = new Tag("Sample", 1, 2, 3);
    }

    @Test
    void testSetColor_Success() {

        Color color = new Color(1, 2, 3);
        this.tag.setColor(color);

        assertEquals(color, this.tag.getColor());

    }

    @Test
    void testEquals_Failure() {

        Tag wrongTag = new Tag("Wrong", 3, 2, 1);

        assertNotEquals(wrongTag, this.tag);

    }

    @Test
    void testEquals_Success() {

        Tag testTag = new Tag("Sample", 1, 2, 3);

        assertEquals(testTag, this.tag);

    }

}

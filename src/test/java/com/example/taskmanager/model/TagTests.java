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
        this.tag = new Tag(1L, "Sample", Color.of(1L, 1, 2, 3));
    }

    @Test
    void testSetColor_Success() {

        Color color = new Color(1L, 1, 2, 3);
        this.tag.setColor(color);

        assertEquals(color, this.tag.getColor());

    }

    @Test
    void testEquals_Failure() {

        Tag wrongTag = new Tag(2L, "Wrong", Color.of(2L, 3, 2, 1));

        assertNotEquals(wrongTag, this.tag);

    }

    @Test
    void testEquals_Success() {

        Tag testTag = new Tag(1L, "Sample", Color.of(1L, 1, 2, 3));

        assertEquals(testTag, this.tag);

    }

    @Test
    void testEqualsNullID_Success() {

        Tag testTag = new Tag(null, "Sample", Color.of(null, 1, 2, 3));

        assertEquals(testTag, this.tag);

    }

    @Test
    void testEqualsNullID_Failure() {

        Tag wrongTag = new Tag(null, "Wrong", Color.of(null, 1, 2, 3));

        assertNotEquals(wrongTag, this.tag);

    }

}

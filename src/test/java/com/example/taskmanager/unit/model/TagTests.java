package com.example.taskmanager.unit.model;

import com.example.taskmanager.model.Color;
import com.example.taskmanager.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TagTests {

    private Tag tag;

    @BeforeEach
    void setUp() {
        this.tag = new Tag(1L, "Sample", null, Color.of(
                1L, null, 1, 2, 3));
    }

    @Test
    void testSetColor_Success() {

        Color color = new Color(1L, null, 1, 2, 3);
        this.tag.setColor(color);

        assertEquals(color, this.tag.getColor());

    }

    @Test
    void testEquals_Failure() {

        Tag wrongTag = new Tag(2L, "Wrong", null, Color.of(2L, null, 3, 2, 1));

        assertNotEquals(wrongTag, this.tag);

    }

    @Test
    void testEquals_Success() {

        Tag testTag = new Tag(1L, "Sample", null, Color.of(1L, null,  1, 2, 3));

        assertEquals(testTag, this.tag);

    }

    @Test
    void testEqualsNullID_Success() {

        Tag testTag = new Tag(null, "Sample", null, Color.of(null, null, 1, 2, 3));

        assertEquals(testTag, this.tag);

    }

    @Test
    void testEqualsNullID_Failure() {

        Tag wrongTag = new Tag(null, "Wrong", null, Color.of(null, null,  1, 2, 3));

        assertNotEquals(wrongTag, this.tag);

    }

}

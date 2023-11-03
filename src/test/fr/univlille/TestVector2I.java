package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestVector2I {
    Vector2i vector1;
    Vector2i vector2;
    Vector2i vector3;
    Vector2i vector4;
    Vector2i vector5;
    
    @BeforeEach
    public void setup(){
        vector1 = new Vector2i(2, 3);
        vector2 = new Vector2i(4, 6);
        vector3 = new Vector2i(2,3);
        vector4 = new Vector2i(0, 0);
        vector5 = new Vector2i(0, 5);
    }

    @Test
    public void testDistance(){
        assertEquals(5,vector4.distance(vector5));
        assertEquals(3.61,vector1.distance(vector2),0.01);
    }

    @Test
    public void testEquals(){
        assertFalse(vector1.equals(vector2));
        assertTrue(vector1.equals(vector3));
        assertFalse(vector1.equals(null));
    }

    @Test
    public void testToString(){
        assertEquals("(2, 3)",vector1.toString());
        assertNotEquals("4, 6",vector2.toString());
    }
}

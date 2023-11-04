package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCoordinate {
    Coordinate vector1;
    Coordinate vector2;
    Coordinate vector3;
    Coordinate vector4;
    Coordinate vector5;
    
    @BeforeEach
    public void setup(){
        vector1 = new Coordinate(2, 3);
        vector2 = new Coordinate(4, 6);
        vector3 = new Coordinate(2,3);
        vector4 = new Coordinate(0, 0);
        vector5 = new Coordinate(0, 5);
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

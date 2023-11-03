package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestCellEvent {
    CellEvent ce1;

    @BeforeEach
    public void setup(){
        ce1 = new CellEvent(new Vector2i(1,2), CellInfo.WALL, 2);
    }

    @Test
    public void testToString(){
        assertEquals("<(1, 2), WALL, 2>",ce1.toString());
    }
}

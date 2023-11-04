package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class TestGame {
    Game game;

    @BeforeEach
    public void setup() {
        game = new Game();
        game.generateMaze(11, 11);
    }

    @Test
    public void testGameSetEnded() {
        assertFalse(game.isGameEnded());
        game.setGameEnded(true);
        assertTrue(game.isGameEnded());
    }

    @Test
    public void testIsWallAt() { 
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                // all borders must be walls
                if (y == 0 || y == 10 || x == 0 || x == 10) {
                    assertTrue(game.isWallAt(x, y));
                }
                // every other square is supposed to be a wall
                else if (x % 2 == 0 && y % 2 == 0) {
                    assertTrue(game.isWallAt(x, y));
                }
                // all the other cells are supposed to be empty
                else {
                    assertFalse(game.isWallAt(x, y));
                }
            }
        }
    }

    @Test
    public void testMazeIsTheRightSize() {
        game.generateMaze(10, 5);
        assertEquals(10,game.getMazeDimensions().getCol());
        assertEquals(5,game.getMazeDimensions().getRow());
    }
    
    @Test
    public void testRandomPositionIsWithinBounds() {
        Coordinate randomPos = game.randomPosition();
        assertTrue(randomPos.getCol()>=0 && randomPos.getCol()<=game.getMazeDimensions().getCol());
        assertTrue(randomPos.getRow()>=0 && randomPos.getRow()<=game.getMazeDimensions().getRow());
    }

    @Test
    public void testTurnGetsIncremented() {
        game.incrementTurn();
        assertEquals(2,game.getTurn());
    }

    @Test
    public void testGetsAddedToHistory() {
        CellEvent event = new CellEvent(new Coordinate(0,0), CellInfo.WALL,2);
        game.addToHistory(event);
        assertEquals(1,game.getHistory().size());
    }

    
}

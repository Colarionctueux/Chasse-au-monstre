package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;

public class TestGame {
    GameModel game;
    GameParameters gameParameters = new GameParameters();
    
    @BeforeEach
    public void setup() {
        gameParameters.mazeHeight = 10;
        gameParameters.mazeWidth = 10;
        game = new GameModel();
        game.generateMaze(gameParameters);
    }

    @Test
    public void testGameSetEnded() {
        assertFalse(game.isGameEnded());
        game.setGameEnded(true);
        assertTrue(game.isGameEnded());
    }

    @Test
    public void testMazeIsTheRightSize() {
        game.generateMaze(gameParameters);
        assertEquals(10,game.getMazeDimensions().getCol());
        assertEquals(10,game.getMazeDimensions().getRow());
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

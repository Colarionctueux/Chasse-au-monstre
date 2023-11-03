package fr.univlille;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;



public class TestGame {
    Game game;

    @BeforeEach
    public void setup(){
        game = new Game();
        game.generateMaze(5, 5);

    }

    @Test
    public void testGameSetEnded(){
        assertFalse(game.isGameEnded());
        game.setGameEnded(true);
        assertTrue(game.isGameEnded());
    }

    @Test
    public void testIsWallAt(){ 
        for (int i = 0; i<5; i++){ //i being the horizontal, j being the vertical, we check if everywhere from the corner is a wall
            for (int j = 0; j<5; j++){
                if (i == 0 || i == 4 || j == 0 || j == 4){
                    assertTrue(game.isWallAt(i,j));
                }
            }
        }
    }

    @Test
    public void testMazeIsTheRightSize(){
        game.generateMaze(10, 5);
        assertEquals(10,game.getMazeDimensions().getCol());
        assertEquals(5,game.getMazeDimensions().getRow());
    }
    
    @Test
    public void testRandomPositionIsWithinBounds(){
        Vector2i randomPos = game.randomPosition();
        assertTrue(randomPos.getCol()>=0 && randomPos.getCol()<=game.getMazeDimensions().getCol());
        assertTrue(randomPos.getRow()>=0 && randomPos.getRow()<=game.getMazeDimensions().getRow());
    }

    @Test
    public void testTurnGetsIncremented(){
        game.incrementTurn();
        assertEquals(2,game.getTurn());
    }

    @Test
    public void testGetsAddedToHistory(){
        CellEvent event = new CellEvent(new Vector2i(0,0), CellInfo.WALL,2);
        game.addToHistory(event);
        assertEquals(1,game.getHistory().size());
    }

    
}

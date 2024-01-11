package fr.univlille;

import org.junit.Before;
import org.junit.Test;

import fr.univlille.models.GameModel;

public class TestGameModel {
    GameModel gameModel;

    @Before
    public void initialize() {
        gameModel = new GameModel();
        gameModel.generateMaze(new GameParameters());
    }
}

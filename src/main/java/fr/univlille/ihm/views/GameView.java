package fr.univlille.ihm.views;

import java.util.Random;
import java.util.Vector;

import fr.univlille.Game;
import fr.univlille.Vector2i;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameView extends Canvas {
    public Game model;
    public GraphicsContext gc;

    public static int TILE_SIZE = 32;
    
    private Image image = new Image(getClass().getResourceAsStream("/images/spritesheet.png"));

    public GameView(Game model) {
        this.model = model;
        this.gc = getGraphicsContext2D();

        setWidth(TILE_SIZE * model.getMazeDimensions().getCol());
        setHeight(TILE_SIZE * model.getMazeDimensions().getRow());
        
    }

    private final Color HUNTER_FLOOR = new Color(1.0, 1.0, 1.0, 1.0);
    private final Color HUNTER_FLOOR_ALT = new Color(0.78, 0.86, 0.81, 1.0);
    
    private final Color MONSTER_FLOOR = new Color(0.13, 0.56, 0.39, 1.0);
    private final Color MONSTER_FLOOR_ALT = new Color(0.12, 0.62, 0.43, 1.0);

    private void drawMonsterView() {
        gc.setFill(MONSTER_FLOOR);
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        Random random = new Random();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    gc.setFill(MONSTER_FLOOR_ALT);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                if(model.isWallAt(x, y)) {
                    gc.drawImage(image, 0, 64, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    if(random.nextDouble() > 0.85) {
                        switch (random.nextInt(3)) {
                            case 0:
                                gc.drawImage(image, 0, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                                break;
                            case 1:
                                gc.drawImage(image, 64, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                                break;
                            case 2:
                                gc.drawImage(image, 128, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                                break;
                        }
                    }
                }
            }
        }

        Vector2i monsterPosition = model.getMonster().getPosition();
        gc.drawImage(
            image, 0, 0, 64, 64,
            monsterPosition.getCol() * TILE_SIZE,
            monsterPosition.getRow() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );

        Vector2i exitPosition = model.getExit();

        gc.drawImage(
            image, 128, 0, 64, 128,
            exitPosition.getCol() * TILE_SIZE,
            exitPosition.getRow() * TILE_SIZE - TILE_SIZE,
            TILE_SIZE, TILE_SIZE * 2
        );
    }

    private void drawHunterView() {
        gc.setFill(new Color(1.0, 1.0, 1.0, 1.0));
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x == 0 || y == 0 || x == dimensions.getCol() - 1 || y == dimensions.getRow() - 1) {
                    gc.drawImage(image, 0, 64, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public void update(boolean isHunterTurn) {
        if(isHunterTurn) {
            drawHunterView();
        } else {
            drawMonsterView();
        }
    }

}

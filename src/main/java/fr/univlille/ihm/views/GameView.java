package fr.univlille.ihm.views;

import java.util.Random;
import java.util.Vector;

import fr.univlille.Game;
import fr.univlille.Vector2i;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameView extends Canvas {
    public Game model;
    public GraphicsContext gc;

    public static int TILE_SIZE = 32;

    public boolean isHunterTurn = false;

    public boolean isMoveValid = false;
    public Vector2i movePosition;

    private int[][] decorations;

    private Vector2i cursorPosition;
    
    private Image image = new Image(getClass().getResourceAsStream("/images/spritesheet.png"));

    public GameView(Game model) {
        this.model = model;
        this.gc = getGraphicsContext2D();

        Vector2i mazeDimensions = model.getMazeDimensions(); 
        setWidth(TILE_SIZE * mazeDimensions.getCol());
        setHeight(TILE_SIZE * mazeDimensions.getRow());

        addDecorations(mazeDimensions);

        cursorPosition = new Vector2i(0, 0);
        movePosition = new Vector2i(-1, -1);
        setOnMouseMoved((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Vector2i relativeMousePosition = new Vector2i(
                    (int) (event.getSceneX() - getLayoutX() - (TILE_SIZE * 0.5)),
                    (int) (event.getSceneY() - getLayoutY() - (TILE_SIZE * 0.5))
                );
                cursorPosition = new Vector2i((int) (relativeMousePosition.getCol() / TILE_SIZE), (int) (relativeMousePosition.getRow() / TILE_SIZE));
                update();
            }
        });
        
        setOnMousePressed((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isMonterMovementValid(cursorPosition)) {
                    movePosition = cursorPosition;
                    update();
                }
            }
        });
    }

    public void addDecorations(Vector2i mazeDimensions) {
        Random random = new Random();
        decorations = new int[mazeDimensions.getRow()][mazeDimensions.getRow()];
        for (int y = 0; y < mazeDimensions.getRow(); y++) {
            for (int x = 0; x < mazeDimensions.getCol(); x++) {
                if(!model.isWallAt(x, y)) {
                    if(random.nextDouble() > 0.85) {
                        decorations[y][x] = random.nextInt(3);
                    } else {
                        decorations[y][x] = -1;
                    }
                }
            }   
        }
    }

    public boolean isMonterMovementValid(Vector2i movement) {
        Vector2i mazeDimensions = model.getMazeDimensions();

        // On vérifie déjà si le déplacement est dans la grille du jeu
        if(movement.getCol() < 0 || movement.getCol() > mazeDimensions.getCol() || movement.getRow() < 0 || movement.getRow() > mazeDimensions.getRow()) {
            return false;
        }
        
        double distance = model.getMonster().getPosition().distance(movement);
        if(distance > 1.0) {
            return false;
        }

        // Et si le déplacement n'est pas dans un obstacle
        if(model.isWallAt(movement.getCol(), movement.getRow())) {
            return false;
        }
        return true;
    }

    private final Color HUNTER_FLOOR = new Color(1.0, 1.0, 1.0, 1.0);
    private final Color HUNTER_FLOOR_ALT = new Color(0.78, 0.86, 0.81, 1.0);
    
    private final Color MONSTER_FLOOR = new Color(0.13, 0.56, 0.39, 1.0);
    private final Color MONSTER_FLOOR_ALT = new Color(0.12, 0.62, 0.43, 1.0);

    private void drawMonsterView() {
        gc.setFill(MONSTER_FLOOR);
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    gc.setFill(MONSTER_FLOOR_ALT);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                if(model.isWallAt(x, y)) {
                    gc.drawImage(image, 0, 64, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    switch (decorations[y][x]) {
                        case 0:
                            gc.drawImage(image, 0, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            break;
                        case 1:
                            gc.drawImage(image, 64, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            break;
                        case 2:
                            gc.drawImage(image, 128, 128, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                            break;
                        default:
                            break;
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

        if(!isMonterMovementValid(cursorPosition)) {
            gc.drawImage(
                image, 128, 192, 64, 64,
                cursorPosition.getCol() * TILE_SIZE,
                cursorPosition.getRow() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE
            );
        } else {
            gc.drawImage(
                image, 0, 192, 64, 64,
                cursorPosition.getCol() * TILE_SIZE,
                cursorPosition.getRow() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE
            );
        }
        
        gc.drawImage(
            image, 64, 192, 64, 64,
            movePosition.getCol() * TILE_SIZE,
            movePosition.getRow() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    private void drawHunterView() {
        gc.setFill(HUNTER_FLOOR);
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    gc.setFill(HUNTER_FLOOR_ALT);
                    gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
                if(x == 0 || y == 0 || x == dimensions.getCol() - 1 || y == dimensions.getRow() - 1) {
                    gc.drawImage(image, 0, 64, 64, 64, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        if(!isMonterMovementValid(cursorPosition)) {
            gc.drawImage(
                image, 128, 192, 64, 64,
                cursorPosition.getCol() * TILE_SIZE,
                cursorPosition.getRow() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE
            );
        } else {
            gc.drawImage(
                image, 0, 192, 64, 64,
                cursorPosition.getCol() * TILE_SIZE,
                cursorPosition.getRow() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE
            );
        }
        
        gc.drawImage(
            image, 64, 192, 64, 64,
            movePosition.getCol() * TILE_SIZE,
            movePosition.getRow() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    public void update() {
        if(isHunterTurn) {
            drawHunterView();
        } else {
            drawMonsterView();
        }
    }

    public boolean playMove() {
        if(isHunterTurn) {
            return true;
        } else {
            if(isMonterMovementValid(movePosition)) {
                model.getMonster().play(movePosition);
            } else {
                return false;
            }
        }
        movePosition = new Vector2i(-1, 1);
        return true;
    }

}

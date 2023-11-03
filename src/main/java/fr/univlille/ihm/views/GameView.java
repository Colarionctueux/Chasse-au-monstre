package fr.univlille.ihm.views;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.CellEvent;
import fr.univlille.Game;
import fr.univlille.Theme;
import fr.univlille.Vector2i;
import fr.univlille.ihm.GameController;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GameView extends Canvas {
    public Game model;
    public GraphicsContext gc;

    public static int TILE_SIZE = 32;

    public boolean isHunterTurn = false;
    public boolean hunterShooted = false;

    public Vector2i movePosition;

    private int[][] decorations;

    private Vector2i cursorPosition;

    public GameController mainPage;
    
    private Image image = new Image(getClass().getResourceAsStream("/images/spritesheet.png"));
    public Theme theme;

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
                if(model.isGameEnded() || hunterShooted) {
                    return;
                }
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
                if(model.isGameEnded() || hunterShooted) {
                    return;
                }
                if(isHunterTurn && isHunterShootValid(cursorPosition)) {
                    playMove();
                    update();
                } else if(isMonterMovementValid(cursorPosition)) {
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
        if(distance > 1.0 || distance < 1.0) {
            return false;
        }

        // Et si le déplacement n'est pas dans un obstacle
        if(model.isWallAt(movement.getCol(), movement.getRow())) {
            return false;
        }
        return true;
    }

    public boolean isHunterShootValid(Vector2i shoot) {
        Vector2i mazeDimensions = model.getMazeDimensions();
        return shoot.getCol() > 0 && shoot.getCol() < mazeDimensions.getCol() - 1 && shoot.getRow() > 0 && shoot.getRow() < mazeDimensions.getRow() - 1;
    }

    public void drawSimpleTexture(Vector2i spritesheetPosition, Vector2i gamePosition) {
        gc.drawImage(
            image, spritesheetPosition.getCol(), spritesheetPosition.getRow(), 64, 64,
            gamePosition.getCol() * TILE_SIZE,
            gamePosition.getRow() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    public void drawSimpleTexture(int x, int y, Vector2i gamePosition) {
        gc.drawImage(
            image, x, y, 64, 64,
            gamePosition.getCol() * TILE_SIZE,
            gamePosition.getRow() * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }
    
    public void drawSimpleTexture(Vector2i spritesheetPosition, int x, int y) {
        gc.drawImage(
            image, spritesheetPosition.getCol(), spritesheetPosition.getRow(), 64, 64,
            x * TILE_SIZE,
            y * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    public void drawSimpleTexture(int sx, int sy, int gx, int gy) {
        gc.drawImage(
            image, sx, sy, 64, 64,
            gx * TILE_SIZE,
            gy * TILE_SIZE,
            TILE_SIZE, TILE_SIZE
        );
    }

    private void drawMonsterView() {

        // On dessine d'abord le sol
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    drawSimpleTexture(192, 0, x, y);
                } else {
                    drawSimpleTexture(192, 64, x, y);
                }
                if(model.isWallAt(x, y)) {
                    drawSimpleTexture(0, 64, x, y); // Arbre
                } else {
                    // Décorations
                    switch (decorations[y][x]) {
                        case 0:
                            drawSimpleTexture(0, 128, x, y);
                            break;
                        case 1:
                            drawSimpleTexture(64, 128, x, y);
                            break;
                        case 2:
                            drawSimpleTexture(128, 128, x, y);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        Vector2i monsterPosition = model.getMonster().getPosition();
        Vector2i exitPosition = model.getExit();

        gc.drawImage(
            image, 128, 0, 64, 128,
            exitPosition.getCol() * TILE_SIZE,
            exitPosition.getRow() * TILE_SIZE - TILE_SIZE,
            TILE_SIZE, TILE_SIZE * 2
        );
        
        if(!hunterShooted) {
            if(!isMonterMovementValid(cursorPosition)) {
                drawSimpleTexture(128, 192, cursorPosition); // Position souris (si mouvement impossible)
            } else {
                drawSimpleTexture(0, 192, cursorPosition); // Position souris (si mouvement possible)
            }
            drawSimpleTexture(64, 192, movePosition); // Le mouvement
        }

        ArrayList<ICellEvent> shoots = model.getHunter().shootsHistory;
        if(shoots.size() > 0) {
            Vector2i coord = (Vector2i) shoots.get(shoots.size() - 1).getCoord();
            drawSimpleTexture(64, 256, coord);
        }
        drawSimpleTexture(0, 0, monsterPosition); // Joueur
    }

    private void drawHunterView() {
        gc.fillRect(0, 0, getWidth(), getHeight());
        Vector2i dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    drawSimpleTexture(192, 128, x, y);
                } else {
                    drawSimpleTexture(192, 192, x, y);
                }
                if(x == 0 || y == 0 || x == dimensions.getCol() - 1 || y == dimensions.getRow() - 1) {
                    drawSimpleTexture(0, 64, x, y); // Arbre
                }
            }
        }


        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Comic Sans MS", 16));
        for (ICellEvent cellEvent : model.getHunter().shootsHistory) {
            Vector2i coord = (Vector2i) cellEvent.getCoord();
            if(cellEvent.getState() == CellInfo.WALL) {
                drawSimpleTexture(0, 64, coord.getCol(), coord.getRow());
            }
            if(cellEvent.getState() == CellInfo.EMPTY) {
                drawSimpleTexture(192, 256, coord.getCol(), coord.getRow());
            }
            if(cellEvent.getState() == CellInfo.MONSTER) {
                if(cellEvent.getTurn() == model.getTurn()) { // Si le monstre est actuellement sur la case (en gros il est mort)
                    drawSimpleTexture(64, 0, coord.getCol(), coord.getRow()); // Tombe
                } else {
                    if(cellEvent.getState() == CellInfo.MONSTER) {
                        gc.fillText(String.valueOf(cellEvent.getTurn()), coord.getCol() * TILE_SIZE + TILE_SIZE / 2, coord.getRow() * TILE_SIZE + TILE_SIZE / 2);
                    }
                }
            }
        }

        if(!isHunterShootValid(cursorPosition)) {
            drawSimpleTexture(new Vector2i(128, 256), cursorPosition); // Position souris (si mouvement impossible)
        } else {
            drawSimpleTexture(new Vector2i(0, 256), cursorPosition); // Position souris (si mouvement possible)
        }
        drawSimpleTexture(new Vector2i(64, 256), movePosition); // Le mouvement
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
            if(hunterShooted) {
                return false;
            } else {
                hunterShooted = true;
                ICellEvent cellEvent = model.play(cursorPosition);
                if(cellEvent.getState() == CellInfo.WALL) {
                    mainPage.errorLabel.setText("Vous avez touché un arbre.");
                } else if(cellEvent.getState() == CellInfo.MONSTER) {
                    if(cellEvent.getTurn() == model.getTurn()) { // Si le monstre est actuellement sur cette case
                        mainPage.errorLabel.setText("Vous avez tué le monstre! Félicitations!");
                        model.setGameEnded(true);
                    } else {
                        mainPage.errorLabel.setText("Le monstre est passé ici il y a " + (model.getTurn() - cellEvent.getTurn()) + " tours.");
                    }
                } else {
                    mainPage.errorLabel.setText("Vous n'avez rien touché...");
                }
                update();
            }
        } else {
            if(isMonterMovementValid(movePosition)) {
                model.incrementTurn();
                model.getMonster().play(movePosition);
                model.addToHistory(new CellEvent(new Vector2i(movePosition.getCol(), movePosition.getRow()), CellInfo.MONSTER, model.getTurn()));
            } else {
                return false;
            }
        }
        cursorPosition = new Vector2i(-1, -1);
        movePosition = new Vector2i(-1, -1);
        return true;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        switch (theme) {
            case DEFAULT:
                image = new Image(getClass().getResourceAsStream("/images/spritesheet.png"));
                break;
            case HALLOWEEN:
                image = new Image(getClass().getResourceAsStream("/images/spritesheet_halloween.png"));
                break;
            default:
                break;
        }
        update();
    }

}

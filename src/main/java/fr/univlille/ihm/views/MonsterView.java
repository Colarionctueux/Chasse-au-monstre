package fr.univlille.ihm.views;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.Game;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.scene.canvas.GraphicsContext;

public class MonsterView {
    
    public GameView gameView;
    private GraphicsContext gc;
    public Game model;

    /**
     * This array contains the index of decorations within the spritesheet.
     * It has the same dimensions of the maze, and the first element in this 2D-array is the decoration of the first cell within the maze.
     * 
     * 0 would be the first 64x64 image from the spritesheet of the selected theme.
     * Some cells don't have a decoration, and as such the index is "-1".
     */
    private int[][] decorations;

    public MonsterView(GraphicsContext gc, GameView gameView, Game model) {
        this.gc = gc;
        this.gameView = gameView;
        this.model = model;
        addDecorations(model.getMazeDimensions());
    }

    /**
     * Initializes the set of decorations randomly.
     * @param mazeDimensions The dimensions of the maze as an instance of `Coordinate`.
     */
    public void addDecorations(Coordinate mazeDimensions) {
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


    public void draw() {
        Coordinate dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    ViewUtils.drawSimpleTexture(gc, 192, 0, x, y);
                } else {
                    ViewUtils.drawSimpleTexture(gc, 192, 64, x, y);
                }
                if(model.isWallAt(x, y)) {
                    ViewUtils.drawSimpleTexture(gc, 0, 64, x, y); // Arbre
                } else {
                    // Décorations
                    switch (decorations[y][x]) {
                        case 0:
                            ViewUtils.drawSimpleTexture(gc, 0, 128, x, y);
                            break;
                        case 1:
                            ViewUtils.drawSimpleTexture(gc, 64, 128, x, y);
                            break;
                        case 2:
                            ViewUtils.drawSimpleTexture(gc, 128, 128, x, y);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        Coordinate monsterPosition = model.getMonster().getPosition();
        ICoordinate exitPosition = model.getExit();

        gc.drawImage(
            GameView.spritesheet, 128, 0, 64, 128,
            exitPosition.getCol() * GameView.TILE_SIZE,
            exitPosition.getRow() * GameView.TILE_SIZE - GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE * 2
        ); // La sortie
        
        if(!gameView.hunterView.hunterShooted) {
            if(!isMonsterMovementValid(gameView.getCursorPosition())) {
                ViewUtils.drawSimpleTexture(gc, 128, 192, gameView.getCursorPosition()); // Position souris (si mouvement impossible)
            } else {
                ViewUtils.drawSimpleTexture(gc, 0, 192, gameView.getCursorPosition()); // Position souris (si mouvement possible)
            }
            ViewUtils.drawSimpleTexture(gc, 64, 192, gameView.getMovePosition()); // Le mouvement
        }

        ArrayList<ICellEvent> shoots = model.getHunter().shootsHistory;
        if(shoots.size() > 0) {
            Coordinate coord = (Coordinate) shoots.get(shoots.size() - 1).getCoord();
            ViewUtils.drawSimpleTexture(gc, 64, 256, coord);
        }
        ViewUtils.drawSimpleTexture(gc, 0, 0, monsterPosition); // Joueur
    }

        /**
     * Makes sure that the given movement is valid for the monster.
     * The monster cannot move outside of the maze.
     * It cannot move into a wall.
     * It cannot jump cells.
     * @param movement The desired movement of the monster.
     * @return `true` if the movement is valid, `false` otherwise.
     */
    public boolean isMonsterMovementValid(Coordinate movement) {
        Coordinate mazeDimensions = model.getMazeDimensions();

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

    public boolean playMove() {
        if(isMonsterMovementValid(gameView.getMovePosition())) {
            model.incrementTurn();
            model.getMonster().play(gameView.getMovePosition());
            model.addToHistory(new CellEvent(new Coordinate(gameView.getMovePosition().getCol(), gameView.getMovePosition().getRow()), CellInfo.MONSTER, model.getTurn()));
        } else {
            return false;
        }
        gameView.setMovePosition(new Coordinate(-1, -1));
        gameView.setCursorPosition(new Coordinate(-1, -1));
        return true;
    }
}

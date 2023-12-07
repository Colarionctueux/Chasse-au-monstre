package fr.univlille.views;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;
import fr.univlille.models.MonsterModel;
import javafx.scene.canvas.GraphicsContext;

public class MonsterView {
    
    public GameView gameView;
    private GraphicsContext gc;
    public GameModel gameModel;
    public MonsterModel model;

    /**
     * This array contains the index of decorations within the spritesheet.
     * It has the same dimensions of the maze, and the first element in this 2D-array is the decoration of the first cell within the maze.
     * 
     * 0 would be the first 64x64 image from the spritesheet of the selected theme.
     * Some cells don't have a decoration, and as such the index is "-1".
     */
    private int[][] decorations;

    public MonsterView(GraphicsContext gc, GameView gameView, fr.univlille.models.GameModel gameModel) {
        this.gc = gc;
        this.gameView = gameView;
        this.gameModel = gameModel;
        this.model = gameModel.getMonster();
        addDecorations(gameModel.getMazeDimensions());
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
                if(!gameModel.isWallAt(x, y)) {
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
        Coordinate dimensions = gameModel.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    ViewUtils.drawSimpleTexture(gc, 192, 0, x, y);
                } else {
                    ViewUtils.drawSimpleTexture(gc, 192, 64, x, y);
                }
                if(gameModel.isWallAt(x, y)) {
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

        Coordinate monsterPosition = gameModel.getMonster().getPosition();
        ICoordinate exitPosition = gameModel.getExit();

        gc.drawImage(
            GameView.spritesheet, 128, 0, 64, 128,
            exitPosition.getCol() * GameView.TILE_SIZE,
            exitPosition.getRow() * GameView.TILE_SIZE - GameView.TILE_SIZE,
            GameView.TILE_SIZE, GameView.TILE_SIZE * 2
        ); // La sortie
        
        if(!model.isMonsterMovementValid(gameView.getCursorPosition(), 1.0)) {
            ViewUtils.drawSimpleTexture(gc, 128, 192, gameView.getCursorPosition()); // Position souris (si mouvement impossible)
        } else {
            ViewUtils.drawSimpleTexture(gc, 0, 192, gameView.getCursorPosition()); // Position souris (si mouvement possible)
        }
        ViewUtils.drawSimpleTexture(gc, 64, 192, gameView.getMovePosition()); // Le mouvement

        ArrayList<ICellEvent> shoots = gameModel.getHunter().shootsHistory;
        if(shoots.size() > 0) {
            Coordinate coord = (Coordinate) shoots.get(shoots.size() - 1).getCoord();
            ViewUtils.drawSimpleTexture(gc, 64, 256, coord);
        }
        ViewUtils.drawSimpleTexture(gc, 0, 0, monsterPosition); // Joueur
    }

    public boolean playMove() {
        if(model.isMonsterMovementValid(gameView.getMovePosition(), 1.0)) {
            gameModel.incrementTurn();
            gameModel.getMonster().move(gameView.getMovePosition());
            gameModel.addToHistory(new CellEvent(new Coordinate(gameView.getMovePosition().getCol(), gameView.getMovePosition().getRow()), CellInfo.MONSTER, gameModel.getTurn()));
        } else {
            return false;
        }
        gameView.setMovePosition(new Coordinate(-1, -1));
        gameView.setCursorPosition(new Coordinate(-1, -1));
        return true;
    }
}

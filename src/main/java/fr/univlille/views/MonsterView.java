package fr.univlille.views;

import java.util.ArrayList;
import java.util.Random;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.GameParameters;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;
import fr.univlille.models.MonsterModel;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MonsterView {
    
    public GameView gameView;
    private GraphicsContext gc;
    public GameModel gameModel;
    public MonsterModel model;

    public boolean fogEnabled = false;
    private int fogRadius;

    /**
     * This array contains the index of decorations within the spritesheet.
     * It has the same dimensions of the maze, and the first element in this 2D-array is the decoration of the first cell within the maze.
     * 
     * 0 would be the first 64x64 image from the spritesheet of the selected theme.
     * Some cells don't have a decoration, and as such the index is "-1".
     */
    private int[][] decorations;

    public MonsterView(GraphicsContext gc, GameView gameView, fr.univlille.models.GameModel gameModel, GameParameters parameters) {
        this.gc = gc;
        this.gameView = gameView;
        this.gameModel = gameModel;
        this.model = gameModel.getMonster();
        Coordinate mazeDimensions = gameModel.getMazeDimensions();
        
        fogEnabled = parameters.fogOfWar;
        fogRadius = parameters.fogOfWarRadius;

        addDecorations(mazeDimensions);
        if(fogEnabled) {
            model.fogOfWar = new boolean[mazeDimensions.getRow()][mazeDimensions.getCol()];
            updateFog(this.gameModel.getMonster().getPosition());
        }
    }

    public void turnStarted() {
        updateFog(gameModel.getMonster().getPosition());
    }


    /**
     * Initializes the set of decorations randomly.
     * @param mazeDimensions The dimensions of the maze as an instance of `Coordinate`.
     */
    public void addDecorations(Coordinate mazeDimensions) {
        Random random = new Random();
        decorations = new int[mazeDimensions.getRow()][mazeDimensions.getCol()];
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

    private void GraphicStyle() {
        gc.setFill(Color.RED);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Comic Sans MS", 16));
    }

    public void draw() {
        Coordinate dimensions = gameModel.getMazeDimensions();
        GraphicStyle();
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
                if(fogEnabled && !model.fogOfWar[y][x]) {
                    ViewUtils.drawSimpleTexture(gc, 256, 0, x, y);
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
        if(gameModel.getMonster().superJump == true && gameModel.getMonster().superJumpLeft > 0){
            if(!model.isMonsterMovementValid(gameView.getCursorPosition(), 2.0)) {
                ViewUtils.drawSimpleTexture(gc, 128, 192, gameView.getCursorPosition()); // Position souris (si mouvement impossible)
            }
            else {
                ViewUtils.drawSimpleTexture(gc, 0, 192, gameView.getCursorPosition()); // Position souris (si mouvement possible)
            }
        }
        else if(!model.isMonsterMovementValid(gameView.getCursorPosition(), 1.0)) {
            ViewUtils.drawSimpleTexture(gc, 128, 192, gameView.getCursorPosition()); // Position souris (si mouvement impossible)
        } 
        else {
            ViewUtils.drawSimpleTexture(gc, 0, 192, gameView.getCursorPosition()); // Position souris (si mouvement possible)
        }
        ViewUtils.drawSimpleTexture(gc, 64, 192, gameView.getMovePosition()); // Le mouvement

        ArrayList<ICellEvent> shoots = gameModel.getHunter().shootsHistory;
        if(shoots.size() > 0) {
            for(int i = 0; i<shoots.size(); i++){
                Coordinate coord = (Coordinate) shoots.get(i).getCoord();                                   
                ViewUtils.drawSimpleTexture(gc, 64, 256, coord);
                gc.fillText(String.valueOf(shoots.get(i).getTurn() - 1), coord.getCol() * GameView.TILE_SIZE + GameView.TILE_SIZE / 2, coord.getRow() * GameView.TILE_SIZE + GameView.TILE_SIZE / 2);
            }
            
        }
        ViewUtils.drawSimpleTexture(gc, 0, 0, monsterPosition); // Joueur
    }

    public boolean playMove() {
        if(gameModel.getMonster().superJump == true && gameModel.getMonster().superJumpLeft > 0){
            if(model.isMonsterMovementValid(gameView.getMovePosition(), 1.0)) {
                gameModel.incrementTurn();
                gameModel.getMonster().move(gameView.getMovePosition());
                gameModel.addToHistory(new CellEvent(new Coordinate(gameView.getMovePosition().getCol(), gameView.getMovePosition().getRow()), CellInfo.MONSTER, gameModel.getTurn()));
            }
        }
        else if(model.isMonsterMovementValid(gameView.getMovePosition(), 1.0)) {
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


    public void updateFog(ICoordinate coordinate) {
        Coordinate mazeDimensions = gameModel.getMazeDimensions();
        for (int y = -fogRadius; y < fogRadius + 1; y++) {
            for (int x = -fogRadius; x < fogRadius + 1; x++) {
                if(x * x + y * y <= Math.pow(3, 2) ) {
                    int posX = coordinate.getCol() + x;
                    int posY = coordinate.getRow() + y;
                    if(posX >= 0 && posX < mazeDimensions.getCol() && posY >= 0 && posY < mazeDimensions.getRow()) {
                        model.fogOfWar[posY][posX] = true;
                    }
                }
            }
        }
    }
}

package fr.univlille.ihm.views;

import fr.univlille.Coordinate;
import fr.univlille.Game;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class HunterView {

    public GameView gameView;
    private GraphicsContext gc;
    public Game model;
    public boolean hunterShooted = false;

    public HunterView(GraphicsContext gc, GameView gameView, Game model) {
        this.gc = gc;
        this.gameView = gameView;
        this.model = model;
    }


    public void draw() {
        Coordinate dimensions = model.getMazeDimensions();
        for (int y = 0; y < dimensions.getRow(); y++) {
            for (int x = 0; x < dimensions.getCol(); x++) {
                if(x % 2 == 0 && y % 2 == 0 || x % 2 == 1 && y % 2 == 1) {
                    ViewUtils.drawSimpleTexture(gc, 192, 128, x, y);
                } else {
                    ViewUtils.drawSimpleTexture(gc, 192, 192, x, y);
                }
                if(x == 0 || y == 0 || x == dimensions.getCol() - 1 || y == dimensions.getRow() - 1) {
                    ViewUtils.drawSimpleTexture(gc, 0, 64, x, y); // Arbre
                }
            }
        }

        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(new Font("Comic Sans MS", 16));
        for (ICellEvent cellEvent : model.getHunter().shootsHistory) {
            Coordinate coord = (Coordinate) cellEvent.getCoord();
            if(cellEvent.getState() == CellInfo.WALL) {
                ViewUtils.drawSimpleTexture(gc, 0, 64, coord.getCol(), coord.getRow());
            }
            if(cellEvent.getState() == CellInfo.EMPTY) {
                ViewUtils.drawSimpleTexture(gc, 192, 256, coord.getCol(), coord.getRow());
            }
            if(cellEvent.getState() == CellInfo.MONSTER) {
                if(cellEvent.getTurn() == model.getTurn()) { // Si le monstre est actuellement sur la case (en gros il est mort)
                    ViewUtils.drawSimpleTexture(gc, 64, 0, coord.getCol(), coord.getRow()); // Tombe
                } else {
                    if(cellEvent.getState() == CellInfo.MONSTER) {
                        gc.fillText(String.valueOf(cellEvent.getTurn()), coord.getCol() * GameView.TILE_SIZE + GameView.TILE_SIZE / 2, coord.getRow() * GameView.TILE_SIZE + GameView.TILE_SIZE / 2);
                    }
                }
            }
        }

        if(!isHunterShootValid(gameView.getCursorPosition())) {
            ViewUtils.drawSimpleTexture(gc, new Coordinate(128, 256), gameView.getCursorPosition()); // Position souris (si mouvement impossible)
        } else {
            ViewUtils.drawSimpleTexture(gc, new Coordinate(0, 256), gameView.getCursorPosition()); // Position souris (si mouvement possible)
        }
        ViewUtils.drawSimpleTexture(gc, new Coordinate(64, 256), gameView.getMovePosition()); // Le mouvement
    }

    public boolean playMove() {
        if(hunterShooted) {
            return false;
        } else {
            hunterShooted = true;
            ICellEvent cellEvent = model.play(gameView.getCursorPosition());
            if(cellEvent.getState() == CellInfo.WALL) {
                gameView.mainPage.errorLabel.setText("Vous avez touché un arbre.");
            } else if(cellEvent.getState() == CellInfo.MONSTER) {
                if(cellEvent.getTurn() == model.getTurn()) { // Si le monstre est actuellement sur cette case
                    gameView.mainPage.errorLabel.setText("Vous avez tué le monstre! Félicitations!");
                    model.setGameEnded(true);
                } else {
                    gameView.mainPage.errorLabel.setText("Le monstre est passé ici il y a " + (model.getTurn() - cellEvent.getTurn()) + " tours.");
                }
            } else {
                gameView.mainPage.errorLabel.setText("Vous n'avez rien touché...");
            }
            draw();
        }
        gameView.setMovePosition(new Coordinate(-1, -1));
        gameView.setCursorPosition(new Coordinate(-1, -1));
        return true;
    }

    /**
     * Makes sure that the given hunter's target is valid.
     * The hunter cannot shoot outside of the maze and cannot shoot the borders.
     * @param shoot The target's position.
     * @return `true` if the target's position is valid, `false` otherwise.
     */
    public boolean isHunterShootValid(Coordinate shoot) {
        Coordinate mazeDimensions = model.getMazeDimensions();
        return shoot.getCol() > 0 && shoot.getCol() < mazeDimensions.getCol() - 1 && shoot.getRow() > 0 && shoot.getRow() < mazeDimensions.getRow() - 1;
    }
    
}

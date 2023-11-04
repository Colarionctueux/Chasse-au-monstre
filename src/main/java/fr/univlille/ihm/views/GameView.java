package fr.univlille.ihm.views;

import fr.univlille.CellEvent;
import fr.univlille.Game;
import fr.univlille.Theme;
import fr.univlille.Coordinate;
import fr.univlille.ihm.GameController;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GameView extends Canvas {
    /**
     * A reference to the instance of "Game" containing the hunter and monster models, as well as the maze itself.
     */
    public final Game model;

    /**
     * The context that allows us to draw stuff into.
     */
    public final GraphicsContext gc;

    /**
     * A tile is one image from the tileset (spritesheet).
     * Each tile is 32 pixels wide.
     */
    public static final int TILE_SIZE = 32;

    public boolean isHunterTurn = false;

    private Coordinate cursorPosition;
    public Coordinate movePosition;

    public GameController mainPage;

    public HunterView hunterView;
    public MonsterView monsterView;
    
    /**
     * Each image in the game is contained in a spritesheet.
     * A spritesheet is a set of fixed-size images, and each image is a "decoration".
     * Each decoration has a unique index, just like an array.
     */
    public static Image spritesheet = new Image(GameView.class.getResourceAsStream("/images/spritesheet.png"));

    /**
     * The game allows the player to choose a custom theme.
     * Each theme has its own set of graphics.
     */
    public Theme theme;

    public GameView(Game model) {
        this.model = model;
        this.gc = getGraphicsContext2D();

        hunterView = new HunterView(gc, this, model);
        monsterView = new MonsterView(gc, this, model);

        Coordinate mazeDimensions = model.getMazeDimensions(); 
        setWidth(TILE_SIZE * mazeDimensions.getCol());
        setHeight(TILE_SIZE * mazeDimensions.getRow());

        cursorPosition = new Coordinate(0, 0);
        movePosition = new Coordinate(-1, -1);
        setOnMouseMoved((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(model.isGameEnded() || hunterView.hunterShooted) {
                    return;
                }
                Coordinate relativeMousePosition = new Coordinate(
                    (int) (event.getSceneX() - getLayoutX() - (TILE_SIZE * 0.5)),
                    (int) (event.getSceneY() - getLayoutY() - (TILE_SIZE * 0.5))
                );
                cursorPosition = new Coordinate((int) (relativeMousePosition.getCol() / TILE_SIZE), (int) (relativeMousePosition.getRow() / TILE_SIZE));
                draw();
            }
        });
        
        setOnMousePressed((EventHandler<? super MouseEvent>) new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(model.isGameEnded() || hunterView.hunterShooted) {
                    return;
                }
                if(isHunterTurn && hunterView.isHunterShootValid(cursorPosition)) {
                    playMove();
                    draw();
                } else if(monsterView.isMonsterMovementValid(cursorPosition)) {
                    movePosition = cursorPosition;
                    draw();
                }
            }
        });
    }

    public Coordinate getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(Coordinate cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public Coordinate getMovePosition() {
        return movePosition;
    }

    public void setMovePosition(Coordinate movePosition) {
        this.movePosition = movePosition;
    }

    public void draw() {
        if(isHunterTurn) {
            hunterView.draw();
        } else {
            monsterView.draw();
        }
    }

    public boolean playMove() {
        if(isHunterTurn) {
            if(hunterView.hunterShooted) {
                return false;
            } else {
                hunterView.hunterShooted = true;
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
                draw();
            }
        } else {
            if(monsterView.isMonsterMovementValid(movePosition)) {
                model.incrementTurn();
                model.getMonster().play(movePosition);
                model.addToHistory(new CellEvent(new Coordinate(movePosition.getCol(), movePosition.getRow()), CellInfo.MONSTER, model.getTurn()));
            } else {
                return false;
            }
        }
        cursorPosition = new Coordinate(-1, -1);
        movePosition = new Coordinate(-1, -1);
        return true;
    }

    /**
     * Sets the selected theme and re-draws the UI accordingly.
     * If the theme isn't valid, nothing happens.
     * @param theme The theme to be applied to the game.
     */
    public void setTheme(Theme theme) {
        switch (theme) {
            case DEFAULT:
                spritesheet = new Image(getClass().getResourceAsStream("/images/spritesheet.png"));
                break;
            case HALLOWEEN:
                spritesheet = new Image(getClass().getResourceAsStream("/images/spritesheet_halloween.png"));
                break;
            default:
                return;
        }
        this.theme = theme;
        draw();
    }
}

package fr.univlille.views;

import fr.univlille.Theme;
import fr.univlille.controllers.GameController;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;
import fr.univlille.utils.Observer;
import fr.univlille.utils.Subject;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GameView extends Canvas implements Observer {
    /**
     * A reference to the instance of "Game" containing the hunter and monster models, as well as the maze itself.
     */
    public final GameModel model;

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

    public GameView(GameModel model) {
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
                if(model.isGameEnded() || (isHunterTurn && model.getHunter().shootLeft <= 0)) {
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
                if(isHunterTurn) {
                    handleMousePressedHunter();
                } else {
                    handleMousePressedMonster();
                }
            }
        });


        // on attache la vue au hunter
        model.getHunter().attach(this);
    }

    public void handleMousePressedHunter() {
        if(model.isGameEnded() || model.getHunter().shootLeft <= 0) {
            return;
        }
        if(model.getHunter().grenade == true){
            if(model.getHunter().isHunterGrenadeValid(cursorPosition)){
                playGrenade();
            }
        }
        if(model.getHunter().isHunterShootValid(cursorPosition)) {
            play();
        }
        draw();
    }
    
    public void handleMousePressedMonster() {
        if(model.getMonster().isMonsterMovementValid(cursorPosition)) {
            movePosition = cursorPosition;
            draw();
        }
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

    /**
     * Cette fonction affiche sur le Canvas les informations nécessaires. Elle est appellée à chaque mouvement de souris ou à chaque action.
     */
    public void draw() {
        if(isHunterTurn) {
            hunterView.draw();
        } else {
            monsterView.draw();
        }
    }


    public boolean playHunterMove() {
        if(model.getHunter().shootLeft <= 0) {
            return false;
        }
        model.getHunter().shoot(cursorPosition);
        return true;
    }


    public boolean play() {
        boolean isValid = false;
        if(isHunterTurn) {
            isValid = playHunterMove();
        } else {
            isValid = model.getMonster().play(movePosition);
        }
        if(isValid) {
            cursorPosition = new Coordinate(-1, -1);
            movePosition = new Coordinate(-1, -1);
        }
        return isValid;
    }

    public boolean playHunterGrenade() {
        if(model.getHunter().shootLeft <= 0) {
            return false;
        }
        model.getHunter().shoot(cursorPosition);
        return true;
    }


    public boolean playGrenade() {
        boolean isValid = false;
        if(isHunterTurn) {
            isValid = playHunterMove();
        } else {
            isValid = model.getMonster().play(movePosition);
        }
        if(isValid) {
            cursorPosition = new Coordinate(-1, -1);
            movePosition = new Coordinate(-1, -1);
        }
        return isValid;
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

    @Override
    public void update(Subject subj) {
        ICellEvent cellEvent = (ICellEvent) subj;
        if(cellEvent.getState() == CellInfo.WALL) {
            mainPage.errorLabel.setText("Vous avez touché un arbre.");
        } else if(cellEvent.getState() == CellInfo.MONSTER) {
            monsterCase(cellEvent);
        } else {
            mainPage.errorLabel.setText("Vous n'avez rien touché...");
        }
        draw();
    }

    @Override
    public void update(Subject subj, Object data) {
        ICellEvent cellEvent = (ICellEvent) data;
        if(cellEvent.getState() == CellInfo.WALL) {
            mainPage.errorLabel.setText("Vous avez touché un arbre.");
        } else if(cellEvent.getState() == CellInfo.MONSTER) {
            monsterCase(cellEvent);
        } else {
            mainPage.errorLabel.setText("Vous n'avez rien touché...");
        }
        draw();
    }

    private void monsterCase(ICellEvent cellEvent) {
        if(cellEvent.getTurn() == model.getTurn()) { // Si le monstre est actuellement sur cette case
            mainPage.errorLabel.setText("Vous avez tué le monstre! Félicitations!");
            model.setGameEnded(true);
        } else {
            mainPage.errorLabel.setText("Le monstre est passé ici il y a " + (model.getTurn() - cellEvent.getTurn()) + " tours.");
        }
    }
}

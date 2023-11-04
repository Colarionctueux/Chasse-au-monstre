package fr.univlille.ihm;

import fr.univlille.CellEvent;
import fr.univlille.Game;
import fr.univlille.Theme;
import fr.univlille.Coordinate;
import fr.univlille.ihm.views.GameView;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class GameController {

    @FXML
    public Button stopGameButton;
    
    @FXML
    public Label turnLabel;
    
    @FXML
    public Label currentPlayerLabel;

    @FXML
    public VBox mainVBox;

    @FXML
    public AnchorPane switchPane;

    @FXML
    public Label switchPaneCountdown;

    @FXML
    public Label errorLabel;

    @FXML
    public CheckBox halloweenButton;


    private GameView gameView;
    private Game game;

    public void initGame() {
        game = new Game();
        game.generateMaze(11, 11);
        
        if(gameView != null) {
            mainVBox.getChildren().remove(gameView);
        }
        gameView = new GameView(game);
        mainVBox.getChildren().add(2, gameView);
        gameView.draw();
        gameView.mainPage = this;

        currentPlayerLabel.setText("C'est le tour du monstre.");
        turnLabel.setText("Tour n°" + game.getTurn());

        Coordinate monsterPosition = game.getMonster().getPosition();
        game.addToHistory(new CellEvent(new Coordinate(monsterPosition.getCol(), monsterPosition.getRow()), CellInfo.MONSTER, game.getTurn()));
        
        if(halloweenButton.isSelected()) {
            gameView.setTheme(Theme.HALLOWEEN);
        }
    }

    @FXML
    public void initialize() {
        initGame();
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
      }

    @FXML
    public void playButtonPressed() throws InterruptedException {

        if(game.isGameEnded()) {
            return;
        }
        if(gameView.isHunterTurn) {
            gameView.hunterView.hunterShooted = false;
        } else {
            if(gameView.playMove()) {
                errorLabel.setText("");
            } else {
                errorLabel.setText("Movment invalide!");
                return;
            }
            
        }
        turnLabel.setText("Tour n°" + game.getTurn());
        
        if(game.monsterWon()) {
            game.setGameEnded(true);
            gameView.draw();
            errorLabel.setText("Le monstre a gagné!");
            return;
        }
        switchPane.setVisible(true);
        switchPaneCountdown.setText("Dans 3...");
        delay(1000, () -> switchPaneCountdown.setText("Dans 2.."));
        delay(2000, () -> switchPaneCountdown.setText("Dans 1."));
        delay(3000, () -> switchPane.setVisible(false));
        gameView.isHunterTurn = !gameView.isHunterTurn;
        if(gameView.isHunterTurn) {
            currentPlayerLabel.setText("C'est le tour du chasseur.");
        } else {
            currentPlayerLabel.setText("C'est le tour du monstre.");
        }
        gameView.draw();
    }

    @FXML
    public void restartGamePressed() {
        initGame();
    }

    @FXML
    public void halloweenThemePressed() {
        if(gameView.theme == Theme.HALLOWEEN) {
            gameView.setTheme(Theme.DEFAULT);
        } else {
            gameView.setTheme(Theme.HALLOWEEN);
        }
    }
}

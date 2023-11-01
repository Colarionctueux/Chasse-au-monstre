package fr.univlille.ihm;

import fr.univlille.Game;
import fr.univlille.ihm.views.GameView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class GameController {
    @FXML
    public Button stopGameButton;


    @FXML
    public VBox mainVBox;
    @FXML
    public AnchorPane switchPane;

    @FXML
    public Label switchPaneCountdown;

    @FXML
    public Label errorLabel;

    private GameView gameView;
    private Game game;

    @FXML
    public void initialize() {
        game = new Game();
        game.generateMaze(11, 11);
        gameView = new GameView(game);
        mainVBox.getChildren().add(2, gameView);
        gameView.update();
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
        if(gameView.playMove()) {
            errorLabel.setText("");
        } else {
            errorLabel.setText("Movment invalide!");
            return;
        }
        switchPane.setVisible(true);
        switchPaneCountdown.setText("Dans 3...");
        delay(1000, () -> switchPaneCountdown.setText("Dans 2.."));
        delay(2000, () -> switchPaneCountdown.setText("Dans 1."));
        delay(3000, () -> switchPane.setVisible(false));
        // gameView.isHunterTurn = !gameView.isHunterTurn;
        gameView.update();
    }
}

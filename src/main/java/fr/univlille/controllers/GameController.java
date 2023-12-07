package fr.univlille.controllers;

import fr.univlille.CellEvent;
import fr.univlille.Coordinate;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import fr.univlille.models.GameModel;
import fr.univlille.views.GameView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class GameController {

    @FXML
    public Button stopGameButton;

    @FXML
    public Button grenadeButton;

    @FXML
    public Label turnLabel;

    @FXML 
    public Label grenadeLabel;

    @FXML
    public Label test;
    
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



    private GameView gameView;
    private GameModel game;

    /**
     * Cette méthode permet d'initialiser la partie. Elle est appellé à chaque rédemarrage du jeu.
     */
    public void initGame() {
        game = new GameModel();
        game.generateMaze(19, 19);
        
        if(gameView != null) {
            mainVBox.getChildren().remove(gameView);
        }
        gameView = new GameView(game);
        mainVBox.getChildren().add(3, gameView);
        gameView.draw();
        gameView.mainPage = this;

        currentPlayerLabel.setText("C'est le tour du monstre.");
        turnLabel.setText("Tour n°" + game.getTurn());

        // On ajoute la première position du monstre dans l'historique
        Coordinate monsterPosition = game.getMonster().getPosition();

        // Cela peut paraître bizarre de récreer une coordonnée avec les mêmes coordonnées,
        // mais c'est simplement car sinon les deux instances seront liés et cette position
        // sera dans l'historique sera modifiée à chaque nouveau déplacement du monstre (ce qu'on ne veut pas!)
        game.addToHistory(new CellEvent(new Coordinate(monsterPosition.getCol(), monsterPosition.getRow()), CellInfo.MONSTER, game.getTurn()));
    }


    @FXML
    public void initialize() {
        initGame();
    }

    /** Cette méthode permet de créer un Thread qui attends automatiquement le nombre de millisecondes données en paramètre, puis éxecute le code de l'argument continuation.
     * @param millis Le nombre de millisecondes à attendre
     * @param continuation Le code à éxecuter à la fin du delay.
     */
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
        if(gameView.isHunterTurn || gameView.play()) {
            errorLabel.setText("");
        } else {
            errorLabel.setText("Mouvement invalide!");
            return;
        }
        turnLabel.setText("Tour n°" + game.getTurn());
        
        if(game.monsterWon()) {
            game.setGameEnded(true);
            errorLabel.setText("Le monstre a gagné!");
            gameView.draw();
            return;
        }
        swapScreen();
        gameView.draw();
    }

    @FXML
    public void grenadeButtonPressed() throws InterruptedException {
        if(game.getHunter().grenadeLeft > 0){
            if(game.getHunter().grenade == true){
                grenadeLabel.setText(" ");
                game.getHunter().grenade = false;
            }
            else{
                grenadeLabel.setText("Actif");
                game.getHunter().grenade = true;
            }
        }
        else{
            grenadeLabel.setText("Vous n'avez plus de grenade...");
        }
    }

    private void swapScreen() {
        // Animation de l'écran
        switchPane.setVisible(true);
        switchPaneCountdown.setText("Dans 3...");
        delay(1000, () -> switchPaneCountdown.setText("Dans 2.."));
        delay(2000, () -> switchPaneCountdown.setText("Dans 1."));
        delay(3000, () -> switchPane.setVisible(false));

        // On échange les tours
        gameView.isHunterTurn = !gameView.isHunterTurn;
        if(gameView.isHunterTurn) {
            game.getHunter().turnBegin();
            currentPlayerLabel.setText("C'est le tour du chasseur.");
            grenadeButton.isVisible();
        } else {
            game.getMonster().turnBegin();
            currentPlayerLabel.setText("C'est le tour du monstre.");
            
        }
    }

    @FXML
    public void restartGamePressed() {
        initGame();
    }
}

package fr.univlille;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class App extends Application {

    private static Parent loadFXML(String filename) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/" + filename + ".fxml"));
        
        if(fxmlLoader.getLocation() == null) {
            System.err.println("Le chemin du fichier FXML est invalide!");
            System.exit(1);
        }

        Parent parent = fxmlLoader.load();

        return parent;
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        // VBox root = new VBox();
        // Scene scene = new Scene(root, 600, 500);
        // stage.setScene(scene);
        // Game game = new Game();
        // game.generateMaze(11, 11);
        // GameView gameView = new GameView(game);
        // root.getChildren().addAll(new Label("Test du jeu"), gameView);
        // gameView.update();
        // stage.show();
        Parent parent = loadFXML("game");
        Scene scene = new Scene(parent, 800, 510);
        stage.setScene(scene);
        stage.setTitle("Chasse au monstre");
        stage.show();
    }
}
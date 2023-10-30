package fr.univlille.ihm;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class App extends Application {
    private static Parent loadFXML(String filename) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(filename + ".fxml"));
        if(fxmlLoader.getLocation() == null) {
            System.err.println("Le chemin du fichier FXML est invalide!");
            System.exit(1);
        }
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Chasse au monstre");
        Scene scene = new Scene(loadFXML("menu"), 800, 510);
        stage.setScene(scene);
        stage.setTitle("Chasse au monstre");
        stage.show();
    }
}
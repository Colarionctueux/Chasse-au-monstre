package fr.univlille;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class App extends Application {
    private static App app;

    public static App getApp() {
        if(app == null) {
            app = new App();
        }
        return app;
    }

    private static Scene scene;

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
        Parent parent = loadFXML("menu");
        App.scene = new Scene(parent, 1000, 1000);
        stage.setScene(scene);
        stage.setTitle("Chasse au monstre");
        stage.show();
    }

    public void changeScene(String name) throws IOException {
        scene.setRoot(loadFXML(name));
    }
}
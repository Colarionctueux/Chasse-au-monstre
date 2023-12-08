package fr.univlille.controllers;


import java.io.IOException;

import fr.univlille.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MultiplayerMenuController extends AnchorPane {
    private App app;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    private Button boutonHeberger;
    
    @FXML
    private Button boutonRejoindre;

    @FXML
    public void backButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("menu");
    }

    @FXML
    public void localButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("game");
    }

    @FXML
    public void lanButtonPressed() throws IOException {
        boutonHeberger.setVisible(true);
        boutonRejoindre.setVisible(true);
    }


}

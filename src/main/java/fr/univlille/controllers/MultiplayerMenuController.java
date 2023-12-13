package fr.univlille.controllers;

import java.io.IOException;

import fr.univlille.App;
import fr.univlille.multiplayer.MultiplayerUtils;
import fr.univlille.multiplayer.Server;
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
        app.changeScene("settings");
    }

    @FXML
    public void hostButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("lobby");
        if (!Server.getInstance().isAlive()) {
            Server.getInstance().host();
            System.out.println("Server is running at '" + MultiplayerUtils.getHostname() + "'");
        }
    }
    
    @FXML
    public void joinButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("recherche");
    }

    @FXML
    public void lanButtonPressed() {
        boutonHeberger.setVisible(!boutonHeberger.isVisible());
        boutonRejoindre.setVisible(!boutonRejoindre.isVisible());
    }
}

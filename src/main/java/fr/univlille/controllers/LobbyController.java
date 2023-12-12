package fr.univlille.controllers;


import java.io.IOException;

import fr.univlille.App;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class LobbyController extends AnchorPane {
    private App app;

    @FXML
    public void backButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("menu");
    }
}

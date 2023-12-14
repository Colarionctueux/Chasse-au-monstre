package fr.univlille.controllers;


import java.io.IOException;

import fr.univlille.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LobbyController extends AnchorPane {
    private App app;

    @FXML
    private Label wtf;

    public void initialize() {
        wtf.setText("FDHSHGREGtreuhrhhgug");
    }

    @FXML
    public void backButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("menu");
    }
}

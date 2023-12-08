package fr.univlille.controllers;

import java.io.IOException;

import fr.univlille.App;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;

public class SettingsController {
    @FXML
    public CheckBox halloweenTheme;

    @FXML
    public Spinner<Integer> hunterShootsSpinner;
    
    @FXML
    public Spinner<Integer> hunterGrenadesSpinner;
    
    @FXML
    public Spinner<Integer> mazeSizeXSpinner;
    
    @FXML
    public Spinner<Integer> mazeSizeYSpinner;


    @FXML
    public void startGamePressed() throws IOException {
        App.getApp().changeScene("game");
    }
    
    @FXML
    public void cancelPressed() throws IOException {
        App.getApp().changeScene("menu");
    }
}

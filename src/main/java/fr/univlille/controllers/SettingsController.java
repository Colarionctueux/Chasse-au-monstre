package fr.univlille.controllers;

import java.io.IOException;

import fr.univlille.App;
import fr.univlille.GameParameters;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class SettingsController {
    
    @FXML
    public CheckBox fogOfWarCheckBox;
    
    @FXML
    public Spinner<Integer> fogOfWarSpinner;

    @FXML
    public Spinner<Integer> hunterShootsSpinner;

    
    @FXML
    public Spinner<Integer> hunterGrenadesSpinner;
    
    @FXML
    public Spinner<Integer> mazeSizeXSpinner;
    
    @FXML
    public Spinner<Integer> mazeSizeYSpinner;
    
    @FXML
    public Spinner<Integer> wallPercentageSpinner;
    
    
    private void bindFactory(Spinner<Integer> spinner, int min, int max, int defaultValue) {
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max);
        factory.setValue(defaultValue); 
        spinner.setValueFactory(factory);
    }

    
    @FXML
    public void initialize() {
        bindFactory(hunterShootsSpinner, 1, 10, 1);
        bindFactory(hunterGrenadesSpinner, 0, 10, 1);
        bindFactory(mazeSizeXSpinner, 5, 19, 9);
        bindFactory(mazeSizeYSpinner, 5, 19, 9);
        bindFactory(wallPercentageSpinner, 50, 100, 100);
        bindFactory(fogOfWarSpinner, 1, 10, 1);

        
    }


    @FXML
    public void startGamePressed() throws IOException {
        GameParameters parameters = new GameParameters();
        parameters.setMazeWidth(mazeSizeXSpinner.getValue());
        parameters.setMazeHeight(mazeSizeYSpinner.getValue());
        parameters.setHunterShoots(hunterShootsSpinner.getValue());
        parameters.setHunterGrenades(hunterGrenadesSpinner.getValue());

        // fog of war
        parameters.setFogOfWar(fogOfWarCheckBox.isSelected());
        parameters.setFogOfWarRadius(fogOfWarSpinner.getValue());

        parameters.setWallsPercentage(wallPercentageSpinner.getValue() / 100.0);
        
        App.getApp().startGame(parameters);
    }
    
    @FXML
    public void cancelPressed() throws IOException {
        App.getApp().changeScene("menu");
    }

    @FXML
    public void fogChecked() {
        fogOfWarSpinner.setVisible(!fogOfWarSpinner.isVisible());
    }
}

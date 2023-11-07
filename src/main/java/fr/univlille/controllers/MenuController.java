package fr.univlille.controllers;


import fr.univlille.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    private App app;

    public App getApp() {
        return app;
    }


    public void setApp(App app) {
        this.app = app;
    }


    @FXML
    public Button pvpButton;
    
    @FXML
    public Button pveButton;

    
    @FXML
    public void gitlabLinkPressed() {
    //     getHostServices().showDocument("https://gitlab.univ-lille.fr/sae2.01-2.02/2023/F4");
    }
}

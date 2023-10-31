package fr.univlille.ihm;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

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

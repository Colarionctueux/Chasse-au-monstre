package fr.univlille.controllers;

import java.io.IOException;

import fr.univlille.App;
import fr.univlille.multiplayer.Client;
import fr.univlille.multiplayer.MultiplayerCommand;
import fr.univlille.multiplayer.MultiplayerCommunication;
import fr.univlille.multiplayer.MultiplayerUtils;
import fr.univlille.multiplayer.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LobbyController extends AnchorPane {
    private App app;

    @FXML
    private Label hostname_label;

    @FXML
    private Label client_name;

    @FXML
    public void backButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("menu");
        Server.getInstance().kill();
    }

    public void initialize() {
        if (Client.getInstance().isAlive()) {
            // The client should already have the welcome message in its pending communications.
            MultiplayerCommunication welcomeMessage = Client.getInstance().pollCommunication();
            if (welcomeMessage.isCommand(MultiplayerCommand.HOST) && welcomeMessage.hasParameters()) {
                hostname_label.setText(welcomeMessage.getParameter(0));
                client_name.setText(MultiplayerUtils.getHostname());
            }
        } else {
            Server server = Server.getInstance();
            // The server listens to the client's arrival
            // in a non-blocking way for the main thread.
            // Indeed we don't know when he's going to arrive, or if he's going to arrive at all.
            server.setIncomingCommunicationCallback(() -> {
                // Because we cannot update the JavaFX UI outside of the main thread,
                // as it would cause synchronization issues,
                // we tell it to run it "later"
                // (so as soon as it can).
                Platform.runLater(() -> {
                    MultiplayerCommunication announce = server.pollCommunication();
                    if (announce.isCommand(MultiplayerCommand.JOIN) && announce.hasParameters()) {
                        client_name.setText(announce.getParameter(0));
                        // the server should not receive any other request
                        server.stopIncomingCommunicationCallback();
                        server.closeRequests();
                    }
                });
            });
            hostname_label.setText(MultiplayerUtils.getHostname());
        }
    }
}

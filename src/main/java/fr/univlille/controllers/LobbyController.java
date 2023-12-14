package fr.univlille.controllers;

import java.io.IOException;

import fr.univlille.App;
import fr.univlille.models.LobbyModel;
import fr.univlille.multiplayer.Client;
import fr.univlille.multiplayer.MultiplayerCommand;
import fr.univlille.multiplayer.MultiplayerCommunication;
import fr.univlille.multiplayer.MultiplayerUtils;
import fr.univlille.multiplayer.Server;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

public class LobbyController extends AnchorPane {
    private LobbyModel model;
    private App app;

    @FXML
    private Label hostname_label;

    @FXML
    private Label client_name;

    @FXML
    private Label host_role;

    @FXML
    private Label client_role;

    @FXML
    private Button button_start_game;

    @FXML
    public void backButtonPressed() throws IOException {
        app = App.getApp();
        app.changeScene("menu");
        try {
            if (Server.getInstance().isAlive()) {
                Server.getInstance().kill(true);
            } else {
                Client.getInstance().kill(true);
            }
        } catch (Exception e) {
            System.err.println("Cannot kill the instance of multiplayer body : " + e.getMessage());
        }
    }

    @FXML
    public void startGame() throws IOException {
        // The host is the only able to start the game.
        // He cannot start it if there is no client.
        if (Client.getInstance().isAlive()) {
            System.out.println("starting game...");
        }
    }

    @FXML
    public void reverseRoles() {
        // Only the host can change the roles
        if (Server.getInstance().isAlive()) {
            model.invertRoles();
            applyModel();
            try {
                Server.getInstance().broadcast(
                    new MultiplayerCommunication(
                        MultiplayerCommand.INVERTED_ROLES,
                        model.isHostHunter() ? "1" : "0"
                    )
                );
            } catch (IOException e) {
                System.err.println("Broadcast of inverted roles failed : " + e.getMessage());
                // TODO: handle error message
            }
        }
    }

    private void applyModel() {
        if (model.isHostHunter()) {
            host_role.setText("Chasseur");
            client_role.setText("Monstre");
        } else {
            host_role.setText("Monstre");
            client_role.setText("Chasseur");
        }
    }

    private void hideStartGameButton() {
        button_start_game.setDisable(true); 
        button_start_game.setVisible(false);
    }

    public void initialize() {
        this.model = new LobbyModel();
        if (Client.getInstance().isAlive()) {
            hideStartGameButton(); // the client cannot launch the game
            Client client = Client.getInstance();

            // Will handle all messages from the server (in the lobby only)
            client.setIncomingCommunicationCallback(() -> {
                Platform.runLater(() -> {
                    MultiplayerCommunication message = Client.getInstance().pollCommunication();
                    try {
                        switch (message.getCommand()) {
                            case HOST:
                                hostname_label.setText(message.getParameter(0));
                                client_name.setText(MultiplayerUtils.getHostname());
                                break;
                            case INVERTED_ROLES:
                                model.setIsHostHunter(Integer.parseInt(message.getParameter(0)) == 1);
                                applyModel();
                                break;
                            case SERVER_TERMINATION:
                                try {
                                    System.out.println("server termination, about to kill Client");
                                    Client.getInstance().kill(false);
                                    App.getApp().changeScene("menu");
                                } catch (Exception e) {
                                    System.out.println("Server termination led to error : " + e.getMessage());
                                }
                                break;
                            default:
                                System.out.println("incoming communication from server was ignored by client : " + message);
                                // ignored
                        }
                    } catch (Exception e) {
                        System.err.println("An error occured while interpreting the communication of the server : " + message);
                        e.printStackTrace();
                    }
                });
            });
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
                    System.out.println("Server handling communication : " + announce);
                    switch (announce.getCommand()) {
                        case JOIN:
                            client_name.setText(announce.getParameter(0));
                            server.closeRequests();
                            break;
                        case DISCONNECTION:
                            client_name.setText("???");
                            server.reacceptRequests();
                            break;
                        default:
                            System.out.println("incoming communication from server was ignored by client : " + announce);
                            // ignored
                    }
                });
            });
            hostname_label.setText(MultiplayerUtils.getHostname());
        }
    }
}

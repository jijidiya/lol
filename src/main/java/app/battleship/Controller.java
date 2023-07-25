package app.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

    @FXML
    private Button newGameButton;
    @FXML
    private Button newCustomGame;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button newAIGameButton;

    // Méthode pour démarrer une nouvelle partie
    @FXML
    private void startNewGame(ActionEvent e) {
        // À compléter : Afficher la configuration pour une nouvelle partie (choix des options)
    }
    @FXML
    private void startNewCustomGame(ActionEvent e){

    }

    // Méthode pour charger une partie existante depuis un fichier
    @FXML
    private void loadGame(ActionEvent e) {
        // À compléter : Afficher la fenêtre de chargement de partie (si nécessaire)
    }
    @FXML
    private void startNewAIGame(ActionEvent e){

    }

    // Méthode pour quitter l'application
    @FXML
    private void exit(ActionEvent e) {
        System.exit(0);
    }
}

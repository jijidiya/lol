package app.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import classes.BattleShipGameController;
import classes.CombatZone;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class Controller {
    @FXML
    private Pane combatPane; // Référence au Pane avec l'ID "combatPane" dans le fichier FXML
    @FXML
    private Group zone;
    @FXML
    private TextField columnsTextField; // Champ de texte pour les colonnes
    @FXML
    private TextField rowsTextField; // Champ de texte pour les lignes
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
    private void startNewGame(ActionEvent e) throws Exception {
        NormalGame normalGame = new NormalGame();
        normalGame.start(new Stage());
        newGameButton.getScene().getWindow().hide();
    }
    @FXML
    private void startNewCustomGame(ActionEvent e) throws IOException {
        CustomGame customGame = new CustomGame();
        customGame.start(new Stage());
        newCustomGame.getScene().getWindow().hide();


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

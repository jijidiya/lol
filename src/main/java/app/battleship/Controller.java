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
import java.util.Optional;


public class Controller {
    @FXML
    private Pane combatPane; // Référence au Pane avec l'ID "combatPane" dans le fichier FXML

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
    private void startNewGame(ActionEvent e) {
        // À compléter : Afficher la configuration pour une nouvelle partie (choix des options)
    }
    @FXML
    private void startNewCustomGame(ActionEvent e) {
        CustomGameDialog customGameDialog = new CustomGameDialog();
        customGameDialog.showAndWait();

        int customWidth = customGameDialog.getCustomWidth();
        int customHeight = customGameDialog.getCustomHeight();

        if (customWidth >= 6 && customHeight >= 6 && customWidth <= 26 && customHeight <= 26) {
            // Créer une nouvelle grille personnalisée
            BattleShipGameController gameController = new BattleShipGameController(customWidth, customHeight, new int[]{6, 5, 4, 3, 2, 1});
            int[][] customGrid = gameController.getGrid();
            CombatZone zone = new CombatZone(customGrid);
            gameController.setGridRectangles(zone.getGridRectangles());
            Group zoneGroup = zone.getZone();

            // Ajouter la zone de combat au Pane combatPane
            combatPane.getChildren().clear();
            combatPane.getChildren().add(zoneGroup);

            // À ce stade, la nouvelle partie personnalisée est prête à être jouée !
        } else {
            // Afficher un message d'erreur si les dimensions sont invalides
            // (vous pouvez également mettre en surbrillance les champs de texte en rouge pour indiquer une erreur)
            System.out.println("Dimensions invalides. La grille doit avoir une taille comprise entre 6x6 et 26x26.");
        }
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

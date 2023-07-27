package app.battleship;

import classes.BattleShipGameController;
import classes.CombatZone;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class NormalGame extends Application {
    private Group zone;
    private BattleShipGameController gameController;
    private CombatZone combatZone;
    private TextField targetInput;
    private Label resultLabel;
    @Override
    public void start(Stage thirdStage) throws IOException {
        thirdStage.setTitle("Normal Game");

        gameController = new BattleShipGameController();
        gameController.placeShipsRandomly();
        combatZone = new CombatZone(gameController.getGrid());
        zone = combatZone.getZone();
        gameController.setGridRectangles(combatZone.getGridRectangles());
        BorderPane root = new BorderPane(zone);
        root.setStyle("-fx-background-color: #333333;");

        // Crée un HBox pour la zone de saisie et le bouton "Tirer"
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10));
        targetInput = new TextField();
        targetInput.setPromptText("Entrez la position cible (ex: A3)");
        Button fireButton = new Button("Tirer");
        fireButton.setOnAction(e -> handleFireButtonClick());
        inputBox.getChildren().addAll(targetInput, fireButton);

        // Ajoute le HBox à la zone inférieure (bottom) du BorderPane
        root.setBottom(inputBox);


        Scene scene = new Scene(root, 1350, 685);
        thirdStage.setScene(scene);
        thirdStage.show();

    }

    private void handleFireButtonClick() {
        String targetPosition = targetInput.getText().trim().toUpperCase();
        if (gameController.isValidTargetPosition(targetPosition)) {
            String result = gameController.fireAtTargetPosition(targetPosition);
            resultLabel.setText(result);
        } else {
            resultLabel.setText("Position cible invalide !");
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}

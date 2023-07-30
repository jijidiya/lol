package app.battleship;

import classes.BattleShipGameController;
import classes.CombatZone;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class NormalGame extends Application {
    private Group zone;
    private BattleShipGameController gameController;
    private CombatZone combatZone;
    private TextField targetInput;
    private TextArea distanceTextArea;
    private Label resultLabel = new Label();
    private ImageView backButtonImageView;
    private Button backButton;
    @Override
    public void start(Stage thirdStage) throws IOException {
        thirdStage.setTitle("Normal Game");

        gameController = new BattleShipGameController();
        gameController.placeShipsRandomly();
        combatZone = new CombatZone(gameController.getGrid());
        zone = combatZone.getZone();
        gameController.setGridRectangles(combatZone.getGridRectangles());
        zone.setTranslateX(140);
        BorderPane root = new BorderPane(zone);
        root.setStyle("-fx-background-color: #333333;");

        // Chargement de l'image à partir des ressources
        Image backButtonImage = new Image(getClass().getResource("image/back_button.png").openStream());
        backButtonImageView = new ImageView(backButtonImage);

        // Création du bouton avec l'image
        backButton = new Button();
        backButton.setGraphic(backButtonImageView);
        backButton.setOnAction(e -> {
            try {
                handleBackButtonClick();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        backButton.setStyle("-fx-background-color: #333333;");

        // Ajout du bouton dans le coin supérieur gauche du BorderPane
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
        BorderPane.setMargin(backButton, new Insets(10));
        root.setTop(backButton);

        // Crée un HBox pour la zone de saisie et le bouton "Tirer"
        HBox inputBox = new HBox(10);
        inputBox.setPadding(new Insets(10));
        targetInput = new TextField();
        targetInput.setPromptText("Entrez la position cible (ex: A3)");
        Button fireButton = new Button("Tirer");
        fireButton.setOnAction(e -> handleFireButtonClick());
        inputBox.getChildren().addAll(targetInput, fireButton);




        distanceTextArea = new TextArea();
        distanceTextArea.setEditable(false);
        distanceTextArea.setWrapText(true); // Permet le retour à la ligne automatique
        distanceTextArea.setPrefSize(285, 150);
        distanceTextArea.setMaxHeight(285);
        distanceTextArea.setMaxHeight(150);
        distanceTextArea.setTranslateY(200);
        root.setRight(distanceTextArea);

        // Ajoute le HBox à la zone inférieure (bottom) du BorderPane
        root.setBottom(inputBox);
        inputBox.setTranslateX(565);
        inputBox.setTranslateY(-105);


        Scene scene = new Scene(root, 1350, 685);
        thirdStage.setScene(scene);
        thirdStage.show();

    }

    private void handleBackButtonClick() throws Exception {
        BattleShipApp startMenu = new BattleShipApp();
        startMenu.start(new Stage());
    }

    private void handleFireButtonClick() {
        String targetPosition = targetInput.getText().trim().toUpperCase();
        if (gameController.isValidTargetPosition(targetPosition)) {
            String result = gameController.fireAtTargetPosition(targetPosition);
            resultLabel.setText(result);

            // Affiche les distances de Manhattan dans distanceTextField
            String distanceResult = gameController.getDistanceFromShips(targetPosition);

            distanceTextArea.setText(distanceResult);
            // Ajout des retours à la ligne pour le résultat
            //resultLabel.setText(result.replace("\n", System.getProperty("line.separator")));
        } else {
            resultLabel.setText("Position cible invalide !");
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}

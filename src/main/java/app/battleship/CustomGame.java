package app.battleship;

import classes.BattleShipGameController;
import classes.CombatZone;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Group;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class CustomGame extends Application {
    private BattleShipGameController gameController;
    private CombatZone combatZone;
    private Group zone;
    private TextField targetInput;
    private TextArea distanceTextArea;
    private Label resultLabel = new Label();
    private Label distanceLabel;

    private ImageView backButtonImageView;
    private Button backButton;
    private Button cheatModeButton;
    private boolean isCheat;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bataille Navale");

        int[] customDimensions = showCustomGridDialog();
        int gridWidth = customDimensions[0];
        int gridHeight = customDimensions[1];

        // Vérifie si l'utilisateur a annulé la saisie ou si les dimensions sont incorrectes
        if (gridWidth == 0 || gridHeight == 0) {
            handleBackButtonClick(); // Ferme la fenêtre si les dimensions sont incorrectes ou si l'utilisateur a annulé la saisie
        } else {
            // Génération de la grille personnalisée et affichage de la zone de combat
            gameController = new BattleShipGameController(gridWidth, gridHeight, new int[]{6, 5, 4, 3, 2, 1});
            gameController.placeShipsRandomly();
            combatZone = new CombatZone(gameController.getGrid());
            gameController.setGridRectangles(combatZone.getGridRectangles());
            zone = combatZone.getZone();
            BorderPane root = new BorderPane(zone);
            root.setStyle("-fx-background-color: #333333;");

            // Chargement de l'image du bouton retour
            Image backButtonImage = new Image(getClass().getResource("image/back_button.png").openStream());
            backButtonImageView = new ImageView(backButtonImage);

            // Bouton retour
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
            // Bouton du mode triche
            cheatModeButton = new Button("Mode Triche");
            cheatModeButton.setOnAction(e -> activateCheatMode());

            // Ajout du bouton dans le coin supérieur gauche du BorderPane
            BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
            BorderPane.setMargin(backButton, new Insets(10));
            root.setLeft(backButton);

            // Ajoute le bouton "Activer le mode triche" dans le coin supérieur droit du BorderPane
            BorderPane.setAlignment(cheatModeButton, Pos.TOP_RIGHT);
            BorderPane.setMargin(cheatModeButton, new Insets(10));
            root.setTop(cheatModeButton);

            // Crée un HBox pour la zone de saisie et le bouton "Tirer"
            HBox inputBox = new HBox(10);
            inputBox.setPadding(new Insets(10));
            targetInput = new TextField();
            targetInput.setPromptText("Entrez la position cible (ex: A3)");
            Button fireButton = new Button("Tirer");
            fireButton.setOnAction(e -> handleFireButtonClick());
            inputBox.getChildren().addAll(targetInput, fireButton);


            // Crée un label pour afficher les distances de Manhattan
            distanceLabel = new Label("       Les Distances de Manhattan ");
            distanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

            distanceTextArea = new TextArea();
            distanceTextArea.setEditable(false);
            distanceTextArea.setWrapText(true); // Permet le retour à la ligne automatique
            distanceTextArea.setPrefSize(285, 150);
            distanceTextArea.setMaxHeight(150);


            // Crée un VBox pour les deux éléments (distanceLabel et distanceTextArea)
            VBox distanceBox = new VBox(10);
            distanceBox.setTranslateY(200);
            distanceBox.getChildren().addAll(distanceLabel, distanceTextArea);

            // Ajoute le VBox à la zone droite (right) du BorderPane
            root.setRight(distanceBox);


            // Ajoute le HBox à la zone inférieure (bottom) du BorderPane
            root.setBottom(inputBox);
            inputBox.setTranslateX(565);
            inputBox.setTranslateY(-45);



            Scene scene = new Scene(root, 1350, 685);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String args){
        launch(args);
    }

    private int[] showCustomGridDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Grille personnalisée");
        dialog.setHeaderText("Veuillez saisir les dimensions de la grille personnalisée.");
        dialog.setContentText("Nombre de colonnes (entre 6 et 26) :");
        Optional<String> columnsResult = dialog.showAndWait();

        int[] dimensions = new int[2];

        columnsResult.ifPresent(columns -> {
            try {
                int columnsValue = Integer.parseInt(columns);
                if (columnsValue >= 6 && columnsValue <= 26) {
                    dimensions[0] = columnsValue;

                    // Demande le nombre de lignes
                    TextInputDialog rowDialog = new TextInputDialog();
                    rowDialog.setTitle("Grille personnalisée");
                    rowDialog.setHeaderText(null);
                    rowDialog.setContentText("Nombre de lignes (entre 6 et 26) :");
                    Optional<String> rowsResult = rowDialog.showAndWait();
                    rowsResult.ifPresent(rows -> {
                        try {
                            int rowsValue = Integer.parseInt(rows);
                            if (rowsValue >= 6 && rowsValue <= 26) {
                                dimensions[1] = rowsValue;
                            } else {
                                // Les lignes sont incorrectes, affiche à nouveau la boîte de dialogue
                                showCustomGridDialog();
                            }
                        } catch (NumberFormatException e) {
                            // Erreur de saisie pour les lignes, affiche à nouveau la boîte de dialogue
                            showCustomGridDialog();
                        }
                    });
                } else {
                    // Les colonnes sont incorrectes, affiche à nouveau la boîte de dialogue
                    showCustomGridDialog();
                }
            } catch (NumberFormatException e) {
                // Erreur de saisie pour les colonnes, affiche à nouveau la boîte de dialogue
                showCustomGridDialog();
            }
        });

        return dimensions;
    }
    private void handleBackButtonClick() throws Exception {
        BattleShipApp startMenu = new BattleShipApp();
        startMenu.start(new Stage());
        backButton.getScene().getWindow().hide();
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

    public void activateCheatMode(){
        isCheat = !isCheat;
        combatZone.setTricheMode(isCheat);
    }


}


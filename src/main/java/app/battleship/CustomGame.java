package app.battleship;

import classes.BattleShipGameController;
import classes.CombatZone;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


public class CustomGame extends Application implements IGame {
    private BorderPane root;
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

    public CustomGame(BattleShipGameController gameController){
        this.gameController = gameController;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Bataille Navale");


        // Génération de la grille personnalisée et affichage de la zone de combat

        gameController.placeShipsRandomly();
        combatZone = new CombatZone(gameController.getGrid());
        gameController.setGridRectangles(combatZone.getGridRectangles());
        zone = combatZone.getZone();
        root = new BorderPane(zone);
        root.setStyle("-fx-background-color: #333333;");

        // Bouton retour
        Image backButtonImage = new Image(Objects.requireNonNull(getClass().getResource("image/back_button.png")).openStream());
        backButtonImageView = new ImageView(backButtonImage);
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
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
        BorderPane.setMargin(backButton, new Insets(10));
        root.setLeft(backButton);

        // Bouton du mode triche
        cheatModeButton = new Button("Mode Triche");
        cheatModeButton.setOnAction(e -> activateCheatMode());
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
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(targetInput, fireButton);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        resultLabel = new Label("Résultat du tire");
        resultLabel.setStyle("-fx-text-fill: #FFFFFF;");
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(inputBox, resultLabel);
        root.setBottom(vBox);



        //Affiche les distances de Manhattan
        distanceLabel = new Label("       Les Distances de Manhattan ");
        distanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        distanceTextArea = new TextArea();
        distanceTextArea.setEditable(false);
        distanceTextArea.setWrapText(true);
        distanceTextArea.setPrefSize(285, 150);
        distanceTextArea.setMaxHeight(150);
        VBox distanceBox = new VBox(10);
        distanceBox.setTranslateY(200);
        distanceBox.getChildren().addAll(distanceLabel, distanceTextArea);
        root.setRight(distanceBox);



        Scene scene = new Scene(root, 1280, 685);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String args){
        launch(args);
    }


    public void handleBackButtonClick() throws Exception {
        CustomWindow customWindow = new CustomWindow();
        customWindow.start(new Stage());
        backButton.getScene().getWindow().hide();
    }

    public void handleFireButtonClick() {
        String targetPosition = targetInput.getText().trim().toUpperCase();
        if (gameController.isValidTargetPosition(targetPosition)) {

            String distanceResult = gameController.getDistanceFromShips(targetPosition);

            //Procède au tir
            String result = gameController.fireAtTargetPosition(targetPosition);
            resultLabel.setText(result);

            distanceTextArea.setText(distanceResult);
            endOfGame(gameController.checkAllShipsSunk());
        } else {
            resultLabel.setText("Position cible invalide !");
        }
    }

    public void endOfGame(boolean allShipsSunk){
        if(allShipsSunk) {
            EndGame endGame = new EndGame(gameController, this);
            endGame.start(new Stage());
        }
    }

    public void restartGame() {
        // Réinitialiser le contrôleur du jeu
        gameController.restartGame();
        gameController.placeShipsRandomly();
        isCheat = false;

        // Réinitialiser la zone de combat
        combatZone = new CombatZone(gameController.getGrid());
        gameController.setGridRectangles(combatZone.getGridRectangles());
        zone.getChildren().clear();
        zone.getChildren().add(combatZone.getZone());

        resultLabel.setText("");
        distanceTextArea.setText("");
    }

    public void activateCheatMode(){
        isCheat = !isCheat;
        combatZone.setTricheMode(isCheat);
    }


}


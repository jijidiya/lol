package app.battleship;

import classes.BattleShipGameController;
import classes.BattleShipAI;
import classes.CombatZone;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class AIGame extends Application implements IGame{
    private BorderPane root;
    private Group zone;
    private BattleShipGameController gameController;
    private BattleShipAI aIPlayer;
    private CombatZone combatZone;
    private TextArea distanceTextArea;
    private Label resultLabel;
    private Label distanceLabel;

    private ImageView backButtonImageView;
    private Button backButton;
    private Button cheatModeButton;
    private boolean isCheat;
    @Override
    public void start(Stage fourthStage) throws Exception {
        fourthStage.setTitle("Normal Game");

        gameController = new BattleShipGameController();
        gameController.placeShipsRandomly();
        combatZone = new CombatZone(gameController.getGrid());
        zone = combatZone.getZone();
        gameController.setGridRectangles(combatZone.getGridRectangles());
        aIPlayer = new BattleShipAI(gameController);
        zone.setTranslateX(100);
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

        //Zone de tire
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));
        resultLabel = new Label("Résultat du tire");
        resultLabel.setStyle("-fx-text-fill: #FFFFFF;");
        Button fireButton = new Button("Tirer");
        Button destroyAllShips = new Button("Couler la flotte");
        destroyAllShips.setOnAction(e -> handleDestroyFleetClick());
        fireButton.setOnAction(e -> handleFireButtonClick());
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(fireButton,destroyAllShips, resultLabel);


        // Distances de manhattan
        distanceLabel = new Label("       Les Distances de Manhattan ");
        distanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        distanceTextArea = new TextArea();
        distanceTextArea.setEditable(false);
        distanceTextArea.setWrapText(true);
        distanceTextArea.setPrefSize(285, 180);
        distanceTextArea.setMaxHeight(150);


        // VBox pour (distanceLabel et distanceTextArea)
        VBox distanceBox = new VBox(10);
        distanceBox.setTranslateY(200);
        distanceBox.getChildren().addAll(distanceLabel, distanceTextArea);
        root.setRight(distanceBox);
        vBox.setTranslateY(-35);
        root.setBottom(vBox);



        Scene scene = new Scene(root, 1280, 685);
        fourthStage.setScene(scene);
        fourthStage.show();

    }

    public void handleBackButtonClick() throws Exception {
        BattleShipApp startMenu = new BattleShipApp();
        startMenu.start(new Stage());
        backButton.getScene().getWindow().hide();
    }
    @Override
    public void handleFireButtonClick() {
        // l'IA choisit un emplacement de tir.
        String targetPosition = aIPlayer.chooseTarget();

        // Affiche les distances de Manhattan dans distanceTextArea
        String distanceResult = gameController.getDistanceFromShips(targetPosition);
        distanceTextArea.setText(distanceResult);

        //Procède au tir
        String result = gameController.fireAtTargetPosition(targetPosition);
        aIPlayer.rememberShotResult(targetPosition, result);
        aIPlayer.optimizeShots(targetPosition, gameController.manhattanDistance(targetPosition));

        // Afficher le résultat du tir dans le label
        resultLabel.setText(result);


        // Vérifier si tous les bateaux ont été coulés
        endOfGame(gameController.checkAllShipsSunk());
    }
    public void handleDestroyFleetClick(){
        //Methode dont le but est de tirer jusqu'à ce que tous les bateaux soient coulés.
        while(!gameController.checkAllShipsSunk()){
            handleFireButtonClick();
        }
    }

    public void endOfGame(boolean allShipsSunk){
        if(allShipsSunk) {
            EndGame endGame = new EndGame(gameController, this);
            endGame.start(new Stage());
        }
    }

    public void restartGame() {
        // Réinitialise le contrôleur du jeu
        gameController.restartGame();
        gameController.placeShipsRandomly();

        isCheat = false;

        // Réinitialise la zone de combat
        combatZone = new CombatZone(gameController.getGrid());
        gameController.setGridRectangles(combatZone.getGridRectangles());
        zone.getChildren().clear();
        zone.getChildren().add(combatZone.getZone());

        // Réinitialise l'IA.
        aIPlayer = new BattleShipAI(gameController);

        // Effacer les résultats précédents et les distances de Manhattan
        resultLabel.setText("");
        distanceTextArea.setText("");
    }

    public void activateCheatMode(){
        isCheat = !isCheat;
        combatZone.setTricheMode(isCheat);
    }

    public static void main(String[] args){
        launch(args);
    }
}


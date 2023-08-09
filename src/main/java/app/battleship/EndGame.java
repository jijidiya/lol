package app.battleship;

import classes.BattleShipGameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class EndGame extends Application {
    private Stage endGameStage;
    private final BattleShipGameController gameController;
    private final Application previousStage;


    public EndGame(BattleShipGameController gameController, Application previousStage) {
        this.gameController = gameController;
        this.previousStage = previousStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.endGameStage = primaryStage;
        this.endGameStage.setTitle("Fin du Jeu");


        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #333333;");

        Label scoreLabel = new Label("Score : " + gameController.getScore());
        scoreLabel.setStyle("-fx-text-fill: #FFFFFF;");

        Button restartButton = new Button("Recommencer");
        Button quitButton = new Button("Quitter");
        Button mainMenuButton = new Button("Retour au Menu Principal");

        // Définir la taille préférée des boutons pour les uniformiser
        double buttonWidth = 150;
        double buttonHeight = 30;
        restartButton.setPrefSize(buttonWidth, buttonHeight);
        quitButton.setPrefSize(buttonWidth, buttonHeight);
        mainMenuButton.setPrefSize(buttonWidth, buttonHeight);

        restartButton.setStyle("-fx-background-color: #FF5733;");
        quitButton.setStyle("-fx-background-color: #FF5733;");
        mainMenuButton.setStyle("-fx-background-color: #FF5733;");

        // Actions des boutons
        restartButton.setOnAction(e -> {
            try {
                handleRestartButtonClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        quitButton.setOnAction(e -> System.exit(0));
        mainMenuButton.setOnAction(e -> mainMenu());

        vbox.getChildren().addAll(scoreLabel, restartButton, mainMenuButton, quitButton);

        Scene scene = new Scene(vbox, 600, 600);
        this.endGameStage.setScene(scene);
        this.endGameStage.show();
    }

    private void handleRestartButtonClick() throws IOException {
        // Appeler la méthode restartGame() du stage précédent pour redémarrer le jeu
        if (previousStage instanceof NormalGame) {
            ((NormalGame) previousStage).restartGame();
        } else if (previousStage instanceof CustomGame) {
            ((CustomGame) previousStage).restartGame();
        } else if (previousStage instanceof AIGame){
            ((AIGame) previousStage).restartGame();
        } else if (previousStage instanceof LoadGame) {
            ((LoadGame) previousStage).restartGame();
        }

        // Fermer la fenêtre de fin de jeu
        endGameStage.close();
    }

    private void mainMenu(){
        BattleShipApp mainMenu = new BattleShipApp();
        try {
            mainMenu.start(new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.endGameStage.close();
    }
}


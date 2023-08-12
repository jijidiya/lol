package app.battleship;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoiceLevelController {
    @FXML
    private Button level1;
    @FXML
    private Button level2;
    @FXML
    private Button level3;
    @FXML
    private Button level4;
    @FXML
    private Button backButton;

    private String filePath;


    @FXML
    private void startGame() throws IOException {
        LoadGame loadGame = new LoadGame(filePath);
        loadGame.start(new Stage());
    }

    @FXML
    private void choiceLevel1() throws IOException {
        filePath = "src/main/resources/app/battleship/Parties/level1.txt";
        startGame();
        level1.getScene().getWindow().hide();
    }
    @FXML
    private void choiceLevel2() throws IOException {
        filePath = "src/main/resources/app/battleship/Parties/level2.txt";
        startGame();
        level2.getScene().getWindow().hide();
    }

    @FXML
    private void choiceLevel3() throws IOException {
        filePath = "src/main/resources/app/battleship/Parties/level3.txt";
        startGame();
        level3.getScene().getWindow().hide();
    }

    @FXML
    private void choiceLevel4() throws IOException {
        filePath = "src/main/resources/app/battleship/Parties/level4.txt";
        startGame();
        level4.getScene().getWindow().hide();
    }
    @FXML
    public void backStartMenu() throws Exception {
        BattleShipApp startMenu = new BattleShipApp();
        startMenu.start(new Stage());
        backButton.getScene().getWindow().hide();
    }
}

package app.battleship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


import javafx.stage.Stage;

import java.io.IOException;


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




    @FXML
    private void startNewGame() throws Exception {
        NormalGame normalGame = new NormalGame();
        normalGame.start(new Stage());
        newGameButton.getScene().getWindow().hide();
    }
    @FXML
    private void startNewCustomGame() throws Exception {
        CustomWindow customWindow = new CustomWindow();
        customWindow.start(new Stage());
        newCustomGame.getScene().getWindow().hide();


    }



    @FXML
    private void loadGame() throws IOException {
        ChoiceLoadGame loadGame = new ChoiceLoadGame();
        loadGame.start(new Stage());
        newGameButton.getScene().getWindow().hide();
    }
    @FXML
    private void startNewAIGame() throws Exception {
        AIGame aIGame = new AIGame();
        aIGame.start(new Stage());
        newGameButton.getScene().getWindow().hide();

    }

    @FXML
    private void exit(ActionEvent e) {
        System.exit(0);
    }
}

package app.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class CustomGame extends Application {
    @Override
    public void start(Stage secondStage) throws IOException {
        secondStage.setTitle("Custom Game");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/custom_game.fxml")));

        Scene scene = new Scene(root, 1350, 685);
        secondStage.setScene(scene);
        secondStage.show();

    }
    public static void main(String args){
        launch(args);
    }

}


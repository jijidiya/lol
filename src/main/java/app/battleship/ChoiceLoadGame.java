package app.battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class ChoiceLoadGame extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Bataille Navale");
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/choice_level.fxml")));

        Scene scene = new Scene(root, 1350, 685);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}


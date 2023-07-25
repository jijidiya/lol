package app.battleship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomGameDialog {
    @FXML
    private TextField columnsTextField;
    @FXML
    private TextField rowsTextField;
    @FXML
    private Button startButton;

    private Stage stage;
    private int customWidth;
    private int customHeight;

    public CustomGameDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("custom_game_dialog.fxml"));
            fxmlLoader.setController(this);
            VBox root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nouvelle Partie Personnalisée");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStartButtonClicked() {
        try {
            customWidth = Integer.parseInt(columnsTextField.getText());
            customHeight = Integer.parseInt(rowsTextField.getText());
            stage.close();
        } catch (NumberFormatException e) {
            // Afficher un message d'erreur si les dimensions saisies ne sont pas des nombres valides
            System.out.println("Veuillez saisir des nombres valides pour les colonnes et les lignes.");
        }
    }

    public int getCustomWidth() {
        return customWidth;
    }

    public int getCustomHeight() {
        return customHeight;
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}

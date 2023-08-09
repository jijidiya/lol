package app.battleship;

import classes.BattleShipGameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Grille Personnalisée");

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Label columnLabel = new Label("Nombre de colonnes (entre 6 et 26) :");
        TextField columnInput = new TextField();
        columnInput.setPromptText("Entrez le nombre de colonnes");

        Label rowLabel = new Label("Nombre de lignes (entre 6 et 26) :");
        TextField rowInput = new TextField();
        rowInput.setPromptText("Entrez le nombre de lignes");

        Label numShipsLabel = new Label("Nombre de bateaux :");
        TextField numShipsInput = new TextField();
        numShipsInput.setPromptText("Entrez le nombre de bateaux");

        Label shipSizesLabel = new Label("Tailles des bateaux (séparées par une virgule) :");
        TextField shipSizesInput = new TextField();
        shipSizesInput.setPromptText("Entrez les tailles des bateaux");

        Button startButton = new Button("Commencer la partie");

        startButton.setOnAction(e -> {
            try{
                int columns = Integer.parseInt(columnInput.getText());
                int rows = Integer.parseInt(rowInput.getText());
                int numShips = Integer.parseInt(numShipsInput.getText());
                String[] sizes = shipSizesInput.getText().split(",");

                if (numShips != sizes.length) {
                    displayErrorMessage("Le nombre de tailles de bateaux doit correspondre au nombre de bateaux saisi.");
                    return;
                }

                int[] shipSizes = new int[numShips];
                int sumSizes = 0;
                for (int i = 0; i < numShips; i++) {
                    int size = Integer.parseInt(sizes[i].trim());
                    if (size < 1 || size > 6) {
                        displayErrorMessage("La taille des bateaux doit être comprise entre 1 et 6.");
                        return;
                    }
                    sumSizes += size;
                    shipSizes[i] = size;
                }
                if(sumSizes > (rows*columns)/2){
                    displayErrorMessage("Vous avez ajouter trop de pieces ! \nVeuillez a ce que la somme des tailles" +
                            "\n de bateaux valent moins de " +(rows*columns)/2);
                    return;
                }


                if (columns >= 6 && columns <= 26 && rows >= 6 && rows <= 26) {
                    BattleShipGameController gameController = new BattleShipGameController(columns, rows, shipSizes);
                    CustomGame customGame = new CustomGame(gameController);
                    try {
                        customGame.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    primaryStage.close();
                } else {
                    // Afficher un message d'erreur si les dimensions ne sont pas valides
                    displayErrorMessage("Les dimensions de la grille doivent être comprises entre 6 et 26.");
                }
            }catch (NumberFormatException ex){
                displayErrorMessage("Veuillez remplir tous les champs.");
            }

        });

        vbox.getChildren().addAll(columnLabel, columnInput, rowLabel, rowInput, numShipsLabel, numShipsInput,
                shipSizesLabel, shipSizesInput, startButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayErrorMessage(String message) {
        Stage errorStage = new Stage();
        errorStage.setTitle("Erreur");
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        Label errorMessage = new Label(message);
        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> errorStage.close());
        vbox.getChildren().addAll(errorMessage, closeButton);
        Scene scene = new Scene(vbox, 400, 150);
        errorStage.setScene(scene);
        errorStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

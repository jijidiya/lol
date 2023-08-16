package app.battleship;

import classes.BattleShipGameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class CustomWindow extends Application {
    private Button backButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Grille Personnalisée");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #333333;");

        Image backButtonImage = new Image(Objects.requireNonNull(getClass().getResource("image/back_button.png")).openStream());
        ImageView backButtonImageView = new ImageView(backButtonImage);

        backButton = new Button();
        backButton.setGraphic(backButtonImageView);
        backButton.setOnAction(e -> {
            try {
                handleBackButtonClick();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        vbox.setStyle("-fx-background-color: #EAEAEA;");
        vbox.setMinSize(300, 450);
        vbox.setMaxSize(300, 450);
        vbox.setPrefSize(300, 450);

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

                if(numShips > 21){
                    displayErrorMessage("Le nombre de bateau est limité à 21 :'-(");
                    return;
                }

                if (numShips != sizes.length) {
                    displayErrorMessage("Le nombre de tailles de bateaux doit correspondre\n au nombre de bateaux saisi.");
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
                if(sumSizes >= (rows*columns/2)){
                    // cela permet d'éviter les bugs dans placeShipRandomly

                    displayErrorMessage("Vous avez ajouter trop de bateaux trop grand ! " +
                            "\nVeuillez a ce que la somme des tailles" +
                            "\n des bateaux valent moins de " +(rows*columns)/2);
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
                    // Affiche un message d'erreur si les dimensions ne sont pas valides
                    displayErrorMessage("Les dimensions de la grille doivent être comprises entre 6 et 26.");
                }
            }catch (NumberFormatException ex){
                // Affiche un message d'erreur si le joueur ne remplit pas tous les champs
                displayErrorMessage("Veuillez remplir tous les champs.");
            }

        });

        vbox.getChildren().addAll(columnLabel, columnInput, rowLabel, rowInput, numShipsLabel, numShipsInput,
                shipSizesLabel, shipSizesInput, startButton);

        //Placement du Bouton retour
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);
        BorderPane.setMargin(backButton, new Insets(10));
        backButton.setStyle("-fx-background-color: #333333;");
        root.setLeft(backButton);



        root.setCenter(vbox);

        Scene scene = new Scene(root, 1280, 685);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayErrorMessage(String message) {
        // message d'alerte quand le joueur ne remplit pas le formulaire
        // comme souhaiter.
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
    public void handleBackButtonClick() throws Exception {
        BattleShipApp startMenu = new BattleShipApp();
        startMenu.start(new Stage());
        backButton.getScene().getWindow().hide();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

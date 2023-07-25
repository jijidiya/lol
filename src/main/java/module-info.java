module app.battleship {
    requires javafx.controls;
    requires javafx.fxml;


    opens app.battleship to javafx.fxml;
    exports app.battleship;
}
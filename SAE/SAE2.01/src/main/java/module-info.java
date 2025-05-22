module salut {
    requires javafx.controls;
    requires javafx.fxml;

    opens salut to javafx.fxml;
    exports salut;
}

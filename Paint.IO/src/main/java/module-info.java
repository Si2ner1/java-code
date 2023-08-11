module paintio {
    requires javafx.controls;
    requires javafx.fxml;
    opens mareza to javafx.fxml;
    exports mareza;
}
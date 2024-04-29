module javafx.wcp.wcp1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens javafx.wcp.wcp1 to javafx.fxml;
    exports javafx.wcp.wcp1;
}

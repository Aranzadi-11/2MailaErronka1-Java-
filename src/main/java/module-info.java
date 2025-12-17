module com.example.erronkon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens erronkon to javafx.fxml, javafx.graphics;
    exports erronkon;
}


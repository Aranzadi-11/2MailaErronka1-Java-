module erronkon {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires org.json;
    requires java.sql;

    exports erronkon.ui;

    opens erronkon.controller to javafx.fxml;

    opens erronkon.model to com.google.gson, javafx.base;
    opens erronkon.dao to com.google.gson;
    opens erronkon.service to com.google.gson;
    opens erronkon.client to com.google.gson;
    opens erronkon.session to com.google.gson;

}

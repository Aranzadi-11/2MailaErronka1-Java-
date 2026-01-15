module erronkon {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // HTTP client
    requires java.net.http;

    // Librerías externas
    requires org.json;
    requires com.google.gson;

    // Abrir paquetes a JavaFX para FXML
    opens erronkon to javafx.fxml, com.google.gson;

    // Abrir paquetes a Gson para reflexión

    // Exportar el paquete principal
    exports erronkon;
}

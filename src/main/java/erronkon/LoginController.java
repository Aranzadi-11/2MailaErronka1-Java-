package erronkon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    @FXML
    private TextField erabiltzaileaField;

    @FXML
    private PasswordField pasahitzaField;

    @FXML
    private Label infoLabel;

    private int saiakerak = 3;

    private static final String API_URL =
            "https://localhost:7236/api/Langileak/Login";

    @FXML
    private void sartu() {

        String erabiltzailea = erabiltzaileaField.getText().trim();
        String pasahitza = pasahitzaField.getText().trim();

        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            infoLabel.setStyle("-fx-text-fill: red;");
            infoLabel.setText("Mesedez, sartu erabiltzailea eta pasahitza.");
            return;
        }

        try {
            // JSON para la API
            JSONObject json = new JSONObject();
            json.put("erabiltzailea", erabiltzailea);
            json.put("pasahitza", pasahitza);

            // Cliente HTTP común (SSL ignorado)
            HttpClient client = ApiClient.getClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200) {

                JSONObject respJson = new JSONObject(response.body());

                int rolaId = respJson.getInt("rolaId");

                // SOLO admin (3) o jefe (4)
                if (rolaId == 3 || rolaId == 4) {

                    // Guardamos sesión
                    Session.setId(respJson.getInt("id"));
                    Session.setIzena(respJson.getString("izena"));
                    Session.setErabiltzailea(respJson.getString("erabiltzailea"));
                    Session.setRolaId(rolaId);

                    infoLabel.setStyle("-fx-text-fill: green;");
                    infoLabel.setText("Login zuzena!");

                    // Abrimos menú principal
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/erronkon/menu.fxml")
                    );
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.setTitle("AB EJ - Kudeaketa Sistema");
                    stage.setScene(new Scene(root));
                    stage.show();

                    // Cerramos login
                    erabiltzaileaField.getScene().getWindow().hide();

                } else {
                    infoLabel.setStyle("-fx-text-fill: red;");
                    infoLabel.setText("Ez duzu baimenik. Adminarekin hitz egin.");
                }

            } else {
                saiakerak--;
                infoLabel.setStyle("-fx-text-fill: red;");
                infoLabel.setText("Datu okerrak. Saiakerak: " + saiakerak);

                if (saiakerak <= 0) {
                    infoLabel.setText("Saiakera gehiegi. Itxiko da...");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            infoLabel.setStyle("-fx-text-fill: red;");
            infoLabel.setText("Errorea zerbitzariarekin.");
            e.printStackTrace();
        }
    }
}

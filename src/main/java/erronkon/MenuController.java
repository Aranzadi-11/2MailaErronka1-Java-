package erronkon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Label erabiltzaileaLabel;

    @FXML
    private Label rolaLabel;

    public void setUserData(String erabiltzailea, String rola) {
        erabiltzaileaLabel.setText("Erabiltzailea: " + erabiltzailea);
        rolaLabel.setText("Rola: " + rola);
    }

    @FXML
    private void kudeatuLangileak() throws IOException {
        irekiLeihoa("/erronkon/langileak.fxml", "Langileen Kudeaketa", true);
    }

    @FXML
    private void kudeatuPlaterak() throws IOException {
        irekiLeihoa("/erronkon/platerak.fxml", "Plateren Kudeaketa", true);
    }

    @FXML
    private void kudeatuFinantzak() throws IOException {
        irekiLeihoa("/erronkon/finantzak.fxml", "Finantzak Kudeaketa", true);
    }

    @FXML
    private void kudeatuStock() throws IOException {
        irekiLeihoa("/erronkon/stock.fxml", "Stock-a Kudeaketa", true);
    }

    private void irekiLeihoa(String fxmlPath, String titulua, boolean maximized) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(titulua);
        stage.setScene(new Scene(root));

        if (maximized) {
            stage.setMaximized(true); // Ventana maximizada (casi pantalla completa)
        }

        stage.show();
    }
}
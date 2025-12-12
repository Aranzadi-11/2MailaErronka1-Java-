package erronkon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField erabiltzaileaField;

    @FXML
    private PasswordField pasahitzaField;

    @FXML
    private Label infoLabel;

    private int saioak = 3;

    @FXML
    private void sartu() {
        String erabiltzailea = erabiltzaileaField.getText();
        String pasahitza = pasahitzaField.getText();

        try (Connection konexioa = DBKonexioa.lortuKonexioa()) {

            if (konexioa == null) {
                infoLabel.setText("Ezin da DB-ra konektatu.");
                return;
            }

            String sql = "SELECT rola_id FROM langileak WHERE erabiltzailea = ? AND pasahitza = ?";
            PreparedStatement ps = konexioa.prepareStatement(sql);
            ps.setString(1, erabiltzailea);
            ps.setString(2, pasahitza);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int rola = rs.getInt("rola_id");

                if (rola == 3 || rola == 4) {
                    infoLabel.setStyle("-fx-text-fill: green;");
                    infoLabel.setText("Login zuzena!");


                    erabiltzaileaField.getScene().getWindow().hide();

                } else {
                    infoLabel.setText("Ez duzu baimenik. Adminarekin hitz egin.");
                }

            } else {
                saioak--;
                infoLabel.setText("Datu okerrak. Saioak: " + saioak);

                if (saioak <= 0) {
                    infoLabel.setText("Saio gehiegi. Itxiko da...");
                    System.exit(0);
                }
            }

        } catch (SQLException e) {
            infoLabel.setText("Errorea: " + e.getMessage());
        }
}
}
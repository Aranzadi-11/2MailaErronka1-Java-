package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class LangileaFormController {

    @FXML private TextField txtIzena;
    @FXML private TextField txtErabiltzailea;
    @FXML private PasswordField txtPasahitza;
    @FXML private ComboBox<String> cbAktibo;
    @FXML private ComboBox<String> cbRola;
    @FXML private Button btnGehitu;
    @FXML private Button btnEguneratu;
    @FXML private Button btnEzabatu;
    @FXML private TableView<Langilea> table;

    private ObservableList<Langilea> datuak;
    private Gson gson = new Gson();

    @FXML
    private void initialize() {
        // Combo box para activo
        cbAktibo.setItems(FXCollections.observableArrayList("Bai", "Ez"));
        // Combo box para rol
        cbRola.setItems(FXCollections.observableArrayList("1 - sukaldaria", "2 - zerbitzaria", "3 - admin", "4 - jefea"));

        kargatuLangileak();
    }

    // Método público para actualizar la tabla
    public void kargatuLangileak() {
        try {
            HttpResponse<String> response = ApiService.get(LangileaDAO.BASE_URL);
            String json = response.body();

            Type listType = new TypeToken<List<Langilea>>() {}.getType();
            List<Langilea> lista = gson.fromJson(json, listType);

            datuak = FXCollections.observableArrayList(lista);
            table.setItems(datuak);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errorea datuak kargatzerakoan.");
            alert.showAndWait();
        }
    }

    @FXML
    private void gehituLangilea() {
        try {
            Langilea l = new Langilea();
            l.setIzena(txtIzena.getText());
            l.setErabiltzailea(txtErabiltzailea.getText());
            l.setPasahitza(txtPasahitza.getText());
            l.setAktibo(cbAktibo.getValue());
            l.setRolaId(extractRolaId(cbRola.getValue()));

            HttpResponse<String> response = ApiService.post(LangileaDAO.BASE_URL, gson.toJson(l));
            String result = response.body();

            kargatuLangileak(); // refresca la tabla

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errorea langilea gehitzerakoan.");
            alert.showAndWait();
        }
    }

    @FXML
    private void eguneratuLangilea() {
        Langilea l = table.getSelectionModel().getSelectedItem();
        if (l == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ez da langilerik aukeratu.");
            alert.showAndWait();
            return;
        }

        try {
            l.setIzena(txtIzena.getText());
            l.setErabiltzailea(txtErabiltzailea.getText());
            l.setPasahitza(txtPasahitza.getText());
            l.setAktibo(cbAktibo.getValue());
            l.setRolaId(extractRolaId(cbRola.getValue()));

            String url = LangileaDAO.BASE_URL + "/" + l.getId();
            HttpResponse<String> response = ApiService.put(url, gson.toJson(l));
            String result = response.body();

            kargatuLangileak();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errorea langilea eguneratzerakoan.");
            alert.showAndWait();
        }
    }

    @FXML
    private void ezabatuLangilea() {
        Langilea l = table.getSelectionModel().getSelectedItem();
        if (l == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ez da langilerik aukeratu.");
            alert.showAndWait();
            return;
        }

        try {
            String url = LangileaDAO.BASE_URL + "/" + l.getId();
            HttpResponse<String> response = ApiService.delete(url);
            String result = response.body();

            kargatuLangileak();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errorea langilea ezabatzeko.");
            alert.showAndWait();
        }
    }

    // Método auxiliar para extraer el ID de rol del string del ComboBox
    private int extractRolaId(String rolString) {
        if (rolString == null || rolString.isEmpty()) return 1;
        return Integer.parseInt(rolString.split(" ")[0]);
    }
}

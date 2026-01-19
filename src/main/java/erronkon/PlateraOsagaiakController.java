package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlateraOsagaiakController {

    @FXML private TableView<PlateraOsagaia> tableView;
    @FXML private TableColumn<PlateraOsagaia, String> plateraColumn;
    @FXML private TableColumn<PlateraOsagaia, String> osagaiaColumn;
    @FXML private TableColumn<PlateraOsagaia, Integer> kantitateaColumn;
    @FXML private TableColumn<PlateraOsagaia, String> neurriaColumn;

    @FXML private ComboBox<String> plateraComboBox;
    @FXML private ComboBox<String> osagaiaComboBox;
    @FXML private TextField kantitateaField;
    @FXML private ComboBox<String> neurriaComboBox;

    @FXML private Button gehituBtn;
    @FXML private Button editatuBtn;
    @FXML private Button ezabatuBtn;
    @FXML private Button garbituBtn;

    private ObservableList<PlateraOsagaia> plateraOsagaiakList;

    private Map<Integer, String> platerakMap = new HashMap<>();
    private Map<String, Integer> plateraNombreToId = new HashMap<>();

    private Map<Integer, String> osagaiakMap = new HashMap<>();
    private Map<String, Integer> osagaiaNombreToId = new HashMap<>();

    private Map<Integer, String> osagaiaNeurriaMap = new HashMap<>();

    @FXML
    public void initialize() {
        plateraColumn.setCellValueFactory(new PropertyValueFactory<>("plateraIzena"));
        osagaiaColumn.setCellValueFactory(new PropertyValueFactory<>("osagaiIzena"));
        kantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        neurriaColumn.setCellValueFactory(new PropertyValueFactory<>("neurria"));

        ObservableList<String> neurriak = FXCollections.observableArrayList(
                "Unitatea", "Kilogramo", "Litro", "Gramo", "Mililitro", "Taza", "Cucharada"
        );
        neurriaComboBox.setItems(neurriak);

        kargatuDatuak();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                kargatuFormularioa(newSelection);
            }
        });

        osagaiaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Integer id = osagaiaNombreToId.get(newVal);
                if (id != null && osagaiaNeurriaMap.containsKey(id)) {
                    neurriaComboBox.setValue(osagaiaNeurriaMap.get(id));
                }
            }
        });
    }

    private void kargatuDatuak() {
        platerakMap.clear();
        plateraNombreToId.clear();
        osagaiakMap.clear();
        osagaiaNombreToId.clear();
        osagaiaNeurriaMap.clear();

        kargatuPlaterak();

        kargatuOsagaiak();

        kargatuRelazioak();

        kargatuComboBox();
    }

    private void kargatuPlaterak() {
        try {
            List<Platera> platerak = PlateraDAO.getAll();
            for (Platera platera : platerak) {
                int id = platera.getId();
                String izena = platera.getIzena();
                if (id > 0 && izena != null && !izena.isEmpty()) {
                    platerakMap.put(id, izena);
                    plateraNombreToId.put(izena, id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Ezin izan dira platerak kargatu: " + e.getMessage());
        }
    }

    private void kargatuOsagaiak() {
        try {
            List<Inbentarioa> osagaiak = InbentarioaDAO.getAll();
            for (Inbentarioa osagaia : osagaiak) {
                int id = osagaia.getId();
                String izena = osagaia.getIzena();
                String neurria = osagaia.getNeurriaUnitatea();

                if (id > 0 && izena != null && !izena.isEmpty()) {
                    osagaiakMap.put(id, izena);
                    osagaiaNombreToId.put(izena, id);

                    if (neurria != null && !neurria.isEmpty()) {
                        osagaiaNeurriaMap.put(id, neurria);
                    } else {
                        osagaiaNeurriaMap.put(id, "Unitatea");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Ezin izan dira osagaiak kargatu: " + e.getMessage());
        }
    }

    private void kargatuRelazioak() {
        plateraOsagaiakList = FXCollections.observableArrayList();

        try {
            String url = "https://localhost:7236/api/PlaterenOsagaiak";
            HttpResponse<String> response = ApiService.get(url);

            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    PlateraOsagaia po = new PlateraOsagaia();
                    po.setPlateraId(obj.optInt("plateraId", 0));
                    po.setInbentarioaId(obj.optInt("inbentarioaId", 0));
                    po.setKantitatea(obj.optInt("kantitatea", 0));

                    int plateraId = po.getPlateraId();
                    int osagaiaId = po.getInbentarioaId();

                    String plateraIzena = platerakMap.getOrDefault(plateraId,
                            "Platera " + plateraId + " (Ez dago)");
                    String osagaiIzena = osagaiakMap.getOrDefault(osagaiaId,
                            "Osagaia " + osagaiaId + " (Ez dago)");
                    String neurria = osagaiaNeurriaMap.getOrDefault(osagaiaId, "Unitatea");

                    po.setPlateraIzena(plateraIzena);
                    po.setOsagaiIzena(osagaiIzena);
                    po.setNeurria(neurria);

                    plateraOsagaiakList.add(po);
                }

                tableView.setItems(plateraOsagaiakList);
            } else {
                alerta("Errorea", "Ezin izan dira erlazioak kargatu. HTTP kodea: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Errorea erlazioak kargatzean: " + e.getMessage());
        }
    }

    private void kargatuComboBox() {
        ObservableList<String> plateraIzenak = FXCollections.observableArrayList(plateraNombreToId.keySet());
        plateraIzenak.sort(String::compareTo);
        plateraComboBox.setItems(plateraIzenak);

        ObservableList<String> osagaiIzenak = FXCollections.observableArrayList(osagaiaNombreToId.keySet());
        osagaiIzenak.sort(String::compareTo);
        osagaiaComboBox.setItems(osagaiIzenak);
    }

    private void kargatuFormularioa(PlateraOsagaia po) {
        if (po == null) return;

        String plateraIzena = po.getPlateraIzena();
        String osagaiIzena = po.getOsagaiIzena();

        plateraComboBox.setValue(plateraIzena != null ? plateraIzena : "");
        osagaiaComboBox.setValue(osagaiIzena != null ? osagaiIzena : "");
        kantitateaField.setText(String.valueOf(po.getKantitatea()));

        String neurria = po.getNeurria();
        if (neurria != null && !neurria.isEmpty()) {
            neurriaComboBox.setValue(neurria);
        }
    }

    @FXML
    private void gehituPlateraOsagaia() {
        String plateraNombre = plateraComboBox.getValue();
        String osagaiaNombre = osagaiaComboBox.getValue();
        String kantitateaStr = kantitateaField.getText().trim();

        if (plateraNombre == null || plateraNombre.isEmpty()) {
            alerta("Errorea", "Hautatu plater bat");
            return;
        }

        if (osagaiaNombre == null || osagaiaNombre.isEmpty()) {
            alerta("Errorea", "Hautatu osagai bat");
            return;
        }

        if (kantitateaStr.isEmpty()) {
            alerta("Errorea", "Sartu kantitatea");
            return;
        }

        try {
            int kantitatea = Integer.parseInt(kantitateaStr);
            if (kantitatea <= 0) {
                alerta("Errorea", "Kantitatea 0 baino handiagoa izan behar da");
                return;
            }

            Integer plateraId = plateraNombreToId.get(plateraNombre);
            Integer osagaiaId = osagaiaNombreToId.get(osagaiaNombre);

            if (plateraId == null || osagaiaId == null) {
                alerta("Errorea", "Ezin izan da platera edo osagaia identifikatu");
                return;
            }

            for (PlateraOsagaia po : plateraOsagaiakList) {
                if (po.getPlateraId() == plateraId && po.getInbentarioaId() == osagaiaId) {
                    alerta("Oharra", "Platera-osagaia erlazioa dagoeneko existitzen da");
                    return;
                }
            }

            JSONObject json = new JSONObject();
            json.put("plateraId", plateraId);
            json.put("inbentarioaId", osagaiaId);
            json.put("kantitatea", kantitatea);

            String url = "https://localhost:7236/api/PlaterenOsagaiak";
            HttpResponse<String> response = ApiService.post(url, json.toString());

            if (response.statusCode() == 201 || response.statusCode() == 200) {
                PlateraOsagaia po = new PlateraOsagaia();
                po.setPlateraId(plateraId);
                po.setInbentarioaId(osagaiaId);
                po.setKantitatea(kantitatea);
                po.setPlateraIzena(plateraNombre);
                po.setOsagaiIzena(osagaiaNombre);
                po.setNeurria(neurriaComboBox.getValue());

                plateraOsagaiakList.add(po);
                tableView.refresh();

                garbituEremuak();
                alerta("Arrakasta", "Platera-osagaia erlazioa ondo gehitu da!");
            } else {
                alerta("Errorea", "Ezin izan da erlazioa gehitu. HTTP kodea: " + response.statusCode());
            }
        } catch (NumberFormatException e) {
            alerta("Errorea", "Kantitatea zenbaki bat izan behar da");
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Errorea gertatu da: " + e.getMessage());
        }
    }

    @FXML
    private void editatuPlateraOsagaia() {
        PlateraOsagaia po = tableView.getSelectionModel().getSelectedItem();
        if (po == null) {
            alerta("Errorea", "Hautatu erlazio bat editatzeko");
            return;
        }

        String plateraNombre = plateraComboBox.getValue();
        String osagaiaNombre = osagaiaComboBox.getValue();
        String kantitateaStr = kantitateaField.getText().trim();

        if (plateraNombre != null && !plateraNombre.equals(po.getPlateraIzena())) {
            Integer nuevoPlateraId = plateraNombreToId.get(plateraNombre);
            if (nuevoPlateraId != null) {
                po.setPlateraId(nuevoPlateraId);
            }
        }

        if (osagaiaNombre != null && !osagaiaNombre.equals(po.getOsagaiIzena())) {
            Integer nuevoOsagaiaId = osagaiaNombreToId.get(osagaiaNombre);
            if (nuevoOsagaiaId != null) {
                po.setInbentarioaId(nuevoOsagaiaId);
                po.setNeurria(neurriaComboBox.getValue());
            }
        }

        if (!kantitateaStr.isEmpty()) {
            try {
                int kantitatea = Integer.parseInt(kantitateaStr);
                po.setKantitatea(kantitatea);
            } catch (NumberFormatException e) {
                alerta("Errorea", "Kantitatea zenbaki bat izan behar da");
                return;
            }
        }

        po.setPlateraIzena(plateraNombre);
        po.setOsagaiIzena(osagaiaNombre);

        try {
            JSONObject json = new JSONObject();
            json.put("plateraId", po.getPlateraId());
            json.put("inbentarioaId", po.getInbentarioaId());
            json.put("kantitatea", po.getKantitatea());

            String url = "https://localhost:7236/api/PlaterenOsagaiak/" +
                    po.getPlateraId() + "/" + po.getInbentarioaId();
            HttpResponse<String> response = ApiService.put(url, json.toString());

            if (response.statusCode() == 200) {
                tableView.refresh();
                alerta("Arrakasta", "Platera-osagaia erlazioa ondo eguneratu da!");
            } else {
                alerta("Errorea", "Ezin izan da erlazioa eguneratu. HTTP kodea: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta("Errorea", "Errorea gertatu da: " + e.getMessage());
        }
    }

    @FXML
    private void ezabatuPlateraOsagaia() {
        PlateraOsagaia po = tableView.getSelectionModel().getSelectedItem();
        if (po == null) {
            alerta("Errorea", "Hautatu erlazio bat ezabatzeko");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Berrespena");
        confirm.setHeaderText(null);
        confirm.setContentText("Ziur zaude '" + po.getPlateraIzena() +
                "' eta '" + po.getOsagaiIzena() +
                "' erlazioa ezabatu nahi duzula?");

        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    String url = "https://localhost:7236/api/PlaterenOsagaiak/" +
                            po.getPlateraId() + "/" + po.getInbentarioaId();
                    HttpResponse<String> responseApi = ApiService.delete(url);

                    if (responseApi.statusCode() == 200 || responseApi.statusCode() == 204) {
                        plateraOsagaiakList.remove(po);
                        garbituEremuak();
                        alerta("Arrakasta", "Platera-osagaia erlazioa ondo ezabatu da!");
                    } else {
                        alerta("Errorea", "Ezin izan da erlazioa ezabatu. HTTP kodea: " +
                                responseApi.statusCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    alerta("Errorea", "Errorea gertatu da: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void garbituEremuak() {
        plateraComboBox.setValue(null);
        osagaiaComboBox.setValue(null);
        kantitateaField.clear();
        neurriaComboBox.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void alerta(String izenburua, String mezua) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(izenburua);
        alert.setHeaderText(null);
        alert.setContentText(mezua);
        alert.showAndWait();
    }
}
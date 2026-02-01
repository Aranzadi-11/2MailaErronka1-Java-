package erronkon.controller;

import erronkon.dao.InbentarioaDAO;
import erronkon.dao.PlateraDAO;
import erronkon.model.Inbentarioa;
import erronkon.model.Platera;
import erronkon.model.PlateraOsagaia;
import erronkon.service.ApiService;
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
    @FXML private ComboBox<String> neurriaComboBox;
    @FXML private TextField kantitateaField;

    @FXML private Button gehituBtn;
    @FXML private Button editatuBtn;
    @FXML private Button ezabatuBtn;
    @FXML private Button garbituBtn;

    private ObservableList<PlateraOsagaia> plateraOsagaiakList = FXCollections.observableArrayList();

    private final Map<Integer, String> platerakMap = new HashMap<>();
    private final Map<String, Integer> plateraIzenaToId = new HashMap<>();

    private final Map<Integer, String> osagaiakMap = new HashMap<>();
    private final Map<String, Integer> osagaiaIzenaToId = new HashMap<>();
    private final Map<Integer, String> osagaiaNeurriaMap = new HashMap<>();

    private int selectedPlateraId;
    private int selectedOsagaiaId;

    @FXML
    public void initialize() {
        plateraColumn.setCellValueFactory(new PropertyValueFactory<>("plateraIzena"));
        osagaiaColumn.setCellValueFactory(new PropertyValueFactory<>("osagaiIzena"));
        kantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        neurriaColumn.setCellValueFactory(new PropertyValueFactory<>("neurria"));

        neurriaComboBox.setItems(FXCollections.observableArrayList(
                "Unitatea", "Kilogramo", "Litro", "Gramo", "Mililitro"
        ));

        kargatuDatuak();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old, po) -> {
            if (po != null) {
                selectedPlateraId = po.getPlateraId();
                selectedOsagaiaId = po.getInbentarioaId();
                kargatuFormularioa(po);
            }
        });

        osagaiaComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, berria) -> {
            if (berria != null) {
                Integer id = osagaiaIzenaToId.get(berria);
                if (id != null) {
                    neurriaComboBox.setValue(osagaiaNeurriaMap.getOrDefault(id, "Unitatea"));
                }
            }
        });
    }

    private void kargatuDatuak() {
        platerakMap.clear();
        plateraIzenaToId.clear();
        osagaiakMap.clear();
        osagaiaIzenaToId.clear();
        osagaiaNeurriaMap.clear();

        kargatuPlaterak();
        kargatuOsagaiak();
        kargatuRelazioak();
        kargatuComboBoxak();
    }

    private void kargatuPlaterak() {
        try {
            List<Platera> platerak = PlateraDAO.getAll();
            for (Platera p : platerak) {
                platerakMap.put(p.getId(), p.getIzena());
                plateraIzenaToId.put(p.getIzena(), p.getId());
            }
        } catch (Exception e) {
            alerta("Errorea", "Platerak ezin izan dira kargatu");
        }
    }

    private void kargatuOsagaiak() {
        try {
            List<Inbentarioa> osagaiak = InbentarioaDAO.getAll();
            for (Inbentarioa o : osagaiak) {
                osagaiakMap.put(o.getId(), o.getIzena());
                osagaiaIzenaToId.put(o.getIzena(), o.getId());
                osagaiaNeurriaMap.put(
                        o.getId(),
                        o.getNeurriaUnitatea() != null ? o.getNeurriaUnitatea() : "Unitatea"
                );
            }
        } catch (Exception e) {
            alerta("Errorea", "Osagaiak ezin izan dira kargatu");
        }
    }

    private void kargatuRelazioak() {
        plateraOsagaiakList.clear();
        try {
            HttpResponse<String> res = ApiService.get("https://localhost:7236/api/PlaterenOsagaiak");
            JSONArray arr = new JSONArray(res.body());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                PlateraOsagaia po = new PlateraOsagaia();
                po.setPlateraId(obj.getInt("plateraId"));
                po.setInbentarioaId(obj.getInt("inbentarioaId"));
                po.setKantitatea(obj.getInt("kantitatea"));

                po.setPlateraIzena(platerakMap.get(po.getPlateraId()));
                po.setOsagaiIzena(osagaiakMap.get(po.getInbentarioaId()));
                po.setNeurria(osagaiaNeurriaMap.get(po.getInbentarioaId()));

                plateraOsagaiakList.add(po);
            }
            tableView.setItems(plateraOsagaiakList);
        } catch (Exception e) {
            alerta("Errorea", "Erlazioak ezin izan dira kargatu");
        }
    }

    private void kargatuComboBoxak() {
        plateraComboBox.setItems(FXCollections.observableArrayList(plateraIzenaToId.keySet()));
        osagaiaComboBox.setItems(FXCollections.observableArrayList(osagaiaIzenaToId.keySet()));
    }

    private void kargatuFormularioa(PlateraOsagaia po) {
        plateraComboBox.setValue(po.getPlateraIzena());
        osagaiaComboBox.setValue(po.getOsagaiIzena());
        kantitateaField.setText(String.valueOf(po.getKantitatea()));
        neurriaComboBox.setValue(po.getNeurria());
    }

    @FXML
    private void gehituPlateraOsagaia() {
        try {
            Integer pId = plateraIzenaToId.get(plateraComboBox.getValue());
            Integer oId = osagaiaIzenaToId.get(osagaiaComboBox.getValue());
            int kant = Integer.parseInt(kantitateaField.getText());

            JSONObject json = new JSONObject();
            json.put("plateraId", pId);
            json.put("inbentarioaId", oId);
            json.put("kantitatea", kant);

            ApiService.post("https://localhost:7236/api/PlaterenOsagaiak", json.toString());
            kargatuDatuak();
            garbituEremuak();
            alerta("Ondo", "Erregistroa ondo gehitu da");
        } catch (Exception e) {
            alerta("Errorea", "Ezin izan da gehitu");
        }
    }

    @FXML
    private void editatuPlateraOsagaia() {
        PlateraOsagaia po = tableView.getSelectionModel().getSelectedItem();
        if (po == null) {
            alerta("Errorea", "Ez da erregistrorik hautatu");
            return;
        }

        try {
            Integer pIdBerria = plateraIzenaToId.get(plateraComboBox.getValue());
            Integer oIdBerria = osagaiaIzenaToId.get(osagaiaComboBox.getValue());
            int kant = Integer.parseInt(kantitateaField.getText());

            JSONObject json = new JSONObject();
            json.put("plateraId", pIdBerria);
            json.put("inbentarioaId", oIdBerria);
            json.put("kantitatea", kant);

            String url = "https://localhost:7236/api/PlaterenOsagaiak/"
                    + selectedPlateraId + "/" + selectedOsagaiaId;

            HttpResponse<String> res = ApiService.put(url, json.toString());

            if (res.statusCode() == 200) {
                selectedPlateraId = pIdBerria;
                selectedOsagaiaId = oIdBerria;
                kargatuDatuak();
                garbituEremuak();
                alerta("Ondo", "Erregistroa ondo eguneratu da");
            } else {
                alerta("Errorea", "Eguneratzeak huts egin du");
            }
        } catch (Exception e) {
            alerta("Errorea", "Ezin izan da eguneratu");
        }
    }

    @FXML
    private void ezabatuPlateraOsagaia() {
        PlateraOsagaia po = tableView.getSelectionModel().getSelectedItem();
        if (po == null) {
            alerta("Errorea", "Ez da erregistrorik hautatu");
            return;
        }

        try {
            String url = "https://localhost:7236/api/PlaterenOsagaiak/"
                    + po.getPlateraId() + "/" + po.getInbentarioaId();

            ApiService.delete(url);
            kargatuDatuak();
            garbituEremuak();
            alerta("Ondo", "Erregistroa ondo ezabatu da");
        } catch (Exception e) {
            alerta("Errorea", "Ezin izan da ezabatu");
        }
    }

    @FXML
    private void garbituEremuak() {
        plateraComboBox.setValue(null);
        osagaiaComboBox.setValue(null);
        kantitateaField.clear();
        neurriaComboBox.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void alerta(String titulua, String mezua) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulua);
        a.setHeaderText(null);
        a.setContentText(mezua);
        a.showAndWait();
    }
}

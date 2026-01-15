package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonType;

import java.util.List;

public class LangileakController {

    @FXML
    private TableView<Langilea> tableView;

    @FXML
    private TableColumn<Langilea, Integer> idColumn;

    @FXML
    private TableColumn<Langilea, String> izenaColumn;

    @FXML
    private TableColumn<Langilea, String> erabiltzaileaColumn;

    @FXML
    private TableColumn<Langilea, String> aktiboColumn;

    @FXML
    private TableColumn<Langilea, Integer> rolaColumn;

    @FXML
    private TableColumn<Langilea, String> erregistroColumn;

    @FXML
    private TextField izenaField;

    @FXML
    private TextField erabiltzaileaField;

    @FXML
    private TextField pasahitzaField;

    @FXML
    private ComboBox<String> rolaComboBox;

    @FXML
    private ComboBox<String> aktiboComboBox;

    @FXML
    private Button gehituBtn;

    @FXML
    private Button ezabatuBtn;

    @FXML
    private Button editatuBtn;

    @FXML
    private Button garbituBtn;

    private ObservableList<Langilea> langileakList;

    @FXML
    public void initialize() {
        // Configurar las columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        erabiltzaileaColumn.setCellValueFactory(new PropertyValueFactory<>("erabiltzailea"));
        aktiboColumn.setCellValueFactory(new PropertyValueFactory<>("aktibo"));
        rolaColumn.setCellValueFactory(new PropertyValueFactory<>("rolaId"));
        erregistroColumn.setCellValueFactory(new PropertyValueFactory<>("erregistroData"));

        // Cargar opciones para los ComboBox
        cargarComboBoxes();

        // Cargar datos desde la API
        recargarLangileak();

        // Manejar selección de la tabla
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosFormulario(newSelection);
            }
        });

        // Asignar acciones a los botones
        gehituBtn.setOnAction(e -> gehituLangilea());
        ezabatuBtn.setOnAction(e -> ezabatuLangilea());
        editatuBtn.setOnAction(e -> editatuLangilea());
        garbituBtn.setOnAction(e -> garbituEremuak());
    }

    private void cargarComboBoxes() {
        // Opciones para Rola ID
        ObservableList<String> rolak = FXCollections.observableArrayList();
        rolak.add("1 - sukaldaria");
        rolak.add("2 - zerbitzaria");
        rolak.add("3 - admin");
        rolak.add("4 - jefea");
        rolaComboBox.setItems(rolak);

        // Opciones para Aktibo
        ObservableList<String> aktiboak = FXCollections.observableArrayList();
        aktiboak.add("Bai");
        aktiboak.add("Ez");
        aktiboComboBox.setItems(aktiboak);
    }

    private void recargarLangileak() {
        List<Langilea> lista = LangileaDAO.getAll();
        langileakList = FXCollections.observableArrayList(lista);
        tableView.setItems(langileakList);
    }

    private void cargarDatosFormulario(Langilea langilea) {
        izenaField.setText(langilea.getIzena());
        erabiltzaileaField.setText(langilea.getErabiltzailea());
        pasahitzaField.setText(langilea.getPasahitza());

        // Establecer el valor del ComboBox de rol
        int rolaId = langilea.getRolaId();
        String rolaString = rolaId + " - " + getRolaIzena(rolaId);
        rolaComboBox.setValue(rolaString);

        aktiboComboBox.setValue(langilea.getAktibo());
    }

    private String getRolaIzena(int rolaId) {
        switch (rolaId) {
            case 1: return "sukaldaria";
            case 2: return "zerbitzaria";
            case 3: return "admin";
            case 4: return "jefea";
            default: return "ezezaguna";
        }
    }

    @FXML
    private void gehituLangilea() {
        String izena = izenaField.getText().trim();
        String erabiltzailea = erabiltzaileaField.getText().trim();
        String pasahitza = pasahitzaField.getText().trim();
        String rolaString = rolaComboBox.getValue();
        String aktibo = aktiboComboBox.getValue();

        if (izena.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty() || rolaString == null || aktibo == null) {
            alerta("Error", "Datu guztiak bete behar dira");
            return;
        }

        // Obtener el ID del rol del string seleccionado
        int rolaId = Integer.parseInt(rolaString.split(" ")[0]);

        // Crear nuevo langilea
        Langilea l = new Langilea();
        l.setIzena(izena);
        l.setErabiltzailea(erabiltzailea);
        l.setPasahitza(pasahitza);
        l.setAktibo(aktibo);
        l.setErregistroData(java.time.LocalDate.now().toString());
        l.setRolaId(rolaId);

        boolean ok = LangileaDAO.create(l);

        if (ok) {
            recargarLangileak();
            garbituEremuak();
            alerta("Arrakasta", "Langilea ondo gehitu da!");
        } else {
            alerta("Error", "Langilea ezin izan da gehitu");
        }
    }

    @FXML
    private void ezabatuLangilea() {
        Langilea l = tableView.getSelectionModel().getSelectedItem();
        if (l == null) {
            alerta("Error", "Hautatu langilea ezabatzeko");
            return;
        }

        // Confirmar antes de eliminar
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Berrespena");
        confirm.setHeaderText(null);
        confirm.setContentText("Ziur zaude '" + l.getIzena() + "' langilea ezabatu nahi duzula?");

        // Mostrar el diálogo y esperar a que el usuario pulse un botón
        java.util.Optional<ButtonType> result = confirm.showAndWait();

        // Si el usuario pulsa OK (aceptar)
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean ok = LangileaDAO.delete(l.getId());
            if (ok) {
                langileakList.remove(l);
                garbituEremuak();
                alerta("Arrakasta", "Langilea ondo ezabatu da!");
            } else {
                alerta("Error", "Langilea ezin izan da ezabatu");
            }
        }
    }

    @FXML
    private void editatuLangilea() {
        Langilea l = tableView.getSelectionModel().getSelectedItem();
        if (l == null) {
            alerta("Error", "Hautatu langilea editatzeko");
            return;
        }

        String izena = izenaField.getText().trim();
        String erabiltzailea = erabiltzaileaField.getText().trim();
        String pasahitza = pasahitzaField.getText().trim();
        String rolaString = rolaComboBox.getValue();
        String aktibo = aktiboComboBox.getValue();

        if (!izena.isEmpty()) l.setIzena(izena);
        if (!erabiltzailea.isEmpty()) l.setErabiltzailea(erabiltzailea);
        if (!pasahitza.isEmpty()) l.setPasahitza(pasahitza);
        if (rolaString != null) {
            int rolaId = Integer.parseInt(rolaString.split(" ")[0]);
            l.setRolaId(rolaId);
        }
        if (aktibo != null) l.setAktibo(aktibo);

        boolean ok = LangileaDAO.update(l);

        if (ok) {
            tableView.refresh();
            alerta("Arrakasta", "Langilea ondo eguneratu da!");
        } else {
            alerta("Error", "Langilea ezin izan da eguneratu");
        }
    }

    @FXML
    private void garbituEremuak() {
        izenaField.clear();
        erabiltzaileaField.clear();
        pasahitzaField.clear();
        rolaComboBox.setValue(null);
        aktiboComboBox.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void alerta(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
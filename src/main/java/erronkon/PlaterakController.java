package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PlaterakController {

    @FXML
    private TableView<Platera> tableView;

    @FXML
    private TableColumn<Platera, Integer> idColumn;

    @FXML
    private TableColumn<Platera, String> izenaColumn;

    @FXML
    private TableColumn<Platera, String> deskribapenaColumn;

    @FXML
    private TableColumn<Platera, Double> prezioaColumn;

    @FXML
    private TableColumn<Platera, Integer> kategoriaColumn;

    @FXML
    private TableColumn<Platera, String> erabilgarriColumn;

    @FXML
    private TableColumn<Platera, String> sortzeDataColumn;

    @FXML
    private TextField izenaField;

    @FXML
    private TextArea deskribapenaField;

    @FXML
    private TextField prezioaField;

    @FXML
    private TextField irudiaField;

    @FXML
    private ComboBox<String> kategoriaComboBox;

    @FXML
    private ComboBox<String> erabilgarriComboBox;

    @FXML
    private Button gehituBtn;

    @FXML
    private Button ezabatuBtn;

    @FXML
    private Button editatuBtn;

    @FXML
    private Button garbituBtn;

    private ObservableList<Platera> platerakList;

    @FXML
    public void initialize() {
        // Configurar las columnas con los nombres correctos (camelCase para JavaFX)
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        deskribapenaColumn.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));
        prezioaColumn.setCellValueFactory(new PropertyValueFactory<>("prezioa"));
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<>("kategoriaId"));
        erabilgarriColumn.setCellValueFactory(new PropertyValueFactory<>("erabilgarri"));
        sortzeDataColumn.setCellValueFactory(new PropertyValueFactory<>("sortzeData"));

        // Cargar opciones para los ComboBox
        cargarComboBoxes();

        // Cargar datos desde la API
        recargarPlaterak();

        // Manejar selección de la tabla
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosFormulario(newSelection);
            }
        });

        // Asignar acciones a los botones
        gehituBtn.setOnAction(e -> gehituPlatera());
        ezabatuBtn.setOnAction(e -> ezabatuPlatera());
        editatuBtn.setOnAction(e -> editatuPlatera());
        garbituBtn.setOnAction(e -> garbituEremuak());
    }

    private void cargarComboBoxes() {
        // Opciones para Kategoria ID
        ObservableList<String> kategoriak = FXCollections.observableArrayList();
        kategoriak.add("1 - Lehenengo platerak");
        kategoriak.add("2 - Bigarren platerak");
        kategoriak.add("3 - Postreak");
        kategoriak.add("4 - Edariak");
        kategoriaComboBox.setItems(kategoriak);

        // Opciones para Erabilgarri (Sí/No)
        ObservableList<String> erabilgarriak = FXCollections.observableArrayList();
        erabilgarriak.add("Bai");
        erabilgarriak.add("Ez");
        erabilgarriComboBox.setItems(erabilgarriak);
    }

    private void recargarPlaterak() {
        List<Platera> lista = PlateraDAO.getAll();
        platerakList = FXCollections.observableArrayList(lista);
        tableView.setItems(platerakList);
    }

    private void cargarDatosFormulario(Platera platera) {
        izenaField.setText(platera.getIzena());
        deskribapenaField.setText(platera.getDeskribapena());
        prezioaField.setText(String.format("%.2f", platera.getPrezioa()));
        irudiaField.setText(platera.getIrudia());

        // Establecer el valor del ComboBox de kategoria
        int kategoriaId = platera.getKategoriaId();
        String kategoriaString = kategoriaId + " - " + getKategoriaIzena(kategoriaId);
        kategoriaComboBox.setValue(kategoriaString);

        erabilgarriComboBox.setValue(platera.getErabilgarri());
    }

    private String getKategoriaIzena(int kategoriaId) {
        switch (kategoriaId) {
            case 1: return "Lehenengo platerak";
            case 2: return "Bigarren platerak";
            case 3: return "Postreak";
            case 4: return "Edariak";
            default: return "ezezaguna";
        }
    }

    @FXML
    private void gehituPlatera() {
        String izena = izenaField.getText().trim();
        String deskribapena = deskribapenaField.getText().trim();
        String prezioaStr = prezioaField.getText().trim().replace(",", ".");
        String irudia = irudiaField.getText().trim();
        String kategoriaString = kategoriaComboBox.getValue();
        String erabilgarri = erabilgarriComboBox.getValue();

        // Validaciones
        if (izena.isEmpty() || deskribapena.isEmpty() || prezioaStr.isEmpty() ||
                kategoriaString == null || erabilgarri == null) {
            alerta("Error", "Datu guztiak bete behar dira (irudia aukerakoa da)");
            return;
        }

        double prezioa;
        try {
            prezioa = Double.parseDouble(prezioaStr);
            if (prezioa <= 0) {
                alerta("Error", "Prezioa 0 baino handiagoa izan behar da");
                return;
            }
        } catch (NumberFormatException e) {
            alerta("Error", "Prezioa zenbaki baliogarria izan behar da (adibidez: 12.50)");
            return;
        }

        // Obtener el ID de la kategoria del string seleccionado
        int kategoriaId = Integer.parseInt(kategoriaString.split(" ")[0]);

        // Crear nuevo platera
        Platera p = new Platera();
        p.setIzena(izena);
        p.setDeskribapena(deskribapena);
        p.setPrezioa(prezioa);
        p.setKategoriaId(kategoriaId);
        p.setErabilgarri(erabilgarri);
        p.setSortzeData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        p.setIrudia(irudia);

        boolean ok = PlateraDAO.create(p);

        if (ok) {
            recargarPlaterak();
            garbituEremuak();
            alerta("Arrakasta", "Platera ondo gehitu da!");
        } else {
            alerta("Error", "Platera ezin izan da gehitu");
        }
    }

    @FXML
    private void ezabatuPlatera() {
        Platera p = tableView.getSelectionModel().getSelectedItem();
        if (p == null) {
            alerta("Error", "Hautatu platera ezabatzeko");
            return;
        }

        // Confirmar antes de eliminar
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Berrespena");
        confirm.setHeaderText(null);
        confirm.setContentText("Ziur zaude '" + p.getIzena() + "' platera ezabatu nahi duzula?");

        // Configurar botones personalizados
        ButtonType baiBotoia = new ButtonType("Bai");
        ButtonType ezBotoia = new ButtonType("Ez");
        confirm.getButtonTypes().setAll(baiBotoia, ezBotoia);

        // Mostrar el diálogo y esperar respuesta
        Optional<ButtonType> emaitza = confirm.showAndWait();

        // Si el usuario presiona "Bai"
        if (emaitza.isPresent() && emaitza.get() == baiBotoia) {
            boolean ok = PlateraDAO.delete(p.getId());
            if (ok) {
                // Refrescar toda la lista desde la API
                recargarPlaterak();
                garbituEremuak();
                alerta("Arrakasta", "Platera ondo ezabatu da!");
            } else {
                alerta("Error", "Platera ezin izan da ezabatu. Ba al dago konexioa APIarekin?");
            }
        }
        // Si el usuario presiona "Ez", no hacer nada
    }

    @FXML
    private void editatuPlatera() {
        Platera p = tableView.getSelectionModel().getSelectedItem();
        if (p == null) {
            alerta("Error", "Hautatu platera editatzeko");
            return;
        }

        String izena = izenaField.getText().trim();
        String deskribapena = deskribapenaField.getText().trim();
        String prezioaStr = prezioaField.getText().trim().replace(",", ".");
        String irudia = irudiaField.getText().trim();
        String kategoriaString = kategoriaComboBox.getValue();
        String erabilgarri = erabilgarriComboBox.getValue();

        if (!izena.isEmpty()) p.setIzena(izena);
        if (!deskribapena.isEmpty()) p.setDeskribapena(deskribapena);
        if (!prezioaStr.isEmpty()) {
            try {
                double prezioa = Double.parseDouble(prezioaStr);
                p.setPrezioa(prezioa);
            } catch (NumberFormatException e) {
                alerta("Error", "Prezioa zenbaki baliogarria izan behar da");
                return;
            }
        }
        if (!irudia.isEmpty()) p.setIrudia(irudia);

        if (kategoriaString != null) {
            int kategoriaId = Integer.parseInt(kategoriaString.split(" ")[0]);
            p.setKategoriaId(kategoriaId);
        }

        if (erabilgarri != null) p.setErabilgarri(erabilgarri);

        boolean ok = PlateraDAO.update(p);

        if (ok) {
            tableView.refresh();
            alerta("Arrakasta", "Platera ondo eguneratu da!");
        } else {
            alerta("Error", "Platera ezin izan da eguneratu");
        }
    }

    @FXML
    private void garbituEremuak() {
        izenaField.clear();
        deskribapenaField.clear();
        prezioaField.clear();
        irudiaField.clear();
        kategoriaComboBox.setValue(null);
        erabilgarriComboBox.setValue(null);
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
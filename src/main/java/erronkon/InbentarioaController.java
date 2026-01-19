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
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class InbentarioaController {

    @FXML
    private TableView<Inbentarioa> tableView;

    @FXML
    private TableColumn<Inbentarioa, Integer> idColumn;

    @FXML
    private TableColumn<Inbentarioa, String> izenaColumn;

    @FXML
    private TableColumn<Inbentarioa, String> deskribapenaColumn;

    @FXML
    private TableColumn<Inbentarioa, Integer> kantitateaColumn;

    @FXML
    private TableColumn<Inbentarioa, String> neurriaColumn;

    @FXML
    private TableColumn<Inbentarioa, Integer> stockMinimoColumn;

    @FXML
    private TableColumn<Inbentarioa, String> eguneratzeColumn;

    @FXML
    private TextField izenaField;

    @FXML
    private TextArea deskribapenaField;

    @FXML
    private TextField kantitateaField;

    @FXML
    private ComboBox<String> neurriaComboBox;

    @FXML
    private TextField stockMinimoField;

    @FXML
    private Button gehituBtn;

    @FXML
    private Button ezabatuBtn;

    @FXML
    private Button editatuBtn;

    @FXML
    private Button garbituBtn;

    private ObservableList<Inbentarioa> inbentarioaList;

    @FXML
    public void initialize() {
        // Configurar las columnas
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        izenaColumn.setCellValueFactory(new PropertyValueFactory<>("izena"));
        deskribapenaColumn.setCellValueFactory(new PropertyValueFactory<>("deskribapena"));
        kantitateaColumn.setCellValueFactory(new PropertyValueFactory<>("kantitatea"));
        neurriaColumn.setCellValueFactory(new PropertyValueFactory<>("neurriaUnitatea"));
        stockMinimoColumn.setCellValueFactory(new PropertyValueFactory<>("stockMinimoa"));
        eguneratzeColumn.setCellValueFactory(new PropertyValueFactory<>("azkenEguneratzea"));


        cargarComboBox();


        recargarInbentarioa();


        configurarEstiloTabla();


        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosFormulario(newSelection);
            }
        });

        // Asignar acciones a los botones
        gehituBtn.setOnAction(e -> gehituInbentarioa());
        ezabatuBtn.setOnAction(e -> ezabatuInbentarioa());
        editatuBtn.setOnAction(e -> editatuInbentarioa());
        garbituBtn.setOnAction(e -> garbituEremuak());
    }

    private void cargarComboBox() {
        ObservableList<String> neurriak = FXCollections.observableArrayList();
        neurriak.add("Unitatea");
        neurriak.add("Kilogramo");
        neurriak.add("Litro");
        neurriak.add("Gramo");
        neurriak.add("Mililitro");
        neurriak.add("Taza");
        neurriak.add("Cucharada");
        neurriaComboBox.setItems(neurriak);
    }

    private void configurarEstiloTabla() {
        tableView.setRowFactory(tv -> new javafx.scene.control.TableRow<Inbentarioa>() {
            @Override
            protected void updateItem(Inbentarioa item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    // Si la cantidad es menor que el stock mínimo, mostrar en rojo
                    if (item.getKantitatea() < item.getStockMinimoa()) {
                        setStyle("-fx-background-color: #ffcccc; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void recargarInbentarioa() {
        List<Inbentarioa> lista = InbentarioaDAO.getAll();
        inbentarioaList = FXCollections.observableArrayList(lista);
        tableView.setItems(inbentarioaList);
    }

    private void cargarDatosFormulario(Inbentarioa inbentarioa) {
        izenaField.setText(inbentarioa.getIzena());
        deskribapenaField.setText(inbentarioa.getDeskribapena());
        kantitateaField.setText(String.valueOf(inbentarioa.getKantitatea()));
        neurriaComboBox.setValue(inbentarioa.getNeurriaUnitatea());
        stockMinimoField.setText(String.valueOf(inbentarioa.getStockMinimoa()));
    }

    @FXML
    private void gehituInbentarioa() {
        String izena = izenaField.getText().trim();
        String deskribapena = deskribapenaField.getText().trim();
        String kantitateaStr = kantitateaField.getText().trim();
        String neurria = neurriaComboBox.getValue();
        String stockMinimoStr = stockMinimoField.getText().trim();


        if (izena.isEmpty() || kantitateaStr.isEmpty() || neurria == null || stockMinimoStr.isEmpty()) {
            alerta("Error", "Izena, Kantitatea, Neurria eta Stock Minimoa bete behar dira");
            return;
        }

        int kantitatea, stockMinimo;
        try {
            kantitatea = Integer.parseInt(kantitateaStr);
            stockMinimo = Integer.parseInt(stockMinimoStr);

            if (kantitatea < 0 || stockMinimo < 0) {
                alerta("Error", "Zenbakiak 0 edo gehiago izan behar dira");
                return;
            }
        } catch (NumberFormatException e) {
            alerta("Error", "Kantitatea eta Stock Minimoa zenbaki baliogarriak izan behar dira");
            return;
        }


        Inbentarioa i = new Inbentarioa();
        i.setIzena(izena);
        i.setDeskribapena(deskribapena);
        i.setKantitatea(kantitatea);
        i.setNeurriaUnitatea(neurria);
        i.setStockMinimoa(stockMinimo);
        i.setAzkenEguneratzea(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        boolean ok = InbentarioaDAO.create(i);

        if (ok) {
            recargarInbentarioa();
            garbituEremuak();
            alerta("Arrakasta", "Inbentario elementua ondo gehitu da!");
        } else {
            alerta("Error", "Inbentario elementua ezin izan da gehitu");
        }
    }

    @FXML
    private void ezabatuInbentarioa() {
        Inbentarioa i = tableView.getSelectionModel().getSelectedItem();
        if (i == null) {
            alerta("Error", "Hautatu inbentario elementua ezabatzeko");
            return;
        }


        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Berrespena");
        confirm.setHeaderText(null);
        confirm.setContentText("Ziur zaude '" + i.getIzena() + "' elementua ezabatu nahi duzula?");

        ButtonType baiBotoia = new ButtonType("Bai");
        ButtonType ezBotoia = new ButtonType("Ez");
        confirm.getButtonTypes().setAll(baiBotoia, ezBotoia);

        Optional<ButtonType> emaitza = confirm.showAndWait();

        if (emaitza.isPresent() && emaitza.get() == baiBotoia) {
            boolean ok = InbentarioaDAO.delete(i.getId());
            if (ok) {
                recargarInbentarioa();
                garbituEremuak();
                alerta("Arrakasta", "Inbentario elementua ondo ezabatu da!");
            } else {
                alerta("Error", "Inbentario elementua ezin izan da ezabatu");
            }
        }
    }

    @FXML
    private void editatuInbentarioa() {
        Inbentarioa i = tableView.getSelectionModel().getSelectedItem();
        if (i == null) {
            alerta("Error", "Hautatu inbentario elementua editatzeko");
            return;
        }

        String izena = izenaField.getText().trim();
        String deskribapena = deskribapenaField.getText().trim();
        String kantitateaStr = kantitateaField.getText().trim();
        String neurria = neurriaComboBox.getValue();
        String stockMinimoStr = stockMinimoField.getText().trim();

        if (!izena.isEmpty()) i.setIzena(izena);
        if (!deskribapena.isEmpty()) i.setDeskribapena(deskribapena);

        if (!kantitateaStr.isEmpty()) {
            try {
                int kantitatea = Integer.parseInt(kantitateaStr);
                i.setKantitatea(kantitatea);
            } catch (NumberFormatException e) {
                alerta("Error", "Kantitatea zenbaki baliogarria izan behar da");
                return;
            }
        }

        if (neurria != null) i.setNeurriaUnitatea(neurria);

        if (!stockMinimoStr.isEmpty()) {
            try {
                int stockMinimo = Integer.parseInt(stockMinimoStr);
                i.setStockMinimoa(stockMinimo);
            } catch (NumberFormatException e) {
                alerta("Error", "Stock minimoa zenbaki baliogarria izan behar da");
                return;
            }
        }


        i.setAzkenEguneratzea(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        boolean ok = InbentarioaDAO.update(i);

        if (ok) {
            tableView.refresh(); // Refrescar para actualizar los estilos
            alerta("Arrakasta", "Inbentario elementua ondo eguneratu da!");
        } else {
            alerta("Error", "Inbentario elementua ezin izan da eguneratu");
        }
    }

    @FXML
    private void garbituEremuak() {
        izenaField.clear();
        deskribapenaField.clear();
        kantitateaField.clear();
        neurriaComboBox.setValue(null);
        stockMinimoField.clear();
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
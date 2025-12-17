package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GerenteController {

    @FXML private TableView<Langilea> table;
    @FXML private TableColumn<Langilea, String> colIzena;
    @FXML private TableColumn<Langilea, String> colErabiltzailea;
    @FXML private TableColumn<Langilea, Number> colRola;

    private ObservableList<Langilea> datuak;

    @FXML
    private void initialize() {
        colIzena.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIzena()));
        colErabiltzailea.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getErabiltzailea()));
        colRola.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRolaId()));

        kargatuDatuak();
    }

    private void kargatuDatuak() {
        datuak = FXCollections.observableArrayList(LangileaDAO.getAll());
        table.setItems(datuak);
    }

    @FXML
    private void aldatuRola() {
        Langilea l = table.getSelectionModel().getSelectedItem();
        if (l == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ez da langilerik aukeratu.");
            alert.showAndWait();
            return;
        }

        // Creamos el cuadro de texto
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aldatu Rola");
        dialog.setHeaderText("Idatzi rol berria (1-4):");
        dialog.setContentText("1 - sukaldaria\n"
                + "2 - zerbitzaria\n"
                + "3 - admin\n"
                + "4 - jefea");

        // Mostramos el diálogo y recogemos la respuesta
        dialog.showAndWait().ifPresent(input -> {
            try {
                int berria = Integer.parseInt(input);

                if (berria < 1 || berria > 4) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Rol okerra. Aukeratu 1-4.");
                    alert.showAndWait();
                    return;
                }

                // Actualizamos en la base de datos
                LangileaDAO.updateRola(l.getId(), berria);

                // Actualizamos el objeto en memoria
                l.setRolaId(berria);

                // Refrescamos la tabla
                table.refresh();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Sartu zenbaki bat (1-4).");
                alert.showAndWait();
            }
        });
    }

}

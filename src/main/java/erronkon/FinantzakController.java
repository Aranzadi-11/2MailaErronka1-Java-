package erronkon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FinantzakController {

    @FXML
    private TableView<Finantza> tableView;

    @FXML
    private TableColumn<Finantza, String> eskaeraDataColumn;

    @FXML
    private TableColumn<Finantza, Double> guztiraColumn;

    @FXML
    private TextField guztiraField;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private Label totalTransakzioakLabel;

    private ObservableList<Finantza> finantzakList;
    private List<Finantza> allFinantzak;

    @FXML
    public void initialize() {

        eskaeraDataColumn.setCellValueFactory(new PropertyValueFactory<>("eskaeraData"));
        guztiraColumn.setCellValueFactory(new PropertyValueFactory<>("guztira"));


        dataPicker.setValue(LocalDate.now());


        allFinantzak = FinantzaDAO.getAll();
        finantzakList = FXCollections.observableArrayList(allFinantzak);


        tableView.setItems(finantzakList);


        kalkulatuTotalak();


        dataPicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                filtrarData(newDate);
            }
        });
    }

    private void filtrarData(LocalDate data) {
        if (data == null) return;

        String dataFormateatua = data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ObservableList<Finantza> filteredList = FXCollections.observableArrayList();
        double totalDia = 0.0;
        int kontadorea = 0;

        for (Finantza finantza : allFinantzak) {
            if (finantza.getEskaeraData() != null &&
                    finantza.getEskaeraData().startsWith(dataFormateatua)) {
                filteredList.add(finantza);
                totalDia += finantza.getGuztira();
                kontadorea++;
            }
        }


        tableView.setItems(filteredList);


        guztiraField.setText(String.format("%.2f €", totalDia));


        totalTransakzioakLabel.setText(kontadorea + " transakzio");
    }

    private void kalkulatuTotalak() {
        double totalGuztira = 0.0;
        int kontadorea = 0;

        for (Finantza finantza : allFinantzak) {
            totalGuztira += finantza.getGuztira();
            kontadorea++;
        }


        LocalDate gaur = LocalDate.now();
        filtrarData(gaur);


        totalTransakzioakLabel.setText(kontadorea + " transakzio guztira");
    }

    @FXML
    private void erakutsiDenak() {
        dataPicker.setValue(null);


        tableView.setItems(finantzakList);


        double totalGuztira = 0.0;
        for (Finantza finantza : allFinantzak) {
            totalGuztira += finantza.getGuztira();
        }
        guztiraField.setText(String.format("%.2f €", totalGuztira));
        totalTransakzioakLabel.setText(allFinantzak.size() + " transakzio guztira");
    }
}
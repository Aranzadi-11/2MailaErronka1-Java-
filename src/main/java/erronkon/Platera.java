package erronkon;

import javafx.beans.property.*;

public class Platera {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty izena = new SimpleStringProperty();
    private final StringProperty deskribapena = new SimpleStringProperty();
    private final DoubleProperty prezioa = new SimpleDoubleProperty();
    private final IntegerProperty kategoriaId = new SimpleIntegerProperty();
    private final StringProperty erabilgarri = new SimpleStringProperty();
    private final StringProperty sortzeData = new SimpleStringProperty();
    private final StringProperty irudia = new SimpleStringProperty();

    public Platera() {}

    public Platera(String izena, String deskribapena, double prezioa) {
        this.izena.set(izena);
        this.deskribapena.set(deskribapena);
        this.prezioa.set(prezioa);
    }

    public Platera(int id, String izena, String deskribapena, double prezioa) {
        this.id.set(id);
        this.izena.set(izena);
        this.deskribapena.set(deskribapena);
        this.prezioa.set(prezioa);
    }

    // Getters y Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getIzena() { return izena.get(); }
    public void setIzena(String izena) { this.izena.set(izena); }
    public StringProperty izenaProperty() { return izena; }

    public String getDeskribapena() { return deskribapena.get(); }
    public void setDeskribapena(String deskribapena) { this.deskribapena.set(deskribapena); }
    public StringProperty deskribapenaProperty() { return deskribapena; }

    public double getPrezioa() { return prezioa.get(); }
    public void setPrezioa(double prezioa) { this.prezioa.set(prezioa); }
    public DoubleProperty prezioaProperty() { return prezioa; }

    public int getKategoriaId() { return kategoriaId.get(); }
    public void setKategoriaId(int kategoriaId) { this.kategoriaId.set(kategoriaId); }
    public IntegerProperty kategoriaIdProperty() { return kategoriaId; }

    public String getErabilgarri() { return erabilgarri.get(); }
    public void setErabilgarri(String erabilgarri) { this.erabilgarri.set(erabilgarri); }
    public StringProperty erabilgarriProperty() { return erabilgarri; }

    public String getSortzeData() { return sortzeData.get(); }
    public void setSortzeData(String sortzeData) { this.sortzeData.set(sortzeData); }
    public StringProperty sortzeDataProperty() { return sortzeData; }

    public String getIrudia() { return irudia.get(); }
    public void setIrudia(String irudia) { this.irudia.set(irudia); }
    public StringProperty irudiaProperty() { return irudia; }
}
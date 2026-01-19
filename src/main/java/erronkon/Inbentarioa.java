package erronkon;

import javafx.beans.property.*;

public class Inbentarioa {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty izena = new SimpleStringProperty();
    private final StringProperty deskribapena = new SimpleStringProperty();
    private final IntegerProperty kantitatea = new SimpleIntegerProperty();
    private final StringProperty neurriaUnitatea = new SimpleStringProperty();
    private final IntegerProperty stockMinimoa = new SimpleIntegerProperty();
    private final StringProperty azkenEguneratzea = new SimpleStringProperty();

    public Inbentarioa() {}

    public Inbentarioa(String izena, String deskribapena, int kantitatea) {
        this.izena.set(izena);
        this.deskribapena.set(deskribapena);
        this.kantitatea.set(kantitatea);
    }

    public Inbentarioa(int id, String izena, String deskribapena, int kantitatea) {
        this.id.set(id);
        this.izena.set(izena);
        this.deskribapena.set(deskribapena);
        this.kantitatea.set(kantitatea);
    }


    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getIzena() { return izena.get(); }
    public void setIzena(String izena) { this.izena.set(izena); }
    public StringProperty izenaProperty() { return izena; }

    public String getDeskribapena() { return deskribapena.get(); }
    public void setDeskribapena(String deskribapena) { this.deskribapena.set(deskribapena); }
    public StringProperty deskribapenaProperty() { return deskribapena; }

    public int getKantitatea() { return kantitatea.get(); }
    public void setKantitatea(int kantitatea) { this.kantitatea.set(kantitatea); }
    public IntegerProperty kantitateaProperty() { return kantitatea; }

    public String getNeurriaUnitatea() { return neurriaUnitatea.get(); }
    public void setNeurriaUnitatea(String neurriaUnitatea) { this.neurriaUnitatea.set(neurriaUnitatea); }
    public StringProperty neurriaUnitateaProperty() { return neurriaUnitatea; }

    public int getStockMinimoa() { return stockMinimoa.get(); }
    public void setStockMinimoa(int stockMinimoa) { this.stockMinimoa.set(stockMinimoa); }
    public IntegerProperty stockMinimoaProperty() { return stockMinimoa; }

    public String getAzkenEguneratzea() { return azkenEguneratzea.get(); }
    public void setAzkenEguneratzea(String azkenEguneratzea) { this.azkenEguneratzea.set(azkenEguneratzea); }
    public StringProperty azkenEguneratzeaProperty() { return azkenEguneratzea; }
}
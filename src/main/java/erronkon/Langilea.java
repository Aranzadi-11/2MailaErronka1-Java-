package erronkon;

import javafx.beans.property.*;

public class Langilea {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty izena = new SimpleStringProperty();
    private final StringProperty abizena = new SimpleStringProperty();
    private final StringProperty erabiltzailea = new SimpleStringProperty();
    private final StringProperty pasahitza = new SimpleStringProperty();
    private final StringProperty aktibo = new SimpleStringProperty();
    private final StringProperty erregistroData = new SimpleStringProperty();
    private final IntegerProperty rolaId = new SimpleIntegerProperty();

    public Langilea() {}

    public Langilea(String izenaOsoa) {
        this.izena.set(izenaOsoa);
    }

    public Langilea(int id, String izenaOsoa) {
        this.id.set(id);
        this.izena.set(izenaOsoa);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getIzena() { return izena.get(); }
    public void setIzena(String izena) { this.izena.set(izena); }
    public StringProperty izenaProperty() { return izena; }

    public String getAbizena() { return abizena.get(); }
    public void setAbizena(String abizena) { this.abizena.set(abizena); }
    public StringProperty abizenaProperty() { return abizena; }

    public String getErabiltzailea() { return erabiltzailea.get(); }
    public void setErabiltzailea(String e) { this.erabiltzailea.set(e); }
    public StringProperty erabiltzaileaProperty() { return erabiltzailea; }

    public String getPasahitza() { return pasahitza.get(); }
    public void setPasahitza(String p) { this.pasahitza.set(p); }
    public StringProperty pasahitzaProperty() { return pasahitza; }

    public String getAktibo() { return aktibo.get(); }
    public void setAktibo(String a) { this.aktibo.set(a); }
    public StringProperty aktiboProperty() { return aktibo; }

    public String getErregistroData() { return erregistroData.get(); }
    public void setErregistroData(String d) { this.erregistroData.set(d); }
    public StringProperty erregistroDataProperty() { return erregistroData; }

    public int getRolaId() { return rolaId.get(); }
    public void setRolaId(int r) { this.rolaId.set(r); }
    public IntegerProperty rolaIdProperty() { return rolaId; }
}
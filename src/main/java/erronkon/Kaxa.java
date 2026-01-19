package erronkon;

import javafx.beans.property.*;

public class Kaxa {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty izena = new SimpleStringProperty();
    private final DoubleProperty kaxaGuztira = new SimpleDoubleProperty();
    private final StringProperty helbidea = new SimpleStringProperty();
    private final StringProperty telefonoa = new SimpleStringProperty();

    public Kaxa() {}

    public Kaxa(double kaxaGuztira) {
        this.kaxaGuztira.set(kaxaGuztira);
    }


    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getIzena() { return izena.get(); }
    public void setIzena(String izena) { this.izena.set(izena); }
    public StringProperty izenaProperty() { return izena; }

    public double getKaxaGuztira() { return kaxaGuztira.get(); }
    public void setKaxaGuztira(double kaxaGuztira) { this.kaxaGuztira.set(kaxaGuztira); }
    public DoubleProperty kaxaGuztiraProperty() { return kaxaGuztira; }

    public String getHelbidea() { return helbidea.get(); }
    public void setHelbidea(String helbidea) { this.helbidea.set(helbidea); }
    public StringProperty helbideaProperty() { return helbidea; }

    public String getTelefonoa() { return telefonoa.get(); }
    public void setTelefonoa(String telefonoa) { this.telefonoa.set(telefonoa); }
    public StringProperty telefonoaProperty() { return telefonoa; }
}
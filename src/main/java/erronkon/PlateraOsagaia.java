package erronkon;

import javafx.beans.property.*;

public class PlateraOsagaia {
    private final IntegerProperty plateraId = new SimpleIntegerProperty();
    private final IntegerProperty inbentarioaId = new SimpleIntegerProperty();
    private final IntegerProperty kantitatea = new SimpleIntegerProperty();

    private final StringProperty plateraIzena = new SimpleStringProperty();
    private final StringProperty osagaiIzena = new SimpleStringProperty();
    private final StringProperty neurria = new SimpleStringProperty();

    public PlateraOsagaia() {}

    public PlateraOsagaia(int plateraId, int inbentarioaId, int kantitatea) {
        this.plateraId.set(plateraId);
        this.inbentarioaId.set(inbentarioaId);
        this.kantitatea.set(kantitatea);
    }

    public int getPlateraId() { return plateraId.get(); }
    public void setPlateraId(int plateraId) { this.plateraId.set(plateraId); }
    public IntegerProperty plateraIdProperty() { return plateraId; }

    public int getInbentarioaId() { return inbentarioaId.get(); }
    public void setInbentarioaId(int inbentarioaId) { this.inbentarioaId.set(inbentarioaId); }
    public IntegerProperty inbentarioaIdProperty() { return inbentarioaId; }

    public int getKantitatea() { return kantitatea.get(); }
    public void setKantitatea(int kantitatea) { this.kantitatea.set(kantitatea); }
    public IntegerProperty kantitateaProperty() { return kantitatea; }

    public String getPlateraIzena() { return plateraIzena.get(); }
    public void setPlateraIzena(String plateraIzena) { this.plateraIzena.set(plateraIzena); }
    public StringProperty plateraIzenaProperty() { return plateraIzena; }

    public String getOsagaiIzena() { return osagaiIzena.get(); }
    public void setOsagaiIzena(String osagaiIzena) { this.osagaiIzena.set(osagaiIzena); }
    public StringProperty osagaiIzenaProperty() { return osagaiIzena; }

    public String getNeurria() { return neurria.get(); }
    public void setNeurria(String neurria) { this.neurria.set(neurria); }
    public StringProperty neurriaProperty() { return neurria; }
}
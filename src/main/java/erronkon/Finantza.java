package erronkon;

import javafx.beans.property.*;

public class Finantza {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty bezeroaId = new SimpleIntegerProperty();
    private final IntegerProperty langileaId = new SimpleIntegerProperty();
    private final IntegerProperty plateraId = new SimpleIntegerProperty();
    private final StringProperty eskaeraData = new SimpleStringProperty();
    private final StringProperty egoera = new SimpleStringProperty();
    private final DoubleProperty guztira = new SimpleDoubleProperty();

    public Finantza() {}

    public Finantza(int id, String eskaeraData, double guztira) {
        this.id.set(id);
        this.eskaeraData.set(eskaeraData);
        this.guztira.set(guztira);
    }


    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public int getBezeroaId() { return bezeroaId.get(); }
    public void setBezeroaId(int bezeroaId) { this.bezeroaId.set(bezeroaId); }
    public IntegerProperty bezeroaIdProperty() { return bezeroaId; }

    public int getLangileaId() { return langileaId.get(); }
    public void setLangileaId(int langileaId) { this.langileaId.set(langileaId); }
    public IntegerProperty langileaIdProperty() { return langileaId; }

    public int getPlateraId() { return plateraId.get(); }
    public void setPlateraId(int plateraId) { this.plateraId.set(plateraId); }
    public IntegerProperty plateraIdProperty() { return plateraId; }

    public String getEskaeraData() { return eskaeraData.get(); }
    public void setEskaeraData(String eskaeraData) { this.eskaeraData.set(eskaeraData); }
    public StringProperty eskaeraDataProperty() { return eskaeraData; }

    public String getEgoera() { return egoera.get(); }
    public void setEgoera(String egoera) { this.egoera.set(egoera); }
    public StringProperty egoeraProperty() { return egoera; }

    public double getGuztira() { return guztira.get(); }
    public void setGuztira(double guztira) { this.guztira.set(guztira); }
    public DoubleProperty guztiraProperty() { return guztira; }
}
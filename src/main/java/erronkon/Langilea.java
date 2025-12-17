package erronkon;

public class Langilea {
    private int id;
    private String izena;
    private String erabiltzailea;
    private String pasahitza;
    private String baimena; // Bai/Ez
    private String alta;
    private int rolaId;

    public Langilea(int id, String izena, String erabiltzailea, String pasahitza,
                    String baimena, String alta, int rolaId) {
        this.id = id;
        this.izena = izena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
        this.baimena = baimena;
        this.alta = alta;
        this.rolaId = rolaId;
    }

    public int getId() { return id; }
    public String getIzena() { return izena; }
    public String getErabiltzailea() { return erabiltzailea; }
    public String getPasahitza() { return pasahitza; }
    public String getBaimena() { return baimena; }
    public String getAlta() { return alta; }
    public int getRolaId() { return rolaId; }

    public void setBaimena(String b) { this.baimena = b; }
    public void setRolaId(int r) { this.rolaId = r; }
}

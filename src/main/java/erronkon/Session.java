package erronkon;

public class Session {

    private static int id;
    private static String erabiltzailea;
    private static String izena;
    private static int rolaId;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Session.id = id;
    }

    public static String getErabiltzailea() {
        return erabiltzailea;
    }

    public static void setErabiltzailea(String erabiltzailea) {
        Session.erabiltzailea = erabiltzailea;
    }

    public static String getIzena() {
        return izena;
    }

    public static void setIzena(String izena) {
        Session.izena = izena;
    }

    public static int getRolaId() {
        return rolaId;
    }

    public static void setRolaId(int rolaId) {
        Session.rolaId = rolaId;
    }

    public static String getRolaIzena() {
        return switch (rolaId) {
            case 3 -> "Admin";
            case 4 -> "Jefea";
            default -> "Langilea";
        };
    }
}

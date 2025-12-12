package erronkon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBKonexioa {

    private static final String URL = "jdbc:mysql://192.168.115.153:3306/jatetxea";
    private static final String ERABILTZAILEA = "admin";
    private static final String PASAHITZA = "abc123ABC@";

    public static Connection lortuKonexioa() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, ERABILTZAILEA, PASAHITZA);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Errorea konexioan: " + e.getMessage());
            return null;
        }
    }
}
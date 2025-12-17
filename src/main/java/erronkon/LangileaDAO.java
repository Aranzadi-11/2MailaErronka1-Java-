package erronkon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LangileaDAO {

    public static List<Langilea> getAll() {
        List<Langilea> lista = new ArrayList<>();
        String sql = "SELECT * FROM langileak";

        try (Connection con = DBKonexioa.lortuKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Langilea l = new Langilea(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7)
                );
                lista.add(l);
            }

        } catch (SQLException e) {
            System.out.println("Errorea getAll(): " + e.getMessage());
        }
        return lista;
    }

    public static void updateBaimena(int id, String baimena) {
        String sql = "UPDATE langileak SET baimena = ? WHERE id = ?";
        try (Connection con = DBKonexioa.lortuKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, baimena);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Errorea updateBaimena(): " + e.getMessage());
        }
    }

    public static void updateRola(int id, int rolaId) {
        String sql = "UPDATE langileak SET rola_id = ? WHERE id = ?";
        try (Connection con = DBKonexioa.lortuKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rolaId);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Errorea updateRola(): " + e.getMessage());
        }
    }
}

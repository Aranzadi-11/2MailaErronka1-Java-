package erronkon;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LangileaDAO {

    public static final String BASE_URL = "https://localhost:7236/api/Langileak";

    public static List<Langilea> getAll() {
        List<Langilea> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    lista.add(jsonToLangilea(obj));
                }
            } else {
                System.err.println("Error API al obtener: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Exception in getAll: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean create(Langilea l) {
        try {
            JSONObject obj = langileaToJson(l);
            System.out.println("Enviando JSON: " + obj.toString());

            HttpResponse<String> response = ApiService.post(BASE_URL, obj.toString());
            System.out.println("Respuesta API: " + response.statusCode() + " - " + response.body());

            return response.statusCode() == 201 || response.statusCode() == 200;

        } catch (Exception e) {
            System.err.println("Exception in create: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Langilea l) {
        try {
            JSONObject obj = langileaToJson(l);
            System.out.println("Actualizando JSON: " + obj.toString());

            String url = BASE_URL + "/" + l.getId();
            HttpResponse<String> response = ApiService.put(url, obj.toString());
            System.out.println("Respuesta API: " + response.statusCode() + " - " + response.body());

            return response.statusCode() == 200;

        } catch (Exception e) {
            System.err.println("Exception in update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try {
            String url = BASE_URL + "/" + id;
            HttpResponse<String> response = ApiService.delete(url);
            System.out.println("Respuesta API al eliminar: " + response.statusCode() + " - " + response.body());

            return response.statusCode() == 200 || response.statusCode() == 204;

        } catch (Exception e) {
            System.err.println("Exception in delete: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRola(int id, int rolaId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("rolaId", rolaId);

            String url = BASE_URL + "/" + id + "/rola";
            HttpResponse<String> response = ApiService.put(url, obj.toString());
            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares
    private static Langilea jsonToLangilea(JSONObject obj) {
        Langilea l = new Langilea();
        l.setId(obj.getInt("id"));
        l.setIzena(obj.getString("izena"));
        l.setErabiltzailea(obj.getString("erabiltzailea"));
        l.setPasahitza(obj.optString("pasahitza", ""));
        l.setAktibo(obj.getString("aktibo"));
        l.setErregistroData(obj.getString("erregistroData"));
        l.setRolaId(obj.getInt("rolaId"));
        return l;
    }

    private static JSONObject langileaToJson(Langilea l) {
        JSONObject obj = new JSONObject();
        obj.put("id", l.getId());
        obj.put("izena", l.getIzena());
        obj.put("erabiltzailea", l.getErabiltzailea());
        obj.put("pasahitza", l.getPasahitza());
        obj.put("aktibo", l.getAktibo());
        obj.put("erregistroData", l.getErregistroData());
        obj.put("rolaId", l.getRolaId());
        return obj;
    }
}
package erronkon;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlateraDAO {

    public static final String BASE_URL = "https://localhost:7236/api/Platerak";

    public static List<Platera> getAll() {
        List<Platera> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    lista.add(jsonToPlatera(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean create(Platera p) {
        try {
            JSONObject obj = plateraToJson(p);
            HttpResponse<String> response = ApiService.post(BASE_URL, obj.toString());
            return response.statusCode() == 201 || response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Platera p) {
        try {
            JSONObject obj = plateraToJson(p);
            String url = BASE_URL + "/" + p.getId();
            HttpResponse<String> response = ApiService.put(url, obj.toString());
            return response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try {
            String url = BASE_URL + "/" + id;
            HttpResponse<String> response = ApiService.delete(url);
            return response.statusCode() == 200 || response.statusCode() == 204;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método robusto que maneja diferentes formatos de nombres
    private static Platera jsonToPlatera(JSONObject obj) {
        Platera p = new Platera();

        // Intenta obtener los valores en diferentes formatos
        p.setId(getIntFromJson(obj, "id", "Id"));
        p.setIzena(getStringFromJson(obj, "izena", "Izena", ""));
        p.setDeskribapena(getStringFromJson(obj, "deskribapena", "Deskribapena", ""));
        p.setPrezioa(getDoubleFromJson(obj, "prezioa", "Prezioa"));
        p.setKategoriaId(getIntFromJson(obj, "kategoriaId", "KategoriaId", "kategoria_id", 0));
        p.setErabilgarri(getStringFromJson(obj, "erabilgarri", "Erabilgarri", "Bai"));
        p.setSortzeData(getStringFromJson(obj, "sortzeData", "SortzeData", "sortze_data", ""));
        p.setIrudia(getStringFromJson(obj, "irudia", "Irudia", ""));

        return p;
    }

    // Métodos auxiliares para manejar múltiples formatos
    private static int getIntFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) {
                return obj.getInt(key);
            }
        }
        return 0;
    }

    private static int getIntFromJson(JSONObject obj, String key1, String key2, String key3, int defaultValue) {
        if (obj.has(key1)) return obj.getInt(key1);
        if (obj.has(key2)) return obj.getInt(key2);
        if (obj.has(key3)) return obj.getInt(key3);
        return defaultValue;
    }

    private static String getStringFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) {
                return obj.getString(key);
            }
        }
        return "";
    }

    private static String getStringFromJson(JSONObject obj, String key1, String key2, String key3, String defaultValue) {
        if (obj.has(key1)) return obj.getString(key1);
        if (obj.has(key2)) return obj.getString(key2);
        if (obj.has(key3)) return obj.getString(key3);
        return defaultValue;
    }

    private static double getDoubleFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) {
                return obj.getDouble(key);
            }
        }
        return 0.0;
    }

    // Convierte Platera a JSON con PascalCase (formato .NET)
    private static JSONObject plateraToJson(Platera p) {
        JSONObject obj = new JSONObject();
        obj.put("Id", p.getId());
        obj.put("Izena", p.getIzena());
        obj.put("Deskribapena", p.getDeskribapena());
        obj.put("Prezioa", p.getPrezioa());
        obj.put("KategoriaId", p.getKategoriaId());
        obj.put("Erabilgarri", p.getErabilgarri());
        obj.put("SortzeData", p.getSortzeData());
        obj.put("Irudia", p.getIrudia());
        return obj;
    }
}
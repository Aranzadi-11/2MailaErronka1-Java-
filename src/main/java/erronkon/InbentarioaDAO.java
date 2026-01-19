package erronkon;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class InbentarioaDAO {

    public static final String BASE_URL = "https://localhost:7236/api/Inbentarioa";

    public static List<Inbentarioa> getAll() {
        List<Inbentarioa> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    lista.add(jsonToInbentarioa(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean create(Inbentarioa i) {
        try {
            JSONObject obj = inbentarioaToJson(i);
            HttpResponse<String> response = ApiService.post(BASE_URL, obj.toString());
            return response.statusCode() == 201 || response.statusCode() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Inbentarioa i) {
        try {
            JSONObject obj = inbentarioaToJson(i);
            String url = BASE_URL + "/" + i.getId();
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

    private static Inbentarioa jsonToInbentarioa(JSONObject obj) {
        Inbentarioa i = new Inbentarioa();
        i.setId(getIntFromJson(obj, "id", "Id"));
        i.setIzena(getStringFromJson(obj, "izena", "Izena", ""));
        i.setDeskribapena(getStringFromJson(obj, "deskribapena", "Deskribapena", ""));
        i.setKantitatea(getIntFromJson(obj, "kantitatea", "Kantitatea"));
        i.setNeurriaUnitatea(getStringFromJson(obj, "neurriaUnitatea", "NeurriaUnitatea", ""));
        i.setStockMinimoa(getIntFromJson(obj, "stockMinimoa", "StockMinimoa"));
        i.setAzkenEguneratzea(getStringFromJson(obj, "azkenEguneratzea", "AzkenEguneratzea", ""));
        return i;
    }

    private static JSONObject inbentarioaToJson(Inbentarioa i) {
        JSONObject obj = new JSONObject();
        obj.put("Id", i.getId());
        obj.put("Izena", i.getIzena());
        obj.put("Deskribapena", i.getDeskribapena());
        obj.put("Kantitatea", i.getKantitatea());
        obj.put("NeurriaUnitatea", i.getNeurriaUnitatea());
        obj.put("StockMinimoa", i.getStockMinimoa());
        obj.put("AzkenEguneratzea", i.getAzkenEguneratzea());
        return obj;
    }

    private static int getIntFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) return obj.getInt(key);
        }
        return 0;
    }

    private static String getStringFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) return obj.getString(key);
        }
        return "";
    }
}
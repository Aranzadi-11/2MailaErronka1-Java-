package erronkon;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FinantzaDAO {

    public static final String BASE_URL = "https://localhost:7236/api/Zerbitzuak";

    public static List<Finantza> getAll() {
        List<Finantza> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    lista.add(jsonToFinantza(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    private static Finantza jsonToFinantza(JSONObject obj) {
        Finantza f = new Finantza();
        f.setId(getIntFromJson(obj, "id", "Id"));
        f.setBezeroaId(getIntFromJson(obj, "bezeroaId", "BezeroaId"));
        f.setLangileaId(getIntFromJson(obj, "langileaId", "LangileaId"));
        f.setPlateraId(getIntFromJson(obj, "plateraId", "PlateraId"));
        f.setEskaeraData(getStringFromJson(obj, "eskaeraData", "EskaeraData", ""));
        f.setEgoera(getStringFromJson(obj, "egoera", "Egoera", ""));
        f.setGuztira(getDoubleFromJson(obj, "guztira", "Guztira"));
        return f;
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

    private static double getDoubleFromJson(JSONObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key)) return obj.getDouble(key);
        }
        return 0.0;
    }
}
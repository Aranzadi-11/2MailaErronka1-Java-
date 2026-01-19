package erronkon;

import java.net.http.HttpResponse;
import org.json.JSONObject;

public class KaxaDAO {


    public static final String BASE_URL = "https://localhost:7236/api/JatetxekoInfo";

    public static Kaxa getKaxa() {
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONObject obj = new JSONObject(response.body());
                return jsonToKaxa(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Kaxa jsonToKaxa(JSONObject obj) {
        Kaxa k = new Kaxa();

        k.setKaxaGuztira(getDoubleFromJson(obj, "kaxaTotal", "kaxaGuztira", "KaxaGuztira"));


        k.setId(getIntFromJson(obj, "id", "Id"));
        k.setIzena(getStringFromJson(obj, "izena", "Izena", ""));
        k.setHelbidea(getStringFromJson(obj, "helbidea", "Helbidea", ""));
        k.setTelefonoa(getStringFromJson(obj, "telefonoa", "Telefonoa", ""));

        return k;
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
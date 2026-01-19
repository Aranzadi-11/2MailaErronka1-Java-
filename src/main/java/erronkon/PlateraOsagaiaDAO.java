package erronkon;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlateraOsagaiaDAO {

    public static final String BASE_URL = "https://localhost:7236/api/PlaterenOsagaiak";

    public static List<PlateraOsagaia> getAll() {
        List<PlateraOsagaia> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = ApiService.get(BASE_URL);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    PlateraOsagaia po = new PlateraOsagaia();
                    po.setPlateraId(obj.optInt("plateraId", 0));
                    po.setInbentarioaId(obj.optInt("inbentarioaId", 0));
                    po.setKantitatea(obj.optInt("kantitatea", 0));
                    lista.add(po);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<PlateraOsagaia> getByPlateraId(int plateraId) {
        List<PlateraOsagaia> lista = new ArrayList<>();
        try {
            String url = BASE_URL + "/platera/" + plateraId;
            HttpResponse<String> response = ApiService.get(url);
            if (response.statusCode() == 200) {
                JSONArray arr = new JSONArray(response.body());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    PlateraOsagaia po = new PlateraOsagaia();
                    po.setPlateraId(obj.optInt("plateraId", 0));
                    po.setInbentarioaId(obj.optInt("inbentarioaId", 0));
                    po.setKantitatea(obj.optInt("kantitatea", 0));
                    lista.add(po);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean create(PlateraOsagaia po) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("plateraId", po.getPlateraId());
            obj.put("inbentarioaId", po.getInbentarioaId());
            obj.put("kantitatea", po.getKantitatea());

            HttpResponse<String> response = ApiService.post(BASE_URL, obj.toString());
            return response.statusCode() == 201 || response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(PlateraOsagaia po) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("plateraId", po.getPlateraId());
            obj.put("inbentarioaId", po.getInbentarioaId());
            obj.put("kantitatea", po.getKantitatea());

            String url = BASE_URL + "/" + po.getPlateraId() + "/" + po.getInbentarioaId();
            HttpResponse<String> response = ApiService.put(url, obj.toString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int plateraId, int inbentarioaId) {
        try {
            String url = BASE_URL + "/" + plateraId + "/" + inbentarioaId;
            HttpResponse<String> response = ApiService.delete(url);
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SnackParser {

    public static List<Snack> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<Snack> snackList = new ArrayList<Snack>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Snack snack = new Snack();

                snack.setId(obj.getInt("id"));
                snack.setSodaId(obj.getInt("soda_id"));
                snack.setNombre(obj.getString("nombre"));
                snack.setPrecio(obj.getString("precio"));

                snackList.add(snack);
            }
            return snackList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

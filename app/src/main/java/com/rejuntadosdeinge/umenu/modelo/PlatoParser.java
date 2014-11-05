package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlatoParser {

    public static List<Plato> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<Plato> platoList = new ArrayList<Plato>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Plato plato = new Plato();

                plato.setId(obj.getInt("id"));
                plato.setSodaId(obj.getInt("soda_id"));
                plato.setNombre(obj.getString("nombre"));
                plato.setPrecio(obj.getString("precio"));
                plato.setCategoria(obj.getString("categoria"));
                plato.setTipo(obj.getString("tipo"));
                plato.setCalificaciones(obj.getInt("calificaciones"));
                plato.setTotal(obj.getInt("total"));
                plato.setPromedio(obj.getInt("promedio"));

                platoList.add(plato);
            }
            return platoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

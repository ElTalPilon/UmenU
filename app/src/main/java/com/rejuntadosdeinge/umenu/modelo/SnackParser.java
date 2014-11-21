package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnackParser {

    public static List<Snack> parseFeed(String content) {

        try {
            // Pasamos formato JSON a JSONArray
            JSONArrayImpl ar = (JSONArrayImpl) new JSONArrayImpl(content);
            Iterator JAI = ar.iterator();
            // Se crea una lista donde van a estar los snacks obtenidos
            List<Snack> snackList = new ArrayList<Snack>();

            // la clase JSONArray no implementaba la interface Iterator
            while(JAI.hasNext()){

                // obtenemos una referencia al JSONObject actual
                JSONObject obj = (JSONObject) JAI.next();

                Snack snack = new Snack();

                // llenamos la instancia con los setters de Snack y se van metiendo en la lista
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


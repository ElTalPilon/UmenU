package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    PlatoParser se encarga de leer un String en formato JSON y retornar los campos ordenados en un List
*/
public class PlatoParser {

    public static List<Plato> parseFeed(String content) {

        try {
            // Pasamos formato JSON a JSONArray
            JSONArrayImpl ar = (JSONArrayImpl) new JSONArrayImpl(content);
            Iterator JAI = ar.iterator();
            // Se crea una lista donde van a estar los platos obtenidos
            List<Plato> platoList = new ArrayList<Plato>();

            // la clase JSONArray no implementa la interface Iterator
            while(JAI.hasNext()){

                // obtenemos una referencia al JSONObject actual
                JSONObject obj = (JSONObject) JAI.next();
				
                Plato plato = new Plato();
                // llenamos la instancia con los setters de Plato y se van metiendo en la lista
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
            // retorna la lista de platos obtenida
            return platoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

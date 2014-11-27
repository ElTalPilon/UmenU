package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    Acompanamiento Parser se encarga de leer un String en formato JSON y retornar los campos ordenados en un List
*/
public class AcompanamientoParser {

    public static List<Acompanamiento> parseFeed(String content) {

        try {
            // Pasamos formato JSON a JSONArray
            JSONArrayImpl ar = (JSONArrayImpl) new JSONArrayImpl(content);
            Iterator JAI = ar.iterator();
            // Se crea una lista donde van a estar los platos obtenidos
            List<Acompanamiento> acompanamientoList = new ArrayList<Acompanamiento>();

            // la clase JSONArray no implementa la interface Iterator
            while(JAI.hasNext()){

                // obtenemos una referencia al JSONObject actual
                JSONObject obj = (JSONObject) JAI.next();

                Acompanamiento ac = new Acompanamiento();
                // llenamos la instancia con los setters de Acompanamiento y se van metiendo en la lista
                ac.setId(obj.getInt("id"));
                ac.setAcompanamiento(obj.getString("acompanamiento"));
                ac.setGuarnicion(obj.getString("guarnicion"));
                ac.setEnsalada1(obj.getString("ensalada1"));
                ac.setEnsalada2(obj.getString("ensalada2"));
                ac.setEnsalada3(obj.getString("ensalada3"));
                ac.setEnsalada4(obj.getString("ensalada4"));
                ac.setEnsalada5(obj.getString("ensalada5"));
                ac.setEnsalada6(obj.getString("ensalada6"));
                ac.setFruta1(obj.getString("fruta1"));
                ac.setFruta2(obj.getString("fruta2"));
                ac.setFresco1(obj.getString("fresco1"));
                ac.setFresco2(obj.getString("fresco2"));
                ac.setFrescoSinAzucar(obj.getString("frescosinazucar"));

                acompanamientoList.add(ac);
            }
            // retorna la lista de platos obtenida
            return acompanamientoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}


package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComentarioParser {

    public static List<Comentario> parseFeed(String content) {

        try {
            // Pasamos formato JSON a JSONArray
            JSONArrayImpl ar = (JSONArrayImpl) new JSONArrayImpl(content);
            Iterator JAI = ar.iterator();
            // Se crea una lista donde van a estar los platos obtenidos
            List<Comentario> comentarioList = new ArrayList<Comentario>();

            // la clase JSONArray no implementaba la interface Iterator
            while(JAI.hasNext()){

                // obtenemos una referencia al JSONObject actual
                JSONObject obj = (JSONObject) JAI.next();

                Comentario comentario = new Comentario();

                // llenamos la instancia con los setters de Comentario y se van metiendo en la lista
                comentario.setId(obj.getInt("id"));
                comentario.setUsuarioId(obj.getInt("usuario_id"));
                comentario.setPlatoId(obj.getInt("plato_id"));
                comentario.setComentario(obj.getString("comentario"));
                comentario.setPuntos(obj.getInt("puntos"));

                comentarioList.add(comentario);
            }
            return comentarioList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}


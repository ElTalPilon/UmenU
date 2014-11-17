package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComentarioParser {

    public static List<Comentario> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<Comentario> comentarioList = new ArrayList<Comentario>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Comentario comentario = new Comentario();

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

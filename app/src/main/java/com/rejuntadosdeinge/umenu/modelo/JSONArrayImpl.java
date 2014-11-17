package com.rejuntadosdeinge.umenu.modelo;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Hector on 15/11/2014.
 */
public class JSONArrayImpl extends JSONArray {

    JSONArray ar;

    public JSONArrayImpl(String content) throws JSONException {
        super(content);
        ar = new JSONArray(content);
    }

    public JSONArrayIterator iterator(){
        return new JSONArrayIterator(this.ar);
    }

}

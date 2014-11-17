package com.rejuntadosdeinge.umenu.modelo;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;

public class JSONArrayIterator implements Iterator {

    JSONArray items;
    int position = 0;

    public JSONArrayIterator(JSONArray items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        if(position >= items.length() || items.isNull(position)) {
            return false;
        }
        return true;
    }

    @Override
    public Object next() {
        Object item = null;
        try {
            item = items.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        position++;
        return item;
    }

    @Override
    public void remove() {
    }
}

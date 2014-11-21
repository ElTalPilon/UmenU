package com.rejuntadosdeinge.umenu.modelo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rejuntadosdeinge.umenu.R;

import java.util.List;

/**
 * Created by Hector on 19/11/2014.
 */
public class SnackAdapter extends ArrayAdapter<Snack> {

    private Context context;
    private List<Snack> obj;

    public SnackAdapter(Context context, int resource, List<Snack> obj) {
        super(context, resource, obj);
        this.context = context;
        this.obj = obj;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // identificamos cual dato es el que debe mostrar
        Snack snack = obj.get(position);

        // crea objeto view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_snack, null);

        TextView tv_nombre = (TextView) view.findViewById(R.id.nombre_snack);
        tv_nombre.setText(snack.getNombre());

        TextView tv_precio = (TextView) view.findViewById(R.id.precio_snack);
        tv_precio.setText(snack.getPrecio());

        return view;
    }
}

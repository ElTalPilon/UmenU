package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuSoda extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_menu_soda);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // Setea el título de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e) {
            Log.e("ListaPlatos", "No se pudo cambiar el título de la Activity");
        }

        // toma datos y los pasa a la lista

        // Lista de sodas (quemada porque no variará mucho con el tiempo y no se quiere
        // estar haciendo una consulta cada vez que se abre la actividad principal).
        String[] listaMenu = {
                "Platos",
                "Ensaladas",
                "Refrescos",
                "Snacks"
        };

        // Se le conecta un adapter personalizado para cargar las imágenes
        MenuAdapter menu_adapter = new MenuAdapter(this, listaMenu);
        final ListView listView = (ListView) this.findViewById(R.id.menu_soda);
        listView.setAdapter(menu_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    irPlatos();
                }

                if(i == 3) {
                    irSnacks();
                }


            }
        });
    }

    public void irPlatos() {
        Intent intent = new Intent(this, ListaPlatos.class);
        startActivity(intent);
    }

    public void irSnacks() {
        Intent intent = new Intent(this, ListaSnacks.class);
        startActivity(intent);
    }


    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_soda, menu);
        return true;
    }

    /**
     * Llamado cuando se presiona uno de los elementos del Action Bar.
     * En este caso, sólo se puede presionar "Detalles de la Soda"
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.ir_detalle_soda){
            Intent intent = new Intent(this, DetallesSoda.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adaptador personalizado para cargar tanto los nombres como las imagenes de las sodas
     */
    private class MenuAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MenuAdapter(Context context, String[] values) {
            super(context, R.layout.item_soda, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_menu, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.nombre_menu);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imagen_menu);
            textView.setText(values[position]);
            switch(position) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_plato);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ic_ensalada);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ic_fresco);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_snack);
                    break;

            }
            return rowView;
        }
    }
}


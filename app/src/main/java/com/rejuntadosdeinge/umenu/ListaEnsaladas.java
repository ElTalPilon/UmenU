package com.rejuntadosdeinge.umenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaEnsaladas extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lista_ensalada);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // Setea el título de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e) {
            Log.e("ListaSnacks", "No se pudo cambiar el título de la Activity");
        }

        String ensalada1 = pref.getString("ensalada1", null);
        String ensalada2 = pref.getString("ensalada2", null);
        String ensalada3 = pref.getString("ensalada3", null);
        String ensalada4 = pref.getString("ensalada4", null);
        String ensalada5 = pref.getString("ensalada5", null);
        String ensalada6 = pref.getString("ensalada6", null);

        String[] ensaladas = {ensalada1,ensalada2,ensalada3,ensalada4,ensalada5,ensalada6};

        List<String> listaEnsaladasProvisional = new ArrayList<String>(Arrays.asList(ensaladas));

        ArrayAdapter<String> listaEnsaladasAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_ensalada,
                R.id.nombre_ensalada,
                listaEnsaladasProvisional);

        ListView listView = (ListView) this.findViewById(R.id.lista_ensaladas);
        listView.setAdapter(listaEnsaladasAdapter);
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_ensaladas, menu);
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

}

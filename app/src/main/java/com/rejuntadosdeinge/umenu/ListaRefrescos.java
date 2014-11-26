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

public class ListaRefrescos extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lista_refrescos);

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

        String fresco1 = "Fresco 1: " + pref.getString("fresco1", null);
        String fresco2 = "Fresco 2: " + pref.getString("fresco2", null);
        String frescosinazucar = "Fresco sin azúcar: " + pref.getString("frescosinazucar", null);

        String[] refrescos = {fresco1,fresco2,frescosinazucar};

        List<String> listaFrescosProvisional = new ArrayList<String>(Arrays.asList(refrescos));

        ArrayAdapter<String> listaFrescosAdapter = new ArrayAdapter<String>(
                this,
                R.layout.item_refresco,
                R.id.nombre_fresco,
                listaFrescosProvisional);

        ListView listView = (ListView) this.findViewById(R.id.lista_frescos);
        listView.setAdapter(listaFrescosAdapter);
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_refrescos, menu);
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


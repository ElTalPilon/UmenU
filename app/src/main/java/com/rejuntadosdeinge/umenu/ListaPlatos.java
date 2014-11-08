package com.rejuntadosdeinge.umenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListaPlatos extends ActionBarActivity {

    // SharedPreferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        // Setea el nombre de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e){
            Log.e("ListaPlatos", "No se pudo cambiar el título de la Activity");
        }

        // Lista de platos (sólo se cuenta con 3 platos por soda)
        String[] platosArray = {
                "Plato Básico 1",
                "Plato Básico 2",
                "Plato Vegetariano"
        };

        //TODO: Cargar los nombres de los platos

        List<String> listaPlatosProvisional = new ArrayList<String>(Arrays.asList(platosArray));
        ArrayAdapter<String> listaPlatosAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.list_item_textview,
                listaPlatosProvisional);

        final ListView listView = (ListView) this.findViewById(R.id.lista_platos);
        listView.setAdapter(listaPlatosAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToDetallesPlato(i, (String)(listView.getItemAtPosition(i)));
            }
        });
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_platos, menu);
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
     * Llamado cuando se presiona el botón de Snacks
     */
    public void goToListaSnacks(View view){
        Intent intent = new Intent(this, ListaSnacks.class);
        startActivity(intent);
    }

    /**
     * Llamado cuando se presiona uno de los platos de la lista.
     */
    public void goToDetallesPlato(int categoria, String nombrePlato){
        // TODO: En vez de la categoría, podría pasarse el ID del plato -> editor.putInt("IDPlato", 56);
        switch(categoria){
            case 0:
                editor.putString("categoriaPlato", "B%C3%A1sico%201");
                break;
            case 1:
                editor.putString("categoriaPlato", "B%C3%A1sico%202");
                break;
            case 2:
                editor.putString("categoriaPlato", "Vegetariano");
                break;
        }
        editor.putString("nombrePlato", nombrePlato);
        editor.commit();

        Intent intent = new Intent(this, DetallesPlato.class);
        startActivity(intent);
    }
}


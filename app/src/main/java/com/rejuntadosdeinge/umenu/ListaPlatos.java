package com.rejuntadosdeinge.umenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    int idSoda = 0;
    String sodaElegida = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        Intent intent = getIntent();
        if(intent.hasExtra("sodaElegida") && intent.hasExtra("sodaId")){
            idSoda = intent.getIntExtra("sodaId", 0);
            sodaElegida = intent.getStringExtra("sodaElegida");
            getActionBar().setTitle(sodaElegida);
        }

        // Dummy data for the ListView.
        String[] platosArray = {
                "Plato Básico 1",
                "Plato Básico 2",
                "Plato Vegetariano"
        };

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
                goToDetallesPlato(i);//view, (String)(listView.getItemAtPosition(i)));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_platos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.ir_detalle_soda:
                goToDetallesSoda((ListView) this.findViewById(R.id.lista_platos));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks Snacks
     */
    public void goToListaSnacks(View view){
        Intent intent = new Intent(this, ListaSnacks.class);
        intent.putExtra("idSoda", idSoda);
        startActivity(intent);
    }

    /**
     * Called when the user clicks any dish item
     */
    public void goToDetallesPlato(int categoria){//View view, String platoElegido){
        Intent intent = new Intent(this, DetallesPlato.class);
        intent.putExtra("idSoda", idSoda);
        intent.putExtra("sodaElegida", sodaElegida); // Pasa el nombre de la soda elegida
        intent.putExtra("categoria", categoria);    // Pasa el plato
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Soda Detail button
     * @param view
     */
    public void goToDetallesSoda(View view){
        Intent intent = new Intent(this, DetallesSoda.class);
        intent.putExtra("idSoda", idSoda);
        intent.putExtra("sodaElegida", sodaElegida); // Pasa el nombre de la soda elegida
        startActivity(intent);
    }
}


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

    int idSoda;
    String nombreSoda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        Intent intent = getIntent();
        idSoda = intent.getIntExtra("sodaId", 0);
        nombreSoda = intent.getStringExtra("sodaElegida");
        if(intent.hasExtra("sodaElegida")){
            getActionBar().setTitle(intent.getStringExtra("sodaElegida"));
        }

        // Dummy data for the ListView.
        String[] platosArray = {
                "Plato Especial",
                "Plato Básico A",
                "Plato Básico B",
                "Plato Vegetariano"
        };

        List<String> listaPlatosProvisional = new ArrayList<String>(Arrays.asList(platosArray));

        ArrayAdapter<String> listaPlatosAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.list_item_textview,
                listaPlatosProvisional);

        ListView listView = (ListView) this.findViewById(R.id.lista_platos);
        listView.setAdapter(listaPlatosAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToDetallesPlato(view);
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
    public void goToDetallesPlato(View view){
        Intent intent = new Intent(this, DetallesPlato.class);
        intent.putExtra("idSoda", idSoda);
        intent.putExtra("nombreSoda", nombreSoda);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Soda Detail button
     * @param view
     */
    public void goToDetallesSoda(View view){
        Intent intent = new Intent(this, DetallesSoda.class);
        intent.putExtra("idSoda", idSoda);
        startActivity(intent);
    }
}


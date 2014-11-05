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

    Globals g = Globals.getInstance();
    int idSoda;
    String sodaElegida = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        idSoda = g.getIdSoda();
        sodaElegida = g.getSodaElegida();
        getActionBar().setTitle(sodaElegida);

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
                goToDetallesPlato(i);
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
        startActivity(intent);
    }

    /**
     * Called when the user clicks any dish item
     */
    public void goToDetallesPlato(int categoria){//View view, String platoElegido){

        if(categoria == 0)
            g.setCategoria("B%C3%A1sico%201");
        else
            if(categoria == 1)
                g.setCategoria("B%C3%A1sico%202");
            else
                if(categoria == 2)
                    g.setCategoria("Vegetariano");

        Intent intent = new Intent(this, DetallesPlato.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Soda Detail button
     * @param view
     */
    public void goToDetallesSoda(View view){
        Intent intent = new Intent(this, DetallesSoda.class);
        startActivity(intent);
    }
}


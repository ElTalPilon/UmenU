package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaSodas extends ActionBarActivity {
    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sodas);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        // Lista de sodas (hard coded porque no variará mucho con el tiempo y no se quiere
        // estar haciendo una consulta cada vez que se abre la actividad principal).
        String[] sodasArray = {
                "Facultad de Odontología",
                "Facultad de Derecho",
                "Facultad de Agroalimentarias",
                "Ciencias Económicas",
                "Estudios Generales",
                "Facultad de Educación",
                "Ciencias Sociales",
                "Comedor Universitario"
        };

        List<String> listaSodasProvisional = new ArrayList<String>(Arrays.asList(sodasArray));
        ArrayAdapter<String> listaSodasAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.list_item_textview,
                listaSodasProvisional);

        final ListView listView = (ListView) this.findViewById(R.id.lista_sodas);
        listView.setAdapter(listaSodasAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToListaPlatos(i, (String)(listView.getItemAtPosition(i)));
            }
        });
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_sodas, menu);
        return true;
    }

    /**
     * Llamado cuando se presiona uno de los elementos del Action Bar
     * En este caso, sólo se puede presionar "Sugerir Plato".
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ir_sugerir_plato) {
            popUpSugerenciaPlato();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Llamado cuando el usuario elige una de las sodas de la lista.
     * Inicializa la actividad "ListaPlatos"
     * @param IDSoda - El ID de la soda elegida
     * @param nombreSoda - El nombre de la soda elegida
     */
    public void goToListaPlatos(int IDSoda, String nombreSoda){
        editor.putInt("IDSoda", IDSoda+1); // Se le suma 1 puesto que en la BD los ID empiezan desde 1
        editor.putString("nombreSoda", nombreSoda);
        editor.commit();                   // Se guardan los cambios en Shared Preferences
        Intent intent = new Intent(this, ListaPlatos.class);
        startActivity(intent);
    }

    /**
     * Llamado cuando el usuario presiona el botón de "Sugerir Plato".
     * Inicializa la actividad "SugerenciaPlato".
     */
    public void popUpSugerenciaPlato( ){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_sugerencia_plato);
        dialog.setTitle(R.string.title_fragment_sugerencia_plato);
        // TODO: Cargar el plato más gustado del día desde la BD
        dialog.show();

        Button b = (Button) dialog.findViewById((R.id.ir_a_detalle_plato));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetallesPlato.class);
                // TODO: editor.putInd("IDPlato", idPlato);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }
}
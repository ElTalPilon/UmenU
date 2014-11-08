package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Plato;
import com.rejuntadosdeinge.umenu.modelo.PlatoParser;
import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListaPlatos extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Lista de platos (sólo se cuenta con 3 platos por soda)
    String[] platosArray = new String[3];

    ProgressBar pb;
    List<Plato> platoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        // Inicializa las SharedPreferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        // Setea el nombre de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e){
            Log.e("ListaPlatos", "No se pudo cambiar el título de la Activity");
        }


        pb = (ProgressBar) findViewById( R.id.progressBarListaPlatos);
        pb.setVisibility(View.INVISIBLE);

        if (isOnline()) {

            Toast.makeText(this, "Id soda: "+ String.valueOf(pref.getInt("IDSoda", 0))
                    + " semana: "+ String.valueOf(pref.getInt("semana", 0))
                    + " dia: "+ String.valueOf(pref.getInt("dia", 0)), Toast.LENGTH_LONG).show();
            //requestData("http://limitless-river-6258.herokuapp.com/platos?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0))
            //        + "&semana=" + String.valueOf(pref.getInt("semana", 0))
            //        + "&dia=" + String.valueOf(pref.getInt("dia", 0)));
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("soda_id", String.valueOf(pref.getInt("IDSoda", 0)));
        p.setParam("semana", String.valueOf(pref.getInt("semana", 0)));
        p.setParam("dia", String.valueOf(pref.getInt("dia", 0)));

        MyTask task = new MyTask();
        task.execute(p);
    }

    protected void updateDisplay() {

        if (platoList != null) {
            for (Plato plato : platoList) {

                Toast.makeText(this, plato.getCategoria().toString(), Toast.LENGTH_LONG).show();
                if(plato.getCategoria()=="Básico 1")
                    platosArray[0] = "Básico 1: "+ plato.getNombre().toString();
                if(plato.getCategoria()=="Básico 2")
                    platosArray[1] = "Básico 2: "+ plato.getNombre().toString();
                if(plato.getCategoria()=="Vegetariano")
                    platosArray[3] = "Vegetariano: "+ plato.getNombre().toString();
            }
            desplegarLista();
        }
    }

    public void desplegarLista() {

        //Cargar los nombres de los platos

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


    // para no bloquear el hilo principal la conexion la realiza otro hilo
    private class MyTask extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            platoList = PlatoParser.parseFeed(result);
            updateDisplay();
            pb.setVisibility(View.INVISIBLE);
        }
    }
}


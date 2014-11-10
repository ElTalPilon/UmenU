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

    //variables de uso comun entre varios metodos
    String soda_id;
    String semana;
    String dia;
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

        // se llenan las variables globales
        soda_id = String.valueOf(pref.getInt("IDSoda", 0));
        semana = String.valueOf(pref.getInt("semana", 0));
        dia = String.valueOf(pref.getInt("dia", 0));
        pb = (ProgressBar) findViewById( R.id.progressBarListaPlatos);
        pb.setVisibility(View.INVISIBLE);

        // si hay internet...
        if (isOnline()) {
            // se hace la consulta
            requestData("http://limitless-river-6258.herokuapp.com/platos?menu=1&soda_id=" + soda_id
                    + "&semana=" + semana
                    + "&dia=" + dia
                    + "&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Valida conexion disponiblea internet
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Se llenan parametros que utilizaremos en la consulta POST
     */
    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("menu", "1");
        p.setParam("soda_id", soda_id);
        p.setParam("semana", semana);
        p.setParam("dia", dia);

        MyTask task = new MyTask();
        task.execute(p);
    }

    /**
     * Se llena el listView con los datos obtenidos del web service
     */
    protected void updateDisplay() {

        String b1 = "";
        String b2 = "";
        String v = "";

        if (platoList != null) {
            for (Plato plato : platoList) {

                if(plato.getCategoria().toString().equals("Básico 1"))
                    b1 = "Básico 1: "+ plato.getNombre().toString();
                if(plato.getCategoria().toString().equals("Básico 2"))
                    b2 = "Básico 2: "+ plato.getNombre().toString();
                if(plato.getCategoria().toString().equals("Vegetariano"))
                    v = "Vegetariano: "+ plato.getNombre().toString();
            }

            // data for the ListView.
            String[] platosArray = { b1, b2, v };

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

    /**
     *  Para no bloquear el hilo principal la conexion la realiza otro hilo
     */
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


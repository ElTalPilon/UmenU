package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    // Variables globales
    List<Plato> platoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lista_platos);

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

        // Obtiene los platos de la BD
        MyTask myTask = new MyTask();
        myTask.execute();
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
     * Verifica que haya una conexión disponible
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

                if(plato.getCategoria().equals("Básico 1"))
                    b1 = plato.getNombre();
                if(plato.getCategoria().equals("Básico 2"))
                    b2 = plato.getNombre();
                if(plato.getCategoria().equals("Vegetariano"))
                    v = plato.getNombre();
            }

            // data for the ListView.
            String[] platosArray = { b1, b2, v };

            //Cargar los nombres de los platos

            List<String> listaPlatosProvisional = new ArrayList<String>(Arrays.asList(platosArray));
            ArrayAdapter<String> listaPlatosAdapter = new ArrayAdapter<String>(
                    this,
                    R.layout.item_comida,
                    R.id.nombre_comida,
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
     * Llamado cuando se presiona el botón de Snacks
     */
    public void goToListaSnacks(View view){
        // TODO: Ya que más adelante desplegaremos ensaladas, frescos, etc., esto podría ponerse ahí mismo
        Intent intent = new Intent(this, ListaSnacks.class);
        startActivity(intent);
    }

    /**
     * Llamado cuando se presiona uno de los platos de la lista.
     */
    public void goToDetallesPlato(int categoria, String nombrePlato){
        // TODO: En vez de la categoría, podría pasarse el ID del plato
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
     *  Realiza las consultas a la BD sobre los platos de la soda
     */
    private class MyTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String JSON = "";

            if(isOnline()){
                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                p.setUri("http://limitless-river-6258.herokuapp.com/platos?menu=1&soda_id=" + pref.getInt("IDSoda", 0)
                        + "&semana=" + pref.getInt("semana", 0)
                        + "&dia=" + pref.getInt("dia", 0)
                        + "&get=1");
                JSON = HttpManager.getData(p);

                Log.d("Consulta", p.getUri());
                Log.d("Resultado", JSON);
            }
            else{
                Toast.makeText(getBaseContext(), "Red no disponible", Toast.LENGTH_LONG).show();
            }
            return JSON;
        }

        @Override
        protected void onPostExecute(String result) {
            platoList = PlatoParser.parseFeed(result);
            updateDisplay();
            setProgressBarIndeterminateVisibility(false);
        }
    }
}


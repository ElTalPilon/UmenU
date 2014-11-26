package com.rejuntadosdeinge.umenu;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;
import com.rejuntadosdeinge.umenu.modelo.Snack;
import com.rejuntadosdeinge.umenu.modelo.SnackParser;

import java.util.List;



public class ListaSnacks extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Variable global
    List<Snack> snackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lista_snacks);

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

        // Obtiene los snacks de la BD
        SnacksTask snacksTask = new SnacksTask();
        snacksTask.execute();
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_snacks, menu);
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


    protected void updateSnacks() {

        // toma datos y los pasa a la lista
        SnackAdapter adapter = new SnackAdapter(this, R.layout.item_snack, snackList);
        final ListView listView = (ListView) this.findViewById(R.id.lista_snacks);
        listView.setAdapter(adapter);
    }
/*

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
*/

    /**
     *  Realiza las consultas a la BD sobre los snacks de la soda
     */
    private class SnacksTask extends AsyncTask<Void, String, String> {

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
                p.setUri("http://limitless-river-6258.herokuapp.com/snacks?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0))
                        +"&get=1");
                JSON = HttpManager.getData(p);

                Log.d("Resultado", JSON);
            }
            else{
                Toast.makeText(getBaseContext(), "Red no disponible", Toast.LENGTH_LONG).show();
            }
            return JSON;
        }

        @Override
        protected void onPostExecute(String result) {
            snackList = SnackParser.parseFeed(result);
            updateSnacks();
            setProgressBarIndeterminateVisibility(false);
        }
    }

    /**
     * clase anónima
     * Adaptador personalizado para cargar los snacks y sus precios
     */
    public class SnackAdapter extends ArrayAdapter<Snack> {

        private Context context;
        private List<Snack> obj;

        public SnackAdapter(Context context, int resource, List<Snack> obj) {
            super(context, resource, obj);
            this.context = context;
            this.obj = obj;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // identificamos cual dato es el que debe mostrar
            Snack snack = obj.get(position);

            // crea objeto view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.item_snack, null);

            TextView tv_nombre = (TextView) view.findViewById(R.id.nombre_snack);
            tv_nombre.setText(snack.getNombre());

            TextView tv_precio = (TextView) view.findViewById(R.id.precio_snack);
            tv_precio.setText(snack.getPrecio());

            return view;
        }
    }
}


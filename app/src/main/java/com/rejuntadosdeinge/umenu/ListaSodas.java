package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListaSodas extends ActionBarActivity {
    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Dialogo de sugerencia
    Dialog dialog;

    int semana;
    int dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sodas);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // se carga el valor para el día y la semana en la actividad principal
        semana = 101;
        dia = 1;
        editor.putInt("semana", semana);
        editor.putInt("dia", dia);

        // Lista de sodas (hard coded porque no variará mucho con el tiempo y no se quiere
        // estar haciendo una consulta cada vez que se abre la actividad principal).
        String[] sodasArray = {
                "Facultad de Odontología",
                "Facultad de Derecho",
                "Ciencias Económicas",
                "Facultad de Agroalimentarias",
                "Estudios Generales",
                "Facultad de Educación",
                "Ciencias Sociales",
                "Comedor Universitario"
        };

        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, sodasArray);

        final ListView listView = (ListView) this.findViewById(R.id.lista_sodas);
        listView.setAdapter(customArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToListaPlatos(i, (String) (listView.getItemAtPosition(i)));
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
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.fragment_sugerencia_plato);
            dialog.show();

            //MyTask myTask = new MyTask();
            //myTask.execute();
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
     */
    public void popUpSugerenciaPlato(final String[] results){
        /*
        TextView nombreP = (TextView)dialog.findViewById(R.id.nombre_plato_popup);
        TextView precioP = (TextView)dialog.findViewById(R.id.precio_plato_popup);
        TextView sodaP = (TextView)dialog.findViewById(R.id.nombre_soda_popup);

        //nombreP.setText(results[0]);
        //precioP.setText(results[1]);
        //sodaP.setText(results[2]);
        */

        Button b = (Button) dialog.findViewById((R.id.ir_a_detalle_plato));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetallesPlato.class);
                editor.putInt("IDPlato", Integer.parseInt(results[3]));
                editor.commit();
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private class CustomArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CustomArrayAdapter(Context context, String[] values) {
            super(context, R.layout.list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.nombre_soda);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imagen_soda);
            textView.setText(values[position]);
            switch(position) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_odonto);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ic_derecho);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ic_economicas);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_agro);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ic_generales);
                    break;
                case 5:
                    imageView.setImageResource(R.drawable.ic_educacion);
                    break;
                case 6:
                    imageView.setImageResource(R.drawable.ic_sociales);
                    break;
                case 7:
                    imageView.setImageResource(R.drawable.ic_comedor);
                    break;
            }
            return rowView;
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class MyTask extends AsyncTask<String, String, String[]> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] resultados = new String[4];
            String JSON;
            if(isOnline()){
                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                // TODO: Cambiar los parametros de semana y día
                p.setUri("https://limitless-river-6258.herokuapp.com/platos?semana=1&dia=1&best=1");
                JSON = HttpManager.getData(p);

                try{
                    JSONArray arr = new JSONArray(JSON);
                    JSONObject obj = arr.getJSONObject(0);
                    resultados[0] = obj.getString("nombre");
                    resultados[1] = obj.getString("precio");
                    resultados[3] = String.valueOf(obj.getInt("id"));
                    int sodaIDPlato = obj.getInt("soda_id");

                    p = new RequestPackage();
                    p.setMethod("POST");
                    p.setUri("https://limitless-river-6258.herokuapp.com/sodas?" + sodaIDPlato);
                    p.setParam("id", "" + sodaIDPlato);
                    JSON = HttpManager.getData(p);

                    try{
                        arr = new JSONArray(JSON);
                        obj = arr.getJSONObject(0);
                        resultados[2] = obj.getString("nombre");
                    } catch(JSONException e){
                        Log.e("JSONException", "Error manejando el JSON para la soda");
                    }

                } catch(JSONException e){
                    Log.e("JSONException", "Error manejando el JSON para el popup");
                }
            }
            return resultados;
        }

        @Override
        protected void onPostExecute(String[] result) {
            popUpSugerenciaPlato(result);
        }
    }
}
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
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ListaSodas extends ActionBarActivity {
    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Dialogo de sugerencia, para accederlo desde varios hilos.
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_lista_sodas);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // Carga el valor del día y semana
        calcularDiaYSemana();

        // Lista de sodas (quemada porque no variará mucho con el tiempo y no se quiere
        // estar haciendo una consulta cada vez que se abre la actividad principal).
        String[] listaDeSodas = {
                "Facultad de Odontología",
                "Facultad de Derecho",
                "Ciencias Económicas",
                "Facultad de Agroalimentarias",
                "Estudios Generales",
                "Facultad de Educación",
                "Ciencias Sociales",
                "Comedor Universitario"
        };

        // Se le conecta un adapter personalizado para cargar las imágenes de cada soda
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, listaDeSodas);
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
            // Muestra el PopUp de la Sugerencia del Día
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.fragment_sugerencia_plato);
            dialog.show();

            // Jala el plato del día de la BD
            MyTask myTask = new MyTask();
            myTask.execute();
        }
        if(item.getItemId() == R.id.salir){
            editor.putBoolean("loggeado", false);
            editor.commit();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calcula el día y la semana en la que nos encontramos dentro del ciclo.
     */
    private void calcularDiaYSemana() {
        Calendar calendar = Calendar.getInstance();
        //Domingo = 1, sábado = 7
        int dia = calendar.get(Calendar.DAY_OF_WEEK)-1;
        //La semana comienza lunes (sí, aunque el primer día de la semana es domingo :S)
        int semana = calendar.get(Calendar.WEEK_OF_YEAR);
        //III ciclo 2015 comienza el 5 de enero del 2015 y termina el 28 de febrero
        //Semana 2 a 9 más la semana de examenes (10)
        if (semana >= 2 && semana <= 10) {
            semana = ((semana + 3) % 5) + 1;
            //I ciclo 2015 comienza el 11 de marzo del 2015 y termina el 4 de julio
            //Semana 11 a 27 más la semana de examenes (28)
        } else {
            if (semana >= 11 && semana <= 28) {
                semana = ((semana + 4) % 5) + 1;
            } else {
                //II semestre 2014 comienza la semana 33 y termina la 48
                //Semana 33 a 48 más la semana de examenes (49)
                if (semana >= 33 && semana <= 49) {
                    semana = ((semana + 2) % 5) + 1;
                }
            }
        }
        /*
        TODO: Poner esto bien:
        editor.putInt("semana", semana);
        editor.putInt("dia", dia);
        */
        editor.putInt("semana", 5);
        editor.putInt("dia", 3);
    }

    /**
     * Llamado cuando el usuario elige una de las sodas de la lista.
     */
    public void goToListaPlatos(int IDSoda, String nombreSoda){
        editor.putInt("IDSoda", IDSoda+1); // Se le suma 1 puesto que en la BD los ID empiezan desde 1
        editor.putString("nombreSoda", nombreSoda);
        editor.commit();
        Intent intent = new Intent(this, ListaPlatos.class);
        startActivity(intent);
    }

    /**
     * Le asigna al PopUp los valores obtenidos de la consulta a la BD
     */
    public void popUpSugerenciaPlato(final String[] resultados){

        final TextView nombreP = (TextView)dialog.findViewById(R.id.nombre_plato_popup);
        final TextView precioP = (TextView)dialog.findViewById(R.id.precio_plato_popup);
        final TextView sodaP = (TextView)dialog.findViewById(R.id.nombre_soda_popup);

        nombreP.setText(resultados[0]);
        precioP.setText(resultados[1]);
        sodaP.setText(resultados[2]);

        Button b = (Button) dialog.findViewById(R.id.ir_a_detalle_plato);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Esta lista está muy fea, sería mejor que DetallesPlato solo reciba IDPlato
                Intent intent = new Intent(getBaseContext(), DetallesPlato.class);
                editor.putInt("IDPlato", Integer.parseInt(resultados[3]));
                editor.putInt("IDSoda", Integer.parseInt(resultados[4]));
                editor.putString("categoriaPlato", String.valueOf(resultados[5]));
                editor.putString("nombrePlato", String.valueOf(nombreP.getText()));
                editor.putString("nombreSoda", String.valueOf(sodaP.getText()));
                editor.commit();

                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    public String[] getSugerenciaPlatoTest(){
        String[] resultados = new String[3];
        TextView nombre_plato = (TextView) dialog.findViewById(R.id.nombre_plato_popup);
        TextView precio_plato = (TextView) dialog.findViewById(R.id.precio_plato_popup);
        TextView soda_plato = (TextView) dialog.findViewById(R.id.nombre_soda_popup);
        resultados[0] = (String) nombre_plato.getText();
        resultados[1] = (String) precio_plato.getText();
        resultados[2] = (String) soda_plato.getText();
        return resultados;
    }

    /**
     * Adaptador personalizado para cargar tanto los nombres como las imagenes de las sodas
     */
    private class CustomArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CustomArrayAdapter(Context context, String[] values) {
            super(context, R.layout.item_soda, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_soda, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.nombre_comida);
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

    /**
     * Verifica que la BD esté disponible
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Realiza las consultas a la BD sobre el plato del día y la soda a la que pertenece
     */
    private class MyTask extends AsyncTask<String, String, String[]> {
        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String[] doInBackground(String... params) {
            String[] resultados = new String[6];
            String JSON;

            if(isOnline()){
                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                p.setUri("https://limitless-river-6258.herokuapp.com/platos?semana="
                          + pref.getInt("semana", 1)
                          + "&dia="
                          + pref.getInt("dia",1)
                          + "&best=1");
                JSON = HttpManager.getData(p);

                Log.d("Resultado 1", JSON);

                try{
                    JSONObject obj = new JSONObject(JSON);
                    resultados[0] = obj.getString("nombre");
                    resultados[1] = obj.getString("precio");
                    resultados[3] = String.valueOf(obj.getInt("id"));
                    int sodaIDPlato = obj.getInt("soda_id");

                    p = new RequestPackage();
                    p.setMethod("GET");
                    p.setUri("https://limitless-river-6258.herokuapp.com/sodas/" + sodaIDPlato);
                    p.setParam("id", "" + sodaIDPlato);
                    JSON = HttpManager.getData(p);

                    Log.d("Resultado 2", JSON);

                    try{
                        obj = new JSONObject(JSON);
                        resultados[2] = obj.getString("nombre");
                        resultados[4] = obj.getString("id");
                        resultados[5] = obj.getString("categoria");
                    } catch(JSONException e){
                        Log.e("JSONException", "Error manejando el JSON para la soda");
                    }

                } catch(JSONException e){
                    Log.e("JSONException", "Error manejando el JSON para el popup");
                }
            }
            else {
                Toast.makeText(getBaseContext(), "Red no disponible", Toast.LENGTH_LONG).show();
            }
            return resultados;
        }

        @Override
        protected void onPostExecute(String[] result) {
            popUpSugerenciaPlato(result);
            setProgressBarIndeterminateVisibility(false);
        }
    }
}
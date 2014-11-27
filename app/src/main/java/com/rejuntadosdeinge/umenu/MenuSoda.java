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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Acompanamiento;
import com.rejuntadosdeinge.umenu.modelo.AcompanamientoParser;
import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import java.util.List;

public class MenuSoda extends ActionBarActivity {

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Variable global
    List<Acompanamiento> acompanamientoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_menu_soda);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // Setea el título de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e) {
            Log.e("MenuSoda", "No se pudo cambiar el título de la Activity");
        }

        // Obtiene los acompañamientos de la BD
        AcompanamientosTask acompanamientosTask = new AcompanamientosTask();
        acompanamientosTask.execute();
    }

    public void irPlatos() {
        Intent intent = new Intent(this, ListaPlatos.class);
        startActivity(intent);
    }

    public void irSnacks() {
        Intent intent = new Intent(this, ListaSnacks.class);
        startActivity(intent);
    }

    public void irEnsaladas() {
        Intent intent = new Intent(this, ListaEnsaladas.class);
        startActivity(intent);
    }

    public void irRefrescos() {
        Intent intent = new Intent(this, ListaRefrescos.class);
        startActivity(intent);
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_soda, menu);
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
     *  Toma datos y los guarda en SharedPreferences
     */
    protected void updateAcompanamiento() {

        int id = acompanamientoList.get(0).getId();

        editor.putString("acompanamiento", acompanamientoList.get(0).getAcompanamiento());
        editor.putString("guarnicion", acompanamientoList.get(0).getGuarnicion());
        editor.putString("ensalada1", acompanamientoList.get(0).getEnsalada1());
        editor.putString("ensalada2", acompanamientoList.get(0).getEnsalada2());
        editor.putString("ensalada3", acompanamientoList.get(0).getEnsalada3());
        editor.putString("ensalada4", acompanamientoList.get(0).getEnsalada4());
        editor.putString("ensalada5", acompanamientoList.get(0).getEnsalada5());
        editor.putString("ensalada6", acompanamientoList.get(0).getEnsalada6());
        editor.putString("fruta1", acompanamientoList.get(0).getFruta1());
        editor.putString("fruta2", acompanamientoList.get(0).getFruta2());
        editor.putString("fresco1", acompanamientoList.get(0).getFresco1());
        editor.putString("fresco2", acompanamientoList.get(0).getFresco2());
        editor.putString("frescosinazucar", acompanamientoList.get(0).getFrescoSinAzucar());

        editor.commit();

        // toma datos y los pasa a la lista
        String[] listaMenu = {
                "Platos",
                "Ensaladas",
                "Refrescos",
                "Frutas",
                "Snacks"
        };

        // Se le conecta un adapter personalizado para cargar las imágenes
        MenuAdapter menu_adapter = new MenuAdapter(this, listaMenu);
        final ListView listView = (ListView) this.findViewById(R.id.menu_soda);
        listView.setAdapter(menu_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        irPlatos();
                        break;
                    case 1:
                        irEnsaladas();
                        break;
                    case 2:
                        //irRefrescos();
                        break;
                    case 3:
                        //irFrutas();
                        break;
                    case 4:
                        irSnacks();
                        break;
                }
            }
        });
    }

    /**
     * Adaptador personalizado para cargar tanto los nombres como las imagenes de las sodas
     */
    private class MenuAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MenuAdapter(Context context, String[] values) {
            super(context, R.layout.item_soda, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_menu, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.nombre_menu);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imagen_menu);
            textView.setText(values[position]);
            switch(position) {
                case 0:
                    imageView.setImageResource(R.drawable.ic_plato);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.ic_ensalada);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.ic_fresco);
                    break;
                case 3:
                    imageView.setImageResource(R.drawable.ic_fruta);
                    break;
                case 4:
                    imageView.setImageResource(R.drawable.ic_snack);
                    break;
            }
            return rowView;
        }
    }


    /**
     * Verifica que haya una conexión disponible
     */
    protected boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     *  Realiza las consultas a la BD sobre los platos de la soda
     */
    private class AcompanamientosTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String JSON = "";

            if(hayConexion()){
                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                p.setUri("http://limitless-river-6258.herokuapp.com/acompanamientos?busq=8&soda_id=" + pref.getInt("IDSoda", 0)
                        + "&semana=" + pref.getInt("semana", 0)
                        + "&dia=" + pref.getInt("dia", 0)
                        + "&get=1");
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
            acompanamientoList = AcompanamientoParser.parseFeed(result);
            updateAcompanamiento();
            setProgressBarIndeterminateVisibility(false);
        }
    }
}


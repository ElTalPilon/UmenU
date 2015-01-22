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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Comentario;
import com.rejuntadosdeinge.umenu.modelo.ComentarioParser;
import com.rejuntadosdeinge.umenu.modelo.Plato;
import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import java.util.List;

import static com.rejuntadosdeinge.umenu.R.id;

public class DetallesPlato extends ActionBarActivity {

    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Plato que se muestra
    Plato plato;

    // Ya el usuario ha puntuado este plato
    boolean yaPuntuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_detalles_plato);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        // Obtiene los datos del plato y actualiza la actividad
        Intent i = getIntent();
        if(i.hasExtra("platoSeleccionado")){
            plato = i.getParcelableExtra("platoSeleccionado");
        }
        actualizarDatosPlato();

        // Cuando se le cambie la puntuación al plato, se abrirá el popUp
        RatingBar ratingBar = (RatingBar) findViewById(id.ratingBar);
        ratingBar.setStepSize((float) 1.0);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int nota= ((int) ratingBar.getRating());
                popUpPuntuarPlato(nota);
            }
        });
    }

    /**
     * Infla el menú (Agrega elementos al Action Bar)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalles_plato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Actualiza la interfaz con los datos propios del plato
     */
    protected void actualizarDatosPlato() {
        // Cambia el título de la actividad
        try{
            getSupportActionBar().setTitle(plato.getNombre());
        } catch(NullPointerException e){
            Log.e("DetallesPlato", "No se le pudo cambiar el nombre a la Activity");
        }

        TextView nombre_soda = (TextView) findViewById(id.nombre_soda);
        nombre_soda.setText(pref.getString("nombreSoda", null));

        ImageView imagen_soda = (ImageView) findViewById(R.id.imagen_soda);
        switch(plato.getSodaId()){
            case 1:
                imagen_soda.setImageResource(R.drawable.ic_odonto);
                break;
            case 2:
                imagen_soda.setImageResource(R.drawable.ic_derecho);
                break;
            case 3:
                imagen_soda.setImageResource(R.drawable.ic_economicas);
                break;
            case 4:
                imagen_soda.setImageResource(R.drawable.ic_agro);
                break;
            case 5:
                imagen_soda.setImageResource(R.drawable.ic_generales);
                break;
            case 6:
                imagen_soda.setImageResource(R.drawable.ic_educacion);
                break;
            case 7:
                imagen_soda.setImageResource(R.drawable.ic_sociales);
                break;
            case 8:
                imagen_soda.setImageResource(R.drawable.ic_comedor);
                break;
        }

        TextView nombre_plato = (TextView) findViewById(id.nombre_plato);
        nombre_plato.setText(plato.getNombre());

        TextView precio_plato = (TextView) findViewById(id.precio_plato);
        precio_plato.setText(plato.getPrecio());

        //ObtenerComentarios oc = new ObtenerComentarios();
        //oc.execute((Integer) plato.getId());
        actualizarComentarios();
    }

    /**
     * TODO: Toa la jugada
     * Actualiza la interfaz con los comentarios del plato
     */
    private void actualizarComentarios(){//List<Comentario> comentarios){
        String[] listaDeComentarios = {
                "Me encantó!",
                "Estuvo muy bueno",
                "El arroz estaba muy salado"
        };

        // Se le conecta un adapter personalizado para cargar las imágenes de cada soda
        CustomAdapter customAdapter = new CustomAdapter(context, listaDeComentarios);
        final ListView listView = (ListView) this.findViewById(id.lista_comentarios);
        listView.setAdapter(customAdapter);
    }

    /**
     * TODO: Toa la jugada
     * Despliega el popUp para calificar el plato, con la puntuación que ya se le asignó.
     */
    private void popUpPuntuarPlato(float nota) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_calificar_plato);

        final RatingBar rb = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
        rb.setRating(nota);
        dialog.show();

        TextView nombre_plato = (TextView) dialog.findViewById(id.nombre_plato_popup);
        nombre_plato.setText(plato.getNombre());

        Button b = (Button) dialog.findViewById((R.id.botonComentario));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*EditText textoComentario = (EditText) findViewById(id.texto_comentario);
                int puntuacion = (int) rb.getRating();
                String comentario = String.valueOf(textoComentario.getText());

                if(yaPuntuo){

                } else{
                    
                }*/

                dialog.dismiss();
            }
        });
    }

    /**
     * Verifica que haya una conexión disponible
     */
    protected boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CustomAdapter (Context context, String[] values) {
            super(context, R.layout.item_soda, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.item_comentario, parent, false);
            TextView puntuacion = (TextView) rowView.findViewById(id.puntuacion);
            TextView comentario = (TextView) rowView.findViewById(id.comentario);
            comentario.setText(values[position]);
            switch(position) {
                case 0:
                    puntuacion.setText("5");
                    break;
                case 1:
                    puntuacion.setText("4");
                    break;
                case 2:
                    puntuacion.setText("2");
                    break;
            }
            return rowView;
        }
    }

    private class ObtenerComentarios extends AsyncTask<Integer, Void, String> {
        @Override
        protected void onPreExecute(){
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(Integer... params){
            // TODO: Setear la variable yaPuntuo
            int IDplato = params[0];
            String JSON = "";

            if(hayConexion()){
                RequestPackage rp = new RequestPackage();
                rp.setMethod("POST");
                rp.setUri(""); //TODO: Poner la consulta que es

                JSON = HttpManager.getData(rp);
                Log.d("Resultado calificacion previa", JSON);
            }
            else{
                Toast.makeText(context, "Red no disponible", Toast.LENGTH_LONG).show();
            }
            return JSON;
        }

        @Override
        protected void onPostExecute(String JSON){
            //actualizarComentarios(ComentarioParser.parseFeed(JSON));
            setProgressBarIndeterminateVisibility(false);
        }
    }

    private class PuntuarPlato extends AsyncTask<Comentario, Void, Void> {
        @Override
        public void onPreExecute(){
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        public Void doInBackground(Comentario... params){
            Comentario nuevoComentario = params[0];

            if(hayConexion()){
                RequestPackage rp = new RequestPackage();
                rp.setMethod("POST");
                rp.setUri("");
            } else{
                Toast.makeText(context, "Red no disponible", Toast.LENGTH_LONG).show();
            }
            return (Void) null;
        }

        public void onPostExecute(){
            setProgressBarIndeterminateVisibility(false);
        }
    }
}

package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Plato;
import com.rejuntadosdeinge.umenu.modelo.PlatoParser;
import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import java.util.List;

import static com.rejuntadosdeinge.umenu.R.id;

public class DetallesPlato extends ActionBarActivity {

    // Clase creada para no bloquear el hilo principal con la conexión a la BD
    private class MyTask extends AsyncTask<RequestPackage, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            listaDeSodas = PlatoParser.parseFeed(result);
            updateDisplay();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Variables de control del día
    private int semana;
    private int dia;

    TextView tv_nombre_plato;
    TextView tv_precio_plato;
    ProgressBar progressBar;

    List<Plato> listaDeSodas; //TODO: Supongo que esta lista podría reusarse en ListaSodas.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_plato);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        //Inicializa variables
        semana = 3;
        dia = 3;

        // Setea el nombre de la actividad y del banner
        TextView tv_nombre = (TextView) findViewById(R.id.banner_detalles_plato);
        try{
            getSupportActionBar().setTitle(pref.getString("nombrePlato", null));
            tv_nombre.setText(pref.getString("nombreSoda", null));
        } catch(NullPointerException e){
            Log.e("DetallesPlato", "No se le pudo cambiar el nombre a la Activity");
        }

        // TextView inicializado con scroll vertical
        tv_nombre_plato = (TextView) findViewById(R.id.nombre_plato);
        tv_precio_plato = (TextView) findViewById(R.id.tv_precio);
        tv_nombre_plato.setMovementMethod(new ScrollingMovementMethod());
        tv_precio_plato.setMovementMethod(new ScrollingMovementMethod());


        progressBar = (ProgressBar) findViewById( R.id.progressBarDetallesPlato);
        progressBar.setVisibility(View.INVISIBLE);

        if (isOnline()) {
            requestData("http://limitless-river-6258.herokuapp.com/platos?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0))
                    + "&semana=" + String.valueOf(semana)
                    + "&dia=" + String.valueOf(dia)
                    + "&categoria=" + pref.getString("categoriaPlato", null) +"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }

        RatingBar ratingBar = (RatingBar) findViewById(id.ratingBar);
        // Cuando haya un cambio en la calificación
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float nota= ratingBar.getRating(); // Obtengo la nueva calificación
                popUpPuntuarPlato(nota); // Instancio el popUp
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

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("soda_id", String.valueOf(pref.getInt("IDSoda", 0)));
        p.setParam("semana", String.valueOf(semana));
        p.setParam("dia", String.valueOf(dia));
        p.setParam("categoria", pref.getString("categoriaPlato", null));

        MyTask task = new MyTask();
        task.execute(p);
    }

    protected void updateDisplay() {

        if (listaDeSodas != null) {
            for (Plato plato : listaDeSodas) {
                tv_nombre_plato.append("\n" + plato.getNombre());
                tv_precio_plato.append("\n" + plato.getPrecio());
            }
        }
    }

    private void popUpPuntuarPlato(float nota) {

        //inicializacion del dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_calificar_plato);
        dialog.setTitle(getString(R.string.title_fragment_calificar_plato));
        TextView text = (TextView) dialog.findViewById(id.escribaComentario);
        text.setText(getString(R.string.Comentario));

        RatingBar rb = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
        // Se le asigna la nueva nota al RatingBar del popUp
        rb.setRating(nota);
        dialog.show();

        Button b = (Button) dialog.findViewById((R.id.botonComentario));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*
                // TODO: Cambiar dummy data por el ID del plato que se pasará por pref.getInt("IDPlato", 0);
                // TODO: Darle una acción al botón (Enviar a la BD)
                RatingBar rb1 = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
                //float notaFinal=rb1.getRating();

                int id = 94;
                int calif = 5;
                int total = 25;
                double promedio = total/calif;

                StringBuilder uri = new StringBuilder("https://limitless-river-6258.herokuapp.com/platos?c=1&id=");
                uri.append(id);
                uri.append("&calificaciones=");
                uri.append(calif);
                uri.append("&total=");
                uri.append(calif);
                uri.append("&promedio=");
                uri.append(promedio);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                //startActivity(intent);
                */
                dialog.dismiss();
            }
        });
    }
}

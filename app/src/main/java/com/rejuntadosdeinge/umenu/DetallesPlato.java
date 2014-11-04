package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.rejuntadosdeinge.umenu.R.id;

public class DetallesPlato extends ActionBarActivity {

    final Context context = this;
    public int idSoda;
    public String sodaElegida;
    public String platoElegido;
    int semana = 3;
    int dia = 3;
    int categoria;
    String categoria2;

    TextView output;
    ProgressBar pb;

    List<Plato> platoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_plato);

        // textView inicializado con scroll vertical
        output = (TextView) findViewById(R.id.tv_nombre_plato);
        output.setMovementMethod(new ScrollingMovementMethod());

        // extraer id del intent que viene de ListaPlato
        Intent intent = getIntent();
        idSoda = intent.getIntExtra("idSoda", 0);
        sodaElegida = intent.getStringExtra("sodaElegida");
        categoria = intent.getIntExtra("categoria", 0);

        if(categoria == 0)
            categoria2 = "B%C3%A1sico%201";
            else
            if(categoria == 1)
                categoria2 = "B%C3%A1sico%202";
            else
            if(categoria == 2)
                categoria2 = "Vegetariano";


        TextView tv_nombre = (TextView) findViewById(R.id.textView7);
        tv_nombre.setText(sodaElegida);

        pb = (ProgressBar) findViewById( R.id.progressBarDetallesPlato);
        pb.setVisibility(View.INVISIBLE);

        if (isOnline()) {
            requestData("http://limitless-river-6258.herokuapp.com/platos?soda_id=" + String.valueOf(idSoda) + "&semana=" + String.valueOf(semana) + "&dia=" + String.valueOf(dia) + "&categoria=" + categoria2 +"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }

        //inicializacion del dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_calificar_plato);
        dialog.setTitle(getString(R.string.agregueComentario));
        TextView text = (TextView) dialog.findViewById(id.escribaComentario);
        text.setText(getString(R.string.Comentario));

        RatingBar ratingBar = (RatingBar) findViewById(id.ratingBar);
        //cuando se modifique la nota, hacer los siguente:
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float nota= ratingBar.getRating(); //obtengo la nota del RatingBar
                popUpPuntuarPlato(dialog, nota); //Convoco al popUp
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalles_plato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void popUpPuntuarPlato(final Dialog dialog, float nota) {
        RatingBar rb = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
        //Al Rating bar del popUp ledoy la nota del fragmento, para q pueda ser modificada y enviada
        //a la base de datos (pendiente)
        rb.setRating(nota);//le doy la nota
        dialog.show(); //mostar el popUp
        //falta darle acción al Botón para q cierre el popUp y envie los datos a la base
        Button b = (Button) dialog.findViewById((id.botonComentario));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RatingBar rb1 = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
                //float notaFinal=rb1.getRating();
                /*
                 * tengo q obtener de alguna forma el id del platillo para hacer el update en la DB
                 * y cerrar el popup
                 */

                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.howtosolvenow.com"));
                startActivity(browserIntent);
            }
        });
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
        p.setParam("soda_id", String.valueOf(idSoda));
        p.setParam("semana", String.valueOf(semana));
        p.setParam("dia", String.valueOf(dia));
        p.setParam("categoria", categoria2);

        MyTask task = new MyTask();
        task.execute(p);
    }

    protected void updateDisplay(String msg) {
        Toast.makeText(this, "JSON: "+msg, Toast.LENGTH_LONG).show();
        try {

            JSONObject obj = new JSONObject(msg);
            String nombre = obj.getString("nombre");
            String precio = obj.getString("precio");
            TextView tv_nombre = (TextView) findViewById(R.id.tv_nombre_plato);
            tv_nombre.setText(nombre);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void updateDisplay2() {

        if (platoList != null) {
            for (Plato plato : platoList) {
                output.append(plato.getNombre() + " " + plato.getPrecio() + "\n");
            }
        }
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

            //updateDisplay(result);

            platoList = PlatoParser.parseFeed(result);
            updateDisplay2();
            pb.setVisibility(View.INVISIBLE);
        }
    }
}

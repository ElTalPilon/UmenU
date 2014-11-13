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
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Plato;
import com.rejuntadosdeinge.umenu.modelo.PlatoParser;
import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import java.util.List;

import static com.rejuntadosdeinge.umenu.R.id;

public class DetallesPlato extends ActionBarActivity {

    final Context context = this;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Variables de control del día
    private String semana;
    private String dia;

    // 1. creamos instancias para los widget que llenaremos de la actividad activity_detalles_plato
    TextView tv_nombre_plato;
    TextView tv_precio_plato;

    // 2. se crea una lista para todos los platos que vamos a obtener del web service
    List<Plato> listaDePlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_detalles_plato);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        //Inicializa variables
        dia = String.valueOf(pref.getInt("dia", 0));
        semana = String.valueOf(pref.getInt("semana", 0));

        // Setea el nombre de la actividad y del banner
        TextView tv_nombre = (TextView) findViewById(R.id.banner_detalles_plato);
        try{
            getSupportActionBar().setTitle(pref.getString("nombrePlato", null));
            tv_nombre.setText(pref.getString("nombreSoda", null));
        } catch(NullPointerException e){
            Log.e("DetallesPlato", "No se le pudo cambiar el nombre a la Activity");
        }

        // 3. TextView inicializado con scroll vertical
        tv_nombre_plato = (TextView) findViewById(R.id.nombre_plato);
        tv_precio_plato = (TextView) findViewById(R.id.precio_plato);
        tv_nombre_plato.setMovementMethod(new ScrollingMovementMethod());
        tv_precio_plato.setMovementMethod(new ScrollingMovementMethod());

        // 5. Revisamos que hay conexión a internet
        if (isOnline()) {
            // 6. se hace la consulta
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

    /*
        requestData(String uri) encapsula en una instancia tipo RequestPackage
        los parámetros que se necesitan para la consulta POST

        recibe: uri, la dirección donde se encuentran los datos
     */
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

    /*
        updateDisplay() llena los campos obtenidos, se espera para ésta consulta un único plato,
        si tenemos otra consulta donde obtenemos más de un platos ver el método updateDisplay() en listaSnacks
     */
    protected void updateDisplay() {
        if (listaDePlatos != null) {
            for (Plato plato : listaDePlatos) {
                tv_nombre_plato.setText(plato.getNombre());
                tv_precio_plato.setText(plato.getPrecio());
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

    /*
            Clase creada para no bloquear el hilo principal, se encarga de la conexión a la BD,
            las comunicaciones de red deben correr en el background thread
            Parámetros:
                Params: RequestPackage
                Progress: String
                Result: String
        */
    private class MyTask extends AsyncTask<RequestPackage, String, String> {

        // onPreExecute hace visible el progressBar
        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        // doInBackground recibe una lista de parámetros, todos de tipo RequestPackage
        // (así se definió en la decoración de la clase en el campo Params),
        // doInBackground retorna un String con el resultado
        // (así se definió en la decoración de la clase en el campo Result),
        @Override
        protected String doInBackground(RequestPackage... params) {
            return HttpManager.getData(params[0]);
        }


        // onPostExecute(String result) el campo resulta es la respuesta del Web-Service
        // obtenido a travéz de HttpManager, viene en formato JSON por lo que necesitamos
        // Parsear el resultado.
        @Override
        protected void onPostExecute(String result) {

            //PlatoParser retorna un List que puede ser un plato ó
            // varios platos dependiendo de la consulta realizada
            listaDePlatos = PlatoParser.parseFeed(result);
            // updateDisplay se encarga de llenar los campos del activity, ó sacar información
            // como calificaciones, promedio, etc.. usando los getters de Plato
            updateDisplay();
            // se oculta de progressBar
            setProgressBarIndeterminateVisibility(false);
        }
    }
}

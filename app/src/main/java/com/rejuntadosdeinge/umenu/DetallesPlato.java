package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
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
    private float promedio;


    // 1. creamos instancias para los widget que llenaremos de la actividad activity_detalles_plato
    TextView tv_nombre_plato;
    TextView tv_precio_plato;

    TextView tv_promedio_plato;

    // 2. se crea una lista para todos los platos que vamos a obtener del web service
    List<Plato> listaDePlatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_detalles_plato);

        // Inicializa las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.apply();

        //Inicializa variables
        dia = String.valueOf(pref.getInt("dia", 0));
        semana = String.valueOf(pref.getInt("semana", 0));

        // Setea el nombre de la actividad y del banner
        TextView nombre_soda = (TextView) findViewById(id.nombre_soda);
        TextView nombre_plato = (TextView) findViewById(id.nombre_plato);
        try{
            getSupportActionBar().setTitle(pref.getString("nombrePlato", null));
            nombre_soda.setText(pref.getString("nombreSoda", null));
            nombre_plato.setText(pref.getString("nombrePlato", null));
        } catch(NullPointerException e){
            Log.e("DetallesPlato", "No se le pudo cambiar el nombre a la Activity");
        }

        String[] listaDeComentarios = {
                "Me encantó!",
                "Estuvo muy bueno.",
                "No fue la gran cosa...",
                "Estaba pasado de salado.",
                "Me salió un pelo!"
        };

        // Se le conecta un adapter personalizado para cargar las imágenes de cada soda
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(this, listaDeComentarios);
        final ListView listView = (ListView) this.findViewById(id.lista_comentarios);
        listView.setAdapter(customArrayAdapter);

        // 3. TextView inicializado con scroll vertical
        tv_nombre_plato = (TextView) findViewById(R.id.nombre_plato);
        tv_precio_plato = (TextView) findViewById(R.id.precio_plato);
        //tv_promedio_plato= (TextView) findViewById(id.nota_plato);
        //tv_nombre_plato.setMovementMethod(new ScrollingMovementMethod());
        //tv_precio_plato.setMovementMethod(new ScrollingMovementMethod());
        //tv_promedio_plato.setMovementMethod(new ScrollingMovementMethod());

        // 5. Revisamos que hay conexión a internet
        if (isOnline()) {
            // 6. se hace la consulta
            requestData("http://limitless-river-6258.herokuapp.com/platos?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0))
                    + "&semana=" + String.valueOf(semana)
                    + "&dia=" + String.valueOf(dia)
                    + "&categoria=" + pref.getString("categoriaPlato", null)
                    + "&promedio=" + String.valueOf(promedio)+"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }

        RatingBar ratingBar = (RatingBar) findViewById(id.ratingBar);
        // Cuando haya un cambio en la calificación
        ratingBar.setStepSize((float) 1.0);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int nota= ((int) ratingBar.getRating()); // Obtengo la nueva calificación

                popUpPuntuarPlato(nota); // Instancio el popUp
            }
        });
    }
//.../platos?nota=[]&id=[
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
        p.setParam("promedio", String.valueOf(promedio));


        MyTask task = new MyTask();
        task.execute(p);
    }

    /*
        updateDisplay() llena los campos obtenidos, se espera para ésta consulta un único plato,
        si tenemos otra consulta donde obtenemos más de un platos ver el método updateDisplay() en listaSnacks
     */
    protected void updateDisplay() {
        ImageView imagen_soda = (ImageView) findViewById(R.id.imagen_soda);

        switch(pref.getInt("IDSoda", 0)-1){
            case 0:
                imagen_soda.setImageResource(R.drawable.ic_odonto);
                break;
            case 1:
                imagen_soda.setImageResource(R.drawable.ic_derecho);
                break;
            case 2:
                imagen_soda.setImageResource(R.drawable.ic_economicas);
                break;
            case 3:
                imagen_soda.setImageResource(R.drawable.ic_agro);
                break;
            case 4:
                imagen_soda.setImageResource(R.drawable.ic_generales);
                break;
            case 5:
                imagen_soda.setImageResource(R.drawable.ic_educacion);
                break;
            case 6:
                imagen_soda.setImageResource(R.drawable.ic_sociales);
                break;
            case 7:
                imagen_soda.setImageResource(R.drawable.ic_comedor);
                break;
        }
        if (listaDePlatos != null) {
            for (Plato plato : listaDePlatos) {
                tv_nombre_plato.setText(plato.getNombre());
                tv_precio_plato.setText(plato.getPrecio());
                //tv_promedio_plato.setText(String.valueOf(plato.getPromedio()));
            }
        }
    }

    private void popUpPuntuarPlato(float nota) {

        //inicializacion del dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_calificar_plato);

        RatingBar rb = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
        // Se le asigna la nueva nota al RatingBar del popUp
        rb.setRating(nota);
        dialog.show();

        TextView nombre_plato = (TextView) dialog.findViewById(id.nombre_plato_popup);
        nombre_plato.setText(pref.getString("nombrePlato", null));

        Button b = (Button) dialog.findViewById((R.id.botonComentario));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //1ra opción

                RatingBar rb1 = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
                float notaFinal=rb1.getRating();

                if (isOnline()) {
                    // 6. se hace la consulta
                    requestData("http://limitless-river-6258.herokuapp.com/platos?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0))
                            + "&semana=" + String.valueOf(semana)
                            + "&dia=" + String.valueOf(dia)
                            + "&categoria=" + pref.getString("categoriaPlato", null)
                            + "&promedio=" + String.valueOf(promedio)+"&get=1");
                } /*else {
                    Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
                }

                String uri="https://umenuadmin.herokuapp.com/platos";

                RequestPackage p = new RequestPackage();
                p.setMethod("POST");
                p.setUri(uri);
                p.setParam("id", String.valueOf(pref.getInt("IDPlato", 0)));
                p.setParam("nota", String.valueOf(notaFinal));

                MyTask task = new MyTask();
                //task.execute(p); está comentada xq se cae cuando la envía.


                /*2da opción--------------------------------------------------------------
                StringEntity se = null;
                RatingBar rb1 = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
                float notaFinal=rb1.getRating();
                try {
                    String json = "";

                    //Construir el objeto json
                    JSONObject jConsulta = new JSONObject();

                    // se acumulan los campos necesarios, el primer parametro
                    // es la etiqueta json que tendran los campos de la base
                    jConsulta.accumulate("id",String.valueOf(pref.getInt("IDPlato", 0)));
                    jConsulta.accumulate("nota", String.valueOf(notaFinal));

                    // Convertir el objeto Json a String
                    json = jConsulta.toString();

                    // setear json al stringEntity
                    se = new StringEntity(json);

                }catch (Exception e){
                    Log.d("String to json error", e.getLocalizedMessage());
                }

                HttpClient httpclient = new DefaultHttpClient();
                String url="https://umenuadmin.herokuapp.com/platos";
                //Hacer el request para un POST a la url
                HttpPost httpPost = new HttpPost(url);

                // setear la Entity de httpPost
                httpPost.setEntity(se);

                // incluir los headers para que el Api sepa que es json
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = null;
                try{
                    // ejecutar el request de post en la url
                    httpResponse = httpclient.execute(httpPost);
                }catch (Exception e){
                    Log.d("InputStream", e.getLocalizedMessage());
                }
                //String resultado = StreamToString(httpResponse);
                */


                dialog.dismiss();
            }
        });
    }

    /**
     * Adaptador personalizado para cargar tanto los nombres como las imagenes de las sodas
     */
    private class CustomArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CustomArrayAdapter(Context context, String[] values) {
            super(context, R.layout.item_comentario, values);
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
                    puntuacion.setText("3");
                    break;
                case 3:
                    puntuacion.setText("2");
                    break;
                case 4:
                    puntuacion.setText("1");
                    break;
            }
            return rowView;
        }
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

        // onPostExecute(String result) el campo result es la respuesta del Web-Service
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

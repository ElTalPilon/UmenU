package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.Snack;
import com.rejuntadosdeinge.umenu.modelo.SnackParser;

import java.util.List;

public class ListaSnacks  extends ActionBarActivity {

    TextView output;
    ProgressBar pb;
    public int idSoda;
    public String nombreProducto;
    public String precioProducto;

    List<Snack> snackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_snaks);

        //		Initialize the TextView for vertical scrolling
        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

        // extraer datos del intent hecho en ListaPlatos
        Intent intent = getIntent();
        idSoda = intent.getIntExtra("idSoda", 0);

        pb = (ProgressBar) findViewById( R.id.progressBarListaSnacks);
        pb.setVisibility(View.INVISIBLE);

        if (isOnline()) {
            Toast.makeText(this, "Red si disponible", Toast.LENGTH_LONG).show();
            requestData("http://limitless-river-6258.herokuapp.com/snacks?soda_id=" + String.valueOf(idSoda) +"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }
    }

    public void desplegarDetalles() {

        TextView tv_nombre = (TextView) findViewById(R.id.tv_nombre_snack);
        tv_nombre.setText(nombreProducto);

        TextView tv_precio = (TextView) findViewById(R.id.tv_nombre_snack);
        tv_precio.setText(precioProducto);

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
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {

        if (snackList != null) {
            for (Snack snack : snackList) {
                output.append(snack.getNombre() + " " + snack.getPrecio() + "\n");
            }
        }

    }

    // para no bloquear el hilo principal la conexion la realiza otro hilo
    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {

            snackList = SnackParser.parseFeed(result);
            updateDisplay();
            pb.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
   //         updateDisplay(values[0]);
        }
    }
}


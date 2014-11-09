package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;
import com.rejuntadosdeinge.umenu.modelo.Snack;
import com.rejuntadosdeinge.umenu.modelo.SnackParser;

import java.util.List;

public class ListaSnacks  extends ActionBarActivity {

    // SharedPreferences
    private SharedPreferences pref;

    TextView output;
    ProgressBar pb;
    List<Snack> snackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_snacks);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();

        try {
            getSupportActionBar().setTitle("Snacks | " + pref.getString("nombreSoda", null));
        }catch(NullPointerException e){
            Log.e("ListaPlatos", "No se pudo cambiar el título de la Activity");
        }

        // textView inicializado con scroll vertical
        output = (TextView) findViewById(R.id.tv_snacks);
        output.setMovementMethod(new ScrollingMovementMethod());

        pb = (ProgressBar) findViewById( R.id.progressBarListaSnacks);
        pb.setVisibility(View.INVISIBLE);

        if (isOnline()) {
            requestData("http://limitless-river-6258.herokuapp.com/snacks?soda_id=" + String.valueOf(pref.getInt("IDSoda", 0)) +"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void requestData(String uri) {

        RequestPackage p = new RequestPackage();
        p.setMethod("POST");
        p.setUri(uri);
        p.setParam("soda_id", String.valueOf(pref.getInt("IDSoda", 0)));

        MyTask task = new MyTask();
        task.execute(p);
    }

    protected void updateDisplay() {

        if (snackList != null) {
            for (Snack snack : snackList) {
                output.append(snack.getNombre() + " " + snack.getPrecio() + "\n");
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
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            snackList = SnackParser.parseFeed(result);
            updateDisplay();
            pb.setVisibility(View.INVISIBLE);
        }
    }
}


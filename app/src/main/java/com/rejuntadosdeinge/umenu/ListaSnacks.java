package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ListaSnacks  extends ActionBarActivity {

    ProgressBar pb;
    public int idSoda;
    public String nombreProducto;
    public String precioProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_snaks);

        // extraer datos del intent hecho en ListaPlatos
        Intent intent = getIntent();
        idSoda = intent.getIntExtra("idSoda", 0);

        pb = (ProgressBar) findViewById( R.id.progressBarListaSnacks);
        pb.setVisibility(View.INVISIBLE);


        if (isOnline()) {
            Toast.makeText(this, "Red si disponible", Toast.LENGTH_LONG).show();
            //requestData("http://limitless-river-6258.herokuapp.com/snacks?soda_id=" + String.valueOf(idSoda) +"&get=1");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }

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



}


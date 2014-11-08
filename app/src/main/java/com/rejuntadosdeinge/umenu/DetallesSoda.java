package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;


public class DetallesSoda extends ActionBarActivity {

    // Clase creada para no bloquear el hilo principal con la conexión a la BD
    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            updateDisplay(result);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }
    }


    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public String horario;
    public float latitud;
    public float longitud;
    ProgressBar progressBar;
    LatLng latLng = null;
    boolean mMostrarMapa = false;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        // Inicializa las SharedPreferences
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        getActionBar().setTitle(pref.getString("nombreSoda", null));

        // Setea el nombre de la actividad
        try {
            getSupportActionBar().setTitle(pref.getString("nombreSoda", null));
        }catch(NullPointerException e){
            Log.e("ListaPlatos", "No se pudo cambiar el título de la Activity");
        }

        progressBar = (ProgressBar) findViewById( R.id.progressBarDetallesSoda);
        progressBar.setVisibility(View.INVISIBLE);

        // Revisa si hay disponibilidad de mapa
        int estaDisponible = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if( (estaDisponible == ConnectionResult.SUCCESS) && initMap() ){
            mMostrarMapa = true;
            mMap.setMyLocationEnabled(true);
        }
        else {
            Toast.makeText(this, "No se pudo conectar con Google Play services", Toast.LENGTH_SHORT).show();
        }

        if (isOnline()) {
            requestData("http://limitless-river-6258.herokuapp.com/sodas/"
                        + String.valueOf(pref.getInt("IDSoda", 0)));
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
        }
    }

    private boolean initMap() {
        if (mMap == null) {
            MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return (mMap != null);
    }

    public void desplegarDetalles() {

        TextView tv_nombre = (TextView) findViewById(R.id.nombre_soda);
        tv_nombre.setText(pref.getString("nombreSoda", null));

        TextView tv_horario = (TextView) findViewById(R.id.horario_soda);
        tv_horario.setText(horario);

        if (mMostrarMapa) {

            latLng = new LatLng(latitud, longitud);

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.moveCamera(update);

            String markerNombre = tv_nombre.getText().toString();

            mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(markerNombre)
                            .anchor(.5f, .5f)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_starmarker))
            );
        }
    }

    /**
     * Infla el menú. (Agrega elementos al Action Bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalles_soda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.ver_map:

                int estaDisponible = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

                if( (estaDisponible == ConnectionResult.SUCCESS) && initMap() ){
                    enviar_A();
                }
                break;

            case R.id.gp_licencia:
                Intent intent = new Intent(this, GPSLicencia.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    public void enviar_A() {

        //	Reenvia a otra aplicacion de mapas

        TextView tv_nombre = (TextView) findViewById(R.id.nombre_soda);
        String markerNombre = tv_nombre.getText().toString();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitud
                                                                + "," + longitud
                                                                + "?z=17" + "&q="
                                                                + URLEncoder.encode(markerNombre)));
        startActivity(intent);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String msg) {
        try {

            JSONObject obj = new JSONObject(msg);
            horario = obj.getString("descripcion");
            longitud = (float)obj.getDouble("long");
            latitud = (float)obj.getDouble("lat");
            desplegarDetalles();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

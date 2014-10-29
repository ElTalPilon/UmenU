package com.rejuntadosdeinge.umenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    public int id = 3;      // modificando id manualmente se ven diferentes detalles sodas
    public String nombre;
    public String horario = "Lunes a Viernes de 7 am a 7 pm";
    public float latitud;
    public float longitud;
    ProgressBar pb;

    LatLng latLng = null;

    boolean mMostrarMapa = false;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        // extraer id del intent que viene de ListaPlato
        //Intent intent = getIntent();
        //id = Integer.parseInt(intent.getStringExtra("id"));

        pb = (ProgressBar) findViewById( R.id.progressBarDetallesSoda);
        pb.setVisibility(View.INVISIBLE);

        // revisa si hay disponibilidad de mapa
        int estaDisponible = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if( (estaDisponible == ConnectionResult.SUCCESS) && initMap() ){
            mMostrarMapa = true;
            mMap.setMyLocationEnabled(true);
        }
        else {
            Toast.makeText(this, "No se pudo conectar con Google Play services", Toast.LENGTH_SHORT).show();
        }


        if (isOnline()) {
            requestData("http://limitless-woodland-1130.herokuapp.com/sodas/" + String.valueOf(id));
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
        tv_nombre.setText(nombre);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        StringBuilder uri = new StringBuilder("geo:");
        uri.append(latitud);
        uri.append(",");
        uri.append(longitud);
        uri.append("?z=17");
        uri.append("&q=" + URLEncoder.encode(markerNombre));

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
        startActivity(intent);
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String msg) {
        try {

            JSONObject obj = new JSONObject(msg);
            nombre = obj.getString("nombre");
            //horario = obj.getString("horario");
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
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
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

            updateDisplay(result);
            pb.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }
    }
}

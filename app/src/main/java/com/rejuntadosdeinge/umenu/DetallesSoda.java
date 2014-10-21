package com.rejuntadosdeinge.umenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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

import java.net.URLEncoder;


public class DetallesSoda extends ActionBarActivity {

    private int soda_id = 1;
    private String nombre = "Comedor UCR";
    private String horario = "Lunes a Viernes de 7 am a 7 pm";
    private double latitud = 9.937240;
    private double longitud = -84.053132;

    LatLng latLng = null;

    boolean mMostrarMapa = false;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        // revisa si hay disponibilidad de mapa
        int estaDisponible = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if( (estaDisponible == ConnectionResult.SUCCESS) && initMap() ){
            mMostrarMapa = true;
            mMap.setMyLocationEnabled(true);
        }
        else {
            Toast.makeText(this, "No se pudo conectar con Google Play services", Toast.LENGTH_SHORT).show();
        }

        // extraer datos del intent en la actividad que recibe
        //Intent intent = getIntent();
        //soda_id = Integer.parseInt(intent.getStringExtra("soda_id"));

        desplegarDetalles();
    }

    private boolean initMap() {
        if (mMap == null) {
            MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return (mMap != null);
    }

    private void desplegarDetalles() {

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

            case R.id.mas_prod:
                Toast msg = Toast.makeText(this, "Snacks 24/7", Toast.LENGTH_LONG);
                msg.show();
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
}

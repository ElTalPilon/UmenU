package rejuntadosdeinge.com.umenu;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DetallesSoda extends ActionBarActivity {

    String idSoda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        desplegarDetallesSoda();
    }

    public void desplegarDetallesSoda(){

        // PENDIENTE
        // extraer datos del intent en la actividad que se recibe de listaPlatos
        // Intent intent = getIntent();
        // idSoda = intent.getStringExtra("idSoda");
        // idSoda la vamos a necesitar para las consultar bd y poder llenar la info de mas adelante

        TextView tv = (TextView) findViewById(R.id.nombreSoda);
        tv.setText("Poner aqui el título");

        tv = (TextView) findViewById(R.id.horarioSoda);
        tv.setText("Poner el String con el horario");

        tv = (TextView) findViewById(R.id.descripcionSoda);
        tv.setText("La implementacion para el campo descripcion permite que para textos muy largos se haga scroll por lo que siempre se puede leer la totalidad de la informacion, todo lo que se quiera poner en ambos fragmentos se insertan desde clase DetallesSoda. el horario en la base de datos es una sola cadena?, puede que se vea meor poner el boton mas productos en barra arriba para ser constantes con el diseño  ");


        //Diego
        ImageView im = (ImageView) findViewById(R.id.imMapa);
        im.setImageResource(R.drawable.mapa_prueba);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalles_soda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_info) {
            goToMasProductos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToMasProductos() {
        Toast msg = Toast.makeText(this, "Se llama a la activity de otros productos", Toast.LENGTH_LONG);
        msg.show();

        //Intent intent = new Intent(this, MasProductosSoda.class);
        //intent.putExtra("idSoda", id);
        //startActivity(intent);
    }
}
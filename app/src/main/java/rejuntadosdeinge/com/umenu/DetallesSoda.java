package rejuntadosdeinge.com.umenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class DetallesSoda extends ActionBarActivity {

    String idSoda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

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
        return super.onOptionsItemSelected(item);
    }

    public void popUpSnacks(View view) {
        Toast msg = Toast.makeText(this, "Snacks 24/7", Toast.LENGTH_LONG);
        msg.show();
    }
}
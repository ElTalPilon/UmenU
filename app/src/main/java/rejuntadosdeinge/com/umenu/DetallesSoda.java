package rejuntadosdeinge.com.umenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class DetallesSoda extends ActionBarActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button boton_mas_productos = (Button)findViewById(R.id.btn_mas_productos);
        boton_mas_productos.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Toast msg = Toast.makeText(this, "Boton presionado", Toast.LENGTH_LONG);
        msg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalles_soda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package rejuntadosdeinge.com.umenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ListaPlatos extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_platos);

        // Dummy data for the ListView.
        String[] platosArray = {
                "Plato Básico A",
                "Plato Básico B",
                "Plato Especial",
                "Plato Vegetariano"
        };

        List<String> listaPlatosProvisional = new ArrayList<String>(Arrays.asList(platosArray));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.list_item_textview,
                listaPlatosProvisional);

        final ListView listView = (ListView) this.findViewById(R.id.lista_sodas);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToDetallesPlato(listView);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_platos, menu);
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


    /**
     * Called when the user clicks the Soda Detail Button
     */
    public void goToDetallesPlato(View view){
        Intent intent = new Intent(this, DetallesPlato.class);
        startActivity(intent);
    }
}

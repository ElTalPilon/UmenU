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


public class ListaSodas extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sodas);

        // Dummy data for the ListView.
        String[] sodasArray = {
            "Facultad de Derecho",
            "Facultad de Agronomía",
            "Facultad de Odontología",
            "Facultad de Economía",
            "Estudios Generales",
            "Facultad de Letras",
            "Facultad de Educación",
            "Facultad de Ciencias Sociales"
        };

        List<String> listaSodasProvisional = new ArrayList<String>(Arrays.asList(sodasArray));

        ArrayAdapter<String> adaptadorProvisional = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                R.id.list_item_textview,
                listaSodasProvisional);

        final ListView listView = (ListView) this.findViewById(R.id.lista_sodas);
        listView.setAdapter(adaptadorProvisional);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToListaPlatos(listView);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_sodas, menu);
        return super.onCreateOptionsMenu(menu);
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

    /*
     * Called when the user clicks the Soda Detail Button
     */
    public void goToDetallesSoda(View view){
        Intent intent = new Intent(this, DetallesSoda.class);
        startActivity(intent);
    }

    /*
     * Called when the user clicks the Dishes List button
     */
    public void goToListaPlatos(View view){
        Intent intent = new Intent(this, ListaPlatos.class);
        startActivity(intent);
    }

    /*
     * Called when the user clicks the Suggest Dish button
     */
    public void popUpSugerenciaPlato(View view){
        Intent intent = new Intent(this, SugerenciaPlato.class);
        startActivity(intent);
    }
}

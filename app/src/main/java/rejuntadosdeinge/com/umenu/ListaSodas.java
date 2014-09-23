package rejuntadosdeinge.com.umenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import rejuntadosdeinge.com.umenu.data.DatosSoda;
import rejuntadosdeinge.com.umenu.data.Soda;


public class ListaSodas extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        List<Soda> sodas = new DatosSoda().getSodas();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_sodas);

        // toma datos y los pasa a la lista
        SodaAdapter adapter = new SodaAdapter(this, R.layout.list_item, sodas);

        final ListView listView = (ListView) this.findViewById(R.id.lista_sodas);
        listView.setAdapter(adapter);

   // modificar en el adapter
   //     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //         @Override
   //         public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
   //             goToListaPlatos(listView);
   //         }
   //     });
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

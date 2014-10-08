package rejuntadosdeinge.com.umenu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DetallesSoda extends ActionBarActivity {


    TextView output;
    String idSoda;
    ProgressBar pb;
    int soda_id = 1;    // valor temporal
    List<MyTask> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_soda);

        output = (TextView) findViewById(R.id.horario_soda);
        output.setMovementMethod(new ScrollingMovementMethod());

        ImageView im = (ImageView) findViewById(R.id.imMapa);
        im.setImageResource(R.drawable.mapa_prueba);

        pb = (ProgressBar) findViewById( R.id.progressBarDetallesSoda);
        pb.setVisibility(View.INVISIBLE);

        // extraer datos del intent en la actividad que recibe
        //Intent intent = getIntent();
        //soda_id = Integer.parseInt(intent.getStringExtra("soda_id"));

        tasks = new ArrayList<MyTask>();
 
        if (isOnline()) {
            requestData("http://limitless-woodland-1130.herokuapp.com/sodas");
        } else {
            Toast.makeText(this, "Red no disponible", Toast.LENGTH_LONG).show();
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
        return super.onOptionsItemSelected(item);
    }

    public void popUpSnacks(View view) {
        Toast msg = Toast.makeText(this, "Snacks 24/7", Toast.LENGTH_LONG);
        msg.show();
    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay(String msg) {
        output.append(msg + "\n");
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
            updateDisplay("Iniciando tarea");

            if (tasks.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            updateDisplay(result);

            tasks.remove(this);
            if (tasks.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            updateDisplay(values[0]);
        }

    }
}
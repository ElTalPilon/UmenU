package rejuntadosdeinge.com.umenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;



public class DetallesPlato extends ActionBarActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_calificar_plato);
        dialog.setTitle(getString(R.string.agregueComentario));
        TextView text = (TextView) dialog.findViewById(R.id.escribaComentario);
        text.setText(getString(R.string.Comentario));


        setContentView(R.layout.activity_detalles_plato);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float nota= ratingBar.getRating();
                popUpPuntuarPlato(dialog, nota);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalles_plato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void popUpPuntuarPlato(Dialog dialog, float nota) {
        RatingBar rb = (RatingBar) dialog.findViewById(R.id.ratingBarPopUp);
        rb.setRating(nota);
        dialog.show();

        //Toast msg = Toast.makeText(this, "Puntuar Plato: "+ nota, Toast.LENGTH_LONG);
        //msg.show();
    }
}

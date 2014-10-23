package com.rejuntadosdeinge.umenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import static com.rejuntadosdeinge.umenu.R.id;




public class DetallesPlato extends ActionBarActivity {
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //inicializacion del dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_calificar_plato);
        dialog.setTitle(getString(R.string.agregueComentario));
        TextView text = (TextView) dialog.findViewById(id.escribaComentario);
        text.setText(getString(R.string.Comentario));


        setContentView(R.layout.activity_detalles_plato);
        RatingBar ratingBar = (RatingBar) findViewById(id.ratingBar);
        //cuando se modifique la nota, hacer los siguente:
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float nota= ratingBar.getRating(); //obtengo la nota del RatingBar
                popUpPuntuarPlato(dialog, nota); //Convoco al popUp
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

    private void popUpPuntuarPlato(final Dialog dialog, float nota) {
        RatingBar rb = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
        //Al Rating bar del popUp ledoy la nota del fragmento, para q pueda ser modificada y enviada
        //a la base de datos (pendiente)
        rb.setRating(nota);//le doy la nota
        dialog.show(); //mostar el popUp
        //falta darle acción al Botón para q cierre el popUp y envie los datos a la base
        Button b = (Button) dialog.findViewById((id.botonComentario));
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RatingBar rb1 = (RatingBar) dialog.findViewById(id.ratingBarPopUp);
                //float notaFinal=rb1.getRating();
                /*
                 * tengo q obtener de alguna forma el id del platillo para hacer el update en la DB
                 * y cerrar el popup
                 */


                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.howtosolvenow.com"));
                startActivity(browserIntent);

            }
        });
    }
}

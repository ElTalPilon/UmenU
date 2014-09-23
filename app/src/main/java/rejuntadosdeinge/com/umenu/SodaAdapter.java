package rejuntadosdeinge.com.umenu;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rejuntadosdeinge.com.umenu.data.DatosSoda;
import rejuntadosdeinge.com.umenu.data.Soda;

public class SodaAdapter extends ArrayAdapter<Soda> {

    List<Soda> sodas = new DatosSoda().getSodas();

    public Context context;
    private List<Soda> obj;

    public SodaAdapter(Context context, int resource, List<Soda> obj) {
        super(context, resource, obj);
        this.context = context;
        this.obj = obj;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // identificamos cual dato es el que debe mostrar
        Soda soda = obj.get(position);

        // crea inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // obtener fila de inflater
        final View fila = inflater.inflate(R.layout.list_item, parent, false);

        // obtener dos elementos de la fila
        ImageView imagen = (ImageView) fila.findViewById(R.id.iv_soda);
        imagen.setImageResource(soda.imagen);

        TextView nombre = (TextView) fila.findViewById(R.id.tv_soda);
        nombre.setText(soda.nombreSoda);
        nombre.setMovementMethod(LinkMovementMethod.getInstance());
/*
        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btn_ir_detalles = (Button) fila.findViewById(R.id.btn_ir_detalles);

        btn_ir_detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(this, DetallesSoda.class);
                intent.putExtra("nombreSoda", soda.nombreSoda);
                intent.putExtra("imagen", soda.imagen);
                intent.putExtra("detalles", soda.detalles);
                startActivity(intent);

                context.startActivity(fila);


            }
        });

        */


        return fila;
    }
}


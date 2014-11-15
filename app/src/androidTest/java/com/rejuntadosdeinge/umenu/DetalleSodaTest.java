package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class DetalleSodaTest extends ActivityInstrumentationTestCase2<DetallesSoda> {
    private SharedPreferences pref;
    private DetallesSoda activity;
    TextView nombreDeLaSoda;

    public DetalleSodaTest() { super(DetallesSoda.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();
        nombreDeLaSoda = (TextView) activity.findViewById(R.id.banner_detalles_soda);

        //Suponemos que se escogio la soda de derecho de la lista de sodas
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 2);
        editor.commit();

        Thread.sleep(3000);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("DetallesSoda es null", activity);
    }

    public  void testProbarNombre() {

        String obtenido = nombreDeLaSoda.getText().toString();
        String esperado = "Derecho";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarLongitud() {

        double obtenido = activity.longitud;
        double esperado = -84.0539321899414;
        assertEquals("La longitud no concuerda.",esperado, obtenido);
    }

    public  void testProbarLatitud() {

        double obtenido = activity.latitud;
        double esperado = 9.936667442321777;
        assertEquals("La latitud no concuerda.",esperado, obtenido);
    }
}

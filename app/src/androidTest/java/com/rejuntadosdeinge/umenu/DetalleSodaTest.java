package com.rejuntadosdeinge.umenu;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class DetalleSodaTest
        extends ActivityInstrumentationTestCase2<DetallesSoda> {

    private DetallesSoda activity;
    TextView nombreDeLaSoda;

    public DetalleSodaTest() { super(DetallesSoda.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        activity = (DetallesSoda) getActivity();
        nombreDeLaSoda = (TextView) activity.findViewById(R.id.nombre_soda);
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

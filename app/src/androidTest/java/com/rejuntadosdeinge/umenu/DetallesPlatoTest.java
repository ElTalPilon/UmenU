package com.rejuntadosdeinge.umenu;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class DetallesPlatoTest
        extends ActivityInstrumentationTestCase2<DetallesPlato> {

    Globals g = Globals.getInstance();
    private DetallesPlato activity;
    TextView nombreDelPlato;
    TextView precioDelPlato;

    public DetallesPlatoTest() { super(DetallesPlato.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        //Suponemos que se escogio la soda de derecho
        g.setIdSoda(2);
        g.setCategoria("B%C3%A1sico%201");
        g.setNombreSoda("Derecho");

        activity = (DetallesPlato) getActivity();
        nombreDelPlato = (TextView) activity.findViewById(R.id.tv_nombre_plato);
        precioDelPlato = (TextView) activity.findViewById(R.id.tv_precio);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("DetallesPlato es null", activity);
    }

    public  void testProbarNombrePlato() {

        String obtenido = nombreDelPlato.getText().toString();
        String esperado = "Bistec de cerdo al tamarindo";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarPrecioPlato() {

        String obtenido = precioDelPlato.getText().toString();
        String esperado = "[Precio:\n" + "1200";
        assertEquals("El precio no concuerda.",esperado, obtenido);
    }
}


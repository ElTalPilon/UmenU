package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class DetallesPlatoTest extends ActivityInstrumentationTestCase2<DetallesPlato> {
    private SharedPreferences pref;
    private DetallesPlato activity;
    TextView nombreDelPlato;
    TextView precioDelPlato;

    public DetallesPlatoTest() { super(DetallesPlato.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = (DetallesPlato) getActivity();
        nombreDelPlato = (TextView) activity.findViewById(R.id.nombre_plato);
        precioDelPlato = (TextView) activity.findViewById(R.id.precio_plato);

        //Suponemos que se escogio la soda de derecho
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 2);
        editor.putInt("semana", 101);
        editor.putInt("dia", 1);
        editor.putString("nombrePlato", "Bolitas de carne molida en salsa de tomate");
        editor.putString("categoriaPlato", "B%C3%A1sico%201");
        editor.putString("nombreSoda", "Derecho");
        editor.commit();

        Thread.sleep(3000);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("DetallesPlato es null", activity);
    }

    public  void testProbarNombrePlato() {

        String obtenido = nombreDelPlato.getText().toString();
        String esperado = "Bolitas de carne molida en salsa de tomate";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarPrecioPlato() {

        String obtenido = precioDelPlato.getText().toString();
        String esperado = "1200";
        assertEquals("El precio no concuerda.",esperado, obtenido);
    }
}


package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class ListaSnacksTest extends ActivityInstrumentationTestCase2<ListaSnacks> {
    private SharedPreferences pref;
    private ListaSnacks activity;
    TextView nombreDeLaSoda;

    public ListaSnacksTest() { super(ListaSnacks.class); }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();

        //Suponemos que se escogio la soda de derecho de la lista de sodas
        editor.putInt("IDSoda", 2);

        activity = getActivity();
        nombreDeLaSoda = (TextView) activity.findViewById(R.id.nombre_soda);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("ListaSnacks es null", activity);
    }
}


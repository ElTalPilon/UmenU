package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class ListaSnacksTest extends ActivityInstrumentationTestCase2<ListaSnacks> {
    private SharedPreferences pref;
    private ListaSnacks activity;
    TextView snacks;

    public ListaSnacksTest() { super(ListaSnacks.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();
        snacks = (TextView) activity.findViewById(R.id.tv_snacks);

        //Suponemos que se escogio la soda de derecho
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 2);
        editor.putString("nombreSoda", "Derecho");
        editor.commit();

        Thread.sleep(3000);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("ListaSnacks es null", activity);
    }

    public  void testProbarSnacksObtenidos() {

        String obtenido = snacks.getText().toString();
        String esperado = "Gelatina 600"+"\n"+"Empanada 500"+"\n";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }
}


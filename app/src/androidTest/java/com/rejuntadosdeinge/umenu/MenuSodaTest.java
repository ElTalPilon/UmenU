package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

public class MenuSodaTest extends ActivityInstrumentationTestCase2<MenuSoda> {

    private SharedPreferences pref;
    private MenuSoda activity;


    public MenuSodaTest() { super(MenuSoda.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();

        //Suponemos que se escogio la soda de agronomía de la lista de sodas
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 4);
        editor.commit();

        Thread.sleep(3000);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("MenuSoda es null", activity);
    }

    public  void testProbarAcompañamientos() {

        String obtenido = pref.getString("acompanamiento", null);
        String esperado = "Arroz y frijoles";
        assertEquals("El acompañamiento no concuerda.",esperado, obtenido);

        obtenido = pref.getString("guarnicion", null);
        esperado = "Ayote sazón en cubos";
        assertEquals("La guarnición no concuerda.",esperado, obtenido);

        obtenido = pref.getString("ensalada1", null);
        esperado = "Vainica, coliflor y tomate";
        assertEquals("La ensalada1 no concuerda.",esperado, obtenido);

        obtenido = pref.getString("ensalada2", null);
        esperado = "Repollo con tomate y espinacas";
        assertEquals("La ensalada2 no concuerda.",esperado, obtenido);

        obtenido = pref.getString("fresco1", null);
        esperado = "Frutas";
        assertEquals("El fresco1 no concuerda.",esperado, obtenido);

        obtenido = pref.getString("fresco2", null);
        esperado = "Cas";
        assertEquals("El fresco2 no concuerda.",esperado, obtenido);

        obtenido = pref.getString("frescosinazucar", null);
        esperado = "Limón";
        assertEquals("El fresco sin azucar no concuerda.",esperado, obtenido);
    }
}

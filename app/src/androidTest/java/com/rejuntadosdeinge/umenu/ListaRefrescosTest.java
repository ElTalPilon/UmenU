package com.rejuntadosdeinge.umenu;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

public class ListaRefrescosTest extends ActivityInstrumentationTestCase2<ListaRefrescos> {
    private SharedPreferences pref;
    private ListaRefrescos activity;
    ListView view;
    Object fresco1;
    Object fresco2;
    Object frescosinazucar;

    public ListaRefrescosTest() { super(ListaRefrescos.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();
        view = (ListView) activity.findViewById(R.id.lista_frescos);

        //Suponemos que se escogio la soda de derecho
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 2);
        editor.putInt("semana", 5);
        editor.putInt("dia", 3);
        editor.putString("fresco1", "Cas");
        editor.putString("fresco2", "Naranja/Zanahoria");
        editor.putString("frescosinazucar", "Fresa");
        editor.commit();

    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("ListaRefrescos es null", activity);
    }

    public  void testProbarFresco1() {

        fresco1 = view.getItemAtPosition(0);
        String obtenido = fresco1.toString();
        String esperado = "Fresco 1: Cas";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarFresco2() {

        fresco2 = view.getItemAtPosition(1);
        String obtenido = fresco2.toString();
        String esperado = "Fresco 2: Naranja/Zanahoria";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarFrescoSinAzucar() {

        frescosinazucar = view.getItemAtPosition(2);
        String obtenido = frescosinazucar.toString();
        String esperado = "Fresco sin azúcar: Fresa";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }
}

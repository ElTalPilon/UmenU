package com.rejuntadosdeinge.umenu;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

public class ListaPlatosTest extends ActivityInstrumentationTestCase2<ListaPlatos> {
    private SharedPreferences pref;
    private ListaPlatos activity;
    ListView view;
    Object basico1;
    Object basico2;
    Object vegetariano;

    public ListaPlatosTest() { super(ListaPlatos.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();
        view = (ListView) activity.findViewById(R.id.lista_platos);

        //Suponemos que se escogio la soda de derecho
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 2);
        editor.putInt("semana", 101);
        editor.putInt("dia", 1);
        editor.putString("nombrePlato", "Bolitas de carne molida en salsa de tomate");
        editor.putString("nombreSoda", "Derecho");
        editor.commit();

        Thread.sleep(3000);
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("ListaPlatos es null", activity);
    }

    public  void testProbarBasico1() {

        basico1 = view.getItemAtPosition(0);
        String obtenido = basico1.toString();
        String esperado = "Básico 1: Bolitas de carne molida en salsa de tomate";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarBasico2() {

        basico2 = view.getItemAtPosition(1);
        String obtenido = basico2.toString();
        String esperado = "Básico 2: Lentejas con pollo";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarVegetariano() {

        vegetariano = view.getItemAtPosition(2);
        String obtenido = vegetariano.toString();
        String esperado = "Vegetariano: Chancletas ( Chayote horneado con queso blanco)";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }
}


package com.rejuntadosdeinge.umenu;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

public class ListaEnsaladasTest extends ActivityInstrumentationTestCase2<ListaEnsaladas> {
    private SharedPreferences pref;
    private ListaEnsaladas activity;
    ListView view;
    Object ensalada1;
    Object ensalada2;

    public ListaEnsaladasTest() { super(ListaEnsaladas.class); }

    @Override
    protected void setUp() throws Exception {

        super.setUp();

        activity = getActivity();
        view = (ListView) activity.findViewById(R.id.lista_ensaladas);

        //Suponemos que se escogio la soda de Agronomia
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putInt("IDSoda", 4);
        editor.putInt("semana", 5);
        editor.putInt("dia", 3);
        editor.putString("ensalada1", "Vainica, coliflor y tomate");
        editor.putString("ensalada2", "Repollo con tomate y espinacas");
        editor.commit();
    }

    public void testPreconditions() {

        //Se agrega mensaje en asserts, éstos se mostrarán si un test falla
        //y hace mas sencillo de entender por qué fallo el test
        assertNotNull("ListaEnsaladas es null", activity);
    }

    public  void testProbarEnsalada1() {

        ensalada1 = view.getItemAtPosition(0);
        String obtenido = ensalada1.toString();
        String esperado = "Vainica, coliflor y tomate";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }

    public  void testProbarEnsalada2() {

        ensalada2 = view.getItemAtPosition(1);
        String obtenido = ensalada2.toString();
        String esperado = "Repollo con tomate y espinacas";
        assertEquals("El nombre no concuerda.",esperado, obtenido);
    }
}

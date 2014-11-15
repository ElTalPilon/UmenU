package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by JosePablo on 14/11/2014.
 */
public class LoginTest extends ActivityInstrumentationTestCase2<Login> {
    private SharedPreferences pref;
    private String email;
    private String password;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Login activity;


    public LoginTest() {
        super(Login.class);
    }

    @UiThreadTest
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putBoolean("loggeado", false);
        editor.commit();

        email = "prueba@test.tst";
        password = "prueba123";
    }


    @UiThreadTest
    public void testMeh(){
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginEmailVacio() {
        activity.testSetEmail("");
        Log.e("hola", "holaEmailVacio");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginPasswordVacio() {
        activity.testSetPassword("");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginEmailInvalido() {
        mEmailView.setText("emailInvalido");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginNoHayCuenta() {
        mEmailView.setText("noHayCuenta@correo.com");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginPasswordIncorrecto() {
        mEmailView.setText(email);
        mPasswordView.setText("passwordIncorrecto");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginTodoBien() {
        mEmailView.setText(email);
        mPasswordView.setText(password);
        activity.attemptLogin();
        assertTrue(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testSignUp(){
        fail("Prueba aún no implementada");
    }
}

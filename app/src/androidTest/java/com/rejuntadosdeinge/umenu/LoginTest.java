package com.rejuntadosdeinge.umenu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Created by JosePablo on 14/11/2014.
 */
public class LoginTest extends ActivityInstrumentationTestCase2<Login> {
    private SharedPreferences pref;
    private String email;
    private String password;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Login activity;

    /*
    Para que los tests sean exitosos no se debe estar logueado, ya que la pantalla de login no aparece
     */

    public LoginTest() {
        super(Login.class);
    }

    @UiThreadTest
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        mEmailView = (AutoCompleteTextView) activity.findViewById(R.id.email);
        mPasswordView = (EditText) activity.findViewById(R.id.password);

        pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.apply();
        editor.putBoolean("loggeado", false);
        editor.commit();

        email = "prueba@test.tst";
        password = "prueba123";

        Thread.sleep(3000);
    }


    @UiThreadTest
    public void testLoginEmailVacio() {
        activity.testSetEmail("");
        //activity.mEmailView.setText("");
        //Log.e("hola", "holaEmailVacio");
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
        activity.testSetEmail("emailInvalido");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginNoHayCuenta() {
        activity.testSetEmail("noHayCuenta@correo.com");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginPasswordIncorrecto() {
        activity.testSetEmail(email);
        activity.testSetPassword("passwordIncorrecto");
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testSignUpConCuentaExistente() throws Throwable {
        super.setUp();
        mEmailView.setText(email);
        mPasswordView.setText(password);
        activity.attemptSignUp();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testSignUp() throws Throwable {
        super.setUp();
        mEmailView.setText("testprueba1@test.com");
        mPasswordView.setText(password);
        activity.attemptSignUp();
        mEmailView.setText("testPrueba1@test.com");
        mPasswordView.setText(password);
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }

    @UiThreadTest
    public void testLoginTodoBien() {

        mEmailView.setText(email);
        mPasswordView.setText(password);
        activity.attemptLogin();
        assertFalse(pref.getBoolean("loggeado", false));
    }
}

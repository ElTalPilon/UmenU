package com.rejuntadosdeinge.umenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rejuntadosdeinge.umenu.modelo.RequestPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity implements LoaderCallbacks<Cursor> {
    // Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Inicializa el editor de las SharedPreferences
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        // Si el usuario ya se autenticó anteriormente, va directo a la lista de sodas.
        if(pref.contains("loggeado")){
            if(pref.getBoolean("loggeado", false)){
                Intent intent = new Intent(this, ListaSodas.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);
            }
        }
        else{
            editor.putBoolean("loggeado", false);
            editor.apply();
        }

        // Esconde la Action Bar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setea el formulario.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailLogInButton = (Button) findViewById(R.id.email_log_in_button);
        mEmailLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Le permite al usuario ingresa al sistema pero en caso de devolverse,
     * volverá a esta vista, para procurar que se autentitique.
     */
    public void entrarComoInvitado(View v){
        Intent intent = new Intent(this, ListaSodas.class);
        startActivity(intent);
    }

    private void populateAutoComplete() {
        if(VERSION.SDK_INT >= 14) {
            getLoaderManager().initLoader(0, null, this);
        } else if(VERSION.SDK_INT >= 8) {
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    /**
     * Intenta registrar una nueva cuenta en el sistema.
     */
    public void attemptSignUp() {
        if(mAuthTask != null) {
            return;
        }

        // Resetea los errores anteriores (el focus en elementos)
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Guarda los valores al momento del intento
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Se fija que el usuario haya ingresado un password,
        // y que este cumpla con los requisitos establecidos
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Se fija que el usuario haya ingresado un correo,
        // y que este cumpla con los requisitos establecidos
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if(!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(cancel) {
            // Hay un error en el formulario.
            focusView.requestFocus();
        } else {
            // Mostrar la barra de progreso e iniciar el intento
            // de registrar la cuenta por medio de una background task.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Integer) 1);
        }
    }

    /**
     * Intenta iniciar sesión con la cuenta especificada.
     */
    public void attemptLogin() {
        if(mAuthTask != null) {
            return;
        }

        // Resetea los errores anteriores (el focus en elementos)
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Guarda los valores al momento del intento
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Se fija que el usuario haya ingresado un password
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Se fija que el usuario haya ingresado un correo,
        // y que este cumpla con los requisitos establecidos
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if(!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(cancel) {
            // Hay un error en el formulario
            focusView.requestFocus();
        } else {
            // Mostrar la barra de progreso e iniciar el intento
            // de iniciar sesión por medio de una background task.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Integer) 2);
        }
    }

    /**
     * Valida el email usando una expresión regular
     */
    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Valida que el password sea de mínimo 6 símbolos
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }


    /**
     * Según el resultado del intento de iniciar sesión o registrar cuenta,
     * muestra errores o continúa el proceso.
     */
    protected void procederSegunResultado(Integer resultadoLogin){
        switch(resultadoLogin){
            case 1:
                // [Log in] Exito
                editor.putBoolean("loggeado", true);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), ListaSodas.class);
                startActivity(intent);
                finish();
            break;
            case 2:
                // [Log in] No hay cuenta asociada al email
                mEmailView.setError(getString(R.string.error_incorrect_email));
                mEmailView.requestFocus();
            break;
            case 3:
                // [Log in] El password no coincide
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            break;
            case 4:
                // [Sign up] Ya existe una cuenta con el email
                mEmailView.setError(getString(R.string.error_account_exists));
                mEmailView.requestFocus();
            break;
            case 5:
                // [Sign up] Exito
                Toast.makeText(this, "Su cuenta ha sido creada!", Toast.LENGTH_LONG).show();
            break;
        }
    }

    /**
     * Muestra el progreso del log in / sign up.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {}

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

    /**
     * Verifica que la BD esté disponible.
     */
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(Login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void testSetEmail(String email){
        mEmailView.setText(email);
    }

    public void testSetPassword(String password){
        mPasswordView.setText(password);
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<String>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while(emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Integer, Void, Integer> {
        // TODO: Asignar un tiempo de timeout
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            Integer resultCode = 0;

            if(params[0] == 1){
                // Hay que hacer SignUp
                if(isOnline()){
                    RequestPackage p = new RequestPackage();
                    p.setMethod("POST");
                    p.setUri("https://limitless-river-6258.herokuapp.com/usuarios?opt=1&direccion=" + mEmail);
                    p.setParam("direccion", mEmail);

                    try{
                        JSONArray arr = new JSONArray(HttpManager.getData(p));
                        if(arr.length() == 0){
                            RequestPackage rp = new RequestPackage();
                            rp.setMethod("POST");
                            rp.setUri("https://limitless-river-6258.herokuapp.com/usuarios?direccion="
                                    + mEmail
                                    + "&password="
                                    + mPassword);

                            // Guarda la nueva cuenta en la BD
                            HttpManager.getData(rp);
                            resultCode = 5;
                        }
                        else{
                            resultCode = 4; // Ya hay cuentas asociadas
                        }
                    } catch(JSONException e){
                        Log.e("Password","Error en el manejo del JSON");
                    }
                }
            }
            else {
                // Hay que hacer LogIn
                if(isOnline()){
                    RequestPackage p = new RequestPackage();
                    p.setMethod("POST");
                    p.setUri("https://limitless-river-6258.herokuapp.com/usuarios?opt=1&direccion=" + mEmail);

                    try{
                        JSONArray arr = new JSONArray(HttpManager.getData(p));
                        if(arr.length() > 0){
                            JSONObject obj = arr.getJSONObject(0);
                            String pass = obj.getString("password");
                            if(mPassword.equals(pass)){
                                resultCode = 1;
                            }
                            else {
                                resultCode = 3; // Error por contraseña
                            }
                        }
                        else{
                            resultCode = 2; // Error por correo
                        }
                    } catch(JSONException e){
                        Log.e("Password","Error en el manejo del JSON");
                    }
                }
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(final Integer success) {
            mAuthTask = null;
            showProgress(false);
            procederSegunResultado(success);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}




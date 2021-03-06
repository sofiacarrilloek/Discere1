package com.jhpat.discere;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    private EditText user, pass;
    private Button mSubmit, mRegister, logD;
    private ProgressDialog pDialog;
    private CheckBox RBsesion;
    private boolean isActivateCheckBox;
    private ObjectAnimator animacionrote;
    private AnimatorSet animatorSet;
    JSONObject jsonObject;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

    private static final String LOGIN_URL = "http://100.26.2.12/discere/cas/login.php";
    private int DURACION_SPLASH = 1000;
    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1, TIPO1;
    String nom = null;
    String pas = null;
    ImageView imageLogin;

    private static final String STRING_PREFERENCE = "estadoc";
    private static final String PREFERENCE_ESTADO_BUTTON = "estado.button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

        } else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                    Toast.makeText(getApplicationContext(), "There is no connection", Toast.LENGTH_SHORT).show();

                }

                ;
            }, DURACION_SPLASH);
            Toast.makeText(getApplicationContext(), "There is no connection", Toast.LENGTH_SHORT).show();
        }

        if (obtenerestadoc()) {
            Intent i = new Intent(Login.this, pantalla_principal.class);
            startActivity(i);
            finish();
        }
        // setup input fields
        user = (EditText) findViewById(R.id.userD);
        pass = (EditText) findViewById(R.id.passD);
        imageLogin = (ImageView) findViewById(R.id.imageLogin);
        logD = findViewById(R.id.button_login);


        // register listeners

        logD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animacion("rotation");
                new AttemptLogin().execute();

                datosc(user.getText().toString());

            }
        });


        //Don´t out sesion
        RBsesion = (CheckBox) findViewById(R.id.RBSecion);
        isActivateCheckBox = RBsesion.isChecked(); //DESACTIVADO

        RBsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivateCheckBox) {
                    RBsesion.setChecked(false);
                }
                isActivateCheckBox = RBsesion.isChecked();

            }
        });

    }

    public static void cambiarEstado(Context c, boolean b) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON, b).apply();
    }

    public void estadoCheckBox() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON, RBsesion.isChecked()).apply();

    }

    public boolean obtenerestadoc() {
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE, MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON, false);
    }

    class AttemptLogin extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
//Aqui hace la encriptación a md5 y luego a Base64
            MessageDigest md = null;
            final String MD5 = "MD5";
            try {
                md = java.security.MessageDigest
                        .getInstance(MD5);
                //step 2
            } catch (NoSuchAlgorithmException e) {
            }
            try {
                md.update(password.getBytes("UTF-8"));//step 3
            } catch (UnsupportedEncodingException e) {
            }
            byte raw[] = md.digest(); //step 4
            String hash = new String(Base64.encodeBase64(raw));
            //Base64.decodeBase64(hash.getBytes());

            try {


                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", hash));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();
                    Intent i = new Intent(Login.this, pantalla_principal.class);
                    i.putExtra("hola", EMAIL1);
                    finish();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarPreferencias() {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String parametroUsu = user.getText().toString();
        String contraUsu = pass.getText().toString();


        SharedPreferences.Editor editor = preferencia.edit();
        editor.clear();
        editor.putString("user", parametroUsu);
        editor.putString("password", contraUsu);
        editor.putString("NAME2", NAME1);
        editor.putString("LAST_NAME2", LAST_NAME1);
        editor.putString("GENDER2", GENDER1);
        editor.putString("ID2", ID1);
        editor.putString("PASSWORD2", PASSWORD1);
        editor.putString("EMAIL2", EMAIL1);
        editor.putString("TEL2", TEL1);

        editor.commit();


    }

    public void datosc(String Correo) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://100.26.2.12/discere/usuario_nombre.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("correo", Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) // Lo mismo que con LOGIN
                {


                    estadoCheckBox();
                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                        LAST_NAME1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("last_name");
                        NAME1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("name");
                        ID1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("id_");
                        PASSWORD1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("password");
                        GENDER1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("gender");
                        EMAIL1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("email");
                        TEL1 = jsonObject.getJSONArray("datos").getJSONObject(0).getString("telephone_number");


                        guardarPreferencias();
                        obtenTipo(ID1);

                    } catch (JSONException e) {

                    }
                } else {
                    Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN DATOSSC


    public void obtenTipo(String Correo) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://100.26.2.12/discere/cas/obtener_tipo_usuario.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("correo", Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (statusCode == 200) // Lo mismo que con LOGIN
                {

                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                        final String TIPO = jsonObject.getJSONArray("tipo").getJSONObject(0).getString("id_role");

                        if (TIPO.equalsIgnoreCase("17")) {
                            TIPO1 = "Coach";
                        }
                        if (TIPO.equalsIgnoreCase("24")) {
                            TIPO1 = "Fellow";
                        }
                        if (TIPO.equalsIgnoreCase("18")) {
                            TIPO1 = "Speaker";
                        }

                        guardarPreferencias2();
                    } catch (JSONException e) {

                    }
                } else {
                    Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN DATOSSC


    private void guardarPreferencias2() {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("TIPO2", TIPO1);
        editor.commit();

    }

    private void animacion(String animacion) {
        switch (animacion) {
            case "rotation":
                animacionrote = ObjectAnimator.ofFloat(imageLogin, "rotation", 0f, 360f);
                animacionrote.setDuration(500);
                AnimatorSet animatorSetRotation = new AnimatorSet();
                animatorSetRotation.play(animacionrote);
                animatorSetRotation.start();
                break;
        }
    }
}
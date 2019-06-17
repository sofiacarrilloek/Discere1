package com.jhpat.discere;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    private EditText user, pass;
    private Button mSubmit, mRegister,logD;
    private ProgressDialog pDialog;
    private CheckBox RBsesion;
    private boolean isActivateCheckBox;

    JSONObject jsonObject;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

    private static final String LOGIN_URL = "http://puntosingular.mx/cas/login.php";
            private int DURACION_SPLASH=3000;
    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1, TIPO1;
    String nom = null;
    String pas = null;

    private static final String STRING_PREFERENCE = "estadoc";
    private static final String PREFERENCE_ESTADO_BUTTON = "estado.button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnectedOrConnecting()){

            Toast.makeText(getApplicationContext(),"Conectado",Toast.LENGTH_SHORT).show();
        }else{
            new Handler().postDelayed(new Runnable(){
                public void run(){
                    // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                    Intent intent = new Intent(Login.this, ErrorConexion.class);
                    startActivity(intent);
                    finish();
                };
            }, DURACION_SPLASH);
            Toast.makeText(getApplicationContext(),"Falta Internet",Toast.LENGTH_SHORT).show();
        }
        if (obtenerestadoc()){
            Intent i = new Intent(Login.this, pantalla_principal.class);
            startActivity(i);
            finish();
        }
        if (obtenerestadoc()){
            Intent i = new Intent(Login.this, pantalla_principal.class);
            startActivity(i);
            finish();
        }
        // setup input fields
        user = (EditText) findViewById(R.id.userD);
        pass = (EditText) findViewById(R.id.passD);
        logD =  findViewById(R.id.button_login);

        // register listeners

        logD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttemptLogin().execute();

                datosc(user.getText().toString());
            }
        });




        //Don´t out sesion
        RBsesion = (CheckBox) findViewById(R.id.RBSecion);
        isActivateCheckBox = RBsesion.isChecked(); //DESACTIVADO

        RBsesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isActivateCheckBox){
                    RBsesion.setChecked(false);
                }
                isActivateCheckBox = RBsesion.isChecked();

            }
        });

    }

    public static void cambiarEstado(Context c,boolean b){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON,b).apply();
    }
    public void estadoCheckBox(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        preferences.edit().putBoolean(PREFERENCE_ESTADO_BUTTON,RBsesion.isChecked()).apply();

    }

    public boolean obtenerestadoc(){
        SharedPreferences preferences = getSharedPreferences(STRING_PREFERENCE,MODE_PRIVATE);
        return preferences.getBoolean(PREFERENCE_ESTADO_BUTTON,false);
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




            try {



                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

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
                    i.putExtra("hola",EMAIL1);
                    finish();
                    startActivity(i);

                    //---------------------
                    Intent noti = new Intent(Login.this, contenedor_notificaciones.class);
                    noti.putExtra("noti", EMAIL1);
                    startActivity(noti);
                    //----------------------

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

    private void guardarPreferencias()
    {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String parametroUsu = user.getText().toString();
        String contraUsu = pass.getText().toString();


        SharedPreferences.Editor editor = preferencia.edit();
        //editor.clear();
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

    public void datosc (String Correo)
    {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/usuario_nombre.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams =new RequestParams();
        requestParams.add("correo",Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                if (statusCode==200) // Lo mismo que con LOGIN
                {


                    estadoCheckBox();
                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                        LAST_NAME1= jsonObject.getJSONArray("datos").getJSONObject(0).getString("last_name");
                        NAME1= jsonObject.getJSONArray("datos").getJSONObject(0).getString("name");
                        ID1=jsonObject.getJSONArray("datos").getJSONObject(0).getString("id_");
                        PASSWORD1=jsonObject.getJSONArray("datos").getJSONObject(0).getString("password");
                        GENDER1= jsonObject.getJSONArray("datos").getJSONObject(0).getString("gender");
                        EMAIL1  = jsonObject.getJSONArray("datos").getJSONObject(0).getString("email");
                        TEL1= jsonObject.getJSONArray("datos").getJSONObject(0).getString("telephone_number");



                        guardarPreferencias();
                        obtenTipo(ID1);

                    } catch (JSONException e) {

                    }
                }

                else
                {
                    Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

                Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN DATOSSC



    public void obtenTipo (String Correo)
    {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obtener_tipo_usuario.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams =new RequestParams();
        requestParams.add("correo",Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                if (statusCode==200) // Lo mismo que con LOGIN
                {

                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                       final String TIPO= jsonObject.getJSONArray("tipo").getJSONObject(0).getString("id_role");

                       if (TIPO.equalsIgnoreCase("17"))
                       {
                           TIPO1="Coach";
                       }
                        if (TIPO.equalsIgnoreCase("24"))
                        {
                            TIPO1="Fellow";
                        }
                        if (TIPO.equalsIgnoreCase("18"))
                        {
                            TIPO1="Speaker";
                        }

                        guardarPreferencias2();
                    } catch (JSONException e) {

                    }
                }

                else
                {
                    Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

                Toast.makeText(Login.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN DATOSSC


    private void guardarPreferencias2()
    {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("TIPO2", TIPO1);
        editor.commit();

    }




}
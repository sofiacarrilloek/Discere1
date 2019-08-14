package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jhpat.discere.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Taudio_defect extends AppCompatActivity {


    JSONObject jsonObject;
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE


    EditText DX1, DPX1, DTX1, DDX1;
    EditText DX2, DPX2, DTX2, DDX2;
    EditText DX3, DPX3, DTX3, DDX3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabla_audio_defect);

        DX1 = (EditText) findViewById(R.id.Duration1);
        DPX1 = (EditText) findViewById(R.id.DP1);
        DTX1 = (EditText) findViewById(R.id.DT1);
        DDX1 = (EditText) findViewById(R.id.DD1);

        DX2 = (EditText) findViewById(R.id.Duration2);
        DPX2 = (EditText) findViewById(R.id.DP2);
        DTX2 = (EditText) findViewById(R.id.DT2);
        DDX2 = (EditText) findViewById(R.id.DD2);

        DX3 = (EditText) findViewById(R.id.Duration3);
        DPX3 = (EditText) findViewById(R.id.DP3);
        DTX3 = (EditText) findViewById(R.id.DT3);
        DDX3 = (EditText) findViewById(R.id.DD3);
        datosc("709");

    }
    public void regresarX (View view){
        Intent abrir_tabla2=new Intent (Taudio_defect.this, Prueba.class);
        startActivity(abrir_tabla2);
    }

    public void datosc(String Correo) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://puntosingular.mx/cas/audio_defect.php"; //la url del web service
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


                        DX1.setText(jsonObject.getJSONArray("datos").getJSONObject(0).getString("duration"));
                        DPX1.setText(jsonObject.getJSONArray("datos").getJSONObject(0).getString("defect_priority"));
                        DTX1.setText(jsonObject.getJSONArray("datos").getJSONObject(0).getString("defect_type"));
                        DDX1.setText(jsonObject.getJSONArray("datos").getJSONObject(0).getString("defect_description"));
                        //___________________________________________________________________________________________________________
                        DX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("duration"));
                        DPX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("defect_priority"));
                        DTX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("defect_type"));
                        DDX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("defect_description"));






                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Taudio_defect.this, "" + e, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN DATOSSC


    /*private void cargarP() {
        SharedPreferences preferencia = this.getApplicationContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        ID1 = preferencia.getString("ID2", "NO EXISTE");



        datosc(ID1);}*/


    //Fin cargar preferencias

}
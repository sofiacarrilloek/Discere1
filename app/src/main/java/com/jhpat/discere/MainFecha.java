package com.jhpat.discere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainFecha extends AppCompatActivity{
    public static String id_fellow;
    JSONObject jsonObject;
    JSONObject jsonObjecte;
    String id_F="";
    RequestQueue requestQueue;
    String url="http://puntosingular.mx/cas/tabla/Lista_fechas.php?id_fellow"+id_F+"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);


        final ListView lv=(ListView) findViewById(R.id.lvf);
        final DownloaderF d=new DownloaderF(this,url,lv);

        d.execute();
        cargarP();
        obtenIDFELLOW(id_fellow);


    }
    private  void cargarP() {
        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id_fellow= preferencia.getString("ID2", "NO EXISTE");
    }
    public void obtenIDFELLOW (String ID_USER)
    {
        //Para el fellow
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();



        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/Lista_nombre_fellow.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_user",ID_USER); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA="";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_fellows[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_fellows[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_fellows[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    Toast.makeText(MainFecha.this, "LOADING...", Toast.LENGTH_SHORT).show();

                    id_F=CONSULTA;
                    //datosEnEspera(CONSULTA);
                    //datosLessons(CONSULTA);



                } catch (JSONException e) {
                    Toast.makeText(MainFecha.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN OBTENIDFELLOW

}

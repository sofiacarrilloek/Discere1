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

public class MainLista extends AppCompatActivity {
    public static String id_user;
    JSONObject jsonObject;
    JSONObject jsonObjecte;
    String id_U="";



    String url="http://puntosingular.mx/cas/tabla/Lista_nombre?id_teacher="+id_U+"";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lista);
        FloatingActionButton botonFecha = findViewById(R.id.fabotton1);
        botonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLista.this, MainFecha.class);
                startActivity(intent);
            }
        });


        final ListView lv=(ListView) findViewById(R.id.lv);
        final Downloader d=new Downloader(this,url,lv);

        d.execute();

        cargarP();
        obtenIDTEACHER(id_user);

    }
    private  void cargarP() {
        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
         id_user= preferencia.getString("ID2", "NO EXISTE");
    }
    public void obtenIDTEACHER (String ID_USER)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/calendar/obten_id_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_user",ID_USER); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    String CONSULTA="";
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_teachers[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_teachers[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_teachers[i];
                        if (cuentaOr < tamanio - 1) {
                            CONSULTA = CONSULTA + OR;
                        }


                        cuentaOr++;

                    }

                    id_U=CONSULTA;
                    //datosLessonsTeacher(CONSULTA);
                    //datosEnEsperaTeacher(CONSULTA);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainLista.this, "Error..."+e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN OBTEN_ID_TEACHER

}

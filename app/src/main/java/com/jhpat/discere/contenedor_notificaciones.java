package com.jhpat.discere;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class contenedor_notificaciones extends AppCompatActivity {

    JSONObject jsonObject1;
    ArrayList<String> listDatos;
    RecyclerView recycler;
    String id01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notificaciones);


        recycler = (RecyclerView) findViewById(R.id.reNoti);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listDatos = new ArrayList<String>();

        cargarP();


    }

    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id01 = preferencia.getString("ID2", "No Existe");
        datoNoti(id01);

    }

    public void datoNoti (String Correo)
    {


        AsyncHttpClient conexioNoti = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obtener_notificacion.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("email",Correo); //envio el parametro

        conexioNoti.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {



                    try {
                        jsonObject1 = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice


                        int tamanio = jsonObject1.getJSONArray("datos").length();
                        String message[] = new String[tamanio];


                        for (int i = 0; i < tamanio; i++) {
                            message[i] = jsonObject1.getJSONArray("datos").getJSONObject(i).getString("message");
                             if(message[i]=="1"){
                                 message[i]= "Sesión aceptada";
                             }
                            if (message[i]=="2"){
                                message[i]="Sesión rechazada";
                            }

                            listDatos.add(message[i]);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                Adaptar_notificaciones adapter = new Adaptar_notificaciones(listDatos);
                recycler.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });



    }
}

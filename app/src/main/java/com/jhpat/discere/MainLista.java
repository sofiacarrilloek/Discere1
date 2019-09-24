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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainLista extends AppCompatActivity {


    private String id,c,n,ape;

    private String id2,id3,id4,id5,id6;
    RequestQueue requestQueue;
    String url;

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
        cargarp2();
        cargarPreferencias();

    }
    public void cargarp2(){
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id=preferencia.getString("ID2", "NO EXISTE");
        obtener_en_fellow("http://puntosingular.mx/cas/tabla/elchido?user="+id+"");

    }
    public void obtener_en_fellow(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject=response.getJSONObject(i);
                        id2=jsonObject.getString("id_teacher");
                        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferencia.edit();
                        editor.clear();
                        editor.putString("Id_A", id2);
                        editor.commit();
                        //Toast.makeText(getApplicationContext(),"Hola"+id2,Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "NO eres fellow intentalo con la cueta de un fellow", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
//
        FloatingActionButton cv;

        cv =  (FloatingActionButton) findViewById(R.id.GF);

        cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Grafico.class);
                intent.putExtra("tam", id2);
                startActivity(intent);
            }
        });
        //

    }
    public void cargarAudioDefect(String ID_AUDIO_ANALYST) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        url = "http://puntosingular.mx/cas/tabla/Lista_nombre.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_teacher", ID_AUDIO_ANALYST);}

    private void cargarPreferencias()
    {
        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);
        String id_Analyst = preferencia.getString("Id_A", "NO EXISTE");
        cargarAudioDefect(id_Analyst);
    }
}

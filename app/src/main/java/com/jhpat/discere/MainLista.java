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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id_teacher;
        SharedPreferences preferencia =getSharedPreferences("id_teacher", Context.MODE_PRIVATE);
        id_teacher= preferencia.getString("teacher", "NO EXISTE");
        Toast.makeText(getApplicationContext(),"hola"+id_teacher,Toast.LENGTH_LONG).show();

        String url="http://puntosingular.mx/cas/tabla/Lista_nombre.php?id_teacher="+id_teacher+"";


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


    }


}
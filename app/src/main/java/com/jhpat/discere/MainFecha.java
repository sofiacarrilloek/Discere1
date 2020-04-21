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

    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);
        String id_fellow;
        SharedPreferences preferencia =getSharedPreferences("id_fellow", Context.MODE_PRIVATE);
        id_fellow= preferencia.getString("sariel", "NO EXISTE");
        Toast.makeText(getApplicationContext(),"Hola"+id_fellow,Toast.LENGTH_LONG).show();
        String url="http://100.26.2.12/discere/Lista_fechas.php?id_fellow="+id_fellow+"";



        final ListView lv=(ListView) findViewById(R.id.lvf);
        final DownloaderF d=new DownloaderF(this,url,lv);

        d.execute();



    }

}

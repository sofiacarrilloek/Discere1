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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainFecha extends AppCompatActivity{
    private String id,c,n,ape;

    private String id2,id3,id4,id5,id6;
    RequestQueue requestQueue;
    String url="http://puntosingular.mx/cas/tabla/Lista_fechas.php?id_fellow=6028";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);


        final ListView lv=(ListView) findViewById(R.id.lvf);
        final DownloaderF d=new DownloaderF(this,url,lv);

        d.execute();


    }

}

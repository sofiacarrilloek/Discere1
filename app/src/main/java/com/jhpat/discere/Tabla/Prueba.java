package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jhpat.discere.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Prueba extends AppCompatActivity {
    private String id,c,n,ape;

    private String id2,id3,id4,id5,id6;
    RequestQueue requestQueue;
    TableView<String[]> tb;
    TableHelper tableHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        tableHelper=new TableHelper(this);
        tb=(TableView<String[]>)findViewById(R.id.tableview);
        tb.setColumnCount(3);
        tb.setHeaderBackgroundColor(Color.parseColor("#5d9b9b"));
        tb.setHeaderAdapter(new SimpleTableHeaderAdapter(this,tableHelper.getSpace()));
        new MySQLClient(Prueba .this).retrieve(tb);
         cargarp2();


       //Toast.makeText(getApplicationContext(),"hola "+id,Toast.LENGTH_LONG).show();

    }
    public void siguiente_tabla(View view){
        Intent abrir_tabla2=new Intent (Prueba.this, Taudio_defect.class);
        startActivity(abrir_tabla2);
    }
    public void cargarp2(){
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id=preferencia.getString("ID2", "NO EXISTE");
        obtener_en_fellow("http://puntosingular.mx/cas/tabla/obtener_fellow.php?user=" + id + "");

    }
    public void obtener_en_fellow(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject=response.getJSONObject(i);
                        id2=jsonObject.getString("id_");
                        obtener_en_lesson("http://puntosingular.mx/cas/tabla/obtener_lesson.php?id_fellow="+id2+"");
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
    }


    public void obtener_en_lesson(String URL2){
        JsonArrayRequest jsonA=new JsonArrayRequest(URL2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonO = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonO=response.getJSONObject(i);
                        id3=jsonO.getString("id_");
                        obtener_en_lessonresult("http://puntosingular.mx/cas/tabla/obtener_lessonR.php?id_lesson="+id3+"");
                        //Toast.makeText(getApplicationContext(),"el id de la tabla leson es:"+id3,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /// Toast.makeText(getApplicationContext(), "NO eres fellow intentalo con la cueta de un fellow", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonA);
    }


    public void obtener_en_lessonresult(String URL){
        JsonArrayRequest jsonA=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonO = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonO=response.getJSONObject(i);
                        id4=jsonO.getString("id_");
                        //guardarPreferencias_tabla(id4);
                        obtener_en_audio("http://puntosingular.mx/cas/tabla/obtener_audio.php?id_lesson_result="+id4+"");
                        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferencia.edit();
                        editor.clear();
                        editor.putString("preferencia1",id4);
                        editor.apply();

                         Toast.makeText(getApplicationContext(),"HOla"+id4,Toast.LENGTH_LONG).show();
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
        requestQueue.add(jsonA);
    }

    public void obtener_en_audio(String URL){
        JsonArrayRequest jsonA=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonO = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonO=response.getJSONObject(i);
                        id5=jsonO.getString("id_");
                        //guardarPreferencias_tabla(id5);
                        //Toast.makeText(getApplicationContext(),"HOla"+id5,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        requestQueue.add(jsonA);
    }
    private void guardarPreferencias_tabla(String ID)
    {

        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        //editor.clear();
        editor.putString("Id_A", ID);
        //Toast.makeText(getApplicationContext(),"SE a guardado"+ID,Toast.LENGTH_LONG).show();




    }





}

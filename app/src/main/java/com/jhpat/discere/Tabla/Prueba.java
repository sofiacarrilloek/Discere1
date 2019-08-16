package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private String id2;
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
    public void cargarp2(){
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id=preferencia.getString("ID2", "NO EXISTE");
        obtener_en_fellow("http://puntosingular.mx/cas/tabla/elchido.php?user=" + id + "");

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
                        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = preferencia.edit();
                        editor.remove("Id_A");
                        editor.putString("Id_A", id2).clear();
                        //Toast.makeText(getApplicationContext(),"SE a guardado"+ID,Toast.LENGTH_LONG).show();
                        editor.commit();

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



    private void guardarPreferencias_tabla(String ID)
    {

        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        editor.remove("Id_A");
        editor.putString("Id_A", ID);
        //Toast.makeText(getApplicationContext(),"SE a guardado"+ID,Toast.LENGTH_LONG).show();
        editor.commit();



    }





}

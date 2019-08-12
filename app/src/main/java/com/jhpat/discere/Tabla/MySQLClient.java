package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class MySQLClient {

    private final Context c;
    private String id;
    private SimpleTableDataAdapter adapter;
    private String Data_RETRIVE_URL="";

    public MySQLClient(Context c){
        this.c=c;
        cargarDatosTabla();
    }
    public void cargarDatosTabla(){
        SharedPreferences preferencia =c.getSharedPreferences("Credenciales2", Context.MODE_PRIVATE);
        id=preferencia.getString("Id_A", "NO EXISTE");
        Data_RETRIVE_URL="http://puntosingular.mx/cas/tabla/obtener_audio_defec.php?id_audio_analyst="+id+"";
        Toast.makeText(c.getApplicationContext(),"hola"+id,Toast.LENGTH_LONG).show();
    }


    public void retrieve(final TableView tb){
        final ArrayList<Spacecraft>spacecrafts=new ArrayList<>();

        AndroidNetworking.get(Data_RETRIVE_URL)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jo;
                        Spacecraft s;
                        try {
                            for(int i=0;i<response.length();i++){
                                jo=response.getJSONObject(i);

                                int id_audio_analyst=jo.getInt("id_audio_analyst");
                                String name= jo.getString("defect_priority");
                                String hola=jo.getString("defect_type");
                                String destinatio=jo.getString("defect_description");

                                s=new Spacecraft();
                                s.setId_(id_audio_analyst);
                                s.setDefect_priority(name);
                                s.setDefect_type(hola);
                                s.setDefect_description(destinatio);
                                spacecrafts.add(s);
                            }

                            adapter=new SimpleTableDataAdapter(c,new TableHelper(c).returnSpaceArray(spacecrafts));
                                tb.setDataAdapter(adapter);
                        }catch (JSONException e){
                            Toast.makeText(c,"GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIEVED. "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(c,"UNSUCCESSFUL : ERROR IS : "+anError.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
    }

}

package com.jhpat.discere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jhpat.discere.Tabla.Mainlista2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser extends AsyncTask<Void,Integer,Integer> {
    Context c;
    ListView lv;
    String data;
    ArrayList<String>players=new ArrayList<>();
    ProgressDialog pd;
    String id_fellow;
    ArrayList<String>fellow=new ArrayList<>();



    public Parser(Context c,  String data,ListView lv) {
        this.c = c;
        this.lv = lv;
        this.data = data;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd= new ProgressDialog(c);
        pd.setTitle("Parser");
        pd.setMessage("Parsing.... PLease wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(final Integer integer) {

        super.onPostExecute(integer);
        if(integer==1){
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,players);
            lv.setAdapter(adapter);
            pd.dismiss();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent ca=new Intent(c.getApplicationContext(),MainFecha.class);
                    SharedPreferences preferencia = c.getSharedPreferences("id_fellow", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencia.edit();
                    editor.putString("sariel", fellow.get(i));
                    editor.commit();
                    c.startActivity(ca);
                    Toast.makeText(c,"hola"+fellow.get(i),Toast.LENGTH_LONG).show();
                    //Toast.makeText(c,"hola"+,Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Toast.makeText(c,"Unable to parse",Toast.LENGTH_SHORT).show();
        }


    }
    private int parse(){
        try {
            JSONArray ja=new JSONArray(data);
            JSONObject jo=null;

            players.clear();

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                String name=jo.getString("name");
                id_fellow=jo.getString("id_fellow");
                players.add(name);
                fellow.add(id_fellow);
            }
            return 1;
        }catch (JSONException e){

        }
        return 0;
    }

}

package com.jhpat.discere.Tabla;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Parser2 extends AsyncTask<Void,Integer,Integer> {
    Context c2;
    ListView lv2;
    String data;
    ArrayList<String> players2=new ArrayList<>();
    ProgressDialog pd2;




    public Parser2(Context c,  String data,ListView lv) {
        this.c2 = c;
        this.lv2 = lv;
        this.data = data;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd2= new ProgressDialog(c2);
        pd2.setTitle("Parser");
        pd2.setMessage("Parsing.... PLease wait");
        pd2.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parse2();
    }

    @Override
    protected void onPostExecute(final Integer integer) {

        super.onPostExecute(integer);
        if(integer==1){
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(c2,android.R.layout.simple_list_item_1,players2);
            lv2.setAdapter(adapter);
            pd2.dismiss();
            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(c2,"hola"+players2.get(i),Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Toast.makeText(c2,"Unable to parse",Toast.LENGTH_SHORT).show();
        }


    }
    private int parse2(){
        try {
            JSONArray je=new JSONArray(data);
            JSONObject ji=null;

            players2.clear();

            for(int i=0;i<je.length();i++){
                ji=je.getJSONObject(i);
                String dia=ji.getString("create_date");

                players2.add(dia);
            }
            return 1;
        }catch (JSONException e){

        }
        return 0;
    }

}

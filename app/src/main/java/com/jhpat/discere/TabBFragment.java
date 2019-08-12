package com.jhpat.discere;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class TabBFragment extends Fragment {

    Spinner spinner;
    AsyncHttpClient client;
    View vista;

    public TabBFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_tab_b, container, false);
        spinner = (Spinner) vista.findViewById(R.id.SpinnerFellows);
        client = new AsyncHttpClient();

        llenarSpinner();


        return vista;
    }

    public void llenarSpinner(){
        String url="http://puntosingular.mx/cas/obtener_fellows.php";
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    cargarSpiner(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarSpiner(String s) {
        ArrayList<Nombres> lista = new ArrayList<Nombres>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for (int i=0; i<jsonArray.length();i++){
                Nombres n = new Nombres();
                n.setName(jsonArray.getJSONObject(i).getString("name"));
                lista.add(n);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        cargarAudio();
    }

    private void cargarAudio() {

    }

    private class Nombres{
        private int id;
        private String name;
        private String last_name;

        public Nombres(){

        }

        public Nombres(int id, String name, String last_name){
            this.id = id;
            this.name = name;
            this.last_name = last_name;
        }

        public void setId(int id){
            this.id=id;
        }

        public void setName(String name){
            this.name=name;
        }

        public void setLast_name(String last_name){
            this.last_name=last_name;
        }

        @Override
        public String toString(){
            return name;
        }
    }

}

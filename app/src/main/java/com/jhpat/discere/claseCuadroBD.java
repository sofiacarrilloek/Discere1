package com.jhpat.discere;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class claseCuadroBD  extends AppCompatActivity {
    //Variables
    JSONObject jsonObject;
  /*  EditText editText_id;
    TextView textView_Star, textView_End, tDia, tMes, tAnio, tTipo;
    Button button;
    String fechaInicio, anio="", mes="", dia="", hora="", minuto="";
    String fechaFinal, anio2="", mes2="", dia2="", hora2="", minuto2="", sTipo; */
   String fecha;

   private String nombreE;
   private String fechaE;
   private String horaIE;
   private String horaFE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuadrodialogo2);

        //Inicio Spinner
        final Spinner spf = (Spinner) findViewById(R.id.spinnerFechas);
              Button r = (Button) findViewById(R.id.botonr);
              Button x = (Button) findViewById(R.id.botonx);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.fechas, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spf.setAdapter(adapter);
        spf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String f= spf.getSelectedItem().toString();
                fecha = f;

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Fin Spinner


     /*   button = (Button) findViewById(R.id.button);

        editText_id = (EditText)findViewById(R.id.et_id);
        tDia=(TextView)findViewById(R.id.tv_dia);
        tAnio=(TextView)findViewById(R.id.tv_anio);
        tMes=(TextView)findViewById(R.id.tv_mes);
        tTipo=(TextView)findViewById(R.id.tv_tipo); */


        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerFecha(fecha);
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(claseCuadroBD.this, dialogo.class);
                i.putExtra("Rnombre", nombreE);
                i.putExtra("Rfecha", fechaE);
                i.putExtra("RhoraI", horaIE);
                i.putExtra("RhoraF", horaFE);


                startActivity(i);

            }
        });

    }


    public void obtenerFecha (String Correo)
    {
//Conexion
        AsyncHttpClient conexion = new AsyncHttpClient();
         final String url ="http://100.26.2.12/discere/obtenerS.php"; //la url del web service
        final RequestParams requestParams =new RequestParams();
        requestParams.add("fecha",Correo); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice

                    nombreE=jsonObject.getJSONArray("datos").getJSONObject(0).getString("name_fellow");
                    fechaE=jsonObject.getJSONArray("datos").getJSONObject(0).getString("date");
                    horaIE=jsonObject.getJSONArray("datos").getJSONObject(0).getString("start_time");
                    horaFE=jsonObject.getJSONArray("datos").getJSONObject(0).getString("end_time");






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }


}

package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jhpat.discere.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Taudio_defect extends AppCompatActivity {

    private String id,c,n,ape;
    PieChart pieChart;
    int[] colorClassArray = new int[]{Color.LTGRAY,Color.BLUE,Color.CYAN};
    int[] sale = new int[]{29,23,22};

    JSONObject jsonObject;
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE


    TextView DX1, DPX1, DTX1, DDX1;
    EditText DX2, DPX2, DTX2, DDX2;
    EditText DX3, DPX3, DTX3, DDX3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabla_audio_defect);

        DX1 = (TextView) findViewById(R.id.Duration1);
        DPX1 = (TextView) findViewById(R.id.DP1);
        DTX1 = (TextView) findViewById(R.id.DT1);
        DDX1 = (TextView) findViewById(R.id.DD1);



        pieChart = findViewById(R.id.pieChart);
        PieDataSet pieDataSet = new PieDataSet(Entries(),"");
        pieDataSet.setColors(colorClassArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setHoleRadius(10);
        pieChart.invalidate();

        cargarp2();
        //

    }
    private ArrayList<PieEntry> dataValuesl(){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry(15,"Sun"));
        dataVals.add(new PieEntry(34,"Mon"));
        dataVals.add(new PieEntry(56,"Tue"));
        return dataVals;
    }
    private ArrayList<PieEntry> Entries() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < sale.length; i++)
            entries.add(new PieEntry(sale[i]));
        return entries;

    }
    public void cargarp2(){
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id=preferencia.getString("ID2", "NO EXISTE");
        datosc("http://puntosingular.mx/cas/tabla/elchido?user="+id+"");

    }
    public void datosc(String Correo) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://puntosingular.mx/cas/audio_defect.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("correo", Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) // Lo mismo que con LOGIN
                {


                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice

                        int tamanio=0;
                        tamanio = jsonObject.getJSONArray("datos").length();
                        String type="";
                        String type2="";
                        String type3="";
                        String type4="";
                        String duration [] = new String[tamanio];
                        String defect_priority [] = new String[tamanio];
                        String defect_type [] = new String[tamanio];
                        String defect_description [] = new String[tamanio];


                        for (int i=0; i<tamanio; i++)
                        {
                            duration[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("duration");
                            defect_priority[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_priority");
                            defect_type[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_type");
                            defect_description[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_description");

                            type=type+duration[i]+"\n"+"\n";
                            type2=type2+defect_priority[i]+"\n"+"\n";
                            type3=type3+defect_type[i]+"\n"+"\n";
                            type4=type4+defect_description[i]+"\n"+"\n";
                        }



                       // _________________________________________________________________________________________________________


                        DX1.setText(""+type);
                        DPX1.setText(""+type2);
                        DTX1.setText(""+type3);
                        DDX1.setText(""+type4);

                       /* DTX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("defect_type"));
                        DDX2.setText(jsonObject.getJSONArray("datos").getJSONObject(1).getString("defect_description"));
*/





                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Taudio_defect.this, "ERROR: " + e, Toast.LENGTH_SHORT).show();
                    }


                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });




    }//FIN DATOSSC
    private void guardarPreferencias_tabla(String ID)
    {

        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        //editor.clear();
        editor.putString("Id_A", ID);
        //Toast.makeText(getApplicationContext(),"SE a guardado"+ID,Toast.LENGTH_LONG).show();




    }


}



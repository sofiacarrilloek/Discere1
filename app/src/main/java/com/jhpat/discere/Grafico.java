package com.jhpat.discere;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jhpat.discere.Tabla.Taudio_defect;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Grafico extends AppCompatActivity {
    PieChart pieChart;
    int[] colorClassArray = new int[]{Color.RED,Color.YELLOW};
    int[] sale = new int[]{29,23,22};
    JSONObject jsonObject;
    //Pie
    int t_Phontics=0, t_Grammar=0, t_Sintaxt=0, t_Meaning=0;
    double p_PH=0, p_GR=0, p_SI=0, p_ME=0, t_p=0;

    String color_Phonetics="#fe6383", color_Grammar="#63ff84", color_Sintax="#7fff63",
            color_Meaning="#8663fc";


    //Pie


    ArrayList<PieEntry> dataV = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);


        cargarPreferencias();
    }



    public void cargarAudioDefect(String ID_AUDIO_ANALYST) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://puntosingular.mx/cas/audio_defect.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_audio_analyst", ID_AUDIO_ANALYST); //envio el parametro
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


                            //PIE Acumulador

                            if (defect_type[i].substring(0,1).equalsIgnoreCase("1"))
                            {
                                t_Phontics++;
                            }
                            if (defect_type[i].substring(0,1).equalsIgnoreCase("2"))
                            {
                                t_Grammar++;
                            }

                            if (defect_type[i].substring(0,1).equalsIgnoreCase("3"))
                            {
                                t_Sintaxt++;

                            }
                            if (defect_type[i].substring(0,1).equalsIgnoreCase("4"))
                            {
                                t_Meaning++;
                            }



                            //PIE

                        }


                        t_p=10000/tamanio;

                        //Sacar porcentaje
                        p_PH=t_p*t_Phontics;
                        p_GR=t_p*t_Grammar;
                        p_SI=t_p*t_Sintaxt;
                        p_ME=t_p*t_Meaning;


                        dibujaGrafica(p_PH,p_GR,p_SI, p_ME);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Grafico.this, "ERROR: " + e, Toast.LENGTH_SHORT).show();
                    }


                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });




    }//FIN DATOSSC
    private void cargarPreferencias()
    {
        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);
        String id_Analyst = preferencia.getString("Id_A", "NO EXISTE");
        cargarAudioDefect(id_Analyst);
    }

    private void dibujaGrafica(double v_Critical, double v_Desirable,double
            v_Important, double v_Pertinent)
    {

        //10,000 el valor

        AnimatedPieView animatedPieView = findViewById(R.id.pieView2);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        if (v_Critical>0)
        {
            config.addData(new SimplePieInfo(v_Critical, Color.parseColor(color_Phonetics), "PHONETICS "+v_Critical/100+"%" ));
        }
        if (v_Desirable>0)
        {
            config.addData(new SimplePieInfo(v_Desirable, Color.parseColor(color_Grammar), "GRAMMAR " + v_Desirable / 100 + "%"));
        }

        if (v_Pertinent>0)
        {
            config.addData(new SimplePieInfo(v_Pertinent, Color.parseColor(color_Meaning), "MEANING " + v_Pertinent / 100 + "%"));
        }
        if (v_Important>0)
        {
            config.addData(new SimplePieInfo(v_Important, Color.parseColor(color_Sintax), "SINTAX "+v_Important/100+"%"));
        }


        config.duration(1000);
        config.drawText(true);
        config.strokeMode(false);
        config.textSize(25);
        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {

                Toast.makeText(Grafico.this, pieInfo.getDesc() + " - " + pieInfo.getValue()/100+"% ", Toast.LENGTH_SHORT).show();
            }
        });
        config.startAngle(-180);

        animatedPieView.applyConfig(config);
        animatedPieView.start();


    }



}

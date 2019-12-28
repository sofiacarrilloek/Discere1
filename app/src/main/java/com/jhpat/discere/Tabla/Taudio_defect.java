package com.jhpat.discere.Tabla;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
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

    int[] colorClassArray = new int[]{Color.MAGENTA,Color.BLUE,Color.CYAN};
    int[] sale = new int[]{29, 20};

    //Pie
    int t_critical=0, t_important=0, t_relevant=0, t_pertinent=0, t_desirable=0;
    double p_c=0, p_i=0, p_r=0, p_p=0, p_d=0, t_p;

    String color_critical="#fe6383", color_important="#63ff84", color_relevant="#7fff63",
        color_pertinent="#8663fc", color_desirable="#6387ff";

    String CRITICAL="Critical", IMPORTANT="Important", RELEVANT="Relevant", PERTINENT="Pertinent", DESIRABLE="Desirable";


    //Pie

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
        DX1.setMovementMethod(new ScrollingMovementMethod());

        DPX1 = (TextView) findViewById(R.id.DP1);
        DPX1.setMovementMethod(new ScrollingMovementMethod());

        DTX1 = (TextView) findViewById(R.id.DT1);
        DTX1.setMovementMethod(new ScrollingMovementMethod());

        DDX1 = (TextView) findViewById(R.id.DD1);
        DDX1.setMovementMethod(new ScrollingMovementMethod());

       cargarPreferencias();
        //

    }


    public void cargarAudioDefect(String ID_AUDIO_ANALYST) {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://34.226.77.86/discere/cas/audio_defect.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_audio_analyst", "2"); //envio el parametro
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


                            //PIE Acumulador

                            if (defect_priority[i].equalsIgnoreCase("desirable"))
                            {
                                t_desirable++;
                            }
                            if (defect_priority[i].equalsIgnoreCase("important"))
                            {
                                t_important++;
                            }

                            if (defect_priority[i].equalsIgnoreCase("critical"))
                            {
                                t_critical++;

                            }
                            if (defect_priority[i].equalsIgnoreCase("relevant"))
                            {
                                t_relevant++;
                            }
                            if (defect_priority[i].equalsIgnoreCase("pertinent"))
                            {
                                t_pertinent++;
                            }


                            //PIE

                        }



                       // _________________________________________________________________________________________________________


                        DX1.setText(""+type);
                        DPX1.setText(""+type2);
                        DTX1.setText(""+type3);
                        DDX1.setText(""+type4);

                        t_p=10000/tamanio;

                        //Sacar porcentaje
                        p_c=t_p*t_critical;
                        p_d=t_p*t_desirable;
                        p_i=t_p*t_important;
                        p_p=t_p*t_pertinent;
                        p_r=t_p*t_relevant;

                        dibujaGrafica(p_c,p_d,p_i, p_p, p_r );

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
    private void cargarPreferencias()
    {
        SharedPreferences preferencia = getSharedPreferences("Credencialestabla", Context.MODE_PRIVATE);
        String id_Analyst = preferencia.getString("Id_A", "NO EXISTE");
        cargarAudioDefect(id_Analyst);
    }

    private void dibujaGrafica(double v_Critical, double v_Desirable,double
            v_Important, double v_Pertinent, double v_Relevant)
    {

        //10,000 el valor

        AnimatedPieView animatedPieView = findViewById(R.id.pieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        if (v_Critical>0)
        {
            config.addData(new SimplePieInfo(v_Critical, Color.parseColor(color_critical), CRITICAL+" "+v_Critical/100+"%"));
        }
        if (v_Desirable>0)
        {
            config.addData(new SimplePieInfo(v_Desirable, Color.parseColor(color_desirable), DESIRABLE+" "+v_Desirable/100+"%"));
        }
        if (v_Important>0)
        {
            config.addData(new SimplePieInfo(v_Important, Color.parseColor(color_important), IMPORTANT+" "+v_Important/100+"%"));
        }
        if (v_Pertinent>0)
        {
            config.addData(new SimplePieInfo(v_Pertinent, Color.parseColor(color_pertinent), PERTINENT+" "+v_Pertinent/100+"%"));
        }
        if (v_Relevant>0) {
            config.addData(new SimplePieInfo(v_Relevant, Color.parseColor(color_relevant), RELEVANT + " " + v_Relevant / 100 + "%"));
        }

        config.duration(1000);
        config.drawText(true);
        config.strokeMode(false);
        config.textSize(25);

        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {




                Toast.makeText(Taudio_defect.this, pieInfo.getDesc() + " - " + pieInfo.getValue()/100+"% ", Toast.LENGTH_SHORT).show();
            }
        });
        config.startAngle(-180);

        animatedPieView.applyConfig(config);
        animatedPieView.start();


    }


}



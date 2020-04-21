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


    private void cargarPreferencias()
    {
        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String user = preferencia.getString("ID2", "NO EXISTE");
        obtenIDFELLOW(user);
   }

            //PARA EL FELLOW
            /*En este apartado se encuentran las funcionalidades para el fellow*/

            //PRIMERO: se obtienen TODOS los id_fellow de la tabla fellow pasando como parametro el id_user
            public void obtenIDFELLOW (String ID_USER)
            {
                //Para el fellow
                AsyncHttpClient conexion = new AsyncHttpClient();
                final String url ="http://100.26.2.12/discere/cas/calendar/obten_id_fellow.php"; //la url del web service obtener_fecha_lessons.ph
                final RequestParams requestParams =new RequestParams();
                requestParams.add("id_user",ID_USER); //envio el parametro id_user

                conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        try {
                            String CONSULTA="";//Acumulador de String

                            jsonObject = new JSONObject(new String(responseBody));
                            //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                            int tamanio =jsonObject.getJSONArray("datos").length();
                            String id_fellows[] = new String[tamanio]; //Vector para almacenar los registros que regrese
                            int cuentaOr=0;
                            String OR;

                            OR=", ";

                            for (int i=0; i<tamanio; i++) {
                                id_fellows[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                                CONSULTA = CONSULTA + id_fellows[i];
                                if (cuentaOr<tamanio-1) {
                                    CONSULTA= CONSULTA + OR;
                                }

                                cuentaOr++;
                            }

                            obtenIDLessons(CONSULTA);


                        } catch (JSONException e) {
                            Toast.makeText(Grafico.this, "Error al cargar los datos"+e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

                    }
                });


            }//FIN OBTENIDFELLOW

            //SEGUNDO: Una vez que se obtienen los id_fellow se envian como parámetros para obtener los id_lesson
            public void obtenIDLessons (String ID_FELLOW)
            {
                //Para el fellow
                AsyncHttpClient conexion = new AsyncHttpClient();
                final String url ="http://100.26.2.12/discere/cas/calendar/obtener_fecha_lessons.php"; //la url del web service obtener_fecha_lessons.ph
                final RequestParams requestParams =new RequestParams();
                requestParams.add("id_fellow",ID_FELLOW); //envio el parametro

                conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        try {
                            String CONSULTA="";

                            jsonObject = new JSONObject(new String(responseBody));
                            //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                            int tamanio =jsonObject.getJSONArray("datos").length();
                            String id_lessons[] = new String[tamanio];
                            int cuentaOr=0;
                            String OR;

                            OR=", ";

                            for (int i=0; i<tamanio; i++) {
                                id_lessons[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                                CONSULTA = CONSULTA + id_lessons[i];
                                if (cuentaOr<tamanio-1) {
                                    CONSULTA= CONSULTA + OR;
                                }

                                cuentaOr++;
                            }
                            //Toast.makeText(Grafico.this, "LOADING..."+CONSULTA, Toast.LENGTH_SHORT).show();
                            obtenLessonResult(CONSULTA);

                        } catch (JSONException e) {
                            Toast.makeText(Grafico.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

                    }
                });


            }//FIN OBTENIDLessons

            //TERCERO: Mandando id_lessons se obtiene id_lessons_result
            public void obtenLessonResult (String ID_LESSON)
            {
                //Para el fellow
                AsyncHttpClient conexion = new AsyncHttpClient();
                final String url ="http://100.26.2.12/discere/Obten_lesson_result.php"; //la url del web service obtener_fecha_lessons.ph
                final RequestParams requestParams =new RequestParams();
                requestParams.add("id_lesson",ID_LESSON); //envio el parametro

                conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        try {
                            String CONSULTA="";

                            jsonObject = new JSONObject(new String(responseBody));
                            //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                            int tamanio =jsonObject.getJSONArray("datos").length();
                            String id_lesson_result[] = new String[tamanio];
                            int cuentaOr=0;
                            String OR;

                            OR=", ";

                            for (int i=0; i<tamanio; i++) {
                                id_lesson_result[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                                CONSULTA = CONSULTA + id_lesson_result[i];
                                if (cuentaOr<tamanio-1) {
                                    CONSULTA= CONSULTA + OR;
                                }

                                cuentaOr++;
                            }
                            //Toast.makeText(Grafico.this, "LOADING..."+CONSULTA, Toast.LENGTH_SHORT).show();
                            obtenDatosAudio(CONSULTA);

                        } catch (JSONException e) {
                            Toast.makeText(Grafico.this, "Error 276: "+e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

                    }
                });


            }//FIN OBTENLESSONRESULT

            //CUARTO: Obten datos de la tabla audio enviando id_lesson_result
            public void obtenDatosAudio (String ID_LESSON_RESULT)
            {
                //Para el fellow
                AsyncHttpClient conexion = new AsyncHttpClient();
                final String url ="http://100.26.2.12/discere/Obten_datos_audio.php"; //la url del web service obtener_fecha_lessons.ph
                final RequestParams requestParams =new RequestParams();
                requestParams.add("id_lesson_result",ID_LESSON_RESULT); //envio el parametro

                conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        try {
                            String CONSULTA="";

                            jsonObject = new JSONObject(new String(responseBody));
                            //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                            int tamanio =jsonObject.getJSONArray("datos").length();
                            String id_audio_analyst[] = new String[tamanio];
                            int cuentaOr=0;
                            String OR;

                            OR=", ";

                            for (int i=0; i<tamanio; i++) {
                                id_audio_analyst[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");


                                CONSULTA = CONSULTA + id_audio_analyst[i];
                                if (cuentaOr<tamanio-1) {
                                    CONSULTA= CONSULTA + OR;
                                }

                                cuentaOr++;
                            }
                            cargarAudioDefect(CONSULTA);


                        } catch (JSONException e) {
                            Toast.makeText(Grafico.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

                    }
                });


            }//FIN OBTENLESSONRESULT

            //QUINTO: Obtiene los datos de audio_defect enviando id_audio_analyst
            public void cargarAudioDefect(String ID_AUDIO_ANALYST) {

                AsyncHttpClient conexion = new AsyncHttpClient();
                final String url = "http://100.26.2.12/discere/Obten_datos_audio_defect.php"; //la url del web service
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




            }//FIN AUDIOS DEFECT






}

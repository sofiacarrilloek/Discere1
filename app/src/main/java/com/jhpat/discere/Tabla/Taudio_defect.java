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

import com.jhpat.discere.Grafico;
import com.jhpat.discere.MainActivity2;
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


    JSONObject jsonObject;
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE


    TextView DX1, DPX1, DTX1, DDX1;


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
        //En esta clase obtenemos los datos de los errores de la lista del fellow
        //.............

    }

    private void cargarPreferencias() {
        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String user = preferencia.getString("ID2", "NO EXISTE");
        obtenIDTEACHER(user);
    }

    //PARA EL FELLOW
    /*En este apartado se encuentran las funcionalidades para el fellow*/

    //PRIMERO: se obtienen TODOS los id_fellow de la tabla fellow pasando como parametro el id_user
    public void obtenIDTEACHER (String ID_USER)
    {
        //PARA EL TEACHER


        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/calendar/obten_id_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_user",ID_USER); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    String CONSULTA="";
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_teachers[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_teachers[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_teachers[i];
                        if (cuentaOr < tamanio - 1) {
                            CONSULTA = CONSULTA + OR;
                        }

                        cuentaOr++;

                    }
                    obtenIDLessons(CONSULTA);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Taudio_defect.this, "Error..."+e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN OBTEN_ID_TEACHER


    //SEGUNDO: Una vez que se obtienen los id_fellow se envian como parámetros para obtener los id_lesson
    public void obtenIDLessons (String ID_FELLOW)
    {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/calendar/obtener_fecha_lessons_teacher.php";//la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",ID_FELLOW); //envio el parametro

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
                    Toast.makeText(Taudio_defect.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

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
    public void obtenLessonResult(String ID_LESSON) {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://34.226.77.86/discere/Obten_lesson_result.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_lesson", ID_LESSON); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA = "";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio = jsonObject.getJSONArray("datos").length();
                    String id_lesson_result[] = new String[tamanio];
                    int cuentaOr = 0;
                    String OR;

                    OR = ", ";

                    for (int i = 0; i < tamanio; i++) {
                        id_lesson_result[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_lesson_result[i];
                        if (cuentaOr < tamanio - 1) {
                            CONSULTA = CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    //Toast.makeText(Grafico.this, "LOADING..."+CONSULTA, Toast.LENGTH_SHORT).show();
                    obtenDatosAudio(CONSULTA);

                } catch (JSONException e) {
                    Toast.makeText(Taudio_defect.this, "Error 276: " + e, Toast.LENGTH_SHORT).show();

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
    public void obtenDatosAudio(String ID_LESSON_RESULT) {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://34.226.77.86/discere/Obten_datos_audio.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        requestParams.add("id_lesson_result", ID_LESSON_RESULT); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA = "";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio = jsonObject.getJSONArray("datos").length();
                    String id_audio_analyst[] = new String[tamanio];
                    int cuentaOr = 0;
                    String OR;

                    OR = ", ";

                    for (int i = 0; i < tamanio; i++) {
                        id_audio_analyst[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");


                        CONSULTA = CONSULTA + id_audio_analyst[i];
                        if (cuentaOr < tamanio - 1) {
                            CONSULTA = CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    cargarAudioDefect(CONSULTA);


                } catch (JSONException e) {
                    Toast.makeText(Taudio_defect.this, "Error al cargar los datos del teacher " + e, Toast.LENGTH_SHORT).show();

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
        final String url = "http://34.226.77.86/discere/Obten_datos_audio_defect.php"; //la url del web service
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

                        int tamanio = 0;
                        tamanio = jsonObject.getJSONArray("datos").length();
                        String type = "";
                        String type2 = "";
                        String type3 = "";
                        String type4 = "";
                        String duration[] = new String[tamanio];
                        String defect_priority[] = new String[tamanio];
                        String defect_type[] = new String[tamanio];
                        String defect_description[] = new String[tamanio];


                        for (int i = 0; i < tamanio; i++) {
                            duration[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("duration");
                            defect_priority[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_priority");
                            defect_type[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_type");
                            defect_description[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("defect_description");

                            type = type + duration[i] + "\n" + "\n";
                            type2 = type2 + defect_priority[i] + "\n" + "\n";
                            type3 = type3 + defect_type[i] + "\n" + "\n";
                            type4 = type4 + defect_description[i] + "\n" + "\n";


                            //PIE Acumulador




                            //PIE

                        }


                        // _________________________________________________________________________________________________________


                        DX1.setText("" + type);
                        DPX1.setText("" + type2);
                        DTX1.setText("" + type3);
                        DDX1.setText("" + type4);


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


    }//FIN AUDIOS DEFECT
}




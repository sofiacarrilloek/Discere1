package com.jhpat.discere;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity2 extends AppCompatActivity{
    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    JSONObject jsonObject;
    JSONObject jsonObjecte;
    String fecha;
    private String nombreE;
    private String fechaE;
    private String horaIE;
    private String horaFE;
    private String mandafecha;
    private Context context;
    String ID_USER, TIPO;
    String lolitox;

    ArrayList<String> listDatos;
    RecyclerView recycler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//------------------------------------------------------------------------------------------
        recycler= (RecyclerView) findViewById(R.id.Recyclerid);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listDatos = new ArrayList<String>();

        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");
        listDatos.add("aqui van los datos");


        AdaptarDatos adapter = new AdaptarDatos(listDatos);
        recycler.setAdapter(adapter);
//------------------------------------------------------------------------------------------
        //Aqui llamamo al metodo


        cargarP();

        //


        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(this, cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity2.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity2.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                String fecham= selectedGridDate;
            ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity2.this);
            /* Intent i = new Intent(MainActivity2.this, dialogo.class);
                i.putExtra("Rnombre", nombreE);
                i.putExtra("Rfecha", fecham);
                i.putExtra("RhoraI", horaIE);
                i.putExtra("RhoraF", horaFE);

                obtenerFecha(fecham);
                startActivity(i);*/


            }

        });
    }
    public void obtenerFecha (String Correo)
    {
//Conexion
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obtenerS.php"; //la url del web service
        final RequestParams requestParams =new RequestParams();
        requestParams.add("fecha",Correo); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObjecte = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice

                    nombreE=jsonObjecte.getJSONArray("datos").getJSONObject(0).getString("name_fellow");
                    fechaE=jsonObjecte.getJSONArray("datos").getJSONObject(0).getString("date");
                    horaIE=jsonObjecte.getJSONArray("datos").getJSONObject(0).getString("start_time");
                    horaFE=jsonObjecte.getJSONArray("datos").getJSONObject(0).getString("end_time");




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }



    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        ID_USER = preferencia.getString("ID2", "NO EXISTE");
        TIPO = preferencia.getString("TIPO2", "NO EXISTE");

        if (TIPO.equalsIgnoreCase("Coach"))
        {
            obtenIDTEACHER(ID_USER);
            verTeacher(ID_USER);

        }
        if (TIPO.equalsIgnoreCase("Speaker"))
        {
            obtenIDTEACHER(ID_USER);
            verTeacher(ID_USER);
        }

        if (TIPO.equalsIgnoreCase("Fellow")) {
            obtenIDFELLOW(ID_USER);
            datosTeacher("");

        }

    }//Fin cargar preferencias



    public void obtenIDFELLOW (String ID_USER)
    {
        //Para el fellow
                        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

                        AsyncHttpClient conexion = new AsyncHttpClient();
                        final String url ="http://puntosingular.mx/cas/obten_id_fellow.php"; //la url del web service obtener_fecha_lessons.ph
                        final RequestParams requestParams =new RequestParams();
                        requestParams.add("id_user",ID_USER); //envio el parametro

                        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                                try {
                                    String CONSULTA="";

                                    jsonObject = new JSONObject(new String(responseBody));
                                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                                    int tamanio =jsonObject.getJSONArray("datos").length();
                                    String id_fellows[] = new String[tamanio];
                                    int cuentaOr=0;
                                    String OR;

                                    OR=", ";

                                    for (int i=0; i<tamanio; i++) {
                                        id_fellows[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");

                                        CONSULTA = CONSULTA + id_fellows[i];
                                        if (cuentaOr<tamanio-1) {
                                            CONSULTA= CONSULTA + OR;
                                        }

                                        cuentaOr++;
                    }

                    datosLessons(CONSULTA);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


}//FIN OBTENIDFELLOW

    public void obtenIDTEACHER (String ID_USER)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obten_id_teacher.php"; //la url del web service obtener_fecha_lessons.ph
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
                        id_teachers[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");

                        CONSULTA = CONSULTA + id_teachers[i];
                        if (cuentaOr < tamanio - 1) {
                            CONSULTA = CONSULTA + OR;
                        }

                        cuentaOr++;

                    }

        datosLessonsTeacher(CONSULTA);

    } catch (JSONException e) {
        e.printStackTrace();
    }

}

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

    }
});


    }//FIN OBTEN_ID_TEACHER


    public void datosLessons (String Correo)
    {
        //PARA EL FELLOW
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obtener_fecha_lessons.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("correo",Correo); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String tipo[]=new String[tamanio];

                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");

                        HomeCollection.date_collection_arr.add( new HomeCollection(fechaInicio[i] ,"Ocupado","",tipo[i]));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN DATOS LESSONS

    public void datosLessonsTeacher (String Correo)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obtener_fecha_lessons_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",Correo); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String tipo[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");


                        HomeCollection.date_collection_arr.add( new HomeCollection(fechaInicio[i] ,"Ocupado","",tipo[i]));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN  DATOS LESSONS TEACHER



    public void datosTeacher (final String Correo)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/obten_disponibilidad_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("correo",Correo); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String fechaInicio2[]=new String[tamanio];
                    String status[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String email[]=new String[tamanio];




                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        status[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");


                        fechaInicio2[i]=fechaInicio[i].substring(0, 10);

                        email[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("email");

                        lolitox=email[i];



                        if (status[i].equals("0"))
                        {
                            HomeCollection.date_collection_arr.add(new HomeCollection(fechaInicio2[i], "Disponible", email[i], tipo[i]));
                        }
                        if (status[i].equals("1"))
                        {
                            HomeCollection.date_collection_arr.add(new HomeCollection(fechaInicio2[i], "Pendiente", email[i], tipo[i]));
                        }



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN DATOS TEACHER





    public void verTeacher (final String ID_USER)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/teacher_disponibilidad.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("user",ID_USER); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String fechaInicio2[]=new String[tamanio];
                    String status[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String email[]=new String[tamanio];

                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        status[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");

                        email[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("email");

                        lolitox=email[i];


                        fechaInicio2[i]=fechaInicio[i].substring(0, 10);
                        if (status[i].equals("0"))

                        {
                            HomeCollection.date_collection_arr.add(new HomeCollection(fechaInicio2[i], "Disponible", email[i], tipo[i]));
                        }
                        if (status[i].equals("1"))
                        {
                        HomeCollection.date_collection_arr.add(new HomeCollection(fechaInicio2[i], "Pendiente", email[i], tipo[i]));
                    }



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN VER TEACHER



}

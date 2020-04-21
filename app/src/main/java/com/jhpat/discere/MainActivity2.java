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
    public static String id_teacher;
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

    String id_tea;

    ArrayList<String> listDatos;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cargarP();

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
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity2.this);

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
            datosEnEsperaFellow(ID_USER);
            datosTeacher();

        }

    }//Fin cargar preferencias

//-------------------------------------------------------- PARA EL TEACHER --------------------------------------------------------

    public void obtenIDTEACHER (String ID_USER)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obten_id_teacher.php"; //la url del web service obtener_fecha_lessons.ph
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

                    datosLessonsTeacher(CONSULTA);
                    datosEnEsperaTeacher(CONSULTA);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity2.this, "Error..."+e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN OBTEN_ID_TEACHER

    public void datosLessonsTeacher (String ID_TEACHER1)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obtener_fecha_lessons_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",ID_TEACHER1); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String id_fellow[]=new String[tamanio];
                    String start_time[]=new String[tamanio];
                    String end_time[]=new String[tamanio];
                    String status[]=new String[tamanio];
                    for (int i=0; i<tamanio; i++) {
                        fechaInicio[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        id_teacher[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");
                        start_time[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_time");
                        end_time[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_time");
                        status[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_fellow[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");

                        id_tea=id_teacher[i];
                        guardaridteacher();

                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Coaching")) {
                            cargarIdUsercargarIdUserFellowOcupado("" + fechaInicio[i], "Ocupado", "" + tipo[i], "" + id_teacher[i], "" +
                                    fechaInicio[i] + " " + start_time[i]+"-"+end_time[i], id_fellow[i]);
                        }

                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Speaking")) {
                            cargarIdUsercargarIdUserFellowOcupado("" + fechaInicio[i], "Ocupado", "" + tipo[i], "" + id_teacher[i], "" +
                                    fechaInicio[i] + " " + start_time[i]+"-"+end_time[i], id_fellow[i]);
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(MainActivity2.this, "Error..."+e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }//FIN  DATOS LESSONS TEACHER


    public void verTeacher (final String ID_USER)
    {
        //PARA EL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/teacher_disponibilidad.php"; //la url del web service obtener_fecha_lessons.ph
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

                    String status[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String user[]=new String[tamanio];
                    String start_time[]=new String[tamanio];
                    String end_time[]=new String[tamanio];

                    for (int i=0; i<tamanio; i++) {
                        fechaInicio[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        status[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_teacher[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");
                        user[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("user");
                        start_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date").substring(11,18);
                        end_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_date").substring(11,18);

                        if(Integer.parseInt(status[i])==1)
                        {
                            cargarNombreTeacherDisponible(""+fechaInicio[i].substring(0, 10),"Disponible",""+tipo[i],user[i]+"",
                                    fechaInicio[i]+" "+start_time[i]+"-"+end_time[i],""+id_teacher[i]);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity2.this, "Error 347: "+e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //   Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();
            }
        });


    }//FIN VER TEACHER

    // Datos en Espera para el Teacher
    public void datosEnEsperaTeacher (String ID_TEACHER2)
    {
        //PARA EL FELLOW    OBTIENE LAS SESIONES EN ESPERA (COLOR NARANJA O AMARILLO) :'V
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obtener_fecha_lessons_teacher.php"; //la url del web service obtener_sesionesEnEspera.php
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",ID_TEACHER2); //envio el parametro


        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {




                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String fechaInicio2[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String id_fellow[]=new String[tamanio];
                    String status[]=new String [tamanio];
                    String start_time[]=new String[tamanio];
                    String end_time[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        id_teacher[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");
                        start_time[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_time");
                        end_time[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_time");
                        status[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_fellow[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");

                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Coaching!Pending")) {
                            cargarIdUsercargarIdUserFellowOcupado("" + fechaInicio[i], "Pendiente", "" + tipo[i], "" + id_teacher[i], "" +
                                    fechaInicio[i] + " " + start_time[i]+"-"+end_time[i], id_fellow[i]);
                        }

                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Speaking!Pending")) {
                            cargarIdUsercargarIdUserFellowOcupado("" + fechaInicio[i], "Pendiente", "" + tipo[i], "" + id_teacher[i], "" +
                                    fechaInicio[i] + " " + start_time[i]+"-"+end_time[i], id_fellow[i]);
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                      Toast.makeText(MainActivity2.this, "Error al cargar los datos"+e, Toast.LENGTH_SHORT).show();


                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });




    }//FIN SESIONES EN ESPERA PARA EL FELLOW :3

    public void cargarIdUsercargarIdUserFellowOcupado (final String fechaInicio, final String estado, final String tipo, final String id_teacher, final String hora, final String id_fellow)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/cargar_id_user_mandando_id_fellow.php"; //la url del web service
        final RequestParams requestParams =new RequestParams("id_fellow",id_fellow);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String user[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {

                        user[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("user");
                        //Aqui es para cargar el datos del fellow
                        cargarNombreUsuarioLessons(fechaInicio+"",""+estado,""+tipo,""+user[i], hora, id_teacher, id_fellow);

                    }


                } catch (JSONException e) {
                    Toast.makeText(MainActivity2.this, "Error 475: "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public void cargarNombreUsuarioLessons (final String fechaInicio, final String estado, final String tipo, final String user,
                                            final String hora, final String id_teacher, final String id_fellow) {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/cargar_datos_teacher.php"; //la url del web service
        final RequestParams requestParams =new RequestParams("id_user", user);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String nombre[]=new String[tamanio];
                    String email[]=new String[tamanio];
                    String last_name[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {

                        nombre[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("name");
                        email[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("email");
                        last_name[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("last_name");

                        HomeCollection.date_collection_arr.add( new HomeCollection(fechaInicio ,estado+"",""+tipo,""+user,email[i]+"", nombre[i]+" "+last_name[i], hora, ""+id_teacher, ""+id_fellow,""));

                    }


                } catch (JSONException e) {
                    Toast.makeText(MainActivity2.this, "Error 525: "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
    public void cargarNombreTeacherDisponible (final String fechaInicio, final String estado, final String tipo, final String user, final String hora, final String id_teacher)
    {
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/cargar_datos_teacher.php"; //la url del web service
        final RequestParams requestParams =new RequestParams("id_user", user);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String nombre[]=new String[tamanio];
                    String email[]=new String[tamanio];
                    String last_name[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {

                        nombre[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("name");
                        email[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("email");
                        last_name[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("last_name");

                        HomeCollection.date_collection_arr.add( new HomeCollection(fechaInicio ,estado,tipo,user,email[i]+"", nombre[i]+" "+last_name[i], ""+hora, ""+id_teacher, "",""));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity2.this, "Error 579: "+e, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    //--------------------------------------------------------------PARTE FELLOW--------------------------------------------------------------

    public void obtenIDFELLOW (String ID_USER)
    {
        //Para el fellow
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();



        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obten_id_fellow.php"; //la url del web service obtener_fecha_lessons.ph
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
                        id_fellows[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_fellows[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }

                    datosEnEsperaFellow(CONSULTA);
                    datosLessons(CONSULTA);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN OBTENIDFELLOW

    public void datosTeacher ()
    {
        //PARA EL TEACHER MUESTRA LAS SESIONES DISPONIBLES DEL TEACHER
        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();


        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obten_disponibilidad_teacher.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String fechaFinal[]=new String[tamanio];
                    String status[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];

                    String id_user[]=new String[tamanio];
                    String hora[]=new String[tamanio];

                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");

                        status[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        id_user[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("user");
                        fechaFinal[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_date");
                        hora[i]=fechaFinal[i].substring(10, 19);

                        if(Integer.parseInt(status[i])==1) {
                            cargarNombreTeacherDisponible(fechaInicio[i].substring(0, 10), "Disponible", tipo[i], id_user[i], fechaInicio[i] + "-"+hora[i], ""+id_teacher[i]);
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


    public void datosLessons (String ID_FELLOW2)
    {
        //PARA EL FELLOW    OBTIENE LAS LECCIONES YA AGENDADAS (AZUL O VERDE FUERTE)

        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();


        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obtener_fecha_lessons.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_fellow",ID_FELLOW2); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String start_time[]=new String[tamanio];
                    String  end_time[]=new String[tamanio];
                    String  status[]=new String[tamanio];

                    String id_fellow[]=new String[tamanio];
                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        start_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_time");
                        end_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_time");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");
                        status[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_fellow[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");


                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Speaking"))
                        {
                            cargarIdUserTeacherOcupado("" + fechaInicio[i], "Ocupado", "" + tipo[i], "" + id_teacher[i],
                                    fechaInicio[i] + " " + start_time[i] + "-" + end_time[i], "" + id_fellow[i]);
                        }

                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Coaching"))
                        {
                            cargarIdUserTeacherOcupado("" + fechaInicio[i], "Ocupado", "" + tipo[i], "" + id_teacher[i],
                                    fechaInicio[i] + " " + start_time[i] + "-" + end_time[i], "" + id_fellow[i]);
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


    }//FIN DATOS LESSONS

    public void cargarIdUserTeacherOcupado (final String fechaInicio, final String estado, final String tipo, final String id_teacher, final String hora, final String id_fellow)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/cargar_id_user_mandando_id_teacher.php"; //la url del web service
        final RequestParams requestParams =new RequestParams("id_teacher", id_teacher);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String user[]=new String[tamanio];


                    for (int i=0; i<tamanio; i++)
                    {

                        user[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("user");
                        cargarNombreUsuarioLessons(fechaInicio+"",""+estado,""+tipo,""+user[i], hora, id_teacher, id_fellow);
                    }


                } catch (JSONException e) {
                    //    Toast.makeText(MainActivity2.this, "Error al cargar los datos"+e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    // Datos en Espera para el FELLOW

    public void datosEnEsperaFellow (String ID_FELLOW2)
    {
        //PARA EL FELLOW    OBTIENE LAS LECCIONES YA AGENDADAS (AZUL O VERDE FUERTE)

        HomeCollection.date_collection_arr=new ArrayList<HomeCollection>();


        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/cas/calendar/obtener_fecha_lessons.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_fellow",ID_FELLOW2); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String fechaInicio[]=new String[tamanio];
                    String tipo[]=new String[tamanio];
                    String id_teacher[]=new String[tamanio];
                    String start_time[]=new String[tamanio];
                    String  end_time[]=new String[tamanio];
                    String  status[]=new String[tamanio];

                    String id_fellow[]=new String[tamanio];
                    for (int i=0; i<tamanio; i++)
                    {
                        fechaInicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        start_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_time");
                        end_time[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_time");
                        tipo[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("type");
                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_teacher");
                        status[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("status");
                        id_fellow[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_fellow");



                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Speaking!Pending"))
                        {
                            cargarIdUserTeacherOcupado("" + fechaInicio[i], "Pendiente", "Speaking", "" + id_teacher[i],
                                    fechaInicio[i] + " " + start_time[i] + "-" + end_time[i], "" + id_fellow[i]);
                        }
                        if (Integer.parseInt(status[i])==1&&tipo[i].equalsIgnoreCase("Coaching!Pending"))
                        {
                            cargarIdUserTeacherOcupado("" + fechaInicio[i], "Pendiente", "Coaching", "" + id_teacher[i],
                                    fechaInicio[i] + " " + start_time[i] + "-" + end_time[i], "" + id_fellow[i]);
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


    }//FIN DATOS LESSONS

    //------------------------------------FIN PARTE FELLOW------------------------------------------
    private void guardaridteacher()
    {

        SharedPreferences preferencia = getSharedPreferences("id_teacher", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("teacher", id_tea);
        editor.commit();
        Toast.makeText(getApplicationContext(),"hola"+id_tea,Toast.LENGTH_LONG).show();
    }

}

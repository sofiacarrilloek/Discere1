package com.jhpat.discere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cz.msebera.android.httpclient.Header;

class DialogAdaptorStudent extends BaseAdapter {
    Activity activity;

    private Activity context;
    private ArrayList<Dialogpojo> alCustom;
    public ArrayList<HomeCollection> date_collection_arr;
    private String sturl;
    public static List<String> day_string;
    String ID_TEACHER, CLLC, ID_FELLOW, USER;
    Session session = null;
    JSONObject jsonObject;
    String EMAIL, NAME, LAST_NAME, TIPO, ESTADO_SESION, EMAIL_FELLOW;
    ProgressDialog pdialog = null;
    String subC = "Sesión Discere";
    String msgC = "Lo sentimos, tú sesión ha sido rechazada";
    String sub = "Sesión Discere";
    String msg = "Tienes una solicitud de sesión";
    String MENSAJE;
    String TITULO;

    public DialogAdaptorStudent(Activity context, ArrayList<Dialogpojo> alCustom) {
        this.context = context;
        this.alCustom = alCustom;
    }

    @Override
    public int getCount() {
        return alCustom.size();

    }

    @Override
    public Object getItem(int i) {
        return alCustom.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cdialogo, null, true);

        TextView tvName = (TextView) listViewItem.findViewById(R.id.time);
        TextView tvEmail = (TextView) listViewItem.findViewById(R.id.fellow);
        TextView tvDate = (TextView) listViewItem.findViewById(R.id.date);
        final TextView Tipo = (TextView) listViewItem.findViewById(R.id.tvTipo);
        Button boton = (Button) listViewItem.findViewById(R.id.btnaceptar);
        Button boton_cancelar=(Button)listViewItem.findViewById(R.id.btncancel);

        boton.setEnabled(true);
        boton_cancelar.setEnabled(true);
        cargarP();

        ESTADO_SESION=alCustom.get(position).getEstado();
        tvName.setText("Name: " + alCustom.get(position).getNombre_teacher());
        tvEmail.setText("Email: " + alCustom.get(position).getEmail_teacher());
        EMAIL = alCustom.get(position).getEmail_teacher();
        tvDate.setText("Date " + alCustom.get(position).getDia());

//
        switch (alCustom.get(position).getEstado().toUpperCase())
        {
            case "DISPONIBLE":
                if (alCustom.get(position).getTipo().equalsIgnoreCase("Coaching"))
                {
                    Tipo.setText("AVAILABLE COACHING SESSION");
                }
                else
                {
                    Tipo.setText("AVAILABLE SPEAKING SESSION");
                }
                break;

            case "PENDIENTE":
                if (alCustom.get(position).getTipo().equalsIgnoreCase("Coaching 1"))
                {
                    Tipo.setText("PENDING COACHING SESSION");
                }
                else
                {
                    Tipo.setText("PENDING SPEAKING SESSION");
                }
                break;

            case "OCUPADO":

                Tipo.setText(alCustom.get(position).getTipo().toUpperCase()+" SESSION");
                break;

        }

        if(TIPO.equalsIgnoreCase("Fellow"))
        {
            switch (ESTADO_SESION.toUpperCase())
            {

                case "DISPONIBLE":

                    boton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            //Agendar sesion PENDIENTE
                            String tipoSesion="";//Variable para el tipo de sesion

                            if (alCustom.get(position).getTipo().equalsIgnoreCase("Coaching")) {
                                tipoSesion = "Coaching 1";
                            }else
                            {
                                tipoSesion="Speaking 1";
                            }

                            String fecha = alCustom.get(position).getDia().substring(0, 10);//Obtiene la fecha en formato yyyy-MM-dd
                            String hora_inicio=alCustom.get(position).getDia().substring(11, 19);
                            String hora_final=alCustom.get(position).getDia().substring(21, 29);
                            String start_date=fecha+" "+hora_inicio;
                            String end_date=fecha+" "+hora_final;
                            String start=fecha+"T"+hora_inicio;//Formato yyyy-MM-ddT00:00:00
                            String end=fecha+"T"+hora_final;

                         agendarSesionPendienteFellow(""+USER, ""+tipoSesion, ""+start, ""+end, null,""+getNombreDia(fecha), ""+getFechaActual(),
                                  ""+1,""+start_date ,
                                   ""+end_date, ""+alCustom.get(position).getId_teacher());

                            }});


                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setBackgroundColor(000000);
                    boton_cancelar.setVisibility(View.INVISIBLE);

                    break;

                case "PENDIENTE":
                    boton.setEnabled(false);
                    boton.setBackgroundColor(000000);
                    boton.setVisibility(View.INVISIBLE);

                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setBackgroundColor(000000);
                    boton_cancelar.setVisibility(View.INVISIBLE);
                    break;

                case "OCUPADO":
                    MENSAJE="LO SENTIMOS, TU SESIÓN HA SIDO CANCELADA";
                    TITULO="SESIÓN CANCELADA";
                    //Ocultar botón
                    boton.setEnabled(false);
                    boton.setBackgroundColor(000000);
                    boton.setVisibility(View.INVISIBLE);

                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setBackgroundColor(000000);
                    boton_cancelar.setVisibility(View.INVISIBLE);

                    break;
            }


        }
        else
        {
            switch (ESTADO_SESION.toUpperCase())
            {
                case "DISPONIBLE":

                    MENSAJE="LO SENTIMOS, SESIÓN CANCELADA";
                    TITULO="SESIÓN CANCELADA";

                    boton.setEnabled(false);
                    boton.setVisibility(View.INVISIBLE);
                   //SE OCULTA EL BOTON CANCELAR
                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setVisibility(View.INVISIBLE);
                    break;

                case "PENDIENTE":


                    boton.setEnabled(true);
                    boton.setVisibility(View.VISIBLE);

                    boton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                            TITULO="SESIÓN ACEPTADA";
                            MENSAJE="TU SESIÓN HA SIDO ACEPTADA";
                            //Actualizar sesiones penientes
                            String Fecha= alCustom.get(position).getDia().substring(0,10);
                            String FechaCompleta=alCustom.get(position).getDia();

                            String id_teacher=alCustom.get(position).getId_teacher();
                            String id_fellow=alCustom.get(position).getId_fellow();
                            String tipoSesion=alCustom.get(position).getTipo();


                            if (tipoSesion.equalsIgnoreCase("Coaching!Pending"))
                            {
                                //actualizaSesionAceptada(id_fellow, id_teacher, "Coaching");
                                String title = "Coaching 1";
                                String hora_inicio=FechaCompleta.substring(10, 19);
                                String hora_final=FechaCompleta.substring(20, 28);
                                String start=Fecha+"T"+hora_inicio;
                                String end=Fecha+"T"+hora_final;
                                String start_date=Fecha+" "+hora_inicio;
                                String end_date=Fecha+" "+hora_final;

                                int cuentaOr=0;
                                String OR;

                                OR=", ";
                                String FechasS[]= new String[11];
                                FechasS[0]=Fecha;
                                String total="";
                                for (int i=1; i<10; i++){
                                    FechasS[i]=getFecha7Dias(FechasS[i-1]);
                                    total=total+"'"+FechasS[i]+" "+hora_inicio+"'";
                                     if (cuentaOr < 8) {
                                        total = total + OR;
                                    }

                                    cuentaOr++;
                                }


                                cargaIdUserMandandoIdFellow(""+alCustom.get(position).getId_fellow(), ""+USER, ""+total, ""+title,
                                        ""+start, ""+end, null, ""+getNombreDia(Fecha), ""+getFechaActual(), "1", ""+start_date, ""+end_date);

                            }
                            if (tipoSesion.equalsIgnoreCase("Speaking!Pending"))
                            {
                                //actualizaSesionAceptada(id_fellow, id_teacher, "Coaching");
                                String title = "Coaching 1";
                                String hora_inicio=FechaCompleta.substring(10, 19);
                                String hora_final=FechaCompleta.substring(20, 28);
                                String start=Fecha+"T"+hora_inicio;
                                String end=Fecha+"T"+hora_final;
                                String start_date=Fecha+" "+hora_inicio;
                                String end_date=Fecha+" "+hora_final;

                                int cuentaOr=0;
                                String OR;

                                OR=", ";
                                String FechasS[]= new String[11];
                                FechasS[0]=Fecha;
                                String total="";
                                for (int i=1; i<10; i++){
                                    FechasS[i]=getFecha7Dias(FechasS[i-1]);
                                    total=total+"'"+FechasS[i]+" "+hora_inicio+"'";
                                    if (cuentaOr < 8) {
                                        total = total + OR;
                                    }

                                    cuentaOr++;
                                }

                                cargaIdUserMandandoIdFellow(""+alCustom.get(position).getId_fellow(), ""+USER, ""+total, ""+title,
                                        ""+start, ""+end, null, ""+getNombreDia(Fecha), ""+getFechaActual(), "1", ""+start_date, ""+end_date);

                          }

                            //Enviar correo

                            Properties props = new Properties();
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.socketFactory.port", "465");
                            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.port", "465");

                            session = Session.getDefaultInstance(props, new Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication("discerenc2019@gmail.com", "Adrian16");
                                }
                            });

                            pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);

                            RetreiveFeedTask task = new RetreiveFeedTask();
                            task.execute();
                        }


                        class RetreiveFeedTask extends AsyncTask<String, Void, String> {

                            @Override
                            protected String doInBackground(String... params) {

                                try {
                                    Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL));
                                    message.setSubject(TITULO);
                                    message.setContent(MENSAJE, "text/html; charset=utf-8");
                                    Transport.send(message);
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                pdialog.dismiss();

                                Toast.makeText(context, "Email sent", Toast.LENGTH_LONG).show();
                            }
                        }

                    });


                    // Boton cancelar
                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setVisibility(View.INVISIBLE);
                    break;

                case "OCUPADO":
                    MENSAJE="LO SENTIMOS, TU SESIÓN HA SIDO CANCELADA";
                    TITULO="SESIÓN CANCELADA";

                    boton.setEnabled(false);
                    boton.setVisibility(View.INVISIBLE);
                    boton_cancelar.setEnabled(false);



                    break;
            }

        }

        return listViewItem;



    }

    private void cargarP ()
    {
        //Cargar preferencias sirve para almacenar datos

        SharedPreferences preferencia = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        NAME = preferencia.getString("NAME2", "NO EXISTE");
        LAST_NAME = preferencia.getString("LAST_NAME2", "NO EXISTE");
        TIPO = preferencia.getString("TIPO2", "NO EXISTE");
        USER=preferencia.getString("ID2", "NO EXISTE");
        EMAIL_FELLOW=preferencia.getString("EMAIL2", "NO EXISTE");
    }//Fin cargar preferencias





    public static String getFechaMod(Date fechaMod){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(fechaMod);
    }

    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formateador.format(ahora);
    }
    public static String getNombreDia(String Fecha) {
        String month = Fecha.substring(5,7);
        String day = Fecha.substring(8,10);
        String year = Fecha.substring(0,4);

        String inputDateStr = String.format("%s/%s/%s", day, month, year);
        Date inputDate = null;
        try {
            inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

        return dayOfWeek;
    }

    public static String getFecha7Dias(String fecha)
    {
        //0btiene las 3 fechas
        String month = fecha.substring(5,7);
        String day = fecha.substring(8,10);
        String year = fecha.substring(0,4);

        String inputDateStr = String.format("%s/%s/%s", day, month, year);
        Date inputDate = null;
        try {
            inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        fecha=calendar.getTime().toString();

        calendar.add(Calendar.DAY_OF_YEAR, 7);
        fecha=getFechaMod(calendar.getTime());

        return fecha;
    }


    public void actualizaSesionAceptada (String id_fellow, String id_teacher, String actualizacion)
    {
        //Este metodo actualiza el status a 0 cuando el teacher cancela una disponibilidad
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/calendar/actualiza_lessons_pendiente.php"; //la url del web service obtener_sesionesEnEspera.php
        final RequestParams requestParams =new RequestParams();
        requestParams.add("tipoActualizado",actualizacion);
        requestParams.add("id_fellow",id_fellow);
        requestParams.add("id_teacher",id_teacher);


        //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(context, "Sesión agendada", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "Error al agendar", Toast.LENGTH_SHORT).show();
            }
        });



    }//FIN SESIONES


    public void agendarSesionPendienteLessons ( final String id_fellow, final String id_teacher,
                                       final String type,final String day,final String status, final String create_date, final String start_date,
                                       final String end_date,  final String start_time, final String end_time)
    {


        AsyncHttpClient conexion = new AsyncHttpClient();

        final String url = "http://34.226.77.86/discere/calendar/insertar_sesion_aceptada.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        //envio el parametro
        requestParams.add("id_fellow", id_fellow);
        requestParams.add("id_teacher", id_teacher);
        requestParams.add("type", type);
        requestParams.add("day", day);
        requestParams.add("status", status);
        requestParams.add("create_date", create_date);
        requestParams.add("start_date", start_date);
        requestParams.add("start_time", start_time);
        requestParams.add("end_date", end_date);
        requestParams.add("end_time", end_time);



        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(context, "Session saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error 535: " + error, Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN

    public void agendarSesionPendienteFellow (final String user,final String title,final String start, final String end, final String constrain,
                                              final String day , final String create_date, final String status, final String start_date,final String end_date, final String id_teacher)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
       final String url = "http://34.226.77.86/discere/calendar/insertar_tabla_fellow.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        //envio el parametro
        requestParams.add("user", user);
        requestParams.add("title", title);
        requestParams.add("start", start);
        requestParams.add("end", end);
        requestParams.add("constrain", constrain);
        requestParams.add("day", day);
        requestParams.add("create_date", create_date);
        requestParams.add("status", status);
        requestParams.add("start_date", start_date);
        requestParams.add("end_date", end_date);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

               // Toast.makeText(context, "Session saved", Toast.LENGTH_SHORT).show();

                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_fellow[]=new String[tamanio];
                    String type="";



                    for (int i=0; i<tamanio; i++)
                    {

                        id_fellow[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");


                    }

                   if (title.equalsIgnoreCase("Coaching 1")){
                        type="Coaching!Pending";
                    }else
                    {
                        type="Speaking!Pending";
                    }

                 agendarSesionPendienteLessons(""+id_fellow[0], ""+id_teacher, ""+type, ""+day, ""+status, ""+getFechaActual(), ""+start_date.substring(0, 10),
                 ""+end_date.substring(0, 10) , ""+start_date.substring(10, 19), ""+end_date.substring(10, 19));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "ERROR 589"+e, Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //error.toString();
                Toast.makeText(context, "Error 598: " + error, Toast.LENGTH_SHORT).show();

        }
        });


    }//FIN

    public void cargaIDT (String user, String consulta, final String USER_FELLOW, final String title,final String start, final String end, final String constrain,final String day , final String create_date, final String status, final String start_date,final String end_date)
    {

        AsyncHttpClient conexion = new AsyncHttpClient();

        final String url ="https://projectzerowaste.000webhostapp.com/app/cargar_id_teacher_btn_pendiente.php"; //la url del web service obtener_sesionesEnEspera.php
        //final String url ="http://34.226.77.86/discere/cas/calendar/cargar_id_teacher_btn_pendiente.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("user",user);
        requestParams.add("consulta",consulta);



        //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_teacher[]=new String[tamanio];
                    String fecha_inicio[]=new String [tamanio];
                    String fecha_final[]=new String [tamanio];
                    String f="";


                    for (int i=0; i<tamanio; i++)
                    {

                        id_teacher[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");
                        fecha_inicio[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("start_date");
                        fecha_final[i]=jsonObject.getJSONArray("datos").getJSONObject(i).getString("end_date");

                        agendarSesionOcupadaFellow(""+USER_FELLOW, ""+title, ""+fecha_inicio[i].substring(0,10)+"T"+fecha_inicio[i].substring(11,19), ""+fecha_final[0].substring(0,10)+"T"+fecha_final[0].substring(11,19), ""+constrain,
                                ""+day, ""+create_date,""+status,
                        ""+fecha_inicio[i], ""+fecha_final[i], ""+id_teacher[i]);

                    }

                    //Toast.makeText(context, "Start: "+fecha_final[0].substring(0,10)+"T"+fecha_final[0].substring(11,19), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    Toast.makeText(context, "Error 529: "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "Error"+error, Toast.LENGTH_SHORT).show();
            }
        });



    }//FIN SESIONES

    public void cargaIdUserMandandoIdFellow (final String id_fellow, final String Userteacher, final String consulta, final String title,
                                             final String start, final String end, final String constrain, final String day, final String create_date, final String status,
                                             final String start_date, final String end_date)
    {
        //Este metodo actualiza el status a 0 cuando el teacher cancela una disponibilidad
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/calendar/cargar_id_user_mandando_id_fellow.php"; //la url del web service obtener_sesionesEnEspera.php
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_fellow",id_fellow);

        //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice

                    String id_user;
                    id_user=jsonObject.getJSONArray("datos").getJSONObject(0).getString("user");

                    cargaIDT(""+Userteacher,""+consulta,""+id_user,""+title, ""+start, ""+end, ""+constrain, ""+day,
                            ""+create_date, ""+status, ""+start_date, ""+end_date);



                } catch (JSONException e) {
                    Toast.makeText(context, "Error 577: "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "Error"+error, Toast.LENGTH_SHORT).show();
            }
        });



    }//FIN SESIONES


    public void agendarSesionOcupadaFellow (final String user,final String title,final String start, final String end, final String constrain,
                                              final String day , final String create_date, final String status, final String start_date,final String end_date, final String id_teacher)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();

        final String url = "https://projectzerowaste.000webhostapp.com/app/insertar_tabla_fellow.php";
        //final String url = "http://34.226.77.86/discere/calendar/insertar_tabla_fellow.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        //envio el parametro
        requestParams.add("user", user);
        requestParams.add("title", title);
        requestParams.add("start", start);
        requestParams.add("end", end);
        requestParams.add("constrain", constrain);
        requestParams.add("day", day);
        requestParams.add("create_date", create_date);
        requestParams.add("status", status);
        requestParams.add("start_date", start_date);
        requestParams.add("end_date", end_date);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                // Toast.makeText(context, "Session saved", Toast.LENGTH_SHORT).show();

                try {
                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice

                    String id_fellow="";
                    String type="";
                    if (title.equalsIgnoreCase("Coaching 1"))
                    {
                        type="Coaching";
                    }
                    if (title.equalsIgnoreCase("Speaking 1"))
                    {
                        type="Speaking";
                    }

                        id_fellow=jsonObject.getJSONArray("datos").getJSONObject(0).getString("id_");

                    agendarSesionOcupadaLessons(""+id_fellow, ""+id_teacher, ""+type, ""+day, ""+status, ""+create_date,
                            ""+start_date.substring(0, 10),
                                ""+end_date.substring(0, 10) , ""+start_date.substring(10, 19), ""+end_date.substring(10, 19));



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "ERROR 589"+e, Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //error.toString();
                Toast.makeText(context, "Error 598: " + error, Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN

    public void agendarSesionOcupadaLessons ( final String id_fellow, final String id_teacher,
                                              final String type,final String day,final String status, final String create_date, final String start_date,
                                              final String end_date,  final String start_time, final String end_time)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="https://projectzerowaste.000webhostapp.com/app/insertar_sesion_aceptada.php";
        //final String url ="http://34.226.77.86/discere/cas/calendar/insertar_sesion_aceptada.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();

        //envio el parametro
        requestParams.add("id_fellow", id_fellow);
        requestParams.add("id_teacher", id_teacher);
        requestParams.add("type", type);
        requestParams.add("day", day);
        requestParams.add("status", status);
        requestParams.add("create_date", create_date);
        requestParams.add("start_date", start_date);
        requestParams.add("start_time", start_time);
        requestParams.add("end_date", end_date);
        requestParams.add("end_time", end_time);


        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(context, "Session saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN
}

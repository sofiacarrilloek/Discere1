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

import javax.mail.Message;
import javax.mail.MessagingException;
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
                            String tipoSesion="";

                            if (alCustom.get(position).getTipo().equalsIgnoreCase("Coaching")) {
                                tipoSesion = "Coaching 1";
                            }else
                            {
                                tipoSesion="Speaking 1";
                            }

                            String fecha = alCustom.get(position).getDia().substring(0, 10);
                            String hora_inicio=alCustom.get(position).getDia().substring(11, 19);
                            String hora_final=alCustom.get(position).getDia().substring(21, 29);
                            String start_date=fecha+" "+hora_inicio;
                            String end_date=fecha+" "+hora_final;
                            String start=fecha+"T"+hora_inicio;
                            String end=fecha+"T"+hora_final;

                           // Toast.makeText(context, "Hora inicio:"+hora_inicio, Toast.LENGTH_SHORT).show();
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
                    //EL teacher da de baja su sesión
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
                            Date Fecha1 = null, Fecha2, Fecha3, Fecha4;

                            String id_teacher=alCustom.get(position).getId_teacher();
                            String id_fellow=alCustom.get(position).getId_fellow();
                            String tipoSesion=alCustom.get(position).getTipo();

                            if (tipoSesion.equalsIgnoreCase("Coaching!Pending"))
                            {
                                Toast.makeText(context, "Fecha:"+Fecha, Toast.LENGTH_SHORT).show();
                                //agendarSesionAceptada(id_fellow, id_teacher, "Coaching");
                            }
                            if (tipoSesion.equalsIgnoreCase("Speaking!Pending"))
                            {

                                //0btiene las 3 fechas
                                String month = Fecha.substring(5,7);
                                String day = Fecha.substring(8,10);
                                String year = Fecha.substring(0,4);

                                String hora_inicio=FechaCompleta.substring(12, 19);
                                String hora_final=FechaCompleta.substring(21, 28);

                                String inputDateStr = String.format("%s/%s/%s", day, month, year);
                                Date inputDate = null;
                                try {
                                    inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(inputDateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(inputDate);
                                Fecha1=calendar.getTime();

                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                                Fecha2=calendar.getTime();

                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                                Fecha3=calendar.getTime();

                                calendar.add(Calendar.DAY_OF_YEAR, 7);
                                Fecha4=calendar.getTime();

                                String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

                           /*     Toast.makeText(context, "Fecha 1: "+getFechaMod(Fecha1)+"Fecha 2 :"+getFechaMod(Fecha2)+
                                   "Fecha 3: "+getFechaMod(Fecha3)+"Fecha 4 :"+getFechaMod(Fecha4)+"Dia: "+dayOfWeek+"Ahora: "+getFechaActual()+" 00:00:00", Toast.LENGTH_SHORT).show();
*/



                                //agendarSesionAceptada(id_fellow, id_teacher, "Speaking");
                            }

                            //Enviar correo

                         /*   Properties props = new Properties();
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
                            task.execute();*/
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

    //--------------------------------------PARA EL FELLOW------------------------------------------------

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


    //--------------------------------------FIN PARA EL FELLOW------------------------------------------------

    //-----------------------PARA EL TEACHER---------------------

    public void agendarSesionAceptada (String id_fellow, String id_teacher, String actualizacion)
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

    public String getFechaMod(Date fechaMod){
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

    public void agendarSesionOcupada ( final String id_fellow, final String id_teacher,
                                       final String type,final String day,final String status, final String create_date, final String start_date,
                                       final String end_date,  final String start_time, final String end_time)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/calendar/insertar_sesion_aceptada.php"; //la url del web service obtener_fecha_lessons.ph
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
                Toast.makeText(context, "Error 529: " + error, Toast.LENGTH_SHORT).show();

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
}

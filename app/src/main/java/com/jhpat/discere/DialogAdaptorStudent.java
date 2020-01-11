package com.jhpat.discere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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
        TextView Tipo = (TextView) listViewItem.findViewById(R.id.tvTipo);
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
                            //Agendar sesionn ocupada
                            }});
                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setBackgroundColor(000000);

                    break;

                case "PENDIENTE":
                    boton.setEnabled(false);
                    boton.setBackgroundColor(000000);

                    boton_cancelar.setEnabled(false);
                    boton_cancelar.setBackgroundColor(000000);

                    break;

                case "OCUPADO":
                    MENSAJE="LO SENTIMOS, TU SESIÓN HA SIDO CANCELADA";
                    TITULO="SESIÓN CANCELADA";

                    boton.setEnabled(false);
                    boton.setBackgroundColor(000000);

                    boton_cancelar.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                            actualizarStatusTeacher(alCustom.get(position).getId_teacher(),"0",""+alCustom.get(position).getId_fellow());
                            //para enviar los correos
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
                    boton_cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//***
                            actualizarStatus(alCustom.get(position).getId_teacher(), "0");//Cambia el status para que ya no este activo el registro


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
                            protected String doInBackground(String... params)
                            {

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
                    break;

                case "PENDIENTE":

                    final String fecha=alCustom.get(position).getDia();
                    final String fechaFinal=alCustom.get(position).getEnd_date();
                    boton.setEnabled(true);
                    boton.setVisibility(View.VISIBLE);

                    boton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                            //Actualizar sesiones penientes
                            //Agendar sesion ocupada
                        }
                    });


                    // Boton cancelar
                    boton_cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //actualizarStatusPendiente(alCustom.get(position).getId_teacher(), "2");

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
                                    message.setSubject("Sesión Cancelada");
                                    message.setContent(msgC, "text/html; charset=utf-8");
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

                    break;

                case "OCUPADO":
                    MENSAJE="LO SENTIMOS, TU SESIÓN HA SIDO CANCELADA";
                    TITULO="SESIÓN CANCELADA";

                    boton.setEnabled(false);
                    boton.setVisibility(View.INVISIBLE);
                    boton_cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            actualizarStatusTeacher(alCustom.get(position).getId_teacher(), "0", "" + alCustom.get(position).getId_fellow());

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
                                    message.setSubject( TITULO);
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
    public void actualizarStatusTeacher (String id_teacher, String status, String id_fellow)
    {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/calendar/actualizar_status_teacher.php"; //la url del web service obtener_sesionesEnEspera.php
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",id_teacher);
        requestParams.add("status",status);
        requestParams.add("id_fellow",id_fellow);

        //envio el parametro


        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {




            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "Error status"+error, Toast.LENGTH_SHORT).show();
            }
        });



    }//FIN SESIONES


    public void actualizarStatus (String id_teacher, String status)
    {
        //Este metodo actualiza el status a 0 cuando el teacher cancela una disponibilidad
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/calendar/actualiza_status.php"; //la url del web service obtener_sesionesEnEspera.php
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",id_teacher);
        requestParams.add("status",status);

        //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Toast.makeText(context, "Error status", Toast.LENGTH_SHORT).show();
            }
        });



    }//FIN SESIONES

    class RetreiveFeedTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL));
                message.setSubject(sub);
                message.setContent(msg, "text/html; charset=utf-8");
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



}

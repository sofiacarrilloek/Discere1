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

import java.util.ArrayList;
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
    String ID_TEACHER, CLLC;
    Session session = null;
    String EMAIL, NAME, LAST_NAME, TIPO, ESTADO_SESION;
    ProgressDialog pdialog = null;
    String subC = "Sesión Discere";
    String msgC = "Lo sentimos, tú sesión ha sido rechazada";
    String sub = "Sesión Discere";
    String msg = "Tienes una solicitud de sesión";


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



        cargarP();


        ESTADO_SESION=alCustom.get(position).getEstado();
        tvName.setText("Name: " + alCustom.get(position).getNombre_teacher());
        tvEmail.setText("Email: " + alCustom.get(position).getEmail_teacher());
        EMAIL = alCustom.get(position).getEmail_teacher();
        tvDate.setText("Date " + alCustom.get(position).getDia());//
        Tipo.setText(alCustom.get(position).getTipo().toUpperCase() + " SESSION");

        if (TIPO.equalsIgnoreCase("Fellow")&&ESTADO_SESION.equalsIgnoreCase("disponible"))
        {
            boton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    agendarSesionPendiente("6028", "" + alCustom.get(position).getId_teacher(), "" + alCustom.get(position).getTipo(),
                            "" + alCustom.get(position).getNombre_teacher(), "" + alCustom.get(position).getNombre_teacher(),
                            "" + NAME, "" + LAST_NAME, "" + alCustom.get(position).getDia(), "0", "" + alCustom.get(position).getEmail_teacher());

                    ID_TEACHER = alCustom.get(position).getId_teacher();
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

//-------------------------------------------------------------------------


//--------------------------------------------------------------------------

            });

            boton_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

//-----------------------
            });
            boton_cancelar.setEnabled(false);
            boton_cancelar.setBackgroundColor(000000);




        }
        else
        {
            boton_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
//hola
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

//-----------------------
            });
            if (ESTADO_SESION.equalsIgnoreCase("Ocupado")){
                boton.setEnabled(false);
                boton.setBackgroundColor(000000);}
        }


//PENDIENTE--------------------------

        if (TIPO.equalsIgnoreCase("Fellow")&&ESTADO_SESION.equalsIgnoreCase("pendiente"))
        {
            boton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    ID_TEACHER = alCustom.get(position).getId_teacher();
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
                            message.setSubject("Sesion Aceptada");
                            message.setContent("Tu sesión ha sido aceptada", "text/html; charset=utf-8");
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

            boton_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                            message.setSubject("Sesión Rechazada");
                            message.setContent("Tu sesión ha sido rechazada", "text/html; charset=utf-8");
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




        }



        return listViewItem;
    }

    public void agendarSesionPendiente ( final String id_fellow, final String id_teacher,
                                         final String type, final String name_teacher, final String last_name_teacher,
                                         final String name_fellow, final String last_name_fellow,
                                         final String start_date, final String status, final String email)
    {


        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://puntosingular.mx/cas/calendar/insertar_sesion_pendiente.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        //envio el parametro
        requestParams.add("id_fellow", id_fellow);
        requestParams.add("id_teacher", id_teacher);
        requestParams.add("type", type);
        requestParams.add("name_teacher", name_teacher);
        requestParams.add("last_name_teacher", last_name_teacher);
        requestParams.add("name_fellow", name_fellow);
        requestParams.add("last_name_fellow", last_name_fellow);
        requestParams.add("start_date", start_date);
        requestParams.add("status", status);
        requestParams.add("email", email);


        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN

    private void cargarP ()
    {
        SharedPreferences preferencia = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        NAME = preferencia.getString("NAME2", "NO EXISTE");
        LAST_NAME = preferencia.getString("LAST_NAME2", "NO EXISTE");
        TIPO = preferencia.getString("TIPO2", "NO EXISTE");
    }//Fin cargar preferencias


}




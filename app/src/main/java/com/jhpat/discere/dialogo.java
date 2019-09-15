package com.jhpat.discere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

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


public class dialogo extends MainActivity2 {

    public String nombre_1_1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView nombre, apellido, email,nombre_1,apellido_1;

    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE
    JSONObject jsonObject;



    public  static String  USUARIO, ID_USUARIO;

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    String reciep ="adrian241619@gmail.com";
    String sub ="Sesión Discere";
    String msg ="Tienes una solicitud de sesión";
    Session sessionC = null;
    ProgressDialog pdialogC = null;
    Context contextC = null;
    String reciepC ="adrian241619@gmail.com";
    String subC ="Sesión Discere";
    String msgC ="Lo sentimos, tú sesión ha sido rechazada";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cdialogo);


        recibirDatos();


        context = this;



        Button aceptar = (Button) findViewById(R.id.btnaceptar);
        aceptar.setOnClickListener(new View.OnClickListener() {   @Override
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

                    try{
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(lolitox));
                        message.setSubject(sub);
                        message.setContent(msg, "text/html; charset=utf-8");
                        Transport.send(message);
                    } catch(MessagingException e) {
                        e.printStackTrace();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    pdialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                }
            }});


        contextC = this;
        Button rechazar = (Button) findViewById(R.id.btncancel);
        rechazar.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(View v) {

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            sessionC = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("discerenc2019@gmail.com", "Adrian16");
                }
            });

            pdialogC = ProgressDialog.show(contextC, "", "Sending Mail...", true);

            RetreiveFeedTask task = new RetreiveFeedTask();
            task.execute();

        }

            class RetreiveFeedTask extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... params) {

                    try{
                        Message message = new MimeMessage(sessionC);
                        message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL1));
                        message.setSubject(subC);
                        message.setContent(msgC, "text/html; charset=utf-8");
                        Transport.send(message);
                    } catch(MessagingException e) {
                        e.printStackTrace();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    pdialogC.dismiss();

                    Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                }
            }});





    }





    public void recibirDatos(){
        Bundle extras = getIntent().getExtras();
        String nombreF = extras.getString("Rnombre");
        String fecha = extras.getString("Rfecha");
        String horaInicio = extras.getString("RhoraI");
        String horaFin = extras.getString("RhoraF");


        TextView date = (TextView) findViewById(R.id.date);
        TextView timeset = (TextView) findViewById(R.id.time);
        TextView namess = (TextView) findViewById(R.id.fellow);
        namess.setText("Fellow: " + nombreF);
        date.setText("Date: "+ fecha);
        timeset.setText("Time: " + horaInicio + " - " + horaFin );
    }

}


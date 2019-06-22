package com.jhpat.discere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
    private String sturl;
    public static List<String> day_string;
    String ID_TEACHER, CLLC;
    Session session = null;
    String EMAIL;
    ProgressDialog pdialog = null;
    String subC ="Sesión Discere";
    String msgC ="Lo sentimos, tú sesión ha sido rechazada";
    String sub ="Sesión Discere";
    String msg ="Tienes una solicitud de sesión";


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

        TextView tvTitle=(TextView)listViewItem.findViewById(R.id.time);
        TextView tvSubject=(TextView)listViewItem.findViewById(R.id.fellow);
        TextView tvDuedate=(TextView)listViewItem.findViewById(R.id.date);
        TextView Tipo=(TextView)listViewItem.findViewById(R.id.tvTipo);
        Button boton = (Button)listViewItem.findViewById(R.id.btnaceptar);


        tvTitle.setText("Name : "+alCustom.get(position).getTypes());
        tvSubject.setText("Email: "+alCustom.get(position).getSubjects());
        EMAIL=alCustom.get(position).getSubjects();
        tvDuedate.setText("Date : "+alCustom.get(position).getDuedates());
        Tipo.setText(alCustom.get(position).getDescripts().toUpperCase()+" SESSION");
        boton.setOnClickListener(new View.OnClickListener()
                                 {
                                     @Override
                                     public void onClick(View v)
                                     {

                                         ID_TEACHER=alCustom.get(position).getSubjects();
                                         Toast.makeText(context, "SELECCIONADO: "+ID_TEACHER, Toast.LENGTH_SHORT).show();
                                         actualizarStatus(ID_TEACHER, "1");

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
                                                 message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL));
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

                                             Toast.makeText(context, "Email sent", Toast.LENGTH_LONG).show();
                                         }
                                     }});


        return  listViewItem;
    }


    public void actualizarStatus (final String Correo, String Status)
    {

        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/actualiza_status.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_teacher",Correo); //envio el parametro

        requestParams.add("status",Status);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error: "+error, Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN

}


package com.jhpat.discere;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import cz.msebera.android.httpclient.Header;

class DialogAdaptorStudent extends BaseAdapter {
    Activity activity;

    private Activity context;
    private ArrayList<Dialogpojo> alCustom;
    private String sturl;
    public static List<String> day_string;
    String ID_TEACHER, CLLC;



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
        View listViewItem = inflater.inflate(R.layout.row_addapt, null, true);

        TextView tvTitle=(TextView)listViewItem.findViewById(R.id.tv_name);
        final TextView tvSubject=(TextView)listViewItem.findViewById(R.id.tv_type);
        TextView tvDuedate=(TextView)listViewItem.findViewById(R.id.tv_desc);
        final TextView tvDescription=(TextView)listViewItem.findViewById(R.id.tv_class);
        Button boton = (Button)listViewItem.findViewById(R.id.btn1);


        tvTitle.setText("Title : "+alCustom.get(position).getTitles());
        tvSubject.setText("Subject: "+alCustom.get(position).getSubjects());
        tvDuedate.setText("Due Date : "+alCustom.get(position).getDuedates());
        tvDescription.setText("Description : "+alCustom.get(position).getDescripts());
        boton.setOnClickListener(new View.OnClickListener()
                                 {
                                     @Override
                                     public void onClick(View v)
                                     {

                                         ID_TEACHER=alCustom.get(position).getSubjects();
                                         Toast.makeText(context, "SELECCIONADO: "+ID_TEACHER, Toast.LENGTH_SHORT).show();
                                         actualizarStatus(ID_TEACHER, "1");

                                     }
                                 }

        );


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

            }
        });


    }//FIN

}


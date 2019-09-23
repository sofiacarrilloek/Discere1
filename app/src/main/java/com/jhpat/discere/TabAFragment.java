package com.jhpat.discere;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabAFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    EditText te_1,te_2;
    private  int dia,mes,ano,hora,minutos;
    Button b1,b2,b3;
    String month;
    String day;
    String mas,x_1,x_2;
    String Minutos;
    //barra de progreso
    ProgressDialog progreso;
    private String usuario;
    String tip;
    String email;
    String nombre,apellido;
    //importante
    JSONObject jsonObject;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    View rootView;
    int comprobar_1=1,comprobar_2=1;

    public TabAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_a, container, false);
        te_1=rootView.findViewById(R.id.t_1);
        te_2=rootView.findViewById(R.id.t_2);

        b1=rootView.findViewById(R.id.b_1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = null;
                switch (v.getId()) {
                    case R.id.b_1:
                        final Calendar c= Calendar.getInstance();
                        dia=c.get(Calendar.DAY_OF_MONTH);
                        mes=c.get(Calendar.MONTH);
                        ano=c.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //te_1.setText(year+"/"+"0"+(monthOfYear+1)+"/"+dayOfMonth);
                                if(monthOfYear < 10){

                                    month = "0" + (monthOfYear+1);
                                }else if(monthOfYear >=10){
                                    month=""+(monthOfYear+1);
                                }
                                if(dayOfMonth < 10){

                                    day  = "0" + dayOfMonth ;
                                }
                                else if(dayOfMonth>=10){
                                    day=""+dayOfMonth;
                                }
                                comprobar_1=2;
                                te_1.setText(year+"/"+month+"/"+day);
                            }
                        }
                                ,ano,mes,dia);
                        datePickerDialog.show();
                        break;

                }
                if (miIntent != null) {
                    startActivity(miIntent);
                }
            }
        });
        b2=rootView.findViewById(R.id.b_2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = null;
                switch (v.getId()) {
                    case R.id.b_2:
                        final Calendar x= Calendar.getInstance();
                        hora=x.get(Calendar.HOUR_OF_DAY);
                        minutos=x.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(rootView.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(minute>=10){
                                    Minutos=""+minute;
                                }else{
                                    Minutos="0"+minute;
                                }
                                comprobar_2=2;
                                te_2.setText(hourOfDay+":"+Minutos);

                                //agrega los 15 o 45 minutos
                                if (tip.equals("Coach") ){
                                    if(minute>5){
                                        hourOfDay=hourOfDay+1;
                                        minute=minute-60;
                                    }

                                    mas=(hourOfDay+":"+(minute+45));
                                }else{
                                    if(minute>45){
                                        hourOfDay=hourOfDay+1;
                                        minute=minute-60;
                                    }
                                    mas=(hourOfDay+":"+(minute+15));
                                }

                            }
                        },hora,minutos,false);
                        timePickerDialog.show();
                        break;


                }
                if (miIntent != null) {
                    startActivity(miIntent);
                }
            }
        });
        b3=rootView.findViewById(R.id.add);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent miIntent = null;
                switch (v.getId()) {

                    case R.id.add:
                        if (comprobar_1 !=1 && comprobar_2 != 1){
                            cargarWebService();
                        }else{
                            Toast.makeText(rootView.getContext(),"Datos vacios",Toast.LENGTH_LONG).show();
                        }



                        break;

                }
                if (miIntent != null) {
                    startActivity(miIntent);
                }
            }
        });

        request= Volley.newRequestQueue(rootView.getContext());
        cargarP();
        Toast.makeText(rootView.getContext(),"HOLA"+usuario+"Tipo"+tip+" El Email: "+email+" "+nombre+" "+apellido,Toast.LENGTH_LONG).show();

        return rootView;


    }

    //receptor
    public void onClick (View view){


    }
    //receptor
    private  void cargarP()
    {
        SharedPreferences preferencia =getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        usuario= preferencia.getString("ID2", "NO EXISTE");
        tip=preferencia.getString("TIPO2", "holaperro");
        email=preferencia.getString("EMAIL2","No hay email");
        nombre=preferencia.getString("NAME2","HOLA Crack");
        apellido=preferencia.getString("LAST_NAME2","hola");
    }//Fin cargar preferencias

    private void cargarWebService() {
        //barra de dialogo
        progreso=new ProgressDialog(rootView.getContext());
        progreso.setMessage("cargando....");
        progreso.show();
        //barra de dialogo
        int id=14;
        String tipo="coach",titulo="pruebas",star="2019-06-15 01:40:00.000000",fin="2019-06-26 2000:02:00.000000";

             x_1=te_1.getText().toString()+" "+te_2.getText().toString()+":00.000000";
             x_2=te_1.getText().toString()+" "+mas+":00.000000";


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        String x="http://puntosingular.mx/cas/conexcion_coach/pruebas.php?id_="+usuario+"&type="+tip+"&title="+tip+"&start="+x_1+"&end_="+x_2+"&status="+0+"&email="+email+"&nombre="+nombre+"&apellido="+apellido;
        String URL="http://puntosingular.mx/cas/conexcion_coach/registro.php?user="+usuario+"&type="+tip+"&title="+tip+"&start="+fecha+"&end=10&start_date="+x_1+"&end_date="+x_2;


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"No se pudo registrar"+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Se ha resgistrado correctamente",Toast.LENGTH_SHORT).show();
        progreso.hide();
    }

}

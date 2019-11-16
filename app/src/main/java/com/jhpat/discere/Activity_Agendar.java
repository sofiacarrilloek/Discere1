package com.jhpat.discere;
//NO SE USA
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.jhpat.discere.Login.TIPO1;

public class Activity_Agendar extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    EditText te_1,te_2;
    private  int dia,mes,ano,hora,minutos;
    String month;
    String day;
    String mas;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__agendar);

        te_1=findViewById(R.id.t_1);
        te_2=findViewById(R.id.t_2);

        request= Volley.newRequestQueue(this);
        cargarP();
        Toast.makeText(getApplicationContext(),"HOLA"+usuario+"Tipo"+tip+" El Email: "+email+" "+nombre+" "+apellido,Toast.LENGTH_LONG).show();

    }
    //receptor
    public void onClick (View view){
        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.b_1:
                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                ano=c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                        te_1.setText(year+"/"+month+"/"+day);
                    }
                }
                        ,ano,mes,dia);
                datePickerDialog.show();
                break;
            case R.id.b_2:
                final Calendar x= Calendar.getInstance();
                hora=x.get(Calendar.HOUR_OF_DAY);
                minutos=x.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(minute>=10){
                            Minutos=""+minute;
                        }else{
                            Minutos="0"+minute;
                        }
                        te_2.setText(hourOfDay+":"+Minutos);
                        mas=(hourOfDay+":"+(minute+15));
                    }
                },hora,minutos,false);
                timePickerDialog.show();
                break;
            case R.id.add:
                cargarWebService();
                break;

        }
        if (miIntent != null) {
            startActivity(miIntent);
        }

    }


    //receptor
    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        usuario= preferencia.getString("ID2", "NO EXISTE");
        tip=preferencia.getString("TIPO2", "holaperro");
        email=preferencia.getString("EMAIL2","No hay email");
        nombre=preferencia.getString("NAME2","HOLA Crack");
        apellido=preferencia.getString("LAST_NAME2","hola");
    }//Fin cargar preferencias

    private void cargarWebService() {
        //barra de dialogo
        progreso=new ProgressDialog(this);
        progreso.setMessage("cargando....");
        progreso.show();
        //barra de dialogo
        int id=14;
        String tipo="coach",titulo="pruebas",star="2019-06-15 01:40:00.000000",fin="2019-06-26 2000:02:00.000000";
        String x_1=te_1.getText().toString()+" "+te_2.getText().toString()+":00.000000";
        String x_2=te_1.getText().toString()+" "+mas+":00.000000";

        String URL="http://puntosingular.mx/cas/conexcion_coach/pruebas.php?id_="+usuario+"&type="+tip+"&title="+tip+"&start="+x_1+"&end_="+x_2+"&status="+0+"&email="+email+"&nombre="+nombre+"&apellido="+apellido;



        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this,"No se pudo registrar"+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(this,"Se ha resgistrado correctamente",Toast.LENGTH_SHORT).show();
        progreso.hide();
    }
}

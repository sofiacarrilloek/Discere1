package com.jhpat.discere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Profile_Password extends AppCompatActivity {
    EditText et_password, et_cpassword, et_cpassword_2;
    Button btn_actualizarCon, btn_cancelCon;

    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE
    private static final String UURL = "http://100.26.2.12/discere/consulta_password.php";
    private static final String TAG_SUCCESS = "success";
    JSONObject jsonObject;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_password);

        et_password = (EditText) findViewById(R.id.enter_Password);
        et_cpassword = (EditText)findViewById(R.id.et_cpass);
        et_cpassword_2=(EditText)findViewById(R.id.et_cpass_2);
        btn_actualizarCon= (Button) findViewById(R.id.btn_updateCon);
        btn_cancelCon= (Button) findViewById(R.id.btn_cancel_con);


        btn_cancelCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile_Password.this, "Cancel", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Profile_Password.this,pantalla_principal.class);
                startActivity(intent);

            }
        });

        btn_actualizarCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cargarP();

            }
        });


    }


    public void editarContraseña(String id_usu)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://100.26.2.12/discere/consulta_password.php"; //la url del web service
        final RequestParams requestParams =new RequestParams();


        //Aqui hace la encriptación a md5 y luego a Base64
        MessageDigest md = null;
        final String MD5 = "MD5";
        try {
            md = java.security.MessageDigest
                    .getInstance(MD5);
            //step 2
        } catch (NoSuchAlgorithmException e) {
        }
        try {
            md.update(et_cpassword_2.getText().toString().getBytes("UTF-8"));//step 3
        } catch (UnsupportedEncodingException e) {
        }
        byte raw[] = md.digest(); //step 4
        String hashX = new String(Base64.encodeBase64(raw));
        //Base64.decodeBase64(hash.getBytes());

        //ENVIO LOS PARAMETROS
        requestParams.add("id_", ID1);
        requestParams.add("password",hashX);

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {



            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                if (statusCode==200) // Lo mismo que con LOGIN
                {
                    Toast.makeText(Profile_Password.this, "Change saved", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Profile_Password.this,pantalla_principal.class);
                    startActivity(intent);



                }
                else
                {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Profile_Password.this, "ERROR", Toast.LENGTH_SHORT).show();
            }





        });




    }//FIN EDITAR CONTRASEÑA
    //Aquí se ejecuta le cógio y consulta para poder cambiar la contraseña desde la BD


    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        ID1 = preferencia.getString("ID2", "NO EXISTE");
        String c = preferencia.getString("user", "NO EXISTE");
        String contra = preferencia.getString("password", "NO EXISTE");

        String pas1, pas2, con;
        pas1 = et_cpassword.getText().toString();
        pas2=et_cpassword_2.getText().toString();
        con = et_password.getText().toString();//UNO

        //Con esto cargamos la contraseña desde la BD

        if (con.equals(contra))
        {
            if(pas1.equals(pas2))
            {
                editarContraseña(ID1);
                guardarPreferencias(pas2);



            }else
            {
                Toast.makeText(Profile_Password.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(Profile_Password.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }//Aquí comparamos la nueva contraseña para verificar que sena iguales



    }//Fin cargar preferencias

    private void guardarPreferencias(String PASSWORD1)
    {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("password", PASSWORD1);
        editor.commit();

    }


}

package com.jhpat.discere;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProfileDos extends AppCompatActivity
{
    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;
    int TIPO =1;




    ImageView iv;
    EditText et;

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://puntosingular.mx/cas/imagen.php";

    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";




    EditText nombre, apellido, correoe, genero, tel;
    public static String NAME1, LAST_NAME1, GENDER1, ID1, EMAIL1, TEL1, PASSWORD1;//CLASE
    JSONObject jsonObject;
    public  static String  USUARIO, ID_USUARIO;
    Button botonCargar;
    ImageView imagen;
    Button btn_actualizar, btn_cancelar, bt_subir;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_dos);



        imagen = (ImageView) findViewById(R.id.ImageProfile);
        btn_actualizar = (Button) findViewById(R.id.btn_update);
        btn_cancelar=(Button)findViewById(R.id.btn_cancelar);
        bt_subir=(Button)findViewById(R.id.subirimg);
        nombre = (EditText)findViewById(R.id.et_name);
        apellido = (EditText)findViewById(R.id.et_lastname);
        correoe = (EditText)findViewById(R.id.et_email);
        genero = (EditText)findViewById(R.id.et_gender);
        tel = (EditText)findViewById(R.id.et_phone);


        if(validaPermisos()){
            imagen.setEnabled(true);
        }else{
            imagen.setEnabled(false);
        }

        cargarP();

        //cargarPreferencias();

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editarDatos(ID1);
                Intent intent=new Intent(ProfileDos.this, pantalla_principal.class);
                startActivity(intent);

            }
        });//Boton_actualizar fin

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(ProfileDos.this, "Unsaved data", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileDos.this,pantalla_principal.class);
                startActivity(intent);

            }
        });//Boton_cancelar prro fin

        bt_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
uploadImage();

            }
        });
    }



    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                botonCargar.setEnabled(true);
            }else{
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ProfileDos.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(ProfileDos.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ProfileDos.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                } else {
                    if (opciones[i].equals("Cargar Imagen")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), COD_SELECCIONA);
                    } else {
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }




    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(ProfileDos.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ProfileDos.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nombre = ID1;

                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_NOMBRE, nombre);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }





    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen= ID1;
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);

        ////
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imagen.setImageURI(miPath);
                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });

                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);

                    break;
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }}

    }



    public void editarDatos(final String id_usu)
    {
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://puntosingular.mx/cas/editar_usuario.php"; //la url del web service
        final RequestParams requestParams =new RequestParams();

        //ENVIO LOS PARAMETROS
        NAME1=nombre.getText().toString();
        EMAIL1=correoe.getText().toString();
        LAST_NAME1=apellido.getText().toString();
        GENDER1=genero.getText().toString();
        TEL1=tel.getText().toString();
        guardarPreferencias();
        requestParams.add("correo",correoe.getText().toString());
        requestParams.add("id_",id_usu);
        requestParams.add("nombre",nombre.getText().toString());
        requestParams.add("apellido",apellido.getText().toString());
        requestParams.add("genero",genero.getText().toString());
        requestParams.add("nTelefono",tel.getText().toString());



        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {



            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                if (statusCode==200) // Lo mismo que con LOGIN
                {
                    Toast.makeText(ProfileDos.this, "Changes saved", Toast.LENGTH_SHORT).show();

                }
                else
                {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ProfileDos.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

    }//FIN EDITAR USUARIO


    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        ID1 = preferencia.getString("ID2", "NO EXISTE");
        String vEmail= preferencia.getString("EMAIL2", "NO EXISTE"),
                VName= preferencia.getString("NAME2", "NO EXISTE"),
                VLastName= preferencia.getString("LAST_NAME2", "NO EXISTE"),
                vPhone= preferencia.getString("TEL2", "NO EXISTE"),
                vGen=preferencia.getString("GENDER2", "NO EXISTE");


        nombre.setText(VName);
        apellido.setText(VLastName);
        correoe.setText(vEmail);
        genero.setText(vGen);
        tel.setText(vPhone);

        //   datosc(ID1);
    }//Fin cargar preferencias

    private void guardarPreferencias()
    {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencia.edit();
        //editor.clear();

        editor.putString("NAME2", NAME1);
        editor.putString("LAST_NAME2", LAST_NAME1);
        editor.putString("GENDER2", GENDER1);
        editor.putString("ID2", ID1);
        editor.putString("PASSWORD2", PASSWORD1);
        editor.putString("EMAIL2", EMAIL1);
        editor.putString("TEL2", TEL1);

        editor.commit();
    }

}
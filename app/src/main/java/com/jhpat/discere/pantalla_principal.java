package com.jhpat.discere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import com.jhpat.discere.Tabla.Prueba;
import com.jhpat.discere.Tabla.Taudio_defect;
import com.jhpat.discere.Tabla.splash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class pantalla_principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,profile_principal.OnFragmentInteractionListener,fragment_principal.OnFragmentInteractionListener, Fragment_skype.OnFragmentInteractionListener{
    private TextView N;
    private TextView C;
    private String id,c,n,ape;
    private MenuItem listenaudio;
    String usuario,TIPO1,nombre;
    FloatingActionMenu actionMenu;
    com.github.clans.fab.FloatingActionButton ver,Agendar;
    JSONObject jsonObject;
    String tipo;
    private String id2,id3,id4,id5,id6;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Agendar=(FloatingActionButton) findViewById(R.id.agendar);
        actionMenu=(FloatingActionMenu) findViewById(R.id.fab);
        actionMenu.setClosedOnTouchOutside(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview=navigationView.getHeaderView(0);
        N=(TextView) hview.findViewById(R.id.Correo);
        C=(TextView)hview.findViewById(R.id.Nombre);
        navigationView.setNavigationItemSelectedListener(this);
        cargarPreferencias();
        //Bundle datos = this.getIntent().getExtras();
        //usuario=datos.getString("hola");




        cargarp2();


        N.setText(c);
        C.setText(n+" "+ape);

    }

    public void pasar(){
        Intent inten = new Intent(getApplicationContext(), Prueba.class);
        startActivity(inten);
    }
    public void SubirAudio(){
        Intent inten = new Intent(getApplicationContext(), UploadAudio.class);
        startActivity(inten);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment miFragment=null;
        boolean fragmentSeleccionado=false;
        if (id == R.id.nav_home) {
            panta();
            return true;
        }else if (id == R.id.nav_camera) {
            miFragment = new profile_principal();
            actionMenu.setVisibility(View.GONE);

        } else if (id == R.id.nav_gallery) {
            pasar();
            return true;

        } else if (id == R.id.nav_skype){
            Skype();
            return true;
        }else if (id == R.id.nav_listafellows) {
            listafellows();
            return true;
        } else if (id == R.id.nav_out) {
            onOutSesion();
            return true;
        }
        if(fragmentSeleccionado=true){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_principal,miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void listafellows() {

        Intent inten = new Intent(this, MainLista.class);
        startActivity(inten);
    }

    public void Skype(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.skype.raider");

        if (launchIntent != null){
            startActivity(launchIntent);
        }else{
            Toast.makeText(this,"You do not have the Skype application 😞",Toast.LENGTH_LONG).show();
        }

    }

    public void onOutSesion() {
        Login.cambiarEstado(pantalla_principal.this,false);
        Intent intent= new Intent(pantalla_principal.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void notificacion(){
        Intent intentc= new Intent(pantalla_principal.this,UploadAudio.class);
        startActivity(intentc);
    }

    public void ver(View view){
        Intent inten= new Intent(pantalla_principal.this, splash.class);
        startActivity(inten);
    }
    public void agendar(View view){
        cargarP();
        if(tipo.equals("Coach") || tipo.equals("Speaker") ){
            Intent intent= new Intent(pantalla_principal.this,TabsActivity.class);
            startActivity(intent);
        }else if(tipo.equals("Fellow")){
            Toast.makeText(getApplicationContext(),"Error permisos insuficientes",Toast.LENGTH_LONG).show();
            actionMenu.close(true);
        }else {
            Toast.makeText(getApplicationContext(),"Error contacte a un administrador para solucionar el problema",Toast.LENGTH_LONG).show();
            actionMenu.close(true);
        }


    }
    public void panta(){
        Intent intent=new Intent(pantalla_principal.this,pantalla_principal.class);
        startActivity(intent);
    }
    public void pant(){
        Intent intent=new Intent(pantalla_principal.this, Taudio_defect.class);
        startActivity(intent);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void cargarp2(){
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        id=preferencia.getString("ID2", "NO EXISTE");
        n=preferencia.getString("NAME2", "no hay nombre");
        c=preferencia.getString("EMAIL2","No hay email");
        ape=preferencia.getString("LAST_NAME2", "");
        obtenTipo(c);
        //Toast.makeText(getApplicationContext(),"El id del usuario es"+id,Toast.LENGTH_LONG).show();

    }



    public void obtenTipo (String Correo)
    {
        //
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/obtener_tipo_usuario.php"; //la url del web service
        // final String urlimagen ="http://dominio.com/assets/img/perfil/"; //aqui se encuentran todas las imagenes de perfil. solo especifico la ruta por que el nombre de las imagenes se encuentra almacenado en la bd.
        final RequestParams requestParams =new RequestParams();
        requestParams.add("correo",Correo); //envio el parametro
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                if (statusCode==200) // Lo mismo que con LOGIN
                {

                    try {
                        jsonObject = new JSONObject(new String(responseBody));
                        //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                        final String TIPO= jsonObject.getJSONArray("tipo").getJSONObject(0).getString("id_role");
                        //Toast.makeText(getApplicationContext(),"hola"+TIPO,Toast.LENGTH_LONG).show();

                        if (TIPO.equalsIgnoreCase("17"))
                        {
                            TIPO1="Coach";
                        }
                        if (TIPO.equalsIgnoreCase("24"))
                        {
                            TIPO1="Fellow";
                        }
                        if (TIPO.equalsIgnoreCase("18"))
                        {
                            TIPO1="Speaker";
                        }

                        guardarPreferencias2();
                        if(TIPO1.equals("Fellow")){
                            Agendar.setColorNormal(Color.GRAY);
                            Agendar.setClickable(false);
                            Agendar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(),"¡Solo los Speakers y los Coachs pueden agendar!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (JSONException e) {

                    }
                }

                else
                {
                    Toast.makeText(pantalla_principal.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {

                Toast.makeText(pantalla_principal.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN DATOSSC



    private void guardarPreferencias2()
    {

        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("TIPO2", TIPO1);
        editor.commit();

    }

    //parte de la tabla


    private void cargarPreferencias()
    {
        SharedPreferences preferencia = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String user = preferencia.getString("ID2", "NO EXISTE");
        obtenIDFELLOW(user);
    }

    //PARA EL FELLOW
    /*En este apartado se encuentran las funcionalidades para el fellow*/

    //PRIMERO: se obtienen TODOS los id_fellow de la tabla fellow pasando como parametro el id_user
    public void obtenIDFELLOW (String ID_USER)
    {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/calendar/obten_id_fellow.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_user",ID_USER); //envio el parametro id_user

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA="";//Acumulador de String

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_fellows[] = new String[tamanio]; //Vector para almacenar los registros que regrese
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_fellows[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_fellows[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }

                    obtenIDLessons(CONSULTA);


                } catch (JSONException e) {
                    //Toast.makeText(Grafico.this, "Error al cargar los datos"+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN OBTENIDFELLOW

    //SEGUNDO: Una vez que se obtienen los id_fellow se envian como parámetros para obtener los id_lesson
    public void obtenIDLessons (String ID_FELLOW)
    {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/cas/calendar/obtener_fecha_lessons.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_fellow",ID_FELLOW); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA="";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_lessons[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_lessons[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_lessons[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    //Toast.makeText(Grafico.this, "LOADING..."+CONSULTA, Toast.LENGTH_SHORT).show();
                    obtenLessonResult(CONSULTA);

                } catch (JSONException e) {
                    //Toast.makeText(Grafico.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN OBTENIDLessons

    //TERCERO: Mandando id_lessons se obtiene id_lessons_result
    public void obtenLessonResult (String ID_LESSON)
    {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/Obten_lesson_result.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_lesson",ID_LESSON); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA="";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_lesson_result[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_lesson_result[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");

                        CONSULTA = CONSULTA + id_lesson_result[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    //Toast.makeText(Grafico.this, "LOADING..."+CONSULTA, Toast.LENGTH_SHORT).show();
                    obtenDatosAudio(CONSULTA);

                } catch (JSONException e) {
                    //Toast.makeText(Grafico.this, "Error 276: "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }//FIN OBTENLESSONRESULT

    //CUARTO: Obten datos de la tabla audio enviando id_lesson_result
    public void obtenDatosAudio (String ID_LESSON_RESULT)
    {
        //Para el fellow
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url ="http://34.226.77.86/discere/Obten_datos_audio.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams =new RequestParams();
        requestParams.add("id_lesson_result",ID_LESSON_RESULT); //envio el parametro

        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                try {
                    String CONSULTA="";

                    jsonObject = new JSONObject(new String(responseBody));
                    //Apartir de aqui, les asigno a los editText el valor que obtengo del webservice
                    int tamanio =jsonObject.getJSONArray("datos").length();
                    String id_audio_analyst[] = new String[tamanio];
                    int cuentaOr=0;
                    String OR;

                    OR=", ";

                    for (int i=0; i<tamanio; i++) {
                        id_audio_analyst[i] = jsonObject.getJSONArray("datos").getJSONObject(i).getString("id_");


                        CONSULTA = CONSULTA + id_audio_analyst[i];
                        if (cuentaOr<tamanio-1) {
                            CONSULTA= CONSULTA + OR;
                        }

                        cuentaOr++;
                    }
                    SharedPreferences preferencia = getSharedPreferences("idaudio", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencia.edit();
                    editor.putString("audio", CONSULTA);
                    editor.commit();;

                    //Toast.makeText(getApplicationContext(),"hola"+CONSULTA,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    //Toast.makeText(Grafico.this, "Error al cargar los datos del teacher "+e, Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity2.this, "Error al cargar los datos del teacher", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        tipo=preferencia.getString("TIPO2", "no existe");

    }//Fin cargar preferencias


}
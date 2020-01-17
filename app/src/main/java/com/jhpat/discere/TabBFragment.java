package com.jhpat.discere;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TabBFragment extends Fragment {

    private static final int REQUEST_CODE_ASK_PERMISSION = 111;
    Spinner spinner;
    AsyncHttpClient client;
    View vista;
    Button buscarA,añadirA;
    TextView nombreA;
    private static final String TAG = TabBFragment.class.getSimpleName();
    private String selectedFilePath;
    TextView tvFileName;
    private static final String SERVER_PATH = "https://samplexl.000webhostapp.com/Acceso/s3.php";
    private File file,fileN;
    private int VALOR_RETORNO = 1;
    Uri fileUri;
    private static final int REQUEST_FILE_CODE = 200;
    String TIPO,NOMBRE;

    public TabBFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_tab_b, container, false);
        spinner = (Spinner) vista.findViewById(R.id.SpinnerFellows);
        client = new AsyncHttpClient();
        añadirA = (Button) vista.findViewById(R.id.añadirA);
        nombreA = (TextView) vista.findViewById(R.id.NombreAudio);
        buscarA= (Button) vista.findViewById(R.id.BuscarAudio);
        solicitarpermisos();
        cargarPreferencias();
        httpHandler handler = new httpHandler();
        String txt = handler.post("https://samplexl.000webhostapp.com/Acceso/s3.php");
        buscarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooserIntent();

            }
        });


        añadirA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileN != null) {
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(getContext());
                    uploadAsyncTask.execute();
                    //UploadAsyncTask.setNotificationConfig(new UploadAsyncTask());
                    //insertarAudio("http://34.226.77.86/discere/audios/"+file.getName());

                } else {
                    Toast.makeText(getActivity(),
                            "Please select a file first", Toast.LENGTH_LONG).show();

                }
            }
        });



        llenarSpinner();


        return vista;
    }

    private  void cargarPreferencias(){
        SharedPreferences preferencia =this.getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        TIPO = preferencia.getString("TIPO2", "NO EXISTE");
        NOMBRE = preferencia.getString("NOMBRE", "NO EXISTE");

        Toast.makeText(this.getActivity(), "TIPO: "+TIPO+","+NOMBRE, Toast.LENGTH_SHORT).show();


    }//Fin cargar preferencias
    public class httpHandler {
        public String post(String posturl){

            try {
                HttpClient httpclient = new DefaultHttpClient();

                /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/

                HttpPost httppost = new HttpPost(posturl);

                /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/

                //AÑADIR PARAMETROS
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("nombre", NOMBRE));

                /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/

                httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(params));
                /*Finalmente ejecutamos enviando la info al server*/
                HttpResponse resp = httpclient.execute(httppost);
                HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
                String text = EntityUtils.toString(ent);
                return text;
            }
            catch(Exception e) { return "error";}
        }
    }

    private void solicitarpermisos(){
        int perimisosExternal= ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE);
        if (perimisosExternal != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSION);
            }

        }
    }



    public void llenarSpinner(){
        String url="http://34.226.77.86/discere/obtener_fellows.php";
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    cargarSpiner(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cargarSpiner(String s) {
        ArrayList<Nombres> lista = new ArrayList<Nombres>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for (int i=0; i<jsonArray.length();i++){
                Nombres n = new Nombres();
                n.setName(jsonArray.getJSONObject(i).getString("name"));
                lista.add(n);
            }
            ArrayAdapter<Nombres> adapter = new ArrayAdapter<Nombres>(this.getActivity(), android.R.layout.simple_spinner_item,lista);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private class Nombres{
        private int id;
        private String name;
        private String last_name;

        public Nombres(){

        }

        public Nombres(int id, String name, String last_name){
            this.id = id;
            this.name = name;
            this.last_name = last_name;
        }

        public void setId(int id){
            this.id=id;
        }

        public void setName(String name){
            this.name=name;
        }

        public void setLast_name(String last_name){
            this.last_name=last_name;
        }

        @Override
        public String toString(){
            return name;
        }


    }

    protected void buscarAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            fileUri = data.getData();
            // previewFile(fileUri);
            String filePath = getRealPath(getContext(), fileUri);
            file = new File(filePath);
            Date date = new Date();
            DateFormat hourdateFormat = new SimpleDateFormat("yyyy-dd-MM");
            String historial = hourdateFormat.format(date);
            String nombreUsuario=TIPO;
            fileN=new File("audio_"+nombreUsuario+"_"+historial);

            Log.d(TAG, "Filename " + fileN.getName());
            nombreA.setText(fileN.getName());
            //insertarAudio(file.getName());
        }
    }

    public void insertarAudio(String urlAudio){
        AsyncHttpClient conexion = new AsyncHttpClient();
        final String url = "http://34.226.77.86/discere/insertarAudio.php"; //la url del web service obtener_fecha_lessons.ph
        final RequestParams requestParams = new RequestParams();
        //envio el parametro
        requestParams.add("link", urlAudio);
        conexion.post(url, requestParams, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Toast.makeText(getActivity(), "Session saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        });
    }
    /**
     * Show the file name and preview once the file is chosen
     * @param uri
     */


    /**
     * Shows an intent which has options from which user can choose the file like File manager, Gallery etc
     */
    private void showFileChooserIntent() {
        Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Choose any file
        fileManagerIntent.setType("*/*");
        startActivityForResult(fileManagerIntent, REQUEST_FILE_CODE);

    }



    public static String getRealPath(final Context context, final Uri uri) {

        if (uri.getScheme().equals("file")) {
            return uri.toString();

        } else if (uri.getScheme().equals("content")) {
            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (DocumentsContract.isDocumentUri(context, uri)) {

                    // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            return Environment.getExternalStorageDirectory() + "/" + split[1];
                        }

                        // TODO handle non-primary volumes
                    }
                    // DownloadsProvider
                    else if (isDownloadsDocument(uri)) {

                        final String id = DocumentsContract.getDocumentId(uri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    }
                    // MediaProvider
                    else if (isMediaDocument(uri)) {
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[]{
                                split[1]
                        };

                        return getDataColumn(context, contentUri, selection, selectionArgs);
                    }
                }
            }
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getContext());
    }



    private class UploadAsyncTask extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;
        private Exception exception;
        private ProgressDialog progressDialog;

        private UploadAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;


                HttpPost httpPost = new HttpPost(SERVER_PATH);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                multipartEntityBuilder.addPart("file", new FileBody(fileN));

                // Progress listener - updates task's progress
                MyHttpEntity.ProgressListener progressListener =
                        new MyHttpEntity.ProgressListener() {
                            @Override
                            public void transferred(float progress) {
                                publishProgress((int) progress);
                            }
                        };

                // POST
                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                        progressListener));


            try {
                httpResponse = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();

            }

            //  httpEntity = httpResponse.getEntity();




            return responseString;
        }

        @Override
        protected void onPreExecute() {

            // Init and show dialog
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            // Close dialog
            this.progressDialog.dismiss();
            //Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            buscarAudio();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress((int) progress[0]);
        }
    }
}
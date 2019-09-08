package com.jhpat.discere;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TabBFragment extends Fragment {

    Spinner spinner;
    AsyncHttpClient client;
    View vista;
    Button buscarA,añadirA;
    TextView nombreA;
    private static final String TAG = TabBFragment.class.getSimpleName();
    private String selectedFilePath;
    TextView tvFileName;
    private static final String SERVER_PATH = "http://puntosingular.mx/cas/upload.php";
    private File file;
    private int VALOR_RETORNO = 1;
    private ProgressDialog progreso;
    RequestQueue requestQueue;
    Bitmap bitmap;
    StringRequest stringRequest;

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
        buscarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAudio();

            }
        });


        añadirA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    TabBFragment.UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(getContext());
                    uploadAsyncTask.execute();
                    //UploadAsyncTask.setNotificationConfig(new UploadAsyncTask());


                } else {
                    Toast.makeText(getContext(),
                            "Please select a file first", Toast.LENGTH_LONG).show();

                }
            }
        });



        llenarSpinner();


        return vista;
    }




    public void llenarSpinner(){
        String url="http://puntosingular.mx/cas/obtener_fellows.php";
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

    public void buscarAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO )) {
            if (data == null) {
                //no data present
                return;
            }



            Uri selectedFileUri = data.getData();
            selectedFilePath = FilePath.getPath(getContext(), selectedFileUri);
            Log.i(TAG, "Selected File Path:" + selectedFilePath);

            if (selectedFilePath != null && !selectedFilePath.equals("")) {
                nombreA.setText(nombreA.getText()+""+selectedFilePath);
                Toast.makeText(getContext(),"Seleccionado",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
            }

        }
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

            try {
                HttpPost httpPost = new HttpPost(SERVER_PATH);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                multipartEntityBuilder.addPart("file", new FileBody(file));

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


                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(httpEntity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                e.printStackTrace();
                Log.e("UPLOAD", e.getMessage());
                this.exception = e;
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            Toast.makeText(getContext(),
                    result, Toast.LENGTH_LONG).show();
            buscarAudio();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress((int) progress[0]);
        }
    }



}

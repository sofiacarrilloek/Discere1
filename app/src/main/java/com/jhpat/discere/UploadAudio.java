package com.jhpat.discere;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class UploadAudio extends AppCompatActivity implements PermissionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_FILE_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://puntosingular.mx/cas/upload.php";
    Button fileBrowseBtn;
    Button uploadBtn;
    ImageView previewImage;
    TextView fileName;
    Uri fileUri;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_audio);

        fileBrowseBtn = findViewById(R.id.btn_choose_file);
        uploadBtn = findViewById(R.id.btn_upload);
        previewImage = findViewById(R.id.iv_preview);
        fileName = findViewById(R.id.tv_file_name);

        fileBrowseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check if app has permission to access the external storage.
                if (EasyPermissions.hasPermissions(UploadAudio.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showFileChooserIntent();

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(UploadAudio.this, "This app needs access to your file storage so that it can read files.", READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(UploadAudio.this);
                    uploadAsyncTask.execute();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please select a file first", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            fileUri = data.getData();
            previewFile(fileUri);

        }
    }

    /**
     * Show the file name and preview once the file is chosen
     * @param uri
     */
    private void previewFile(Uri uri) {
        String filePath = getRealPathFromURIPath(uri, UploadAudio.this);
        file = new File(filePath);
        Log.d(TAG, "Filename " + file.getName());
        fileName.setText(file.getName());

        ContentResolver cR = this.getContentResolver();
        String mime = cR.getType(uri);

        //Show preview if the uploaded file is an image.
        if (mime != null && mime.contains("image")) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            previewImage.setImageBitmap(bitmap);
        } else {
            previewImage.setImageResource(R.drawable.ic_file);
        }

        hideFileChooser();
    }

    /**
     * Shows an intent which has options from which user can choose the file like File manager, Gallery etc
     */
    private void showFileChooserIntent() {
        Intent fileManagerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //Choose any file
        fileManagerIntent.setType("*/*");
        startActivityForResult(fileManagerIntent, REQUEST_FILE_CODE);

    }

    /**
     * Returns the actual path of the file in the file system
     *
     * @param contentURI
     * @param activity
     * @return
     */
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        String realPath = "";
        if (cursor == null) {
            realPath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            realPath = cursor.getString(idx);
        }
        if (cursor != null) {
            cursor.close();
        }

        return realPath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, UploadAudio.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        showFileChooserIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Hides the Choose file button and displays the file preview, file name and upload button
     */
    private void hideFileChooser() {
        fileBrowseBtn.setVisibility(View.GONE);
        uploadBtn.setVisibility(View.VISIBLE);
        fileName.setVisibility(View.VISIBLE);
        previewImage.setVisibility(View.VISIBLE);
    }

    /**
     *  Displays Choose file button and Hides the file preview, file name and upload button
     */
    private void showFileChooser() {
        fileBrowseBtn.setVisibility(View.VISIBLE);
        uploadBtn.setVisibility(View.GONE);
        fileName.setVisibility(View.GONE);
        previewImage.setVisibility(View.GONE);

    }

    /**
     * Background network task to handle file upload.
     */
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
            Toast.makeText(getApplicationContext(),
                    result, Toast.LENGTH_LONG).show();
            showFileChooser();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            this.progressDialog.setProgress((int) progress[0]);
        }
    }

}


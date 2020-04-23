package com.jhpat.discere.Tabla;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader2 extends AsyncTask<Void,Integer,String> {
    Context c;
    String address2;
    ListView lv;

    ProgressDialog pd;

    public Downloader2(Context c, String address, ListView lv) {
        this.c = c;
        this.address2 = address;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Fetch Data");
        pd.setMessage("Fetching Data.... Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        String data=dowloadData();
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(s!=null){
            Parser2 p=new Parser2(c,s,lv);
            p.execute();


        }else{
            Toast.makeText(c,"Unable to dowload data",Toast.LENGTH_SHORT).show();
        }
    }


    private String dowloadData(){
        InputStream is=null;
        String line=null;

        try {
            URL url=new URL(address2);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuffer sb=new StringBuffer();

            if(br!=null){
                while((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
            }else{
                return null;
            }
            return sb.toString();

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try{
                    is.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}

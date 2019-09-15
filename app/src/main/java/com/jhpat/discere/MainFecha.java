package com.jhpat.discere;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

public class MainFecha extends AppCompatActivity{
    String url="http://puntosingular.mx/cas/tabla/Lista_nombre.php?id_teacher=6023";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);

        final ListView lv=(ListView) findViewById(R.id.lvf);
        final DownloaderF d=new DownloaderF(this,url,lv);

        d.execute();

    }
}

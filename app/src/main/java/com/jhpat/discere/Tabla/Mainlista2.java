package com.jhpat.discere.Tabla;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.jhpat.discere.R;

public class Mainlista2 extends AppCompatActivity {

    String url2="http://34.226.77.86/discere/cas/Lista_fechas.php?id_fellow=6028";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista2);

        final ListView lv=(ListView) findViewById(R.id.lv2);
        final Downloader2 d=new Downloader2(this,url2,lv);

        d.execute();

    }
}

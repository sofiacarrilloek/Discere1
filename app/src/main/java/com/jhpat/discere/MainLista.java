package com.jhpat.discere;

import android.content.Intent;
import android.os.Bundle;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class MainLista extends AppCompatActivity {


    String url="http://puntosingular.mx/cas/tabla/Lista_nombre.php?id_teacher=6023";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lista);
        FloatingActionButton botonFecha = findViewById(R.id.fabotton1);
        botonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainLista.this, MainFecha.class);
                startActivity(intent);
            }
        });


        final ListView lv=(ListView) findViewById(R.id.lv);
        final Downloader d=new Downloader(this,url,lv);

        d.execute();

    }
}

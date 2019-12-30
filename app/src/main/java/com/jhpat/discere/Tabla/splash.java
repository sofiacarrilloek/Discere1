package com.jhpat.discere.Tabla;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jhpat.discere.MainActivity2;
import com.jhpat.discere.R;

public class splash extends AppCompatActivity {
    private final int DURACION_SPLASH = 4000; // 3 segundos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)


        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(splash.this, MainActivity2.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}

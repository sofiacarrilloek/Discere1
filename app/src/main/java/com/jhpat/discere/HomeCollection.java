package com.jhpat.discere;

import java.util.ArrayList;

class HomeCollection {
    public String fecha_inicio;
    public String estado;
    public String tipo;
    public String id_teacher;
    public String email_teacher;
    public String nombre_teacher;

    public static ArrayList<HomeCollection> date_collection_arr;
    public HomeCollection(String fecha_inicio, String estado, String tipo, String id_teacher, String email_teacher, String nombre_teacher){

        this.fecha_inicio=fecha_inicio;
        this.estado=estado;
        this.tipo=tipo;
        this.id_teacher= id_teacher;
        this.email_teacher=email_teacher;
        this.nombre_teacher=nombre_teacher;


    }
}

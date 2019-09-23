package com.jhpat.discere;

import java.util.ArrayList;

class HomeCollection {
    public String fecha_inicio;
    public String estado;
    public String tipo;
    public String id_user_teacher;
    public String email_teacher;
    public String nombre_teacher;
    public String dia;
    public String id_teacher;
    public String id_fellow;

    public static ArrayList<HomeCollection> date_collection_arr;
    public HomeCollection(String fecha_inicio, String estado, String tipo, String id_user_teacher, String email_teacher,
                          String nombre_teacher, String dia, String id_teacher, String id_fellow){

        this.fecha_inicio=fecha_inicio;
        this.estado=estado;
        this.tipo=tipo;
        this.id_user_teacher= id_user_teacher;
        this.email_teacher=email_teacher;
        this.nombre_teacher=nombre_teacher;
        this.dia=dia;
        this.id_teacher=id_teacher;
        this.id_fellow=id_fellow;


    }
}

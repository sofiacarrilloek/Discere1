package com.jhpat.discere;

public class Dialogpojo {
    private String fecha_inicio;
    private String estado;
    private String tipo;
    private String id_user_teacher;
    private String email_teacher;
    private String nombre_teacher;
    private String dia;
    private String id_teacher;
    public void setDia(String dia){this.dia=dia;}

    public void setNombre_teacher(String nombre_teacher){this.nombre_teacher=nombre_teacher;}

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setId_user_teacher(String id_user_teacher) {
        this.id_user_teacher = id_user_teacher;
    }

    public  void setId_teacher(String id_teacher){
        this.id_teacher=id_teacher;
    }
    public void setEmail_teacher(String email_teacher) {
        this.email_teacher = email_teacher;
    }


    public String getId_teacher(){return  id_teacher;}
    public String getDia(){return  dia;}
    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public String getEstado() {
        return estado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getId_user_teacher() {
        return id_user_teacher;
    }

    public String getEmail_teacher() {
        return email_teacher;
    }

    public String getNombre_teacher(){return  nombre_teacher;}
}
package com.jhpat.discere.Tabla;
//Metodo donde obtengo los datos necesarios para la tabla
public  class Spacecraft {
    private int id_;
    private int  duration;
    private String defect_priority;
    private String defect_type;
    private String defect_description;
    private int id_audio_analyst;


    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDefect_priority() {
        return defect_priority;
    }

    public void setDefect_priority(String defect_priority) {
        this.defect_priority = defect_priority;
    }

    public String getDefect_type() {
        return defect_type;
    }

    public void setDefect_type(String defect_type) {
        this.defect_type = defect_type;
    }

    public String getDefect_description() {
        return defect_description;
    }

    public void setDefect_description(String defect_description) {
        this.defect_description = defect_description;
    }

    public int getId_audio_analyst() {
        return id_audio_analyst;
    }

    public void setId_audio_analyst(int id_audio_analyst) {
        this.id_audio_analyst = id_audio_analyst;
    }

    @Override
    public String toString() {
        return "Spacecraft{" +
                "defect_priority='" + defect_priority + '\'' +
                ", defect_type='" + defect_type + '\'' +
                ", defect_description='" + defect_description + '\'' +
                '}';
    }
}

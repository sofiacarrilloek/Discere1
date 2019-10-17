package com.jhpat.discere.Tabla;

import android.content.Context;

import java.util.ArrayList;

public class TableHelper {
    Context c;



    private String[] Space={"defect_priority ","defect_type ","defect_description"};//son los que se mostraran en la parte inferior de la tabla
    private String[][]SpaceProbes;

    public TableHelper(Context c) {
        this.c = c;
    }
    public String[] getSpace() {
        return Space;
    }
    public String[][] returnSpaceArray(ArrayList<Spacecraft> spacecrafts){
        SpaceProbes=new String[spacecrafts.size()][3];
        Spacecraft s;
        for(int i=0;i<spacecrafts.size();i++){
            s=spacecrafts.get(i);

            SpaceProbes[i][0]=s.getDefect_priority();//imprimo los datos que son necesarios para la tabla teniendo en cuenta el bucle
            SpaceProbes[i][1]=s.getDefect_type();
            SpaceProbes[i][2]=s.getDefect_description();
        }

        return SpaceProbes;

    }

}

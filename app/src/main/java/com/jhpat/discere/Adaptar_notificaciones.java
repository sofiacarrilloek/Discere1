package com.jhpat.discere;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptar_notificaciones extends RecyclerView.Adapter<Adaptar_notificaciones.ViewHolderDatos> {

    ArrayList<String> listDatos;


    public Adaptar_notificaciones(ArrayList<String> listDatos){ this.listDatos = listDatos;}

    @Override
    public Adaptar_notificaciones.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_contenedor_notificaciones,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }


    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder{

        TextView noti;

        public ViewHolderDatos(View itemView){
            super(itemView);
            noti = (TextView) itemView.findViewById(R.id.noti);
        }

        public void asignarDatos(String s) {
            noti.setText(s);
        }
    }

}

package com.example.app_final_bd;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Adaptador extends ArrayAdapter<Datos> {

    private Datos[] datos;

    public Adaptador(@NonNull Context context, Datos[] datos) {
        super(context,R.layout.element,datos);
        this.datos = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(getContext());
        View elemento = mostrado.inflate(R.layout.element, null, false);
        TextView texto1 = (TextView) elemento.findViewById(R.id.textView);
        texto1.setText(datos[position].getTexto1());
        TextView texto2 = (TextView) elemento.findViewById(R.id.textView2);
        texto2.setText(datos[position].getTexto2());
        TextView texto3 = (TextView) elemento.findViewById(R.id.textView3);
        texto3.setText(datos[position].getTexto3());
        return elemento;
    }
}
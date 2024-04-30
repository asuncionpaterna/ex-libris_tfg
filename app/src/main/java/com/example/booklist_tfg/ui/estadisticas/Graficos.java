package com.example.booklist_tfg.ui.estadisticas;

import android.content.res.Resources;

import com.example.booklist_tfg.R;

import java.util.ArrayList;

//Clase para la creación de gráficos y su actividades asociadas
public class Graficos {

    private final String name;
    private Class activityClass;

    //Constructor
    // @param name el nombre del gráfico
    // @param activityClass la clase de actividad asociada al gráfico
    private Graficos(String name, Class activityClass) {
        this.name = name;
        this.activityClass = activityClass;
    }

    public String getName() {
        return name;
    }

    Class getActivityClass() {
        return activityClass;
    }

    //Método que devuelve la lista de gráficos y sus actividades asociadas
    static ArrayList<Graficos> createChartList(Resources resources) {
        ArrayList<Graficos> chartList = new ArrayList<>();
        //Etiquetas
        String generosLiterariosLabel = resources.getString(R.string.label_generos_literarios);
        String lecturasMesLabel = resources.getString(R.string.label_lecturas_mes);

        //Se añaden los gráficos a la lista con las clases de actividad asociadas
        chartList.add(new Graficos(generosLiterariosLabel, BarChartActivity.class));
        chartList.add(new Graficos(lecturasMesLabel, PieChartActivity.class));

        return chartList;
    }

}
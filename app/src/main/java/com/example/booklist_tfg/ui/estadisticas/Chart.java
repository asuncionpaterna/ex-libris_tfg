package com.example.booklist_tfg.ui.estadisticas;

import android.content.res.Resources;

import java.util.ArrayList;

public class Chart {

    private final String name;
    private Class activityClass;

    private Chart(String name, Class activityClass) {
        this.name = name;
        this.activityClass = activityClass;
    }

    public String getName() {
        return name;
    }

    Class getActivityClass() {
        return activityClass;
    }

    static ArrayList<Chart> createChartList(Resources resources) {
        ArrayList<Chart> chartList = new ArrayList<>();

        chartList.add(new Chart("Grafico Barras", BarChartActivity.class));
        chartList.add(new Chart("Grafico Pie", PieChartActivity.class));


        return chartList;
    }

}
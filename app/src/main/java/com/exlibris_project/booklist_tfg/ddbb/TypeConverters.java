package com.exlibris_project.booklist_tfg.ddbb;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Clase para definir los métodos de conversión de tipos
public class TypeConverters {
    //Formato de fecha
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    //Método para convertir un ArrayList de cadenas de texto
    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
    //Método para convertir un String a un Date
    @TypeConverter
    public static Date toDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT);
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
    //Método para convertir un Date a un String
    @TypeConverter
    public static String fromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        return format.format(date);
    }
}

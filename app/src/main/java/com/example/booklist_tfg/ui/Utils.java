package com.example.booklist_tfg.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    ArrayList<String> generosLiterarios;

    public static String verificarDatos(String datos) {
        if (datos == null || datos.isEmpty()) {
            datos = "No hay datos disponibles.";
        }
        return datos;
    }


    public static String verificarGeneroLiterario(String generoLiterario) {
        if (generoLiterario == null || generoLiterario.isEmpty()) {
            return "No hay datos disponibles.";
        } else {
            String regex = ".*[\\[\\]\"].*"; // Para comprobar si tiene corchetes o comillas dobles
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(generoLiterario);

            if (matcher.matches()) {
                // Eliminar corchetes y comillas dobles, junto con espacios en blanco al inicio y al final
                generoLiterario = generoLiterario.replaceAll("[\\[\\]\"]", "").trim();
            }
        }
        return generoLiterario;
    }

    public static String formateoFecha(Date fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(fecha);
    }

    public static String formateoFechaString(String fecha) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaDate = formatoFecha.parse(fecha);
            formateoFecha(fechaDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "No hay datos disponibles.";
    }

    public static StringBuilder formateoAutoria(ArrayList<String> autoriaLista) {
        StringBuilder autoria = new StringBuilder();
        for (int i = 0; i < autoriaLista.size(); i++) {
            autoria.append(autoriaLista.get(i));
            autoria.append(i < autoriaLista.size() - 1 ? ", " : "");
        }
        return autoria;
    }


}

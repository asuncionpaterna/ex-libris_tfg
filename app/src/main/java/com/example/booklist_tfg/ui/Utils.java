package com.example.booklist_tfg.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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


    public static void showDatePicker(Context context, TextView fechaLecturaTV, Date fecha) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final Date[] FECHAFINAL = {fecha};
        // Crear el DatePickerDialog y configurarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Se establece la fecha seleccionada en el TextView
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        FECHAFINAL[0] = selectedDate.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(FECHAFINAL[0]);
                        fechaLecturaTV.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
        // Mostrar el diálogo
        datePickerDialog.show();
    }

}

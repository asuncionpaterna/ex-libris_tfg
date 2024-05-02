package com.exlibris_project.booklist_tfg.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.booklist_tfg.R;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    ArrayList<String> generosLiterarios;

    public static String verificarDatos(String datos, Context context) {
        if (datos == null || datos.isEmpty()) {
            datos =context.getString(R.string.error_no_datos);
        }
        return datos;
    }

    public static String verificarGeneroLiterario(String generoLiterario, Context context) {
        if (generoLiterario == null || generoLiterario.isEmpty()) {
            return context.getString(R.string.error_no_datos);
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

    public static StringBuilder formateoAutoria(ArrayList<String> autoriaLista) {
        StringBuilder autoria = new StringBuilder();
        for (int i = 0; i < autoriaLista.size(); i++) {
            autoria.append(autoriaLista.get(i));
            autoria.append(i < autoriaLista.size() - 1 ? ", " : "");
        }
        return autoria;
    }


    public static void showDatePicker(Context context, TextView fechaLecturaTV, Date fecha, OnDateSelectedListener listener) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int diaMes = calendar.get(Calendar.DAY_OF_MONTH);
        // Crear el DatePickerDialog y configurarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Se establece la fecha seleccionada en el TextView
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        Date fechaSeleccionada = selectedDate.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(fechaSeleccionada);
                        fechaLecturaTV.setText(formattedDate);
                        listener.onDateSelected(fechaSeleccionada);
                    }
                }, anio, mes, diaMes);
        // Mostrar el diálogo
        datePickerDialog.show();
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Date selectedDate);
    }

    public static void establecerTema(int uiMode, FrameLayout frameLayout) {
        if (uiMode == Configuration.UI_MODE_NIGHT_YES) {
            frameLayout.setBackgroundResource(R.drawable.fondo_oscuro);
        } else {
            frameLayout.setBackgroundResource(R.drawable.fondo_dia);
        }
    }

    public static void shareImage(Bitmap image, Context context) {

// Convertir el Bitmap en un ByteArray
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);

// Crear un Intent con la acción ACTION_SEND
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

// Establecer el tipo de datos que se compartirá (imagen PNG en este caso)
        shareIntent.setType("image/png");
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
// Añadir el ByteArray como un extra al Intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

// Iniciar la actividad para compartir
        context.startActivity(Intent.createChooser(shareIntent, "Share Chart"));

    }

    @SuppressLint("Range")
    public static String obtenerRutaRealDeUri(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static void restartApplication(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        // Required for API 34 and later
        // Ref: https://developer.android.com/about/versions/14/behavior-changes-14#safer-intents
        mainIntent.setPackage(context.getPackageName());
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}

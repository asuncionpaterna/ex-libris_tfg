package com.example.booklist_tfg.utils;

import static com.example.booklist_tfg.MainActivity.database;

import android.os.Environment;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.ddbb.LibroDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Json {
    public static void paletteJsonCreator() {
        JSONObject json = new JSONObject();
        JSONArray librosArr = new JSONArray();

        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();
            List<Libro> listaLibros = libroDAO.getAll();

            try {
                for (Libro libro : listaLibros) {
                    JSONObject libroObj = new JSONObject();

                    libroObj.put("Titulo", libro.getTitulo());
                    libroObj.put("Autoria", libro.getNombreAutoria());
                    libroObj.put("Editorial", libro.getEditorial());
                    libroObj.put("Genero", libro.getGenero());
                    libroObj.put("Año publicacion", libro.getAnioPublicacion());
                    libroObj.put("Portada", libro.getPortada());
                    libroObj.put("Favorito", libro.getFavorito());
                    libroObj.put("Es papel", libro.getEsPapel());
                    libroObj.put("Fecha lectura", libro.getFechaLectura());

                    librosArr.put(libroObj);

                }

                json.put("libros", librosArr);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss", Locale.getDefault());
                String fileName = "palettes_" + dateFormat.format(new Date()) + ".json";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(json.toString(2));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }


}

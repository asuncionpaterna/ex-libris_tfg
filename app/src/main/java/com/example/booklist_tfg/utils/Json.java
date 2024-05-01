package com.example.booklist_tfg.utils;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.utils.Utils.obtenerRutaRealDeUri;
import static com.example.booklist_tfg.utils.Utils.restartApplication;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.example.booklist_tfg.MainActivity;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.ddbb.LibroDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Json {
    public static void crearJsonBackup() {
        JSONObject json = new JSONObject();
        JSONArray librosArr = new JSONArray();

        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();
            List<Libro> listaLibros = libroDAO.getAll();

            try {
                for (Libro libro : listaLibros) {
                    JSONObject libroObj = new JSONObject();

                    libroObj.put("Titulo", libro.getTitulo());
                    libroObj.put("Autoria", new JSONArray(libro.getNombreAutoria()));
                    libroObj.put("Editorial", libro.getEditorial());
                    libroObj.put("Genero", libro.getGenero());
                    libroObj.put("Descripcion", libro.getDescripcion());
                    libroObj.put("Año publicacion", libro.getFechaPublicacion());
                    libroObj.put("Portada", libro.getPortada());
                    libroObj.put("Paginas", libro.getPaginas());
                    libroObj.put("Favorito", libro.getFavorito());
                    libroObj.put("Es papel", libro.getEsPapel());
                    libroObj.put("Fecha lectura", libro.getFechaLectura());

                    librosArr.put(libroObj);

                }

                json.put("libros", librosArr);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss", Locale.getDefault());
                String fileName = "libros" + dateFormat.format(new Date()) + ".json";
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

    public static void gestionarJson(Activity activity, Uri uri) {
        String nombreArchivo = obtenerRutaRealDeUri(activity, uri);

        if (nombreArchivo != null && nombreArchivo.endsWith(".json")) {
            try {
                // Leer el archivo y comprobar el atributo "libros"
                String contenidoJson = leerArchivo(uri, activity);


                JSONObject objetoJson = new JSONObject(contenidoJson);
                boolean tieneAtributoLibros = objetoJson.has("libros");

                if (tieneAtributoLibros) {
                    // El archivo es un .json y tiene el atributo "libros"
                    // Puedes continuar con el procesamiento del archivo

                    System.out.println("ES UN JSON CON LIBROS");
                    List<Libro> listaLibrosARestaurar = new ArrayList<>();


                    JSONArray librosObj = objetoJson.getJSONArray("libros");
                    //Se pueden hacer mas comprobaciones, si los libros tienen los demas atributos
                    for (int i = 0; i < librosObj.length(); i++) {
                        JSONObject libroObj = librosObj.getJSONObject(i);
                        String titulo = libroObj.getString("Titulo");

                        JSONArray autoria = libroObj.getJSONArray("Autoria");
                        ArrayList<String> autoriaLista = new ArrayList<>();
                        for (int j = 0; j < autoria.length(); j++) {
                            autoriaLista.add(autoria.getString(0));
                        }
                        String editorial = libroObj.getString("Editorial");
                        String genero = libroObj.getString("Genero");
                        String descripcion = libroObj.getString("Descripcion");
                        String anioPub = libroObj.getString("Año publicacion");
                        String portada = libroObj.getString("Portada");
                        int paginas = libroObj.getInt("Paginas");
                        boolean favorito = libroObj.getBoolean("Favorito");
                        boolean esPapel = libroObj.getBoolean("Es papel");
                        Date fechaLectura = new Date(libroObj.getString("Fecha lectura"));

                        Libro libro = new Libro(titulo, autoriaLista, editorial, genero, descripcion, anioPub, paginas, portada);
                        libro.setFechaLectura(fechaLectura);
                        libro.setFavorito(favorito);
                        libro.setEsPapel(esPapel);
                        listaLibrosARestaurar.add(libro);

                    }
                    Thread thread = new Thread(() -> {
                        LibroDAO libroDAO = MainActivity.database.libroDAO();

                        libroDAO.insertListaLibros(listaLibrosARestaurar);
                        restartApplication(activity);


                    });
                    thread.start();

                } else {
                    // El archivo no tiene el atributo "libros"
                    // Mostrar un mensaje de error o manejar la situación
                    System.out.println("NO ES UN JSON CON LIBROS");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // El archivo no es un .json
            // Mostrar un mensaje de error o manejar la situación
            System.out.println("NO ES UN JSON");

        }

    }


    public static String leerArchivo(Uri uri, Context context) {
        String fileContent;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                inputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileContent = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileContent;
    }

}
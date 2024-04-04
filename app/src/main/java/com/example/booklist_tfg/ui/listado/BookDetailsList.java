package com.example.booklist_tfg.ui.listado;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookDetailsList extends AppCompatActivity {

    String titulo, editorial, generoLiterario, portada, fechaLectura;
    private ArrayList<String> autoriaList;
    boolean favorito, esPapel;
    Libro libro;
    private Date fecha;


    private ImageView portadaIV;
    TextView tituloTV, autoriaTV, editorialTV, generoLiterarioTV, fechaLecturaTV;

    CheckBox favoritoCB, esPapelCB;
    Button volverBtn, modificarBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_list);

        //Inicializando los elementos
        tituloTV = findViewById(R.id.idTVTitulo);
        autoriaTV = findViewById(R.id.idTVAutoria);
        editorialTV = findViewById(R.id.idTVEditorial);
        generoLiterarioTV = findViewById(R.id.idTVGenero);
        fechaLecturaTV = findViewById(R.id.idTVFechaLectura);

        portadaIV = findViewById(R.id.idIVPortada);
        favoritoCB = findViewById(R.id.idCBFavorito);
        esPapelCB = findViewById(R.id.idCBesPapel);

        volverBtn = findViewById(R.id.idBtnEliminar);
        modificarBtn = findViewById(R.id.idBtnModificar);

        //Almacenando los datos en las variables
        libro = (Libro) getIntent().getSerializableExtra("libro");
        titulo = libro.getTitulo();
        autoriaList = libro.getNombreAutoria();
        editorial = libro.getEditorial();
        generoLiterario = libro.getGenero();
        fecha = libro.getFechaLectura();
        favorito = libro.getFavorito();
        esPapel = libro.getEsPapel();
        portada = libro.getPortada();


        StringBuilder autoria = new StringBuilder();
        for (int i = 0; i < autoriaList.size(); i++) {
            autoria.append(autoriaList.get(i));
            autoria.append(i < autoriaList.size() - 1 ? ", " : "");
        }

        fechaLectura = formateoFecha(fecha);
        tituloTV.setText("Título: " + titulo);
        autoriaTV.setText(("Autoria: ") + autoria);
        editorialTV.setText("Editorial: " + verificarEditorial(editorial));
        generoLiterarioTV.setText("Género literario: " + verificarGeneroLiterario(generoLiterario));
        fechaLecturaTV.setText(fechaLectura);
        esPapelCB.setChecked(esPapel);
        favoritoCB.setChecked(favorito);

        Picasso.get().load(portada).into(portadaIV);
    }

    protected String verificarEditorial(String editorial) {
        if (editorial.length() == 0 || editorial.equals("")) {
            editorial = "No hay datos";
        }
        return editorial;
    }

    protected String verificarGeneroLiterario(String generoLiterario) {
        if (generoLiterario == null || generoLiterario.isEmpty()) {
            return "No hay datos";
        } else {
            String regex = ".*[\\[\\]\"].*"; // Para comprobar si tiene corchetes o comillas dobles
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(generoLiterario);

            if (matcher.matches()) {
                // Eliminar corchetes y comillas dobles, junto con espacios en blanco al inicio y al final
                generoLiterario = generoLiterario.replaceAll("[\\[\\]\"]", "").trim();
            }
        }
        System.out.println(generoLiterario);
        return generoLiterario;
    }

    protected String formateoFecha(Date fecha) {

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechaLectura = formatoFecha.format(fecha);

        return fechaLectura;
    }
}

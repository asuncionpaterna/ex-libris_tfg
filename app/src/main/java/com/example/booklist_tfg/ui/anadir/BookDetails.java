package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.ui.Utils.formateoAutoria;
import static com.example.booklist_tfg.ui.Utils.formateoFechaString;
import static com.example.booklist_tfg.ui.Utils.showDatePicker;
import static com.example.booklist_tfg.ui.Utils.verificarDatos;
import static com.example.booklist_tfg.ui.Utils.verificarGeneroLiterario;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String tituloDetails, subTituloDetails, editorialDetails, fechaPublicacionDetails, sinopsisDetails, portadaDetails, generoLiterarioDetails, previewLink, infoLink, buyLink;
    Libro libro;
    int numPaginasDetails;
    private ArrayList<String> autoriaListDetails;

    TextView tituloTV, autoriaTV, editorialTV, descTV, paginasTV, anioPublicacionTV, fechaLecturaTV, generoLiterarioTV;

    ImageButton anadirBtn, fechaBtn;

    CheckBox favoritoCB, esPapelCB;
    private ImageView bookIV;
    boolean favorito, esPapel;

    public Date fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Inicializando las vistas
        tituloTV = findViewById(R.id.idTVTitle);
        autoriaTV = findViewById(R.id.idTVAutoriaDetails);
        editorialTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        paginasTV = findViewById(R.id.idTVNoOfPages);
        anioPublicacionTV = findViewById(R.id.idTVPublishDate);
        anadirBtn = findViewById(R.id.idBtnAnadir);
        bookIV = findViewById(R.id.idIVPortadaList);
        fechaBtn = findViewById(R.id.idBtnFecha);
        fechaLecturaTV = findViewById(R.id.idTVFechaLectura);
        generoLiterarioTV = findViewById(R.id.idTVgeneroLiterarioDetails);
        favoritoCB = findViewById(R.id.idCBFavoritoDetails);
        esPapelCB = findViewById(R.id.idCBesPapelDetails);


        // Se recogen los datos que se pasan con la clase BookAdapter
        libro = (Libro) getIntent().getSerializableExtra("libro");
        tituloDetails = libro.getTitulo();
        autoriaListDetails = libro.getNombreAutoria();
        editorialDetails = libro.getEditorial();
        fechaPublicacionDetails = libro.getAnioPublicacion();
        numPaginasDetails = libro.getPaginas();
        generoLiterarioDetails = libro.getGenero();
        subTituloDetails = getIntent().getStringExtra("subtitle");
        sinopsisDetails = getIntent().getStringExtra("description");
        portadaDetails = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");

        // after getting the data we are setting
        // that data to our text views and image view.
        tituloTV.setText(tituloDetails);
        autoriaTV.setText(formateoAutoria(autoriaListDetails));
        generoLiterarioTV.setText(verificarGeneroLiterario(generoLiterarioDetails));
        editorialTV.setText("Editorial: " + verificarDatos(editorialDetails));
        anioPublicacionTV.setText(formateoFechaString(fechaPublicacionDetails));
        descTV.setText(verificarDatos(sinopsisDetails));
        paginasTV.setText(""+numPaginasDetails);

        Picasso.get().load(portadaDetails).into(bookIV);


        // Funcionalidad botón AÑADIR
        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorito = favoritoCB.isChecked();
                esPapel = esPapelCB.isChecked();
                libro.setFechaLectura(fecha);
                libro.setFavorito(favorito);
                libro.setEsPapel(esPapel);
                new GuardarLibroAsinc(libro, getBaseContext()).execute();
                finish();
            }
        });

        fechaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker(BookDetails.this, fechaLecturaTV, fecha);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fecha = new Date();
        fechaLecturaTV.setText(sdf.format(fecha));
    }

    //Para mostrar el calendario de fecha de lectura

}

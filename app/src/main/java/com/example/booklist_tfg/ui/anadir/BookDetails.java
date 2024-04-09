package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.ui.Utils.formateoAutoria;
import static com.example.booklist_tfg.ui.Utils.formateoFechaString;
import static com.example.booklist_tfg.ui.Utils.showDatePicker;
import static com.example.booklist_tfg.ui.Utils.verificarDatos;
import static com.example.booklist_tfg.ui.Utils.verificarGeneroLiterario;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ui.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BookDetails extends AppCompatActivity {

    // creating variables for strings,text view, image views and button.
    String tituloBD, editorialBD, fechaPublicacionBD, descripcionBD, portadaBD, generoLiterarioBD;
    Libro libro;
    int paginasBD;
    private ArrayList<String> autoriaListBD;

    TextView tituloTV, autoriaTV, editorialTV, descripcionTV, paginasTV, anioPublicacionTV, fechaLecturaTV, generoLiterarioTV;

    ImageButton anadirBtn, fechaBtn;

    CheckBox favoritoCB, esPapelCB;
    private ImageView imagenLibroIV;
    boolean favorito, esPapel;

    public Date fechaBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Inicializando las vistas
        tituloTV = findViewById(R.id.idTVTituloBookDetails);
        autoriaTV = findViewById(R.id.idTVAutoriaBookDetails);
        editorialTV = findViewById(R.id.idTVEditorialBookDetails);
        descripcionTV = findViewById(R.id.idTVDescripcionBookDetails);
        paginasTV = findViewById(R.id.idTVPaginasBookDetails);
        anioPublicacionTV = findViewById(R.id.idTVFechaPublicacionBookDetails);
        anadirBtn = findViewById(R.id.idBtnAnadir);
        imagenLibroIV = findViewById(R.id.idIVPortadaList);
        fechaBtn = findViewById(R.id.idBtnFechaBookDetails);
        fechaLecturaTV = findViewById(R.id.idTVFechaLecturaBookDetails);
        generoLiterarioTV = findViewById(R.id.idTVgeneroLiterarioBookDetails);
        favoritoCB = findViewById(R.id.idCBFavoritoBookDetails);
        esPapelCB = findViewById(R.id.idCBesPapelBookDetails);


        // Se recogen los datos que se pasan con la clase BookAdapter
        libro = (Libro) getIntent().getSerializableExtra("libro");
        tituloBD = libro.getTitulo();
        autoriaListBD = libro.getNombreAutoria();
        editorialBD = libro.getEditorial();
        fechaPublicacionBD = libro.getAnioPublicacion();
        paginasBD = libro.getPaginas();
        generoLiterarioBD = libro.getGenero();
        descripcionBD = getIntent().getStringExtra("description");
        portadaBD = getIntent().getStringExtra("thumbnail");

        // after getting the data we are setting
        // that data to our text views and image view.
        tituloTV.setText(tituloBD);
        autoriaTV.setText(formateoAutoria(autoriaListBD));
        generoLiterarioTV.setText(verificarGeneroLiterario(generoLiterarioBD));
        editorialTV.setText("Editorial: " + verificarDatos(editorialBD));
        anioPublicacionTV.setText(formateoFechaString(fechaPublicacionBD));
        descripcionTV.setText(verificarDatos(descripcionBD));
        paginasTV.setText("" + paginasBD);

        Picasso.get().load(portadaBD).into(imagenLibroIV);


        // Funcionalidad botón AÑADIR
        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorito = favoritoCB.isChecked();
                esPapel = esPapelCB.isChecked();
                libro.setFechaLectura(fechaBD);
                libro.setFavorito(favorito);
                libro.setEsPapel(esPapel);
                new GuardarLibroAsinc(libro, getBaseContext()).execute();
                finish();
            }
        });

        fechaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar a showDatePicker pasando un OnDateSelectedListener
                showDatePicker(BookDetails.this, fechaLecturaTV, fechaBD, new Utils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date selectedDate) {
                        // Manejar la fecha seleccionada aquí
                        fechaBD = selectedDate;
                    }
                });
            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaBD = new Date();
        fechaLecturaTV.setText(sdf.format(fechaBD));


        esPapelCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(esPapelCB.getText());
                if (esPapelCB.isChecked()) {
                    esPapelCB.setText("Papel");
                } else {
                    esPapelCB.setText("Digital");
                }
            }
        });
    }


}

package com.example.booklist_tfg.ui.listado;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.R;

import java.util.Date;

public class BookDetailsList extends AppCompatActivity {

    private ImageView portadaIV;
    TextView tituloTV, autoriaTV, editorialTV, generoLiterarioTV, fechaLecturaTV;
    Switch favoritoSwitch, esPapelSwitch;
    private Date fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_list);

        tituloTV = findViewById(R.id.idTVTitulo);
        autoriaTV = findViewById(R.id.idTVAutoria);
        editorialTV = findViewById(R.id.idTVEditorial);
        generoLiterarioTV = findViewById(R.id.idTVGenero);
        fechaLecturaTV = findViewById(R.id.idTVFechaLectura);

        favoritoSwitch = findViewById(R.id.idSwitchFavorito);
        esPapelSwitch = findViewById(R.id.idSwitchEsPapel);
    }
}

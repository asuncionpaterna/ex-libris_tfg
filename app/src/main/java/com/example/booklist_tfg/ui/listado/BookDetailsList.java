package com.example.booklist_tfg.ui.listado;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.ui.Utils.formateoAutoria;
import static com.example.booklist_tfg.ui.Utils.formateoFecha;
import static com.example.booklist_tfg.ui.Utils.verificarDatos;
import static com.example.booklist_tfg.ui.Utils.verificarGeneroLiterario;
import static com.example.booklist_tfg.ui.home.HomeFragment.mostrarLibros;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.ui.anadir.GuardarLibroAsinc;
import com.example.booklist_tfg.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;

public class BookDetailsList extends AppCompatActivity {

    String titulo, editorial, generoLiterario, portada, fechaLectura;
    private ArrayList<String> autoriaList;
    boolean favorito, esPapel;
    Libro libro;
    private Date fecha;

    private ImageView portadaIV;
    TextView tituloTV, autoriaTV, editorialTV, generoLiterarioTV, fechaLecturaTV;

    CheckBox favoritoCB, esPapelCB;
    Button eliminarBtn, modificarBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details_list);

        LibroDAO libroDAO = database.libroDAO();

        //Inicializando los elementos
        tituloTV = findViewById(R.id.idTVTitulo);
        autoriaTV = findViewById(R.id.idTVAutoria);
        editorialTV = findViewById(R.id.idTVEditorial);
        generoLiterarioTV = findViewById(R.id.idTVGenero);
        fechaLecturaTV = findViewById(R.id.idTVFechaLectura);

        portadaIV = findViewById(R.id.idIVPortada);
        favoritoCB = findViewById(R.id.idCBFavorito);
        esPapelCB = findViewById(R.id.idCBesPapel);

        eliminarBtn = findViewById(R.id.idBtnEliminar);
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
        Context mcontext = this;

        fechaLectura = formateoFecha(fecha);
        tituloTV.setText("Título: " + titulo);
        autoriaTV.setText(("Autoria: ") + formateoAutoria(autoriaList));
        editorialTV.setText("Editorial: " + verificarDatos(editorial));
        generoLiterarioTV.setText("Género literario: " + verificarGeneroLiterario(generoLiterario));
        fechaLecturaTV.setText(fechaLectura);
        esPapelCB.setChecked(esPapel);
        favoritoCB.setChecked(favorito);

        Picasso.get().load(portada).into(portadaIV);

        eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se ejecuta la eliminación del libro en un hilo secundario
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Mediante una estructura de control Try&Catch se elimina el libro utilizando el DAO
                            libroDAO.delete(libro);
                            new HomeFragment.RecogerLibrosDB(mcontext).execute();

                            //Tras eliminar el libro se finaliza la actividad
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mostrarLibros(mcontext);
                                    finish();
                                }
                            });
                        } catch (Exception exception) {
                            //Si ocurre alfuna excepción se notifica por consola y por la aplicación
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}

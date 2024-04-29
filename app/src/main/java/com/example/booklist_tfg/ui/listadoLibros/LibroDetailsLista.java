package com.example.booklist_tfg.ui.listadoLibros;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.ui.Inicio.InicioFragment.porcentajeLectura;
import static com.example.booklist_tfg.ui.listadoLecturas.ListadoLecturas.mostrarLibrosLecturas;
import static com.example.booklist_tfg.utils.Utils.establecerTema;
import static com.example.booklist_tfg.utils.Utils.formateoAutoria;
import static com.example.booklist_tfg.utils.Utils.formateoFecha;
import static com.example.booklist_tfg.utils.Utils.showDatePicker;
import static com.example.booklist_tfg.utils.Utils.verificarDatos;
import static com.example.booklist_tfg.utils.Utils.verificarGeneroLiterario;
import static com.example.booklist_tfg.ui.Inicio.InicioFragment.mostrarLibrosInicio;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.utils.Utils;
import com.example.booklist_tfg.ui.Inicio.InicioFragment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class LibroDetailsLista extends AppCompatActivity {

    String titulo, editorial, generoLiterario, portada, fechaLectura;
    private ArrayList<String> autoriaList;
    boolean favorito, esPapel;
    Libro libro;
    private Date fecha;

    private ImageView portadaIV;
    TextView tituloTV, autoriaTV, editorialTV, generoLiterarioTV, fechaLecturaTV;
    FrameLayout libroDetallesFL;
    CheckBox favoritoCB, esPapelCB;
    Button eliminarBtn, actualizarBtn;
    Date[] fechas = new Date[1];
    ImageButton fechaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_detalles_lista);


        //Inicializando los elementos

        libroDetallesFL = findViewById(R.id.idFLLibroDetallesLista);
        tituloTV = findViewById(R.id.idTVTitulo);
        autoriaTV = findViewById(R.id.idTVAutoria);
        editorialTV = findViewById(R.id.idTVEditorial);
        generoLiterarioTV = findViewById(R.id.idTVGenero);
        fechaLecturaTV = findViewById(R.id.idTVFechaLecturaLibroDetalles);

        portadaIV = findViewById(R.id.idIVPortada);
        favoritoCB = findViewById(R.id.idCBFavorito);
        esPapelCB = findViewById(R.id.idCBesPapel);

        eliminarBtn = findViewById(R.id.idBtnEliminar);
        actualizarBtn = findViewById(R.id.idBtnModificar);
        fechaBtn = findViewById(R.id.idBtnFechaDetails);
        fechas[0] = fecha;
        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        establecerTema(modoOscuro, libroDetallesFL);
        //Almacenando los datos en las variables y se crea el contexto
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
        autoriaTV.setText(formateoAutoria(autoriaList));
        editorialTV.setText("Editorial: " + verificarDatos(editorial));
        generoLiterarioTV.setText("Género literario: " + verificarGeneroLiterario(generoLiterario));
        fechaLecturaTV.setText(fechaLectura);
        esPapelCB.setChecked(esPapel);
        actualizarTextoFormato(esPapelCB);
        favoritoCB.setChecked(favorito);

        Picasso.get().load(portada).into(portadaIV);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaLecturaTV.setText(sdf.format(fecha));


        //Funcionalidad del botón Eliminar
        eliminarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se ejecuta la eliminación del libro en un hilo secundario
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LibroDAO libroDAO = database.libroDAO();

                            //Mediante una estructura de control Try&Catch se elimina el libro utilizando el DAO
                            libroDAO.delete(libro);
                            new InicioFragment.RecogerLibrosDB(mcontext).execute();

                            //Tras eliminar el libro se finaliza la actividad
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mostrarLibrosInicio(mcontext);
                                    mostrarLibrosLecturas(mcontext);
                                    porcentajeLectura();
                                    finish();
                                }
                            });
                        } catch (Exception exception) {
                            //Si ocurre alguna excepción se notifica por consola
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        actualizarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                libro.setFechaLectura(fecha);
                libro.setFavorito(favoritoCB.isChecked());
                libro.setEsPapel(esPapelCB.isChecked());
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LibroDAO libroDAO = database.libroDAO();
                            libroDAO.update(libro);
                            new InicioFragment.RecogerLibrosDB(mcontext).execute();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mostrarLibrosInicio(mcontext);
                                    mostrarLibrosLecturas((mcontext));
                                    finish();
                                }
                            });
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
            }
        });

        fechaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamar a showDatePicker pasando un OnDateSelectedListener
                showDatePicker(LibroDetailsLista.this, fechaLecturaTV, fecha, new Utils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date selectedDate) {
                        // Manejar la fecha seleccionada aquí
                        fecha = selectedDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        fechaLecturaTV.setText(sdf.format(selectedDate));
                    }
                });
            }
        });

        esPapelCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esPapelCB.isChecked()) {
                    esPapelCB.setText("Papel");
                } else {
                    esPapelCB.setText("Digital");
                }
            }
        });
    }
    public void actualizarTextoFormato(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setText("Papel");
        } else {
            checkBox.setText("Digital");
        }
    }
}


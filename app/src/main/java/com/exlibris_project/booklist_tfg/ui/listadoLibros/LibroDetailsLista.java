package com.exlibris_project.booklist_tfg.ui.listadoLibros;

import static com.exlibris_project.booklist_tfg.MainActivity.database;
import static com.exlibris_project.booklist_tfg.MainActivity.inicio;
import static com.exlibris_project.booklist_tfg.ui.Inicio.InicioFragment.porcentajeLectura;
import static com.exlibris_project.booklist_tfg.ui.Inicio.InicioFragment.mostrarLibrosInicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.ddbb.LibroDAO;
import com.exlibris_project.booklist_tfg.utils.Utils;
import com.exlibris_project.booklist_tfg.ui.Inicio.InicioFragment;
import com.exlibris_project.booklist_tfg.ui.listadoLecturas.ListadoLecturasFragment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class LibroDetailsLista extends AppCompatActivity {

    String titulo, editorial, generoLiterario, portada, fechaLectura, descripcion;
    private ArrayList<String> autoriaList;
    boolean favorito, esPapel;
    Libro libro;
    private Date fecha;
    Context mcontext = this;

    private ImageView portadaIV;
    TextView tituloTV, autoriaTV, editorialTV, generoLiterarioTV, fechaLecturaTV, descripcionTV;
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
        descripcionTV = findViewById(R.id.idTVDescripcion);

        portadaIV = findViewById(R.id.idIVPortada);
        favoritoCB = findViewById(R.id.idCBFavorito);
        esPapelCB = findViewById(R.id.idCBesPapel);

        eliminarBtn = findViewById(R.id.idBtnEliminar);
        actualizarBtn = findViewById(R.id.idBtnModificar);
        fechaBtn = findViewById(R.id.idBtnFechaDetails);
        fechas[0] = fecha;
        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Utils.establecerTema(modoOscuro, libroDetallesFL);
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
        descripcion = libro.getDescripcion();

        final boolean[] desc = {false};
        descripcionTV.setText(mcontext.getString(R.string.label_descripcion) + " [...]");

        if (descripcion != null && !descripcion.isEmpty() && descripcion.length() > 0) {

            descripcionTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (desc[0]) {
                        desc[0] = false;
                        descripcionTV.setText(mcontext.getString(R.string.label_descripcion) + " [...]");

                    } else {
                        desc[0] = true;
                        descripcionTV.setText(mcontext.getString(R.string.label_descripcion) + "\n" + descripcion);
                    }
                }
            });

        } else {
            descripcionTV.setText(mcontext.getString(R.string.label_descripcion) + " " + mcontext.getString(R.string.error_no_datos));
        }


        fechaLectura = Utils.formateoFecha(fecha);
        tituloTV.setText(getString(R.string.label_titulo) + titulo);
        autoriaTV.setText(Utils.formateoAutoria(autoriaList));
        editorialTV.setText(getString(R.string.label_editorial) + " " + Utils.verificarDatos(editorial, getBaseContext()));
        generoLiterarioTV.setText(getString(R.string.label_genero_literario) + " " + Utils.verificarGeneroLiterario(generoLiterario, getBaseContext()));
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
                mostrarDialogoBorrar();
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
                                    if (inicio) {
                                        mostrarLibrosInicio(mcontext);
                                    } else {
                                        ListadoLecturasFragment.mostrarLibrosLecturas((mcontext));
                                    }
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
                Utils.showDatePicker(LibroDetailsLista.this, fechaLecturaTV, fecha, new Utils.OnDateSelectedListener() {
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
                    esPapelCB.setText(getString(R.string.label_papel));
                } else {
                    esPapelCB.setText(getString(R.string.label_digital));
                }
            }
        });
    }

    public void mostrarDialogoBorrar() {
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //        // Crea un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

        // Establece el título del diálogo
        builder.setTitle(mcontext.getString(R.string.dialogo_titulo) + " " + libro.getTitulo() + "?");

        // Crea un ImageView programáticamente y establece la imagen
        ImageView imageView = new ImageView(mcontext);
        imageView.setPadding(16, 16, 16, 16); // Ajusta el padding si es necesario

        Picasso.get().load(libro.getPortada()).into(imageView);

        // Agrega el ImageView al diálogo
        builder.setView(imageView);

        //Se establece el color del texto de los botones mediante una estructura if/else
        if(modoOscuro ==32){
            SpannableString positiveText = new SpannableString(mcontext.getString(R.string.dialogo_borrar));
            positiveText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, positiveText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString negativeText = new SpannableString(mcontext.getString(R.string.dialogo_cancelar));
            negativeText.setSpan(new ForegroundColorSpan(Color.WHITE), 0, negativeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        SpannableString positiveText = new SpannableString(mcontext.getString(R.string.dialogo_borrar));
        positiveText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, positiveText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString negativeText = new SpannableString(mcontext.getString(R.string.dialogo_cancelar));
        negativeText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, negativeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarLibro();
                dialog.dismiss();
            }
        });

        // Establece el botón de cancelar
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acción cuando se pulsa "Cancelar"
                dialog.dismiss();
            }
        });

        // Crea y muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void borrarLibro() {
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
                            if (inicio) {
                                mostrarLibrosInicio(mcontext);
                            } else {
                                ListadoLecturasFragment.mostrarLibrosLecturas(mcontext);
                            }
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

    public void actualizarTextoFormato(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setText(getString(R.string.label_papel));
        } else {
            checkBox.setText(getString(R.string.label_digital));
        }
    }
}


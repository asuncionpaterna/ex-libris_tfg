package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.utils.Utils.establecerTema;
import static com.example.booklist_tfg.utils.Utils.formateoAutoria;
import static com.example.booklist_tfg.utils.Utils.showDatePicker;
import static com.example.booklist_tfg.utils.Utils.verificarDatos;
import static com.example.booklist_tfg.utils.Utils.verificarGeneroLiterario;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

    //Clase para dar funcionalidad y mostrar los detalles de un libro seleccionado
public class LibroDetalles extends AppCompatActivity {
    // Se crean las variables para almacenar información del libro e inicializar los elementos de la pantalla
    String tituloLD, editorialLD, fechaPublicacionLD, descripcionLD, portadaLD, generoLiterarioLD;
    Libro libro;
    int paginasLD;
    private ArrayList<String> autoriaListLD;
    FrameLayout libroDetallesFL;
    TextView tituloTV, autoriaTV, editorialTV, descripcionTV, paginasTV, anioPublicacionTV, fechaLecturaTV, generoLiterarioTV;

    ImageButton anadirBtn, fechaBtn;

    CheckBox favoritoCB, esPapelCB;
    private ImageView imagenLibroIV;
    boolean favorito, esPapel;
    public Date fechaBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_detalles);

        // Se inicializan los elementos de la pantalla
        libroDetallesFL = findViewById(R.id.idFLLibroDetalles);
        tituloTV = findViewById(R.id.idTVTituloLibroDetalles);
        autoriaTV = findViewById(R.id.idTVAutoriaLibroDetalles);
        editorialTV = findViewById(R.id.idTVEditorialLibroDetalles);
        descripcionTV = findViewById(R.id.idTVDescripcionLibroDetalles);
        paginasTV = findViewById(R.id.idTVPaginasLibroDetalles);
        anioPublicacionTV = findViewById(R.id.idTVFechaPublicacionLibroDetalles);
        anadirBtn = findViewById(R.id.idBtnAnadir);
        imagenLibroIV = findViewById(R.id.idIVPortadaList);
        fechaBtn = findViewById(R.id.idBtnFechaLibroDetalles);
        fechaLecturaTV = findViewById(R.id.idTVFechaLecturaLibroDetalles);
        generoLiterarioTV = findViewById(R.id.idTVgeneroLiterarioLibroDetalles);
        favoritoCB = findViewById(R.id.idCBFavoritoLibroDetalles);
        esPapelCB = findViewById(R.id.idCBesPapelLibroDetalles);

        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        establecerTema(modoOscuro, libroDetallesFL);

        // Se recogen los datos que se pasan con la clase BookAdapter, del Intent
        libro = (Libro) getIntent().getSerializableExtra("libro");
        tituloLD = libro.getTitulo();
        autoriaListLD = libro.getNombreAutoria();
        editorialLD = libro.getEditorial();
        fechaPublicacionLD = libro.getFechaPublicacion();
        paginasLD = libro.getPaginas();
        generoLiterarioLD = libro.getGenero();
        descripcionLD = libro.getDescripcion();
        portadaLD = libro.getPortada();

        // Se establecen los datos
        tituloTV.setText(tituloLD);
        autoriaTV.setText(formateoAutoria(autoriaListLD));
        generoLiterarioTV.setText(verificarGeneroLiterario(generoLiterarioLD, getBaseContext()));
        editorialTV.setText("Editorial: " + verificarDatos(editorialLD, getBaseContext()));
        anioPublicacionTV.setText(fechaPublicacionLD);
        descripcionTV.setText(verificarDatos(descripcionLD, getBaseContext()));
        paginasTV.setText("" + paginasLD);

        //Se carga la imagen de la portada usando Picasso.
        Picasso.get().load(portadaLD).into(imagenLibroIV);

        // Se configura la funcionalidad del botón Añadir
        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtiene el estado de los CheckBox y establece los valores en el Libro
                favorito = favoritoCB.isChecked();
                esPapel = esPapelCB.isChecked();
                libro.setFechaLectura(fechaBD);
                libro.setFavorito(favorito);
                libro.setEsPapel(esPapel);
                //Se guarda el libro empleando el método asíncrono GuardarLibroAsinc
                new GuardarLibroAsinc(libro, getBaseContext()).execute();
                finish();
            }
        });
        // Se configura la funcionalidad del botón de fecha de lectura
        fechaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se muestra el selector de fecha y obtener la fecha seleccionar
                showDatePicker(LibroDetalles.this, fechaLecturaTV, fechaBD, new Utils.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date selectedDate) {
                        // Se establece la fecha seleccionada como la fecha de lectura
                        fechaBD = selectedDate;
                    }
                });
            }
        });

        // Se configura la fecha de lectura por defecto al día actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fechaBD = new Date();
        fechaLecturaTV.setText(sdf.format(fechaBD));

        //Se configura la funcionalidad del checkbox del formato del libro
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
}

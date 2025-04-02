package com.exlibris_project.booklist_tfg.ui.anadir;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.Model.Libro;
import com.exlibris_project.booklist_tfg.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class AnadirManual extends AppCompatActivity {

    Libro libro;

    FrameLayout libroDetallesManualFL;
    EditText tituloET, autoriaET, editorialET, descripcionET, paginasET, anioPublicacionET;
    TextView fechaLecturaInicioTV, fechaLecturaTV;
    CheckBox favoritoCB, esPapelCB;
    Button anadirBtn;
    ImageButton fechaInicioBtn, fechaBtn;
    Spinner generoLiterarioSP;

    boolean favorito, esPapel;
    private Date fechaLectura, fechaInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_manual);

        // Se inicializan los elementos de la pantalla
        libroDetallesManualFL = findViewById(R.id.idFLLibroDetallesManual);

        tituloET = findViewById(R.id.idTVTituloLibroDetalles);
        autoriaET = findViewById(R.id.idTVAutoriaLibroDetalles);
        editorialET = findViewById(R.id.idTVEditorialLibroDetalles);
        descripcionET = findViewById(R.id.idTVDescripcionLibroDetalles);
        paginasET = findViewById(R.id.idTVPaginasLibroDetalles);
        anioPublicacionET = findViewById(R.id.idTVFechaPublicacionLibroDetalles);
        fechaLecturaInicioTV = findViewById(R.id.idTVFechaLecturaLibroDetallesInicio);
        fechaLecturaTV = findViewById(R.id.idTVFechaLecturaLibroDetalles);
        generoLiterarioSP = findViewById(R.id.idSPGeneroLiterario);
        favoritoCB = findViewById(R.id.idCBFavoritoLibroDetalles);
        esPapelCB = findViewById(R.id.idCBesPapelLibroDetalles);
        anadirBtn = findViewById(R.id.idBtnAnadir);
        fechaInicioBtn = findViewById(R.id.idBtnFechaLibroDetallesInicio);
        fechaBtn = findViewById(R.id.idBtnFechaLibroDetalles);

        configurarSpinnerGenero();

        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Utils.establecerTema(modoOscuro, libroDetallesManualFL);

        fechaInicioBtn.setOnClickListener(v -> Utils.showDatePicker(this, fechaLecturaInicioTV, fechaInicio, new Utils.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date selectedDate) {
                fechaInicio = selectedDate;
            }
        }));

        fechaBtn.setOnClickListener(v -> Utils.showDatePicker(this, fechaLecturaTV, fechaLectura, new Utils.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date selectedDate) {
                fechaLectura = selectedDate;
            }
        }));
        anadirBtn.setOnClickListener(v -> guardarLibro());

    }

    private void configurarSpinnerGenero() {
        String[] generos = {"Novela", "Ensayo", "Feminismo", "Fantasía", "Ciencia Ficción", "Historia", "Biografía", "Poesía", "Novela Gráfica", "Viñetas", "Cómic", "Humor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        generoLiterarioSP.setAdapter(adapter);
    }

    private void guardarLibro() {
        // Obtener valores introducidos por el usuario
        String tituloLD = tituloET.getText().toString().trim();
        String autoriaListLD = autoriaET.getText().toString().trim();
        String editorialLD = editorialET.getText().toString().trim();
        String descripcionLD = descripcionET.getText().toString().trim();
        String generoLiterarioLD = generoLiterarioSP.getSelectedItem().toString();
        String fechaPublicacionLD = anioPublicacionET.getText().toString().trim();
        int paginas = paginasET.getText().toString().isEmpty() ? 0 : Integer.parseInt(paginasET.getText().toString());

        // Checkboxes
        boolean esFavorito = favoritoCB.isChecked();
        boolean esPapel = esPapelCB.isChecked();

        // Crear objeto Libro
         libro = new Libro(
                 tituloLD,
                new ArrayList<String>() {{add(autoriaListLD); }},
                 editorialLD,
                 generoLiterarioLD,
                 descripcionLD,
                 fechaPublicacionLD,
                paginas,
                ""
        );
        new GuardarLibroAsinc(libro, getBaseContext()).execute();
        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.anadir_exito), Toast.LENGTH_LONG).show();
        finish();

    }
}



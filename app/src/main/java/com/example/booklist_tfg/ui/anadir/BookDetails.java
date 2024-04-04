package com.example.booklist_tfg.ui.anadir;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    String title, subtitle, publisher, publishedDate, description, thumbnail, previewLink, infoLink, buyLink;
    Libro libro;
    int pageCount;
    private ArrayList<String> authors;

    TextView titleTV, subtitleTV, publisherTV, descTV, pageTV, publishDateTV, idTVFechaLectura;
    Button previewBtn, anadirBtn, fechaBtn;

    Switch favoritoSwitch, esPapelSwitch;
    private ImageView bookIV;
    boolean favorito, esPapel;

    private Date fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // initializing our views..
        titleTV = findViewById(R.id.idTVTitle);
        subtitleTV = findViewById(R.id.idTVSubTitle);
        publisherTV = findViewById(R.id.idTVpublisher);
        descTV = findViewById(R.id.idTVDescription);
        pageTV = findViewById(R.id.idTVNoOfPages);
        publishDateTV = findViewById(R.id.idTVPublishDate);
        previewBtn = findViewById(R.id.idBtnPreview);
        anadirBtn = findViewById(R.id.idBtnBuy);
        bookIV = findViewById(R.id.idIVPortadaList);
        fechaBtn = findViewById(R.id.idBtnFecha);
        idTVFechaLectura = findViewById(R.id.idTVFechaLectura);
        favoritoSwitch = findViewById(R.id.idSwitchFavorito);
        esPapelSwitch = findViewById(R.id.idSwitchEsPapel);


        // getting the data which we have passed from our adapter class.
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
        libro = (Libro) getIntent().getSerializableExtra("libro");


        // after getting the data we are setting
        // that data to our text views and image view.
        titleTV.setText("Título: "+title);
        subtitleTV.setText(subtitle);
        publisherTV.setText(publisher);
        publishDateTV.setText("Publicado el : " + publishedDate);
        descTV.setText(description);
        pageTV.setText("Nº de Páginas : " + pageCount);


        Picasso.get().load(thumbnail).into(bookIV);


        // adding on click listener for our preview button.
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(BookDetails.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening
                // that link via an intent.
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        // Funcionalidad botón AÑADIR
        anadirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorito = favoritoSwitch.isChecked();
                esPapel = esPapelSwitch.isChecked();
                libro.setFechaLectura(fecha);
                libro.setFavorito(favorito);
                libro.setEsPapel(esPapel);
                System.out.println(libro.toString());
                new GuardarLibroAsinc(libro, getBaseContext() ).execute();
            }
        });

        fechaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(BookDetails.this);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fecha = new Date();
        idTVFechaLectura.setText(sdf.format(fecha));


    }

    //Para mostrar el calendario de fecha de lectura
    private void showDatePicker(Context context) {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog y configurarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Se establece la fecha seleccionada en el TextView
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);
                        fecha = selectedDate.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(fecha);
                        idTVFechaLectura.setText(formattedDate);
                    }
                }, year, month, dayOfMonth);
        // Mostrar el diálogo
        datePickerDialog.show();
    }
}

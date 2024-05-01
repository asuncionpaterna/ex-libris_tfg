package com.example.booklist_tfg.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ui.listadoLecturas.ListadoLecturasFragment;


//Clase para dar funcionalidad al diálogo búsqueda avanzada del menú
public class DialogoBusquedaAvanzada {

    private Context context;

    //Constructor
    public DialogoBusquedaAvanzada(Context context) {
        this.context = context;
    }

    //Se crean las variables de almacenamiento y los elementos de la pantalla
    private EditText autoriaET, generoET, editorialET;
    private ImageButton buscarAutoriaBtn, buscarGeneroBtn, buscarEditorialBtn;

    String autoria, genero, editorial;

    //Se implementa la funcionalidad de mostrar el diálogo
    public void showDialog() {
        //Se infla el dialogo, relacionandolo con su layout y estableciendo el diseño
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_busquedaavanzada, null);
        builder.setView(view);
        // Se crea el diálogo con el diseño inflado
        AlertDialog dialog = builder.create();

        // Se inicializan los elementos del diálogo
        autoriaET = view.findViewById(R.id.idETAutoria);
        buscarAutoriaBtn = view.findViewById(R.id.idBtnBuscarAutoria);
        generoET = view.findViewById(R.id.idETGenero);
        buscarGeneroBtn = view.findViewById(R.id.idBtnBuscarGenero);
        editorialET = view.findViewById(R.id.idETEditorial);
        buscarEditorialBtn = view.findViewById(R.id.idBtnBuscarEditorial);

        // Se configura la funcionalidad del botón buscar por autoria
        buscarAutoriaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoria = autoriaET.getText().toString();
                ///Mediante una estructura de control if/else se valida si el campo de texto de autoria tiene texto
                if (autoria.isEmpty()) {
                    autoriaET.setError(context.getString(R.string.error_campo_vacio_busqueda));
                } else {
                    new ListadoLecturasFragment.RecogerLibrosAutoriaDB(context, autoria).execute();
                    //Se cierra el diálogo
                    dialog.dismiss();
                }
            }
        });
        // Se configura la funcionalidad del botón buscar por género literario
        buscarGeneroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = generoET.getText().toString();
                ///Mediante una estructura de control if/else se valida si el campo de texto de género literario tiene texto
                if (genero.isEmpty()) {
                    generoET.setError(context.getString(R.string.error_campo_vacio_busqueda));
                } else {
                    new ListadoLecturasFragment.RecogerLibrosGeneroDB(context, genero).execute();
                    //Se cierra el diálogo
                    dialog.dismiss();
                }
            }
        });
        // Se configura la funcionalidad del botón buscar por editorial
        buscarEditorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorial = editorialET.getText().toString();
                ///Mediante una estructura de control if/else se valida si el campo de texto de editorial tiene texto
                if (editorial.isEmpty()) {
                    editorialET.setError(context.getString(R.string.error_campo_vacio_busqueda));
                } else {
                    new ListadoLecturasFragment.RecogerLibrosEditorialDB(context, editorial).execute();
                    //Se cierra el diálogo
                    dialog.dismiss();
                }
            }
        });
        //Se muestra el diálogo
        dialog.show();
    }
}

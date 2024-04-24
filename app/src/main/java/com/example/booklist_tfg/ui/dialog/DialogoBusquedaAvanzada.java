package com.example.booklist_tfg.ui.dialog;

import static com.example.booklist_tfg.MainActivity.database;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ui.listadoLecturas.ListadoLecturas;

import java.util.List;

public class DialogoBusquedaAvanzada {

    private Context context;

    public DialogoBusquedaAvanzada(Context context) {
        this.context = context;
    }

    private EditText autoriaET, generoET, editorialET;
    private ImageButton buscarAutoriaBtn, buscarGeneroBtn, buscarEditorialBtn;

    String autoria, genero, editorial;

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_busquedaavanzada, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        autoriaET = view.findViewById(R.id.idETAutoria);
        buscarAutoriaBtn = view.findViewById(R.id.idBtnBuscarAutoria);
        generoET = view.findViewById(R.id.idETGenero);
        buscarGeneroBtn = view.findViewById(R.id.idBtnBuscarGenero);
        editorialET = view.findViewById(R.id.idETEditorial);
        buscarEditorialBtn = view.findViewById(R.id.idBtnBuscarEditorial);
        buscarAutoriaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoria = autoriaET.getText().toString();
                if (autoria.isEmpty()) {
                    autoriaET.setError("Este campo no puede estar vacío");
                } else {
                     new ListadoLecturas.RecogerLibrosAutoriaDB(context,autoria).execute();

                    dialog.dismiss();
                }
            }
        });

        buscarGeneroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = generoET.getText().toString();
                if (genero.isEmpty()) {
                    generoET.setError("Este campo no puede estar vacío");
                } else {
                    new ListadoLecturas.RecogerLibrosGeneroDB(context,genero).execute();

                    dialog.dismiss();
                }
            }
        });

        buscarEditorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editorial = editorialET.getText().toString();
                if (editorial.isEmpty()) {
                    editorialET.setError("Este campo no puede estar vacío");
                } else {
                    new ListadoLecturas.RecogerLibrosEditorialDB(context,editorial).execute();

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
//CONTINUAR AQUI
}

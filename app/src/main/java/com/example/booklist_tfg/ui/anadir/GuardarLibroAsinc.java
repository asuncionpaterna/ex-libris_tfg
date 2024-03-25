package com.example.booklist_tfg.ui.anadir;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.booklist_tfg.MainActivity;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.ddbb.LibroDAO;

public class GuardarLibroAsinc extends AsyncTask<Void, Void, Void> {

    Libro libro;
    Context context;

    public GuardarLibroAsinc(Libro libro, Context context) {
        this.libro = libro;
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        LibroDAO libroDAO = MainActivity.database.libroDAO();

        libroDAO.insert(libro);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        Toast.makeText(context, "Se ha añadido libro", Toast.LENGTH_LONG);
    }
}

package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.ui.Inicio.InicioFragment.porcentajeLectura;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.booklist_tfg.MainActivity;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.R;

    //Clase para guardar un lubro de forma asíncrona, se utiliza AsyncTask
public class GuardarLibroAsinc extends AsyncTask<Void, Void, Void> {
    //Se crean los atributos de la clase para almacenar
    Libro libro;
    Context context;

    //Constructor de la clase
    public GuardarLibroAsinc(Libro libro, Context context) {
        this.libro = libro;
        this.context = context;
    }

    //Método para realizar la operación de guardar el libro de fondo
    @Override
    protected Void doInBackground(Void... voids) {
        //Se obtiene el DAO para interactuar con la base de datos
        LibroDAO libroDAO = MainActivity.database.libroDAO();
        //Se instera el libro en la base de datos
        libroDAO.insert(libro);
        return null;
    }
    //Método que se ejecuta después de que la tarea de fondo termine
    @Override
    protected void onPostExecute(Void aVoid){
        //Se muestra un mensaje de éxito al usuario
        Toast.makeText(context, context.getString(R.string.anadir_exito), Toast.LENGTH_LONG);
        //Se llama a la función porcentajeLectura para actualizar el estado del porcentaje
        porcentajeLectura();
    }
}

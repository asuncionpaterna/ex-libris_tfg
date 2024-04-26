package com.example.booklist_tfg.ui.listadoLecturas;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.inicio;
import static com.example.booklist_tfg.MainActivity.listaLibros;
import static com.example.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
import static com.example.booklist_tfg.MainActivity.mostrarListaPeq;
import static com.example.booklist_tfg.MainActivity.peq;
import static com.example.booklist_tfg.utils.Utils.establecerTema;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklist_tfg.MainActivity;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.ui.listadoLibros.LibroAdapterLista;
import com.example.booklist_tfg.ui.listadoLibros.LibroAdapterListaPeq;

import java.util.ArrayList;
import java.util.List;

public class ListadoLecturas extends Fragment {

    static RecyclerView mRecyclerView;
    FrameLayout listadoLecturasFL;
    TextView tituloTV;
    private static ProgressBar progressBar;
    CheckBox favoritoCB, papelCB, digitalCB;
    EditText anioET;
    ImageButton anioBtn;
    boolean filtroFavorito, filtroEsPapel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listadolecturas, container, false);
        if (floatingBTN != null) {
            floatingBTN.show();
        }
        inicio = false;
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        mostrarBusquedaAvanzada = true;
        mostrarListaPeq = true;

        requireActivity().invalidateOptionsMenu();
        mRecyclerView = view.findViewById(R.id.idRVMostrarListaLL);
        listadoLecturasFL = view.findViewById(R.id.listadoLecturasFL);
        tituloTV = view.findViewById(R.id.idTVTituloLL);
        tituloTV.setText("Lecturas realizadas");
        favoritoCB = view.findViewById(R.id.idCBFavoritoLL);
        papelCB = view.findViewById(R.id.idCBPapelLL);
        digitalCB = view.findViewById(R.id.idCBDigitalLL);
        anioET = view.findViewById(R.id.idEdtAnio);
        anioBtn = view.findViewById(R.id.idBtnAnio);
        progressBar = view.findViewById(R.id.idCargaPB);

        filtroFavorito = MainActivity.sharedPreferences.getBoolean("filtro_favorito", false);
        filtroEsPapel = MainActivity.sharedPreferences.getBoolean("filtro_espapel", false);

        establecerTema(modoOscuro, listadoLecturasFL);

        favoritoCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (favoritoCB.isChecked()) {
                    SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                    if (papelCB.isChecked()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new RecogerLibrosFavoritoFormatoDB(getContext()).execute(true);
                        digitalCB.setChecked(false);
                    } else if (papelCB.isChecked()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new RecogerLibrosFavoritoFormatoDB(getContext()).execute(false);
                        papelCB.setChecked(false);
                    } else {
                        new RecogerLibrosFavoritosDB(getContext()).execute();
                    }
                } else {
                    new RecogerTodosLibrosDB(getContext()).execute();
                }
            }
        });

        papelCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (papelCB.isChecked()) {
                    if (favoritoCB.isChecked()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new RecogerLibrosFavoritoFormatoDB(getContext()).execute(true);
                        digitalCB.setChecked(false);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        new RecogerLibrosFormatoDB(getContext()).execute(true);
                        digitalCB.setChecked(false);
                    }
                } else {
                    new RecogerTodosLibrosDB(getContext()).execute();
                }
            }
        });


        digitalCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (digitalCB.isChecked()) {
                    if (favoritoCB.isChecked()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new RecogerLibrosFavoritoFormatoDB(getContext()).execute(false);
                        papelCB.setChecked(false);
                    } else {
                        new RecogerLibrosFormatoDB(getContext()).execute(false); // false para libros en formato digital
                        papelCB.setChecked(false);
                    }
                } else {
                    new RecogerTodosLibrosDB(getContext()).execute();
                }
            }
        });


        anioET.setFilters(new InputFilter[]{new ValidacionNumeros()});
        anioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String anio = anioET.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (anio.isEmpty()) {
                    anioET.setError("Introduce año");
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    new RecogerLibrosAnioDB(getContext()).execute(anio);
                }
            }
        });

        new RecogerTodosLibrosDB(getContext()).execute();
        return view;
    }

    public static void mostrarLibrosLecturas(Context context) {

        progressBar.setVisibility(View.GONE);

        // below line is use to add linear layout
        // manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        // in below line we are setting layout manager and
        // adapter to our recycler view.
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // below line is use to pass our
        // array list in adapter class.
        if (!peq) {
            LibroAdapterLista adapter = new LibroAdapterLista((ArrayList<Libro>) listaLibros, context);
            mRecyclerView.setAdapter(adapter);
        } else {
            LibroAdapterListaPeq adapter = new LibroAdapterListaPeq((ArrayList<Libro>) listaLibros, context);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public static class RecogerTodosLibrosDB extends AsyncTask<Void, Void, Void> {
        Context context;

        public RecogerTodosLibrosDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosFavoritosDB extends AsyncTask<Void, Void, Void> {
        Context context;

        public RecogerLibrosFavoritosDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getbyFavorito();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosFormatoDB extends AsyncTask<Boolean, Void, Void> {
        Context context;

        public RecogerLibrosFormatoDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Boolean... booleans) {
            boolean formato = booleans[0];
            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getbyFormato(formato);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public class ValidacionNumeros implements InputFilter {
        // Método para filtrar caracteres no deseados (letras y simbolos)
        private static final int TAMANO_MAX = 4;
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // Recorremos la parte de 'source' que se intenta añadir al campo de texto
            int tamanoActual = dest.length();
            int nuevoTamano = (dend-dstart) + (end-start);

            if(tamanoActual + nuevoTamano > TAMANO_MAX){
                return "";
            }
            for (int i = start; i < end; i++) {
                // Si algún carácter no es un número, se devuelve una cadena vacía.
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    }

    public static class RecogerLibrosAnioDB extends AsyncTask<String, Void, List<Libro>> {
        Context context;

        public RecogerLibrosAnioDB(Context context) {
            this.context = context;
        }

        @Override
        protected List<Libro> doInBackground(String... strings) {
            String anio = strings[0];
            LibroDAO libroDAO = database.libroDAO();
            return libroDAO.getByAnio(anio);
        }

        @Override
        protected void onPostExecute(List<Libro> libros) {
            listaLibros = libros;
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosAutoriaDB extends AsyncTask<Void, Void, Void> {
        Context context;
        String autoria;

        public RecogerLibrosAutoriaDB(Context context, String autoria) {
            this.context = context;
            this.autoria = autoria;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByAutoria(autoria);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosGeneroDB extends AsyncTask<Void, Void, Void> {
        Context context;
        String genero;

        public RecogerLibrosGeneroDB(Context context, String genero) {
            this.context = context;
            this.genero = genero;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByGenero(genero);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosEditorialDB extends AsyncTask<Void, Void, Void> {
        Context context;
        String editorial;

        public RecogerLibrosEditorialDB(Context context, String editorial) {
            this.context = context;
            this.editorial = editorial;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByEditorial(editorial);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }

    public static class RecogerLibrosFavoritoFormatoDB extends AsyncTask<Boolean, Void, Void> {
        Context context;

        public RecogerLibrosFavoritoFormatoDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Boolean... booleans) {
            boolean formato = booleans[0];
            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByFavoritoFormato(formato);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosLecturas(context);
        }
    }
}
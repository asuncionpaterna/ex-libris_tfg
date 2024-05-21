package com.exlibris_project.booklist_tfg.ui.listadoLecturas;

import static com.exlibris_project.booklist_tfg.MainActivity.database;
import static com.exlibris_project.booklist_tfg.MainActivity.floatingBTN;
import static com.exlibris_project.booklist_tfg.MainActivity.inicio;
import static com.exlibris_project.booklist_tfg.MainActivity.listaLibros;
import static com.exlibris_project.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
import static com.exlibris_project.booklist_tfg.MainActivity.mostrarListaPeq;
import static com.exlibris_project.booklist_tfg.MainActivity.peq;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.exlibris_project.booklist_tfg.MainActivity;
import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.ddbb.LibroDAO;
import com.exlibris_project.booklist_tfg.ui.listadoLibros.LibroAdapterLista;
import com.exlibris_project.booklist_tfg.ui.listadoLibros.LibroAdapterListaPeq;
import com.exlibris_project.booklist_tfg.utils.Utils;

import java.util.ArrayList;

public class ListadoLecturasFragment extends Fragment {

    static RecyclerView mRecyclerView;
    FrameLayout listadoLecturasFL;
    TextView tituloTV;
    private static ProgressBar progressBar;
    CheckBox favoritoCB, papelCB, digitalCB;
    EditText anioET;
    ImageButton anioBtn;
    boolean filtroFavorito, filtroEsPapel, filtroDigital, anioCorrecto;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listadolecturas, container, false);
        if (floatingBTN != null) {
            floatingBTN.show();
        }
        inicio = false;
        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        mostrarBusquedaAvanzada = true;
        mostrarListaPeq = true;

        requireActivity().invalidateOptionsMenu();
        mRecyclerView = view.findViewById(R.id.idRVMostrarListaLL);
        listadoLecturasFL = view.findViewById(R.id.listadoLecturasFL);
        tituloTV = view.findViewById(R.id.idTVTituloLL);
        tituloTV.setText(getString(R.string.titulo_lecturas));
        favoritoCB = view.findViewById(R.id.idCBFavoritoLL);
        papelCB = view.findViewById(R.id.idCBPapelLL);
        digitalCB = view.findViewById(R.id.idCBDigitalLL);
        anioET = view.findViewById(R.id.idEdtAnio);
        anioBtn = view.findViewById(R.id.idBtnAnio);
        progressBar = view.findViewById(R.id.idCargaPB);

        filtroFavorito = MainActivity.sharedPreferences.getBoolean("filtro_favorito", false);
        filtroEsPapel = MainActivity.sharedPreferences.getBoolean("filtro_espapel", false);

        Utils.establecerTema(modoOscuro, listadoLecturasFL);

        favoritoCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                filtroFavorito = favoritoCB.isChecked();
                filtrarLibros();
            }
        });

        papelCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (papelCB.isChecked()) {
                    digitalCB.setChecked(false);
                }
                filtroEsPapel = papelCB.isChecked();
                progressBar.setVisibility(View.VISIBLE);

                filtrarLibros();
            }
        });


        digitalCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (digitalCB.isChecked()) {
                    papelCB.setChecked(false);
                }
                filtroDigital = digitalCB.isChecked();
                progressBar.setVisibility(View.VISIBLE);

                filtrarLibros();
            }
        });


        anioET.setFilters(new InputFilter[]{new ValidacionNumeros()});

        anioET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(anioET.getText().length()!= 0 && anioET.getText().length()<4){
                    anioET.setError(getString(R.string.error_anio));
                    anioCorrecto =false;
                } else if (anioET.getText().length()==4) {
                    anioET.setError(null);
                    anioCorrecto =true;                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        anioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                filtrarLibros();
            }
        });

        return view;
    }

    // Función para mantener las preferencias en la búsqueda cuando se navega en la aplicación (volver)
    @Override
    public void onStart() {
        super.onStart();
        filtrarLibros();
    }
    private void filtrarLibros() {

        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = MainActivity.database.libroDAO();
            StringBuilder consulta = new StringBuilder("SELECT * FROM libros ");
            boolean wherebool = false;

            if (favoritoCB.isChecked()) {
                if (!wherebool) {
                    consulta.append("WHERE ");
                    wherebool = true;
                } else {
                    consulta.append(" AND ");
                }
                consulta.append("favorito = 1");
            }
            if (papelCB.isChecked() && !digitalCB.isChecked()) {
                if (!wherebool) {
                    consulta.append("WHERE ");
                    wherebool = true;
                } else {
                    consulta.append(" AND ");
                }
                consulta.append(" es_papel = 1 ");
            } else if (!papelCB.isChecked() && digitalCB.isChecked()) {
                if (!wherebool) {
                    consulta.append("WHERE ");
                    wherebool = true;
                } else {
                    consulta.append(" AND ");
                }
                consulta.append(" es_papel = 0 ");
            }

            if (!(anioET.getText().toString()).isEmpty() && anioCorrecto) {
                String anio = anioET.getText().toString();
                if (!wherebool) {
                    consulta.append("WHERE ");
                } else {
                    consulta.append(" AND ");
                }
                consulta.append(" fecha_lectura like ").append(anio).append(" || '%' ");
            }

            consulta.append(" ORDER BY fecha_lectura DESC");

            SupportSQLiteQuery sqlq = new SimpleSQLiteQuery(consulta.toString());
            listaLibros = libroDAO.getLibrosFiltro(sqlq);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mostrarLibrosLecturas(getContext());
                }
            });
        });
        thread.start();
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

    public class ValidacionNumeros implements InputFilter {
        // Método para filtrar caracteres no deseados (letras y simbolos)
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            for (int i = start; i < end; i++) {
                // Si algún carácter no es un número, se devuelve una cadena vacía.
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
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

}
package com.example.booklist_tfg.ui.listadoLecturas;
import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.listaLibros;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.ui.listadoHome.BookAdapterList;
import java.util.ArrayList;
import java.util.List;

public class ListadoLecturas extends Fragment {

    static RecyclerView mRecyclerView;
    TextView tituloTV;
    private static ProgressBar progressBar;
    CheckBox favoritoCB, papelCB, digitalCB;
    EditText anioET;

    ImageButton  anioBtn;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listadolecturas, container, false);
        if (floatingBTN != null) {
            floatingBTN.show();
        }
        mRecyclerView = view.findViewById(R.id.idRVMostrarListaLL);
        tituloTV = view.findViewById(R.id.idTVTituloLL);
        tituloTV.setText("Lecturas realizadas");
        favoritoCB = view.findViewById(R.id.idCBFavoritoLL);
        papelCB = view.findViewById(R.id.idCBPapelLL);
        digitalCB = view.findViewById(R.id.idCBDigitalLL);
        anioET = view.findViewById(R.id.idEdtAnio);
        anioBtn = view.findViewById(R.id.idBtnAnio);
        progressBar = view.findViewById(R.id.idCargaPB);

        favoritoCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (favoritoCB.isChecked()) {
                    new RecogerLibrosFavoritosDB(getContext()).execute();
                } else {
                    new RecogerTodosLibrosDB(getContext()).execute();
                }
            }
        });

        papelCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (papelCB.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new RecogerLibrosFormatoDB(getContext()).execute(true);
                    digitalCB.setChecked(false);
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
                    new RecogerLibrosFormatoDB(getContext()).execute(false); // false para libros en formato digital
                    papelCB.setChecked(false);
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
                if(anio.isEmpty()){
                    anioET.setError("Introduce año");
                    return;
                }else{
                progressBar.setVisibility(View.VISIBLE);
                new RecogerLibrosAnioDB(getContext()).execute(anio);
                }
            }
        });

        new RecogerTodosLibrosDB(getContext()).execute();
        return view;
    }

    public static void mostrarLibros(Context context) {

        progressBar.setVisibility(View.GONE);
        List<Libro> listaMostrar = new ArrayList<>();
        // below line is use to pass our
        // array list in adapter class.
        BookAdapterList adapter = new BookAdapterList((ArrayList<Libro>) listaLibros, context);
        // below line is use to add linear layout
        // manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        // in below line we are setting layout manager and
        // adapter to our recycler view.
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
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
            mostrarLibros(context);
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
            mostrarLibros(context);
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
            mostrarLibros(context);
        }
    }

    public class ValidacionNumeros implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
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
            listaLibros =libros;
            mostrarLibros(context);
        }
    }
}
package com.example.booklist_tfg.ui.home;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.listaLibros;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.ui.anadir.BookAdapter;
import com.example.booklist_tfg.ui.listado.BookAdapterList;


import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    static RecyclerView mRecyclerView;
    TextView tituloTV;
    static Year anio = Year.parse(Year.now().toString());


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (floatingBTN != null) {
            floatingBTN.show();
        }
        mRecyclerView = view.findViewById(R.id.idRVMostrarLista);
        tituloTV = view.findViewById(R.id.idTvTitulo);
        tituloTV.setText("Lecturas del " + anio);

        new RecogerLibrosDB(getContext()).execute();
        return view;
    }

    public static void mostrarLibros(Context context) {

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

    public static class RecogerLibrosDB extends AsyncTask<Void, Void, Void> {
        Context context;

        public RecogerLibrosDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByAnio(String.valueOf(anio));
            //libroDAO.deleteAll(listaLibros);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibros(context);
        }
    }
}
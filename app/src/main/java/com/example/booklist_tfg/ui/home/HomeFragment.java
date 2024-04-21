package com.example.booklist_tfg.ui.home;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.listaLibros;
import static com.example.booklist_tfg.MainActivity.objetivoLectura;
import static com.example.booklist_tfg.utils.Utils.establecerTema;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.example.booklist_tfg.ui.listadoHome.BookAdapterList;
import com.google.android.material.button.MaterialButton$InspectionCompanion;


import java.time.Year;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    static RecyclerView mRecyclerView;
    TextView tituloTV;
    FrameLayout homeFL;

    static ProgressBar objetivoLecturaPB;
    static Year anio = Year.parse(Year.now().toString());


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (floatingBTN != null) {
            floatingBTN.show();
        }
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        homeFL = view.findViewById(R.id.homeFL);
        establecerTema(modoOscuro, homeFL);
        mRecyclerView = view.findViewById(R.id.idRVMostrarLista);
        tituloTV = view.findViewById(R.id.idTvTitulo);
        tituloTV.setText("Lecturas del " + anio);
        objetivoLecturaPB = view.findViewById(R.id.idObjetivoLectura);

        porcentajeLectura();
        new RecogerLibrosDB(getContext()).execute();
        return view;
    }

    public static void porcentajeLectura() {

        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();

            objetivoLectura = MainActivity.sharedPreferences.getInt("objetivo_lectura", 0);
            int lecturas = libroDAO.getCountLecturabyAnio(String.valueOf(anio));
            float porcentaje = ((float) lecturas / (float) objetivoLectura) * 100;
            objetivoLecturaPB.setProgress((int) porcentaje);

            ColorStateList progressColor;

            if (porcentaje < 20) {
                progressColor = ColorStateList.valueOf(0xFFCC0000);
            } else if (porcentaje < 45) {
                progressColor = ColorStateList.valueOf(0xFFFF9900);
            } else if (porcentaje < 75) {
                progressColor = ColorStateList.valueOf(0xFF9BD900);
            } else if (porcentaje < 100) {
                progressColor = ColorStateList.valueOf(0xFF05CA74);
            } else {
                progressColor = ColorStateList.valueOf(0xFF40C4FF);
            }

            objetivoLecturaPB.setProgressTintList(progressColor);
        });
        thread.start();
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
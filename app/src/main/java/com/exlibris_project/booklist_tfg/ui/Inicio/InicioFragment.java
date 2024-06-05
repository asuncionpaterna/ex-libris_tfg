package com.exlibris_project.booklist_tfg.ui.Inicio;

import static com.exlibris_project.booklist_tfg.MainActivity.database;
import static com.exlibris_project.booklist_tfg.MainActivity.floatingBTN;
import static com.exlibris_project.booklist_tfg.MainActivity.inicio;
import static com.exlibris_project.booklist_tfg.MainActivity.listaLibros;
import static com.exlibris_project.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
import static com.exlibris_project.booklist_tfg.MainActivity.mostrarListaPeq;
import static com.exlibris_project.booklist_tfg.MainActivity.objetivoLectura;
import static com.exlibris_project.booklist_tfg.MainActivity.peq;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
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

import com.exlibris_project.booklist_tfg.MainActivity;
import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.ddbb.LibroDAO;
import com.exlibris_project.booklist_tfg.ui.listadoLibros.LibroAdapterLista;
import com.exlibris_project.booklist_tfg.ui.listadoLibros.LibroAdapterListaPeq;
import com.exlibris_project.booklist_tfg.utils.Utils;


import java.time.Year;
import java.util.ArrayList;


public class InicioFragment extends Fragment {

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
        inicio = true;
        mostrarBusquedaAvanzada = false;
        mostrarListaPeq = true;
        //Invalida el menú de opciones para que se vuelva a mostrar
        requireActivity().invalidateOptionsMenu();
        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //Elementos de la vista
        homeFL = view.findViewById(R.id.homeFL);
        Utils.establecerTema(modoOscuro, homeFL);
        mRecyclerView = view.findViewById(R.id.idRVMostrarLista);
        tituloTV = view.findViewById(R.id.idTvTitulo);

        //Establece el título del textview con el año actual
        tituloTV.setText(getResources().getString(R.string.titulo_lecturas_inicio) +" "+ anio);
        objetivoLecturaPB = view.findViewById(R.id.idObjetivoLectura);
        // Se actualiza el porcentaje de lectura llamando al método
        porcentajeLectura();
        new RecogerLibrosDB(getContext()).execute();
        return view;
    }

    public static void porcentajeLectura() {
    //Método para calcular y establecer el porcentaje de lectura, con colores
        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();

            objetivoLectura = MainActivity.sharedPreferences.getInt("objetivo_lectura", 0);
            int lecturas = libroDAO.getCountLecturabyAnio(String.valueOf(anio));
            float porcentaje = ((float) lecturas / (float) objetivoLectura) * 100;
            objetivoLecturaPB.setProgress((int) porcentaje);

            ColorStateList progressColor;
            // Se establece el color de la barra de progreso según el porcentaje de lectura
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
    // Método para mostrar la lista de libros en la pantalla de inicio
    public static void mostrarLibrosInicio(Context context) {
        // Se agrega el LinearLayoutManager al RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);

        // Se establece el layout manager y el adaptador para el RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // Se pasa la lista de libros al adaptador correspondiente según si la visualización es de arjeta pequeña o grande
        if (!peq) {
            LibroAdapterLista adapter = new LibroAdapterLista((ArrayList<Libro>) listaLibros, context);
            mRecyclerView.setAdapter(adapter);
        } else {
            LibroAdapterListaPeq adapter = new LibroAdapterListaPeq((ArrayList<Libro>) listaLibros, context);
            mRecyclerView.setAdapter(adapter);
        }

    }
    // Clase AsyncTask para recoger los libros de la base de datos
    public static class RecogerLibrosDB extends AsyncTask<Void, Void, Void> {
        Context context;

        public RecogerLibrosDB(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LibroDAO libroDAO = database.libroDAO();
            listaLibros = libroDAO.getByAnio(String.valueOf(anio));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mostrarLibrosInicio(context);
        }
    }
}
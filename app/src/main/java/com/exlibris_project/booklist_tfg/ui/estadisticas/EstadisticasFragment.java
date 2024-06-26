package com.exlibris_project.booklist_tfg.ui.estadisticas;

import static com.exlibris_project.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
import static com.exlibris_project.booklist_tfg.MainActivity.mostrarListaPeq;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.utils.Utils;

import java.util.List;


public class EstadisticasFragment extends Fragment {

    FrameLayout mEstadisticasFL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        mEstadisticasFL = view.findViewById(R.id.estadisticasFL);
        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Utils.establecerTema(modoOscuro, mEstadisticasFL);
        mostrarBusquedaAvanzada = false;
        mostrarListaPeq = false;

        requireActivity().invalidateOptionsMenu();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        List<Graficos> chartList = Graficos.createChartList(getResources());
        final ChartsAdapter adapter = new ChartsAdapter(chartList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


}
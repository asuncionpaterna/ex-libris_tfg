package com.exlibris_project.booklist_tfg.ui.estadisticas;

import static com.exlibris_project.booklist_tfg.MainActivity.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.LabelsFactory;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.ddbb.LibroDAO;
import com.exlibris_project.booklist_tfg.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GraficoBarrasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        Button compartirBTN = findViewById(R.id.compartirBTN);

        Context context = this;

        anyChartView.setDrawingCacheEnabled(true);
        anyChartView.buildDrawingCache(true);
        compartirBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap chartBitmap = anyChartView.getDrawingCache();
                Utils.shareImage(chartBitmap, context);
            }
        });
        Cartesian graficoBarras = AnyChart.column();
        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();

            Map<String, Integer> mapaGeneros = libroDAO.getGeneros();
            List<DataEntry> datos = new ArrayList<>();
            int i = 0;

            // Se itera sobre el HashMap y se muestran las claves y valores
            for (Map.Entry<String, Integer> entry : mapaGeneros.entrySet()) {
                String clave = Utils.verificarGeneroLiterario(entry.getKey(), context);  // Se obtiene la clave
                Integer valor = entry.getValue();  // Se obtiene el valor
                datos.add(new ValueDataEntry(clave, valor));
                i++;
                if (i == 5) break;
            }

            Column columna = graficoBarras.column(datos);
            LabelsFactory xEtiquetasEje = graficoBarras.xAxis(0).labels();
            xEtiquetasEje.rotation(-90);
            columna.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format(getString(R.string.column_tooltip_format));

            graficoBarras.animation(true);
            graficoBarras.title(getString(R.string.titulo_estadisticas_barras));

            graficoBarras.yScale().minimum(0d);

            graficoBarras.yAxis(0).labels().format(getString(R.string.column_tooltip_format));


            graficoBarras.tooltip().positionMode(TooltipPositionMode.POINT);
            graficoBarras.interactivity().hoverMode(HoverMode.BY_X);

            graficoBarras.xAxis(0).title(getString(R.string.label_generos_literarios));
            graficoBarras.yAxis(0).title(getString(R.string.y_axis_label));


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    anyChartView.setChart(graficoBarras);
                }
            });
        });
        thread.start();

    }
}
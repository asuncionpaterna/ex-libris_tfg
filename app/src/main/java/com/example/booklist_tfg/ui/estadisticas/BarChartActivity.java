package com.example.booklist_tfg.ui.estadisticas;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.utils.Utils.shareImage;
import static com.example.booklist_tfg.utils.Utils.verificarGeneroLiterario;

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
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.ddbb.LibroDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BarChartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

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

                shareImage(chartBitmap, context);
            }
        });
        Cartesian cartesian = AnyChart.column();
        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();

            Map<String, Integer> mapaGeneros = libroDAO.getGeneros();
            List<DataEntry> data = new ArrayList<>();
            int i = 0;

            // Iterar sobre el HashMap y mostrar las claves y valores
            for (Map.Entry<String, Integer> entry : mapaGeneros.entrySet()) {
                String key = verificarGeneroLiterario(entry.getKey());  // Obtener la clave
                Integer value = entry.getValue();  // Obtener el valor
                data.add(new ValueDataEntry(key, value));
                i++;
                if (i == 5) break;
            }

            Column column = cartesian.column(data);
            LabelsFactory xAxisLabels = cartesian.xAxis(0).labels();
            xAxisLabels.rotation(-90);
            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("${%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Top 10 Cosmetic Products by Revenue");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");


            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Product");
            cartesian.yAxis(0).title("Revenue");


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    anyChartView.setChart(cartesian);
                }
            });
        });
        thread.start();

    }
}
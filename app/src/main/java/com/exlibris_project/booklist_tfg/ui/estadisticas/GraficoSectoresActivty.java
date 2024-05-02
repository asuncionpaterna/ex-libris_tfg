package com.exlibris_project.booklist_tfg.ui.estadisticas;

import static com.exlibris_project.booklist_tfg.MainActivity.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.res.Resources;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.ddbb.LibroDAO;
import com.exlibris_project.booklist_tfg.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GraficoSectoresActivty extends AppCompatActivity {

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

        Pie graficoSectores = AnyChart.pie();

        graficoSectores.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(GraficoSectoresActivty.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        Thread thread = new Thread(() -> {
            Resources resources = getResources();
            LibroDAO libroDAO = database.libroDAO();
            List<DataEntry> datos = new ArrayList<>();

            int[] etiquetasMes = {
                    R.string.mes_enero,
                    R.string.mes_febrero,
                    R.string.mes_marzo,
                    R.string.mes_abril,
                    R.string.mes_mayo,
                    R.string.mes_junio,
                    R.string.mes_julio,
                    R.string.mes_agosto,
                    R.string.mes_septiembre,
                    R.string.mes_octubre,
                    R.string.mes_noviembre,
                    R.string.mes_diciembre,
            };

            for (int i = 0; i < 12; i++) {
                String nombreMes = resources.getString(etiquetasMes[i]);
                String numeroMes = String.format("%02d", i + 1); // Para obtener "01", "02", ..., "12"
                int count = libroDAO.getCountLecturabyMes(numeroMes);
                datos.add(new ValueDataEntry(nombreMes, count));
            }

            graficoSectores.data(datos);

            graficoSectores.title(getString(R.string.label_lecturas_mes));
            graficoSectores.labels().position("outside");
            graficoSectores.legend().title().enabled(true);
            graficoSectores.legend().title()
                    .text(getString(R.string.label_meses))
                    .padding(0d, 0d, 10d, 0d);

            graficoSectores.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    anyChartView.setChart(graficoSectores);
                }
            });
        });
        thread.start();
    }
}
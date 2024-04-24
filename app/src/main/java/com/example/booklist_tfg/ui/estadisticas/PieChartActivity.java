package com.example.booklist_tfg.ui.estadisticas;

import static com.example.booklist_tfg.MainActivity.database;
import static com.example.booklist_tfg.utils.Utils.shareImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.example.booklist_tfg.ddbb.LibroDAO;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

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


        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(PieChartActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        Thread thread = new Thread(() -> {
            LibroDAO libroDAO = database.libroDAO();


            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Enero", libroDAO.getCountLecturabyMes("01")));
            data.add(new ValueDataEntry("Febrero", libroDAO.getCountLecturabyMes("02")));
            data.add(new ValueDataEntry("Marzo", libroDAO.getCountLecturabyMes("03")));
            data.add(new ValueDataEntry("Abril", libroDAO.getCountLecturabyMes("04")));
            data.add(new ValueDataEntry("Mayo", libroDAO.getCountLecturabyMes("05")));
            data.add(new ValueDataEntry("Junio", libroDAO.getCountLecturabyMes("06")));
            data.add(new ValueDataEntry("Julio", libroDAO.getCountLecturabyMes("07")));
            data.add(new ValueDataEntry("Agosto", libroDAO.getCountLecturabyMes("08")));
            data.add(new ValueDataEntry("Septiembre", libroDAO.getCountLecturabyMes("09")));
            data.add(new ValueDataEntry("Octubre", libroDAO.getCountLecturabyMes("10")));
            data.add(new ValueDataEntry("Noviembre", libroDAO.getCountLecturabyMes("11")));
            data.add(new ValueDataEntry("Diciembre", libroDAO.getCountLecturabyMes("12")));

            pie.data(data);

            pie.title("Libros por meses");

            pie.labels().position("outside");

            pie.legend().title().enabled(true);
            pie.legend().title()
                    .text("Meses")
                    .padding(0d, 0d, 10d, 0d);

            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    anyChartView.setChart(pie);

                }
            });
        });
        thread.start();

    }
}
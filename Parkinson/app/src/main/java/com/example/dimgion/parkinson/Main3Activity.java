package com.example.dimgion.parkinson;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.addSeries(Main2Activity.series);
        graph.setTitle("Steps/Time");
        graph.setTitleColor(Color.BLACK);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        Main2Activity.series.setDrawDataPoints(true);
        Main2Activity.series.setColor(Color.GREEN);
    }
}

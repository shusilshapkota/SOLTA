package com.cksco.theapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {

    //    static LineGraphSeries<DataPoint> series;
    BarChart graph;
    static ArrayList<graphPoint> points = new ArrayList<>();
    static BarChart _graph;
    static List<BarEntry> entries = new ArrayList<>();
    static BarDataSet dataset;
    static BarData barData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        graph = findViewById(R.id.chart);
        _graph = graph;

    }

    static void populate(graphPoint point) {
        entries.add(new BarEntry(entries.size() + 1, point.amount));
        dataset = new BarDataSet(entries, "Label");
        barData = new BarData(dataset);
        _graph.setData(barData);
        _graph.invalidate();
    }
}

package com.jhpat.discere;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class Grafico extends AppCompatActivity {
    PieChart pieChart;
    int[] colorClassArray = new int[]{Color.RED,Color.YELLOW};
    int[] sale = new int[]{29,23,22};
    ArrayList<PieEntry> dataV = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        Bundle datos = this.getIntent().getExtras();
        String xd = datos.getString("tam");
        if(xd.equals("707")){
            dataV.add(new PieEntry(200,"Critical"));
            dataV.add(new PieEntry(300,"Desirable"));

        }
        if(xd.equals("711")){
            dataV.add(new PieEntry(300,"Syntax"));
        }

        pieChart = findViewById(R.id.pieChartt);
        PieDataSet pieDataSet = new PieDataSet(dataV,"");
        pieDataSet.setColors(colorClassArray);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setHoleRadius(10);
        pieChart.setCenterTextSize(33);
        pieChart.setCenterTextSizePixels(56);
        pieChart.invalidate();
    }

}

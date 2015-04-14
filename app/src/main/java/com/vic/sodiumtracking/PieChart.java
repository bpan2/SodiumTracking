package com.vic.sodiumtracking;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieChart {
    public Intent getIntent(Context context){
        int[] values = {1,2,3,4,5};
        CategorySeries series = new CategorySeries("Pie Graph");
        int k=0;
        for(int value : values){
            series.add("Section " + ++k, value);
        }

        int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN};
        DefaultRenderer renderer = new DefaultRenderer();
        for(int color : colors){
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        renderer.setChartTitle("Daily Sodium Intake Tracking");
        renderer.setChartTitleTextSize(7);
        renderer.setZoomButtonsVisible(true);

        Intent intent = ChartFactory.getPieChartIntent(context, series, renderer, "Pie");
        return intent;
    }
}


 /*//in the calling the activity, use the following function within a button onclicklistener
    public void pieGraphHandler(View view){
        PieChart pie = new PieChart();
        Intent pieintent = pie.getIntent(this);
        startActivity(pieintent);
    }*/
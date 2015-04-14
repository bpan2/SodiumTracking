package com.vic.sodiumtracking;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Calendar;
import java.util.Date;

import static android.util.Log.d;


public class Tracking extends Fragment {

    SharedPreferences trackingLastState;
    View view;
    TextView time=null;
    TextView targetWarning=null;
    TextView consofarWarning=null;
    private static int dailyTarget;
    private static int consumedsofar;
    public LinearLayout pielayout=null;
    private GraphicalView piechart=null;
    private Bundle args=null;
    static int consumedsofarpercentile = 0;
    static int amtavailablepercentile = 0;
    static int overLimitpercentile = 0;

    int[] colors;
    String[] labels;
    int[] values;

    int color1bk;
    String label1;
    int value1bk;

    int color2bk;
    String label2;
    int value2bk;


    // Required empty public constructor
    public Tracking() {
    }

    public void setDailyTarget(int val){
        dailyTarget = val;
        d("dailyTarget: ", Integer.toString(dailyTarget));
    }

    public void setConsumedsofar(int val){
        consumedsofar = val;
        d("consumedsofar",Integer.toString(consumedsofar));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tracking, container, false);
        time = (TextView) view.findViewById(R.id.timetxt);
        targetWarning = (TextView) view.findViewById(R.id.targetWarning);
        consofarWarning = (TextView) view.findViewById(R.id.consofarWarning);
        pielayout = (LinearLayout) view.findViewById(R.id.pielayout);


        //int indicator = trackingLastState.getInt("color1", -1);

        if(trackingLastState != null){
        //if(trackingLastState.contains("color1") == true){
        //if (indicator != -1) {
            loadLastState();
            Log.i("tLSnotNull", "Loading tracking Last state");
        }
        else{
            if (dailyTarget > 1 && consumedsofar > 1) {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //The following expr won't work because the result of consumedsofar/dailyTarget is 0
                //consumedsofarpercentile = (int)(consumedsofar/dailyTarget)*100;
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                consumedsofarpercentile = consumedsofar * 100 / dailyTarget;
                amtavailablepercentile = 100 - consumedsofarpercentile;

                int overAmt = 0;
                if (amtavailablepercentile < 0) {
                    overAmt = amtavailablepercentile * -1;
                    overLimitpercentile = overLimitpercentile + overAmt;
                }
            } else {
                if (dailyTarget <= 1) {
                    targetWarning.setText("Please set a correct Adequate Daily Intake amount");
                }
                if (consumedsofar <= 1) {
                    consofarWarning.setText("Please enter a sodium amount");
                }
                consumedsofarpercentile = 1;
                amtavailablepercentile = 1;
            }

            if (amtavailablepercentile > 0) {
                colors = new int[]{Color.BLUE, Color.GREEN};
                labels = new String[]{"Sodium Consumed", "Sodium Amount Left"};
                values = new int[]{consumedsofarpercentile, amtavailablepercentile};
            } else {
                colors = new int[]{Color.BLUE, Color.RED};
                labels = new String[]{"Sodium Consumed", "Sodium Amount Exceeded"};
                values = new int[]{100, overLimitpercentile};
            }
        }

        CategorySeries series = new CategorySeries("Pie Graph");

        for(int i=0; i<2; i++){
            series.add(labels[i], values[i]);
        }

        DefaultRenderer renderer = new DefaultRenderer();
        for(int color : colors){
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }

        renderer.setChartTitle("Current Sodium Intake Status: \n");
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setZoomButtonsVisible(true);
        piechart = ChartFactory.getPieChartView(getActivity(), series, renderer);
        pielayout.addView(piechart);

        return view;
    }

    private void saveLastState(){
        trackingLastState = this.getActivity().getSharedPreferences("laststate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = trackingLastState.edit();

        color1bk  = colors[0];
        label1 = labels[0];
        value1bk  = values[0];

        color2bk  = colors[1];
        label2 = labels[1];
        value2bk  = values[1];


        editor.putInt("color1", color1bk);
        editor.putString("label1", label1);
        editor.putInt("values1", value1bk);

        editor.putInt("color2", color2bk);
        editor.putString("label2", label2);
        editor.putInt("values2", value2bk);



        Log.i("saveLastState()", "before trackingLastState() commit");
        Log.i("label1", label1);
        Log.i("value1", String.valueOf(value1bk));
        editor.commit();
    }

    private void loadLastState() {
        trackingLastState = this.getActivity().getSharedPreferences("laststate", Context.MODE_PRIVATE);

        colors[0] =  trackingLastState.getInt("color1", color1bk);
        colors[1] =  trackingLastState.getInt("color2", color2bk);
        labels[0] =  trackingLastState.getString("label1", label1);
        labels[1] =  trackingLastState.getString("label2", label2);
        values[0] =  trackingLastState.getInt("values1", value1bk);
        values[1] =  trackingLastState.getInt("values2", value2bk);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveLastState();
    }









    public void drawPieChart(int dailyTarget, int consumedsofar){

        if(dailyTarget > 0 && consumedsofar > 0){
            consumedsofarpercentile = consumedsofar*100/dailyTarget;
            amtavailablepercentile = 100 - consumedsofarpercentile;
        }
        else {
            consumedsofarpercentile = 1;
            amtavailablepercentile  = 1;
        }

        int[] colors = new int[]{Color.BLUE, Color.GREEN};
        String[] labels = {"Sodium Consumed" , "Sodium Amount Left"};
        int[] values = {consumedsofarpercentile,amtavailablepercentile};
        CategorySeries series = new CategorySeries("Pie Graph");

        for(int i=0; i<2; i++){
            series.add(labels[i], values[i]);
        }

        DefaultRenderer renderer = new DefaultRenderer();
        for(int color : colors){
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }

        renderer.setChartTitle("Current Sodium Intake Status: \n");
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLabelsTextSize(20);
        renderer.setZoomButtonsVisible(true);
        piechart = ChartFactory.getPieChartView(getActivity(), series, renderer);
        pielayout.addView(piechart);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread timerThread = null;

        Runnable runnable = new CountDownRunner();
        timerThread = new Thread(runnable);
        timerThread.start();

    }


    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void doWork() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {

                    final Calendar c = Calendar.getInstance();
                    int yy = c.get(Calendar.YEAR);
                    int mm = c.get(Calendar.MONTH);
                    int dd = c.get(Calendar.DAY_OF_MONTH);


                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours + ":" + minutes + ":" + seconds;
                    time.setText(new StringBuilder()
                            // Month is 0 based, just add 1
                            .append(yy).append(" ").append("-").append(mm + 1).append("-")
                            .append(dd) + "/" + curTime);
                } catch (Exception e) {
                }
            }
        });
    }

}
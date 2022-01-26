package com.example.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GraphActivity extends AppCompatActivity {

    private static final String[] DAYS = { "Ne", "Po", "Ut", "St", "Št", "Pi", "So" };
    private static final String[] HOURS = {"0:00", "4:00", "8:00", "12:00", "16:00", "20:00"};
    public int[] HOURS_STEPS = {0, 0, 0, 0, 0, 0};
    public BarChart barChart, barChart2;
    public int currentDay = 0, hour = 0;
    public String steps = "", km = "";
    public float goalSteps = 0;

    public DatabaseHelper databaseHelper = new DatabaseHelper(this);

    public GregorianCalendar gregorianCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //importovanie dát z databázy
        steps = databaseHelper.selectSteps();
        km = databaseHelper.selectKm();
        goalSteps = Float.parseFloat(databaseHelper.selectGoalsSteps());

        barChart = (BarChart) findViewById(R.id.barchart);
        barChart2 = (BarChart) findViewById(R.id.barchart2);

        //nastavenie dňa a hodiny v reálnom čase
        currentDay = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
        hour = gregorianCalendar.get(Calendar.HOUR_OF_DAY);

        setDataForStepsArray();

        setDataForChartToday();
        setDataForChartWeek();
    }

    //počítanie krokov v danej hodine
    public void setDataForStepsArray() {
        if(hour >= 0 && hour < 4) {
            HOURS_STEPS[0] = Integer.parseInt(steps);
        }
        if(hour >= 4 && hour < 8) {
            HOURS_STEPS[1] = Math.abs(HOURS_STEPS[0] - Integer.parseInt(steps));
        }
        if(hour >= 8 && hour < 12) {
            HOURS_STEPS[2] = Math.abs(HOURS_STEPS[1] - Integer.parseInt(steps));
        }
        if(hour >= 12 && hour < 16) {
            HOURS_STEPS[3] = Math.abs(HOURS_STEPS[2] - Integer.parseInt(steps));
        }
        if(hour >= 16 && hour < 20) {
            HOURS_STEPS[4] = Math.abs(HOURS_STEPS[3] - Integer.parseInt(steps));
        }
        if(hour >= 20) {
            HOURS_STEPS[5] = Math.abs(HOURS_STEPS[4] - Integer.parseInt(steps));
        }
    }

    //vytvorenie grafu pre kroky na celý týždeň
    public void setDataForChartWeek() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float y = 0;
            float x = i;
            if(i == (currentDay-1)) {
                y = Float.valueOf(steps);
            }
            values.add(new BarEntry(x, y));
        }

        BarDataSet set1 = new BarDataSet(values, "");
        set1.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(set1);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DAYS[(int) value];
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.SERIF);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisMaximum(goalSteps);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMaximum(goalSteps);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getLegend().setEnabled(false);
        barChart.setTouchEnabled(false);

        barChart.setData(data);
        barChart.invalidate();
    }

    //vytvorenie grafu pre kroku za deň
    public void setDataForChartToday() {
        ArrayList<BarEntry> values2 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            float y = HOURS_STEPS[i];
            float x = i;
            values2.add(new BarEntry(x, y));
        }

        BarDataSet set2 = new BarDataSet(values2, "");
        set2.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(set2);

        XAxis xAxis2 = barChart2.getXAxis();
        xAxis2.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return HOURS[(int) value];
            }
        });
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTypeface(Typeface.SERIF);
        xAxis2.setDrawGridLines(false);
        xAxis2.setDrawAxisLine(false);

        YAxis rightAxis2 = barChart2.getAxisRight();
        rightAxis2.setDrawLabels(false);
        rightAxis2.setDrawAxisLine(false);
        rightAxis2.setAxisMaximum(goalSteps);

        YAxis leftAxis2 = barChart2.getAxisLeft();
        leftAxis2.setDrawLabels(false);
        leftAxis2.setDrawAxisLine(false);
        leftAxis2.setAxisMaximum(goalSteps);

        barChart2.getDescription().setEnabled(false);
        barChart2.setDrawValueAboveBar(false);
        barChart2.getLegend().setEnabled(false);
        barChart2.setTouchEnabled(false);

        barChart2.setData(data);
        barChart2.invalidate();
    }

    //presmerovanie vo footery
    public void goToHealthG(View view) {
        Intent health = new Intent(GraphActivity.this, HealthActivity.class);
        startActivity(health);
    }

    public void goToMainG(View view) {
        Intent main = new Intent(GraphActivity.this, MainActivity.class);
        startActivity(main);
    }

    public void goToSettingsG(View view) {
        Intent settings = new Intent(GraphActivity.this, SettingsActivity.class);
        startActivity(settings);
    }
}
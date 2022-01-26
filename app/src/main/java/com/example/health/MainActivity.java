package com.example.health;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public SensorManager sensorManager;
    public Sensor stepCounterSensor;
    public Sensor stepDetectorSensor;

    public TextView txtSteps, txtGoals, txtKm;
    public Button buttonReset;

    public int steps = 0;
    public int countTime = 1;
    public String goalsSteps = "10000";
    public double km = 0;

    public Handler handler = new Handler();
    public Runnable runnable;
    public int delay = 60000;

    public DatabaseHelper databaseHelper = new DatabaseHelper(this);

    public GregorianCalendar gregorianCalendar = new GregorianCalendar();

    //!!!!!!!!!! treba mať zapnutý XAMPP, kvôli databáze, inak to nespustí :)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        txtSteps = (TextView) findViewById(R.id.textViewSteps);
        txtGoals = (TextView) findViewById(R.id.textViewGoal);
        txtKm = (TextView) findViewById(R.id.textViewKmNumber);

        buttonReset = (Button) findViewById(R.id.buttonStartSteps);

        databaseHelper.insertDataIntoStepsPerDay(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, String.valueOf(steps), String.valueOf(km), goalsSteps);

        goalsSteps = databaseHelper.selectGoalsSteps();
        txtGoals.setText("Cieľ: " + goalsSteps);
    }

    //pomocou tlačidla sa dá zastaviť počítanie krokov alebo znova spustiť
    public void startCountingSteps(View view) {
        if(countTime%2 == 0) {
            onResume();
            countTime = 1;
            Toast.makeText(getApplicationContext(), "Spustili ste počítanie krokov", Toast.LENGTH_SHORT).show();
            buttonReset.setText("ZASTAV POČÍTANIE");
        } else {
            onPause();
            countTime++;
            Toast.makeText(getApplicationContext(), "Zastavili ste počítanie krokov", Toast.LENGTH_SHORT).show();
            buttonReset.setText("SPUSTI POČÍTANIE");
        }
    }

    @Override
    protected void onResume() {
        //nové vlákno, ktoré zapisuje rozdiel krokov každých 5 minút do databázy
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                databaseHelper.insertDataIntoStepsEveryMinute(gregorianCalendar.HOUR + ":" + gregorianCalendar.MINUTE, String.valueOf(steps));
            }
        }, delay);

        super.onResume();

        sensorManager.registerListener(this, stepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, stepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this, stepCounterSensor);
        sensorManager.unregisterListener(this, stepDetectorSensor);

        handler.removeCallbacks(runnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
        }
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            txtSteps.setText(value + "");
            steps = value;
            km = Math.round(((value*(0.76))/1000) * 100.0) / 100.0;
            txtKm.setText(km + "");
            databaseHelper.updateStepsAndKmPerDay(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, String.valueOf(steps), String.valueOf(km));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    //presmerovanie vo footery
    public void goToReportsM(View view) {
        Intent reports = new Intent(MainActivity.this, GraphActivity.class);
        startActivity(reports);
    }

    public void goToHealthM(View view) {
        Intent health = new Intent(MainActivity.this, HealthActivity.class);
        startActivity(health);
    }

    public void goToSettingsM(View view) {
        Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settings);
    }
}
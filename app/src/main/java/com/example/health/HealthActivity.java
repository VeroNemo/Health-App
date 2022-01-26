package com.example.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HealthActivity extends AppCompatActivity {

    public ImageView imageView, imageView1, imageView2, imageView3, imageView4;
    public int water = 0, weight = 0, height = 0;
    public TextView textViewWeight, textViewHeight, textViewWater;

    public DatabaseHelper databaseHelper = new DatabaseHelper(this);

    public GregorianCalendar gregorianCalendar = new GregorianCalendar();

    //Voda je nastavená tak, že každý deň by sa malo "vypiť" 1000ml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        imageView = findViewById(R.id.imageView);
        imageView1 = findViewById(R.id.imageView2);
        imageView2 = findViewById(R.id.imageView3);
        imageView3 = findViewById(R.id.imageView4);
        imageView4 = findViewById(R.id.imageView5);

        textViewWeight = findViewById(R.id.textViewWeight2);
        textViewHeight = findViewById(R.id.textViewHeight2);
        textViewWater = findViewById(R.id.textViewWater);

        databaseHelper.insertDataIntoHealthInfo(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, String.valueOf(weight), String.valueOf(height), String.valueOf(water));

        //ak sa naplnila voda na max a chceš to vidieť znova, tak treba odkomentovať toto, dať runn aplikácie, zakomentovať a znova runn
        //alebo si počkaj do druhého dňa :)
        // databaseHelper.updateWater(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, "0");

        //naplnenie údajov pomocou databázy
        water = Integer.parseInt(databaseHelper.selectWater());
        weight = Integer.parseInt(databaseHelper.selectLastWeightFromHealthInfo());
        height = Integer.parseInt(databaseHelper.selectLastHeightFromHealthInfo());

        textViewWeight.setText(weight + "");
        textViewHeight.setText(height + "");

        countBMI();
        setWaterFromDatabase();
    }

    public void setWaterFromDatabase() {
        textViewWater.setText("Voda: " + water + " / 1000ml");
        switch (water) {
            case 0:
                break;
            case 200:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                break;
            case 400:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                imageView1.setImageResource(R.drawable.glass_full);
                imageView1.setClickable(false);
                break;
            case 600:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                imageView1.setImageResource(R.drawable.glass_full);
                imageView1.setClickable(false);
                imageView2.setImageResource(R.drawable.glass_full);
                imageView2.setClickable(false);
                break;
            case 800:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                imageView1.setImageResource(R.drawable.glass_full);
                imageView1.setClickable(false);
                imageView2.setImageResource(R.drawable.glass_full);
                imageView2.setClickable(false);
                imageView3.setImageResource(R.drawable.glass_full);
                imageView3.setClickable(false);
                break;
            default:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                imageView1.setImageResource(R.drawable.glass_full);
                imageView1.setClickable(false);
                imageView2.setImageResource(R.drawable.glass_full);
                imageView2.setClickable(false);
                imageView3.setImageResource(R.drawable.glass_full);
                imageView3.setClickable(false);
                imageView4.setImageResource(R.drawable.glass_full);
                imageView4.setClickable(false);
                break;
        }
    }

    //zmena vody cez kliknutie na obrázok
    public void changeWater(View view) {
        water += 200;
        textViewWater.setText("Voda: " + water + " / 1000ml");
        databaseHelper.updateWater(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, String.valueOf(water));
        switch (water) {
            case 200:
                imageView.setImageResource(R.drawable.glass_full);
                imageView.setClickable(false);
                break;
            case 400:
                imageView1.setImageResource(R.drawable.glass_full);
                imageView1.setClickable(false);
                break;
            case 600:
                imageView2.setImageResource(R.drawable.glass_full);
                imageView2.setClickable(false);
                break;
            case 800:
                imageView3.setImageResource(R.drawable.glass_full);
                imageView3.setClickable(false);
                break;
            case 1000:
                imageView4.setImageResource(R.drawable.glass_full);
                imageView4.setClickable(false);
                Toast.makeText(this, "Wii, dnes určite nebudete dehydratovaný", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //zmena váhy
    public void changeWeight(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        builder.setCancelable(false).setTitle("Zmena váhy").setMessage("Napíšte svoju váhu v kg.").setPositiveButton("Zmeniť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                weight = Integer.parseInt(editText.getText().toString());
                textViewWeight.setText(weight + " kg");
                countBMI();
                databaseHelper.updateWeight(String.valueOf(weight), gregorianCalendar.DATE + "." + gregorianCalendar.MONTH);
                dialog.cancel();
            }
        }).setNegativeButton("Späť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //zmena výšky
    public void changeHeight(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText2 = new EditText(this);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText2);
        builder.setCancelable(false).setTitle("Zmena výšky").setMessage("Napíšte svoju výsku v cm.").setPositiveButton("Zmeniť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                height = Integer.parseInt(editText2.getText().toString());
                textViewHeight.setText(height + " cm");
                countBMI();
                databaseHelper.updateHeight(String.valueOf(height), gregorianCalendar.DATE + "." + gregorianCalendar.MONTH);
                dialog.cancel();
            }
        }).setNegativeButton("Späť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    //počítanie bmi indexu
    public void countBMI() {
        if(weight > 0 && height > 0) {
            double heightMetres = ((double)height)/100.0;
            double heightPOW = Math.pow(heightMetres,heightMetres);
            double BMI = Math.round((weight/heightPOW)*100)/100;
            TextView textView = findViewById(R.id.textViewBMI2);
            textView.setText(BMI + "");
            TextView textView1 = findViewById(R.id.textViewBMI3);
            if(BMI < 18.5) {
                textView1.setText("Podváha");
                textView1.setTextColor(Color.RED);
            }
            if(BMI >= 18.5 && BMI <= 24.9) {
                textView1.setText("Normálna hmotnosť");
                textView1.setTextColor(Color.GREEN);
            }
            if(BMI >= 25 && BMI <= 29.9) {
                textView1.setText("Nadváha");
                textView1.setTextColor(Color.RED);
            }
            if(BMI >= 30) {
                textView1.setText("Obezita");
                textView1.setTextColor(Color.RED);
            }
        }
    }

    //presmerovanie vo footery
    public void goToReportsH(View view) {
        Intent reports = new Intent(HealthActivity.this, GraphActivity.class);
        startActivity(reports);
    }

    public void goToMainH(View view) {
        Intent main = new Intent(HealthActivity.this, MainActivity.class);
        startActivity(main);
    }

    public void goToSettingsH(View view) {
        Intent settings = new Intent(HealthActivity.this, SettingsActivity.class);
        startActivity(settings);
    }
}
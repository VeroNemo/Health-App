package com.example.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class SettingsActivity extends AppCompatActivity {

    public String goalsSteps = "";
    public TextView txtGoals;
    public int steps = 0, days = 0;
    public double kilometres = 0;

    public DatabaseHelper databaseHelper = new DatabaseHelper(this);

    public GregorianCalendar gregorianCalendar = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

       // steps = Integer.parseInt(databaseHelper.selectSteps());
        //kilometres = Double.parseDouble(databaseHelper.selectAllKm());
       // days = Integer.parseInt(databaseHelper.selectAppConsuming());

      //  Odznaky si berú údaje z databázy, čo je zakomentované nad týmto
      //  Aby sa vedelo, že to funguje sú tu nastavené nejaké hodnoty, ktoré sa dajú meniť
        steps = 3000;
        kilometres = 39.2;
        days = 9;

        txtGoals = (TextView) findViewById(R.id.textViewWeight2);
        goalsSteps = databaseHelper.selectGoalsSteps();
        txtGoals.setText(goalsSteps);

        checkStepsPerDay();
        checkAppConsuming();
        checkKilometres();
    }

    //zmena cieľových krokov
    public void changeGoalsSteps(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText2 = new EditText(this);
        editText2.setText(txtGoals.getText().toString());
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText2);
        builder.setCancelable(false).setTitle("Zmena cieľového počtu krokov").setMessage("Napíšte svoj dosiahnutý cieľ v krokoch.").setPositiveButton("Zmeniť", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goalsSteps = editText2.getText().toString();
                txtGoals.setText(goalsSteps);
                databaseHelper.updateGoals(gregorianCalendar.DATE + "." + gregorianCalendar.MONTH, goalsSteps);
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

    //nastavenie odznakov za kroky za deň
    public void checkStepsPerDay() {
        ImageView imageView = findViewById(R.id.imageViewBadgeDay3);
        ImageView imageView2 = findViewById(R.id.imageViewBadgeDay7);
        ImageView imageView3 = findViewById(R.id.imageViewBadgeDay10);
        ImageView imageView4 = findViewById(R.id.imageViewBadgeDay14);
        ImageView imageView5 = findViewById(R.id.imageViewBadgeDay20);

        if(steps >= 3000 && steps < 7000) {
            imageView.setImageResource(R.drawable.badge_winner);
        }

        if(steps >= 7000 && steps < 10000) {
            imageView.setImageResource(R.drawable.badge_winner);
            imageView2.setImageResource(R.drawable.badge_winner);
        }

        if(steps >= 10000 && steps < 14000) {
            imageView.setImageResource(R.drawable.badge_winner);
            imageView2.setImageResource(R.drawable.badge_winner);
            imageView3.setImageResource(R.drawable.badge_winner);
        }

        if(steps >= 14000 && steps < 20000) {
            imageView.setImageResource(R.drawable.badge_winner);
            imageView2.setImageResource(R.drawable.badge_winner);
            imageView3.setImageResource(R.drawable.badge_winner);
            imageView4.setImageResource(R.drawable.badge_winner);
        }

        if(steps >= 20000 && steps > 20000) {
            imageView.setImageResource(R.drawable.badge_winner);
            imageView2.setImageResource(R.drawable.badge_winner);
            imageView3.setImageResource(R.drawable.badge_winner);
            imageView4.setImageResource(R.drawable.badge_winner);
            imageView5.setImageResource(R.drawable.badge_winner);
        }
    }

    //nastavenie odznakov za používanie aplikácie
    public void checkAppConsuming() {
        ImageView imageView6 = findViewById(R.id.imageViewBadgeApp7);
        ImageView imageView7 = findViewById(R.id.imageViewBadgeApp14);
        ImageView imageView8 = findViewById(R.id.imageViewBadgeApp30);
        ImageView imageView9 = findViewById(R.id.imageViewBadgeApp60);

        if(days >= 7 && days < 14) {
            imageView6.setImageResource(R.drawable.badge_winner);
        }

        if(days >= 14 && days < 30) {
            imageView6.setImageResource(R.drawable.badge_winner);
            imageView7.setImageResource(R.drawable.badge_winner);
        }

        if(days >= 30 && days < 60) {
            imageView6.setImageResource(R.drawable.badge_winner);
            imageView7.setImageResource(R.drawable.badge_winner);
            imageView8.setImageResource(R.drawable.badge_winner);
        }

        if(days >= 60 && days > 60) {
            imageView6.setImageResource(R.drawable.badge_winner);
            imageView7.setImageResource(R.drawable.badge_winner);
            imageView8.setImageResource(R.drawable.badge_winner);
            imageView9.setImageResource(R.drawable.badge_winner);
        }
    }

    //nastavenie odznakov za prejdené kilometre
    public void checkKilometres() {
        ImageView imageView10 = findViewById(R.id.imageViewBadgeKM5);
        ImageView imageView11 = findViewById(R.id.imageViewBadgeKM10);
        ImageView imageView12 = findViewById(R.id.imageViewBadgeKM20);
        ImageView imageView13 = findViewById(R.id.imageViewBadgeKM42);
        ImageView imageView14 = findViewById(R.id.imageViewBadgeKM100);

        if(kilometres >= 5.0 && kilometres < 10.0) {
            imageView10.setImageResource(R.drawable.badge_winner);
        }

        if(kilometres >= 10.0 && kilometres < 20.0) {
            imageView10.setImageResource(R.drawable.badge_winner);
            imageView11.setImageResource(R.drawable.badge_winner);
        }

        if(kilometres >= 20.0 && kilometres < 42.0) {
            imageView10.setImageResource(R.drawable.badge_winner);
            imageView11.setImageResource(R.drawable.badge_winner);
            imageView12.setImageResource(R.drawable.badge_winner);
        }

        if(kilometres >= 42.0 && kilometres < 100.0) {
            imageView10.setImageResource(R.drawable.badge_winner);
            imageView11.setImageResource(R.drawable.badge_winner);
            imageView12.setImageResource(R.drawable.badge_winner);
            imageView13.setImageResource(R.drawable.badge_winner);
        }

        if(kilometres >= 100.0 && kilometres > 100.0) {
            imageView10.setImageResource(R.drawable.badge_winner);
            imageView11.setImageResource(R.drawable.badge_winner);
            imageView12.setImageResource(R.drawable.badge_winner);
            imageView13.setImageResource(R.drawable.badge_winner);
            imageView14.setImageResource(R.drawable.badge_winner);
        }
    }

    //presmerovanie vo footery
    public void goToHealthS(View view) {
        Intent health = new Intent(SettingsActivity.this, HealthActivity.class);
        startActivity(health);
    }

    public void goToMainS(View view) {
        Intent main = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(main);
    }

    public void goToReportsS(View view) {
        Intent reports = new Intent(SettingsActivity.this, GraphActivity.class);
        startActivity(reports);
    }
}
package com.example.health;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "health_database";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.StepsEveryMinute.TABLE_NAME + "( "
                + Tables.StepsEveryMinute.STEPSEVERYMINUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Tables.StepsEveryMinute.TIME + " TEXT, "
                + Tables.StepsEveryMinute.STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + Tables.StepsPerDay.TABLE_NAME + "( "
                + Tables.StepsPerDay.STEPSPERDAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Tables.StepsPerDay.DAY + " TEXT, "
                + Tables.StepsPerDay.STEPS + " TEXT, "
                + Tables.StepsPerDay.KM + " TEXT, "
                + Tables.StepsPerDay.GOALS_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + Tables.HealthInfo.TABLE_NAME + "( "
                + Tables.HealthInfo.HEALTHINFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Tables.HealthInfo.DATE + " TEXT, "
                + Tables.HealthInfo.WEIGHT + " TEXT, "
                + Tables.HealthInfo.HEIGHT + " TEXT, "
                + Tables.HealthInfo.WATER + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.StepsEveryMinute.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.StepsPerDay.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tables.HealthInfo.TABLE_NAME);

        onCreate(db);
    }

    public void insertDataIntoStepsEveryMinute(String minute, String steps) {
        ContentValues values = new ContentValues();
        values.put(Tables.StepsEveryMinute.STEPS, steps);
        values.put(Tables.StepsEveryMinute.TIME, minute);
        SQLiteDatabase database = getWritableDatabase();
        long ID = database.insert(Tables.StepsEveryMinute.TABLE_NAME, null, values);
        database.close();
    }

    public void insertDataIntoStepsPerDay(String day, String steps, String km, String goals) {
        ContentValues values = new ContentValues();
        values.put(Tables.StepsPerDay.DAY, day);
        values.put(Tables.StepsPerDay.STEPS, steps);
        values.put(Tables.StepsPerDay.KM, km);
        values.put(Tables.StepsPerDay.GOALS_STEPS, goals);
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT steps_per_day FROM " + Tables.StepsPerDay.TABLE_NAME + " WHERE day = " + day, null);
        if(cursor.getCount() == 0) {
            cursor.moveToFirst();
            long ID = database.insert(Tables.StepsPerDay.TABLE_NAME, null, values);
        }
        database.close();
    }

    public void insertDataIntoHealthInfo(String date, String weight, String height, String water) {
        ContentValues values = new ContentValues();
        values.put(Tables.HealthInfo.DATE, date);
        values.put(Tables.HealthInfo.HEIGHT, height);
        values.put(Tables.HealthInfo.WEIGHT, weight);
        values.put(Tables.HealthInfo.WATER, water);
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT height FROM " + Tables.HealthInfo.TABLE_NAME + " WHERE date = " + date, null);
        if(cursor.getCount() == 0) {
            cursor.moveToFirst();
            long ID = database.insert(Tables.HealthInfo.TABLE_NAME, null, values);
        }
        database.close();
    }

    public String selectLastWeightFromHealthInfo() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT weight FROM " + Tables.HealthInfo.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("weight"));
    }

    public String selectLastHeightFromHealthInfo() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT height FROM " + Tables.HealthInfo.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("height"));
    }

    public String selectWater() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT water FROM " + Tables.HealthInfo.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("water"));
    }

    public String selectGoalsSteps() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT goals_steps AS GOALS FROM " + Tables.StepsPerDay.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("GOALS"));
    }

    public String selectSteps() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT steps_per_day AS STEPS FROM " + Tables.StepsPerDay.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("STEPS"));
    }

    public String selectAllKm() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT SUM(km) AS ALL_KM FROM " + Tables.StepsPerDay.TABLE_NAME, null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("ALL_KM"));
    }

    public String selectAppConsuming() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT COUNT(day) AS ALL_DAYS FROM " + Tables.StepsPerDay.TABLE_NAME, null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("ALL_DAYS"));
    }

    public String selectKm() {
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT km FROM " + Tables.StepsPerDay.TABLE_NAME + " ORDER BY _id DESC LIMIT 1", null);
        cursor.moveToFirst();
        database.close();
        return cursor.getString(cursor.getColumnIndex("km"));
    }

    public void updateWeight(String w, String date) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + Tables.HealthInfo.TABLE_NAME + " SET weight = " + "'" + w + "'" + " WHERE date = " + date);
        database.close();
    }

    public void updateHeight(String h, String date) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + Tables.HealthInfo.TABLE_NAME + " SET height = " + "'" + h + "'" + " WHERE date = " + date);
        database.close();
    }

    public void updateWater(String date, String water) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + Tables.HealthInfo.TABLE_NAME + " SET water = " + "'" + water + "'" + " WHERE date = " + date);
        database.close();
    }

    public void updateStepsAndKmPerDay(String day, String steps, String km) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + Tables.StepsPerDay.TABLE_NAME + " SET steps_per_day = " + "'" + steps + "', km = " + "'" + km + "'" + " WHERE day = " + day);
        database.close();
    }

    public void updateGoals(String day, String goals) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + Tables.StepsPerDay.TABLE_NAME + " SET goals_steps = " + "'" + goals + "'" + " WHERE day = " + day);
        database.close();
    }
}

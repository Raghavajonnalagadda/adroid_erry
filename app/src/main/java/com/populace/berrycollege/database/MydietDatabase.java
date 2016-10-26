package com.populace.berrycollege.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.populace.berrycollege.activities.Nutrition_Activity;
import com.populace.berrycollege.fragments.MyDiet;

/**
 * Created by admin on 14-03-2016.
 */
public class MydietDatabase extends SQLiteOpenHelper {
    public String date;
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    public static int value_breakfast, value_lunch, value_dinner, value_snacks, value_total;
    public String value_date;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydiet.db";
    public static final String TABLE_NAME = "table_diet";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_BREAKFAST = "brkfst";
    public static final String COLUMN_LUNCH = "lunch";
    public static final String COLUMN_DINNER = "dinner";
    public static final String COLUMN_SNACKS = "snacks";
    public static final String COLUMN_TOTAL = "total";

    private static final String CREATE_INFORMATION = "CREATE TABLE  IF NOT EXISTS "
            + TABLE_NAME + "(" + COLUMN_DATE + " STRING PRIMARY KEY," + COLUMN_BREAKFAST + " INTEGER ," + COLUMN_LUNCH + " INTEGER ," + COLUMN_DINNER + " INTEGER ," + COLUMN_SNACKS + " INTEGER ," + COLUMN_TOTAL + " INTEGER " + ")";


    public MydietDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

    }

    public void createTable() {
        try {
            // creating required tables
            SQLiteDatabase db = this.getWritableDatabase();

            System.out.println("MY TABLE" + CREATE_INFORMATION);

            db.execSQL(CREATE_INFORMATION);
        } catch (Exception e) {
            System.out.println("FAIL TO CREATE TABLE " + e);
        }



    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            // creating required tables

            System.out.println("MY TABLE" + CREATE_INFORMATION);

            db.execSQL(CREATE_INFORMATION);
        } catch (Exception e) {

        }


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS CREATE_INFORMATION");
        onCreate(db);
    }


    public boolean insertData(String date, int breakfast, int lunch, int dinner, int snacks, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_BREAKFAST, breakfast);
        contentValues.put(COLUMN_LUNCH, lunch);
        contentValues.put(COLUMN_DINNER, dinner);
        contentValues.put(COLUMN_SNACKS, snacks);
        contentValues.put(COLUMN_TOTAL, total);
        db.insert("table_diet", null, contentValues);
        return true;
    }

    public int getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            date = Nutrition_Activity.dateStrng.replace(",", "");
            String query = "SELECT * FROM table_diet WHERE date = '"+date+"'";
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                value_date = cursor.getString(0);
                value_breakfast = cursor.getInt(1);
                value_lunch = cursor.getInt(2);
                value_dinner = cursor.getInt(3);
                value_snacks = cursor.getInt(4);
                value_total = cursor.getInt(5);

            }
        } catch (Exception e) {
            System.out.println("Andy error check " + e);

            do
            {

            }

            while (cursor.moveToNext());
        }
        return 0;
    }

//    public boolean updateData(String date, int breakfast, int lunch, int dinner, int snacks, int total) {
//        SQLiteDatabase db = this.getWritableDatabase();
//      //  date = Nutrition_Activity.dateStrng.replace(",", "");
//        Cursor cursor = null;
//        String query = "UPDATE table_diet WHERE date = '"+date+"'";
//        String updateQuery ="UPDATE table_diet WHERE date = "+date;
//        ContentValues contentValues = new ContentValues();
//       // contentValues.put(COLUMN_DATE, date);
//        contentValues.put(COLUMN_BREAKFAST, breakfast);
//        contentValues.put(COLUMN_LUNCH, lunch);
//        contentValues.put(COLUMN_DINNER, dinner);
//        contentValues.put(COLUMN_SNACKS, snacks);
//        contentValues.put(COLUMN_TOTAL, total);
//        return db.update("table_diet", contentValues,query, null) > 0;
//       // return db.update("table_diet", contentValues,WHERE COLUMN_DATE = "", null) > 0;
//      //  cursor = db.rawQuery(query, null);
//     //   cursor.moveToFirst();
////        cursor.close();
////        return true;
//    }

    public boolean updateData(String date, int breakfast, int lunch, int dinner, int snacks, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_BREAKFAST, breakfast);
        contentValues.put(COLUMN_LUNCH, lunch);
        contentValues.put(COLUMN_DINNER, dinner);
        contentValues.put(COLUMN_SNACKS, snacks);
        contentValues.put(COLUMN_TOTAL, total);
          return db.update("table_diet", contentValues,COLUMN_DATE + "='" + date+"'", null) > 0;
        //   return db.update("table_diet", contentValues,"date" + "='" + date+"'",where, null) > 0;

    }




}


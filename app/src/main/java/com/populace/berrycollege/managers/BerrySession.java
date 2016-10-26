package com.populace.berrycollege.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nikhil on 2/8/2016.
 */
public class BerrySession {

    public String SHARED_PERFERENCE_NAME = "RMP_RESOURCE";
    public String DEFAULT_VALUE = null;
    public int DEFAULT_INT_VALUE = -1;
    public Boolean DEFAULT_BOOLEAN_VALUE = false;
    public Long DEFAULT_LONG = 0L;


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    Context context;

    public BerrySession(Context con) {
        prefs = con.getSharedPreferences(SHARED_PERFERENCE_NAME,
                Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void putString(String TAG, String Value) {
        editor.putString(TAG, Value);
        editor.commit();
    }

    public String getString(String TAG) {
        return prefs.getString(TAG, DEFAULT_VALUE);
    }

    public void putInt(String TAG, int Value) {
        editor.putInt(TAG, Value);
        editor.commit();
    }

    public int getInt(String TAG) {
        return prefs.getInt(TAG, DEFAULT_INT_VALUE);
    }

    public void putBoolean(String TAG, Boolean Value) {
        editor.putBoolean(TAG, Value);
        editor.commit();
    }

    public Boolean getBoolean(String TAG) {
        return prefs.getBoolean(TAG, DEFAULT_BOOLEAN_VALUE);
    }

    public void putLong(String TAG, Long Value) {
        editor.putLong(TAG, Value);
        editor.commit();
    }

    public Long getLong(String TAG) {
        return prefs.getLong(TAG, DEFAULT_LONG);
    }

    public void removeValues(String TAG) {

        editor.remove(TAG);

        editor.commit();
    }

}

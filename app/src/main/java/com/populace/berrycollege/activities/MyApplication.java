package com.populace.berrycollege.activities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.populace.berrycollege.R;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(getApplicationContext(), getResources().getString(R.string.Application_Id), getResources().getString(R.string.Client_Key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        Parse.enableLocalDatastore(this);
        ParseUser.getCurrentUser().saveInBackground();

    }
}

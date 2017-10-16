package com.populace.berrycollege.activities;

import android.app.Application;

import com.buddy.sdk.Buddy;
import com.parse.Parse;
import com.parse.ParseUser;

//import com.parse.Parse;
//import com.parse.ParseInstallation;
//mport com.parse.ParseUser;

//import org.testng.annotations.Configuration;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Buddy.init(getApplicationContext(), "bb7bfa72-d8c1-4120-bf33-2c9663fac41e", "");
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("bb7bfa72-d8c1-4120-bf33-2c9663fac41e")
                .clientKey("").server("https://parse.buddy.com/parse").enableLocalDataStore().build());
        ParseUser.enableAutomaticUser();

      /*  Parse.initialize(getApplicationContext(), getResources().getString(R.string.Application_Id), getResources().getString(R.string.Client_Key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseUser.enableAutomaticUser();
        Parse.enableLocalDatastore(this);
        ParseUser.getCurrentUser().saveInBackground();*/

    }
}

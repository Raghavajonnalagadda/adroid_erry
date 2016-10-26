package com.populace.berrycollege.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.ParseDataManager;

/**
 * Created by Nikhil on 1/29/2016.
 */
public class splash extends Activity
   {



    private static Activity act;


       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        act = splash.this;
        setSplashTime();

    }



    private void setSplashTime() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                chekSharedRefrence();
                ParseDataManager.sharedDataManager(act).downloadBannerImages();
                ParseDataManager.sharedDataManager(act).fetchBannerImages();
                finish();
            }
        }, 3000);

    }





       public void chekSharedRefrence()
       { SharedPreferences settings = act.getSharedPreferences(act.getPackageName(), 0);
           String email = settings.getString("Fbemail", null);
           if (email!=null)
           {
           Intent intent= new Intent(act,MainActivity.class);
               startActivity(intent);
           }
           else
           {
               Intent intent = new Intent(act, LoginScreen.class);
               startActivity(intent);
           }


       }


   }












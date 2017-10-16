package com.populace.berrycollege.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.populace.berrycollege.R;
import com.populace.berrycollege.RegistrationIntentService;
import com.populace.berrycollege.managers.ParseDataManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static Context context;
    ImageView menuBtn;
    private ImageView ivnutrition, ivphysical, ivemotional, ivacadmic, ivsprituality, ivcharacter, ivsocial, ivnavicon, ivhealth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ivhealth = (ImageView) findViewById(R.id.health);
        context = MainActivity.this;
        menuBtn = (ImageView) findViewById(R.id.menubutton);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        menuBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);

                } else {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });


        ParseDataManager.sharedDataManager(this).checkDataVersion(true, this);
        ParseDataManager.sharedDataManager(this).downloadBannerImages();
        ParseDataManager.sharedDataManager(this).fetchBannerImages();
        FacebookSdk.sdkInitialize(getApplicationContext());
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.populace.berrycollege", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("Andy key hash "+something);
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        ActionBar bar = getSupportActionBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.sidetray_icon_grey);
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.abs_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.ab_title);
        //textviewTitle.setText("Berry College Wellness App");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
        abar.hide();


        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable( getResources().getColor( R.color.white_color )));


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.trans,  /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
//                getSupportActionBar().setTitle(R.string.title);
//                getSupportActionBar().setSubtitle(R.string.subtitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getSupportActionBar().setTitle(R.string.title);
//                getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        };
        //mDrawerToggle.setDrawerIndicatorEnabled(false);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        initUI();
        InitializeListeners();

        boolean mboolean = false;

        SharedPreferences settings = getSharedPreferences(context.getPackageName(), 0);
        mboolean = settings.getBoolean("FIRST_RUN", false);
        if (!mboolean) {
            ParseDataManager.sharedDataManager(this).downloadInformationDataCharacter();
//            settings = getSharedPreferences(context.getPackageName(), 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putBoolean("FIRST_RUN", true);
//            editor.commit();
        } else {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // getSupportActionBar().setBackgroundDrawable(R.drawable.backgroun);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_color)));
    }

    private void InitializeListeners()
    {
        ivsprituality.setOnClickListener(this);
        ivphysical.setOnClickListener(this);
        ivnutrition.setOnClickListener(this);
        ivemotional.setOnClickListener(this);
        ivcharacter.setOnClickListener(this);
        ivsocial.setOnClickListener(this);
        ivacadmic.setOnClickListener(this);
        ivhealth.setOnClickListener(this);


    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (item.getItemId() == R.id.home) {
            if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
            {
                mDrawerLayout.openDrawer(Gravity.RIGHT);

            }
            else {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    public void initUI(){
        ivacadmic = (ImageView) findViewById(R.id.acadmic);
       ivcharacter = (ImageView) findViewById(R.id.characters);
        ivemotional= (ImageView) findViewById(R.id.emotional);
        ivsocial = (ImageView)findViewById(R.id.Social);
        ivnutrition = (ImageView)findViewById(R.id.nutrition);
        ivphysical = (ImageView)findViewById(R.id.Physical);
        ivsprituality = (ImageView)findViewById(R.id.Sprituality);
        ivhealth = (ImageView) findViewById(R.id.health);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.acadmic:
                Intent in = new Intent(MainActivity.this,Acadmic_Activity.class);
                startActivity(in);

                break;

            case R.id.characters:
                Intent in1 = new Intent(MainActivity.this,Character_Activity.class);
                startActivity(in1);

                break;

            case R.id.emotional:
                Intent in2 = new Intent(MainActivity.this,Emotional_Activity.class);
                startActivity(in2);

                break;

            case R.id.Social:
                Intent in3 = new Intent(MainActivity.this,Social_Activity.class);
                startActivity(in3);

                break;

            case R.id.nutrition:
                Intent in4 = new Intent(MainActivity.this,Nutrition_Activity.class);
                startActivity(in4);

                break;

            case R.id.Physical:
                Intent in5= new Intent(MainActivity.this,Physical_Activity.class);
                startActivity(in5);

                break;

            case R.id.Sprituality:
                Intent in6 = new Intent(MainActivity.this,Sprintual_Activity.class);
                startActivity(in6);

                break;

            case R.id.health:
                Intent in7 = new Intent(MainActivity.this, HealthAndWellness.class);
                startActivity(in7);

                break;

        }


    }





}




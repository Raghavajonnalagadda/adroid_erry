package com.populace.berrycollege.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.populace.berrycollege.R;
import com.populace.berrycollege.fragments.KcabFragment;
import com.populace.berrycollege.fragments.RollCallsFragment;
import com.populace.berrycollege.fragments.SgaFragment;
import com.populace.berrycollege.fragments.SportsFragment;
import com.populace.berrycollege.fragments.StdActivityFragment;
import com.populace.berrycollege.managers.ParseDataManager;
import com.populace.berrycollege.models.GTCUser;
import com.populace.berrycollege.rollcall.GTCRollCallListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil on 2/2/2016.
 */
public class Social_Activity extends AppCompatActivity implements OnClickListener{
    private ImageView kcab,stdActivities, sports,sga,rollCalls,social;
Activity ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_social);
        ac=this;
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Social_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        InitializeListeners();
        getFragmentManager().beginTransaction().add(R.id.container_social,new KcabFragment(),null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(6);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();



        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                 .showImageOnLoading(R.drawable.background__social)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

// Load and display image
        imageLoader.displayImage(imageUri, social, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                pDialog = new ProgressDialog(context);
//                pDialog.setMessage(" Loading Images....");
//                pDialog.setCancelable(false);
//                pDialog.show();

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // pDialog.cancel();
                social.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        kcab = (ImageView) findViewById(R.id.kcab);
        stdActivities = (ImageView) findViewById(R.id.stdActivities);
        sports = (ImageView) findViewById(R.id.sports);
        sga = (ImageView) findViewById(R.id.sga);
        rollCalls = (ImageView) findViewById(R.id.rollCalls);
        social = (ImageView) findViewById(R.id.social_image);

    }

    private void InitializeListeners() {
        kcab.setOnClickListener(this);
        stdActivities.setOnClickListener(this);
        sports.setOnClickListener(this);
        sga.setOnClickListener(this);
        rollCalls.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kcab:
                kcab.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new KcabFragment(),null).commit();
                break;

            case R.id.stdActivities:
                kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new StdActivityFragment(),null).commit();
                break;

            case R.id.sports:
                kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sports.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new SportsFragment(),null).commit();
                break;

            case R.id.sga:
                kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sga.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new SgaFragment(),null).commit();
                break;

            case R.id.rollCalls:
                kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                //getFragmentManager().beginTransaction().replace(R.id.container_social,new RollCallsFragment(),null).commit();
                downloadUsersData();
                break;


        }

    }

    public void downloadUsersData()
    {

        ParseQuery<GTCUser> query = new ParseQuery<GTCUser>(GTCUser.class);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<GTCUser>() {
            public void done(List<GTCUser> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if(arg0.size()>0)
                    {
                        ParseDataManager.sharedDataManager(ac).users=(ArrayList<GTCUser>)arg0;
                        Intent rollcall=new Intent(ac, GTCRollCallListActivity.class);
                        startActivity(rollcall);
                    }

                } else {
                    // handle Parse Exception here

                    Toast.makeText(ac, "Sorry users not found",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
//                onBackPressed();
                break;
            default:
                return false;
        }
        return true;

    }
}

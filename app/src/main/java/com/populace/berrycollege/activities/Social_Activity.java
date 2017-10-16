package com.populace.berrycollege.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
Activity ac;
    private ImageView kcab, stdActivities, sports, sga, rollCalls, social;
    private LinearLayout _kcab, _stdActivities, _sports, _sga, _rollCalls;

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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Social_light)));



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
        _kcab = (LinearLayout) findViewById(R.id.kcab);
        _stdActivities = (LinearLayout) findViewById(R.id.stdActivities);
        _sports = (LinearLayout) findViewById(R.id.sports);
        _sga = (LinearLayout) findViewById(R.id.sga);
        _rollCalls = (LinearLayout) findViewById(R.id.rollCalls);
        social = (ImageView) findViewById(R.id.social_image);

    }

    private void InitializeListeners() {
        _kcab.setOnClickListener(this);
        _stdActivities.setOnClickListener(this);
        _sports.setOnClickListener(this);
        _sga.setOnClickListener(this);
        _rollCalls.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kcab:
                _kcab.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                _stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new KcabFragment(),null).commit();
                break;

            case R.id.stdActivities:
                _kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                _sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new StdActivityFragment(),null).commit();
                break;

            case R.id.sports:
                _kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sports.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                _sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new SportsFragment(),null).commit();
                break;

            case R.id.sga:
                _kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sga.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
                _rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_light));
                getFragmentManager().beginTransaction().replace(R.id.container_social,new SgaFragment(),null).commit();
                break;

            case R.id.rollCalls:
                _kcab.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _stdActivities.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sports.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _sga.setBackgroundColor(getResources().getColor(R.color.Social_light));
                _rollCalls.setBackgroundColor(getResources().getColor(R.color.Social_Dark));
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

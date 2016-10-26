package com.populace.berrycollege.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.populace.berrycollege.R;
import com.populace.berrycollege.fragments.AppointmentsFragment;
import com.populace.berrycollege.fragments.Emotions_Fragment;
import com.populace.berrycollege.fragments.GeneralInfoFragment;
import com.populace.berrycollege.fragments.ResourcesFragment;
import com.populace.berrycollege.managers.ParseDataManager;


public class Emotional_Activity extends AppCompatActivity implements OnClickListener{
    private ImageView emotions, resources,appointments,generalInfo,imotionalBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emotional);
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Emotional_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        InitializeListeners();
        getFragmentManager().beginTransaction().add(R.id.container_emotional,new Emotions_Fragment(),null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(4);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();



        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .cacheInMemory(true)
                .showImageOnLoading(R.drawable.background__emotional)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

// Load and display image
        imageLoader.displayImage(imageUri, imotionalBanner, options, new ImageLoadingListener() {
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
                imotionalBanner.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        emotions = (ImageView) findViewById(R.id.ivemotions);
        resources = (ImageView) findViewById(R.id.ivresorces);
        appointments = (ImageView) findViewById(R.id.ivappointments);
        generalInfo = (ImageView) findViewById(R.id.ivgeneral_info);
        imotionalBanner = (ImageView) findViewById(R.id.emotional_image);

    }

    private void InitializeListeners() {
        emotions.setOnClickListener(this);
        resources.setOnClickListener(this);
        appointments.setOnClickListener(this);
        generalInfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivemotions:
                emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new Emotions_Fragment(),null).commit();
                break;

            case R.id.ivresorces:
                emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                resources.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new ResourcesFragment(),null).commit();
                break;

            case R.id.ivappointments:
                emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new AppointmentsFragment(),null).commit();
                break;

            case R.id.ivgeneral_info:
                emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new GeneralInfoFragment(),null).commit();
                break;


        }

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


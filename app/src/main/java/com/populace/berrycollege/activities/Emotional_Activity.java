package com.populace.berrycollege.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.populace.berrycollege.fragments.Resourcesdetails;
import com.populace.berrycollege.managers.ParseDataManager;


public class Emotional_Activity extends AppCompatActivity implements OnClickListener{
    private ImageView emotions, resources,appointments,generalInfo,imotionalBanner;
    private LinearLayout _emotions, _resources, _appointments, _generalInfo;
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
        getFragmentManager().beginTransaction().add(R.id.container_emotional, new Resourcesdetails(), null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(4);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Emotional_light)));



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
        _emotions = (LinearLayout) findViewById(R.id.ivemotions);
        _resources = (LinearLayout) findViewById(R.id.ivresorces);
        _appointments = (LinearLayout) findViewById(R.id.ivappointments);
        _generalInfo = (LinearLayout) findViewById(R.id.ivgeneral_info);
        imotionalBanner = (ImageView) findViewById(R.id.emotional_image);
        _resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
    }

    private void InitializeListeners() {
        _emotions.setOnClickListener(this);
        _resources.setOnClickListener(this);
        _appointments.setOnClickListener(this);
        _generalInfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivemotions:
                _emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                _resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new Emotions_Fragment(),null).commit();
                break;

            case R.id.ivresorces:
                _emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _resources.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                _appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional, new Resourcesdetails(), null).commit();
                break;

            case R.id.ivappointments:
                _emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
                _generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                getFragmentManager().beginTransaction().replace(R.id.container_emotional,new AppointmentsFragment(),null).commit();
                break;

            case R.id.ivgeneral_info:
                _emotions.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _resources.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _appointments.setBackgroundColor(getResources().getColor(R.color.Emotional_light));
                _generalInfo.setBackgroundColor(getResources().getColor(R.color.Emotional_dark));
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


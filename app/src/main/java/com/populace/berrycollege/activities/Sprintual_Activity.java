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
import com.populace.berrycollege.fragments.CalanderFragment;
import com.populace.berrycollege.fragments.ComplainOffices;
import com.populace.berrycollege.fragments.GroupEvent;
import com.populace.berrycollege.fragments.ReligiousEvent;
import com.populace.berrycollege.fragments.Sprituality;
import com.populace.berrycollege.managers.ParseDataManager;

/**
 * Created by Nikhil on 2/2/2016.
 */
public class Sprintual_Activity extends AppCompatActivity implements OnClickListener{
    ImageView relegiousEvents,sprituality,groups,complainOffice,calander,spritual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sprintuality);
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Sprintual_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        InitializeListeners();
        getFragmentManager().beginTransaction().add(R.id.container_sprituality,new ReligiousEvent(),null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(5);
        System.out.println("Image for banner:" + obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();
        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .showImageOnLoading(R.drawable.background__spirituality)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

// Load and display image
        imageLoader.displayImage(imageUri, spritual, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // pDialog.cancel();
                spritual.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        relegiousEvents = (ImageView) findViewById(R.id.ivreligious_events);
        sprituality = (ImageView) findViewById(R.id.ivsprituality);
        groups = (ImageView) findViewById(R.id.ivgroups);
        complainOffice = (ImageView) findViewById(R.id.ivcomlain_offices);
        calander = (ImageView) findViewById(R.id.ivCalander);
        spritual = (ImageView) findViewById(R.id.spritual_image);

    }

    private void InitializeListeners() {
        relegiousEvents.setOnClickListener(this);
        sprituality.setOnClickListener(this);
        groups.setOnClickListener(this);
        complainOffice.setOnClickListener(this);
        calander.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivreligious_events:
                relegiousEvents.setBackgroundColor(getResources().getColor(R.color.Sprituality_Dark));
                sprituality.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                groups.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                complainOffice.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                calander.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                getFragmentManager().beginTransaction().replace(R.id.container_sprituality,new ReligiousEvent(),null).commit();
                break;

            case R.id.ivsprituality:
                relegiousEvents.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                sprituality.setBackgroundColor(getResources().getColor(R.color.Sprituality_Dark));
                groups.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                complainOffice.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                calander.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                getFragmentManager().beginTransaction().replace(R.id.container_sprituality,new Sprituality(),null).commit();
                break;

            case R.id.ivgroups:
                relegiousEvents.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                sprituality.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                groups.setBackgroundColor(getResources().getColor(R.color.Sprituality_Dark));
                complainOffice.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                calander.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                getFragmentManager().beginTransaction().replace(R.id.container_sprituality,new GroupEvent(),null).commit();
                break;

            case R.id.ivcomlain_offices:
                relegiousEvents.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                sprituality.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                groups.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                complainOffice.setBackgroundColor(getResources().getColor(R.color.Sprituality_Dark));
                calander.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                getFragmentManager().beginTransaction().replace(R.id.container_sprituality,new ComplainOffices(),null).commit();
                break;

            case R.id.ivCalander:
                relegiousEvents.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                sprituality.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                groups.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                complainOffice.setBackgroundColor(getResources().getColor(R.color.Sprituality_light));
                calander.setBackgroundColor(getResources().getColor(R.color.Sprituality_Dark));
                getFragmentManager().beginTransaction().replace(R.id.container_sprituality,new CalanderFragment(),null).commit();
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

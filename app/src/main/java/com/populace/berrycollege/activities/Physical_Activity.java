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
import com.populace.berrycollege.fragments.Activities_Fragment;
import com.populace.berrycollege.fragments.Daily_Workout_Fragment;
import com.populace.berrycollege.fragments.Intramural;
import com.populace.berrycollege.fragments.TheCage_Fragment;
import com.populace.berrycollege.managers.ParseDataManager;


public class Physical_Activity extends AppCompatActivity implements OnClickListener{
    private ImageView dailyWorkout,theCage, activities,intrmural,physical;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_physical);
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Physical_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        InitializeListeners();
        getFragmentManager().beginTransaction().add(R.id.container_physical,new TheCage_Fragment(),null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(2);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();



        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .showImageOnLoading(R.drawable.background__physical)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

// Load and display image
        imageLoader.displayImage(imageUri, physical, options, new ImageLoadingListener() {
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
                physical.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        dailyWorkout = (ImageView) findViewById(R.id.ivdaily_workout);
        theCage = (ImageView) findViewById(R.id.ivthe_cage);
        activities = (ImageView) findViewById(R.id.ivactivities);
        intrmural = (ImageView) findViewById(R.id.ivintramural);
        physical = (ImageView) findViewById(R.id.physical_image);

    }

    private void InitializeListeners() {
        dailyWorkout.setOnClickListener(this);
        theCage.setOnClickListener(this);
        activities.setOnClickListener(this);
        intrmural.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivdaily_workout:
                dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Daily_Workout_Fragment(),null).commit();
                break;

            case R.id.ivthe_cage:
                dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                theCage.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new TheCage_Fragment(),null).commit();
                break;

            case R.id.ivactivities:
                dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                activities.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Activities_Fragment(),null).commit();
                break;

            case R.id.ivintramural:
                dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Intramural(),null).commit();
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

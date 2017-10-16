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
import com.populace.berrycollege.fragments.Activities_Fragment;
import com.populace.berrycollege.fragments.Daily_Workout_Fragment;
import com.populace.berrycollege.fragments.Intramural;
import com.populace.berrycollege.fragments.Quit_Fragment;
import com.populace.berrycollege.fragments.TheCage_Fragment;
import com.populace.berrycollege.managers.ParseDataManager;


public class Physical_Activity extends AppCompatActivity implements OnClickListener{
    private ImageView dailyWorkout, theCage, activities, intrmural, physical, quite_tab;
    private LinearLayout _dailyWorkout, _theCage, _activities, _intrmural, _quiteTab;
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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Physical_light)));



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
        _dailyWorkout = (LinearLayout) findViewById(R.id.ivdaily_workout);
        _theCage = (LinearLayout) findViewById(R.id.ivthe_cage);

        _activities = (LinearLayout) findViewById(R.id.ivactivities);
        _intrmural = (LinearLayout) findViewById(R.id.ivintramural);
        physical = (ImageView) findViewById(R.id.physical_image);
        _quiteTab = (LinearLayout) findViewById(R.id.ivquite_tab);
    }

    private void InitializeListeners() {
        _dailyWorkout.setOnClickListener(this);
        _theCage.setOnClickListener(this);
        _activities.setOnClickListener(this);
        _intrmural.setOnClickListener(this);
        _quiteTab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivdaily_workout:
                _dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                _theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _quiteTab.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Daily_Workout_Fragment(),null).commit();
                break;

            case R.id.ivthe_cage:
                _dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _theCage.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                _activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _quiteTab.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new TheCage_Fragment(),null).commit();
                break;

            case R.id.ivactivities:
                _dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _activities.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                _intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _quiteTab.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Activities_Fragment(),null).commit();
                break;

            case R.id.ivintramural:
                _dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                _quiteTab.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                getFragmentManager().beginTransaction().replace(R.id.container_physical,new Intramural(),null).commit();
                break;

            case R.id.ivquite_tab:
                _dailyWorkout.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _theCage.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _activities.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _intrmural.setBackgroundColor(getResources().getColor(R.color.Physical_light));
                _quiteTab.setBackgroundColor(getResources().getColor(R.color.Physical_Dark));
                getFragmentManager().beginTransaction().replace(R.id.container_physical, new Quit_Fragment(), null).commit();
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

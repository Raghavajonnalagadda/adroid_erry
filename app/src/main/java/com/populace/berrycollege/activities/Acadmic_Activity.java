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
import com.populace.berrycollege.fragments.CalenderFragment;
import com.populace.berrycollege.fragments.ClubFragment;
import com.populace.berrycollege.fragments.LibraryFragment;
import com.populace.berrycollege.fragments.TutoringFragment;
import com.populace.berrycollege.managers.ParseDataManager;


public class Acadmic_Activity extends AppCompatActivity implements OnClickListener {
    private ImageView calender,clubs,library,tutoring,academic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_acadmic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        findViewByIds();
        InitializeListeners();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Acadmic_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        getFragmentManager().beginTransaction().add(R.id.container,new CalenderFragment(),null).commit();
        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(3);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();



        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageOnLoading(R.drawable.background_nutrition)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))

                .build();

// Load and display image
        imageLoader.displayImage(imageUri, academic, options, new ImageLoadingListener() {
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
                academic.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void InitializeListeners() {
        calender.setOnClickListener(this);
        clubs.setOnClickListener(this);
        library.setOnClickListener(this);
        tutoring.setOnClickListener(this);
    }

    private void findViewByIds() {
        calender=(ImageView)findViewById(R.id.ivcalander);
        clubs=(ImageView)findViewById(R.id.ivclubs);
        library=(ImageView)findViewById(R.id.ivlibrary);
        tutoring=(ImageView)findViewById(R.id.ivtutoring);
        academic=(ImageView)findViewById(R.id.acadmic_image);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivcalander:
                calender.setBackgroundColor(getResources().getColor(R.color.acadmic_dark));
                clubs.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                library.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                tutoring.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                getFragmentManager().beginTransaction().replace(R.id.container, new CalenderFragment(), null).commit();
                break;

            case R.id.ivclubs:
                calender.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                clubs.setBackgroundColor(getResources().getColor(R.color.acadmic_dark));
                library.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                tutoring.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                getFragmentManager().beginTransaction().replace(R.id.container,new ClubFragment(),null).commit();
                break;

            case R.id.ivlibrary:
                calender.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                clubs.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                library.setBackgroundColor(getResources().getColor(R.color.acadmic_dark));
                tutoring.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                getFragmentManager().beginTransaction().replace(R.id.container,new LibraryFragment(),null).commit();
                break;

            case R.id.ivtutoring:
                calender.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                clubs.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                library.setBackgroundColor(getResources().getColor(R.color.acadmic_light));
                tutoring.setBackgroundColor(getResources().getColor(R.color.acadmic_dark));
                getFragmentManager().beginTransaction().replace(R.id.container,new TutoringFragment(),null).commit();
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


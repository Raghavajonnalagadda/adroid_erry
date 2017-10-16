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
import android.widget.TextView;

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
import com.populace.berrycollege.database.MydietDatabase;
import com.populace.berrycollege.fragments.CaloryCounter;
import com.populace.berrycollege.fragments.Diets;
import com.populace.berrycollege.fragments.Menu;
import com.populace.berrycollege.fragments.MyDiet;
import com.populace.berrycollege.managers.ParseDataManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Nutrition_Activity extends AppCompatActivity implements OnClickListener {
    public static String dateStrng;
    TextView date_txt;
    MydietDatabase dbHelper;
    Date date;
    private LinearLayout _diet, _menu, _caloryCounter, _myDiete;
    private ImageView diete, menu, caloryCounter, myDiete, back_arrow, frwd_arrow, nutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nutrition);
        findViewByIds();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Nutrition_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        InitializeListeners();
        dbHelper = new MydietDatabase(this);
        dbHelper.createTable();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nutrition_light)));
        getFragmentManager().beginTransaction().add(R.id.container_nutrition, new Diets(), null).commit();
        getDate();

        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(1);
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
        imageLoader.displayImage(imageUri, nutrition, options, new ImageLoadingListener() {
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
                nutrition.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        _diet = (LinearLayout) findViewById(R.id.ivdiet);
        _menu = (LinearLayout) findViewById(R.id.ivmenu);
        _caloryCounter = (LinearLayout) findViewById(R.id.ivcalory_counter);
        _myDiete = (LinearLayout) findViewById(R.id.ivmydiet);
        back_arrow=(ImageView)findViewById(R.id.back_btn);
        frwd_arrow=(ImageView)findViewById(R.id.farword_btn);
        date_txt=(TextView)findViewById(R.id.tvdate);
        nutrition = (ImageView) findViewById(R.id.Nutrition_image);


    }

    private void InitializeListeners() {
        _diet.setOnClickListener(this);
        _menu.setOnClickListener(this);
        _caloryCounter.setOnClickListener(this);
        _myDiete.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
        frwd_arrow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivdiet:
                back_arrow.setVisibility(View.INVISIBLE);
                frwd_arrow.setVisibility(View.INVISIBLE);
                date_txt.setVisibility(View.INVISIBLE);
                _diet.setBackgroundColor(getResources().getColor(R.color.nutrition_Dark));
                _menu.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _caloryCounter.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _myDiete.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new Diets(), null).commit();
                break;

            case R.id.ivmenu:
                back_arrow.setVisibility(View.INVISIBLE);
                frwd_arrow.setVisibility(View.INVISIBLE);
                date_txt.setVisibility(View.INVISIBLE);
                _diet.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _menu.setBackgroundColor(getResources().getColor(R.color.nutrition_Dark));
                _caloryCounter.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _myDiete.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new Menu(), null).commit();

                break;

            case R.id.ivcalory_counter:
                back_arrow.setVisibility(View.INVISIBLE);
                frwd_arrow.setVisibility(View.INVISIBLE);
                date_txt.setVisibility(View.INVISIBLE);
                _diet.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _menu.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _caloryCounter.setBackgroundColor(getResources().getColor(R.color.nutrition_Dark));
                _myDiete.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new CaloryCounter(), null).commit();
                break;

            case R.id.ivmydiet:
                back_arrow.setVisibility(View.VISIBLE);
                frwd_arrow.setVisibility(View.VISIBLE);
                date_txt.setVisibility(View.VISIBLE);
                _diet.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _menu.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _caloryCounter.setBackgroundColor(getResources().getColor(R.color.nutrition_light));
                _myDiete.setBackgroundColor(getResources().getColor(R.color.nutrition_Dark));
                getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new MyDiet(), null).commit();


                break;

            case R.id.back_btn:
//                Intent intent1 = new Intent("custom-event-name1");
//                intent1.putExtra("position1", 1000);
//                // sendBroadcast(intent);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);

                Calendar cal = Calendar.getInstance();
                cal.setTime(date); // convert your date to Calendar object
                int daysToDecrement = -1;
                cal.add(Calendar.DATE, daysToDecrement);
                date = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ");
                dateStrng = sdf.format(date);
                date_txt.setText(dateStrng);
               getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new MyDiet(), null).commit();
//                Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.container_nutrition);
//                if (currentFragment instanceof MyDiet)
//            {
//                FragmentTransaction fragTransaction =   (this).getFragmentManager().beginTransaction();
//                fragTransaction.detach(currentFragment);
//                fragTransaction.attach(currentFragment);
//                fragTransaction.commit();
//            }




            break;

            case R.id.farword_btn:
//                Intent intent = new Intent("custom-event-name");
//                intent.putExtra("position", 1000);
//                // sendBroadcast(intent);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date); // convert your date to Calendar object
                int daysToIncrement = +1;
                cal1.add(Calendar.DATE, daysToIncrement);
                date = cal1.getTime();
                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd, yyyy ");
                dateStrng = sdf1.format(date);
                date_txt.setText(dateStrng);
                getFragmentManager().beginTransaction().replace(R.id.container_nutrition, new MyDiet(), null).commit();


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

    private void getDate() {
       // date = System.currentTimeMillis();
        date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy ");
        dateStrng = sdf.format(date);
        date_txt.setText(dateStrng);


    }

}
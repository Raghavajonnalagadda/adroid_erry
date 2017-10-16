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
import com.populace.berrycollege.fragments.Bcvs;
import com.populace.berrycollege.fragments.Definition;
import com.populace.berrycollege.fragments.HandbookFragment;
import com.populace.berrycollege.fragments.Quote;
import com.populace.berrycollege.managers.ParseDataManager;


public class Character_Activity extends AppCompatActivity implements OnClickListener {
    private ImageView definition, bcvs, quote,handbook,character;

    private LinearLayout _definition, _bcvs, _quote, _handbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_character);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(Character_Activity.this));
        ImageLoader imageLoader = ImageLoader.getInstance();
        findViewByIds();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        InitializeListeners();
        getFragmentManager().beginTransaction().add(R.id.container1,new Definition(),null).commit();

        ParseObject obj= ParseDataManager.sharedDataManager(this).images.get(0);
        System.out.println("Image for banner:"+obj);
        ParseFile fl = (ParseFile) obj.get("section_image");
        String imageUri = fl.getUrl();


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.character_light)));


        //store image in cache memopry
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading()
                .showImageOnLoading(R.drawable.background__character)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .delayBeforeLoading(1000)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();

// Load and display image
        imageLoader.displayImage(imageUri, character, options, new ImageLoadingListener() {
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
                character.setImageBitmap(loadedImage);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }


        });
    }

    private void findViewByIds() {
        _definition = (LinearLayout) findViewById(R.id.definition);
        _bcvs = (LinearLayout) findViewById(R.id.bcvs);
        _quote = (LinearLayout) findViewById(R.id.quote);
        _handbook = (LinearLayout) findViewById(R.id.handbook);
        character = (ImageView) findViewById(R.id.character_image);

    }

    private void InitializeListeners() {
        _definition.setOnClickListener(this);
        _bcvs.setOnClickListener(this);
        _quote.setOnClickListener(this);
        _handbook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.definition:
                _definition.setBackgroundColor(getResources().getColor(R.color.character_dark));
                _bcvs.setBackgroundColor(getResources().getColor(R.color.character_light));
                _quote.setBackgroundColor(getResources().getColor(R.color.character_light));
                _handbook.setBackgroundColor(getResources().getColor(R.color.character_light));
                getFragmentManager().beginTransaction().replace(R.id.container1,new Definition(),null).commit();
                break;

            case R.id.bcvs:
                _definition.setBackgroundColor(getResources().getColor(R.color.character_light));
                _bcvs.setBackgroundColor(getResources().getColor(R.color.character_dark));
                _quote.setBackgroundColor(getResources().getColor(R.color.character_light));
                _handbook.setBackgroundColor(getResources().getColor(R.color.character_light));
                getFragmentManager().beginTransaction().replace(R.id.container1,new Bcvs(),null).commit();
                break;

            case R.id.quote:
                _definition.setBackgroundColor(getResources().getColor(R.color.character_light));
                _bcvs.setBackgroundColor(getResources().getColor(R.color.character_light));
                _quote.setBackgroundColor(getResources().getColor(R.color.character_dark));
                _handbook.setBackgroundColor(getResources().getColor(R.color.character_light));
                getFragmentManager().beginTransaction().replace(R.id.container1,new Quote(),null).commit();
                break;

            case R.id.handbook:
                _definition.setBackgroundColor(getResources().getColor(R.color.character_light));
                _bcvs.setBackgroundColor(getResources().getColor(R.color.character_light));
                _quote.setBackgroundColor(getResources().getColor(R.color.character_light));
                _handbook.setBackgroundColor(getResources().getColor(R.color.character_dark));
                getFragmentManager().beginTransaction().replace(R.id.container1,new HandbookFragment(),null).commit();
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
package com.populace.berrycollege.rollcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.populace.berrycollege.R;

public class SupportTransformerActivity extends AppCompatActivity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);

	        final int height = dm.heightPixels;
	        final int width = dm.widthPixels;
	        if(height < 800 && width < 480){
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        }

		 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
	        /*ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true);
	        actionBar.setDisplayShowHomeEnabled(true);
	        actionBar.setHomeButtonEnabled(true);*/
	    }
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    return true;
		}
	 	 @Override
			public boolean onOptionsItemSelected(MenuItem item) { 
			        switch (item.getItemId()) {
			        case android.R.id.home: 
			            this.finish();
			            return true;
			        }

			    return super.onOptionsItemSelected(item);
			}
		
}

package com.populace.berrycollege.rollcall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.populace.berrycollege.R;
import com.populace.berrycollege.activities.MainActivity;
import com.populace.berrycollege.activities.Social_Activity;
import com.populace.berrycollege.managers.ParseDataManager;

import java.io.File;

import soundcloud.android.crop.Crop;

/**
 * An activity representing a list of GTCRollCalls. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link GTCRollCallDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link GTCRollCallListFragment} and the item details (if present) is a
 * {@link GTCRollCallDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link GTCRollCallListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class GTCRollCallListActivity extends AppCompatActivity implements
		GTCRollCallListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
Activity ac;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rollcall_list);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		//getActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		 ImageView logo = (ImageView) findViewById(android.R.id.home);
	       // logo.setImageResource(R.drawable.trans);
	        ac=this;
	        if (findViewById(R.id.rollcall_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((GTCRollCallListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.rollcall_list))
					.setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link GTCRollCallListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(GTCRollCallDetailFragment.ARG_ITEM_ID, id);
			GTCRollCallDetailFragment fragment = new GTCRollCallDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rollcall_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.

			if(id!=null)
			{
				Intent detailIntent = new Intent(this,
						GTCRollCallDetailActivity.class);
				detailIntent.putExtra(GTCRollCallDetailFragment.ARG_ITEM_ID, id);
				startActivity(detailIntent);

			}
		}
	}

	Bitmap bitmap;
	private final int TAKE_PICTURE = 1;
	private Uri imageUri;
	private static final int REQ_CODE_PICK_IMAGE = 100;
	private final static int PIC_CROP = 2;
	public void takePhoto(View view) {

		if(CheckIsConnectedToInternet(ac)){
			File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			if(photo.exists()){
				photo.delete();
			}
			imageUri = Uri.fromFile(photo);
			ParseDataManager.photoFunctionalityLaunch(this, imageUri);
        }
        else
        {
        	AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Sorry, Your device is not conntected to internet");
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	dialog.cancel();
//                    finish();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }




	}

	 public boolean CheckIsConnectedToInternet(Context _context) {
		   ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   if (connectivity != null) {
		     NetworkInfo[] info = connectivity.getAllNetworkInfo();
		     if (info != null)
		       for (int i = 0; i < info.length; i++)
		         if (info[i].getState() == NetworkInfo.State.CONNECTED) {
		           return true;
		         }

		   }
		   return false;
		 }



	/*
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    switch (requestCode) {
	    case Crop.REQUEST_CROP:
	    	if (resultCode == Activity.RESULT_OK) {

	    		Intent myIntent = new Intent(this, GTCRollCallDetailActivity.class);
	    		imageUri = Crop.getOutput(data);
	    		myIntent.putExtra("URI", imageUri.toString());
	    		startActivityForResult(myIntent,0);
	    	}break;
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	        	Uri selectedImage = imageUri;
		    	this.getContentResolver().notifyChange(selectedImage, null);
	            ContentResolver cr = this.getContentResolver();
	            try {

	                 bitmap = android.provider.MediaStore.Images.Media
	                 .getBitmap(cr, selectedImage);


	            } catch (Exception e) {
	                Log.e("Camera", e.toString());
	            }
	        	GTCDataManager.handleBitmap(bitmap, imageUri);
	        	GTCDataManager.photoCropFunctionality(this, imageUri);

	        }break;
	    case REQ_CODE_PICK_IMAGE:
	        if(resultCode == Activity.RESULT_OK){
	        	Uri selectedImage = data.getData();
	        	String[] filePathColumn = { MediaStore.Images.Media.DATA };
	        	 Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	        	 cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        	 String picturePath = cursor.getString(columnIndex); cursor.close();
	        	 bitmap = BitmapFactory.decodeFile(picturePath);
	            try {
	        		 URI uri = new URI(imageUri.toString());
	        		 FileOutputStream out = new FileOutputStream(new File(uri));
	        	     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
	        	     selectedImage = imageUri;
	        	     bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
	        	 }catch(Exception e){
	        		 e.printStackTrace();
	        	 }
	            GTCDataManager.photoCropFunctionality(this, imageUri);
	        }break;

	    }

	}*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Object obj = ParseDataManager.handlePhotoFunctionalityResult(this, requestCode, resultCode, data, imageUri);
	    if(obj instanceof Bitmap){
	    	bitmap = (Bitmap)obj;
	    }else if(obj instanceof Uri){
	    	imageUri = (Uri)obj;
	    }
	    switch (requestCode) {
	    case Crop.REQUEST_CROP:
	    	if (resultCode == Activity.RESULT_OK) {
	    		Intent myIntent = new Intent(this, GTCRollCallDetailActivity.class);
	    		imageUri = Crop.getOutput(data);
	    		myIntent.putExtra("URI", imageUri.toString());
	    		startActivityForResult(myIntent,0);
	    	}break;
	    }
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		NavUtils.navigateUpTo(this, new Intent(this,
				Social_Activity.class));
	}
}

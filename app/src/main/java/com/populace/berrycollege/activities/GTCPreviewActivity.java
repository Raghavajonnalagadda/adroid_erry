package com.populace.berrycollege.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.ParseDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GTCPreviewActivity extends AppCompatActivity {
	ImageButton btnDone;
    ImageButton btnRotate;
    ImageButton btnFlip;
    ImageView imagePreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static Uri imageUri;
    Bitmap bitmap;
    
    public final static String TAG = "com.populace.GTC";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		imageUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
		File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
		imagePreview = (ImageView) findViewById(R.id.imagePreview);
		btnDone =(ImageButton) findViewById(R.id.btnDone);
		btnDone.setOnClickListener( new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			        	File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
				        if (pictureFile == null){
				        	GTCPreviewActivity.this.finish();
				            //Log.d(TAG, "Error creating media file, check storage permissions: " + e.getMessage());
				            return;
				        }
			        	 try {
			        		 
			        		
			                URI uri = new URI(pictureFile.getAbsolutePath());
			       		 	FileOutputStream fos = new FileOutputStream(pictureFile);
			       		 	bitmap.compress(CompressFormat.JPEG, 100, fos);
			 	           
			 	        } catch (FileNotFoundException e) {
			 	            Log.d(TAG, "File not found: " + e.getMessage());
			 	        } catch (IOException e) {
			 	            Log.d(TAG, "Error accessing file: " + e.getMessage());
			 	        } catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	 GTCPreviewActivity.this.setResult(Activity.RESULT_OK);
			        	GTCPreviewActivity.this.finish();
			        }
		});
		btnRotate =(ImageButton) findViewById(R.id.btnRotate);
		btnRotate.setOnClickListener( new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bitmap = ParseDataManager.rotateImage(bitmap, 90);
	        	imagePreview.setImageBitmap(bitmap);
	        }
		});
		btnFlip =(ImageButton) findViewById(R.id.btnFlip);
		btnFlip.setOnClickListener( new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	bitmap = ParseDataManager.flipImage(bitmap);
	        	imagePreview.setImageBitmap(bitmap);
	        }
		});
		BitmapWorkerTask task = new BitmapWorkerTask(GTCPreviewActivity.this, imagePreview,this);
	    task.execute(pictureFile.getAbsolutePath());
	}
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = null;
		File mediaFile = null;
		if(imageUri == null){
	    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
	    
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	        
	    }
	 // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
		}else{
			try{
			URI uri = new URI(imageUri.toString());
			mediaFile = new File(uri);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	    

	    return mediaFile;
	}
	static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private String data = "";
	    private Context context;
	    private final WeakReference<GTCPreviewActivity> activity;
	    public BitmapWorkerTask(Context ctx,ImageView imageView, GTCPreviewActivity act) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	        context = ctx;
	        activity = new WeakReference<GTCPreviewActivity>(act);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(String... params) {
	        data = params[0];
	        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
    	    Point size = new Point();
    	    display.getSize(size);
    	    int width = size.x;
    	    int height = size.y;
    	    
	        return decodeSampledBitmapFromResource(data, width, height);
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bmp) {
	        if (imageViewReference != null && bmp != null) {
	            final ImageView imageView = imageViewReference.get();
	            final GTCPreviewActivity act= activity.get();
	            act.bitmap = bmp;
	            if (imageView != null) {
	                imageView.setImageBitmap(bmp);
	            }
	        }
	    }
	    public static Bitmap decodeSampledBitmapFromResource(String path,
	            int reqWidth, int reqHeight) {

	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);

	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	        // Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeFile(path, options);
	    }
	    public static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gtcpreview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
}

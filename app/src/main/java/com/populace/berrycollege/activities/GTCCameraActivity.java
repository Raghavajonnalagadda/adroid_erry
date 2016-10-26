package com.populace.berrycollege.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.ParseDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GTCCameraActivity extends AppCompatActivity {
	private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    static Uri imageUri;
    public static int currentCameraId = -1;
    ImageButton btnToggle;
    ImageButton btnCapture;
    FrameLayout preview;
    Bitmap bitmap;
    public static final int PREVIEW = 4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		// Show the Up button in the action bar.
				/*getActionBar().setDisplayHomeAsUpEnabled(true);
				 getActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
				 ImageView logo = (ImageView) findViewById(android.R.id.home);
			        logo.setImageResource(R.drawable.trans);*/
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				GTCCameraActivity.currentCameraId = -1;
		imageUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
		// Add a listener to the Capture button
		btnCapture = (ImageButton) findViewById(R.id.btnCapture);
		btnCapture.setOnClickListener(
		    new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	if(mCamera != null){
		        		//get an image from the camera
		        		mCamera = mPreview.mCamera;
		        		mCamera.takePicture(null, null, mPicture);
		        	}
		        }
		    }
		);
		btnToggle =(ImageButton) findViewById(R.id.btnToggle);
		btnToggle.setOnClickListener(
			    new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			            toggleCamera();
			            mCamera = mPreview.mCamera;
			        }
			    }
			);
		
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);


	}
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	mPreview.setVisibility(View.GONE);
            preview.removeAllViews();
            mPreview = null;
            btnCapture.setVisibility(View.GONE);
            btnToggle.setVisibility(View.GONE);
            
	    	System.gc();
	    	File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	    	FileOutputStream fos = null;
	    	    try {
	                fos = new FileOutputStream(pictureFile);
	                fos.write(data);
	                
	            } catch (FileNotFoundException e) {
	                Log.d(TAG, "File not found: " + e.getMessage());
	            } catch (IOException e) {
	                Log.d(TAG, "Error accessing file: " + e.getMessage());
	            }finally{
	            	try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	    	    
	    	    bitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
	            bitmap = ParseDataManager.imageOreintationValidator(bitmap, pictureFile.getAbsolutePath());
	            CameraInfo info =
			            new CameraInfo();
			     Camera.getCameraInfo(GTCCameraActivity.currentCameraId, info);
			     if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			    	 bitmap = ParseDataManager.flipImage(bitmap);
			     }
			     try {
					fos = new FileOutputStream(pictureFile);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
					bitmap.recycle();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     Intent intent = new Intent(GTCCameraActivity.this,GTCPreviewActivity.class);
			     intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			     GTCCameraActivity.this.startActivityForResult(intent, PREVIEW);
	            
	            
	    }
	};
	
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gtccamera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	    	c = Camera.open(); // attempt to get a Camera instance
	    	
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	public void toggleCamera(){
		if(Camera.getNumberOfCameras() > 1){
	    mPreview.toggleCamera();
		}
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == PREVIEW){
	    this.setResult(resultCode);
	    this.finish();
		}
	}
	public final static String TAG = "com.populace.GTC";
	/** A basic Camera preview class */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	    private SurfaceHolder mHolder;
	    private Camera mCamera;
	    private Context mContext;
	    private Parameters mParameters;
	    public CameraPreview(Context context, Camera camera) {
	        super(context);
	        mContext = context;
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	    	toggleCamera();
	    }
	    public void onOrientationChanged(int orientation) {
		     CameraInfo info =
		            new CameraInfo();
		     Camera.getCameraInfo(GTCCameraActivity.currentCameraId, info);
		     orientation = (orientation + 45) / 90 * 90;
		     int rotation = 0;
		     if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
		         rotation = (info.orientation - orientation + 360) % 360;
		     } else {  // back-facing camera
		         rotation = (info.orientation + orientation) % 360;
		     }
		     mParameters.setRotation(rotation);
		     mCamera.setParameters(mParameters);
		 }
	    public void toggleCamera(){
	    	try {
	    		if(mCamera != null){
	    			mCamera.stopPreview();
	    			mCamera.release();
	    			mCamera = null;
	    		}
		    for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
		    	CameraInfo camInfo = new CameraInfo();
		        Camera.getCameraInfo(camNo, camInfo);
		        
		        if (camNo != currentCameraId) {
		        	GTCCameraActivity.currentCameraId  = camNo;
		        	break;
		        }
		    }
		    
		    mCamera = Camera.open(GTCCameraActivity.currentCameraId);
		    mParameters = mCamera.getParameters();
		    mCamera.setPreviewDisplay(mHolder);
            this.setCameraDisplayOrientation(mContext,GTCCameraActivity.currentCameraId , mCamera);
		    //mCamera.setDisplayOrientation(90);
		    this.onOrientationChanged(0);
		    mCamera.startPreview();
		    Log.d(TAG + "Restarting camera", ""+GTCCameraActivity.currentCameraId);
	    } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
	    }
	    public void setCameraDisplayOrientation(Context context,
	            int cameraId, Camera camera) {
	        CameraInfo info = new CameraInfo();
	        Camera.getCameraInfo(cameraId, info);
	        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay()
	                .getRotation();
	        int degrees = 0;
	        switch (rotation) {
	            case Surface.ROTATION_0: degrees = 0; break;
	            case Surface.ROTATION_90: degrees = 90; break;
	            case Surface.ROTATION_180: degrees = 180; break;
	            case Surface.ROTATION_270: degrees = 270; break;
	        }

	        int result;
	        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	            result = (info.orientation + degrees) % 360;
	            result = (360 - result) % 360;  // compensate the mirror
	        } else {  // back-facing
	            result = (info.orientation - degrees + 360) % 360;
	        }
	        camera.setDisplayOrientation(result);
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // empty. Take care of releasing the Camera preview in your activity.
	    	if(mCamera != null){
    			mCamera.stopPreview();
    			mCamera.release();
    			mCamera = null;
    		}
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	            
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	        	
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
	    }
	}
}

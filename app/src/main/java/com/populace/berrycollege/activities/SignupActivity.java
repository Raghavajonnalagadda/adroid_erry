package com.populace.berrycollege.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.IRegisterCallback;
import com.populace.berrycollege.managers.PIBitmapUtils;
import com.populace.berrycollege.managers.PIThemeUtils;
import com.populace.berrycollege.managers.ParseDataManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soundcloud.android.crop.Crop;


public class SignupActivity extends AppCompatActivity  implements OnItemSelectedListener{
	ImageView logo_image;
	EditText fname_edittext ,lname_edittext, email_edittext , password_edittext , confirm_edittext ;
	TextView submit_button;
	Activity ac;
	ProgressDialog dialog;
	Bitmap	logo_bmp ;
	ImageView back_button;
	private String year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_screen);
		ac=this;
		 /* getActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
			 ImageView logo = (ImageView) findViewById(android.R.id.home);
		        logo.setImageResource(R.drawable.trans);
	        getActionBar().setDisplayHomeAsUpEnabled(true);*/
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		logo_image=(ImageView)findViewById(R.id.logo_image);
		fname_edittext=(EditText)findViewById(R.id.fname_edittext);
		lname_edittext=(EditText)findViewById(R.id.lname_edittext);
		email_edittext=(EditText)findViewById(R.id.email_edittext);
		password_edittext=(EditText)findViewById(R.id.password_edittext);
		confirm_edittext=(EditText)findViewById(R.id.confirm_edittext);
		//back_button=(ImageView)findViewById(R.id.back_button);
		submit_button=(TextView)findViewById(R.id.submit_button);
		// Spinner element
		final Spinner spinner = (Spinner) findViewById(R.id.spinner);

		// Spinner click listener
		spinner.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("2014");
		categories.add("2015");
		categories.add("2016");
		categories.add("2017");
		categories.add("2018");


		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);


		logo_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				takePhoto();
			}
		});
		submit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String fname=fname_edittext.getText().toString();
				String lname=lname_edittext.getText().toString();
				String email=email_edittext.getText().toString();
				String password = password_edittext.getText().toString();
				String confirm_psw=confirm_edittext.getText().toString();
				String year=spinner.getSelectedItem().toString();

				if(fname.length()==0)
				{
					showAlertSimple("Please enter first name");
					return;
				}
				else if(lname.length()==0)
				{
					showAlertSimple("Please enter last name");
					return;
				}
				else if(year==null)
				{
					showAlertSimple("Please select Year");
					return;
				}
				else if(email.length()==0)
				{
					showAlertSimple("Please enter email");
					return;
				}
				else if(password.length()==0)
				{
					showAlertSimple("Please enter password");
					return;
				}
				else if(confirm_psw.length()==0)
				{
					showAlertSimple("Please enter confirm password");
					return;
				}
				else if(!password.equals(confirm_psw))
				{
					showAlertSimple("Password and confirm password should be same");
					return;
				}
				else if(password.length()<6)
				{
					showAlertSimple("Password length should be atleast 6 character long");
					return;
				}
				else if(logo_bmp==null)
				{
					showAlertSimple("Please add a profile pic");
					return;
				}
				else if(!emailValidator(email.trim()))
				{
					showAlertSimple("Please enter a valid email address");
					return;
				}
				else if(!CheckIsConnectedToInternet(ac))
				{
					showAlertSimple(GlobalClass.internet_message);
					return;
				}
				else
				{
					signupUser(fname, lname, email, password, year);
				}


			}
		});

	}

	Bitmap bitmap;
	private final int TAKE_PICTURE = 1;
	private Uri imageUri;
	private static final int REQ_CODE_PICK_IMAGE = 100;
	private final static int PIC_CROP = 2;
	public void takePhoto() {

		File photo = new File(Environment.getExternalStorageDirectory(),"Pic.jpg");
		if(photo.exists()){
			photo.delete();
		}
		imageUri = Uri.fromFile(photo);
		ParseDataManager.photoFunctionalityLaunch(ac, imageUri);


	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		File photo = new File(Environment.getExternalStorageDirectory(),"Pic.jpg");

		imageUri = Uri.fromFile(photo);
		System.out.println("Andy checking crop pic bug " + data);
		try {
			Object obj = ParseDataManager.handlePhotoFunctionalityResult(this, requestCode, resultCode, data, imageUri);
			if(obj instanceof Bitmap){
				bitmap = (Bitmap)obj;
			}else if(obj instanceof Uri){
				imageUri = (Uri)obj;
			}
			switch (requestCode) {
				case Crop.REQUEST_CROP:
					if (resultCode == Activity.RESULT_OK) {


//			    		Intent myIntent = new Intent(this, NearbyActivity.class);
//			    		imageUri = Crop.getOutput(data);
//			    		myIntent.putExtra("URI", imageUri.toString());
//			    		startActivityForResult(myIntent,0);
						showOnImageView(imageUri);

					}break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	public void showOnImageView(Uri image_uri)
	{
//	    	Parse.enableLocalDatastore(this);
		Bitmap bitmap=null;
		try
		{
			bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , image_uri);
		}
		catch (Exception e)
		{
			//handle exception
		}
		logo_bmp=bitmap;
		logo_image.setImageBitmap(bitmap);



	}
	public boolean emailValidator(String email)
	{
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public void showAlertSimple(final String  message)
	{


		AlertDialog.Builder builder1 = new AlertDialog.Builder(ac);
		builder1.setMessage(""+message+"");
		builder1.setCancelable(true);
		builder1.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}
	public void signupUser(final String fname,final String lname,final String email,final String password,final String year)
	{


		// Compress image to lower quality scale 1 - 100
		ByteArrayOutputStream tstream=null;
		Bitmap thumbBmp =null;
		try {
			thumbBmp = PIThemeUtils.ScaleImage(logo_bmp, 87, 87, true);
			tstream = new ByteArrayOutputStream();
			if(thumbBmp != null){
				thumbBmp = PIThemeUtils.CropBitmap(thumbBmp, 0, 0, 86, 86);
				thumbBmp = PIBitmapUtils.getRoundedCornerBitmap(thumbBmp, 12);
				thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, tstream);
			}


		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Andy image size "+e);
		}
		byte[] image = tstream.toByteArray();
		// Create the ParseFile
		final ParseFile imagefile = new ParseFile("main_"+email.trim()+".png", image);
		final ParseFile thumbfile = new ParseFile("thumb_"+email.trim()+".png", image);
		final ProgressDialog dialog = ParseDataManager.showProgress(ac, "Registration", "Registering...");
		imagefile.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				thumbfile.saveInBackground();
				ParseDataManager.sharedDataManager(ac).register(email.trim(), password, fname, lname, year, imagefile, thumbfile, new IRegisterCallback() {

					@Override
					public void onRegister(final boolean success, final ParseException e, ParseUser user) {
//				Crashlytics.log(Log.INFO, "STS", "REgister callback called...");

						if (SignupActivity.this != null) {
							SignupActivity.this.runOnUiThread(new Runnable() {

								@Override
								public void run() {

									dialog.dismiss();

									if (success) {
										final ParseUser user = new ParseUser();
										System.gc();
										finish();
										Intent myIntent = new Intent(SignupActivity.this, LoginScreen.class);
										startActivity(myIntent);
										showToastMessage("You have registered successfully");



									} else {
										ParseDataManager.showAlert(ac, "Warning", e.getMessage());
//                                showAlertSimple("Email already exists please choose diffrent");
										Log.e("Exception", " ", e);
									}
								}
							});
						} else {
							dialog.dismiss();
						}
					}

				});
			}
		});





	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}
	public void showToastMessage(String message)
	{
		Toast.makeText(ac,message,
				Toast.LENGTH_SHORT).show();

	}
	public void noInternet()
	{
		Toast.makeText(ac,GlobalClass.internet_message,
				Toast.LENGTH_SHORT).show();
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// app icon in action bar clicked; goto parent activity.
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// On selecting a spinner item


		year = parent.getItemAtPosition(position).toString();
		// Showing selected spinner item

	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
}


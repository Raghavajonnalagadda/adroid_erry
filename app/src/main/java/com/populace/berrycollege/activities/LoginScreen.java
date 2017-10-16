package com.populace.berrycollege.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.ILoginCallback;
import com.populace.berrycollege.managers.ParseDataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {
	EditText email_txt ,password_txt;
	Button login_button , cancel_button ,signUp;
	Activity ac;
	ProgressDialog dialog;
	Bitmap	logo_bmp ;
	ImageView back_button;
	String fname,lname;

    public static String trimNameForImage(String name) {

        int pos = -1;
        char arr[] = name.toCharArray();

        for (int i = 0; i < arr.length; i++) {

            if (Character.isLetter(arr[i]) || Character.isDigit(arr[i])) {
                break;
            } else {
                pos = i;
            }
        }
        String newname = name.substring(pos + 1);
        return newname;
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		ac=this;
	   //  getActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
		//ImageView logo = (ImageView) findViewById(android.R.id.home);

		       //logo.setImageResource(R.drawable.trans);
	       // getActionBar().setDisplayHomeAsUpEnabled(true);
	      //  getActionBar().setHomeButtonEnabled(true);
		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		email_txt=(EditText)findViewById(R.id.email_txt);
		password_txt=(EditText)findViewById(R.id.password_txt);
		//back_button=(ImageView)findViewById(R.id.back_button);
		cancel_button=(Button)findViewById(R.id.cancel_button);
		login_button=(Button)findViewById(R.id.login_button);
        signUp= (Button)findViewById(R.id.signUp_button);

        signUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginScreen.this,SignupActivity.class);
                startActivity(in);

            }
        });
		cancel_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email=email_txt.getText().toString().trim();
				String password=password_txt.getText().toString();

				if(email.length()==0)
				{
					showAlertSimple("Sorry, invalid login credentials");
				}
				else if(password.length()==0)
				{
					showAlertSimple("Sorry, invalid login credentials");
				}
				else if(!emailValidator(email))
				{
					showAlertSimple("Please enter a valid email");
				}
				else if(!CheckIsConnectedToInternet(ac))
				{
                    showAlertSimple(GlobalClass.internet_message);
                }
				else
				{
					loginUser(email, password);
				}

			}
		});

	}

    public boolean emailValidator(String email) {
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

    public void loginUser(final String email,final String password)
	{
		   final ProgressDialog dialog = ParseDataManager.showProgress(LoginScreen.this, "Authentication", "Logging in...");
		ParseDataManager.sharedDataManager(LoginScreen.this).login(email,password, new ILoginCallback(){

						@Override
						public void onLogin(final boolean success, final ParseException e, ParseUser user) {

							if(LoginScreen.this != null){
//								Crashlytics.log(Log.INFO, "STS", "Will try dismissing and presenting new activity...");
								LoginScreen.this.runOnUiThread(new Runnable(){

								@Override
								public void run() {
//									Crashlytics.log(Log.INFO, "STS", "Dismissing now...");
									dialog.dismiss();
									if(success){
//										Crashlytics.log(Log.INFO, "STS", "Presenting activity...");
										System.gc();
										Intent myIntent = new Intent(LoginScreen.this, MainActivity.class);
						              	startActivityForResult(myIntent, 0);

						              	finish();
									}else{

										 showAlertSimple("Account not exists "+e);
									}

								}

							});

                            }else{
//								Crashlytics.log(Log.INFO, "STS", "Activity is null...");

                                dialog.dismiss();
							}
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
            Toast.makeText(ac, message,
                    Toast.LENGTH_SHORT).show();

        }

    public void noInternet()
	    {
            Toast.makeText(ac, GlobalClass.internet_message,
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

	    void writing_profilepic_external(ParseFile file_this,String image_name)
	    {
	    	if(file_this!=null)
	    	{
                ParseFile file = file_this;

	    		try {
	    			if(file.getName() != null){
	    				String[] filenameTokens = file.getName().split("-");
	    				System.out.println("Andy files check this "+image_name);

                        String fullPath = "/data/data/" + ac.getPackageName() + "/files/"+image_name;

                        File file_check = new File(fullPath);

//	    				if(file_check.exists())
//	    				{
//	    					System.out.println("Andy data parse true");
//	    				}
//	    				else
//	    				{
                        System.out.println("Andy data parse false");
                        byte[] data = file.getData();
	    					FileOutputStream stream = ac.openFileOutput(image_name,Context.MODE_PRIVATE);
	    					stream.write(data);
	    					stream.close();

//	    				}

                    }	} catch (ParseException e1) {

                    e1.printStackTrace();
	    			} catch (FileNotFoundException e) {

                    e.printStackTrace();
	    			} catch (IOException e) {

                    e.printStackTrace();
                }
            }

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

}
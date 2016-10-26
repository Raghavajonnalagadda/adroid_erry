//package com.populace.berrycollege.activities;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.populace.berrycollege.R;
//import com.populace.berrycollege.managers.ILoginCallback;
//import com.populace.berrycollege.managers.ParseDataManager;
//
//
//public class GTCLoginActivity extends Activity {
//	EditText txtEmail;
//	ImageButton btnCancel;
//	ImageButton btnContinue;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
//		txtEmail = (EditText)findViewById(R.id.txtEmail);
//		btnCancel = (ImageButton)findViewById(R.id.btnCancel);
//		btnContinue = (ImageButton)findViewById(R.id.btnContinue);
//		btnCancel.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				GTCLoginActivity.this.finish();
//
//			}
//
//		});
//
//		btnContinue.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				/*Runnable runnable = new Runnable() {
//					   @Override
//					   public void run() {
//						   throw new RuntimeException("This is a crash");
//					   }
//					};
//				Handler handler = new Handler();
//				handler.postDelayed(runnable, 60000);*/
//
//				final ProgressDialog dialog = ParseDataManager.showProgress(GTCLoginActivity.this, "Authentication", "Logging in...");
//				ParseDataManager.sharedDataManager(GTCLoginActivity.this).login(txtEmail.getText().toString(), new ILoginCallback(){
//
//					@Override
//					public void onLogin(final boolean success, final ParseException e, ParseUser user) {
//
//						if(GTCLoginActivity.this != null){
//
//							GTCLoginActivity.this.runOnUiThread(new Runnable(){
//
//							@Override
//							public void run() {
//
//								dialog.dismiss();
//								if(success){
//
//									System.gc();
//					            	Intent myIntent = new Intent(GTCLoginActivity.this, MainActivity.class);
//					            	startActivityForResult(myIntent, 0);
//								}else{
//
//									ParseDataManager.showAlert(GTCLoginActivity.this,"Warning", e.getMessage());
//								}
//
//							}
//
//						});
//
//						}else{
//
//
//							dialog.dismiss();
//						}
//					}
//
//				});
//
//			}
//
//		});
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.gtclogin, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		return super.onOptionsItemSelected(item);
//	}
//
//
//
//}

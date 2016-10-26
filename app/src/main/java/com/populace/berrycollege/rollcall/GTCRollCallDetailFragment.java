package com.populace.berrycollege.rollcall;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.PIBitmapUtils;
import com.populace.berrycollege.managers.PIThemeUtils;
import com.populace.berrycollege.managers.ParseDataManager;
import com.populace.berrycollege.models.GTCPhoto;
import com.populace.berrycollege.models.GTCUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single GTCRollCall detail screen. This fragment is
 * either contained in a {@link GTCRollCallListActivity} in two-pane mode (on
 * tablets) or a {@link GTCRollCallDetailActivity} on handsets.
 */
public class GTCRollCallDetailFragment extends Fragment {
	GTCPhoto photo;
	Bitmap bitmap = null;
	String fb_firstname;
	String fb_lname;
	ParseFile thumb=null;
	 Bitmap thumbBmp = null;
	  EditText txtComment; 
		 Button btnComment;
		   TextView btnAddComment;
		   boolean new_pic=false;
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public GTCRollCallDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
		SharedPreferences settings = getActivity().getSharedPreferences(getActivity().getPackageName(), 0);
   	 fb_firstname=settings.getString("FbFirstName","Empty");
   	 fb_lname=settings.getString("FbLastName","Empty");
   	
				if(thumbBmp == null){
					thumbBmp = PIBitmapUtils.getBitmap(fb_firstname + "_" + fb_lname, getActivity());
					
				   ByteArrayOutputStream tstream=null;
				
      		tstream = new ByteArrayOutputStream();
      		if(thumbBmp != null){
//      			thumbBmp = PIThemeUtils.CropBitmap(thumbBmp, 0, 0, 49, 49);
//      			thumbBmp = PIBitmapUtils.getRoundedCornerBitmap(thumbBmp, 0);
      			thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, tstream);
      		}
      	
          	thumb = new ParseFile(fb_firstname+"_"+fb_lname,tstream.toByteArray());
          	
				}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		String imageStr = getActivity().getIntent().getStringExtra("URI");
		if(CheckIsConnectedToInternet(getActivity())){
			for(int i = 0; i < menu.size(); i++){
				if(menu.getItem(i).getItemId() == R.id.publish){
//					try {
						SharedPreferences settings = getActivity().getSharedPreferences(getActivity().getPackageName(), 0);
				    	 fb_firstname=settings.getString("FbFirstName","Empty");
				    	 fb_lname=settings.getString("FbLastName","Empty");
				    	
		   					if(thumbBmp == null){
		   						thumbBmp = PIBitmapUtils.getBitmap(fb_firstname+"_"+fb_lname, getActivity());
		   						
		   					   ByteArrayOutputStream tstream=null;
		   					
		   	        		tstream = new ByteArrayOutputStream();
		   	        		if(thumbBmp != null){
//		   	        			thumbBmp = PIThemeUtils.CropBitmap(thumbBmp, 0, 0, 49, 49);
//		   	        			thumbBmp = PIBitmapUtils.getRoundedCornerBitmap(thumbBmp, 0);
		   	        			thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, tstream);
		   	        		}
		   	        	
		                   	thumb = new ParseFile(fb_firstname+"_"+fb_lname,tstream.toByteArray());
		   					}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
					
					if(imageStr == null)
					{
						menu.getItem(i).setVisible(false);
					}else{
						new_pic=true;
						menu.getItem(i).setOnMenuItemClickListener(new OnMenuItemClickListener(){

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								final ProgressDialog dialog = ParseDataManager.showProgress(getActivity(), "Publishing", "Publishing your post...");
								Bitmap mainBmp = PIThemeUtils.ScaleImage(bitmap, 560, 560, true);
								
								ByteArrayOutputStream mstream = new ByteArrayOutputStream();
								mainBmp.compress(Bitmap.CompressFormat.PNG, 100, mstream);
								ParseFile image = new ParseFile("Photo_"+ Math.random(), mstream.toByteArray());
								final GTCPhoto photo = ParseObject.create(GTCPhoto.class);
								photo.setCommentsCount(0);
								photo.setImage(image);
								photo.setImageLikes(0);
								photo.setProfile_pic(thumb);
								photo.setUser_name(fb_firstname+" "+fb_lname);
								photo.setUser(ParseDataManager.sharedDataManager(getActivity()).getCurrentUser());
								photo.saveInBackground(new SaveCallback(){

									@Override
									public void done(ParseException e) {
										dialog.dismiss();
										if(e  == null){
											if(txtComment.getText().toString().length()>0)
											{
												postCurrentComment(photo);	
											}
											else
											{
												getActivity().finish();
											}
										}else{
											ParseDataManager.showAlert(getActivity(), "Error", e.getMessage());
										}
										
									}
									
								});
								return false;
							}
							
						});
					}
				}
			}
        }
        else
        {
        	AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Sorry, Your device is not conntected to internet");
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
	
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	public void loadProfilePic()
	{
		SharedPreferences settings = getActivity().getSharedPreferences(getActivity().getPackageName(), 0);
	   	 fb_firstname=settings.getString("FbFirstName","Empty");
	   	 fb_lname=settings.getString("FbLastName","Empty");
	   	
					if(thumbBmp == null){
						thumbBmp = PIBitmapUtils.getBitmap(fb_firstname+"_"+fb_lname, getActivity());
						
					   ByteArrayOutputStream tstream=null;
					
	      		tstream = new ByteArrayOutputStream();
	      		if(thumbBmp != null){
//	      			thumbBmp = PIThemeUtils.CropBitmap(thumbBmp, 0, 0, 49, 49);
//	      			thumbBmp = PIBitmapUtils.getRoundedCornerBitmap(thumbBmp, 0);
	      			thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, tstream);
	      		}
	      	
	          	thumb = new ParseFile(fb_firstname+"_"+fb_lname,tstream.toByteArray());
	          	
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
	public void postCurrentComment(GTCPhoto photonew )
	{

		final GTCPhoto photo=photonew;
		if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())){
			photo.clean();
		final ProgressDialog dialog = ParseDataManager.sharedDataManager(getActivity()).showProgress(getActivity(), "Updating", "Posting comment");
		final ParseObject obj = ParseObject.create("Activity");
        obj.put("photoid",photo);
        obj.put("type","comment");
        obj.put("touser",photo.getUser());
        obj.put("fromuser",ParseDataManager.sharedDataManager(getActivity()).getCurrentUser());
        obj.put("comment",txtComment.getText().toString());
        photo.increment("commentCount",  1);
        obj.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException arg0) {
				ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
				q.whereEqualTo("objectId", photo.getObjectId());
				q.findInBackground(new FindCallback<GTCPhoto>(){

					@Override
					public void done(
							List<GTCPhoto> arg0,
							ParseException arg1) {
						if(arg0.size() > 0){
						arg0.get(0).saveInBackground(new SaveCallback(){
							@Override
							public void done(ParseException arg0) {
								btnComment.setText("" + photo.getCommentsCount());
								if(arg0 != null){
									ParseDataManager.showAlert(getActivity(), "Error", arg0.getMessage());
								}else{
									
								}
								dialog.dismiss();
								InputMethodManager inputManager = (InputMethodManager)
		                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
		                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
		                        InputMethodManager.HIDE_NOT_ALWAYS);
		                        txtComment.setText("");
								getActivity().finish();
							}
							
						});
						}
						
					}
					
				});
					
			
				
			}
        	
        });
	}else{
			ParseDataManager.showAlert(getActivity(), "Warning", "Not connected to internet");
	}
	
	}
	
	
	public void shareImage(Bitmap bitmap)
	{Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("image/jpeg");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
    		bitmap, "TNGTC", null);
    Uri imageUri =  Uri.parse(path);
    share.putExtra(Intent.EXTRA_STREAM, imageUri);
    startActivity(Intent.createChooser(share, "Select"));}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		
		String imageStr = getActivity().getIntent().getStringExtra("URI");
		if(imageStr != null){
		Uri selectedImage = Uri.parse(imageStr);
        getActivity().getContentResolver().notifyChange(selectedImage, null);
        ContentResolver cr = getActivity().getContentResolver();
        try {
        	
             bitmap = MediaStore.Images.Media
             .getBitmap(cr, selectedImage);
            
             
        } catch (Exception e) {
            Log.e("Camera", e.toString());
        }
		}
				if(bitmap == null){
					JSONObject map;
					try {
						map = new JSONObject(getActivity().getIntent().getStringExtra("PHOTO_DICT"));
						photo = new GTCPhoto();
						photo.load(map);
						ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
						q.whereEqualTo("objectId", photo.getObjectId());
						photo = q.find().get(0);
					} catch (JSONException e) {
						
						e.printStackTrace();
					} catch (ParseException e) {
						
						e.printStackTrace();
					}
				}
		View view = inflater.inflate(R.layout.rollcall_detail,
				container, false);
		final ListView lstComments;
		final TextView txtName,txtTime;
		
		  final Button btnLike;
		  final Button btnShareData;
	
		final ParseImageView imgProfile;
		final ParseImageView imgMain;
		LinearLayout containerDetails;
		LinearLayout comment_post_lay;
		  imgProfile = (ParseImageView)view.findViewById(R.id.imgProfile);
		  imgMain = (ParseImageView)view.findViewById(R.id.imgMain);
		  btnLike = (Button)view.findViewById(R.id.btnLike);
		  btnShareData = (Button)view.findViewById(R.id.btnShareData);
		  btnComment = (Button)view.findViewById(R.id.btnComment);
		  btnAddComment = (TextView)view.findViewById(R.id.btnAddComment);
		  txtName = (TextView)view.findViewById(R.id.txtName);
		  txtTime = (TextView)view.findViewById(R.id.txtTime);
		  lstComments = (ListView)view.findViewById(R.id.lstComments);
		  txtComment = (EditText)view.findViewById(R.id.txtComment);
		  containerDetails = (LinearLayout)view.findViewById(R.id.containerDetails);
		  comment_post_lay = (LinearLayout)view.findViewById(R.id.comment_post_lay);
		  if(bitmap != null){
//			  containerDetails.setVisibility(View.GONE);
//			  btnAddComment.setVisibility(View.GONE);
//			  txtComment.setVisibility(View.GONE);
//			  comment_post_lay.setVisibility(View.GONE);
			  imgMain.setImageBitmap(bitmap);
			  if(getUserProfilePic()!=null)
			  {
				  imgProfile.setImageBitmap(thumbBmp);	  
			  }
		  }
		  btnShareData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Andy checking bitmap empty or not "+bitmap);
				
//				if(bitmap != null)
//				{
//				try {
					shareImage(((BitmapDrawable)imgMain.getDrawable()).getBitmap());	
//				} catch (Exception e) {
					// TODO: handle exception
//				}
										
//				}
			}
		});
			btnAddComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					System.out.println("Andy checking post clicked ");
					loadProfilePic();
					if(new_pic)
					{
						

						if(txtComment.getText().toString().length()>0)
						{
							final ProgressDialog dialog = ParseDataManager.showProgress(getActivity(), "Publishing", "Publishing your post...");
							Bitmap mainBmp = PIThemeUtils.ScaleImage(bitmap, 560, 560, true);
							
							ByteArrayOutputStream mstream = new ByteArrayOutputStream();
							mainBmp.compress(Bitmap.CompressFormat.PNG, 100, mstream);
							ParseFile image = new ParseFile("Photo_"+ Math.random(), mstream.toByteArray());
							final GTCPhoto photo = ParseObject.create(GTCPhoto.class);
							photo.setCommentsCount(0);
							photo.setImage(image);
							photo.setImageLikes(0);
							photo.setProfile_pic(thumb);
							photo.setUser_name(fb_firstname+" "+fb_lname);
							photo.setUser(ParseDataManager.sharedDataManager(getActivity()).getCurrentUser());
							photo.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException e) {
									dialog.dismiss();
									if(e  == null){
										if(txtComment.getText().toString().length()>0)
										{
											postCurrentComment(photo);	
										}
										else
										{
											getActivity().finish();
										}
									}else{
										ParseDataManager.showAlert(getActivity(), "Error", e.getMessage());
									}
									
								}
								
							});
							
						}
						else
						{
							ParseDataManager.showAlert(getActivity(), "Warning", "Please add comment text");
						}
					
					}
					else
					{
						if(photo != null)
						  {
							  if(photo.getCreatedAt() != null)
							  {
								  if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())){
										
										if(txtComment.getText().toString().length()>0)
										{
											photo.clean();
											final ProgressDialog dialog = ParseDataManager.sharedDataManager(getActivity()).showProgress(getActivity(), "Updating", "Posting comment");
											final ParseObject obj = ParseObject.create("Activity");
						                    obj.put("photoid",photo);
						                    obj.put("type","comment");
						                    obj.put("touser",photo.getUser());
						                    obj.put("fromuser",ParseDataManager.sharedDataManager(getActivity()).getCurrentUser());
						                    obj.put("comment",txtComment.getText().toString());
						                    photo.increment("commentCount",  1);
						                    obj.saveInBackground(new SaveCallback(){

												@Override
												public void done(ParseException arg0) {
													ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
													q.whereEqualTo("objectId", photo.getObjectId());
													q.findInBackground(new FindCallback<GTCPhoto>(){

														@Override
														public void done(
																List<GTCPhoto> arg0,
																ParseException arg1) {
															if(arg0.size() > 0){
															arg0.get(0).saveInBackground(new SaveCallback(){
																@Override
																public void done(ParseException arg0) {
																	btnComment.setText("" + photo.getCommentsCount());
																	if(arg0 != null){
																		ParseDataManager.showAlert(getActivity(), "Error", arg0.getMessage());
																	}else{
																		
																	}
																	dialog.dismiss();
																	InputMethodManager inputManager = (InputMethodManager)
											                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); 
											                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
											                        InputMethodManager.HIDE_NOT_ALWAYS);
											                        txtComment.setText("");
																	((ItemsAdapter)lstComments.getAdapter()).items.add(obj);
																	ItemsAdapter adapter = new ItemsAdapter(getActivity(),((ItemsAdapter)lstComments.getAdapter()).items);
																	 lstComments.setAdapter(adapter);
																	
																	
																}
																
															});
															}
															
														}
														
													});
														
												
													
												}
						                    	
						                    });
										}
										else
										{
											ParseDataManager.showAlert(getActivity(), "Warning", "Please add comment text");
										}
								}else{
									  ParseDataManager.showAlert(getActivity(), "Warning", "Not connected to internet");
								}  
								  
							  }
						  }
						
					}
					  
					
					
					
				}

			});
		  if(photo != null){
			  if(photo.getCreatedAt() != null){
				  txtTime.setText(DateUtils.getRelativeDateTimeString(getActivity(),photo.getCreatedAt().getTime(),DateUtils.DAY_IN_MILLIS,DateUtils.YEAR_IN_MILLIS,0).toString().split(",")[0]);
				  }else if(photo.getDate("createdAtCopy") != null){
					  txtTime.setText(DateUtils.getRelativeDateTimeString(getActivity(),photo.getDate("createdAt").getTime(),DateUtils.DAY_IN_MILLIS,DateUtils.YEAR_IN_MILLIS,0).toString().split(",")[0]);
				  }else{
					  txtTime.setText("");
				  }
				  final ParseUser user = photo.getUser();
				  
				  if(user!=null){
					  
					  ParseQuery<ParseUser> pu = ParseQuery.getQuery(ParseUser.class);
					  pu.whereEqualTo("objectId", photo.getUser().getObjectId());
					  
					  pu.findInBackground(new FindCallback<ParseUser>(){

						@Override
						public void done(List<ParseUser> arg0, ParseException arg1) {
							if(arg0.size() > 0){
								ParseUser user = arg0.get(0);
								((GTCUser)user.get("profile")).fetchInBackground(new GetCallback<GTCUser>(){

									@Override
									public void done(final GTCUser u,
											ParseException arg1) {
										txtName.setText(u.getFirstName() + " " + u.getLastName());
										imgProfile.setParseFile(u.getThumbnail());
										imgProfile.loadInBackground(new GetDataCallback(){
											@Override
											public void done(byte[] arg0, ParseException arg1) {
												if(arg1 != null){
													
													
												}
											}
										});
									}

									
								});
							}
								
							
							
						}
						  
					  });
				  }
				  imgMain.setPlaceholder(getActivity().getResources().getDrawable(R.drawable.placeholder));
		  if(photo.getImage() != null){
		  imgMain.setParseFile(photo.getImage());
		  imgMain.loadInBackground(new GetDataCallback(){
			@Override
			public void done(byte[] arg0, ParseException arg1) {
				if(arg1 != null){
					
				}
				
			}
			  
		  });
		  }
		  	
		  btnLike.setText("");
		  	btnComment.setText("");
		  	
		  	ParseQuery<ParseObject> q = ParseQuery.getQuery("Activity");
			q.whereEqualTo("photoid", photo);
			q.whereEqualTo("type", "like");
			q.whereEqualTo("likeStatus", 1);
			q.countInBackground(new CountCallback(){

				@Override
				public void done(int arg0, ParseException arg1) {
					
					btnLike.setText(arg0 + "");
				}
				
			});
			ParseQuery<ParseObject> q1 = ParseQuery.getQuery("Activity");
			q1.whereEqualTo("photoid", photo);
			q1.whereEqualTo("type", "comment");
			q1.countInBackground(new CountCallback(){

				@Override
				public void done(int arg0, ParseException arg1) {
					
					btnComment.setText(arg0 + "");
				}
				
			});
			ParseQuery<ParseObject> q2 = ParseQuery.getQuery("Activity");
			q2.whereEqualTo("photoid", photo);
			q2.whereEqualTo("type", "like");
			q2.whereEqualTo("fromuser", ParseUser.getCurrentUser());
			q2.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					if(arg0 != null){
						if(arg0.size() > 0){
							final ParseObject obj = (ParseObject)arg0.get(0);
							btnLike.setTag(obj);
							if(obj.getInt("likeStatus") == 1){
								btnLike.setBackgroundResource(R.drawable.liked_icon);
							}else{
								btnLike.setBackgroundResource(R.drawable.likes_icon);
							}
						}
					}
				}
			});
			btnLike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					photo.clean();
					if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())){
					final ProgressDialog dialog = ParseDataManager.sharedDataManager(getActivity()).showProgress(getActivity(), "Updating", "Posting like");
					ParseQuery<ParseObject> q = ParseQuery.getQuery("Activity");
					q.whereEqualTo("photoid", photo);
					q.whereEqualTo("type", "like");
					q.whereEqualTo("fromuser", ParseUser.getCurrentUser());
					q.findInBackground(new FindCallback<ParseObject>(){

						@Override
						public void done(List<ParseObject> arg0, ParseException arg1) {
							if(arg0 != null){
								if(arg0.size() > 0){
									final ParseObject obj = (ParseObject)arg0.get(0);
									
									if(obj.getInt("likeStatus") == 1){
												obj.put("likeStatus", 0);
												photo.increment("imagelikes",  - 1); 
											}else{
												obj.put("likeStatus", 1);
												photo.increment("imagelikes",  1); 
											}
									obj.saveInBackground(new SaveCallback(){

										@Override
										public void done(ParseException arg0) {
											if(obj.getInt("likeStatus") == 0){
												btnLike.setText("" + photo.getImageLikes());
												btnLike.setBackgroundResource(R.drawable.likes_icon);
											}else{
												btnLike.setText("" + photo.getImageLikes());
												btnLike.setBackgroundResource(R.drawable.liked_icon);
											}
											ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
											q.whereEqualTo("objectId", photo.getObjectId());
											q.findInBackground(new FindCallback<GTCPhoto>(){

												@Override
												public void done(
														List<GTCPhoto> arg0,
														ParseException arg1) {
													if(arg0.size() > 0){
													arg0.get(0).saveInBackground(new SaveCallback(){
														@Override
														public void done(ParseException arg0) {
															if(arg0 != null){
																ParseDataManager.showAlert(getActivity(), "Error", arg0.getMessage());
															}
															dialog.dismiss();
															
														}
														
													});
													}
													
												}
												
											});
											
										}
										
									});
									
									
										
									
								}else{
									
									ParseObject obj = ParseObject.create("Activity");
				                    obj.put("photoid",photo);
				                    obj.put("type","like");
				                    obj.put("touser",photo.getUser());
				                    obj.put("fromuser",ParseDataManager.sharedDataManager(getActivity()).getCurrentUser());
				                    obj.put("likeStatus",1);
				                    photo.increment("imagelikes",  1);
				                    
				                    obj.saveInBackground(new SaveCallback(){

										@Override
										public void done(ParseException arg0) {
											ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
											q.whereEqualTo("objectId", photo.getObjectId());
											q.findInBackground(new FindCallback<GTCPhoto>(){

												@Override
												public void done(
														List<GTCPhoto> arg0,
														ParseException arg1) {
													if(arg0.size() > 0){
													arg0.get(0).saveInBackground(new SaveCallback(){
														@Override
														public void done(ParseException arg0) {
															if(arg0 != null){
																ParseDataManager.showAlert(getActivity(), "Error", arg0.getMessage());
															}else{
																btnLike.setText("1");
																btnLike.setBackgroundResource(R.drawable.liked_icon);
															}
															dialog.dismiss();
															
														}
														
													});
													}
													
												}
												
											});
										}
				                    	
				                    });
								}
								
							}else{
								
							}
							
							
						}

						
		            	
		            });
		            
					}else{
						ParseDataManager.showAlert(getActivity(), "Warning", "Not connected to internet");
					}
					
				}

			});
		  	btnComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
				}

			});
		  
		  
		  if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())){
		  ParseQuery<ParseObject> q4 = ParseQuery.getQuery("Activity");
		  q4.whereEqualTo("photoid", photo);
		  q4.whereEqualTo("type", "comment");
		  q4.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if(arg0 != null){
				 ItemsAdapter adapter = new ItemsAdapter(getActivity(),(ArrayList<ParseObject>) arg0);
				 lstComments.setAdapter(adapter);
				}
				
			}
			  
		  });
		  }
		  }
		 
		  return view;
	}
	public Bitmap getUserProfilePic()
	{
		SharedPreferences settings = getActivity().getSharedPreferences(getActivity().getPackageName(), 0);
   	 fb_firstname=settings.getString("FbFirstName","Empty");
   	 fb_lname=settings.getString("FbLastName","Empty");
   	
			if(thumbBmp == null){
				thumbBmp = PIBitmapUtils.getBitmap(fb_firstname+"_"+fb_lname, getActivity());
			}
			
			return thumbBmp ;
	}
	@SuppressWarnings("hiding")
	private class ItemsAdapter extends BaseAdapter {
		ArrayList<ParseObject> items;
		Context context;
		public ItemsAdapter(Context ctx, ArrayList<ParseObject> items) {
		   this.items = items;
		   context = ctx;
		  }

	
		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			
			final TextView txtUser;
			TextView txtComment, txtTime;
			final ParseImageView imgProfile;
			ParseObject obj = (ParseObject) getItem(position);
			if (view == null)
				view = View.inflate(getActivity(), R.layout.comment_cell, null);
			view.setTag(position);
			txtUser = (TextView) view.findViewById(R.id.txtUser);
			txtComment = (TextView) view.findViewById(R.id.txtComment);
			txtTime =  (TextView) view.findViewById(R.id.txtTime);
			imgProfile = (ParseImageView)view.findViewById(R.id.imgProfile);
			txtComment.setText(obj.getString("comment"));
			txtTime.setText(DateUtils.getRelativeDateTimeString(getActivity(),obj.getCreatedAt().getTime(),DateUtils.SECOND_IN_MILLIS,DateUtils.YEAR_IN_MILLIS,0).toString().split(",")[0]);
			
			final GTCUser u = (GTCUser) ParseDataManager.findByObjectId(ParseDataManager.sharedDataManager(context).users, obj.getParseObject("fromuser").getObjectId());
			if(u != null)
				txtUser.setText(u.getFirstName() + " " + u.getLastName());
			obj.getParseObject("fromuser").fetchIfNeededInBackground(new GetCallback<ParseUser>(){

				@Override
				public void done(ParseUser user, ParseException e) {
					if(user != null){
						user.getParseObject("profile").fetchIfNeededInBackground(new GetCallback<GTCUser>(){

							@Override
							public void done(GTCUser u, ParseException arg1) {
								if(u != null){
								txtUser.setText(u.getFirstName() + " " + u.getLastName());
								imgProfile.setParseFile(u.getThumbnail());
								try {
									imgProfile.setPlaceholder(getActivity().getResources().getDrawable(R.drawable.default_profile_80));	
								} catch (Exception e2) {
									// TODO: handle exception
								}
								
								imgProfile.loadInBackground(new GetDataCallback() {
								     public void done(byte[] data, ParseException e) {
								           
								     }
								});
								}
								
								
							}
							
						});
					}
					
				}
				
			});
			
			return view;
			
		}

			 public int getCount() {
				   return items.size();
				  }

				  public Object getItem(int position) {
				   return items.get(position);
				  }

				  public long getItemId(int position) {
				   return position;
				  }
	}



}

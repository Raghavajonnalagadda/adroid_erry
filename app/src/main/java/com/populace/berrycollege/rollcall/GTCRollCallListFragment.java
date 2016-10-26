package com.populace.berrycollege.rollcall;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
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
import com.populace.berrycollege.managers.ParseDataManager;
import com.populace.berrycollege.models.GTCPhoto;
import com.populace.berrycollege.models.GTCUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A list fragment representing a list of GTCRollCalls. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link GTCRollCallDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class GTCRollCallListFragment extends ListFragment {

	int page;
	ItemsAdapter adapter;
	Button btnMore ;
	public int total_counts=0;
//	Twitter Data
	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";
	String colors[] = new String[] {"Share on Facebook", "Share on Twitter", "Mark user as abusing", "Mark as inappropriete", "Block this user"};
	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private ProgressDialog pDialog;

	private static Twitter twitter;
	private static RequestToken requestToken;

	private static SharedPreferences mSharedPreferences;
	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
	Bitmap twitter_pic=null;
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public GTCRollCallListFragment() {
	}
	public void loadRollCall(final int page, int pageSize){
		ParseQuery<GTCPhoto> q = ParseQuery.getQuery(GTCPhoto.class);
		q.setSkip(page * pageSize);
		q.setLimit(pageSize);
		q.addDescendingOrder("createdAt");
		q.findInBackground(new FindCallback<GTCPhoto>(){

			@Override
			public void done(List<GTCPhoto> photos, ParseException arg1) {
				if(arg1 == null){
				if(adapter == null || page == 0){
				adapter = new ItemsAdapter(getActivity(), (ArrayList<GTCPhoto>) photos, 0);
				setListAdapter(adapter);

				System.out.println("Andy checking adapter counts "+total_counts+"   "+adapter.getCount());

				if(total_counts==adapter.getCount()||photos.size()<5)
				{
					btnMore.setVisibility(View.GONE);
				}
				else
				{
					btnMore.setVisibility(View.VISIBLE);
				}
				}else{
					adapter.addItems( (ArrayList<GTCPhoto>) photos);
					if(total_counts==adapter.getCount()||photos.size()<5)
					{
						btnMore.setVisibility(View.GONE);
					}
					else
					{
						btnMore.setVisibility(View.VISIBLE);
					}
				}
				}else{
//					Crashlytics.logException(arg1);
                    ParseDataManager.showAlert(getActivity(), "Error", "Error loading roll call. Please try again later.");
				}
			}

		});
	}
	public void onResume(){
		super.onResume();
		if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())){
			page = 0;
			getTotalPhotos();
			loadRollCall(page,5);
			page++;

			/*
			new Thread(new Runnable(){
				@Override
				public void run() {
					GTCDataManager.sharedDataManager(getActivity()).downloadPhotos();
					final ArrayList<GTCPhoto> photos = GTCDataManager.sharedDataManager(getActivity()).photos;
					if(getActivity() != null){
					getActivity().runOnUiThread(new Runnable(){
						@Override
						public void run() {
							adapter = new ItemsAdapter(getActivity(), photos, 0);
							setListAdapter(adapter);
						}
					});
				}
				}
			}).start();*/

		}else{

		final ArrayList<GTCPhoto> photos = new ArrayList<GTCPhoto>(); //GTCDataManager.sharedDataManager(getActivity()).photos;
		adapter = new ItemsAdapter(getActivity(), photos, 0);
		setListAdapter(adapter);
		}
	}
	private TextView noItems(String text) {
	    TextView emptyView = new TextView(getActivity());
	    //Make sure you import android.widget.LinearLayout.LayoutParams;
	    emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	            LayoutParams.MATCH_PARENT));
	    //Instead of passing resource id here I passed resolved color
	    //That is, getResources().getColor((R.color.gray_dark))
	    emptyView.setTextColor(Color.WHITE);
	    emptyView.setText(text);
	    emptyView.setTextSize(16);
	    emptyView.setVisibility(View.GONE);
	    emptyView.setGravity(Gravity.CENTER_VERTICAL
	            | Gravity.CENTER_HORIZONTAL);

	    //Add the view to the list view. This might be what you are missing
	    ((ViewGroup) getListView().getParent()).addView(emptyView);

	    return emptyView;
	}
	public void getTotalPhotos()
	{
		 ParseQuery<GTCPhoto> query = ParseQuery.getQuery(GTCPhoto.class);
		 query.countInBackground(new CountCallback() {
		     public void done(int count, ParseException e) {
		         if (e == null) {
		        	 total_counts=count;
		        System.out.println("Andy checking total count "+count);
		         } else {

		         }
		     }
		 });
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);




	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if(ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity()))
        {
//			this.setEmptyText("There are no images in timeline yet");
			this.getListView().setEmptyView(noItems("There are no images in timeline yet"));
        }
		else
		{
//			this.setEmptyText("Sorry your device is not connected to internet");
			this.getListView().setEmptyView(noItems("Sorry your device is not connected to internet"));
		}

		View v= View.inflate(getActivity(), R.layout.rollcall_footer, null);
		 btnMore = (Button)v.findViewById(R.id.btnMore);
		 LocalBroadcastManager.getInstance(getActivity()).registerReceiver(saveTwitterData,
			      new IntentFilter("save_Twitter_Data"));
			initTwitterConfigs();
			/* Enabling strict mode */
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			/* Check if required twitter keys are set */
			if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
				Toast.makeText(getActivity(), "Twitter key and secret not configured",
						Toast.LENGTH_SHORT).show();
				return;
			}

			/* Initialize application preferences */
			mSharedPreferences = getActivity().getSharedPreferences(PREF_NAME, 0);
		btnMore.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				loadRollCall(page,5);
				page++;
			}

		});
		getListView().addFooterView(v);
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	public void openRollCallDetail(int pos){
		GTCPhoto rollcall = (GTCPhoto)adapter.getItem(pos);
		Intent myIntent = new Intent(getActivity(), GTCRollCallDetailActivity.class);
		myIntent.putExtra("PHOTO_DICT",rollcall.fields(getActivity(),true,0).toString());
		startActivityForResult(myIntent,0);

	}
	JSONObject profileImages = new JSONObject();
	JSONObject mainImages = new JSONObject();
	JSONObject users = new JSONObject();
	JSONObject pusers = new JSONObject();
	@SuppressWarnings("hiding")
	private class ItemsAdapter extends BaseAdapter {
		ArrayList<GTCPhoto> items;
		Context context;

		public ItemsAdapter(Context ctx, ArrayList<GTCPhoto> items, int p) {
		   this.items = items;
		   Collections.sort(this.items, new Comparator<GTCPhoto>(){

			@Override
			public int compare(GTCPhoto lhs, GTCPhoto rhs) {
				Date lCAt;
				Date rCAt;
				if(lhs.getCreatedAt() != null){
					lCAt = lhs.getCreatedAt();
				}else{
					lCAt = lhs.getDate("createdAtCopy");
				}
				if(rhs.getCreatedAt() != null){
					rCAt = rhs.getCreatedAt();
				}else{
					rCAt = rhs.getDate("createdAtCopy");
				}

				return rCAt.compareTo(lCAt);
			}

		   });
		   context = ctx;
		  }
		public void addItems(ArrayList<GTCPhoto> items){
			this.items.addAll(items);
			 Collections.sort(this.items, new Comparator<GTCPhoto>(){

					@Override
					public int compare(GTCPhoto lhs, GTCPhoto rhs) {
						Date lCAt;
						Date rCAt;
						if(lhs.getCreatedAt() != null){
							lCAt = lhs.getCreatedAt();
						}else{
							lCAt = lhs.getDate("createdAtCopy");
						}
						if(rhs.getCreatedAt() != null){
							rCAt = rhs.getCreatedAt();
						}else{
							rCAt = rhs.getDate("createdAtCopy");
						}

						return rCAt.compareTo(lCAt);
					}

				   });
			this.notifyDataSetChanged();
		}

		  @Override
		  public View getView(final int position, View view,
		    ViewGroup parent) {
			  final TextView txtName,txtTime;
			  final Button btnLike;
			  final Button btnComment;
			  final ParseImageView imgProfile;
			  final ParseImageView imgMain;
			  final GTCPhoto photo = (GTCPhoto)getItem(position);
			  final Button btnShareData;

			  Log.d("Position", "" + position);
			  if(view == null|| view.getClass() == TextView.class){
				  view = View.inflate(context, R.layout.rollcall_cell, null);
			  }
			  imgProfile = (ParseImageView)view.findViewById(R.id.imgProfile);
			  imgMain = (ParseImageView)view.findViewById(R.id.imgMain);
			  btnLike = (Button)view.findViewById(R.id.btnLike);
			  btnComment = (Button)view.findViewById(R.id.btnComment);
			  txtName = (TextView)view.findViewById(R.id.txtName);
			  txtTime = (TextView)view.findViewById(R.id.txtTime);
			  btnShareData = (Button)view.findViewById(R.id.btnShareData);
			  btnShareData.setTag(photo);
			  btnShareData.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						final GTCPhoto photo_this = (GTCPhoto)v.getTag();

						if(imgMain != null)
						{


							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Choose an option");
						     builder.setAdapter(new AlertListAdapter(getActivity(),  R.layout.alert_list_item, 0, colors),
					                    new DialogInterface.OnClickListener() {
					                        @Override
					                        public void onClick(DialogInterface dialog,
					                                int item) {


										        // the user clicked on colors[which]

										    	Bitmap temp_bmp=((BitmapDrawable)imgMain.getDrawable()).getBitmap();
//										    	try {
//													shareImage(((BitmapDrawable)imgMain.getDrawable()).getBitmap());
//												} catch (Exception e) {
//													// TODO: handle exception
//												}

										    	switch (item) {
												case 0:
//													checkinFacbook("test post", temp_bmp);
													sharePhotoToFacebook("test post", temp_bmp);
													break;
												case 1:
													if(isTwiiterLoggedIn())
													{
														twitter_pic=temp_bmp;
														if(twitter_pic!=null)
														{
															UpdateTwitterStatus save = new UpdateTwitterStatus("");
															save.execute();
														}

													}
													else
													{
														loginToAskInTwitter();

													}
													break;
												case 2:
													postFlagsInformation(photo, "Abusive user", false, true, false);
													break;
												case 3:
													postFlagsInformation(photo, "Inappropriate Photo", true, false, false);
													break;
												case 4:
													postFlagsInformation(photo, "Block User", false, false, true);
													break;

												default:
													break;
												}


//					                            dialog.dismiss();
					                        }
					                    });

//							builder.setItems(colors, new DialogInterface.OnClickListener() {
//							    @Override
//							    public void onClick(DialogInterface dialog, int which) {
//							        // the user clicked on colors[which]
//
//							    	Bitmap temp_bmp=((BitmapDrawable)imgMain.getDrawable()).getBitmap();
////							    	try {
////										shareImage(((BitmapDrawable)imgMain.getDrawable()).getBitmap());
////									} catch (Exception e) {
////										// TODO: handle exception
////									}
//
//							    	switch (which) {
//									case 0:
////										checkinFacbook("test post", temp_bmp);
//										sharePhotoToFacebook("test post", temp_bmp);
//										break;
//									case 1:
//										if(isTwiiterLoggedIn())
//										{
//											twitter_pic=temp_bmp;
//											if(twitter_pic!=null)
//											{
//												UpdateTwitterStatus save = new UpdateTwitterStatus("");
//												save.execute();
//											}
//
//										}
//										else
//										{
//											loginToAskInTwitter();
//
//										}
//										break;
//									case 2:
//										postFlagsInformation(photo, "Abusive user", false, true, false);
//										break;
//									case 3:
//										postFlagsInformation(photo, "Inappropriate Photo", true, false, false);
//										break;
//									case 4:
//										postFlagsInformation(photo, "Block User", false, false, true);
//										break;
//
//									default:
//										break;
//									}
//							    }
//							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) {


							      } });
							builder.show();

						}
					}
				});
			  /*if(view.getTag() != null){
			  if(position == (Integer)view.getTag()){

				  new Handler().post(new Runnable(){

						@Override
						public void run() {
							ParseQuery<ParseObject> q = (ParseQuery<ParseObject>) txtName.getTag(R.id.USER_QUERY);
							if(q != null)
								q.cancel();
						}

			  		});
				  new Handler().post(new Runnable(){

						@Override
						public void run() {
							ParseQuery<ParseObject> q = (ParseQuery<ParseObject>) btnLike.getTag(R.id.LIKES_QUERY);
							if(q != null)
								q.cancel();
						}

			  		});
				  new Handler().post(new Runnable(){

						@Override
						public void run() {
							ParseQuery<ParseObject> q = (ParseQuery<ParseObject>) btnComment.getTag(R.id.COMMENTS_QUERY);
							if(q != null)
								q.cancel();
						}

			  		});
					new Handler().post(new Runnable(){

						@Override
						public void run() {
							ParseQuery<ParseObject> q = (ParseQuery<ParseObject>) btnLike.getTag(R.id.USER_LIKE_QUERY);
							if(q != null)
								q.cancel();

						}

			  		});
			  }
			  }*/
			  view.setTag(position);
			  txtName.setText("");
			  if(photo.getCreatedAt() != null){
			  txtTime.setText(DateUtils.getRelativeDateTimeString(getActivity(),photo.getCreatedAt().getTime(),DateUtils.SECOND_IN_MILLIS,DateUtils.YEAR_IN_MILLIS,0).toString().split(",")[0]);
			  }else if(photo.getDate("createdAtCopy") != null){
				  txtTime.setText(DateUtils.getRelativeDateTimeString(getActivity(),photo.getDate("createdAt").getTime(),DateUtils.SECOND_IN_MILLIS,DateUtils.YEAR_IN_MILLIS,0).toString().split(",")[0]);
			  }else{
				  txtTime.setText("");
			  }
			  txtName.setText("");
			  imgProfile.setPlaceholder(getActivity().getResources().getDrawable(R.drawable.default_profile_80));
			  imgProfile.setImageResource(R.drawable.default_profile_80);


			  ParseUser us = photo.getUser();
			  if(us!=null){
				  if(pusers.has(us.getObjectId())){
					  try{
					  final ParseUser user = (ParseUser) pusers.get(us.getObjectId());
					  if(users.has(user.getObjectId())){

							try {
								final GTCUser u = (GTCUser) users.get(user.getObjectId());
								txtName.setText(u.getFirstName() + " " + u.getLastName());
								if(!profileImages.has(u.getObjectId())){
								imgProfile.setParseFile(u.getThumbnail());
								imgProfile.loadInBackground(new GetDataCallback(){
									@Override
									public void done(byte[] arg0, ParseException arg1) {
										if(arg1 != null){
											try {
												profileImages.put(u.getObjectId(),arg0);
											} catch (JSONException e) {
												e.printStackTrace();
											}

										}
									}
								});
								}else{
									try {
										  byte [] data = ((byte[])profileImages.get(u.getObjectId()));
										  Bitmap bmp = BitmapFactory
								                    .decodeByteArray(data, 0, data.length);
										  imgProfile.setImageBitmap(bmp);

									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							} catch (JSONException e1) {
								e1.printStackTrace();
							}

						}else{
						((GTCUser)user.get("profile")).fetchInBackground(new GetCallback<GTCUser>(){

							@Override
							public void done(final GTCUser u,
									ParseException arg1) {
								try {
									users.put(user.getObjectId(),u);
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								txtName.setText(u.getFirstName() + " " + u.getLastName());
								if(!profileImages.has(u.getObjectId())){
								imgProfile.setParseFile(u.getThumbnail());
								imgProfile.loadInBackground(new GetDataCallback(){
									@Override
									public void done(byte[] arg0, ParseException arg1) {
										if(arg1 != null){
											try {
												profileImages.put(u.getObjectId(),arg0);
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

										}
									}
								});
								}else{
									try {
										  byte [] data = ((byte[])profileImages.get(u.getObjectId()));
										  Bitmap bmp = BitmapFactory
								                    .decodeByteArray(data, 0, data.length);
										  imgProfile.setImageBitmap(bmp);

									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}


						});
						}
					  }catch(Exception e){

					  }
				  }else{
					  final ParseUser user = photo.getUser();

						  ParseQuery<ParseUser> pu = (ParseQuery<ParseUser>) view.getTag(R.id.USER_QUERY);
						  pu = ParseQuery.getQuery(ParseUser.class);
						  pu.whereEqualTo("objectId", photo.getUser().getObjectId());
						  pu.findInBackground(new FindCallback<ParseUser>(){

							@Override
							public void done(List<ParseUser> arg0, ParseException arg1) {
								if(arg0.size() > 0){
									final ParseUser user = arg0.get(0);
									try {
										pusers.put(user.getObjectId(), user);
									} catch (JSONException e2) {
										e2.printStackTrace();
									}
									if(users.has(user.getObjectId())){

										try {
											final GTCUser u = (GTCUser) users.get(user.getObjectId());
											txtName.setText(u.getFirstName() + " " + u.getLastName());
											if(!profileImages.has(u.getObjectId())){
											imgProfile.setParseFile(u.getThumbnail());
											imgProfile.loadInBackground(new GetDataCallback(){
												@Override
												public void done(byte[] arg0, ParseException arg1) {
													if(arg1 != null){
														try {
															profileImages.put(u.getObjectId(),arg0);
														} catch (JSONException e) {
															e.printStackTrace();
														}

													}
												}
											});
											}else{
												try {
													  byte [] data = ((byte[])profileImages.get(u.getObjectId()));
													  Bitmap bmp = BitmapFactory
											                    .decodeByteArray(data, 0, data.length);
													  imgProfile.setImageBitmap(bmp);

												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										} catch (JSONException e1) {
											e1.printStackTrace();
										}

									}else{
									((GTCUser)user.get("profile")).fetchInBackground(new GetCallback<GTCUser>(){

										@Override
										public void done(final GTCUser u,
												ParseException arg1) {
											try {
												users.put(user.getObjectId(),u);
											} catch (JSONException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											txtName.setText(u.getFirstName() + " " + u.getLastName());
											if(!profileImages.has(u.getObjectId())){
											imgProfile.setParseFile(u.getThumbnail());
											imgProfile.loadInBackground(new GetDataCallback(){
												@Override
												public void done(byte[] arg0, ParseException arg1) {
													if(arg1 != null){
														try {
															profileImages.put(u.getObjectId(),arg0);
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}

													}
												}
											});
											}else{
												try {
													  byte [] data = ((byte[])profileImages.get(u.getObjectId()));
													  Bitmap bmp = BitmapFactory
											                    .decodeByteArray(data, 0, data.length);
													  imgProfile.setImageBitmap(bmp);

												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										}


									});
									}
								}



							}

						  });
						  txtName.setTag(R.id.USER_QUERY, pu);
						}


			  }


			  imgMain.setImageBitmap(null);
			  imgMain.setPlaceholder(getActivity().getResources().getDrawable(R.drawable.placeholder));
			  if(photo.getImage() != null){

					if(!mainImages.has(photo.getObjectId())){
						  imgMain.setParseFile(photo.getImage());
						  imgMain.loadInBackground(new GetDataCallback(){
							  	@Override
							  	public void done(byte[] arg0, ParseException arg1) {
							  		if(arg1 != null){
							  			try {
											mainImages.put(photo.getObjectId(),new ParseFile(arg0));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							  		}

							  	}

						  });
					}else{
					  try {
						  byte [] data = ((ParseFile)mainImages.get(photo.getObjectId())).getData();
						  Bitmap bmp = BitmapFactory
				                    .decodeByteArray(data, 0, data.length);
							imgMain.setImageBitmap(bmp);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					}
			  }else{
				  photo.fetchInBackground(new GetCallback<GTCPhoto>(){

					@Override
					public void done(GTCPhoto arg0, ParseException arg1) {
						if(!mainImages.has(photo.getObjectId())){
							  imgMain.setParseFile(photo.getImage());
							  imgMain.loadInBackground(new GetDataCallback(){
								  	@Override
								  	public void done(byte[] arg0, ParseException arg1) {
								  		if(arg1 != null){
								  			try {
												mainImages.put(photo.getObjectId(),new ParseFile(arg0));
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
								  		}

								  	}

							  });
						}else{
						  try {
							  byte [] data = ((ParseFile)mainImages.get(photo.getObjectId())).getData();
							  Bitmap bmp = BitmapFactory
					                    .decodeByteArray(data, 0, data.length);
								imgMain.setImageBitmap(bmp);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					}
					}

				  });
			  }
			  	view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {


						openRollCallDetail(Integer.parseInt(arg0.getTag().toString()));
					}

				});
			  	btnLike.setText("");
			  	btnComment.setText("");

			  	if(photo.getTemp_likes()==1000)
			  	{
				  	ParseQuery<ParseObject> q = ParseQuery.getQuery("Activity");
					q.whereEqualTo("photoid", photo);
					q.whereEqualTo("type", "like");
					q.whereEqualTo("likeStatus", 1);
					q.countInBackground(new CountCallback(){

						@Override
						public void done(int arg0, ParseException arg1) {

							btnLike.setText(arg0 + "");
							photo.setImageLikes(arg0);
							photo.setTemp_likes(arg0);
						}

					});
					btnLike.setTag(R.id.LIKES_QUERY, q);

			  	}
			  	else
			  	{
			  		btnLike.setText(photo.getTemp_likes() + "");
					photo.setImageLikes(photo.getTemp_likes());
			  	}


				if(photo.getTemp_comments()==1000)
			  	{
					ParseQuery<ParseObject> q1 = ParseQuery.getQuery("Activity");
					q1.whereEqualTo("photoid", photo);
					q1.whereEqualTo("type", "comment");
					q1.countInBackground(new CountCallback(){

						@Override
						public void done(int arg0, ParseException arg1) {

							btnComment.setText(arg0 + "");
							photo.setCommentsCount(arg0);
							photo.setTemp_comments(arg0);
						}

					});
					btnComment.setTag(R.id.COMMENTS_QUERY, q1);

			  	}
				else
				{
					btnComment.setText(photo.getTemp_comments() + "");
					photo.setCommentsCount(photo.getTemp_comments());
				}


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
									btnLike.setTextColor(Color.parseColor("#6B8FBF"));
								}else{
									btnLike.setBackgroundResource(R.drawable.likes_icon);
									btnLike.setTextColor(Color.parseColor("#FFFFFF"));
								}
							}
						}
					}
				});
				btnLike.setTag(R.id.USER_LIKE_QUERY, q2);
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

													new AlertDialog.Builder(getActivity())
											        .setIcon(android.R.drawable.ic_dialog_alert)
											        .setTitle("Alert")
											        .setMessage("Do you want to Un-Like this post?")
											        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

											            @Override
											            public void onClick(final DialogInterface ddialog, int which) {
											            	obj.put("likeStatus", 0);
															photo.increment("imagelikes",  - 1);
											            	obj.saveInBackground(new SaveCallback(){

																@Override
																public void done(ParseException arg0) {

																	if(obj.getInt("likeStatus") == 0){
																		btnLike.setText("" + photo.getImageLikes());
																		btnLike.setBackgroundResource(R.drawable.likes_icon);
																		photo.setTemp_likes(photo.getImageLikes());
																	}else{

																		btnLike.setText("" + photo.getImageLikes());
																		btnLike.setBackgroundResource(R.drawable.liked_icon);
																		photo.setTemp_likes(photo.getImageLikes());
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


											            }

											        })
											        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

														@Override
														public void onClick(DialogInterface ddialog, int which) {
															dialog.dismiss();

														}
													})
											        .show();
												}else{
													obj.put("likeStatus", 1);
													photo.increment("imagelikes",  1);
													obj.saveInBackground(new SaveCallback(){

														@Override
														public void done(ParseException arg0) {
															if(obj.getInt("likeStatus") == 0){
																btnLike.setText("" + photo.getImageLikes());
																btnLike.setBackgroundResource(R.drawable.likes_icon);
																photo.setTemp_likes(photo.getImageLikes());
															}else{

																btnLike.setText("" + photo.getImageLikes());
																btnLike.setBackgroundResource(R.drawable.liked_icon);
																photo.setTemp_likes(photo.getImageLikes());
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

												}




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
																	btnLike.setText("" + photo.getImageLikes());
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

			  return view;
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

			public void shareImage(Bitmap bitmap)
			{Intent share = new Intent(Intent.ACTION_SEND);
		    share.setType("image/jpeg");
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.JPEG, 100, bytes);
		    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
		    		bitmap, "TNGTC", null);
		    Uri imageUri =  Uri.parse(path);
		    share.putExtra(Intent.EXTRA_STREAM, imageUri);
		    startActivity(Intent.createChooser(share, "Select"));}
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

	public void checkinFacbook(final String message,Bitmap logo_bmp)
	{
		System.out.println("andy checking data image ");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// Compress image to lower quality scale 1 - 100
	    logo_bmp.compress(CompressFormat.PNG, 100, stream);
		byte[] image = stream.toByteArray();
		Bundle params = new Bundle();
		params.putString("message", message);
		params.putByteArray("source", image);

		/* make the API call */
		new GraphRequest(
		    AccessToken.getCurrentAccessToken(),
		    "/me/photos",
		    params,
		    HttpMethod.POST,
		    new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {
					// TODO Auto-generated method stub
				System.out.println("Andy checking error "+response.getError());

				if(response.getError()==null)
				{

					showToastMessage("Photo shared successfully on facebook");
				}
				else
				{
//					showAlertSimple("Operation was failed, Please try again");
					showToastMessage("Facebook Operation was failed, Please try again");


				}
				}
		    }
		).executeAsync();
}
	public void loginToAskInTwitter()
	{


		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("You are not logged into Twitter. Login Now?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

				loginToTwitter();

            	dialog.cancel();
            }
        });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();


	}
	private void loginToTwitter() {

		System.out.println("logi  function called ");
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 *  Loading twitter login page on webview for authorization
				 *  Once authorized, results are received at onActivityResult
				 *  */
				final Intent intent = new Intent(getActivity(), WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

			} catch (TwitterException e) {
				System.out.println("logi  function called "+e);
				e.printStackTrace();
			}
		} else {

			System.out.println("logi  function called 2");
		}
	}
	private void sharePhotoToFacebook(final String message,Bitmap logo_bmp){

		System.out.println("Andy checking fucntion called ");
//        Bitmap image=logo_bmp;
//        SharePhoto photo = new SharePhoto.Builder()
//                .setBitmap(image)
//                .setCaption(message)
//                .build();
//
//        SharePhotoContent content = new SharePhotoContent.Builder()
//                .addPhoto(photo)
//                .build();
//
//        ShareApi.share(content, null);

		try {
		     SharePhoto photo = new SharePhoto.Builder().setBitmap(logo_bmp).build();
		        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
		        ShareDialog dialog = new ShareDialog(this);
		        if (dialog.canShow(SharePhotoContent.class)){
		            dialog.show(content);
		        }
		        else{
		            Log.d("Activity", "you cannot share photos :(");
		        }
		} catch (Exception e) {
			// TODO: handle exception
		}


    }
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(saveTwitterData);
	}
	public void showToastMessage(String message)
	{
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}

	public boolean isTwiiterLoggedIn()
	{
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		return isLoggedIn ;
	}
	private BroadcastReceiver saveTwitterData = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
		    String message = intent.getStringExtra(oAuthVerifier);
		    Log.d("receiver", "Got message: " + message);
		    saveTwitterData(message);

		  }
		};
public void saveTwitterData(String verify)
{
System.out.println("Andy twitter data saved ");
try {
	twitter4j.auth.AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verify);

	long userID = accessToken.getUserId();
	final User user = twitter.showUser(userID);
	String username = user.getName();

	saveTwitterInfo(accessToken);
	showToastMessage("logged in twitter successfully");
//	UpdateTwitterStatus save = new UpdateTwitterStatus(GlobalClass.global_caption);
//	save.execute();
} catch (Exception e) {
	System.out.println("Andy twitter data saved "+e);
	Log.e("Twitter Login Failed", e.getMessage());
}


}
private void saveTwitterInfo(twitter4j.auth.AccessToken accessToken) {

	long userID = accessToken.getUserId();

	User user;
	try {
		user = twitter.showUser(userID);

		String username = user.getName();

		/* Storing oAuth tokens to shared preferences */
		Editor e = mSharedPreferences.edit();
		e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
		e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
		e.putString(PREF_USER_NAME, username);
		e.commit();

	} catch (TwitterException e1) {
		e1.printStackTrace();
	}
}

class UpdateTwitterStatus extends AsyncTask<String, String, Void> {
	 String caption;

	 public UpdateTwitterStatus(String city_name) {
			super();
			this.caption = city_name;
		}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		pDialog = new ProgressDialog(ac.getParent());
//		pDialog.setMessage("Posting to twitter...");
//		pDialog.setIndeterminate(false);
//		pDialog.setCancelable(false);
//		pDialog.show();
	}

	protected Void doInBackground(String... args) {

//		String status = args[0];
		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			// Access Token
			String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
			// Access Token Secret
			String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

			twitter4j.auth.AccessToken accessToken = new twitter4j.auth.AccessToken(access_token, access_token_secret);
			Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

			// Update status
			StatusUpdate statusUpdate = new StatusUpdate(caption);
//			InputStream is = getResources().openRawResource(R.drawable.ic_launcher);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			twitter_pic.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
			byte[] bitmapdata = bos.toByteArray();
			ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
			statusUpdate.setMedia("test.jpg", bs);
//			LocationManager manager = (LocationManager) ac.getSystemService(Context.LOCATION_SERVICE);
//	        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//	        if (location != null) {
//	            double latitude = location.getLatitude();
//	            double longitude = location.getLongitude();
//	            statusUpdate.setLocation(new GeoLocation(latitude, longitude));
//	        }
//			GeoLocation location=new GeoLocation(current_wayPoint.getLocation().getLatitude(), current_wayPoint.getLocation().getLongitude());

//			System.out.println("Andy checking twitter exception "+location.getLatitude()+" "+location.getLongitude());

//			statusUpdate.setLocation(new GeoLocation(current_wayPoint.getLocation().getLatitude(), current_wayPoint.getLocation().getLongitude()));
//			statusUpdate.displayCoordinates(true);
//			statusUpdate.setDisplayCoordinates(true);
//			statusUpdate.location(location);

			twitter4j.Status response = twitter.updateStatus(statusUpdate);
			System.out.println("Andy checking twitter exception "+response);
			Log.d("Status", response.getText());

		} catch (TwitterException e) {

			System.out.println("Andy checking twitter exception "+e);
			Log.d("Failed to post!", e.getMessage());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {


		showToastMessage("You have successfully shared on Twitter");

	}

}

public void postFlagsInformation(GTCPhoto photo,String request_type,final boolean inapropriate,boolean abusing,boolean block)
{


	if(CheckIsConnectedToInternet(getActivity()))
    {
		   final ParseObject pObject =ParseObject.create("Flag");
		   if(!inapropriate)
		   {
			   pObject.put("abusive_user", photo.getUser());

		   }
		   else
		   {
			   pObject.put("photo", photo);
		   }

       		pObject.put("RequestType",request_type);
       		  final ProgressDialog dialog = ParseDataManager.showProgress(getActivity(), "Please wait", "Submitting your feedback...");
       		  pObject.saveInBackground(new SaveCallback () {
       			   @Override
       			   public void done(ParseException ex) {
       			    if (ex == null) {

       			    	if(inapropriate)
       			    	{
       			    		showAlertSimple("Thanks for submitting your feedback. Our team will review it and unpublish it within next 24 hours if found abusive or inappropriate");
       			    	}
       			    	else
       			    	{
       			    		showAlertSimple("Thanks for submitting your feedback. Our team will review it and block this user within next 24 hours if found abusive or inappropriate");
       			    	}

       			    } else {
       			    	System.out.println("Andy erros check "+ex);
       			    }
       			    dialog.dismiss();
       			  }
       			});

    }
	else
	{
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Sorry, Your device is not connected to internet");
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



class AlertListAdapter extends ArrayAdapter<String>
{
String items[];
	public AlertListAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) {

		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		items=objects;
	}



    ViewHolder holder;
    Drawable icon;

    class ViewHolder {
        ImageView icon;
        TextView title;
    }

    public View getView(int position, View convertView,
            ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater)getActivity()
                .getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.alert_list_item, null);

            holder = new ViewHolder();

            holder.title = (TextView) convertView
                    .findViewById(R.id.title_option);
            convertView.setTag(holder);
        } else {
            // view already defined, retrieve view holder
            holder = (ViewHolder) convertView.getTag();
        }

        if(position<2)
        {
        	holder.title.setTextColor(getActivity().getResources().getColor(R.color.white_color));
        }
        else
        {
        	holder.title.setTextColor(getActivity().getResources().getColor(R.color.red_color));
        }
        holder.title.setText(items[position]);

        return convertView;
    }

	}
public void showAlertSimple(final String  message)
{

	AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
}

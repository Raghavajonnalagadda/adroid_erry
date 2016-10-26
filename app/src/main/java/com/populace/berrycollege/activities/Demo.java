//package com.populace.berrycollege.activities;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.ActivityInfo;
//import android.content.res.AssetManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.ExifInterface;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//
//import com.parse.FindCallback;
//import com.parse.GetCallback;
//import com.parse.LogInCallback;
//import com.parse.Parse;
//import com.parse.ParseAnalytics;
//import com.parse.ParseFile;
//import com.parse.ParseInstallation;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.parse.PushService;
//import com.parse.SaveCallback;
//import com.populace.berrycollege.R;
//import com.populace.berrycollege.managers.DownloadCompleteCallback;
//import com.populace.berrycollege.managers.ILoginCallback;
//import com.populace.berrycollege.managers.IRegisterCallback;
//import com.populace.berrycollege.models.GTCObject;
//import com.populace.berrycollege.models.GTCPhoto;
//import com.populace.berrycollege.models.GTCUser;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Field;
//import java.net.URI;
//import java.nio.channels.FileChannel;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import soundcloud.android.crop.Crop;
//
///**
// * Created by Nikhil on 4/26/2016.
// */
//public class Demo {
//
//
//
//
//    public class GTCDataManager implements DownloadCompleteCallback{
//        public static final String PREFS_NAME = "GTCPrefs";
//        public static final String TIME_FORMAT = "h:mm a";
//        public static final String DATE_FORMAT = "MMM dd, yyyy";
//        public static final String DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm a";
//        //	public static String[] CONF_DATES = {"Aug 4, 2014","Aug 5, 2014","Aug 6, 2014","Aug 7, 2014"};
//        public static String[] CONF_DATES = {"Jan 22, 2016","Jan 23, 2016","Jan 24, 2016","Jan 25, 2016"};
//        //	public static String[] CONF_DAYS = { "Mon","Tue","Wed","Thu"};
//        public static String[] CONF_DAYS = {"Fri","Sat","Sun","Mon"};
//
//        public static String[] EVENTS_DATES = {"Aug 3, 2014","Aug 4, 2014","Aug 5, 2014","Aug 6, 2014","Aug 7, 2014"};
//        public static String[] EVENTS_DAYS = {"Sun","Mon","Tue","Wed","Thu"};
//        public static String[] STUDY_YEARS = {"First Year", "Second Year", "Third Year", "Alumni Events"};
//
//        public static boolean showsDownloadActivity = true;
//        public static final int DEFAULT_COLOR = Color.argb(1, 79, 82, 74);
//        //Data  rawData;
//        Handler msgHandler = new Handler();
//        ArrayList<GTCAttendee>  attendee= new ArrayList<GTCAttendee>();
//        ArrayList<GTCSession>  sessions= new ArrayList<GTCSession>();
//        ArrayList<GTCSession1>  sessions1= new ArrayList<GTCSession1>();
//        ArrayList<GTCSession2>  sessions2= new ArrayList<GTCSession2>();
//        ArrayList<GTCSession3>  sessions3= new ArrayList<GTCSession3>();
//        ArrayList<GTCExhibitors>  exhibitors= new ArrayList<GTCExhibitors>();
//        ArrayList<GTCEvent>  events = new ArrayList<GTCEvent>();
//        ArrayList<GTCAuction>  auctions = new ArrayList<GTCAuction>();
//        ArrayList<GTCUser>  users = new ArrayList<GTCUser>();
//        ArrayList<GTCPhoto>  photos = new ArrayList<GTCPhoto>();
//        ArrayList<GTCSpeaker>  speakers= new ArrayList<GTCSpeaker>();
//        ArrayList<GTCSponsor>  sponsors = new ArrayList<GTCSponsor>();
//        ArrayList<GTCSession>  agenda= new ArrayList<GTCSession>();
//        ArrayList<GTCSession>  session_rating= new ArrayList<GTCSession>();
//
//        ArrayList<GTCMessage> messages = new ArrayList<GTCMessage>();
//        ArrayList<GTCLocation> locations= new ArrayList<GTCLocation>();
//        ArrayList<GTCSurvey>  surveys = new ArrayList<GTCSurvey>();
//        GTCInformation information;
//        Timer timer = new Timer();
//        DatabaseHandler dbHandler;
//        float currentAppVersion;
//        float currentDataVersion;
//        Context context;
//        static GTCDataManager sharedDataManager;
//        float latestDataVersion=1f;
//        private GTCDataManager(Context ctx) {
//            context = ctx;
//        }
//
//        public static GTCDataManager sharedDataManager(Context ctx) {
//
//            if (sharedDataManager == null) {
//                sharedDataManager = new GTCDataManager(ctx);
//                sharedDataManager.initialize();
//            }
//            return sharedDataManager;
//        }
//
//        public void initialize() {
//
//
//            try{
//                sharedDataManager.copyInitialData();
//                sharedDataManager.initializeParse();
//                sharedDataManager.prepareDatabase();
//                sharedDataManager.tryUpdateVersion(1f, 1f);
////			sharedDataManager.tryUpdateVersion(1f, 2.0f);
//                if(CheckIsConnectedToInternet(context)){
//                    new Thread(
//                            new Runnable(){
//
//                                @Override
//                                public void run() {
//                                    sharedDataManager.downloadUsers();
//                                    sharedDataManager.downloadPhotos();
//                                }
//                            }).start();
//                }
//                sharedDataManager.loadData();
//                sharedDataManager.getAppUsers(new FindCallback<ParseUser>(){
//
//                    @Override
//                    public void done(List<ParseUser> arg0,
//                                     com.parse.ParseException arg1) {
//                    }
//
//                });
//                //timer.scheduleAtFixedRate(new TwitterTask(), 0, 3000);
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        private void copyInitialData_new() {
//            AssetManager assetManager = context.getAssets();
//            String[] files = null;
//            try {
//                files = assetManager.list("");
//            } catch (IOException e) {
//                Log.e("tag", "Failed to get asset file list.", e);
//            }
//
//            for(String filename : files) {
//                if(filename.endsWith("json")){
//                    File file = context.getFileStreamPath(filename);
////	    		if (!file.exists() || file.length() == 0) {
//                    InputStream in = null;
//                    OutputStream out = null;
//                    try {
//                        in = assetManager.open(filename);
//                        File outFile = new File(context.getFilesDir(), filename);
//                        out = new FileOutputStream(outFile);
//                        copyFile(in, out);
//                        in.close();
//                        in = null;
//                        out.flush();
//                        out.close();
//                        out = null;
//                    } catch(IOException e) {
//                        System.out.println("Andy json error "+e);
//                        Log.e("tag", "Failed to copy asset file: " + filename, e);
//                    }
////	    		}
//
//                }
//
//            }
//        }
//
//        private void copyInitialData() {
//            AssetManager assetManager = context.getAssets();
//            String[] files = null;
//            try {
//                files = assetManager.list("");
//            } catch (IOException e) {
//                Log.e("tag", "Failed to get asset file list.", e);
//            }
//
//            for(String filename : files) {
//                if(filename.endsWith("json")){
//                    File file = context.getFileStreamPath(filename);
//                    if (!file.exists() || file.length() == 0) {
//                        InputStream in = null;
//                        OutputStream out = null;
//                        try {
//                            in = assetManager.open(filename);
//                            File outFile = new File(context.getFilesDir(), filename);
//                            out = new FileOutputStream(outFile);
//                            copyFile(in, out);
//                            in.close();
//                            in = null;
//                            out.flush();
//                            out.close();
//                            out = null;
//                        } catch(IOException e) {
//                            Log.e("tag", "Failed to copy asset file: " + filename, e);
//                        }
//                    }
//
//                }
////	    	else if(filename.contains("png")&&filename.endsWith("png"))
////	    	{
//                else if(!filename.endsWith("json"))
//                {
//                    String fullPath = "/data/data/" + context.getPackageName() + "/files/";
//                    File dir = new File(fullPath);
//                    if (!dir.exists())
//                        dir.mkdir();
//                    File file = context.getFileStreamPath(filename);
//                    if (!file.exists() || file.length() == 0) {
//                        InputStream in = null;
//                        OutputStream out = null;
//                        try {
//                            in = assetManager.open(filename);
//                            File outFile = new File(fullPath, filename);
//
//                            System.out.println("Andy this path data "+outFile.getAbsolutePath());
//                            out = new FileOutputStream(outFile);
//                            copyFile(in, out);
//                            in.close();
//                            in = null;
//                            out.flush();
//                            out.close();
//                            out = null;
//                        } catch(IOException e) {
//                            System.out.println("Andy json error "+e);
//                            Log.e("tag", "Failed to copy asset file: " + filename, e);
//                        }
//                    }
//                }
//            }
//        }
//        private void copyFile(InputStream in, OutputStream out) throws IOException {
//            byte[] buffer = new byte[1024];
//            int read;
//            while((read = in.read(buffer)) != -1){
//                out.write(buffer, 0, read);
//            }
//        }
//        public class AsyncDownloadData extends AsyncTask<Void, CharSequence, Void> {
//            DownloadCompleteCallback callback;
//            ProgressDialog dialog;
//            Context context;
//            public AsyncDownloadData(DownloadCompleteCallback cb, Context ctx){
//                callback = cb;
//                context = ctx;
//            }
//            @Override
//            protected void onPreExecute() {
//                if(GTCDataManager.showsDownloadActivity){
//                    dialog = new ProgressDialog(context);
//                    dialog.setTitle("Downloading");
//                    dialog.setMessage("Checking Data Version");
//                    dialog.setIndeterminate(true);
//                    dialog.setCancelable(false);
//                    dialog.show();
//                }
//            }
//            @Override
//            protected Void doInBackground(Void... params) {
//                publishProgress("Downloading");
//                publishProgress("Downloading Exhibitors");
//                sharedDataManager.downloadExhibitors();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions1();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions2();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions3();
//                publishProgress("Downloading Events");
//                sharedDataManager.downloadEvents();
//                publishProgress("Downloading Speakers");
//                sharedDataManager.downloadSpeakers();
//                publishProgress("Downloading Sponsors");
//                sharedDataManager.downloadSponsors();
//                //publishProgress("Downloading Surveys");
//                //sharedDataManager.downloadSurveys();
//                publishProgress("Downloading Locations");
//                sharedDataManager.downloadLocations();
//                publishProgress("Downloading Information");
//                sharedDataManager.downloadInformation();
//                publishProgress("Downloading Auctions");
//                sharedDataManager.downloadAuctions();
//                publishProgress("Downloading Attendees");
//                sharedDataManager.downloadAttendee();;
//                publishProgress("Loading Attendees");
//                sharedDataManager.loadAttendees();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions1();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions2();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions3();
//                publishProgress("Loading Events");
//                sharedDataManager.loadEvents();
//                publishProgress("Loading Speakers");
//                sharedDataManager.loadSpeakers();
//                publishProgress("Loading Sponsors");
//                sharedDataManager.loadSponsors();
//                publishProgress("Loading Agenda");
//                sharedDataManager.loadAgenda();
//                //publishProgress("Loading Surveys");
//                //sharedDataManager.loadSurveys();
//                publishProgress("Loading Locations");
//                sharedDataManager.loadLocations();
//                publishProgress("Loading Information");
//                sharedDataManager.loadInformation();
//                publishProgress("Loading Auctions");
//                sharedDataManager.loadAuctions();
//                publishProgress("Loading Users");
//                sharedDataManager.loadUsers();
//                publishProgress("Loading Photos");
//                sharedDataManager.loadPhotos();
//                return null;
//            }
//            protected void onProgressUpdate(CharSequence... message) {
//                if(GTCDataManager.showsDownloadActivity)
//                    dialog.setMessage(message[0]);
//            }
//
//            protected void onPostExecute(Void unused) {
//                if(GTCDataManager.showsDownloadActivity){
//                    dialog.dismiss();
//                }
//                if(callback != null)
//                    callback.downloadCompleted();
//            }
//
//        }
//        public void initializeParse(){
//            try{
//
//                ParseObject.registerSubclass(GTCSession.class);
//                ParseObject.registerSubclass(GTCSession1.class);
//                ParseObject.registerSubclass(GTCSession2.class);
//                ParseObject.registerSubclass(GTCSession3.class);
//                ParseObject.registerSubclass(GTCSponsor.class);
//                ParseObject.registerSubclass(GTCSpeaker.class);
//                ParseObject.registerSubclass(GTCLocation.class);
//                ParseObject.registerSubclass(GTCSurvey.class);
//                ParseObject.registerSubclass(GTCAuction.class);
//                ParseObject.registerSubclass(GTCPhoto.class);
//                ParseObject.registerSubclass(GTCUser.class);
//                ParseObject.registerSubclass(GTCEvent.class);
//                ParseObject.registerSubclass(GTCInformation.class);
//                ParseObject.registerSubclass(GTCMessage.class);
//                ParseObject.registerSubclass(GTCExhibitors.class);
//                ParseObject.registerSubclass(GTCAttendee.class);
////		Parse.initialize(this.context, "7Pc9qchOwiJhld7w9IvCIzn8Wfi8yKa5dDOqwmIK", "7j67S8xIcSLRY1cidYXLWVztfjbpw9j8foNGXQ3J");
//                Parse.initialize(this.context, "4hsxEOXQsrEgP9o2DEWWi75yzf4DF36kpZn2ELM8", "accdSYTFUyhqBsP2m9rDzbMjI8HlcvIqU3AKW4jt");
//                ParseUser.enableAutomaticUser();
////		PushService.setDefaultPushCallback(this.context, GTCMessagesActivity.class);
//                PushService.setDefaultPushCallback(this.context, GTCActivity.class);
//                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                try{
//                    installation.put("appIdentifier", "com.populace.sdgc");
//                }catch(Exception e1){
//                    e1.printStackTrace();
//                }
//                try{
//                    installation.put("appName", "SD Tourism");
//                }catch(Exception e1){
//                    e1.printStackTrace();
//                }
//                installation.saveEventually();
//                ParseAnalytics.trackAppOpened(((Activity) this.context).getIntent());
//                //Intent intent = new Intent(this.context, GTCMessagesService.class);
//                //this.context.startService(intent);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        ProgressDialog checkVersionProgress;
//        public void checkDataVersion(final boolean withProgress, final Context ctx){
//            if(CheckIsConnectedToInternet(ctx)){
//
//                if(withProgress){
//                    checkVersionProgress = GTCDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");
//                }
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//                //query.setLimit(1);
//                query.findInBackground(new FindCallback<ParseObject>(){
//
//                    @Override
//                    public void done(List<ParseObject> result,
//                                     com.parse.ParseException arg1) {
//                        if(withProgress){
//                            checkVersionProgress.dismiss();
//                        }
//                        if(result != null){
//                            if(result.size() > 0){
//                                ParseObject  obj =  result.get(0);
//                                float appVersion = obj.getNumber("AppVersion").floatValue();
//                                float dataVersion = obj.getNumber("DataVersion").floatValue();
//
//                                try {
//                                    String email_feedback= obj.getString("dialog_text");
//                                    String eula_string= obj.getString("eula");
//                                    String privacy_policy=obj.getString("privacy_policy");
//                                    SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings_new.edit();
//                                    editor.putString("dialog_text", email_feedback);
//                                    editor.putString("eula_text", eula_string);
//                                    editor.putString("privacy_policy", privacy_policy);
//
//                                    editor.commit();
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//
//
//
//                                try {
//                                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                                    currentAppVersion =settings.getFloat("AppVersion",1f);
//                                    currentDataVersion = settings.getFloat("DataVersion",1f);
//
//
//                                    if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
//                                        askDownload(dataVersion, ctx);
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//                                    }else{
//                                        sharedDataManager.loadData();
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//
//                                    }
//                                }
//                                catch (Exception exception) {
//                                    askDownload(dataVersion, ctx);
//                                    currentAppVersion =appVersion;
//                                    currentDataVersion = dataVersion;
//                                    System.out.println("andy checking data version "+exception);
//                                }
//                                finally {
//
//                                }
//
//                            }
//                        }else{
//                            sharedDataManager.loadData();
//
//                        }
//                    }
//                });
//
//
//
//
////				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
////				    query2.setLimit(1);
////						query2.findInBackground(new FindCallback<ParseObject>(){
////
////							@Override
////							public void done(List<ParseObject> result,
////									com.parse.ParseException arg1) {
////								if(result != null){
////									ParseObject  obj =  result.get(0);
////							        String defaultstatement = obj.getString("DefaultStatement");
////							        String newstatement = obj.getString("NewStatement");
////							        boolean showstatement = obj.getBoolean("showStatement");
////
////							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
////							        try {
////							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
////							        	SharedPreferences.Editor editor = settings.edit();
////							            editor.putString("DefaultStatement", defaultstatement);
////							            editor.putString("NewStatement", newstatement);
////							            editor.putBoolean("showStatement", showstatement);
////							            editor.commit();
////							        }
////							        catch (Exception exception) {
////
////							        }
////
////								}else{
////
////								}
////							}
////						});
//
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//            }else{
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                if(withProgress){
//                    GTCDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
//                }
//            }
//        }
//        ProgressDialog checkVersionProgress1;
//        public void checkDataVersion_refresh(final boolean withProgress, final Context ctx){
//            if(CheckIsConnectedToInternet(ctx)){
//
//                if(withProgress){
//                    checkVersionProgress1 = GTCDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");
//                }
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//                //query.setLimit(1);
//                query.findInBackground(new FindCallback<ParseObject>(){
//
//                    @Override
//                    public void done(List<ParseObject> result,
//                                     com.parse.ParseException arg1) {
//                        if(withProgress){
//                            checkVersionProgress1.dismiss();
//                        }
//                        if(result != null){
//                            if(result.size() > 0){
//                                ParseObject  obj =  result.get(0);
//                                float appVersion = obj.getNumber("AppVersion").floatValue();
//                                float dataVersion = obj.getNumber("DataVersion").floatValue();
//
//                                try {
//                                    String email_feedback= obj.getString("dialog_text");
//                                    String eula_string= obj.getString("eula");
//                                    String privacy_policy=obj.getString("privacy_policy");
//                                    SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings_new.edit();
//                                    editor.putString("dialog_text", email_feedback);
//                                    editor.putString("eula_text", eula_string);
//                                    editor.putString("privacy_policy", privacy_policy);
//
//                                    editor.commit();
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//
//
//                                try {
//                                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                                    currentAppVersion =settings.getFloat("AppVersion",1f);
//                                    currentDataVersion = settings.getFloat("DataVersion",1f);
//                                    System.out.println("andy checking data version "+currentAppVersion +" "+currentDataVersion);
//
//                                    if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
//                                        askDownload(dataVersion, ctx);
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//                                    }else{
//
//                                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(ctx);
//                                        alertDialog2.setTitle("App is up to date");
//                                        alertDialog2.setMessage("There is no new app data available at this time");
//                                        alertDialog2.setPositiveButton("OK",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                    }
//                                                });
//
//                                        alertDialog2.show();
//                                        sharedDataManager.loadData();
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//
//                                    }
//                                }
//                                catch (Exception exception) {
//                                    askDownload(dataVersion, ctx);
//                                    currentAppVersion =appVersion;
//                                    currentDataVersion = dataVersion;
//
//                                }
//                                finally {
//
//                                }
//
//                            }
//                        }else{
//                            sharedDataManager.loadData();
//
//                        }
//                    }
//                });
//
//
//
//
////				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
////				    query2.setLimit(1);
////						query2.findInBackground(new FindCallback<ParseObject>(){
////
////							@Override
////							public void done(List<ParseObject> result,
////									com.parse.ParseException arg1) {
////								if(result != null){
////									ParseObject  obj =  result.get(0);
////							        String defaultstatement = obj.getString("DefaultStatement");
////							        String newstatement = obj.getString("NewStatement");
////							        boolean showstatement = obj.getBoolean("showStatement");
////
////							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
////							        try {
////							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
////							        	SharedPreferences.Editor editor = settings.edit();
////							            editor.putString("DefaultStatement", defaultstatement);
////							            editor.putString("NewStatement", newstatement);
////							            editor.putBoolean("showStatement", showstatement);
////							            editor.commit();
////							        }
////							        catch (Exception exception) {
////
////							        }
////
////								}else{
////
////								}
////							}
////						});
//
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//            }else{
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                if(withProgress){
//                    GTCDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
//                }
//            }
//        }
//        public void addToAgenda(GTCSession _session){
//            if(!GTCDataManager.sharedDataManager.inAgenda(_session)){
//                int id = (int)dbHandler.addSession(_session);
//                _session.id = id;
//                agenda.add(_session);
//            }
//
//        }
//
//        public void addToRatings(GTCSession _session){
//            if(!GTCDataManager.sharedDataManager.inRatings(_session)){
//                int id = (int)dbHandler.addSessionRatings(_session);
//                _session.id = id;
//                session_rating.add(_session);
//            }
//
//        }
//        public void removeFromAgenda(GTCSession _session){
//
//            dbHandler.deleteSession(_session);
//
//            for(int k = 0; k < agenda.size(); k++){
//                if(GTCDataManager.sharedDataManager.agenda.get(k).getObjectId() == _session.getObjectId()){
//                    agenda.remove(GTCDataManager.sharedDataManager.agenda.get(k));
//                    break;
//                }
//            }
//
//        }
//
//        public GTCScan addScanWithText(String text){
//            GTCScan scan = new GTCScan(0,text,new Date());
//            dbHandler.addScan(scan);
//            return scan;
//        }
//        public ArrayList<GTCScan> scans(){
//            return dbHandler.getAllScans();
//        }
//        public void deleteScan(GTCScan scan){
//            dbHandler.deleteScan(scan);
//        }
//        public ArrayList<String> results(){
//            ArrayList<String> results = new ArrayList<String>();
//            ArrayList<GTCScan> scans = sharedDataManager.scans();
//            for(GTCScan  scan : scans){
//                //[results addObject:[UniversalResultParser parsedResultForString:scan.text]];
//                //TODO
//            }
//            return results;
//        }
//        public void prepareDatabase(){
//            dbHandler = new DatabaseHandler(context);
//
////		if(checkDBAltered())
////		{
////
////		}
////		else
////		{
////			dbHandler.alterAgendaTable();
////			SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
////    		SharedPreferences.Editor editor = settings_new.edit();
////    		editor.putFloat("DBAltered", 1);
////    		editor.commit();
////		}
//
//        }
//
//        public boolean checkDBAltered()
//        {
//
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            if (settings.contains("DBAltered"))
//            {
//                return true;
//            }
//
//            return false;
//        }
//        public void askDownload(float dataVersion, final Context ctx){
//
//            new AlertDialog.Builder(ctx)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle("Download Update")
//                    .setMessage("Version "+dataVersion+" is available.\nDownload now?")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            downloadData(ctx);
//
//                        }
//
//                    })
//                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            final Builder builder = new Builder(ctx);
//                            builder.setCancelable(true);
//                            builder.setTitle("Download Later").setMessage("You can refresh the app content at any time from settings menu").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // TODO Auto-generated method stub
//                                    dialog.cancel();
//                                }
//                            });
//
//
//                            ((Activity) ctx).runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    builder.show();
//
//                                }
//                            });
//
//                        }
//                    })
//                    .show();
//        }
//        public void downloadData(Context ctx){
//
//            new AsyncDownloadData(sharedDataManager, ctx).execute();
//        }
//
//
//        public void downloadAttendee() {
//            attendee = (ArrayList<GTCAttendee>) download("attendees.json",GTCAttendee.class,false);
//        }
//        public void downloadExhibitors() {
//            exhibitors = (ArrayList<GTCExhibitors>) download("exhibitors.json",GTCExhibitors.class,false);
//        }
//        public void downloadSessions() {
//            sessions = (ArrayList<GTCSession>) download("sessions.json",GTCSession.class,false);
//        }
//        public void downloadSessions1() {
//            sessions1 = (ArrayList<GTCSession1>) download("sessions1.json",GTCSession1.class,false);
//        }
//        public void downloadSessions2() {
//            sessions2 = (ArrayList<GTCSession2>) download("sessions2.json",GTCSession2.class,false);
//        }
//        public void downloadSessions3() {
//            sessions3 = (ArrayList<GTCSession3>) download("sessions3.json",GTCSession3.class,false);
//        }
//        public void downloadEvents() {
//            events = (ArrayList<GTCEvent>) download("events.json",GTCEvent.class,false);
//        }
//        public void downloadAuctions() {
//            auctions = (ArrayList<GTCAuction>) download("auctions.json",GTCAuction.class,false);
//        }
//        public void downloadUsers() {
//            users = (ArrayList<GTCUser>) download("users.json",GTCUser.class,true);
//        }
//        public void downloadPhotos() {
//            photos = (ArrayList<GTCPhoto>) download("photos.json",GTCPhoto.class,true);
//        }
//        public void downloadSurveys() {
//            surveys = (ArrayList<GTCSurvey>) download("surveys.json",GTCSurvey.class,false);
//        }
//        public void downloadLocations() {
//            locations = (ArrayList<GTCLocation>) download("locations.json",GTCLocation.class,false);
//        }
//        public void downloadSpeakers() {
//            speakers = (ArrayList<GTCSpeaker>) download("speakers.json",GTCSpeaker.class,false);
//        }
//        public void downloadSponsors() {
//            sponsors = (ArrayList<GTCSponsor>) download("sponsors.json",GTCSponsor.class,false);
//        }
//        public void downloadInformation() {
//            download("information.json",GTCInformation.class,false);
//        }
//        public List<?> download(String filename, Class<? extends GTCObject> c, boolean skipFiles){
//            ParseQuery<?> query = ParseQuery.getQuery(c);
//            List<?> _objects = null;
//            try {
//                query.setLimit(1000);
//                _objects = query.find();
//                FileOutputStream out = null;
//                try {
//                    out = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
//                } catch (FileNotFoundException e1) {
//                    e1.printStackTrace();
//                }
//                JSONArray array = new JSONArray();
//                for(int i = 0; i < _objects.size(); i++){
//                    GTCObject object = (GTCObject)_objects.get(i);
//                    array.put(object.fields(context,skipFiles,0));
//                }
//                try {
//                    out.write(array.toString().getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally{
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (com.parse.ParseException e2) {
//                e2.printStackTrace();
//            }
//            return _objects;
//
//        }
//        public void loadData(){
//            sharedDataManager.loadExhibitors();
//            sharedDataManager.loadSessions();
//            sharedDataManager.loadSessions1();
//            sharedDataManager.loadSessions2();
//            sharedDataManager.loadSessions3();
//            sharedDataManager.loadSpeakers();
//            sharedDataManager.loadSponsors();
//            sharedDataManager.loadAuctions();
//            sharedDataManager.loadUsers();
//            sharedDataManager.loadEvents();
//            sharedDataManager.loadPhotos();
//            sharedDataManager.loadAgenda();
//            sharedDataManager.loadSurveys();
//            sharedDataManager.loadLocations();
//            sharedDataManager.loadInformation();
//            sharedDataManager.loadAttendees();
////        SaveData save = new SaveData();
////        save.execute();
//
//        }
//
//        private class SaveData extends AsyncTask<String, Void, Boolean>{
//
//            @Override
//            protected Boolean doInBackground(String... params) {
//                // your background code here. Don't touch any UI components
//                sharedDataManager.loadAttendees();
//                return true;
//            }
//
//            protected void onPostExecute(Boolean result) {
//                //This is run on the UI thread so you can do as you wish here
//
//            }}
//        public List<?> load(String filename, Class c){
//            ArrayList<GTCObject> list = new ArrayList<GTCObject>();
//            try {
//
//                FileInputStream in = context.openFileInput(filename);
//                int size = in.available();
//                byte[] buffer = new byte[size];
//                in.read(buffer);
//                in.close();
//                String json = new String(buffer, "UTF-8");
//                JSONArray items = new JSONArray(json);
//                for(int i = 0; i < items.length(); i++){
//
//
//// 			   try
//// 			    {
//// 			        File root = new File(Environment.getExternalStorageDirectory(), "Notes_MayorApp");
//// 			        if (!root.exists()) {
//// 			            root.mkdirs();
//// 			        }
//// 			        File gpxfile = new File(root, filename);
//// 			        FileWriter writer = new FileWriter(gpxfile);
//// 			        writer.append(json);
//// 			        writer.flush();
//// 			        writer.close();
//// 			        System.out.println("Andy sponser data done"+Environment.getExternalStorageDirectory());
////
//// 			    }
//// 			    catch(IOException e)
//// 			    {
//// 			         e.printStackTrace();
////
//// 			    }
//                    try{
//
//
//                        JSONObject object = items.getJSONObject(i);
//                        GTCObject obj = (GTCObject) c.getConstructors()[0].newInstance();
//                        obj.load(object);
//                        list.add(obj);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//                Collections.sort(list, new Comparator<GTCObject>() {
//
//                    @Override
//                    public int compare(GTCObject lhs, GTCObject rhs) {
//                        if (lhs.getString("name") != null) {
//                            return lhs.getString("name").compareToIgnoreCase(rhs.getString("name"));
//                        } else if (lhs.getString("title") != null) {
//                            return lhs.getString("title").compareToIgnoreCase(rhs.getString("title"));
//                        } else {
//                            return 0;
//                        }
//                    }
//
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return list;
//        }
//        @SuppressWarnings("unchecked")
//        public void loadExhibitors() {
//            try{
//                exhibitors = (ArrayList<GTCExhibitors>) load("exhibitors.json",GTCExhibitors.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions() {
//            try{
//                sessions = (ArrayList<GTCSession>) load("sessions.json",GTCSession.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadAttendees() {
//            try{
//                attendee = (ArrayList<GTCAttendee>) load("attendees.json",GTCAttendee.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadSessions1() {
//            try{
//                sessions1 = (ArrayList<GTCSession1>) load("sessions1.json",GTCSession1.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions2() {
//            try{
//                sessions2 = (ArrayList<GTCSession2>) load("sessions2.json",GTCSession2.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions3() {
//            try{
//                sessions3 = (ArrayList<GTCSession3>) load("sessions3.json",GTCSession3.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadEvents() {
//            try{
//                events = (ArrayList<GTCEvent>) load("events.json",GTCEvent.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadAuctions() {
//            try{
//                auctions = (ArrayList<GTCAuction>) load("auctions.json",GTCAuction.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadUsers() {
//            try{
//                users = (ArrayList<GTCUser>) load("users.json",GTCUser.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadPhotos() {
//            try{
//                photos = (ArrayList<GTCPhoto>) load("photos.json",GTCPhoto.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSurveys() {
//            try{
//                surveys = (ArrayList<GTCSurvey>) load("surveys.json",GTCSurvey.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadLocations() {
//            try{
//                locations = (ArrayList<GTCLocation>) load("locations.json",GTCLocation.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        public void loadSpeakers(){
//            try{
//                speakers = (ArrayList<GTCSpeaker>) load("speakers.json",GTCSpeaker.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        public void loadSponsors(){
//            try{
//                sponsors = (ArrayList<GTCSponsor>) load("sponsors.json",GTCSponsor.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//
//        }
//        @SuppressWarnings("unchecked")
//        public void loadInformation(){
//            try{
//
//                ArrayList<GTCInformation> infos = (ArrayList<GTCInformation>) load("information.json",GTCInformation.class);
//                information = infos.get(0);
//                String[] dates =(String[]) information.getDates().toArray(new String[information.getDates().size()]);
//                String[] days =(String[]) information.getDays().toArray(new String[information.getDays().size()]);
//                GTCDataManager.CONF_DATES = dates;
//                System.out.println("Andy checking sesssion issues db "+GTCDataManager.CONF_DATES[0]);
//
//                GTCDataManager.CONF_DAYS = days;
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//
//        }
//        public void loadAgenda(){
//            agenda =  dbHandler.getAllSessions();
//            session_rating=  dbHandler.getAllSessionsRatings();
//        }
//        public void loadMessages(){
//            try{
//                ParseQuery<GTCMessage> query = ParseQuery.getQuery(GTCMessage.class);
//                query.findInBackground(new FindCallback<GTCMessage>() {
//
//                    @Override
//                    public void done(List<GTCMessage> objects, com.parse.ParseException e) {
//                        if(objects != null){
//                            int newMessagesCount = 0;
//                            GTCDataManager.sharedDataManager.messages = (ArrayList<GTCMessage>) objects;
//                            if(GTCDataManager.sharedDataManager.messages == null){
//                                GTCDataManager.sharedDataManager.messages = new ArrayList<GTCMessage>();
//                            }
//                            SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
//                            String messageIds  = pref.getString("TwitterMessageIds", "");
//                            String[] ids = messageIds.split(",");
//                            for(int i = 0; i < objects.size(); i++){
//                                GTCMessage message = objects.get(i);
//                                boolean exists = false;
//                                for(int j  = 0; j < ids.length; j++){
//                                    String id = ids[j];
//                                    if(id.compareToIgnoreCase(message.getObjectId()) == 0){
//                                        exists = true;
//                                        break;
//                                    }else{
//
//                                    }
//                                }
//                                if(!exists){
//                                    messageIds += "," + message.getObjectId();
//                                    newMessagesCount++;
//                                }
//                            }
//
//                            Collections.sort(messages, new MessagesComparable());
//                            Intent intent = new Intent();
//                            intent.setAction(context.getPackageName() + ".MessageCount");
//                            intent.putExtra("MessageCount", newMessagesCount);
//                            context.sendBroadcast(intent);
//                            if(newMessagesCount > 0 ){
//                                Intent nintent = new Intent(context, GTCActivity.class);
//                                PendingIntent pintent = PendingIntent.getActivity(context, 0, nintent, 0);
//                                NotificationCompat.Builder mBuilder =
//                                        new NotificationCompat.Builder(context)
//                                                .setSmallIcon(R.drawable.ic_launcher)
//                                                .setContentTitle("Update")
//                                                .setContentIntent(pintent)
//                                                .setContentText("New messages are available.");
//                                mBuilder.setAutoCancel(true);
//                                Notification notif = mBuilder.build();
//                                notif.flags |= Notification.FLAG_AUTO_CANCEL;
//                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                                notificationManager.notify(007, notif);
//                            }
//                        }
//                    }
//                });
//
//
//
//
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        public class MessagesComparable implements Comparator<GTCMessage>{
//
//            @SuppressLint("SimpleDateFormat")
//            @Override
//            public int compare(GTCMessage o1, GTCMessage o2) {
//                DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss Z y");
//                Date date1 = o1.getCreatedAt();
//                Date date2 = o2.getCreatedAt();
//                return date2.compareTo(date1);
//            }
//        }
//
//        public boolean inAgenda(GTCSession session){
//            for(GTCSession _session: agenda){
//                if(_session.getObjectId().compareToIgnoreCase(session.getObjectId()) == 0 &&
//                        _session.getDate().compareToIgnoreCase(session.getDate()) == 0 &&
//                        _session.getTime().compareToIgnoreCase(session.getTime()) == 0 )
//                    return true;
//            }
//            return false;
//        }
//        public boolean inRatings(GTCSession session){
//            for(GTCSession _session: session_rating){
//                if(_session.getObjectId().compareToIgnoreCase(session.getObjectId()) == 0 &&
//                        _session.getDate().compareToIgnoreCase(session.getDate()) == 0 &&
//                        _session.getTime().compareToIgnoreCase(session.getTime()) == 0 )
//                    return true;
//            }
//            return false;
//        }
//
//        public GTCSpeaker speakerByName(String name){
//            for(GTCSpeaker speaker:speakers){
//                if(speaker.getName().compareToIgnoreCase(name) == 0)
//                    return speaker;
//            }
//            return null;
//        }
//        public ArrayList<GTCSession> sessionBySpeaker(String name){
//            ArrayList<GTCSession> speakerSessions = new ArrayList<GTCSession>();
//            for(GTCSession session:sharedDataManager.sessions){
//                if(session.getSpeakers().contains(name)){
//                    speakerSessions.add(session);
//                }
//            }
//            return speakerSessions;
//        }
//        public ArrayList<String> sortedByDates(ArrayList<String> _dates){
//            Collections.sort(_dates, new StringDateComparator());
//            return null;
//        }
//        public  HashMap<String,ArrayList<GTCSurvey>> groupedExhibitorsByField(ArrayList<GTCSurvey> _exhibitors, String _field){
//            try{
//                HashMap<String,ArrayList<GTCSurvey>> groupedExhibitors = new HashMap<String,ArrayList<GTCSurvey>>();
//                for(GTCSurvey session : _exhibitors){
//                    String fieldValue = session.getKeyVals().get(_field);
//                    if(groupedExhibitors.containsKey(fieldValue)){
//                        ArrayList<GTCSurvey>  __exhibitors = groupedExhibitors.get(fieldValue);
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }else{
//                        ArrayList<GTCSurvey> __exhibitors = new ArrayList<GTCSurvey>();
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }
//                }
//                for(String key : groupedExhibitors.keySet()){
//                    ArrayList<GTCSurvey> __exhibitors = groupedExhibitors.get(key);
//                    Collections.sort(__exhibitors, new GTCSurveysComparator());
//
//
//                }
//                return groupedExhibitors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSurvey>>();
//            }
//
//        }
//
//        public HashMap<String,ArrayList<GTCUser>> groupedUsersByInitial(ArrayList<GTCUser> _users){
//            try{
//                HashMap<String,ArrayList<GTCUser>> groupedUsers = new HashMap<String,ArrayList<GTCUser>>();
//                for(GTCUser user : _users){
//                    String fieldValue = user.getLastName().charAt(0) + "";
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCUser>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCUser> __users = new ArrayList<GTCUser>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCUser> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCUsersComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCUser>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCAttendee>> groupedAttendeeByInitial(ArrayList<GTCAttendee> _users){
//            try{
//                HashMap<String,ArrayList<GTCAttendee>> groupedUsers = new HashMap<String,ArrayList<GTCAttendee>>();
//                for(GTCAttendee user : _users){
//
//                    String fieldValue ="";
//                    if(user.getLastName().length()>0)
//                    {
//                        fieldValue = user.getLastName().trim().charAt(0) + "";
//
//                    }
//                    else if(user.getFirstName().length()>0)
//                    {
//                        fieldValue= user.getFirstName().trim().charAt(0) + "";
//                    }
//                    else
//                    {
//                        continue;
//                    }
//
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCAttendee>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCAttendee> __users = new ArrayList<GTCAttendee>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCAttendee> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCAttendeeComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                System.out.println("Ady checking size of list here "+e);
//
//                return new HashMap<String,ArrayList<GTCAttendee>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCSpeaker>> groupedSpeakersByInitial(ArrayList<GTCSpeaker> _users){
//            try{
//                HashMap<String,ArrayList<GTCSpeaker>> groupedUsers = new HashMap<String,ArrayList<GTCSpeaker>>();
//                for(GTCSpeaker user : _users){
//                    String nameTokens[] = user.getName().split(" ");
//                    String fieldValue = (nameTokens.length > 1)?nameTokens[1].charAt(0) + "":nameTokens[0].charAt(0) + "";
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCSpeaker>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCSpeaker> __users = new ArrayList<GTCSpeaker>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCSpeaker> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCSpeakersComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSpeaker>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCSurvey>> groupedExhibitorsByInitial(ArrayList<GTCSurvey> _exhibitors){
//            try{
//                HashMap<String,ArrayList<GTCSurvey>> groupedExhibitors = new HashMap<String,ArrayList<GTCSurvey>>();
//                for(GTCSurvey session : _exhibitors){
//                    String fieldValue = session.getKeyVals().get("Company").charAt(0) + "";
//                    if(groupedExhibitors.containsKey(fieldValue)){
//                        ArrayList<GTCSurvey>  __exhibitors = groupedExhibitors.get(fieldValue);
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }else{
//                        ArrayList<GTCSurvey> __exhibitors = new ArrayList<GTCSurvey>();
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }
//                }
//                for(String key : groupedExhibitors.keySet()){
//                    ArrayList<GTCSurvey> __exhibitors = groupedExhibitors.get(key);
//                    Collections.sort(__exhibitors, new GTCSurveysComparator());
//
//
//                }
//                return groupedExhibitors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSurvey>>();
//            }
//
//        }
//        public  HashMap<String,ArrayList<GTCSponsor>> groupedSponsorsByField(ArrayList<GTCSponsor> _sponsors, String _field){
//            try{
//                HashMap<String,ArrayList<GTCSponsor>> groupedSponsors = new HashMap<String,ArrayList<GTCSponsor>>();
//                for(GTCSponsor session : _sponsors){
//                    String fieldValue = session.getKeyVals().get(_field);
//                    if(groupedSponsors.containsKey(fieldValue)){
//                        ArrayList<GTCSponsor>  __sponsors = groupedSponsors.get(fieldValue);
//                        __sponsors.add(session);
//                        groupedSponsors.put(fieldValue, __sponsors);
//                    }else{
//                        ArrayList<GTCSponsor> __sponsors = new ArrayList<GTCSponsor>();
//                        __sponsors.add(session);
//                        groupedSponsors.put(fieldValue, __sponsors);
//                    }
//                }
//                for(String key : groupedSponsors.keySet()){
//                    ArrayList<GTCSponsor> __sponsors = groupedSponsors.get(key);
//                    Collections.sort(__sponsors, new Comparator<GTCSponsor>(){
//
//                        @Override
//                        public int compare(GTCSponsor lhs, GTCSponsor rhs) {
//
//                            return lhs.getOrder().intValue() - rhs.getOrder().intValue();
//                        }
//
//                    });
//
//
//                }
//                return groupedSponsors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSponsor>>();
//            }
//
//        }
//
//
//
//        public  HashMap<String,ArrayList<GTCSession>> groupedSessionsByField(ArrayList<GTCSession> _sessions, String _field){
//            String andy=null;
//            System.out.println("Andy checking excetion in time db"+_field);
//            try{
//                HashMap<String,ArrayList<GTCSession>> groupedSessions = new HashMap<String,ArrayList<GTCSession>>();
//                for(GTCSession session : _sessions){
//
//                    String fieldValue = session.getKeyVals().get(_field);
////				andy=session.getTitle();
//                    System.out.println("Andy checking excetion in time value "+fieldValue);
//                    if(groupedSessions.containsKey(fieldValue)){
//                        ArrayList<GTCSession>  __sessions = groupedSessions.get(fieldValue);
//                        __sessions.add(session);
//                        groupedSessions.put(fieldValue, __sessions);
//                    }else{
//                        ArrayList<GTCSession> __sessions = new ArrayList<GTCSession>();
//                        __sessions.add(session);
//                        groupedSessions.put(fieldValue, __sessions);
//                    }
//                }
//                for(String key : groupedSessions.keySet()){
//                    ArrayList<GTCSession> __sessions = groupedSessions.get(key);
//                    Collections.sort(__sessions, new GTCSessionComparator());
//
//
//                }
//                return groupedSessions;
//            }catch(Exception e){
//                System.out.println("Andy checking excetion in time "+e);
//
//                return new HashMap<String,ArrayList<GTCSession>>();
//            }
//
//        }
//
//
//
//        public class TwitterTask extends TimerTask {
//
//            @Override
//            public void run() {
//
//                GTCDataManager.sharedDataManager.loadMessages();
//            }
//
//        }
//
////CREATE TABLE SCAN (id INTEGER PRIMARY KEY NOT NULL, text TEXT NOT NULL, stamp REAL DEFAULT 0.0)
////CREATE TABLE AGENDA (id INTEGER PRIMARY KEY NOT NULL, name TEXT, date TEXT, time TEXT, location TEXT, type TEXT, description TEXT, speaker1 TEXT, speaker2 TEXT, speaker3 TEXT, speaker4 TEXT, speaker5 TEXT)
//
//
//        public class DatabaseHandler extends SQLiteOpenHelper {
//
//            // All Static variables
//            // Database Version
//            private static final int DATABASE_VERSION = 1;
//            //	 private static final int DATABASE_VERSION = 2;
//            // Database Name
//            private static final String DATABASE_NAME = "SCANS_2015";
//
//            // Scan table name
//            private static final String TABLE_SCAN = "SCAN";
//
//            //Agenda table name
//            private static final String TABLE_AGENDA = "AGENDA";
//            //Scan Table Columns names
//            private static final String SCAN_KEY_ID = "id";
//            private static final String SCAN_KEY_TEXT = "text";
//            private static final String SCAN_KEY_STAMP = "stamp";
//
//            //Agenda table Column names
////    private static final String AGENDA_KEY_ID = "id";
//            private static final String AGENDA_OBJECT_ID = "object_id";
//            private static final String AGENDA_KEY_NAME = "name";
//            private static final String AGENDA_KEY_DATE = "date";
//            private static final String AGENDA_KEY_TIME = "time";
//            private static final String AGENDA_KEY_TYPE = "type";
//            private static final String AGENDA_KEY_LOCATION = "location";
//            private static final String AGENDA_KEY_DESCRIPTION = "description";
//            private static final String AGENDA_KEY_SPEAKER = "speaker";
//            private static final String AGENDA_KEY_TOPIC = "topic";
//            private static final String AGENDA_KEY_ORDER_ID  = "order_id";
//
//            //Agenda table Column names
//            private static final String TABLE_RSTING = "RATING";
//            private static final String RSTING_OBJECT_ID = "object_id";
//
//            //    private static final String RSTING_KEY_ID = "id";
//            private static final String RSTING_KEY_NAME = "name";
//            private static final String RSTING_KEY_DATE = "date";
//            private static final String RSTING_KEY_TIME = "time";
//            private static final String RSTING_KEY_TYPE = "type";
//            private static final String RSTING_KEY_LOCATION = "location";
//            private static final String RSTING_KEY_DESCRIPTION = "description";
//            private static final String RSTING_KEY_SPEAKER = "speaker";
//
//
//            public DatabaseHandler(Context context) {
//                super(context, DATABASE_NAME, null, DATABASE_VERSION);
//            }
//
//            // Creating Tables
//            @Override
//            public void onCreate(SQLiteDatabase db) {
//                String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_SCAN + "("
//                        + SCAN_KEY_ID + " INTEGER PRIMARY KEY," + SCAN_KEY_TEXT + " TEXT,"
//                        + SCAN_KEY_STAMP + " TEXT" + ")";
//                db.execSQL(CREATE_SCAN_TABLE);
////        String CREATE_AGENDA_TABLE = "CREATE TABLE " + TABLE_AGENDA + "("
////                + AGENDA_KEY_ID + " INTEGER PRIMARY KEY," + AGENDA_KEY_NAME + " TEXT,"
////                + AGENDA_KEY_DATE + " TEXT," + AGENDA_KEY_TIME + " TEXT," + AGENDA_KEY_TYPE + " TEXT," + AGENDA_KEY_LOCATION + " TEXT," + AGENDA_KEY_DESCRIPTION + " TEXT,"+AGENDA_KEY_TOPIC + " TEXT," + AGENDA_KEY_SPEAKER + "1 TEXT," + AGENDA_KEY_SPEAKER + "2 TEXT," + AGENDA_KEY_SPEAKER + "3 TEXT," + AGENDA_KEY_SPEAKER + "4 TEXT," + AGENDA_KEY_SPEAKER + "5 TEXT," +AGENDA_KEY_ORDER_ID+ " TEXT" + ")";
////        db.execSQL(CREATE_AGENDA_TABLE);
//
//                String CREATE_AGENDA_TABLE = "CREATE TABLE " + TABLE_AGENDA + "("
//                        + AGENDA_OBJECT_ID + " TEXT PRIMARY KEY," + AGENDA_KEY_NAME + " TEXT,"
//                        + AGENDA_KEY_DATE + " TEXT," + AGENDA_KEY_TIME + " TEXT," + AGENDA_KEY_TYPE + " TEXT," + AGENDA_KEY_LOCATION + " TEXT," + AGENDA_KEY_DESCRIPTION + " TEXT,"+AGENDA_KEY_TOPIC + " TEXT," + AGENDA_KEY_SPEAKER + "1 TEXT," + AGENDA_KEY_SPEAKER + "2 TEXT," + AGENDA_KEY_SPEAKER + "3 TEXT," + AGENDA_KEY_SPEAKER + "4 TEXT," + AGENDA_KEY_SPEAKER + "5 TEXT"+ ")";
//                db.execSQL(CREATE_AGENDA_TABLE);
//
//                String CREATE_RATING_TABLE = "CREATE TABLE " + TABLE_RSTING + "("
//                        + RSTING_OBJECT_ID + " TEXT PRIMARY KEY," + RSTING_KEY_NAME + " TEXT,"
//                        + RSTING_KEY_DATE + " TEXT," + RSTING_KEY_TIME + " TEXT," + RSTING_KEY_TYPE + " TEXT," + RSTING_KEY_LOCATION + " TEXT," + RSTING_KEY_DESCRIPTION + " TEXT," + RSTING_KEY_SPEAKER + "1 TEXT," + RSTING_KEY_SPEAKER + "2 TEXT," + RSTING_KEY_SPEAKER + "3 TEXT," + RSTING_KEY_SPEAKER + "4 TEXT," + RSTING_KEY_SPEAKER + "5 TEXT" + ")";
//                db.execSQL(CREATE_RATING_TABLE);
//
//            }
//
//            // Upgrading database
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//                // Drop older table if existed
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCAN);
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDA);
////        String upgradeQuery = "ALTER TABLE "+ TABLE_AGENDA+" ADD COLUMN "+AGENDA_KEY_ORDER_ID+" TEXT";
////        if (oldVersion == 1 && newVersion == 2)
////             db.execSQL(upgradeQuery);
//                // Create tables again
//                onCreate(db);
//            }
//            public void alterAgendaTable() {
//                SQLiteDatabase db = this.getWritableDatabase();
//                String upgradeQuery = "ALTER TABLE "+ TABLE_AGENDA+" ADD COLUMN "+AGENDA_KEY_ORDER_ID+" TEXT";
//                db.execSQL(upgradeQuery);
//
//                db.close();
//            }
//            /**
//             * All CRUD(Create, Read, Update, Delete) Operations
//             */
//
//            // Adding new contact
//            void addScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(SCAN_KEY_TEXT, scan.text); // Scan Text
//                values.put(SCAN_KEY_STAMP, scan.stamp.getTime()); // Scan timestamp
//
//                // Inserting Row
//                scan.ident = (int)db.insert(TABLE_SCAN, null, values);
//                db.close(); // Closing database connection
//            }
//
//            // Getting single contact
//            GTCScan getScan(int id) {
//                SQLiteDatabase db = this.getReadableDatabase();
//
//                Cursor cursor = db.query(TABLE_SCAN, new String[] { SCAN_KEY_ID,
//                                SCAN_KEY_TEXT, SCAN_KEY_STAMP }, SCAN_KEY_ID + "=?",
//                        new String[] { String.valueOf(id) }, null, null, null, null);
//                if (cursor != null)
//                    cursor.moveToFirst();
//                GTCScan scan = null;
//                try{
//                    scan = new GTCScan(Integer.parseInt(cursor.getString(0)),
//                            cursor.getString(1), new SimpleDateFormat(GTCDataManager.DATE_TIME_FORMAT).parse(cursor.getString(2)));
//                }catch(Exception e){
//
//                }
//
//                return scan;
//            }
//
//            // Getting All Scans
//            public ArrayList<GTCScan> getAllScans() {
//                ArrayList<GTCScan> scanList = new ArrayList<GTCScan>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_SCAN;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        Date d = new Date(Long.parseLong(cursor.getString(2)));
//                        GTCScan scan = new GTCScan(Integer.parseInt(cursor.getString(0)),cursor.getString(1),d);
//                        // Adding contact to list
//                        scanList.add(scan);
//                    } while (cursor.moveToNext());
//                }
//
//                // return scan list
//                return scanList;
//            }
//
//            // Updating single scan
//            public int updateScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(SCAN_KEY_TEXT, scan.text);
//                values.put(SCAN_KEY_STAMP, scan.stamp.getTime());
//
//                // updating row
//                return db.update(TABLE_SCAN, values, SCAN_KEY_ID + " = ?",
//                        new String[] { String.valueOf(scan.ident) });
//            }
//
//            // Deleting single scan
//            public void deleteScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//                db.delete(TABLE_SCAN, SCAN_KEY_ID + " = ?",
//                        new String[] { String.valueOf(scan.ident) });
//                db.close();
//            }
//
//            // Getting scans Count
//            public int getScansCount() {
//                String countQuery = "SELECT  * FROM " + TABLE_SCAN;
//                SQLiteDatabase db = this.getReadableDatabase();
//                Cursor cursor = db.rawQuery(countQuery, null);
//                cursor.close();
//
//                // return count
//                return cursor.getCount();
//            }
//
//            // Adding new Session
//            long addSession(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(AGENDA_OBJECT_ID, session.getObjectId());
//                values.put(AGENDA_KEY_NAME, session.getTitle()); // Session name
//                values.put(AGENDA_KEY_DATE, session.getDate()); // Session date
//                values.put(AGENDA_KEY_TIME, session.getTime()); // Session time
//                values.put(AGENDA_KEY_TYPE, session.getType()); // Session type
//                values.put(AGENDA_KEY_LOCATION, session.getLocation()); // Session location
////        values.put(AGENDA_KEY_ORDER_ID, session.getOrder());
////        values.put(AGENDA_KEY_DESCRIPTION, session.getDetail()); // Session description
//                values.put(AGENDA_KEY_DESCRIPTION, session.getDescription());
//                values.put(AGENDA_KEY_TOPIC, session.getTopic());
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(AGENDA_KEY_SPEAKER + (i + 1), session.getSpeakers().get(i)); // Session spkear
//                }
//
//                // Inserting Row
//                long cursorVal = db.insert(TABLE_AGENDA, null, values);
////        copyDataBaseBack();
//                db.close(); // Closing database connection
//                return cursorVal;
//            }
//
//
//            // Adding new Session
//            long addSessionRatings(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(RSTING_OBJECT_ID, session.getObjectId());
//                values.put(RSTING_KEY_NAME, session.getTitle()); // Session name
//                values.put(RSTING_KEY_DATE, session.getDate()); // Session date
//                values.put(RSTING_KEY_TIME, session.getTime()); // Session time
//                values.put(RSTING_KEY_TYPE, session.getType()); // Session type
//                values.put(RSTING_KEY_LOCATION, session.getLocation()); // Session location
//                values.put(RSTING_KEY_DESCRIPTION, session.getDetail()); // Session description
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(RSTING_KEY_SPEAKER + (i + 1), session.getSpeakers().get(i)); // Session spkear
//                }
//
//                // Inserting Row
//                long cursorVal = db.insert(TABLE_RSTING, null, values);
//
//
//                db.close(); // Closing database connection
//                return cursorVal;
//            }
//            // int getSessionId(GTCSession session){
////
////	 SQLiteDatabase db = this.getReadableDatabase();
////
////     Cursor cursor = db.query(TABLE_AGENDA, new String[] { AGENDA_OBJECT_ID }, AGENDA_KEY_NAME + "=? AND " + AGENDA_KEY_DATE + "=? AND " + AGENDA_KEY_TIME + "=?",
////             new String[] { session.getTitle()+"",session.getDate(), session.getTime()}, null, null, null, null);
////     if (cursor != null)
////         if(cursor.moveToFirst())
////        	 return cursor.getInt(0);
////     return 0;
//// }
//            // Getting single session
//            GTCSession getSession(String id) {
//                SQLiteDatabase db = this.getReadableDatabase();
//
//                Cursor cursor = db.query(TABLE_AGENDA, new String[] { AGENDA_OBJECT_ID,
//                                AGENDA_KEY_NAME, AGENDA_KEY_DATE, AGENDA_KEY_TIME, AGENDA_KEY_TYPE, AGENDA_KEY_LOCATION, AGENDA_KEY_DESCRIPTION, AGENDA_KEY_SPEAKER + "1",AGENDA_KEY_SPEAKER + "2",AGENDA_KEY_SPEAKER + "3",AGENDA_KEY_SPEAKER + "4",AGENDA_KEY_SPEAKER + "5" }, AGENDA_OBJECT_ID + "=?",
//                        new String[] { String.valueOf(id) }, null, null, null, null);
//                if (cursor != null)
//                    cursor.moveToFirst();
//                GTCSession session = null;
//                try{
//                    session = new GTCSession();
//                    session.setObjectId(cursor.getString(0));
//                    session.setTitle(cursor.getString(1));
//                    session.setDate(cursor.getString(2));
//                    session.setTime(cursor.getString(3));
//                    session.setType(cursor.getString(4));
//                    session.setLocation(cursor.getString(5));
//                    session.setDetail(cursor.getString(6));
//                    for(int i = 7; i < 12; i++){
//                        String speaker = cursor.getString(i);
//                        if(speaker != null){
//                            speaker = speaker.trim();
//                            if(speaker.length() > 0)
//                                session.addSpeaker(speaker);
//                            int k = i - 7;
//                            session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//                        }
//                    }
//                    //session.setKeyVals(t)
//                    session.setDates();
//
//                }catch(Exception e){
//
//                }
//
//                return session;
//            }
//
//            // Getting All Sessions
//            public ArrayList<GTCSession> getAllSessions() {
//                ArrayList<GTCSession> agendaList = new ArrayList<GTCSession>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_AGENDA;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        GTCSession session = null;
//                        try{
//                            session = new GTCSession();
//                            session.setObjectId(cursor.getString(0));
//                            session.setTitle(cursor.getString(1));
//                            session.setDate(cursor.getString(2));
//                            session.setTime(cursor.getString(3));
//                            session.setType(cursor.getString(4));
//                            session.setLocation(cursor.getString(5));
//                            session.setDescription(cursor.getString(6));
//                            session.setTopic(cursor.getString(7));
////                session.setOrder(Integer.parseInt(cursor.getString(13)));
//
//                            for(int i = 8; i < 13; i++){
//                                String speaker = cursor.getString(i);
//                                if(speaker != null){
//                                    speaker = speaker.trim();
//                                    if(speaker.length() > 0)
//                                        session.addSpeaker(speaker);
//                                }
//                                int k = i - 7;
//                                session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//
//                            }
//                            //session.setKeyVals();
//                            session.setDates();
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        // Adding Session to list
//                        agendaList.add(session);
//                    } while (cursor.moveToNext());
//                }
//
//                // return sessions list
//                return agendaList;
//            }
//            // Getting All Sessions
//            public ArrayList<GTCSession> getAllSessionsRatings() {
//                ArrayList<GTCSession> agendaList = new ArrayList<GTCSession>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_RSTING;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        GTCSession session = null;
//                        try{
//                            session = new GTCSession();
//                            session.setObjectId(cursor.getString(0));
//                            session.setTitle(cursor.getString(1));
//                            session.setDate(cursor.getString(2));
//                            session.setTime(cursor.getString(3));
//                            session.setType(cursor.getString(4));
//                            session.setLocation(cursor.getString(5));
//                            session.setDetail(cursor.getString(6));
//                            for(int i = 7; i < 12; i++){
//                                String speaker = cursor.getString(i);
//                                if(speaker != null){
//                                    speaker = speaker.trim();
//                                    if(speaker.length() > 0)
//                                        session.addSpeaker(speaker);
//                                }
//                                int k = i - 6;
//                                session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//
//                            }
//                            //session.setKeyVals();
//                            session.setDates();
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        // Adding Session to list
//                        agendaList.add(session);
//                    } while (cursor.moveToNext());
//                }
//
//                // return sessions list
//                return agendaList;
//            }
//            // Updating single session
//            public int updateSession(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(AGENDA_KEY_NAME, session.getTitle());
//                values.put(AGENDA_KEY_DATE, session.getDate());
//                values.put(AGENDA_KEY_TIME, session.getTime());
//                values.put(AGENDA_KEY_TYPE, session.getType());
//                values.put(AGENDA_KEY_LOCATION, session.getLocation());
//                values.put(AGENDA_KEY_DESCRIPTION, session.getDetail());
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(AGENDA_KEY_SPEAKER + i,session.getSpeakers().get(i) );
//                }
//
//                // updating row
//                return db.update(TABLE_AGENDA, values, AGENDA_OBJECT_ID + " = ?",
//                        new String[] { String.valueOf(session.getObjectId()) });
//            }
//            public void copyDataBaseBack() {
//                Log.i("info", "in copy data base at finally");
//                try {
//                    File sd = Environment.getExternalStorageDirectory();
//                    File data = Environment.getDataDirectory();
//                    System.out.println("Andy path "+sd.canWrite());
//
//                    if (sd.canWrite()) {
//                        String currentDBPath = "/data/" + context.getPackageName()
//                                + "/databases/SCANS_2015";
//                        String backupDBPath = "SCANS_2015.sqlite";
//                        File currentDB = new File(data, currentDBPath);
//                        File backupDB = new File(sd, backupDBPath);
//                        if (currentDB.exists()) {
//                            FileChannel src = new FileInputStream(currentDB)
//                                    .getChannel();
//                            FileChannel dst = new FileOutputStream(backupDB)
//                                    .getChannel();
//                            dst.transferFrom(src, 0, src.size());
//                            src.close();
//                            dst.close();
//                        }
//                    }
//
//                } catch (Exception e) {
//
//                    Log.i("info", "in copy of bata base 10 ");
//
//                }
//            }
//            // Deleting single session
//            public void deleteSession(GTCSession session) {
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                db.delete(TABLE_AGENDA, AGENDA_OBJECT_ID + " = ?",
//                        new String[] { String.valueOf(session.getObjectId()) });
//                db.close();
//            }
//
//            // Getting sessions Count
//            public int getSessionsCount() {
//                String countQuery = "SELECT  * FROM " + TABLE_AGENDA;
//                SQLiteDatabase db = this.getReadableDatabase();
//                Cursor cursor = db.rawQuery(countQuery, null);
//                cursor.close();
//
//                // return count
//                return cursor.getCount();
//            }
//        }
//        public class StringDateComparator implements Comparator<String> {
//
//            @Override
//            public int compare(String lhs, String rhs) {
//
//                Date d1 = null;
//                Date d2 = null;
//                try {
//                    d1 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(lhs);
//
//                } catch (ParseException e) {
//
//                    try{
//                        String tokens1[] = lhs.split("-");
//                        if(tokens1.length > 1){
//                            lhs = tokens1[1];
//                        }else{
//                            lhs = tokens1[0];
//                        }
//
//                        d1 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(lhs);
//                    }catch(ParseException e1){
//                        e1.printStackTrace();
//
//                    }
//
//                }
//                try{
//                    d2 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(rhs);
//
//                }catch (ParseException e) {
//
//                    try{
//                        String tokens2[] = rhs.split("-");
//                        if(tokens2.length > 1){
//                            rhs = tokens2[1];
//                        }else{
//                            rhs = tokens2[0];
//                        }
//
//                        d2 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(rhs);
//                        return d1.compareTo(d2);
//                    }catch(ParseException e1){
//                        e1.printStackTrace();
//
//                    }
//
//
//
//                }
//                return d1.compareTo(d2);
//
//            }
//        }
//        public class GTCSessionComparator implements Comparator<GTCSession>{
//
//            @Override
//            public int compare(GTCSession lhs, GTCSession rhs) {
//
//                return ((GTCSession)lhs).getOrder() - ((GTCSession)rhs).getOrder();
//            }
//        }
//
//        public class GTCSpeakersComparator implements Comparator<GTCSpeaker>{
//
//            @Override
//            public int compare(GTCSpeaker lhs, GTCSpeaker rhs) {
//                return lhs.getOrder() - rhs.getOrder();
//            }
//
//        }
//        public class GTCSurveysComparator implements Comparator<GTCSurvey>{
//
//            @Override
//            public int compare(GTCSurvey lhs, GTCSurvey rhs) {
//
//                return ((GTCSurvey)lhs).getOrder() - ((GTCSurvey)rhs).getOrder();
//            }
//        }
//
//        public class GTCUsersComparator implements Comparator<GTCUser>{
//
//            @Override
//            public int compare(GTCUser lhs, GTCUser rhs) {
//
//                return ((GTCUser)lhs).getFirstName().compareTo(((GTCUser)rhs).getFirstName());
//            }
//        }
//        public class GTCAttendeeComparator implements Comparator<GTCAttendee>{
//
//            @Override
//            public int compare(GTCAttendee lhs, GTCAttendee rhs) {
//
//                return ((GTCAttendee)lhs).getFirstName().compareTo(((GTCAttendee)rhs).getFirstName());
//            }
//        }
//
//        public static int getResId(String variableName, Context context, Class<?> c) {
//
//            try {
//                Field idField = c.getDeclaredField(variableName);
//                return idField.getInt(idField);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return -1;
//            }
//        }
//        public boolean isLoggedIn(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getBoolean("IsLoggedIn",false);
//
//        }
//        public String getStudyYear(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getString("Year","Alumni Events");
//
//        }
//        public String getEmail(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getString("Email",null);
//
//        }
//        public void login(String email, final ILoginCallback callback){
//            ParseUser.logInInBackground(email, "123456", new LogInCallback(){
//                @Override
//                public void done(final ParseUser user, final com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Logged in with email and pass ...");
//                    if(user != null){
//                        Crashlytics.log(Log.INFO, "STS", "User is not null ...");
//                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                        final SharedPreferences.Editor editor = settings.edit();
//                        editor.putBoolean("IsLoggedIn", true);
//                        editor.putString("Email", user.getEmail());
//                        editor.putString("UserName", user.getUsername());
//                        try{
//                            GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                            editor.commit();
//                            Crashlytics.log(Log.INFO, "STS", "Going to fetch user profile ...");
//                            uprof.fetchInBackground(new GetCallback<GTCUser>(){
//
//                                @Override
//                                public void done(GTCUser uprof,
//                                                 com.parse.ParseException arg1) {
//                                    Crashlytics.log(Log.INFO, "STS", "Fetched user profile...");
//                                    if(uprof != null){
//                                        Crashlytics.log(Log.INFO, "STS", "Profile is not null ...");
//                                        editor.putString("Year", uprof.getStudyYear());
//                                        editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                        editor.commit();
//                                        callback.onLogin(true, e,user);
//                                    }else{
//                                        if(e != null){
//                                            Crashlytics.log(Log.INFO, "STS", "Profile is null ...");
//                                            Crashlytics.logException(e);
//
//                                        }else{
//                                            arg1 = new com.parse.ParseException(0, "Profile is null");
//                                            Crashlytics.logException(e);
//                                        }
//                                        callback.onLogin(false, e,user);
//                                    }
//                                }
//
//                            });
//                        }catch(Exception e1){
//                            Crashlytics.log(Log.INFO, "STS", "An exception occurred ..." + e1.getMessage());
//                            Crashlytics.logException(e1);
//                            callback.onLogin(false, e,user);
//                        }
//
//                    }else{
//                        Crashlytics.log(Log.INFO, "STS", "User is null ...");
//                        Crashlytics.logException(e);
//                        callback.onLogin((user == null)?false:true, e,user);
//                    }
//
//
//                }
//
//            });
//        }
//        public void login(String email,String password, final ILoginCallback callback){
//            ParseUser.logInInBackground(email, password, new LogInCallback(){
//                @Override
//                public void done(final ParseUser user, final com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Logged in with email and pass ...");
//                    if(user != null){
//                        Crashlytics.log(Log.INFO, "STS", "User is not null ...");
//                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                        final SharedPreferences.Editor editor = settings.edit();
//                        editor.putBoolean("IsLoggedIn", true);
//                        editor.putString("Email", user.getEmail());
//                        editor.putString("UserName", user.getUsername());
//
//                        try{
//                            GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                            editor.commit();
//                            Crashlytics.log(Log.INFO, "STS", "Going to fetch user profile ...");
//                            uprof.fetchInBackground(new GetCallback<GTCUser>(){
//
//                                @Override
//                                public void done(GTCUser uprof,
//                                                 com.parse.ParseException arg1) {
//                                    Crashlytics.log(Log.INFO, "STS", "Fetched user profile...");
//                                    if(uprof != null){
//                                        Crashlytics.log(Log.INFO, "STS", "Profile is not null ...");
//                                        writing_profilepic_external(uprof.getImage(), uprof.getFirstName()+"_"+uprof.getLastName());
//                                        editor.putString("Year", uprof.getStudyYear());
//                                        editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                        editor.putString("FbFirstName", uprof.getFirstName());
//                                        editor.putString("FbLastName", uprof.getLastName());
//                                        editor.putString("FbEmail", uprof.getUserEmail());
//                                        editor.putString("FbUserId", "");
//                                        editor.commit();
//                                        callback.onLogin(true, e,user);
//                                    }else{
//                                        if(e != null){
//                                            Crashlytics.log(Log.INFO, "STS", "Profile is null ...");
//                                            Crashlytics.logException(e);
//
//                                        }else{
//                                            arg1 = new com.parse.ParseException(0, "Profile is null");
//                                            Crashlytics.logException(e);
//                                        }
//                                        callback.onLogin(false, e,user);
//                                    }
//                                }
//
//                            });
//                        }catch(Exception e1){
//                            Crashlytics.log(Log.INFO, "STS", "An exception occurred ..." + e1.getMessage());
//                            Crashlytics.logException(e1);
//                            callback.onLogin(false, e,user);
//                        }
//
//                    }else{
//                        Crashlytics.log(Log.INFO, "STS", "User is null ...");
//                        Crashlytics.logException(e);
//                        callback.onLogin((user == null)?false:true, e,user);
//                    }
//
//
//                }
//
//            });
//        }
//        void writing_profilepic_external(ParseFile file_this,String image_name)
//        {
//            if(file_this!=null)
//            {
//                ParseFile file = (ParseFile) file_this;
//
//                try {
//                    if(file.getName() != null){
//                        String[] filenameTokens = file.getName().split("-");
//                        System.out.println("Andy files check this "+image_name);
//
//                        String fullPath = "/data/data/" + context.getPackageName() + "/files/"+image_name;
//
//                        File file_check = new File(fullPath);
//
//
//                        System.out.println("Andy data parse false");
//                        byte[] data;
//                        try {
//                            data = file.getData();
//                            FileOutputStream stream = context.openFileOutput(image_name,Context.MODE_PRIVATE);
//                            stream.write(data);
//                            stream.close();
//                        } catch (com.parse.ParseException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//
//
//                    }	} catch (FileNotFoundException e) {
//
//                    e.printStackTrace();
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        public void register(final String email, final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//
//            final ParseUser user = ParseUser.getCurrentUser();
////	user.setEmail(email);
//            user.setUsername(email);
//            user.setPassword("123456");
//            Crashlytics.log(Log.INFO, "STS", "Saving in background...");
//            user.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Save yielded ...");
//                    if( e == null ){
//                        Crashlytics.log(Log.INFO, "STS", "Saved and no exception...");
//                        final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
//                        uprof.setUserEmail(email);
//                        uprof.setUsername(email);
//                        uprof.setFirstName(firstname);
//                        uprof.setLastName(lastname);
//                        if(!year.isEmpty())
//                            uprof.setStudyYear(year);
//                        user.put("profile",uprof);
//                        Crashlytics.log(Log.INFO, "STS", "Saving profile in background ...");
//                        user.saveInBackground(new SaveCallback(){
//
//                            @Override
//                            public void done(com.parse.ParseException e) {
//                                Crashlytics.log(Log.INFO, "STS", "Saving profile yielded...");
//                                if(e == null){
//                                    Crashlytics.log(Log.INFO, "STS", "Profile saved and no exception ...");
//                                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings.edit();
//                                    editor.putBoolean("IsLoggedIn", true);
//                                    editor.putString("Email", user.getEmail());
//                                    editor.putString("UserName", user.getUsername());
//                                    GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                                    editor.putString("Year", uprof.getStudyYear());
//                                    editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                    editor.commit();
//                                    callback.onRegister(true,e, user);
//                                    uprof.setUser(user);
//                                    uprof.setImage(image);
//                                    uprof.setThumbnail(thumb);
//                                    uprof.saveInBackground();
//                                }else{
//                                    Crashlytics.log(Log.INFO, "STS", "Profile save had an exception ..." + e.getMessage());
//                                    Crashlytics.logException(e);
//                                    callback.onRegister(false, e,null);
//                                }
//
//                            }
//
//                        });
//                    }else{
//
//                        Crashlytics.log(Log.INFO, "STS", "Save had an exception ..." + e.getMessage());
//                        Crashlytics.logException(e);
//                        callback.onRegister(false, e,null);
//                    }
//
//                }
//
//            });
//
//        }
//        public void register(final String email, final String password,final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//
//            final ParseUser user = ParseUser.getCurrentUser();
//            user.setEmail(email);
//            user.setUsername(email);
//            user.setPassword(password);
//            Crashlytics.log(Log.INFO, "STS", "Saving in background...");
//            user.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Save yielded ...");
//                    if( e == null ){
//                        Crashlytics.log(Log.INFO, "STS", "Saved and no exception...");
//                        final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
//                        uprof.setUserEmail(email);
//                        uprof.setUsername(email);
//                        uprof.setFirstName(firstname);
//                        uprof.setLastName(lastname);
//                        if(!year.isEmpty())
//                            uprof.setStudyYear(year);
//                        user.put("profile",uprof);
//                        Crashlytics.log(Log.INFO, "STS", "Saving profile in background ...");
//                        user.saveInBackground(new SaveCallback(){
//
//                            @Override
//                            public void done(com.parse.ParseException e) {
//                                Crashlytics.log(Log.INFO, "STS", "Saving profile yielded...");
//                                if(e == null){
//                                    Crashlytics.log(Log.INFO, "STS", "Profile saved and no exception ...");
//                                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings.edit();
////					    editor.putBoolean("IsLoggedIn", true);
////					    editor.putString("Email", user.getEmail());
////					    editor.putString("UserName", user.getUsername());
//                                    GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                                    editor.putString("Year", uprof.getStudyYear());
//                                    editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                    editor.commit();
//                                    callback.onRegister(true,e, user);
//                                    uprof.setUser(user);
//                                    uprof.setImage(image);
//                                    uprof.setThumbnail(thumb);
//                                    uprof.saveInBackground();
//                                }else{
//                                    Crashlytics.log(Log.INFO, "STS", "Profile save had an exception ..." + e.getMessage());
//                                    Crashlytics.logException(e);
//                                    callback.onRegister(false, e,null);
//                                }
//
//                            }
//
//                        });
//                    }else{
//
//                        Crashlytics.log(Log.INFO, "STS", "Save had an exception ..." + e.getMessage());
//                        Crashlytics.logException(e);
//                        callback.onRegister(false, e,null);
//                    }
//
//                }
//
//            });
//
//        }
//        public void update(final String email, final String firstname, final String lastname, final String year, final String phone, final String organization, final String facebook, final String twitter, String linkedin, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//            GTCUser uprof = sharedDataManager.getCurrentProfile();
//            uprof.setUserEmail(email);
//            uprof.setFirstName(firstname);
//            uprof.setLastName(lastname);
//            if(!year.isEmpty())
//                uprof.setStudyYear(year);
//            uprof.setPhone(phone);
//            uprof.setOrganization(organization);
//            uprof.setTwitter(twitter);
//            uprof.setFacebook(facebook);
//            uprof.setLinkedin(linkedin);
//            if(image != null){
//                uprof.setImage(image);
//            }
//            if(thumb != null){
//                uprof.setThumbnail(thumb);
//            }
//            uprof.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    if(e == null){
//                        callback.onRegister(true,e, sharedDataManager.getCurrentUser());
//                    }else{
//                        Crashlytics.logException(e);
//                        callback.onRegister(false,e,sharedDataManager.getCurrentUser());
//                    }
//
//                }
//
//            });
//
//
//
//
//        }
//
//        @Override
//        public void downloadCompleted() {
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putFloat("AppVersion", currentAppVersion);
//            editor.putFloat("DataVersion", currentDataVersion);
//            latestDataVersion=currentDataVersion;
//            editor.commit();
//
//        }
//
//        public void tryUpdateVersion(float av, float dv){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            if(settings.getFloat("AppVersion", 0) <= 0 && settings.getFloat("DataVersion", 0) <= 0){
//                currentAppVersion = av;
//                currentDataVersion = dv;
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putFloat("AppVersion", av);
//                editor.putFloat("DataVersion", dv);
//                editor.commit();
//            }
//
//        }
//        public static void showAlert(Context context, String title, String message){
//            new AlertDialog.Builder(context)
//                    .setTitle(title)
//                    .setMessage(message)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//        public ParseUser getCurrentUser(){
//            return ParseUser.getCurrentUser();
//        }
//        public GTCUser getCurrentProfile(){
//            GTCUser user = null;
//            if(CheckIsConnectedToInternet(context)){
//                ((ParseObject)getCurrentUser().get("profile")).fetchInBackground(new GetCallback<GTCUser>(){
//
//                    @Override
//                    public void done(GTCUser uprof, com.parse.ParseException arg1) {
//                        if(uprof != null){
//                            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                            SharedPreferences.Editor editor = settings.edit();
//                            editor.putString("Year", uprof.getStudyYear());
//                            editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                            editor.commit();
//                        }
//                    }
//
//                });
//
//            }
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            user = GTCUser.create(GTCUser.class);
//            try {
//                user.load(new JSONObject(settings.getString("PROFILE","{}")));
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return user;
//        }
//        public void getAppUsers(final FindCallback<ParseUser> cb){
//            final List<ParseUser> _users = new ArrayList<ParseUser>();
//
//
//            FileInputStream in;
//            try {
//                in = context.openFileInput("appusers.json");
//                int size = in.available();
//                byte[] buffer = new byte[size];
//                in.read(buffer);
//                in.close();
//                String json = new String(buffer, "UTF-8");
//                JSONArray items = new JSONArray(json);
//                for(int i = 0; i < items.length(); i++){
//                    try{
//                        JSONObject object = items.getJSONObject(i);
//                        ParseUser obj = (ParseUser) ParseUser.class.getConstructors()[0].newInstance();
//                        GTCObject.load(obj, object);
//                        _users.add(obj);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            } catch (FileNotFoundException e1) {
//                e1.printStackTrace();
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            cb.done(_users, null);
//            if(CheckIsConnectedToInternet(context)){
//                ParseQuery<ParseUser> q = ParseQuery.getQuery(ParseUser.class);
//                q.findInBackground(new FindCallback<ParseUser>(){
//
//                    @Override
//                    public void done(final List<ParseUser> arg0, final com.parse.ParseException pe) {
//                        if(arg0 != null){
//                            new Thread(
//                                    new Runnable(){
//
//                                        @Override
//                                        public void run() {
//
//                                            FileOutputStream out = null;
//                                            try {
//                                                out = context.openFileOutput("appusers.json", Context.MODE_WORLD_READABLE);
//                                            } catch (FileNotFoundException e1) {
//                                                e1.printStackTrace();
//                                            }
//                                            final JSONArray arr = new JSONArray();
//
//                                            for(final ParseUser u: arg0){
//                                                arr.put(GTCObject.fields(u, context, true, 0));
//                                            }
//
//                                            try {
//                                                out.write(arr.toString().getBytes());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }finally{
//                                                try {
//                                                    out.close();
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                        }
//                                    }).start();
//                        }
//                    }
//
//                });
//
//            }
//
//        }
//        public static ParseUser findUserByObjectId(List<ParseUser> objects, String objId){
//            for(ParseUser u:objects){
//                if(u.getObjectId().equalsIgnoreCase(objId))
//                    return u;
//            }
//            return null;
//        }
//        public static GTCObject findByObjectId(ArrayList<? extends GTCObject> objects, String objId){
//            for(GTCObject u:objects){
//                if(u.getObjectId().equalsIgnoreCase(objId))
//                    return u;
//            }
//            return null;
//        }
//        public static ProgressDialog showProgress(Context context, String title, String message){
//            final ProgressDialog ringProgressDialog = ProgressDialog.show(context, title,	message, true);
//            return ringProgressDialog;
//        }
//        public static void showProgress(Context context, String title, String message, final IWorkCallback cb){
//            final ProgressDialog ringProgressDialog = ProgressDialog.show(context, title,	message, true);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        cb.doWork();
//                    } catch (Exception e) {
//
//                    }
//                    ringProgressDialog.dismiss();
//                }
//            }).start();
//        }
//        public boolean CheckIsConnectedToInternet(Context _context) {
//            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connectivity != null) {
//                NetworkInfo[] info = connectivity.getAllNetworkInfo();
//                if (info != null)
//                    for (int i = 0; i < info.length; i++)
//                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                            return true;
//                        }
//
//            }
//            return false;
//        }
//        public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
//            ExifInterface ei;
//            Bitmap bmp = null;
//            try {
//                ei = new ExifInterface(path);
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                        ExifInterface.ORIENTATION_NORMAL);
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        bmp= rotateImage(bitmap, 90);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        bmp = rotateImage(bitmap, 180);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        bmp = rotateImage(bitmap, 270);
//                        break;
//                    default:{
//                        bmp = bitmap;
//                        break;
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bmp;
//        }
//
//        public static Bitmap rotateImage(Bitmap source, float angle) {
//
//            Bitmap bitmap = null;
//            Matrix matrix = new Matrix();
//            matrix.postRotate(angle);
//            try {
//                bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                        matrix, true);
//                if(bitmap != source){
//                    source.recycle();
//                }
//            } catch (OutOfMemoryError err) {
//                err.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        public static Bitmap flipImage(Bitmap source) {
//
//            Bitmap bitmap = null;
//            Matrix matrix = new Matrix();
//            matrix.setScale(-1,1);
//            try {
//                bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                        matrix, true);
//                if(bitmap != source){
//                    source.recycle();
//                }
//            } catch (OutOfMemoryError err) {
//                err.printStackTrace();
//            }
//            return bitmap;
//        }
//
//
//        public final static int PIC_CROP = 2;
//        public final static int TAKE_PICTURE = 1;
//
//        private static final int REQ_CODE_PICK_IMAGE = 100;
//        public static void photoFunctionalityLaunch(final Activity context, final Uri imageUri){
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Choose an option");
//            builder.setItems(new CharSequence[]{"Choose a picture", "Take a photo"}, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    if(which  == 0){
//                        Intent i = new Intent(Intent.ACTION_PICK);
//                        i.setType("image/*");
//                        context.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
//	        	/*
//	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//	        	photoPickerIntent.setType("image/*");
//	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/
//                    }else{
//                        Intent intent = new Intent(context, GTCCameraActivity.class);//"android.media.action.IMAGE_CAPTURE");//
//                        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        context.startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//
//            });
//            builder.show();
//        }
//        public static void photoFunctionalityLaunch(final Fragment fragment, final Uri imageUri){
//            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
//            builder.setTitle("Choose");
//            builder.setItems(new CharSequence[]{"Choose a picutre", "Take a photo"}, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    if(which  == 0){
//                        Intent i = new Intent(Intent.ACTION_PICK);
//                        i.setType("image/*");
//                        fragment.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
//	        	/*
//	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//	        	photoPickerIntent.setType("image/*");
//	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/
//
//                    }else{
//                        Intent intent = new Intent(fragment.getActivity(), GTCCameraActivity.class); //MediaStore.ACTION_IMAGE_CAPTURE);//
//                        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        fragment.startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//
//            });
//            builder.show();
//        }
//        public static Bitmap handleBitmap(Bitmap bitmap, Uri imageUri){
//            Uri selectedImage = imageUri;
//            try {
//
//                bitmap = GTCDataManager.imageOreintationValidator(bitmap, selectedImage.toString());
//                FileOutputStream out = new FileOutputStream(imageUri.toString());
//                bitmap.compress(CompressFormat.JPEG, 9, out);
//
//
//            } catch (Exception e) {
//                Log.e("Camera", e.toString());
//            }
//            return bitmap;
//        }
//        public static Object handlePhotoFunctionalityResult(Context ctx, int requestCode, int resultCode, Intent data, Uri imageUri){
//            Bitmap bitmap = null;
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    if (resultCode == Activity.RESULT_OK) {
//                        Uri selectedImage = imageUri;
//                        ctx.getContentResolver().notifyChange(selectedImage, null);
//                        ContentResolver cr = ctx.getContentResolver();
//                        try {
//
//                            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                            selectedImage = imageUri;
//
//                        } catch (Exception e) {
//                            Log.e("Camera", e.toString());
//                        }
//                        photoCropFunctionality(ctx, selectedImage);
//                        return selectedImage;
//                    }break;
//                case REQ_CODE_PICK_IMAGE:
//                    if(resultCode == Activity.RESULT_OK){
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        Cursor cursor = ctx.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                        cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex); cursor.close();
//                        bitmap = BitmapFactory.decodeFile(picturePath);
//                        bitmap = GTCDataManager.imageOreintationValidator(bitmap, picturePath);
//                        try {
//                            URI uri = new URI(imageUri.toString());
//                            FileOutputStream out = new FileOutputStream(new File(uri));
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            selectedImage = imageUri;
//                            //bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        photoCropFunctionality(ctx, imageUri);
//                        return selectedImage;
//                    }break;
//                case Crop.REQUEST_CROP:
//                    Uri selectedImage = imageUri;
//                    ctx.getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = ctx.getContentResolver();
//                    try {
//
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//                        return bitmap;
//
//                    } catch (Exception e) {
//                        Log.e("Camera", e.toString());
//                    }
//            }
//            return bitmap;
//
//        }
//        public static Object handlePhotoFunctionalityResult(Fragment fragment, int requestCode, int resultCode, Intent data, Uri imageUri){
//            Bitmap bitmap = null;
//            Context context = fragment.getActivity();
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    if (resultCode == Activity.RESULT_OK) {
//                        Uri selectedImage = imageUri;
//                        context.getContentResolver().notifyChange(selectedImage, null);
//                        ContentResolver cr = context.getContentResolver();
//                        try {
//
//                            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                            selectedImage = imageUri;
//
//                        } catch (Exception e) {
//                            Log.e("Camera", e.toString());
//                        }
//                        photoCropFunctionality(fragment, selectedImage);
//                        return selectedImage;
//                    }break;
//                case REQ_CODE_PICK_IMAGE:
//                    if(resultCode == Activity.RESULT_OK){
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                        cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex); cursor.close();
//                        bitmap = BitmapFactory.decodeFile(picturePath);
//                        bitmap = GTCDataManager.imageOreintationValidator(bitmap, picturePath);
//                        try {
//                            URI uri = new URI(imageUri.toString());
//                            FileOutputStream out = new FileOutputStream(new File(uri));
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            selectedImage = imageUri;
//                            bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        photoCropFunctionality(fragment, imageUri);
//                        return selectedImage;
//                    }break;
//                case Crop.REQUEST_CROP:
//                    Uri selectedImage = Crop.getOutput(data);
//                    context.getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = context.getContentResolver();
//                    try {
//
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//
//                    } catch (Exception e) {
//                        Log.e("Camera", e.toString());
//                    }
//            }
//            return bitmap;
//        }
//        public static void photoCropFunctionality(Fragment fragment, Uri picUri){
//
//            new Crop(picUri).output(picUri).asSquare().start(fragment.getActivity(),fragment);
//        }
//        public static void photoCropFunctionality(Context context, Uri picUri){
//            new Crop(picUri).output(picUri).asSquare().start((Activity) context);
//        }
//
//        public static String capitalize(String str){
//            if(!str.isEmpty()){
//                String[] tokens = str.split(" ");
//                for(int i = 0; i < tokens.length; i++){
//                    String token = tokens[i];
//                    if(token.length() > 0){
//                        tokens[i] = token.substring(0,1).toUpperCase() + ((token.length() > 1)?token.substring(1):"");
//                    }
//                }
//                str = "";
//                for(int i = 0; i < tokens.length; i++){
//                    String token = tokens[i];
//                    if(i == 0){
//                        str += token;
//                    }else{
//                        str += " " +token;
//                    }
//                }
//            }
//            return str;
//
//        }
//    }
//
//    package com.populace.mayorday;
//
//    import java.io.File;
//    import java.io.FileInputStream;
//    import java.io.FileNotFoundException;
//    import java.io.FileOutputStream;
//    import java.io.FileWriter;
//    import java.io.IOException;
//    import java.io.InputStream;
//    import java.io.OutputStream;
//    import java.io.UnsupportedEncodingException;
//    import java.lang.reflect.Field;
//    import java.net.URI;
//    import java.nio.channels.FileChannel;
//    import java.text.DateFormat;
//    import java.text.ParseException;
//    import java.text.SimpleDateFormat;
//    import java.util.ArrayList;
//    import java.util.Collections;
//    import java.util.Comparator;
//    import java.util.Date;
//    import java.util.HashMap;
//    import java.util.List;
//    import java.util.Timer;
//    import java.util.TimerTask;
//
//    import org.json.JSONArray;
//    import org.json.JSONException;
//    import org.json.JSONObject;
//
//    import android.annotation.SuppressLint;
//    import android.app.Activity;
//    import android.app.AlertDialog;
//    import android.app.AlertDialog.Builder;
//    import android.app.Fragment;
//    import android.app.Notification;
//    import android.app.NotificationManager;
//    import android.app.PendingIntent;
//    import android.app.ProgressDialog;
//    import android.content.ContentResolver;
//    import android.content.ContentValues;
//    import android.content.Context;
//    import android.content.DialogInterface;
//    import android.content.Intent;
//    import android.content.SharedPreferences;
//    import android.content.pm.ActivityInfo;
//    import android.content.res.AssetManager;
//    import android.database.Cursor;
//    import android.database.sqlite.SQLiteDatabase;
//    import android.database.sqlite.SQLiteOpenHelper;
//    import android.graphics.Bitmap;
//    import android.graphics.Bitmap.CompressFormat;
//    import android.graphics.BitmapFactory;
//    import android.graphics.Color;
//    import android.graphics.Matrix;
//    import android.media.ExifInterface;
//    import android.net.ConnectivityManager;
//    import android.net.NetworkInfo;
//    import android.net.Uri;
//    import android.os.AsyncTask;
//    import android.os.Environment;
//    import android.os.Handler;
//    import android.provider.MediaStore;
//    import android.support.v4.app.NotificationCompat;
//    import android.util.Log;
//    import android.widget.Toast;
//
//    import com.crashlytics.android.Crashlytics;
//    import com.parse.FindCallback;
//    import com.parse.GetCallback;
//    import com.parse.LogInCallback;
//    import com.parse.Parse;
//    import com.parse.ParseAnalytics;
//    import com.parse.ParseFile;
//    import com.parse.ParseInstallation;
//    import com.parse.ParseObject;
//    import com.parse.ParseQuery;
//    import com.parse.ParseUser;
//    import com.parse.PushService;
//    import com.parse.SaveCallback;
//    import com.soundcloud.android.crop.Crop;
//
//    public class GTCDataManager implements DownloadCompleteCallback{
//        public static final String PREFS_NAME = "GTCPrefs";
//        public static final String TIME_FORMAT = "h:mm a";
//        public static final String DATE_FORMAT = "MMM dd, yyyy";
//        public static final String DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm a";
//        //	public static String[] CONF_DATES = {"Aug 4, 2014","Aug 5, 2014","Aug 6, 2014","Aug 7, 2014"};
//        public static String[] CONF_DATES = {"Jan 22, 2016","Jan 23, 2016","Jan 24, 2016","Jan 25, 2016"};
//        //	public static String[] CONF_DAYS = { "Mon","Tue","Wed","Thu"};
//        public static String[] CONF_DAYS = {"Fri","Sat","Sun","Mon"};
//
//        public static String[] EVENTS_DATES = {"Aug 3, 2014","Aug 4, 2014","Aug 5, 2014","Aug 6, 2014","Aug 7, 2014"};
//        public static String[] EVENTS_DAYS = {"Sun","Mon","Tue","Wed","Thu"};
//        public static String[] STUDY_YEARS = {"First Year", "Second Year", "Third Year", "Alumni Events"};
//
//        public static boolean showsDownloadActivity = true;
//        public static final int DEFAULT_COLOR = Color.argb(1, 79, 82, 74);
//        //Data  rawData;
//        Handler msgHandler = new Handler();
//        ArrayList<GTCAttendee>  attendee= new ArrayList<GTCAttendee>();
//        ArrayList<GTCSession>  sessions= new ArrayList<GTCSession>();
//        ArrayList<GTCSession1>  sessions1= new ArrayList<GTCSession1>();
//        ArrayList<GTCSession2>  sessions2= new ArrayList<GTCSession2>();
//        ArrayList<GTCSession3>  sessions3= new ArrayList<GTCSession3>();
//        ArrayList<GTCExhibitors>  exhibitors= new ArrayList<GTCExhibitors>();
//        ArrayList<GTCEvent>  events = new ArrayList<GTCEvent>();
//        ArrayList<GTCAuction>  auctions = new ArrayList<GTCAuction>();
//        ArrayList<GTCUser>  users = new ArrayList<GTCUser>();
//        ArrayList<GTCPhoto>  photos = new ArrayList<GTCPhoto>();
//        ArrayList<GTCSpeaker>  speakers= new ArrayList<GTCSpeaker>();
//        ArrayList<GTCSponsor>  sponsors = new ArrayList<GTCSponsor>();
//        ArrayList<GTCSession>  agenda= new ArrayList<GTCSession>();
//        ArrayList<GTCSession>  session_rating= new ArrayList<GTCSession>();
//
//        ArrayList<GTCMessage> messages = new ArrayList<GTCMessage>();
//        ArrayList<GTCLocation> locations= new ArrayList<GTCLocation>();
//        ArrayList<GTCSurvey>  surveys = new ArrayList<GTCSurvey>();
//        GTCInformation information;
//        Timer timer = new Timer();
//        DatabaseHandler dbHandler;
//        float currentAppVersion;
//        float currentDataVersion;
//        Context context;
//        static GTCDataManager sharedDataManager;
//        float latestDataVersion=1f;
//        private GTCDataManager(Context ctx) {
//            context = ctx;
//        }
//
//        public static GTCDataManager sharedDataManager(Context ctx) {
//
//            if (sharedDataManager == null) {
//                sharedDataManager = new GTCDataManager(ctx);
//                sharedDataManager.initialize();
//            }
//            return sharedDataManager;
//        }
//
//        public void initialize() {
////		if(context!=null)
////		{
////			SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
////			if (settings.contains("DataVersion")) {
////
////	          if(settings.getFloat("DataVersion",0.0f)<1f)
////	            {
////	                 	copyInitialData_new();
////	                 	SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
////	            		SharedPreferences.Editor editor = settings_new.edit();
////	            		editor.putFloat("AppVersion", 1);
////	            		editor.putFloat("DataVersion", 1f);
////	            		editor.putLong("DateUpdated", 0);
////	            		editor.commit();
////		        }
////			}
////		}
//
//
//            try{
//                sharedDataManager.copyInitialData();
//                sharedDataManager.initializeParse();
//                sharedDataManager.prepareDatabase();
//                sharedDataManager.tryUpdateVersion(1f, 1f);
////			sharedDataManager.tryUpdateVersion(1f, 2.0f);
//                if(CheckIsConnectedToInternet(context)){
//                    new Thread(
//                            new Runnable(){
//
//                                @Override
//                                public void run() {
//                                    sharedDataManager.downloadUsers();
//                                    sharedDataManager.downloadPhotos();
//                                }
//                            }).start();
//                }
//                sharedDataManager.loadData();
//                sharedDataManager.getAppUsers(new FindCallback<ParseUser>(){
//
//                    @Override
//                    public void done(List<ParseUser> arg0,
//                                     com.parse.ParseException arg1) {
//                    }
//
//                });
//                //timer.scheduleAtFixedRate(new TwitterTask(), 0, 3000);
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        private void copyInitialData_new() {
//            AssetManager assetManager = context.getAssets();
//            String[] files = null;
//            try {
//                files = assetManager.list("");
//            } catch (IOException e) {
//                Log.e("tag", "Failed to get asset file list.", e);
//            }
//
//            for(String filename : files) {
//                if(filename.endsWith("json")){
//                    File file = context.getFileStreamPath(filename);
////	    		if (!file.exists() || file.length() == 0) {
//                    InputStream in = null;
//                    OutputStream out = null;
//                    try {
//                        in = assetManager.open(filename);
//                        File outFile = new File(context.getFilesDir(), filename);
//                        out = new FileOutputStream(outFile);
//                        copyFile(in, out);
//                        in.close();
//                        in = null;
//                        out.flush();
//                        out.close();
//                        out = null;
//                    } catch(IOException e) {
//                        System.out.println("Andy json error "+e);
//                        Log.e("tag", "Failed to copy asset file: " + filename, e);
//                    }
////	    		}
//
//                }
//
//            }
//        }
//
//        private void copyInitialData() {
//            AssetManager assetManager = context.getAssets();
//            String[] files = null;
//            try {
//                files = assetManager.list("");
//            } catch (IOException e) {
//                Log.e("tag", "Failed to get asset file list.", e);
//            }
//
//            for(String filename : files) {
//                if(filename.endsWith("json")){
//                    File file = context.getFileStreamPath(filename);
//                    if (!file.exists() || file.length() == 0) {
//                        InputStream in = null;
//                        OutputStream out = null;
//                        try {
//                            in = assetManager.open(filename);
//                            File outFile = new File(context.getFilesDir(), filename);
//                            out = new FileOutputStream(outFile);
//                            copyFile(in, out);
//                            in.close();
//                            in = null;
//                            out.flush();
//                            out.close();
//                            out = null;
//                        } catch(IOException e) {
//                            Log.e("tag", "Failed to copy asset file: " + filename, e);
//                        }
//                    }
//
//                }
////	    	else if(filename.contains("png")&&filename.endsWith("png"))
////	    	{
//                else if(!filename.endsWith("json"))
//                {
//                    String fullPath = "/data/data/" + context.getPackageName() + "/files/";
//                    File dir = new File(fullPath);
//                    if (!dir.exists())
//                        dir.mkdir();
//                    File file = context.getFileStreamPath(filename);
//                    if (!file.exists() || file.length() == 0) {
//                        InputStream in = null;
//                        OutputStream out = null;
//                        try {
//                            in = assetManager.open(filename);
//                            File outFile = new File(fullPath, filename);
//
//                            System.out.println("Andy this path data "+outFile.getAbsolutePath());
//                            out = new FileOutputStream(outFile);
//                            copyFile(in, out);
//                            in.close();
//                            in = null;
//                            out.flush();
//                            out.close();
//                            out = null;
//                        } catch(IOException e) {
//                            System.out.println("Andy json error "+e);
//                            Log.e("tag", "Failed to copy asset file: " + filename, e);
//                        }
//                    }
//                }
//            }
//        }
//        private void copyFile(InputStream in, OutputStream out) throws IOException {
//            byte[] buffer = new byte[1024];
//            int read;
//            while((read = in.read(buffer)) != -1){
//                out.write(buffer, 0, read);
//            }
//        }
//        public class AsyncDownloadData extends AsyncTask<Void, CharSequence, Void>{
//            DownloadCompleteCallback callback;
//            ProgressDialog dialog;
//            Context context;
//            public AsyncDownloadData(DownloadCompleteCallback cb, Context ctx){
//                callback = cb;
//                context = ctx;
//            }
//            @Override
//            protected void onPreExecute() {
//                if(GTCDataManager.showsDownloadActivity){
//                    dialog = new ProgressDialog(context);
//                    dialog.setTitle("Downloading");
//                    dialog.setMessage("Checking Data Version");
//                    dialog.setIndeterminate(true);
//                    dialog.setCancelable(false);
//                    dialog.show();
//                }
//            }
//            @Override
//            protected Void doInBackground(Void... params) {
//                publishProgress("Downloading");
//                publishProgress("Downloading Exhibitors");
//                sharedDataManager.downloadExhibitors();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions1();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions2();
//                publishProgress("Downloading Sessions");
//                sharedDataManager.downloadSessions3();
//                publishProgress("Downloading Events");
//                sharedDataManager.downloadEvents();
//                publishProgress("Downloading Speakers");
//                sharedDataManager.downloadSpeakers();
//                publishProgress("Downloading Sponsors");
//                sharedDataManager.downloadSponsors();
//                //publishProgress("Downloading Surveys");
//                //sharedDataManager.downloadSurveys();
//                publishProgress("Downloading Locations");
//                sharedDataManager.downloadLocations();
//                publishProgress("Downloading Information");
//                sharedDataManager.downloadInformation();
//                publishProgress("Downloading Auctions");
//                sharedDataManager.downloadAuctions();
//                publishProgress("Downloading Attendees");
//                sharedDataManager.downloadAttendee();;
//                publishProgress("Loading Attendees");
//                sharedDataManager.loadAttendees();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions1();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions2();
//                publishProgress("Loading Sessions");
//                sharedDataManager.loadSessions3();
//                publishProgress("Loading Events");
//                sharedDataManager.loadEvents();
//                publishProgress("Loading Speakers");
//                sharedDataManager.loadSpeakers();
//                publishProgress("Loading Sponsors");
//                sharedDataManager.loadSponsors();
//                publishProgress("Loading Agenda");
//                sharedDataManager.loadAgenda();
//                //publishProgress("Loading Surveys");
//                //sharedDataManager.loadSurveys();
//                publishProgress("Loading Locations");
//                sharedDataManager.loadLocations();
//                publishProgress("Loading Information");
//                sharedDataManager.loadInformation();
//                publishProgress("Loading Auctions");
//                sharedDataManager.loadAuctions();
//                publishProgress("Loading Users");
//                sharedDataManager.loadUsers();
//                publishProgress("Loading Photos");
//                sharedDataManager.loadPhotos();
//                return null;
//            }
//            protected void onProgressUpdate(CharSequence... message) {
//                if(GTCDataManager.showsDownloadActivity)
//                    dialog.setMessage(message[0]);
//            }
//
//            protected void onPostExecute(Void unused) {
//                if(GTCDataManager.showsDownloadActivity){
//                    dialog.dismiss();
//                }
//                if(callback != null)
//                    callback.downloadCompleted();
//            }
//
//        }
//        public void initializeParse(){
//            try{
//
//                ParseObject.registerSubclass(GTCSession.class);
//                ParseObject.registerSubclass(GTCSession1.class);
//                ParseObject.registerSubclass(GTCSession2.class);
//                ParseObject.registerSubclass(GTCSession3.class);
//                ParseObject.registerSubclass(GTCSponsor.class);
//                ParseObject.registerSubclass(GTCSpeaker.class);
//                ParseObject.registerSubclass(GTCLocation.class);
//                ParseObject.registerSubclass(GTCSurvey.class);
//                ParseObject.registerSubclass(GTCAuction.class);
//                ParseObject.registerSubclass(GTCPhoto.class);
//                ParseObject.registerSubclass(GTCUser.class);
//                ParseObject.registerSubclass(GTCEvent.class);
//                ParseObject.registerSubclass(GTCInformation.class);
//                ParseObject.registerSubclass(GTCMessage.class);
//                ParseObject.registerSubclass(GTCExhibitors.class);
//                ParseObject.registerSubclass(GTCAttendee.class);
////		Parse.initialize(this.context, "7Pc9qchOwiJhld7w9IvCIzn8Wfi8yKa5dDOqwmIK", "7j67S8xIcSLRY1cidYXLWVztfjbpw9j8foNGXQ3J");
//                Parse.initialize(this.context, "4hsxEOXQsrEgP9o2DEWWi75yzf4DF36kpZn2ELM8", "accdSYTFUyhqBsP2m9rDzbMjI8HlcvIqU3AKW4jt");
//                ParseUser.enableAutomaticUser();
////		PushService.setDefaultPushCallback(this.context, GTCMessagesActivity.class);
//                PushService.setDefaultPushCallback(this.context, GTCActivity.class);
//                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                try{
//                    installation.put("appIdentifier", "com.populace.sdgc");
//                }catch(Exception e1){
//                    e1.printStackTrace();
//                }
//                try{
//                    installation.put("appName", "SD Tourism");
//                }catch(Exception e1){
//                    e1.printStackTrace();
//                }
//                installation.saveEventually();
//                ParseAnalytics.trackAppOpened(((Activity)this.context).getIntent());
//                //Intent intent = new Intent(this.context, GTCMessagesService.class);
//                //this.context.startService(intent);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        ProgressDialog checkVersionProgress;
//        public void checkDataVersion(final boolean withProgress, final Context ctx){
//            if(CheckIsConnectedToInternet(ctx)){
//
//                if(withProgress){
//                    checkVersionProgress = GTCDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");
//                }
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//                //query.setLimit(1);
//                query.findInBackground(new FindCallback<ParseObject>(){
//
//                    @Override
//                    public void done(List<ParseObject> result,
//                                     com.parse.ParseException arg1) {
//                        if(withProgress){
//                            checkVersionProgress.dismiss();
//                        }
//                        if(result != null){
//                            if(result.size() > 0){
//                                ParseObject  obj =  result.get(0);
//                                float appVersion = obj.getNumber("AppVersion").floatValue();
//                                float dataVersion = obj.getNumber("DataVersion").floatValue();
//
//                                try {
//                                    String email_feedback= obj.getString("dialog_text");
//                                    String eula_string= obj.getString("eula");
//                                    String privacy_policy=obj.getString("privacy_policy");
//                                    SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings_new.edit();
//                                    editor.putString("dialog_text", email_feedback);
//                                    editor.putString("eula_text", eula_string);
//                                    editor.putString("privacy_policy", privacy_policy);
//
//                                    editor.commit();
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//
//
//
//                                try {
//                                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                                    currentAppVersion =settings.getFloat("AppVersion",1f);
//                                    currentDataVersion = settings.getFloat("DataVersion",1f);
//
//
//                                    if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
//                                        askDownload(dataVersion, ctx);
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//                                    }else{
//                                        sharedDataManager.loadData();
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//
//                                    }
//                                }
//                                catch (Exception exception) {
//                                    askDownload(dataVersion, ctx);
//                                    currentAppVersion =appVersion;
//                                    currentDataVersion = dataVersion;
//                                    System.out.println("andy checking data version "+exception);
//                                }
//                                finally {
//
//                                }
//
//                            }
//                        }else{
//                            sharedDataManager.loadData();
//
//                        }
//                    }
//                });
//
//
//
//
////				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
////				    query2.setLimit(1);
////						query2.findInBackground(new FindCallback<ParseObject>(){
////
////							@Override
////							public void done(List<ParseObject> result,
////									com.parse.ParseException arg1) {
////								if(result != null){
////									ParseObject  obj =  result.get(0);
////							        String defaultstatement = obj.getString("DefaultStatement");
////							        String newstatement = obj.getString("NewStatement");
////							        boolean showstatement = obj.getBoolean("showStatement");
////
////							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
////							        try {
////							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
////							        	SharedPreferences.Editor editor = settings.edit();
////							            editor.putString("DefaultStatement", defaultstatement);
////							            editor.putString("NewStatement", newstatement);
////							            editor.putBoolean("showStatement", showstatement);
////							            editor.commit();
////							        }
////							        catch (Exception exception) {
////
////							        }
////
////								}else{
////
////								}
////							}
////						});
//
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//            }else{
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                if(withProgress){
//                    GTCDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
//                }
//            }
//        }
//        ProgressDialog checkVersionProgress1;
//        public void checkDataVersion_refresh(final boolean withProgress, final Context ctx){
//            if(CheckIsConnectedToInternet(ctx)){
//
//                if(withProgress){
//                    checkVersionProgress1 = GTCDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");
//                }
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//                //query.setLimit(1);
//                query.findInBackground(new FindCallback<ParseObject>(){
//
//                    @Override
//                    public void done(List<ParseObject> result,
//                                     com.parse.ParseException arg1) {
//                        if(withProgress){
//                            checkVersionProgress1.dismiss();
//                        }
//                        if(result != null){
//                            if(result.size() > 0){
//                                ParseObject  obj =  result.get(0);
//                                float appVersion = obj.getNumber("AppVersion").floatValue();
//                                float dataVersion = obj.getNumber("DataVersion").floatValue();
//
//                                try {
//                                    String email_feedback= obj.getString("dialog_text");
//                                    String eula_string= obj.getString("eula");
//                                    String privacy_policy=obj.getString("privacy_policy");
//                                    SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings_new.edit();
//                                    editor.putString("dialog_text", email_feedback);
//                                    editor.putString("eula_text", eula_string);
//                                    editor.putString("privacy_policy", privacy_policy);
//
//                                    editor.commit();
//                                } catch (Exception e) {
//                                    // TODO: handle exception
//                                }
//
//
//                                try {
//                                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                                    currentAppVersion =settings.getFloat("AppVersion",1f);
//                                    currentDataVersion = settings.getFloat("DataVersion",1f);
//                                    System.out.println("andy checking data version "+currentAppVersion +" "+currentDataVersion);
//
//                                    if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
//                                        askDownload(dataVersion, ctx);
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//                                    }else{
//
//                                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(ctx);
//                                        alertDialog2.setTitle("App is up to date");
//                                        alertDialog2.setMessage("There is no new app data available at this time");
//                                        alertDialog2.setPositiveButton("OK",
//                                                new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                    }
//                                                });
//
//                                        alertDialog2.show();
//                                        sharedDataManager.loadData();
//                                        currentAppVersion =appVersion;
//                                        currentDataVersion = dataVersion;
//
//
//                                    }
//                                }
//                                catch (Exception exception) {
//                                    askDownload(dataVersion, ctx);
//                                    currentAppVersion =appVersion;
//                                    currentDataVersion = dataVersion;
//
//                                }
//                                finally {
//
//                                }
//
//                            }
//                        }else{
//                            sharedDataManager.loadData();
//
//                        }
//                    }
//                });
//
//
//
//
////				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
////				    query2.setLimit(1);
////						query2.findInBackground(new FindCallback<ParseObject>(){
////
////							@Override
////							public void done(List<ParseObject> result,
////									com.parse.ParseException arg1) {
////								if(result != null){
////									ParseObject  obj =  result.get(0);
////							        String defaultstatement = obj.getString("DefaultStatement");
////							        String newstatement = obj.getString("NewStatement");
////							        boolean showstatement = obj.getBoolean("showStatement");
////
////							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
////							        try {
////							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
////							        	SharedPreferences.Editor editor = settings.edit();
////							            editor.putString("DefaultStatement", defaultstatement);
////							            editor.putString("NewStatement", newstatement);
////							            editor.putBoolean("showStatement", showstatement);
////							            editor.commit();
////							        }
////							        catch (Exception exception) {
////
////							        }
////
////								}else{
////
////								}
////							}
////						});
//
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//
//            }else{
//                try {
//                    SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
//                    latestDataVersion =settings.getFloat("LatestDataVersion",1f);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//                if(withProgress){
//                    GTCDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
//                }
//            }
//        }
//        public void addToAgenda(GTCSession _session){
//            if(!GTCDataManager.sharedDataManager.inAgenda(_session)){
//                int id = (int)dbHandler.addSession(_session);
//                _session.id = id;
//                agenda.add(_session);
//            }
//
//        }
//
//        public void addToRatings(GTCSession _session){
//            if(!GTCDataManager.sharedDataManager.inRatings(_session)){
//                int id = (int)dbHandler.addSessionRatings(_session);
//                _session.id = id;
//                session_rating.add(_session);
//            }
//
//        }
//        public void removeFromAgenda(GTCSession _session){
//
//            dbHandler.deleteSession(_session);
//
//            for(int k = 0; k < agenda.size(); k++){
//                if(GTCDataManager.sharedDataManager.agenda.get(k).getObjectId() == _session.getObjectId()){
//                    agenda.remove(GTCDataManager.sharedDataManager.agenda.get(k));
//                    break;
//                }
//            }
//
//        }
//
//        public GTCScan addScanWithText(String text){
//            GTCScan scan = new GTCScan(0,text,new Date());
//            dbHandler.addScan(scan);
//            return scan;
//        }
//        public ArrayList<GTCScan> scans(){
//            return dbHandler.getAllScans();
//        }
//        public void deleteScan(GTCScan scan){
//            dbHandler.deleteScan(scan);
//        }
//        public ArrayList<String> results(){
//            ArrayList<String> results = new ArrayList<String>();
//            ArrayList<GTCScan> scans = sharedDataManager.scans();
//            for(GTCScan  scan : scans){
//                //[results addObject:[UniversalResultParser parsedResultForString:scan.text]];
//                //TODO
//            }
//            return results;
//        }
//        public void prepareDatabase(){
//            dbHandler = new DatabaseHandler(context);
//
////		if(checkDBAltered())
////		{
////
////		}
////		else
////		{
////			dbHandler.alterAgendaTable();
////			SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
////    		SharedPreferences.Editor editor = settings_new.edit();
////    		editor.putFloat("DBAltered", 1);
////    		editor.commit();
////		}
//
//        }
//
//        public boolean checkDBAltered()
//        {
//
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            if (settings.contains("DBAltered"))
//            {
//                return true;
//            }
//
//            return false;
//        }
//        public void askDownload(float dataVersion, final Context ctx){
//
//            new AlertDialog.Builder(ctx)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle("Download Update")
//                    .setMessage("Version "+dataVersion+" is available.\nDownload now?")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            downloadData(ctx);
//
//                        }
//
//                    })
//                    .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            final Builder builder = new Builder(ctx);
//                            builder.setCancelable(true);
//                            builder.setTitle("Download Later").setMessage("You can refresh the app content at any time from settings menu").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // TODO Auto-generated method stub
//                                    dialog.cancel();
//                                }
//                            });
//
//
//                            ((Activity) ctx).runOnUiThread(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    builder.show();
//
//                                }
//                            });
//
//                        }
//                    })
//                    .show();
//        }
//        public void downloadData(Context ctx){
//
//            new AsyncDownloadData(sharedDataManager, ctx).execute();
//        }
//
//
//        public void downloadAttendee() {
//            attendee = (ArrayList<GTCAttendee>) download("attendees.json",GTCAttendee.class,false);
//        }
//        public void downloadExhibitors() {
//            exhibitors = (ArrayList<GTCExhibitors>) download("exhibitors.json",GTCExhibitors.class,false);
//        }
//        public void downloadSessions() {
//            sessions = (ArrayList<GTCSession>) download("sessions.json",GTCSession.class,false);
//        }
//        public void downloadSessions1() {
//            sessions1 = (ArrayList<GTCSession1>) download("sessions1.json",GTCSession1.class,false);
//        }
//        public void downloadSessions2() {
//            sessions2 = (ArrayList<GTCSession2>) download("sessions2.json",GTCSession2.class,false);
//        }
//        public void downloadSessions3() {
//            sessions3 = (ArrayList<GTCSession3>) download("sessions3.json",GTCSession3.class,false);
//        }
//        public void downloadEvents() {
//            events = (ArrayList<GTCEvent>) download("events.json",GTCEvent.class,false);
//        }
//        public void downloadAuctions() {
//            auctions = (ArrayList<GTCAuction>) download("auctions.json",GTCAuction.class,false);
//        }
//        public void downloadUsers() {
//            users = (ArrayList<GTCUser>) download("users.json",GTCUser.class,true);
//        }
//        public void downloadPhotos() {
//            photos = (ArrayList<GTCPhoto>) download("photos.json",GTCPhoto.class,true);
//        }
//        public void downloadSurveys() {
//            surveys = (ArrayList<GTCSurvey>) download("surveys.json",GTCSurvey.class,false);
//        }
//        public void downloadLocations() {
//            locations = (ArrayList<GTCLocation>) download("locations.json",GTCLocation.class,false);
//        }
//        public void downloadSpeakers() {
//            speakers = (ArrayList<GTCSpeaker>) download("speakers.json",GTCSpeaker.class,false);
//        }
//        public void downloadSponsors() {
//            sponsors = (ArrayList<GTCSponsor>) download("sponsors.json",GTCSponsor.class,false);
//        }
//        public void downloadInformation() {
//            download("information.json",GTCInformation.class,false);
//        }
//        public List<?> download(String filename, Class<? extends GTCObject> c, boolean skipFiles){
//            ParseQuery<?> query = ParseQuery.getQuery(c);
//            List<?> _objects = null;
//            try {
//                query.setLimit(1000);
//                _objects = query.find();
//                FileOutputStream out = null;
//                try {
//                    out = context.openFileOutput(filename, Context.MODE_WORLD_READABLE);
//                } catch (FileNotFoundException e1) {
//                    e1.printStackTrace();
//                }
//                JSONArray array = new JSONArray();
//                for(int i = 0; i < _objects.size(); i++){
//                    GTCObject object = (GTCObject)_objects.get(i);
//                    array.put(object.fields(context,skipFiles,0));
//                }
//                try {
//                    out.write(array.toString().getBytes());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally{
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (com.parse.ParseException e2) {
//                e2.printStackTrace();
//            }
//            return _objects;
//
//        }
//        public void loadData(){
//            sharedDataManager.loadExhibitors();
//            sharedDataManager.loadSessions();
//            sharedDataManager.loadSessions1();
//            sharedDataManager.loadSessions2();
//            sharedDataManager.loadSessions3();
//            sharedDataManager.loadSpeakers();
//            sharedDataManager.loadSponsors();
//            sharedDataManager.loadAuctions();
//            sharedDataManager.loadUsers();
//            sharedDataManager.loadEvents();
//            sharedDataManager.loadPhotos();
//            sharedDataManager.loadAgenda();
//            sharedDataManager.loadSurveys();
//            sharedDataManager.loadLocations();
//            sharedDataManager.loadInformation();
//            sharedDataManager.loadAttendees();
////        SaveData save = new SaveData();
////        save.execute();
//
//        }
//
//        private class SaveData extends AsyncTask<String, Void, Boolean>{
//
//            @Override
//            protected Boolean doInBackground(String... params) {
//                // your background code here. Don't touch any UI components
//                sharedDataManager.loadAttendees();
//                return true;
//            }
//
//            protected void onPostExecute(Boolean result) {
//                //This is run on the UI thread so you can do as you wish here
//
//            }}
//        public List<?> load(String filename, Class c){
//            ArrayList<GTCObject> list = new ArrayList<GTCObject>();
//            try {
//
//                FileInputStream in = context.openFileInput(filename);
//                int size = in.available();
//                byte[] buffer = new byte[size];
//                in.read(buffer);
//                in.close();
//                String json = new String(buffer, "UTF-8");
//                JSONArray items = new JSONArray(json);
//                for(int i = 0; i < items.length(); i++){
//
//
//// 			   try
//// 			    {
//// 			        File root = new File(Environment.getExternalStorageDirectory(), "Notes_MayorApp");
//// 			        if (!root.exists()) {
//// 			            root.mkdirs();
//// 			        }
//// 			        File gpxfile = new File(root, filename);
//// 			        FileWriter writer = new FileWriter(gpxfile);
//// 			        writer.append(json);
//// 			        writer.flush();
//// 			        writer.close();
//// 			        System.out.println("Andy sponser data done"+Environment.getExternalStorageDirectory());
////
//// 			    }
//// 			    catch(IOException e)
//// 			    {
//// 			         e.printStackTrace();
////
//// 			    }
//                    try{
//
//
//                        JSONObject object = items.getJSONObject(i);
//                        GTCObject obj = (GTCObject) c.getConstructors()[0].newInstance();
//                        obj.load(object);
//                        list.add(obj);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//                Collections.sort(list, new Comparator<GTCObject>(){
//
//                    @Override
//                    public int compare(GTCObject lhs, GTCObject rhs) {
//                        if(lhs.getString("name") != null){
//                            return lhs.getString("name").compareToIgnoreCase(rhs.getString("name"));
//                        }else if(lhs.getString("title") != null){
//                            return lhs.getString("title").compareToIgnoreCase(rhs.getString("title"));
//                        }else{
//                            return 0;
//                        }
//                    }
//
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return list;
//        }
//        @SuppressWarnings("unchecked")
//        public void loadExhibitors() {
//            try{
//                exhibitors = (ArrayList<GTCExhibitors>) load("exhibitors.json",GTCExhibitors.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions() {
//            try{
//                sessions = (ArrayList<GTCSession>) load("sessions.json",GTCSession.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadAttendees() {
//            try{
//                attendee = (ArrayList<GTCAttendee>) load("attendees.json",GTCAttendee.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadSessions1() {
//            try{
//                sessions1 = (ArrayList<GTCSession1>) load("sessions1.json",GTCSession1.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions2() {
//            try{
//                sessions2 = (ArrayList<GTCSession2>) load("sessions2.json",GTCSession2.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSessions3() {
//            try{
//                sessions3 = (ArrayList<GTCSession3>) load("sessions3.json",GTCSession3.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadEvents() {
//            try{
//                events = (ArrayList<GTCEvent>) load("events.json",GTCEvent.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadAuctions() {
//            try{
//                auctions = (ArrayList<GTCAuction>) load("auctions.json",GTCAuction.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadUsers() {
//            try{
//                users = (ArrayList<GTCUser>) load("users.json",GTCUser.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadPhotos() {
//            try{
//                photos = (ArrayList<GTCPhoto>) load("photos.json",GTCPhoto.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        @SuppressWarnings("unchecked")
//        public void loadSurveys() {
//            try{
//                surveys = (ArrayList<GTCSurvey>) load("surveys.json",GTCSurvey.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public void loadLocations() {
//            try{
//                locations = (ArrayList<GTCLocation>) load("locations.json",GTCLocation.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        public void loadSpeakers(){
//            try{
//                speakers = (ArrayList<GTCSpeaker>) load("speakers.json",GTCSpeaker.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//        }
//        public void loadSponsors(){
//            try{
//                sponsors = (ArrayList<GTCSponsor>) load("sponsors.json",GTCSponsor.class);
//
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//
//        }
//        @SuppressWarnings("unchecked")
//        public void loadInformation(){
//            try{
//
//                ArrayList<GTCInformation> infos = (ArrayList<GTCInformation>) load("information.json",GTCInformation.class);
//                information = infos.get(0);
//                String[] dates =(String[]) information.getDates().toArray(new String[information.getDates().size()]);
//                String[] days =(String[]) information.getDays().toArray(new String[information.getDays().size()]);
//                GTCDataManager.CONF_DATES = dates;
//                System.out.println("Andy checking sesssion issues db "+GTCDataManager.CONF_DATES[0]);
//
//                GTCDataManager.CONF_DAYS = days;
//            }catch(Exception exc){
//                exc.printStackTrace();
//            }
//
//        }
//        public void loadAgenda(){
//            agenda =  dbHandler.getAllSessions();
//            session_rating=  dbHandler.getAllSessionsRatings();
//        }
//        public void loadMessages(){
//            try{
//                ParseQuery<GTCMessage> query = ParseQuery.getQuery(GTCMessage.class);
//                query.findInBackground(new FindCallback<GTCMessage>() {
//
//                    @Override
//                    public void done(List<GTCMessage> objects, com.parse.ParseException e) {
//                        if(objects != null){
//                            int newMessagesCount = 0;
//                            GTCDataManager.sharedDataManager.messages = (ArrayList<GTCMessage>) objects;
//                            if(GTCDataManager.sharedDataManager.messages == null){
//                                GTCDataManager.sharedDataManager.messages = new ArrayList<GTCMessage>();
//                            }
//                            SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
//                            String messageIds  = pref.getString("TwitterMessageIds", "");
//                            String[] ids = messageIds.split(",");
//                            for(int i = 0; i < objects.size(); i++){
//                                GTCMessage message = objects.get(i);
//                                boolean exists = false;
//                                for(int j  = 0; j < ids.length; j++){
//                                    String id = ids[j];
//                                    if(id.compareToIgnoreCase(message.getObjectId()) == 0){
//                                        exists = true;
//                                        break;
//                                    }else{
//
//                                    }
//                                }
//                                if(!exists){
//                                    messageIds += "," + message.getObjectId();
//                                    newMessagesCount++;
//                                }
//                            }
//
//                            Collections.sort(messages, new MessagesComparable());
//                            Intent intent = new Intent();
//                            intent.setAction(context.getPackageName() + ".MessageCount");
//                            intent.putExtra("MessageCount", newMessagesCount);
//                            context.sendBroadcast(intent);
//                            if(newMessagesCount > 0 ){
//                                Intent nintent = new Intent(context, GTCActivity.class);
//                                PendingIntent pintent = PendingIntent.getActivity(context, 0, nintent, 0);
//                                NotificationCompat.Builder mBuilder =
//                                        new NotificationCompat.Builder(context)
//                                                .setSmallIcon(R.drawable.ic_launcher)
//                                                .setContentTitle("Update")
//                                                .setContentIntent(pintent)
//                                                .setContentText("New messages are available.");
//                                mBuilder.setAutoCancel(true);
//                                Notification notif = mBuilder.build();
//                                notif.flags |= Notification.FLAG_AUTO_CANCEL;
//                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                                notificationManager.notify(007, notif);
//                            }
//                        }
//                    }
//                });
//
//
//
//
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//        }
//        public class MessagesComparable implements Comparator<GTCMessage>{
//
//            @SuppressLint("SimpleDateFormat")
//            @Override
//            public int compare(GTCMessage o1, GTCMessage o2) {
//                DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss Z y");
//                Date date1 = o1.getCreatedAt();
//                Date date2 = o2.getCreatedAt();
//                return date2.compareTo(date1);
//            }
//        }
//
//        public boolean inAgenda(GTCSession session){
//            for(GTCSession _session: agenda){
//                if(_session.getObjectId().compareToIgnoreCase(session.getObjectId()) == 0 &&
//                        _session.getDate().compareToIgnoreCase(session.getDate()) == 0 &&
//                        _session.getTime().compareToIgnoreCase(session.getTime()) == 0 )
//                    return true;
//            }
//            return false;
//        }
//        public boolean inRatings(GTCSession session){
//            for(GTCSession _session: session_rating){
//                if(_session.getObjectId().compareToIgnoreCase(session.getObjectId()) == 0 &&
//                        _session.getDate().compareToIgnoreCase(session.getDate()) == 0 &&
//                        _session.getTime().compareToIgnoreCase(session.getTime()) == 0 )
//                    return true;
//            }
//            return false;
//        }
//
//        public GTCSpeaker speakerByName(String name){
//            for(GTCSpeaker speaker:speakers){
//                if(speaker.getName().compareToIgnoreCase(name) == 0)
//                    return speaker;
//            }
//            return null;
//        }
//        public ArrayList<GTCSession> sessionBySpeaker(String name){
//            ArrayList<GTCSession> speakerSessions = new ArrayList<GTCSession>();
//            for(GTCSession session:sharedDataManager.sessions){
//                if(session.getSpeakers().contains(name)){
//                    speakerSessions.add(session);
//                }
//            }
//            return speakerSessions;
//        }
//        public ArrayList<String> sortedByDates(ArrayList<String> _dates){
//            Collections.sort(_dates, new StringDateComparator());
//            return null;
//        }
//        public  HashMap<String,ArrayList<GTCSurvey>> groupedExhibitorsByField(ArrayList<GTCSurvey> _exhibitors, String _field){
//            try{
//                HashMap<String,ArrayList<GTCSurvey>> groupedExhibitors = new HashMap<String,ArrayList<GTCSurvey>>();
//                for(GTCSurvey session : _exhibitors){
//                    String fieldValue = session.getKeyVals().get(_field);
//                    if(groupedExhibitors.containsKey(fieldValue)){
//                        ArrayList<GTCSurvey>  __exhibitors = groupedExhibitors.get(fieldValue);
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }else{
//                        ArrayList<GTCSurvey> __exhibitors = new ArrayList<GTCSurvey>();
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }
//                }
//                for(String key : groupedExhibitors.keySet()){
//                    ArrayList<GTCSurvey> __exhibitors = groupedExhibitors.get(key);
//                    Collections.sort(__exhibitors, new GTCSurveysComparator());
//
//
//                }
//                return groupedExhibitors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSurvey>>();
//            }
//
//        }
//
//        public HashMap<String,ArrayList<GTCUser>> groupedUsersByInitial(ArrayList<GTCUser> _users){
//            try{
//                HashMap<String,ArrayList<GTCUser>> groupedUsers = new HashMap<String,ArrayList<GTCUser>>();
//                for(GTCUser user : _users){
//                    String fieldValue = user.getLastName().charAt(0) + "";
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCUser>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCUser> __users = new ArrayList<GTCUser>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCUser> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCUsersComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCUser>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCAttendee>> groupedAttendeeByInitial(ArrayList<GTCAttendee> _users){
//            try{
//                HashMap<String,ArrayList<GTCAttendee>> groupedUsers = new HashMap<String,ArrayList<GTCAttendee>>();
//                for(GTCAttendee user : _users){
//
//                    String fieldValue ="";
//                    if(user.getLastName().length()>0)
//                    {
//                        fieldValue = user.getLastName().trim().charAt(0) + "";
//
//                    }
//                    else if(user.getFirstName().length()>0)
//                    {
//                        fieldValue= user.getFirstName().trim().charAt(0) + "";
//                    }
//                    else
//                    {
//                        continue;
//                    }
//
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCAttendee>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCAttendee> __users = new ArrayList<GTCAttendee>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCAttendee> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCAttendeeComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                System.out.println("Ady checking size of list here "+e);
//
//                return new HashMap<String,ArrayList<GTCAttendee>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCSpeaker>> groupedSpeakersByInitial(ArrayList<GTCSpeaker> _users){
//            try{
//                HashMap<String,ArrayList<GTCSpeaker>> groupedUsers = new HashMap<String,ArrayList<GTCSpeaker>>();
//                for(GTCSpeaker user : _users){
//                    String nameTokens[] = user.getName().split(" ");
//                    String fieldValue = (nameTokens.length > 1)?nameTokens[1].charAt(0) + "":nameTokens[0].charAt(0) + "";
//                    if(groupedUsers.containsKey(fieldValue)){
//                        ArrayList<GTCSpeaker>  __users = groupedUsers.get(fieldValue);
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }else{
//                        ArrayList<GTCSpeaker> __users = new ArrayList<GTCSpeaker>();
//                        __users.add(user);
//                        groupedUsers.put(fieldValue, __users);
//                    }
//                }
//                for(String key : groupedUsers.keySet()){
//                    ArrayList<GTCSpeaker> __users = groupedUsers.get(key);
//                    Collections.sort(__users, new GTCSpeakersComparator());
//
//
//                }
//                return groupedUsers;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSpeaker>>();
//            }
//
//        }
//        public HashMap<String,ArrayList<GTCSurvey>> groupedExhibitorsByInitial(ArrayList<GTCSurvey> _exhibitors){
//            try{
//                HashMap<String,ArrayList<GTCSurvey>> groupedExhibitors = new HashMap<String,ArrayList<GTCSurvey>>();
//                for(GTCSurvey session : _exhibitors){
//                    String fieldValue = session.getKeyVals().get("Company").charAt(0) + "";
//                    if(groupedExhibitors.containsKey(fieldValue)){
//                        ArrayList<GTCSurvey>  __exhibitors = groupedExhibitors.get(fieldValue);
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }else{
//                        ArrayList<GTCSurvey> __exhibitors = new ArrayList<GTCSurvey>();
//                        __exhibitors.add(session);
//                        groupedExhibitors.put(fieldValue, __exhibitors);
//                    }
//                }
//                for(String key : groupedExhibitors.keySet()){
//                    ArrayList<GTCSurvey> __exhibitors = groupedExhibitors.get(key);
//                    Collections.sort(__exhibitors, new GTCSurveysComparator());
//
//
//                }
//                return groupedExhibitors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSurvey>>();
//            }
//
//        }
//        public  HashMap<String,ArrayList<GTCSponsor>> groupedSponsorsByField(ArrayList<GTCSponsor> _sponsors, String _field){
//            try{
//                HashMap<String,ArrayList<GTCSponsor>> groupedSponsors = new HashMap<String,ArrayList<GTCSponsor>>();
//                for(GTCSponsor session : _sponsors){
//                    String fieldValue = session.getKeyVals().get(_field);
//                    if(groupedSponsors.containsKey(fieldValue)){
//                        ArrayList<GTCSponsor>  __sponsors = groupedSponsors.get(fieldValue);
//                        __sponsors.add(session);
//                        groupedSponsors.put(fieldValue, __sponsors);
//                    }else{
//                        ArrayList<GTCSponsor> __sponsors = new ArrayList<GTCSponsor>();
//                        __sponsors.add(session);
//                        groupedSponsors.put(fieldValue, __sponsors);
//                    }
//                }
//                for(String key : groupedSponsors.keySet()){
//                    ArrayList<GTCSponsor> __sponsors = groupedSponsors.get(key);
//                    Collections.sort(__sponsors, new Comparator<GTCSponsor>(){
//
//                        @Override
//                        public int compare(GTCSponsor lhs, GTCSponsor rhs) {
//
//                            return lhs.getOrder().intValue() - rhs.getOrder().intValue();
//                        }
//
//                    });
//
//
//                }
//                return groupedSponsors;
//            }catch(Exception e){
//                return new HashMap<String,ArrayList<GTCSponsor>>();
//            }
//
//        }
//
//
//
//        public  HashMap<String,ArrayList<GTCSession>> groupedSessionsByField(ArrayList<GTCSession> _sessions, String _field){
//            String andy=null;
//            System.out.println("Andy checking excetion in time db"+_field);
//            try{
//                HashMap<String,ArrayList<GTCSession>> groupedSessions = new HashMap<String,ArrayList<GTCSession>>();
//                for(GTCSession session : _sessions){
//
//                    String fieldValue = session.getKeyVals().get(_field);
////				andy=session.getTitle();
//                    System.out.println("Andy checking excetion in time value "+fieldValue);
//                    if(groupedSessions.containsKey(fieldValue)){
//                        ArrayList<GTCSession>  __sessions = groupedSessions.get(fieldValue);
//                        __sessions.add(session);
//                        groupedSessions.put(fieldValue, __sessions);
//                    }else{
//                        ArrayList<GTCSession> __sessions = new ArrayList<GTCSession>();
//                        __sessions.add(session);
//                        groupedSessions.put(fieldValue, __sessions);
//                    }
//                }
//                for(String key : groupedSessions.keySet()){
//                    ArrayList<GTCSession> __sessions = groupedSessions.get(key);
//                    Collections.sort(__sessions, new GTCSessionComparator());
//
//
//                }
//                return groupedSessions;
//            }catch(Exception e){
//                System.out.println("Andy checking excetion in time "+e);
//
//                return new HashMap<String,ArrayList<GTCSession>>();
//            }
//
//        }
//
//
//
//        public class TwitterTask extends TimerTask{
//
//            @Override
//            public void run() {
//
//                GTCDataManager.sharedDataManager.loadMessages();
//            }
//
//        }
//
////CREATE TABLE SCAN (id INTEGER PRIMARY KEY NOT NULL, text TEXT NOT NULL, stamp REAL DEFAULT 0.0)
////CREATE TABLE AGENDA (id INTEGER PRIMARY KEY NOT NULL, name TEXT, date TEXT, time TEXT, location TEXT, type TEXT, description TEXT, speaker1 TEXT, speaker2 TEXT, speaker3 TEXT, speaker4 TEXT, speaker5 TEXT)
//
//
//        public class DatabaseHandler extends SQLiteOpenHelper {
//
//            // All Static variables
//            // Database Version
//            private static final int DATABASE_VERSION = 1;
//            //	 private static final int DATABASE_VERSION = 2;
//            // Database Name
//            private static final String DATABASE_NAME = "SCANS_2015";
//
//            // Scan table name
//            private static final String TABLE_SCAN = "SCAN";
//
//            //Agenda table name
//            private static final String TABLE_AGENDA = "AGENDA";
//            //Scan Table Columns names
//            private static final String SCAN_KEY_ID = "id";
//            private static final String SCAN_KEY_TEXT = "text";
//            private static final String SCAN_KEY_STAMP = "stamp";
//
//            //Agenda table Column names
////    private static final String AGENDA_KEY_ID = "id";
//            private static final String AGENDA_OBJECT_ID = "object_id";
//            private static final String AGENDA_KEY_NAME = "name";
//            private static final String AGENDA_KEY_DATE = "date";
//            private static final String AGENDA_KEY_TIME = "time";
//            private static final String AGENDA_KEY_TYPE = "type";
//            private static final String AGENDA_KEY_LOCATION = "location";
//            private static final String AGENDA_KEY_DESCRIPTION = "description";
//            private static final String AGENDA_KEY_SPEAKER = "speaker";
//            private static final String AGENDA_KEY_TOPIC = "topic";
//            private static final String AGENDA_KEY_ORDER_ID  = "order_id";
//
//            //Agenda table Column names
//            private static final String TABLE_RSTING = "RATING";
//            private static final String RSTING_OBJECT_ID = "object_id";
//
//            //    private static final String RSTING_KEY_ID = "id";
//            private static final String RSTING_KEY_NAME = "name";
//            private static final String RSTING_KEY_DATE = "date";
//            private static final String RSTING_KEY_TIME = "time";
//            private static final String RSTING_KEY_TYPE = "type";
//            private static final String RSTING_KEY_LOCATION = "location";
//            private static final String RSTING_KEY_DESCRIPTION = "description";
//            private static final String RSTING_KEY_SPEAKER = "speaker";
//
//
//            public DatabaseHandler(Context context) {
//                super(context, DATABASE_NAME, null, DATABASE_VERSION);
//            }
//
//            // Creating Tables
//            @Override
//            public void onCreate(SQLiteDatabase db) {
//                String CREATE_SCAN_TABLE = "CREATE TABLE " + TABLE_SCAN + "("
//                        + SCAN_KEY_ID + " INTEGER PRIMARY KEY," + SCAN_KEY_TEXT + " TEXT,"
//                        + SCAN_KEY_STAMP + " TEXT" + ")";
//                db.execSQL(CREATE_SCAN_TABLE);
////        String CREATE_AGENDA_TABLE = "CREATE TABLE " + TABLE_AGENDA + "("
////                + AGENDA_KEY_ID + " INTEGER PRIMARY KEY," + AGENDA_KEY_NAME + " TEXT,"
////                + AGENDA_KEY_DATE + " TEXT," + AGENDA_KEY_TIME + " TEXT," + AGENDA_KEY_TYPE + " TEXT," + AGENDA_KEY_LOCATION + " TEXT," + AGENDA_KEY_DESCRIPTION + " TEXT,"+AGENDA_KEY_TOPIC + " TEXT," + AGENDA_KEY_SPEAKER + "1 TEXT," + AGENDA_KEY_SPEAKER + "2 TEXT," + AGENDA_KEY_SPEAKER + "3 TEXT," + AGENDA_KEY_SPEAKER + "4 TEXT," + AGENDA_KEY_SPEAKER + "5 TEXT," +AGENDA_KEY_ORDER_ID+ " TEXT" + ")";
////        db.execSQL(CREATE_AGENDA_TABLE);
//
//                String CREATE_AGENDA_TABLE = "CREATE TABLE " + TABLE_AGENDA + "("
//                        + AGENDA_OBJECT_ID + " TEXT PRIMARY KEY," + AGENDA_KEY_NAME + " TEXT,"
//                        + AGENDA_KEY_DATE + " TEXT," + AGENDA_KEY_TIME + " TEXT," + AGENDA_KEY_TYPE + " TEXT," + AGENDA_KEY_LOCATION + " TEXT," + AGENDA_KEY_DESCRIPTION + " TEXT,"+AGENDA_KEY_TOPIC + " TEXT," + AGENDA_KEY_SPEAKER + "1 TEXT," + AGENDA_KEY_SPEAKER + "2 TEXT," + AGENDA_KEY_SPEAKER + "3 TEXT," + AGENDA_KEY_SPEAKER + "4 TEXT," + AGENDA_KEY_SPEAKER + "5 TEXT"+ ")";
//                db.execSQL(CREATE_AGENDA_TABLE);
//
//                String CREATE_RATING_TABLE = "CREATE TABLE " + TABLE_RSTING + "("
//                        + RSTING_OBJECT_ID + " TEXT PRIMARY KEY," + RSTING_KEY_NAME + " TEXT,"
//                        + RSTING_KEY_DATE + " TEXT," + RSTING_KEY_TIME + " TEXT," + RSTING_KEY_TYPE + " TEXT," + RSTING_KEY_LOCATION + " TEXT," + RSTING_KEY_DESCRIPTION + " TEXT," + RSTING_KEY_SPEAKER + "1 TEXT," + RSTING_KEY_SPEAKER + "2 TEXT," + RSTING_KEY_SPEAKER + "3 TEXT," + RSTING_KEY_SPEAKER + "4 TEXT," + RSTING_KEY_SPEAKER + "5 TEXT" + ")";
//                db.execSQL(CREATE_RATING_TABLE);
//
//            }
//
//            // Upgrading database
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//                // Drop older table if existed
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCAN);
////        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENDA);
////        String upgradeQuery = "ALTER TABLE "+ TABLE_AGENDA+" ADD COLUMN "+AGENDA_KEY_ORDER_ID+" TEXT";
////        if (oldVersion == 1 && newVersion == 2)
////             db.execSQL(upgradeQuery);
//                // Create tables again
//                onCreate(db);
//            }
//            public void alterAgendaTable() {
//                SQLiteDatabase db = this.getWritableDatabase();
//                String upgradeQuery = "ALTER TABLE "+ TABLE_AGENDA+" ADD COLUMN "+AGENDA_KEY_ORDER_ID+" TEXT";
//                db.execSQL(upgradeQuery);
//
//                db.close();
//            }
//            /**
//             * All CRUD(Create, Read, Update, Delete) Operations
//             */
//
//            // Adding new contact
//            void addScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(SCAN_KEY_TEXT, scan.text); // Scan Text
//                values.put(SCAN_KEY_STAMP, scan.stamp.getTime()); // Scan timestamp
//
//                // Inserting Row
//                scan.ident = (int)db.insert(TABLE_SCAN, null, values);
//                db.close(); // Closing database connection
//            }
//
//            // Getting single contact
//            GTCScan getScan(int id) {
//                SQLiteDatabase db = this.getReadableDatabase();
//
//                Cursor cursor = db.query(TABLE_SCAN, new String[] { SCAN_KEY_ID,
//                                SCAN_KEY_TEXT, SCAN_KEY_STAMP }, SCAN_KEY_ID + "=?",
//                        new String[] { String.valueOf(id) }, null, null, null, null);
//                if (cursor != null)
//                    cursor.moveToFirst();
//                GTCScan scan = null;
//                try{
//                    scan = new GTCScan(Integer.parseInt(cursor.getString(0)),
//                            cursor.getString(1), new SimpleDateFormat(GTCDataManager.DATE_TIME_FORMAT).parse(cursor.getString(2)));
//                }catch(Exception e){
//
//                }
//
//                return scan;
//            }
//
//            // Getting All Scans
//            public ArrayList<GTCScan> getAllScans() {
//                ArrayList<GTCScan> scanList = new ArrayList<GTCScan>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_SCAN;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        Date d = new Date(Long.parseLong(cursor.getString(2)));
//                        GTCScan scan = new GTCScan(Integer.parseInt(cursor.getString(0)),cursor.getString(1),d);
//                        // Adding contact to list
//                        scanList.add(scan);
//                    } while (cursor.moveToNext());
//                }
//
//                // return scan list
//                return scanList;
//            }
//
//            // Updating single scan
//            public int updateScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(SCAN_KEY_TEXT, scan.text);
//                values.put(SCAN_KEY_STAMP, scan.stamp.getTime());
//
//                // updating row
//                return db.update(TABLE_SCAN, values, SCAN_KEY_ID + " = ?",
//                        new String[] { String.valueOf(scan.ident) });
//            }
//
//            // Deleting single scan
//            public void deleteScan(GTCScan scan) {
//                SQLiteDatabase db = this.getWritableDatabase();
//                db.delete(TABLE_SCAN, SCAN_KEY_ID + " = ?",
//                        new String[] { String.valueOf(scan.ident) });
//                db.close();
//            }
//
//            // Getting scans Count
//            public int getScansCount() {
//                String countQuery = "SELECT  * FROM " + TABLE_SCAN;
//                SQLiteDatabase db = this.getReadableDatabase();
//                Cursor cursor = db.rawQuery(countQuery, null);
//                cursor.close();
//
//                // return count
//                return cursor.getCount();
//            }
//
//            // Adding new Session
//            long addSession(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(AGENDA_OBJECT_ID, session.getObjectId());
//                values.put(AGENDA_KEY_NAME, session.getTitle()); // Session name
//                values.put(AGENDA_KEY_DATE, session.getDate()); // Session date
//                values.put(AGENDA_KEY_TIME, session.getTime()); // Session time
//                values.put(AGENDA_KEY_TYPE, session.getType()); // Session type
//                values.put(AGENDA_KEY_LOCATION, session.getLocation()); // Session location
////        values.put(AGENDA_KEY_ORDER_ID, session.getOrder());
////        values.put(AGENDA_KEY_DESCRIPTION, session.getDetail()); // Session description
//                values.put(AGENDA_KEY_DESCRIPTION, session.getDescription());
//                values.put(AGENDA_KEY_TOPIC, session.getTopic());
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(AGENDA_KEY_SPEAKER + (i + 1), session.getSpeakers().get(i)); // Session spkear
//                }
//
//                // Inserting Row
//                long cursorVal = db.insert(TABLE_AGENDA, null, values);
////        copyDataBaseBack();
//                db.close(); // Closing database connection
//                return cursorVal;
//            }
//
//
//            // Adding new Session
//            long addSessionRatings(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(RSTING_OBJECT_ID, session.getObjectId());
//                values.put(RSTING_KEY_NAME, session.getTitle()); // Session name
//                values.put(RSTING_KEY_DATE, session.getDate()); // Session date
//                values.put(RSTING_KEY_TIME, session.getTime()); // Session time
//                values.put(RSTING_KEY_TYPE, session.getType()); // Session type
//                values.put(RSTING_KEY_LOCATION, session.getLocation()); // Session location
//                values.put(RSTING_KEY_DESCRIPTION, session.getDetail()); // Session description
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(RSTING_KEY_SPEAKER + (i + 1), session.getSpeakers().get(i)); // Session spkear
//                }
//
//                // Inserting Row
//                long cursorVal = db.insert(TABLE_RSTING, null, values);
//
//
//                db.close(); // Closing database connection
//                return cursorVal;
//            }
//            // int getSessionId(GTCSession session){
////
////	 SQLiteDatabase db = this.getReadableDatabase();
////
////     Cursor cursor = db.query(TABLE_AGENDA, new String[] { AGENDA_OBJECT_ID }, AGENDA_KEY_NAME + "=? AND " + AGENDA_KEY_DATE + "=? AND " + AGENDA_KEY_TIME + "=?",
////             new String[] { session.getTitle()+"",session.getDate(), session.getTime()}, null, null, null, null);
////     if (cursor != null)
////         if(cursor.moveToFirst())
////        	 return cursor.getInt(0);
////     return 0;
//// }
//            // Getting single session
//            GTCSession getSession(String id) {
//                SQLiteDatabase db = this.getReadableDatabase();
//
//                Cursor cursor = db.query(TABLE_AGENDA, new String[] { AGENDA_OBJECT_ID,
//                                AGENDA_KEY_NAME, AGENDA_KEY_DATE, AGENDA_KEY_TIME, AGENDA_KEY_TYPE, AGENDA_KEY_LOCATION, AGENDA_KEY_DESCRIPTION, AGENDA_KEY_SPEAKER + "1",AGENDA_KEY_SPEAKER + "2",AGENDA_KEY_SPEAKER + "3",AGENDA_KEY_SPEAKER + "4",AGENDA_KEY_SPEAKER + "5" }, AGENDA_OBJECT_ID + "=?",
//                        new String[] { String.valueOf(id) }, null, null, null, null);
//                if (cursor != null)
//                    cursor.moveToFirst();
//                GTCSession session = null;
//                try{
//                    session = new GTCSession();
//                    session.setObjectId(cursor.getString(0));
//                    session.setTitle(cursor.getString(1));
//                    session.setDate(cursor.getString(2));
//                    session.setTime(cursor.getString(3));
//                    session.setType(cursor.getString(4));
//                    session.setLocation(cursor.getString(5));
//                    session.setDetail(cursor.getString(6));
//                    for(int i = 7; i < 12; i++){
//                        String speaker = cursor.getString(i);
//                        if(speaker != null){
//                            speaker = speaker.trim();
//                            if(speaker.length() > 0)
//                                session.addSpeaker(speaker);
//                            int k = i - 7;
//                            session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//                        }
//                    }
//                    //session.setKeyVals(t)
//                    session.setDates();
//
//                }catch(Exception e){
//
//                }
//
//                return session;
//            }
//
//            // Getting All Sessions
//            public ArrayList<GTCSession> getAllSessions() {
//                ArrayList<GTCSession> agendaList = new ArrayList<GTCSession>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_AGENDA;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        GTCSession session = null;
//                        try{
//                            session = new GTCSession();
//                            session.setObjectId(cursor.getString(0));
//                            session.setTitle(cursor.getString(1));
//                            session.setDate(cursor.getString(2));
//                            session.setTime(cursor.getString(3));
//                            session.setType(cursor.getString(4));
//                            session.setLocation(cursor.getString(5));
//                            session.setDescription(cursor.getString(6));
//                            session.setTopic(cursor.getString(7));
////                session.setOrder(Integer.parseInt(cursor.getString(13)));
//
//                            for(int i = 8; i < 13; i++){
//                                String speaker = cursor.getString(i);
//                                if(speaker != null){
//                                    speaker = speaker.trim();
//                                    if(speaker.length() > 0)
//                                        session.addSpeaker(speaker);
//                                }
//                                int k = i - 7;
//                                session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//
//                            }
//                            //session.setKeyVals();
//                            session.setDates();
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        // Adding Session to list
//                        agendaList.add(session);
//                    } while (cursor.moveToNext());
//                }
//
//                // return sessions list
//                return agendaList;
//            }
//            // Getting All Sessions
//            public ArrayList<GTCSession> getAllSessionsRatings() {
//                ArrayList<GTCSession> agendaList = new ArrayList<GTCSession>();
//                // Select All Query
//                String selectQuery = "SELECT  * FROM " + TABLE_RSTING;
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                Cursor cursor = db.rawQuery(selectQuery, null);
//
//                // looping through all rows and adding to list
//                if (cursor.moveToFirst()) {
//                    do {
//                        GTCSession session = null;
//                        try{
//                            session = new GTCSession();
//                            session.setObjectId(cursor.getString(0));
//                            session.setTitle(cursor.getString(1));
//                            session.setDate(cursor.getString(2));
//                            session.setTime(cursor.getString(3));
//                            session.setType(cursor.getString(4));
//                            session.setLocation(cursor.getString(5));
//                            session.setDetail(cursor.getString(6));
//                            for(int i = 7; i < 12; i++){
//                                String speaker = cursor.getString(i);
//                                if(speaker != null){
//                                    speaker = speaker.trim();
//                                    if(speaker.length() > 0)
//                                        session.addSpeaker(speaker);
//                                }
//                                int k = i - 6;
//                                session.getKeyVals().put("Speaker " + k, (speaker == null)?"":speaker);
//
//                            }
//                            //session.setKeyVals();
//                            session.setDates();
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        // Adding Session to list
//                        agendaList.add(session);
//                    } while (cursor.moveToNext());
//                }
//
//                // return sessions list
//                return agendaList;
//            }
//            // Updating single session
//            public int updateSession(GTCSession session) {
//                SQLiteDatabase db = this.getWritableDatabase();
//
//                ContentValues values = new ContentValues();
//                values.put(AGENDA_KEY_NAME, session.getTitle());
//                values.put(AGENDA_KEY_DATE, session.getDate());
//                values.put(AGENDA_KEY_TIME, session.getTime());
//                values.put(AGENDA_KEY_TYPE, session.getType());
//                values.put(AGENDA_KEY_LOCATION, session.getLocation());
//                values.put(AGENDA_KEY_DESCRIPTION, session.getDetail());
//                for(int i = 0; i < session.getSpeakers().size(); i++){
//                    values.put(AGENDA_KEY_SPEAKER + i,session.getSpeakers().get(i) );
//                }
//
//                // updating row
//                return db.update(TABLE_AGENDA, values, AGENDA_OBJECT_ID + " = ?",
//                        new String[] { String.valueOf(session.getObjectId()) });
//            }
//            public void copyDataBaseBack() {
//                Log.i("info", "in copy data base at finally");
//                try {
//                    File sd = Environment.getExternalStorageDirectory();
//                    File data = Environment.getDataDirectory();
//                    System.out.println("Andy path "+sd.canWrite());
//
//                    if (sd.canWrite()) {
//                        String currentDBPath = "/data/" + context.getPackageName()
//                                + "/databases/SCANS_2015";
//                        String backupDBPath = "SCANS_2015.sqlite";
//                        File currentDB = new File(data, currentDBPath);
//                        File backupDB = new File(sd, backupDBPath);
//                        if (currentDB.exists()) {
//                            FileChannel src = new FileInputStream(currentDB)
//                                    .getChannel();
//                            FileChannel dst = new FileOutputStream(backupDB)
//                                    .getChannel();
//                            dst.transferFrom(src, 0, src.size());
//                            src.close();
//                            dst.close();
//                        }
//                    }
//
//                } catch (Exception e) {
//
//                    Log.i("info", "in copy of bata base 10 ");
//
//                }
//            }
//            // Deleting single session
//            public void deleteSession(GTCSession session) {
//
//                SQLiteDatabase db = this.getWritableDatabase();
//                db.delete(TABLE_AGENDA, AGENDA_OBJECT_ID + " = ?",
//                        new String[] { String.valueOf(session.getObjectId()) });
//                db.close();
//            }
//
//            // Getting sessions Count
//            public int getSessionsCount() {
//                String countQuery = "SELECT  * FROM " + TABLE_AGENDA;
//                SQLiteDatabase db = this.getReadableDatabase();
//                Cursor cursor = db.rawQuery(countQuery, null);
//                cursor.close();
//
//                // return count
//                return cursor.getCount();
//            }
//        }
//        public class StringDateComparator implements Comparator<String> {
//
//            @Override
//            public int compare(String lhs, String rhs) {
//
//                Date d1 = null;
//                Date d2 = null;
//                try {
//                    d1 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(lhs);
//
//                } catch (ParseException e) {
//
//                    try{
//                        String tokens1[] = lhs.split("-");
//                        if(tokens1.length > 1){
//                            lhs = tokens1[1];
//                        }else{
//                            lhs = tokens1[0];
//                        }
//
//                        d1 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(lhs);
//                    }catch(ParseException e1){
//                        e1.printStackTrace();
//
//                    }
//
//                }
//                try{
//                    d2 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(rhs);
//
//                }catch (ParseException e) {
//
//                    try{
//                        String tokens2[] = rhs.split("-");
//                        if(tokens2.length > 1){
//                            rhs = tokens2[1];
//                        }else{
//                            rhs = tokens2[0];
//                        }
//
//                        d2 = new SimpleDateFormat(GTCDataManager.TIME_FORMAT).parse(rhs);
//                        return d1.compareTo(d2);
//                    }catch(ParseException e1){
//                        e1.printStackTrace();
//
//                    }
//
//
//
//                }
//                return d1.compareTo(d2);
//
//            }
//        }
//        public class GTCSessionComparator implements Comparator<GTCSession>{
//
//            @Override
//            public int compare(GTCSession lhs, GTCSession rhs) {
//
//                return ((GTCSession)lhs).getOrder() - ((GTCSession)rhs).getOrder();
//            }
//        }
//
//        public class GTCSpeakersComparator implements Comparator<GTCSpeaker>{
//
//            @Override
//            public int compare(GTCSpeaker lhs, GTCSpeaker rhs) {
//                return lhs.getOrder() - rhs.getOrder();
//            }
//
//        }
//        public class GTCSurveysComparator implements Comparator<GTCSurvey>{
//
//            @Override
//            public int compare(GTCSurvey lhs, GTCSurvey rhs) {
//
//                return ((GTCSurvey)lhs).getOrder() - ((GTCSurvey)rhs).getOrder();
//            }
//        }
//
//        public class GTCUsersComparator implements Comparator<GTCUser>{
//
//            @Override
//            public int compare(GTCUser lhs, GTCUser rhs) {
//
//                return ((GTCUser)lhs).getFirstName().compareTo(((GTCUser)rhs).getFirstName());
//            }
//        }
//        public class GTCAttendeeComparator implements Comparator<GTCAttendee>{
//
//            @Override
//            public int compare(GTCAttendee lhs, GTCAttendee rhs) {
//
//                return ((GTCAttendee)lhs).getFirstName().compareTo(((GTCAttendee)rhs).getFirstName());
//            }
//        }
//
//        public static int getResId(String variableName, Context context, Class<?> c) {
//
//            try {
//                Field idField = c.getDeclaredField(variableName);
//                return idField.getInt(idField);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return -1;
//            }
//        }
//        public boolean isLoggedIn(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getBoolean("IsLoggedIn",false);
//
//        }
//        public String getStudyYear(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getString("Year","Alumni Events");
//
//        }
//        public String getEmail(){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            return settings.getString("Email",null);
//
//        }
//        public void login(String email, final ILoginCallback callback){
//            ParseUser.logInInBackground(email, "123456", new LogInCallback(){
//                @Override
//                public void done(final ParseUser user, final com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Logged in with email and pass ...");
//                    if(user != null){
//                        Crashlytics.log(Log.INFO, "STS", "User is not null ...");
//                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                        final SharedPreferences.Editor editor = settings.edit();
//                        editor.putBoolean("IsLoggedIn", true);
//                        editor.putString("Email", user.getEmail());
//                        editor.putString("UserName", user.getUsername());
//                        try{
//                            GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                            editor.commit();
//                            Crashlytics.log(Log.INFO, "STS", "Going to fetch user profile ...");
//                            uprof.fetchInBackground(new GetCallback<GTCUser>(){
//
//                                @Override
//                                public void done(GTCUser uprof,
//                                                 com.parse.ParseException arg1) {
//                                    Crashlytics.log(Log.INFO, "STS", "Fetched user profile...");
//                                    if(uprof != null){
//                                        Crashlytics.log(Log.INFO, "STS", "Profile is not null ...");
//                                        editor.putString("Year", uprof.getStudyYear());
//                                        editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                        editor.commit();
//                                        callback.onLogin(true, e,user);
//                                    }else{
//                                        if(e != null){
//                                            Crashlytics.log(Log.INFO, "STS", "Profile is null ...");
//                                            Crashlytics.logException(e);
//
//                                        }else{
//                                            arg1 = new com.parse.ParseException(0, "Profile is null");
//                                            Crashlytics.logException(e);
//                                        }
//                                        callback.onLogin(false, e,user);
//                                    }
//                                }
//
//                            });
//                        }catch(Exception e1){
//                            Crashlytics.log(Log.INFO, "STS", "An exception occurred ..." + e1.getMessage());
//                            Crashlytics.logException(e1);
//                            callback.onLogin(false, e,user);
//                        }
//
//                    }else{
//                        Crashlytics.log(Log.INFO, "STS", "User is null ...");
//                        Crashlytics.logException(e);
//                        callback.onLogin((user == null)?false:true, e,user);
//                    }
//
//
//                }
//
//            });
//        }
//        public void login(String email,String password, final ILoginCallback callback){
//            ParseUser.logInInBackground(email, password, new LogInCallback(){
//                @Override
//                public void done(final ParseUser user, final com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Logged in with email and pass ...");
//                    if(user != null){
//                        Crashlytics.log(Log.INFO, "STS", "User is not null ...");
//                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                        final SharedPreferences.Editor editor = settings.edit();
//                        editor.putBoolean("IsLoggedIn", true);
//                        editor.putString("Email", user.getEmail());
//                        editor.putString("UserName", user.getUsername());
//
//                        try{
//                            GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                            editor.commit();
//                            Crashlytics.log(Log.INFO, "STS", "Going to fetch user profile ...");
//                            uprof.fetchInBackground(new GetCallback<GTCUser>(){
//
//                                @Override
//                                public void done(GTCUser uprof,
//                                                 com.parse.ParseException arg1) {
//                                    Crashlytics.log(Log.INFO, "STS", "Fetched user profile...");
//                                    if(uprof != null){
//                                        Crashlytics.log(Log.INFO, "STS", "Profile is not null ...");
//                                        writing_profilepic_external(uprof.getImage(), uprof.getFirstName()+"_"+uprof.getLastName());
//                                        editor.putString("Year", uprof.getStudyYear());
//                                        editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                        editor.putString("FbFirstName", uprof.getFirstName());
//                                        editor.putString("FbLastName", uprof.getLastName());
//                                        editor.putString("FbEmail", uprof.getUserEmail());
//                                        editor.putString("FbUserId", "");
//                                        editor.commit();
//                                        callback.onLogin(true, e,user);
//                                    }else{
//                                        if(e != null){
//                                            Crashlytics.log(Log.INFO, "STS", "Profile is null ...");
//                                            Crashlytics.logException(e);
//
//                                        }else{
//                                            arg1 = new com.parse.ParseException(0, "Profile is null");
//                                            Crashlytics.logException(e);
//                                        }
//                                        callback.onLogin(false, e,user);
//                                    }
//                                }
//
//                            });
//                        }catch(Exception e1){
//                            Crashlytics.log(Log.INFO, "STS", "An exception occurred ..." + e1.getMessage());
//                            Crashlytics.logException(e1);
//                            callback.onLogin(false, e,user);
//                        }
//
//                    }else{
//                        Crashlytics.log(Log.INFO, "STS", "User is null ...");
//                        Crashlytics.logException(e);
//                        callback.onLogin((user == null)?false:true, e,user);
//                    }
//
//
//                }
//
//            });
//        }
//        void writing_profilepic_external(ParseFile file_this,String image_name)
//        {
//            if(file_this!=null)
//            {
//                ParseFile file = (ParseFile) file_this;
//
//                try {
//                    if(file.getName() != null){
//                        String[] filenameTokens = file.getName().split("-");
//                        System.out.println("Andy files check this "+image_name);
//
//                        String fullPath = "/data/data/" + context.getPackageName() + "/files/"+image_name;
//
//                        File file_check = new File(fullPath);
//
//
//                        System.out.println("Andy data parse false");
//                        byte[] data;
//                        try {
//                            data = file.getData();
//                            FileOutputStream stream = context.openFileOutput(image_name,Context.MODE_PRIVATE);
//                            stream.write(data);
//                            stream.close();
//                        } catch (com.parse.ParseException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//
//
//
//                    }	} catch (FileNotFoundException e) {
//
//                    e.printStackTrace();
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        public void register(final String email, final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//
//            final ParseUser user = ParseUser.getCurrentUser();
////	user.setEmail(email);
//            user.setUsername(email);
//            user.setPassword("123456");
//            Crashlytics.log(Log.INFO, "STS", "Saving in background...");
//            user.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Save yielded ...");
//                    if( e == null ){
//                        Crashlytics.log(Log.INFO, "STS", "Saved and no exception...");
//                        final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
//                        uprof.setUserEmail(email);
//                        uprof.setUsername(email);
//                        uprof.setFirstName(firstname);
//                        uprof.setLastName(lastname);
//                        if(!year.isEmpty())
//                            uprof.setStudyYear(year);
//                        user.put("profile",uprof);
//                        Crashlytics.log(Log.INFO, "STS", "Saving profile in background ...");
//                        user.saveInBackground(new SaveCallback(){
//
//                            @Override
//                            public void done(com.parse.ParseException e) {
//                                Crashlytics.log(Log.INFO, "STS", "Saving profile yielded...");
//                                if(e == null){
//                                    Crashlytics.log(Log.INFO, "STS", "Profile saved and no exception ...");
//                                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings.edit();
//                                    editor.putBoolean("IsLoggedIn", true);
//                                    editor.putString("Email", user.getEmail());
//                                    editor.putString("UserName", user.getUsername());
//                                    GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                                    editor.putString("Year", uprof.getStudyYear());
//                                    editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                    editor.commit();
//                                    callback.onRegister(true,e, user);
//                                    uprof.setUser(user);
//                                    uprof.setImage(image);
//                                    uprof.setThumbnail(thumb);
//                                    uprof.saveInBackground();
//                                }else{
//                                    Crashlytics.log(Log.INFO, "STS", "Profile save had an exception ..." + e.getMessage());
//                                    Crashlytics.logException(e);
//                                    callback.onRegister(false, e,null);
//                                }
//
//                            }
//
//                        });
//                    }else{
//
//                        Crashlytics.log(Log.INFO, "STS", "Save had an exception ..." + e.getMessage());
//                        Crashlytics.logException(e);
//                        callback.onRegister(false, e,null);
//                    }
//
//                }
//
//            });
//
//        }
//        public void register(final String email, final String password,final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//
//            final ParseUser user = ParseUser.getCurrentUser();
//            user.setEmail(email);
//            user.setUsername(email);
//            user.setPassword(password);
//            Crashlytics.log(Log.INFO, "STS", "Saving in background...");
//            user.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    Crashlytics.log(Log.INFO, "STS", "Save yielded ...");
//                    if( e == null ){
//                        Crashlytics.log(Log.INFO, "STS", "Saved and no exception...");
//                        final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
//                        uprof.setUserEmail(email);
//                        uprof.setUsername(email);
//                        uprof.setFirstName(firstname);
//                        uprof.setLastName(lastname);
//                        if(!year.isEmpty())
//                            uprof.setStudyYear(year);
//                        user.put("profile",uprof);
//                        Crashlytics.log(Log.INFO, "STS", "Saving profile in background ...");
//                        user.saveInBackground(new SaveCallback(){
//
//                            @Override
//                            public void done(com.parse.ParseException e) {
//                                Crashlytics.log(Log.INFO, "STS", "Saving profile yielded...");
//                                if(e == null){
//                                    Crashlytics.log(Log.INFO, "STS", "Profile saved and no exception ...");
//                                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                                    SharedPreferences.Editor editor = settings.edit();
////					    editor.putBoolean("IsLoggedIn", true);
////					    editor.putString("Email", user.getEmail());
////					    editor.putString("UserName", user.getUsername());
//                                    GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                                    editor.putString("Year", uprof.getStudyYear());
//                                    editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                                    editor.commit();
//                                    callback.onRegister(true,e, user);
//                                    uprof.setUser(user);
//                                    uprof.setImage(image);
//                                    uprof.setThumbnail(thumb);
//                                    uprof.saveInBackground();
//                                }else{
//                                    Crashlytics.log(Log.INFO, "STS", "Profile save had an exception ..." + e.getMessage());
//                                    Crashlytics.logException(e);
//                                    callback.onRegister(false, e,null);
//                                }
//
//                            }
//
//                        });
//                    }else{
//
//                        Crashlytics.log(Log.INFO, "STS", "Save had an exception ..." + e.getMessage());
//                        Crashlytics.logException(e);
//                        callback.onRegister(false, e,null);
//                    }
//
//                }
//
//            });
//
//        }
//        public void update(final String email, final String firstname, final String lastname, final String year, final String phone, final String organization, final String facebook, final String twitter, String linkedin, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback){
//            GTCUser uprof = sharedDataManager.getCurrentProfile();
//            uprof.setUserEmail(email);
//            uprof.setFirstName(firstname);
//            uprof.setLastName(lastname);
//            if(!year.isEmpty())
//                uprof.setStudyYear(year);
//            uprof.setPhone(phone);
//            uprof.setOrganization(organization);
//            uprof.setTwitter(twitter);
//            uprof.setFacebook(facebook);
//            uprof.setLinkedin(linkedin);
//            if(image != null){
//                uprof.setImage(image);
//            }
//            if(thumb != null){
//                uprof.setThumbnail(thumb);
//            }
//            uprof.saveInBackground(new SaveCallback(){
//
//                @Override
//                public void done(com.parse.ParseException e) {
//                    if(e == null){
//                        callback.onRegister(true,e, sharedDataManager.getCurrentUser());
//                    }else{
//                        Crashlytics.logException(e);
//                        callback.onRegister(false,e,sharedDataManager.getCurrentUser());
//                    }
//
//                }
//
//            });
//
//
//
//
//        }
//
//        @Override
//        public void downloadCompleted() {
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putFloat("AppVersion", currentAppVersion);
//            editor.putFloat("DataVersion", currentDataVersion);
//            latestDataVersion=currentDataVersion;
//            editor.commit();
//
//        }
//
//        public void tryUpdateVersion(float av, float dv){
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            if(settings.getFloat("AppVersion", 0) <= 0 && settings.getFloat("DataVersion", 0) <= 0){
//                currentAppVersion = av;
//                currentDataVersion = dv;
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putFloat("AppVersion", av);
//                editor.putFloat("DataVersion", dv);
//                editor.commit();
//            }
//
//        }
//        public static void showAlert(Context context, String title, String message){
//            new AlertDialog.Builder(context)
//                    .setTitle(title)
//                    .setMessage(message)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//        public ParseUser getCurrentUser(){
//            return ParseUser.getCurrentUser();
//        }
//        public GTCUser getCurrentProfile(){
//            GTCUser user = null;
//            if(CheckIsConnectedToInternet(context)){
//                ((ParseObject)getCurrentUser().get("profile")).fetchInBackground(new GetCallback<GTCUser>(){
//
//                    @Override
//                    public void done(GTCUser uprof, com.parse.ParseException arg1) {
//                        if(uprof != null){
//                            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                            SharedPreferences.Editor editor = settings.edit();
//                            editor.putString("Year", uprof.getStudyYear());
//                            editor.putString("PROFILE", uprof.fields(context, true,0).toString());
//                            editor.commit();
//                        }
//                    }
//
//                });
//
//            }
//            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//            user = GTCUser.create(GTCUser.class);
//            try {
//                user.load(new JSONObject(settings.getString("PROFILE","{}")));
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            return user;
//        }
//        public void getAppUsers(final FindCallback<ParseUser> cb){
//            final List<ParseUser> _users = new ArrayList<ParseUser>();
//
//
//            FileInputStream in;
//            try {
//                in = context.openFileInput("appusers.json");
//                int size = in.available();
//                byte[] buffer = new byte[size];
//                in.read(buffer);
//                in.close();
//                String json = new String(buffer, "UTF-8");
//                JSONArray items = new JSONArray(json);
//                for(int i = 0; i < items.length(); i++){
//                    try{
//                        JSONObject object = items.getJSONObject(i);
//                        ParseUser obj = (ParseUser) ParseUser.class.getConstructors()[0].newInstance();
//                        GTCObject.load(obj, object);
//                        _users.add(obj);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            } catch (FileNotFoundException e1) {
//                e1.printStackTrace();
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            cb.done(_users, null);
//            if(CheckIsConnectedToInternet(context)){
//                ParseQuery<ParseUser> q = ParseQuery.getQuery(ParseUser.class);
//                q.findInBackground(new FindCallback<ParseUser>(){
//
//                    @Override
//                    public void done(final List<ParseUser> arg0, final com.parse.ParseException pe) {
//                        if(arg0 != null){
//                            new Thread(
//                                    new Runnable(){
//
//                                        @Override
//                                        public void run() {
//
//                                            FileOutputStream out = null;
//                                            try {
//                                                out = context.openFileOutput("appusers.json", Context.MODE_WORLD_READABLE);
//                                            } catch (FileNotFoundException e1) {
//                                                e1.printStackTrace();
//                                            }
//                                            final JSONArray arr = new JSONArray();
//
//                                            for(final ParseUser u: arg0){
//                                                arr.put(GTCObject.fields(u, context, true, 0));
//                                            }
//
//                                            try {
//                                                out.write(arr.toString().getBytes());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }finally{
//                                                try {
//                                                    out.close();
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                        }
//                                    }).start();
//                        }
//                    }
//
//                });
//
//            }
//
//        }
//        public static ParseUser findUserByObjectId(List<ParseUser> objects, String objId){
//            for(ParseUser u:objects){
//                if(u.getObjectId().equalsIgnoreCase(objId))
//                    return u;
//            }
//            return null;
//        }
//        public static GTCObject findByObjectId(ArrayList<? extends GTCObject> objects, String objId){
//            for(GTCObject u:objects){
//                if(u.getObjectId().equalsIgnoreCase(objId))
//                    return u;
//            }
//            return null;
//        }
//        public static ProgressDialog showProgress(Context context, String title, String message){
//            final ProgressDialog ringProgressDialog = ProgressDialog.show(context, title,	message, true);
//            return ringProgressDialog;
//        }
//        public static void showProgress(Context context, String title, String message, final IWorkCallback cb){
//            final ProgressDialog ringProgressDialog = ProgressDialog.show(context, title,	message, true);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        cb.doWork();
//                    } catch (Exception e) {
//
//                    }
//                    ringProgressDialog.dismiss();
//                }
//            }).start();
//        }
//        public boolean CheckIsConnectedToInternet(Context _context) {
//            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connectivity != null) {
//                NetworkInfo[] info = connectivity.getAllNetworkInfo();
//                if (info != null)
//                    for (int i = 0; i < info.length; i++)
//                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                            return true;
//                        }
//
//            }
//            return false;
//        }
//        public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
//            ExifInterface ei;
//            Bitmap bmp = null;
//            try {
//                ei = new ExifInterface(path);
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                        ExifInterface.ORIENTATION_NORMAL);
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        bmp= rotateImage(bitmap, 90);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        bmp = rotateImage(bitmap, 180);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        bmp = rotateImage(bitmap, 270);
//                        break;
//                    default:{
//                        bmp = bitmap;
//                        break;
//                    }
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return bmp;
//        }
//
//        public static Bitmap rotateImage(Bitmap source, float angle) {
//
//            Bitmap bitmap = null;
//            Matrix matrix = new Matrix();
//            matrix.postRotate(angle);
//            try {
//                bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                        matrix, true);
//                if(bitmap != source){
//                    source.recycle();
//                }
//            } catch (OutOfMemoryError err) {
//                err.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        public static Bitmap flipImage(Bitmap source) {
//
//            Bitmap bitmap = null;
//            Matrix matrix = new Matrix();
//            matrix.setScale(-1,1);
//            try {
//                bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                        matrix, true);
//                if(bitmap != source){
//                    source.recycle();
//                }
//            } catch (OutOfMemoryError err) {
//                err.printStackTrace();
//            }
//            return bitmap;
//        }
//
//
//        public final static int PIC_CROP = 2;
//        public final static int TAKE_PICTURE = 1;
//
//        private static final int REQ_CODE_PICK_IMAGE = 100;
//        public static void photoFunctionalityLaunch(final Activity context, final Uri imageUri){
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Choose an option");
//            builder.setItems(new CharSequence[]{"Choose a picture", "Take a photo"}, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    if(which  == 0){
//                        Intent i = new Intent(Intent.ACTION_PICK);
//                        i.setType("image/*");
//                        context.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
//	        	/*
//	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//	        	photoPickerIntent.setType("image/*");
//	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/
//                    }else{
//                        Intent intent = new Intent(context, GTCCameraActivity.class);//"android.media.action.IMAGE_CAPTURE");//
//                        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        context.startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//
//            });
//            builder.show();
//        }
//        public static void photoFunctionalityLaunch(final Fragment fragment, final Uri imageUri){
//            AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
//            builder.setTitle("Choose");
//            builder.setItems(new CharSequence[]{"Choose a picutre", "Take a photo"}, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    if(which  == 0){
//                        Intent i = new Intent(Intent.ACTION_PICK);
//                        i.setType("image/*");
//                        fragment.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
//	        	/*
//	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//	        	photoPickerIntent.setType("image/*");
//	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/
//
//                    }else{
//                        Intent intent = new Intent(fragment.getActivity(), GTCCameraActivity.class); //MediaStore.ACTION_IMAGE_CAPTURE);//
//                        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        fragment.startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//
//            });
//            builder.show();
//        }
//        public static Bitmap handleBitmap(Bitmap bitmap, Uri imageUri){
//            Uri selectedImage = imageUri;
//            try {
//
//                bitmap = GTCDataManager.imageOreintationValidator(bitmap, selectedImage.toString());
//                FileOutputStream out = new FileOutputStream(imageUri.toString());
//                bitmap.compress(CompressFormat.JPEG, 9, out);
//
//
//            } catch (Exception e) {
//                Log.e("Camera", e.toString());
//            }
//            return bitmap;
//        }
//        public static Object handlePhotoFunctionalityResult(Context ctx, int requestCode, int resultCode, Intent data, Uri imageUri){
//            Bitmap bitmap = null;
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    if (resultCode == Activity.RESULT_OK) {
//                        Uri selectedImage = imageUri;
//                        ctx.getContentResolver().notifyChange(selectedImage, null);
//                        ContentResolver cr = ctx.getContentResolver();
//                        try {
//
//                            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                            selectedImage = imageUri;
//
//                        } catch (Exception e) {
//                            Log.e("Camera", e.toString());
//                        }
//                        photoCropFunctionality(ctx, selectedImage);
//                        return selectedImage;
//                    }break;
//                case REQ_CODE_PICK_IMAGE:
//                    if(resultCode == Activity.RESULT_OK){
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        Cursor cursor = ctx.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                        cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex); cursor.close();
//                        bitmap = BitmapFactory.decodeFile(picturePath);
//                        bitmap = GTCDataManager.imageOreintationValidator(bitmap, picturePath);
//                        try {
//                            URI uri = new URI(imageUri.toString());
//                            FileOutputStream out = new FileOutputStream(new File(uri));
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            selectedImage = imageUri;
//                            //bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        photoCropFunctionality(ctx, imageUri);
//                        return selectedImage;
//                    }break;
//                case Crop.REQUEST_CROP:
//                    Uri selectedImage = imageUri;
//                    ctx.getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = ctx.getContentResolver();
//                    try {
//
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//                        return bitmap;
//
//                    } catch (Exception e) {
//                        Log.e("Camera", e.toString());
//                    }
//            }
//            return bitmap;
//
//        }
//        public static Object handlePhotoFunctionalityResult(Fragment fragment, int requestCode, int resultCode, Intent data, Uri imageUri){
//            Bitmap bitmap = null;
//            Context context = fragment.getActivity();
//            switch (requestCode) {
//                case TAKE_PICTURE:
//                    if (resultCode == Activity.RESULT_OK) {
//                        Uri selectedImage = imageUri;
//                        context.getContentResolver().notifyChange(selectedImage, null);
//                        ContentResolver cr = context.getContentResolver();
//                        try {
//
//                            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
//                            selectedImage = imageUri;
//
//                        } catch (Exception e) {
//                            Log.e("Camera", e.toString());
//                        }
//                        photoCropFunctionality(fragment, selectedImage);
//                        return selectedImage;
//                    }break;
//                case REQ_CODE_PICK_IMAGE:
//                    if(resultCode == Activity.RESULT_OK){
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//                        cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex); cursor.close();
//                        bitmap = BitmapFactory.decodeFile(picturePath);
//                        bitmap = GTCDataManager.imageOreintationValidator(bitmap, picturePath);
//                        try {
//                            URI uri = new URI(imageUri.toString());
//                            FileOutputStream out = new FileOutputStream(new File(uri));
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            selectedImage = imageUri;
//                            bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//                        photoCropFunctionality(fragment, imageUri);
//                        return selectedImage;
//                    }break;
//                case Crop.REQUEST_CROP:
//                    Uri selectedImage = Crop.getOutput(data);
//                    context.getContentResolver().notifyChange(selectedImage, null);
//                    ContentResolver cr = context.getContentResolver();
//                    try {
//
//                        bitmap = android.provider.MediaStore.Images.Media
//                                .getBitmap(cr, selectedImage);
//
//
//                    } catch (Exception e) {
//                        Log.e("Camera", e.toString());
//                    }
//            }
//            return bitmap;
//        }
//        public static void photoCropFunctionality(Fragment fragment, Uri picUri){
//
//            new Crop(picUri).output(picUri).asSquare().start(fragment.getActivity(),fragment);
//        }
//        public static void photoCropFunctionality(Context context, Uri picUri){
//            new Crop(picUri).output(picUri).asSquare().start((Activity) context);
//        }
//
//        public static String capitalize(String str){
//            if(!str.isEmpty()){
//                String[] tokens = str.split(" ");
//                for(int i = 0; i < tokens.length; i++){
//                    String token = tokens[i];
//                    if(token.length() > 0){
//                        tokens[i] = token.substring(0,1).toUpperCase() + ((token.length() > 1)?token.substring(1):"");
//                    }
//                }
//                str = "";
//                for(int i = 0; i < tokens.length; i++){
//                    String token = tokens[i];
//                    if(i == 0){
//                        str += token;
//                    }else{
//                        str += " " +token;
//                    }
//                }
//            }
//            return str;
//
//        }
//    }
//
//
//}

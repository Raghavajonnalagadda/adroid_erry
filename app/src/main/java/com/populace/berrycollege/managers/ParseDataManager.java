package com.populace.berrycollege.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.populace.berrycollege.R;
import com.populace.berrycollege.activities.GTCCameraActivity;
import com.populace.berrycollege.activities.MainActivity;
import com.populace.berrycollege.models.GTCObject;
import com.populace.berrycollege.models.GTCPhoto;
import com.populace.berrycollege.models.GTCUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import soundcloud.android.crop.Crop;

public class ParseDataManager implements DownloadCompleteCallback {
    private static ParseDataManager sharedDataManager = null;
    MainActivity ac;
    public Context context;
    public ParseInstallation installation;
    ProgressDialog berryProgress;
    public static boolean showsDownloadActivity = true;
    public static String TAG_CHAR_DEFINITION = "definition";
    public static String TAG_CHAR_BCV = "bcv";
    public static String TAG_CHAR_QOUTE = "quote";
    public static String TAG_ACAD_EVENT_CAL = "event_calender";
    public static String TAG_ACAD_LIBRARY = "library";
    public static String TAG_ACAD_CLUB = "clubs";
    public static String TAG_ACAD_TUTORING = "tutoring";
    public static String TAG_NUTRITION_DIETS = "diets";
    public static String TAG_NUTRITION_MENU = "menu";
    public static String TAG_NUTRITION_CAL_COUNT = "calorie_counter";
    public static String TAG_NUTRITION_MY_DIET = "my_diet";
    public static String TAG__PHYSICAL_DAILY_WORKOUT = "daily_workout";
    public static String TAG__PHYSICAL_THE_CAGE = "the_cage";
    public static String TAG__PHYSICAL_ACTIVITIES = "activities";
    public static String TAG__PHYSICAL_INTRAMURAL_SCHEDULE = "intramural_schedule";
    public static String TAG__PHYSICAL_INTRAMURAL_REGISTER = "intramural_register";
    public static String TAG__PHYSICAL_INTRAMURAL_ON_BIKING = "on_biking";
    public static String TAG__PHYSICAL_INTRAMURAL_ON_TRAIL_MAP = "on_trail_map";
    public static String TAG__PHYSICAL_INTRAMURAL_OFF_RACE = "off_races";
    public static String TAG__PHYSICAL_INTRAMURAL_OFF_RIVER_RENTALS = "off_river_rentals";
    public static String TAG__PHYSICAL_INTRAMURAL_OFF_HIKING = "off_hiking";
    public static String TAG_EMOTION_DEFINITION = "definition";
    public static String TAG_EMOTION_ON_CAMPUS_RESOURCES = "on_campus_resources";
    public static String TAG_EMOTION_OFF_CAMPUS_RESOURCES = "off_campus_resources";
    public static String TAG__EMOTION_APPOINTMENTS = "http://www.berry.edu/stulife/counseling/appointments/";
    public static String TAG__EMOTION_GENERAL_INFO = "https://www.berry.edu/stulife/counseling/";
    public static String TAG_SPIR_RELIGIOUS_EVENTS = "religious_events";
    public static String TAG_SPIR_SPIRTUALITY = "spirtuality";
    public static String TAG_SPIR_CALANDER = "http://ems.berry.edu/MasterCalendar/MasterCalendar.aspx?data=Ai6R2a1%2f4RXbqjkg%2fwalYtxW1e0PEIGrbZmKzGAU8QCzO0%2b9CsQycR2FvrkVvqJahJxmfkqNLRE%3d";
    public static String TAG_SPIR_GROUPS = "groups";
    public static String TAG_SPIR_CHAMPLAIN_OFFICES = "chaplain_offices";
    public static String TAG_SOCIAL_KCAB = "kcab";
    public static String TAG_SOCIAL_STD_ACT = "student_activities";
    public static String TAG_SOCIAL_SPORTS = "sports";
    public static String TAG_SOCIAL_SGA = "sga";
    public static String TAG_LINK_FACEBOOK = "facebook";
    public static String TAG_LINK_TWITTER = "twitter";
    public static String TAG_LINK_YOU_TUBE = "youtube";
    public static String TAG_LINK_PINTEREST = "pinterest";
    public static String TAG_LINK_INSTAGRAM = "instagram";
    public List<ParseObject> images = new ArrayList<>();;
    public  static ParseObject profileObj = null;

    static DownloadCompleteCallback callback_download;
    public ArrayList<GTCUser> users = new ArrayList<GTCUser>();
    ArrayList<Academic> academics = new ArrayList<Academic>();
    public static List<ParseObject> events_parse = new ArrayList<ParseObject>();
    ProgressDialog dialog;
    public Location currentLocation;
    Handler msgHandler = new Handler();
    float currentDataVersion;
    float currentAppVersion;
    float latestDataVersion=1f;
    public Integer stopCounts[];
    public Integer stopStampCounts[];
    public Integer totalStopStampCount;
    public Integer totalTourStampCount;
    //    ConnectivityBroadcastReceiver connectivityReceiver;
    public boolean isConnected;
    public boolean isParseInitializationPending;
    boolean recipe = false, events_bool = false, intro = false, restuarenta = false, ingredient = false;

    public void setContext(Context ctx) {
//        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
//            sharedDataManager.isConnected = true;
//        }
//        IntentFilter iFilter = new IntentFilter();
//        iFilter.addAction("com.populace.nebraskapassport.ConnectivityIntent");
//        if(context != null && context != ctx){
//            try{
//                if(connectivityReceiver != null){
//                    context.unregisterReceiver(connectivityReceiver);
//                }
//            }catch(Exception e){
//            }
//        }else{
//            if(connectivityReceiver == null){
//                connectivityReceiver= new ConnectivityBroadcastReceiver();
//            }
//        }
        context = ctx;


//        try {
//            context.registerReceiver(connectivityReceiver, iFilter);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//        }


    }


    private ParseDataManager(Context ctx) {
        context = ctx;
    }
    public static GTCObject findByObjectId(ArrayList<? extends GTCObject> objects, String objId) {
        for (GTCObject u : objects) {
            if (u.getObjectId().equalsIgnoreCase(objId))
                return u;
        }
        return null;
    }

    public static ParseDataManager sharedDataManager(Context context) {
        if (sharedDataManager == null) {
            sharedDataManager = new ParseDataManager(context);
            sharedDataManager.setContext(context);
            sharedDataManager.initialize();
            callback_download = sharedDataManager;
        }
        return sharedDataManager;
    }

    public void initialize() {

        try{
            sharedDataManager.copyInitialData();
            sharedDataManager.initializeParse();
            sharedDataManager.prepareDatabase();
            sharedDataManager.tryUpdateVersion(1f, 1.0f);
            if(CheckIsConnectedToInternet(context)){
                new Thread(
                        new Runnable(){

                            @Override
                            public void run() {
                                //   sharedDataManager.downloadUsers();
                                //     sharedDataManager.downloadPhotos();
                            }
                        }).start();
            }
            sharedDataManager.loadData();


        }catch(Exception e){
            e.printStackTrace();
        }
    }



    public void prepareDatabase() {

    }



    public static ProgressDialog showProgress(Context context, String title, String message)
    {
        ProgressDialog ringProgressDialog = ProgressDialog.show(context, title,	message, true);
        return ringProgressDialog;
    }



    //    public static void showProgress(Context context, String title,
//                                    String message, final IWorkCallback cb) {
//        final ProgressDialog ringProgressDialog = ProgressDialog.show(context,
//                title, message, true);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    cb.doWork();
//                } catch (Exception e) {
//
//                }
//                ringProgressDialog.dismiss();
//            }
//        }).start();
//    }
    public void syncCheckins() {
    }

    private void copyInitialData() {
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public void initializeParse() {


        ParseObject.registerSubclass(GTCUser.class);
        ParseObject.registerSubclass(GTCPhoto.class);


        try {

            installation = ParseInstallation.getCurrentInstallation();
            installation.put("appIdentifier", "com.populace.cdguide");
            installation.saveInBackground();


        } catch (Exception e) {
            System.out.println("Andy error check " + e);
            e.printStackTrace();
        }

    }


    void writing_profilepic_external(ParseFile file_this) {
        if (file_this != null) {
            ParseFile file = (ParseFile) file_this;

            try {
                if (file.getName() != null) {
                    String[] filenameTokens = file.getName().split("-");
                    System.out.println("Andy files check this " + trimNameForImage(filenameTokens[filenameTokens.length - 1]));

                    String fullPath = "/data/data/" + context.getPackageName() + "/files/" + trimNameForImage(filenameTokens[filenameTokens.length - 1]);

                    File file_check = new File(fullPath);

                    if (file_check.exists()) {
                        System.out.println("Andy data parse true");
                    } else {
                        System.out.println("Andy data parse false");
                        byte[] data = file.getData();
                        FileOutputStream stream = context.openFileOutput(trimNameForImage(filenameTokens[filenameTokens.length - 1]), Context.MODE_PRIVATE);
                        stream.write(data);
                        stream.close();

                    }

                }
            } catch (ParseException e1) {

                e1.printStackTrace();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    void writing_profilepic_external_Sdcard(ParseFile file_this) {
        if (file_this != null) {
            ParseFile file = (ParseFile) file_this;

            if (file.getName() != null) {
                String[] filenameTokens = file.getName().split("-");
                try {
                    byte[] data = file.getData();
                    File f = new File(Environment.getExternalStorageDirectory()
                            + File.separator + trimNameForImage(filenameTokens[filenameTokens.length - 1]));
                    f.createNewFile();
//			write the bytes in file
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(data);
                    fo.close();

                } catch (ParseException e1) {

                    e1.printStackTrace();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

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

    //    public void tryUpdateVersion(float av, float dv){
//        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//        if(settings.getFloat("AppVersion", 0) <= 0 && settings.getFloat("DataVersion", 0) <= 0){
//            currentAppVersion = av;
//            currentDataVersion = dv;
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putFloat("AppVersion", av);
//            editor.putFloat("DataVersion", dv);
//            editor.commit();
//        }
//
//    }
//    public void checkDataVersion() {
//
//        if(CheckIsConnectedToInternet(context)){
//            System.out.println("Andy check data version ");
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//            // query.setLimit(1);
//            List<ParseObject> result;
//            try {
//                result = query.find();
//                if (result.size() > 0 && result!=null) {
//                    ParseObject obj = result.get(0);
//                    float dataVersion = obj.getNumber("DataVersion").floatValue();
//                    try {
//                        SharedPreferences settings = context.getSharedPreferences(
//                                context.getPackageName(), 0);
//                        if (settings.contains("DataVersion")) {
//                            currentDataVersion = settings.getFloat("DataVersion", 1.0f);
//
//                            if (dataVersion > currentDataVersion) {
//                                askDownload(dataVersion, context);
////							downloadData(context);
//                                currentDataVersion = dataVersion;
//                                System.out.println("Andy download cheking ");
//                            } else {
//                                System.out.println("Andy load cheking ");
//                                sharedDataManager.loadData();
//                            }
//                        } else {
//                            System.out.println("Andy download cheking 1");
////						downloadData(context);
//                            askDownload(dataVersion, context);
//                            currentDataVersion = dataVersion;
//
//                        }
//                    } catch (Exception exception) {
//                        System.out.println("Andy download cheking 2");
////					downloadData(context);
//
//
//                        askDownload(dataVersion, context);
//                        currentDataVersion = dataVersion;
//
//                    } finally {
//
//                    }
//
//                } else {
//                    sharedDataManager.loadData();
//
//                }
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                System.out.println("Andy load cheking 1"+e);
//                e.printStackTrace();
//            }
//        }
//        else {
//            sharedDataManager.loadData();
//
////				ParseDataManager.sharedDataManager(context).showAlert(context, "Alert", "You are not connected to internet. Please try again when internet is available.");
//
//        }
//    }
//
//    public void checkDataVersion_refresh() {
//
//        if(CheckIsConnectedToInternet(context)){
//            System.out.println("Andy check data version ");
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
//            // query.setLimit(1);
//            List<ParseObject> result;
//            try {
//                result = query.find();
//                if (result.size() > 0 && result!=null) {
//                    ParseObject obj = result.get(0);
//                    float dataVersion = obj.getNumber("DataVersion").floatValue();
//                    try {
//                        SharedPreferences settings = context.getSharedPreferences(
//                                context.getPackageName(), 0);
//                        if (settings.contains("DataVersion")) {
//                            currentDataVersion = settings
//                                    .getFloat("DataVersion", 1.0f);
//
//                            if (dataVersion > currentDataVersion) {
//                                askDownload(dataVersion, context);
////							downloadData(context);
//                                currentDataVersion = dataVersion;
//                                System.out.println("Andy download cheking ");
//                            } else {
//
//                                Builder alertDialog2 = new Builder(context);
//                                alertDialog2.setTitle("App is up to date");
//                                alertDialog2.setMessage("There is no new app data available at this time");
//                                alertDialog2.setPositiveButton("OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                            }
//                                        });
//
//                                alertDialog2.show();
//                                sharedDataManager.loadData();
//                            }
//                        } else {
//                            System.out.println("Andy download cheking 1");
////						downloadData(context);
//                            askDownload(dataVersion, context);
//                            currentDataVersion = dataVersion;
//
//                        }
//                    } catch (Exception exception) {
//                        System.out.println("Andy download cheking 2");
////					downloadData(context);
//                        askDownload(dataVersion, context);
//                        currentDataVersion = dataVersion;
//
//                    } finally {
//
//                    }
//
//                } else {
//                    sharedDataManager.loadData();
//
//                }
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                System.out.println("Andy load cheking 1"+e);
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else
//        {
//            sharedDataManager.loadData();
//
//            ParseDataManager.sharedDataManager(context).showAlert(context, "No Internet", "You internet connection seems to be offline. Please check & try again.");
//
//        }
//    }
    public static void showAlert(Context context, String title, String message) {
        new Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

//    public void askDownload(final float dataVersion, final Context ctx){
//
//
//
//        new Builder(ctx)
//                .setIcon(R.drawable.icon_50)
//                .setTitle("Download Update")
//                .setMessage("Version "+dataVersion+" is available.\nDownload now?")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        downloadData(ctx);
//                        currentDataVersion=dataVersion;
//                    }
//
//                })
//                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        final Builder builder = new Builder(ctx);
//                        builder.setCancelable(true);
//                        builder.setTitle("Download Later").setMessage("You can refresh the app content at any time from settings menu").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                                dialog.cancel();
//                            }
//                        });
//
//
//                        ((Activity) ctx).runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                builder.show();
//
//                            }
//                        });
//
//                    }
//                })
//                .show();
//    }

    public void downloadData(Context ctx){


        sharedDataManager.downloadInformationDataCharacter();
        downloadUsersData();
//		new AsyncDownloadData(sharedDataManager).execute();
    }


    public void loadData() {


        Intent intent = new Intent();
        intent.setAction("com.populace.nebraskapassport.loadcompleted");
        context.sendBroadcast(intent);
        sharedDataManager.loadAcademics();

    }


    public void downloadInformationDataCharacter() {
        berryProgress = ParseDataManager.sharedDataManager(context).showProgress(ac.context, "character", "Downloading....");

        final BerrySession bs = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("character");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs.putString(TAG_CHAR_DEFINITION, arg0.get(0).getString(TAG_CHAR_DEFINITION));
                        bs.putString(TAG_CHAR_BCV, arg0.get(0).getString(TAG_CHAR_BCV));
                        bs.putString(TAG_CHAR_QOUTE, arg0.get(0).getString(TAG_CHAR_QOUTE));

                        downloadInformationDataAcademic();
                    }

                } else {
                    // handle Parse Exception here

                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    public void downloadBannerImages() {
        System.out.println("Banner Images Download : ");
        final BerrySession bs = new BerrySession(context);
      //  ParseQuery<ParseObject> query = ParseQuery.getQuery("Banner_images");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Banner_images");
        query.setLimit(100);
        query.orderByAscending("section_order");
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseObject obj : objects) {

                        try {
                            ParseObject.pinAll(objects);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }


                    }


                } else {
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();
                    System.out.println("exception : " + e);
                }
            }
        });


    }


    public void fetchBannerImages() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Banner_images");
        query.fromLocalDatastore();
        query.orderByAscending("section_order");
        try {
            images = query.find();
           System.out.println("Parse size of Images " + images.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void downloadInformationDataAcademic() {
        final BerrySession bs_academics = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("academic");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_academics.putString(TAG_ACAD_CLUB, arg0.get(0).getString(TAG_ACAD_CLUB));
                        bs_academics.putString(TAG_ACAD_LIBRARY, arg0.get(0).getString(TAG_ACAD_LIBRARY));
                        bs_academics.putString(TAG_ACAD_EVENT_CAL, arg0.get(0).getString(TAG_ACAD_EVENT_CAL));
                        bs_academics.putString(TAG_ACAD_TUTORING, arg0.get(0).getString(TAG_ACAD_TUTORING));
                        berryProgress.setTitle("academic");
                        downloadInformationDataNutrition();
                    }

                } else {
                    // handle Parse Exception here

                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadInformationDataNutrition() {
        final BerrySession bs_nutrition = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("nutrition");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_nutrition.putString(TAG_NUTRITION_DIETS, arg0.get(0).getString(TAG_NUTRITION_DIETS));
                        bs_nutrition.putString(TAG_NUTRITION_MENU, arg0.get(0).getString(TAG_NUTRITION_MENU));
                        bs_nutrition.putString(TAG_NUTRITION_CAL_COUNT, arg0.get(0).getString(TAG_NUTRITION_CAL_COUNT));
                        bs_nutrition.putString(TAG_NUTRITION_MY_DIET, arg0.get(0).getString(TAG_NUTRITION_MY_DIET));
                        berryProgress.setTitle("nutrition");
                        downloadInformationDataPhysical();
                    }

                } else {
                    // handle Parse Exception here
                    System.out.println("Exeption..." + e);
                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadInformationDataPhysical() {
        final BerrySession bs_physical = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("physical");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_physical.putString(TAG__PHYSICAL_DAILY_WORKOUT, arg0.get(0).getString(TAG__PHYSICAL_DAILY_WORKOUT));
                        bs_physical.putString(TAG__PHYSICAL_ACTIVITIES, arg0.get(0).getString(TAG__PHYSICAL_ACTIVITIES));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_SCHEDULE, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_SCHEDULE));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_REGISTER, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_REGISTER));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_ON_BIKING, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_ON_BIKING));
                        bs_physical.putString(TAG__PHYSICAL_THE_CAGE, arg0.get(0).getString(TAG__PHYSICAL_THE_CAGE));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_OFF_HIKING, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_OFF_HIKING));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_OFF_RACE, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_OFF_RACE));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_OFF_RIVER_RENTALS, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_OFF_RIVER_RENTALS));
                        bs_physical.putString(TAG__PHYSICAL_INTRAMURAL_ON_TRAIL_MAP, arg0.get(0).getString(TAG__PHYSICAL_INTRAMURAL_ON_TRAIL_MAP));
                        berryProgress.setTitle("physical");
                        downloadInformationEmotion();
                    }

                } else {
                    // handle Parse Exception here

                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadInformationEmotion() {
        final BerrySession bs_emotion = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("emotion");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_emotion.putString(TAG_EMOTION_DEFINITION, arg0.get(0).getString(TAG_EMOTION_DEFINITION));
                        bs_emotion.putString(TAG_EMOTION_OFF_CAMPUS_RESOURCES, arg0.get(0).getString(TAG_EMOTION_OFF_CAMPUS_RESOURCES));
                        bs_emotion.putString(TAG_EMOTION_ON_CAMPUS_RESOURCES, arg0.get(0).getString(TAG_EMOTION_ON_CAMPUS_RESOURCES));

                        berryProgress.setTitle("emotion");
                        System.out.println("Exeption...");
                        downloadInformationSpirtuality();
                    }

                } else {
                    // handle Parse Exception here
                    System.out.println("Exeption..." + e);
                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadInformationSpirtuality() {
        final BerrySession bs_spirtual = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("spirituality");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_spirtual.putString(TAG_SPIR_CHAMPLAIN_OFFICES, arg0.get(0).getString(TAG_SPIR_CHAMPLAIN_OFFICES));
                        bs_spirtual.putString(TAG_SPIR_GROUPS, arg0.get(0).getString(TAG_SPIR_GROUPS));
                        bs_spirtual.putString(TAG_SPIR_RELIGIOUS_EVENTS, arg0.get(0).getString(TAG_SPIR_RELIGIOUS_EVENTS));
                        bs_spirtual.putString(TAG_SPIR_SPIRTUALITY, arg0.get(0).getString(TAG_SPIR_SPIRTUALITY));
                        berryProgress.setTitle("spirituality");
                        downloadInformationSocial();
                    }

                } else {
                    // handle Parse Exception here

                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadInformationSocial() {
        final BerrySession bs_social = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("social");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_social.putString(TAG_SOCIAL_KCAB, arg0.get(0).getString(TAG_SOCIAL_KCAB));
                        bs_social.putString(TAG_SOCIAL_SGA, arg0.get(0).getString(TAG_SOCIAL_SGA));
                        bs_social.putString(TAG_SOCIAL_SPORTS, arg0.get(0).getString(TAG_SOCIAL_SPORTS));
                        bs_social.putString(TAG_SOCIAL_STD_ACT, arg0.get(0).getString(TAG_SOCIAL_STD_ACT));
                        berryProgress.setTitle("social");
                        downloadLinks();
                        //     downloadCompleted();
                    }

                } else {
                    // handle Parse Exception here

                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void downloadLinks() {
        final BerrySession bs_links = new BerrySession(context);
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Links");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                    if (arg0.size() > 0) {
                        bs_links.putString(TAG_LINK_FACEBOOK, arg0.get(0).getString(TAG_LINK_FACEBOOK));
                        bs_links.putString(TAG_LINK_TWITTER, arg0.get(0).getString(TAG_LINK_TWITTER));
                        bs_links.putString(TAG_LINK_INSTAGRAM, arg0.get(0).getString(TAG_LINK_INSTAGRAM));
                        bs_links.putString(TAG_LINK_YOU_TUBE, arg0.get(0).getString(TAG_LINK_YOU_TUBE));
                        bs_links.putString(TAG_LINK_PINTEREST, arg0.get(0).getString(TAG_LINK_PINTEREST));
                        berryProgress.setTitle("Links");
                      //  downloadBannerImages();
                        boolean mboolean = false;
                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                        mboolean = settings.getBoolean("FIRST_RUN", false);
                        if (!mboolean)
                        {
                            settings = context.getSharedPreferences(context.getPackageName(), 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("FIRST_RUN", true);
                            editor.commit();
                            berryProgress.dismiss();
                        } else {
                            downloadCompleted();
                        }

                    }

                } else {
                    // handle Parse Exception here
                    System.out.println("Exeption..." + e);
                    berryProgress.dismiss();
                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    public void downloadUsersData() {

        ParseQuery<GTCUser> query = new ParseQuery<GTCUser>(GTCUser.class);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<GTCUser>() {
            public void done(List<GTCUser> arg0, ParseException e) {
                if (e == null) {
                    // your logic here
                  /*  System.out.println("Check"+e);
                    downloadInformationDataCharacter();
                    berryProgress.dismiss();*/
                    if (arg0.size() > 0) {
                        users = (ArrayList<GTCUser>) arg0;
                    }

                } else {
                    // handle Parse Exception here

                    Toast.makeText(context, "Error Occured While Downloading",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void downloadCompleted() {

        berryProgress.dismiss();

        SharedPreferences settings = context.getSharedPreferences(
                context.getPackageName(), 0);
        System.gc();
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("DataVersion", currentDataVersion);
        editor.putFloat("AppVersion", currentAppVersion);
        editor.commit();
        sharedDataManager.loadData();
        sendMessage();

    }
    private void sendMessage() {

        Intent intent = new Intent("downloadcompleted");
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("com.populace.berrycollege.ConnectivityIntent")){

                boolean isConnected = intent.getBooleanExtra("isConnected", false);
                sharedDataManager.isConnected = isConnected;
                if(isConnected){
                    if(sharedDataManager.isParseInitializationPending){
                        sharedDataManager.initializeParse();
                    }
                    sharedDataManager.syncCheckins();

                }else{

                }
            }



        }

    }


    private class SaveData extends AsyncTask<String, Void, String> {

        ArrayList<ParseFile> image_values;
        String city_name;


        public SaveData(ArrayList<ParseFile> image_values, String city_name) {
            super();
            this.image_values = image_values;
            this.city_name = city_name;
        }

        @Override
        protected String doInBackground(String... params) {
            // your background code here. Don't touch any UI components
            for (int i = 0; i < image_values.size(); i++) {
                writing_profilepic_external(image_values.get(i));

//				writing_profilepic_external_Sdcard(image_values.get(i));
            }


            return city_name;
        }

        protected void onPostExecute(String result) {
            //This is run on the UI thread so you can do as you wish here
        }
    }

    public static void showAlertSimple(Context ac, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ac).create();
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.icon_50);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void noInternet(Context ctx) {
        Toast.makeText(ctx, "Sorry, Your device is not connected to internet",
                Toast.LENGTH_SHORT).show();

    }

    public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        Bitmap bmp = null;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bmp = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bmp = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bmp = rotateImage(bitmap, 270);
                    break;
                default: {
                    bmp = bitmap;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
            if (bitmap != source) {
                source.recycle();
            }
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap flipImage(Bitmap source) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
            if (bitmap != source) {
                source.recycle();
            }
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }


    public final static int PIC_CROP = 2;
    public final static int TAKE_PICTURE = 1;

    private static final int REQ_CODE_PICK_IMAGE = 100;

    public static void photoFunctionalityLaunch(final Activity context, final Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an option");
        builder.setItems(new CharSequence[]{"Choose a picture", "Take a photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    context.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
	        	/*
	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	        	photoPickerIntent.setType("image/*");
	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/
                } else {
                    Intent intent = new Intent(context, GTCCameraActivity.class);//"android.media.action.IMAGE_CAPTURE");//
                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    context.startActivityForResult(intent, TAKE_PICTURE);
                }
            }

        });
        builder.show();
    }

    public static void photoFunctionalityLaunch(final Fragment fragment, final Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Choose");
        builder.setItems(new CharSequence[]{"Choose a picutre", "Take a photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    fragment.startActivityForResult(i, REQ_CODE_PICK_IMAGE);
	        	/*
	        	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	        	photoPickerIntent.setType("image/*");
	        	((Activity) context).startActivityForResult(photoPickerIntent, REQ_CODE_PICK_IMAGE);*/

                } else {
                    Intent intent = new Intent(fragment.getActivity(), GTCCameraActivity.class); //MediaStore.ACTION_IMAGE_CAPTURE);//
                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    fragment.startActivityForResult(intent, TAKE_PICTURE);
                }
            }

        });
        builder.show();
    }

    public static Bitmap handleBitmap(Bitmap bitmap, Uri imageUri) {
        Uri selectedImage = imageUri;
        try {

            bitmap = ParseDataManager.imageOreintationValidator(bitmap, selectedImage.toString());
            FileOutputStream out = new FileOutputStream(imageUri.toString());
            bitmap.compress(CompressFormat.JPEG, 9, out);


        } catch (Exception e) {
            Log.e("Camera", e.toString());
        }
        return bitmap;
    }

    public static Object handlePhotoFunctionalityResult(Context ctx, int requestCode, int resultCode, Intent data, Uri imageUri) {
        Bitmap bitmap = null;
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    ctx.getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = ctx.getContentResolver();
                    try {

                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        selectedImage = imageUri;

                    } catch (Exception e) {
                        Log.e("Camera", e.toString());
                    }
                    photoCropFunctionality(ctx, selectedImage);
                    return selectedImage;
                }
                break;
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = ctx.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = BitmapFactory.decodeFile(picturePath);
                    bitmap = ParseDataManager.imageOreintationValidator(bitmap, picturePath);
                    try {
                        URI uri = new URI(imageUri.toString());
                        FileOutputStream out = new FileOutputStream(new File(uri));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        selectedImage = imageUri;
                        //bitmap = GTCDataManager.handleBitmap(bitmap, selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    photoCropFunctionality(ctx, imageUri);
                    return selectedImage;
                }
                break;
            case Crop.REQUEST_CROP:
                Uri selectedImage = imageUri;
                ctx.getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = ctx.getContentResolver();
                try {

                    bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, selectedImage);
                    return bitmap;

                } catch (Exception e) {
                    Log.e("Camera", e.toString());
                }
        }
        return bitmap;

    }

    public static Object handlePhotoFunctionalityResult(Fragment fragment, int requestCode, int resultCode, Intent data, Uri imageUri) {
        Bitmap bitmap = null;
        Context context = fragment.getActivity();
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    context.getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = context.getContentResolver();
                    try {

                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                        selectedImage = imageUri;

                    } catch (Exception e) {
                        Log.e("Camera", e.toString());
                    }
                    photoCropFunctionality(fragment, selectedImage);
                    return selectedImage;
                }
                break;
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = BitmapFactory.decodeFile(picturePath);
                    bitmap = ParseDataManager.imageOreintationValidator(bitmap, picturePath);
                    try {
                        URI uri = new URI(imageUri.toString());
                        FileOutputStream out = new FileOutputStream(new File(uri));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        selectedImage = imageUri;
                        bitmap = ParseDataManager.handleBitmap(bitmap, selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    photoCropFunctionality(fragment, imageUri);
                    return selectedImage;
                }
                break;
            case Crop.REQUEST_CROP:
                Uri selectedImage = Crop.getOutput(data);
                context.getContentResolver().notifyChange(selectedImage, null);
                ContentResolver cr = context.getContentResolver();
                try {

                    bitmap = android.provider.MediaStore.Images.Media
                            .getBitmap(cr, selectedImage);


                } catch (Exception e) {
                    Log.e("Camera", e.toString());
                }
        }
        return bitmap;
    }

    public static void photoCropFunctionality(Fragment fragment, Uri picUri) {

        new Crop(picUri).output(picUri).asSquare().start(fragment.getActivity(), fragment);
    }

    public static void photoCropFunctionality(Context context, Uri picUri) {
        new Crop(picUri).output(picUri).asSquare().start((Activity) context);
    }


//    public void login(String email, final ILoginCallback callback) {
//        ParseUser.logInInBackground(email, "123456", new LogInCallback() {
//            @Override
//            public void done(final ParseUser user, final com.parse.ParseException e) {
//                if (user != null) {
//                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                    final SharedPreferences.Editor editor = settings.edit();
//                    editor.putBoolean("IsLoggedIn", true);
//                    editor.putString("Email", user.getEmail());
//                    editor.putString("UserName", user.getUsername());
//
//                    try {
//                        GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                        editor.commit();
//                        uprof.fetchInBackground(new GetCallback<GTCUser>() {
//
//                            @Override
//                            public void done(GTCUser uprof,
//                                             com.parse.ParseException arg1) {
//                                if (uprof != null) {
//                                    editor.putString("Year", uprof.getStudyYear());
//                                    editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
//                                    editor.commit();
//                                    callback.onLogin(true, e, user);
//                                } else {
//                                    if (e != null) {
//
//                                    } else {
//                                        arg1 = new com.parse.ParseException(0, "Profile is null");
//                                    }
//                                    callback.onLogin(false, e, user);
//                                }
//                            }
//
//                        });
//                    } catch (Exception e1) {
//                        callback.onLogin(false, e, user);
//                    }
//
//                } else {
//                    callback.onLogin((user == null) ? false : true, e, user);
//                }
//
//
//            }
//
//        });
//    }

    public void login(String email, final String password, final ILoginCallback callback) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(final ParseUser user, final com.parse.ParseException e) {


                if (user != null) {
                    final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                    final SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("IsLoggedIn", true);
                    editor.putString("Email", user.getEmail());
                    editor.putString("UserName", user.getUsername());
                    editor.putString("Password", user.getString("pwd"));
                    editor.commit();
                    try {
                        GTCUser uprof = (GTCUser) user.getParseObject("profile");
                        editor.commit();
                        uprof.fetchInBackground(new GetCallback<GTCUser>() {

                            @Override
                            public void done(GTCUser uprof,
                                             com.parse.ParseException arg1) {
                                if (uprof != null) {
                                    writing_profilepic_external(uprof.getImage(), uprof.getFirstName() + "_" + uprof.getLastName());
                                    editor.putString("Year", uprof.getStudyYear());
                                    editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
                                    editor.putString("FbFirstName", uprof.getFirstName());
                                    editor.putString("FbLastName", uprof.getLastName());
                                    editor.putString("Fbemail", uprof.getUserEmail());
                                    editor.putString("FbUserId", "");
                                    editor.commit();
                                    String frstName = settings.getString("FbFirstName", "na");
                                    Log.d("FirstName", frstName);
                                    String lstName = settings.getString("FbLastName", "na");
                                    Log.d("LastName", lstName);
                                    callback.onLogin(true, e, user);
                                } else {
                                    if (e != null) {
                                    } else {
                                        arg1 = new com.parse.ParseException(0, "Profile is null");
                                    }
                                    System.out.println("Andy checking data wrong " + arg1);
                                    callback.onLogin(false, e, user);
                                }
                            }

                        });
                    } catch (Exception e1) {
                        System.out.println("Andy checking data wrong " + e1);
                        callback.onLogin(false, e, user);
                    }

                } else {
                    callback.onLogin((user == null) ? false : true, e, user);
                }


            }

        });
    }

    void writing_profilepic_external(ParseFile file_this, String image_name) {
        if (file_this != null) {
            ParseFile file = (ParseFile) file_this;

            try {
                if (file.getName() != null) {
                    String[] filenameTokens = file.getName().split("-");
                    System.out.println("Andy files check this " + image_name);

                    String fullPath = "/data/data/" + context.getPackageName() + "/files/" + image_name;

                    File file_check = new File(fullPath);


                    System.out.println("Andy data parse false");
                    byte[] data;
                    try {
                        data = file.getData();
                        FileOutputStream stream = context.openFileOutput(image_name, Context.MODE_PRIVATE);
                        stream.write(data);
                        stream.close();
                    } catch (com.parse.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    public void register(final String email, final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback) {

        final ParseUser user = ParseUser.getCurrentUser();
//	user.setEmail(email);
        user.setUsername(email);
        user.setPassword("123456");
        user.saveInBackground(new SaveCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
                    uprof.setUserEmail(email);
                    uprof.setUsername(email);
                    uprof.setFirstName(firstname);
                    uprof.setLastName(lastname);
                    if (!year.isEmpty())
                        uprof.setStudyYear(year);
                    user.put("profile", uprof);
                    user.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("IsLoggedIn", true);
                                editor.putString("Email", user.getEmail());
                                editor.putString("UserName", user.getUsername());
                                GTCUser uprof = (GTCUser) user.getParseObject("profile");
                                editor.putString("Year", uprof.getStudyYear());
                                editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
                                editor.commit();
                                callback.onRegister(true, e, user);
                                uprof.setUser(user);
                                uprof.setImage(image);
                                uprof.setThumbnail(thumb);
                                uprof.saveInBackground();
                            } else {
                                callback.onRegister(false, e, null);
                            }

                        }

                    });
                } else {

                    callback.onRegister(false, e, null);
                }

            }

        });

    }

    public void register(final String email, final String password, final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback) {

        final ParseUser user = new ParseUser();


        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);
        user.put("firstname", firstname);
        user.put("lastname", lastname);
        user.put("user_image", image);
        user.put("pwd", password);
        user.put("program_year", year);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    try {
                        ParseUser.logIn(email, password);
                        final ParseUser user1 = ParseUser.getCurrentUser();
                        final GTCUser uprof = (GTCUser) GTCUser.create(GTCUser.class);
                        uprof.setUserEmail(email);
                        uprof.setUsername(email);
                        uprof.setFirstName(firstname);
                        uprof.setLastName(lastname);
                        if (!year.isEmpty())
                            uprof.setStudyYear(year);
                        user1.put("profile", uprof);
                        user1.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {

                                System.out.println("Andy checking data wrong " + e);
                                if (e == null) {
                                    SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                                    SharedPreferences.Editor editor = settings.edit();
//					    editor.putBoolean("IsLoggedIn", true);
//					    editor.putString("Email", user.getEmail());
//					    editor.putString("UserName", user.getUsername());
                                    GTCUser uprof = (GTCUser) user1.getParseObject("profile");
                                    editor.putString("Year", uprof.getStudyYear());
                                    editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
//                                    editor.putString("fName", uprof.getFirstName());
//                                    editor.putString("lName", uprof.getLastName());
//                                    editor.putString("uEmail", uprof.getUserEmail());

                                    editor.commit();
                                    System.out.println("Andy checking data wrong " + e);
                                    callback.onRegister(true, e, user1);
                                    uprof.setUser(user1);
                                    uprof.setImage(image);
                                    uprof.setThumbnail(thumb);
                                    uprof.saveInBackground();
                                } else {
                                    System.out.println("Andy checking data wrong " + e);
                                    callback.onRegister(false, e, null);
                                }

                            }

                        });
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    callback.onRegister(false, e, null);
                }
                // Hooray! Let them use the app now.
            }


        });


    }

    public void update_nikhil(final String email, final String password, final String firstname, final String lastname, final String year, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback) {
        //        GTCUser uprof = sharedDataManager.getCurrentProfile();


        final ParseUser user = ParseUser.getCurrentUser();
        //        final ParseUser user = new ParseUser();

        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);
        user.put("firstname", firstname);
        user.put("lastname", lastname);

        if (image != null) {
            user.put("user_image", image);
        }
        user.put("pwd", password);
        user.put("program_year", year);


        try {
            profileObj = ((ParseObject) user.get("profile")).fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        profileObj.put("FirstName", firstname);
        profileObj.put("LastName", lastname);
        profileObj.put("study_year", year);
        profileObj.put("user_email", email);
        profileObj.put("username", email);
        profileObj.put("image", image);
        profileObj.put("thumbnail", thumb);
        profileObj.saveInBackground();

        // final ParseObject finalProfileObj = profileObj;
        profileObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    {
                        final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                        final SharedPreferences.Editor editor = settings.edit();

                        editor.putBoolean("IsLoggedIn", true);
                        editor.putString("Email", user.getEmail());
                        editor.putString("UserName", user.getUsername());
                        editor.putString("Password", user.getString("pwd"));
                        editor.putString("firstNmae",profileObj.getString("FirstName"));
                        editor.putString("lastName",profileObj.getString("LastName"));
                        editor.putString("userEmail",profileObj.getString("user_email"));
                        editor.putString("studyYear",profileObj.getString("study_year"));
                        editor.commit();
                        try {
                            final ParseUser user = ParseUser.getCurrentUser();
                            user.put("profile", profileObj);

                            callback.onRegister(true, e, user);


                        } catch (Exception es) {

                            System.out.println("Andy checking data wrong update " + es);
                        }


                    }
                } else {
                    callback.onRegister(false, e, null);
                }

            }


        });


        //user.put("profile", profile);

//        user.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                {
//                    if (e == null) {
//                        {
//                            final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                            final SharedPreferences.Editor editor = settings.edit();
//
//                            editor.putBoolean("IsLoggedIn", true);
//                            editor.putString("Email", user.getEmail());
//                            editor.putString("UserName", user.getUsername());
//                            editor.putString("Password", user.getString("pwd"));
//                            editor.commit();
//
//                            try {
//                       //   final   GTCUser uprof = (GTCUser) user.getParseObject("User_Profile");
//                                final GTCUser uprof = sharedDataManager.getCurrentProfile();
//
//                                uprof.setUserEmail(email);
//                                uprof.setUsername(email);
//                                uprof.setFirstName(firstname);
//                                uprof.setLastName(lastname);
//                                if (!year.isEmpty())
//                                    uprof.setStudyYear(year);
////    uprof.setUser(user);
////    if(image!=null)
////    {
////        uprof.setImage(image);
////        uprof.setThumbnail(thumb);
////    }
//                                uprof.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(ParseException e)
//                                    { System.out.println("Andy checking data wrong updatedata in user Profile " + e);
//                                        if (e == null) {
//                                            SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//                                            SharedPreferences.Editor editor = settings.edit();
////					    editor.putBoolean("IsLoggedIn", true);
////					    editor.putString("Email", user.getEmail());
////					    editor.putString("UserName", user.getUsername());
////                                        GTCUser uprof = (GTCUser) user.getParseObject("profile");
//                                            editor.putString("Year", uprof.getStudyYear());
//                                            editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
//
//                                            if (image != null) {
//
////                    uprof.setUser(user);
//                                                uprof.setImage(image);
//                                                uprof.setThumbnail(thumb);
//                                                uprof.saveInBackground();
//                                            }
//                                            writing_profilepic_external(thumb, uprof.getFirstName() + "_" + uprof.getLastName());
//                                            editor.putString("Year", uprof.getStudyYear());
//                                            editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
//                                            editor.putString("FbFirstName", uprof.getFirstName());
//                                            editor.putString("FbLastName", uprof.getLastName());
//                                            editor.putString("Fbemail", uprof.getUserEmail());
//                                            editor.putString("FbUserId", "");
//                                            editor.commit();
//                                            callback.onRegister(true, e, user);
//
//                                        }
//                                    }
//                                });
//
//                            } catch (Exception es) {
//
//                                System.out.println("Andy checking data wrong update " + es);
//                            }
//
//
//                        }
//                    } else {
//                        callback.onRegister(false, e, null);
//                    }
//
//                }
//            }
        }


    public ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }

    public GTCUser getCurrentProfile() throws ParseException {
        GTCUser user = null;
        if (CheckIsConnectedToInternet(context))
        {
         //   ParseUser usr =  ParseUser.getCurrentUser();
          //  ParseObject profileObj = ((ParseObject) usr.get("profile")).fetchIfNeeded();
          //  System.out.println("user Profile: "+profileObj);

            ((ParseObject) getCurrentUser().getParseObject("profile")).fetchIfNeededInBackground(new GetCallback<GTCUser>() {

                @Override
                public void done(GTCUser uprof, com.parse.ParseException arg1) {
                    if (uprof != null) {
                        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                        SharedPreferences.Editor editor = settings.edit();
                        //    editor.putString("Year", uprof.getStudyYear());
                        editor.putString("PROFILE", uprof.fields(context, true, 0).toString());
                        editor.commit();
                    }
                        }

                    });

        }
        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
        user = GTCUser.create(GTCUser.class);
        try {
            user.load(new JSONObject(settings.getString("PROFILE", "{}")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public void update(final String email, final String firstname, final String lastname, final String year, final String phone, final String organization, final String facebook, final String twitter, String linkedin, final ParseFile image, final ParseFile thumb, final IRegisterCallback callback) {
//        GTCUser uprof = sharedDataManager.getCurrentProfile();
//        uprof.setUserEmail(email);
//        uprof.setFirstName(firstname);
//        uprof.setLastName(lastname);
//        if (!year.isEmpty())
//            uprof.setStudyYear(year);
//        uprof.setPhone(phone);
//        uprof.setOrganization(organization);
//        uprof.setTwitter(twitter);
//        uprof.setFacebook(facebook);
//        uprof.setLinkedin(linkedin);
//        if (image != null) {
//            uprof.setImage(image);
//        }
//        if (thumb != null) {
//            uprof.setThumbnail(thumb);
//        }
//        uprof.saveInBackground(new SaveCallback() {
//
//            @Override
//            public void done(com.parse.ParseException e) {
//                if (e == null) {
//                    callback.onRegister(true, e, sharedDataManager.getCurrentUser());
//                } else {
//                    callback.onRegister(false, e, sharedDataManager.getCurrentUser());
//                }
//
//            }
//
//        });
    }

    public List<?> load(String filename, Class c) {
        ArrayList<GTCObject> list = new ArrayList<GTCObject>();
        try {

            FileInputStream in = context.openFileInput(filename);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            String json = new String(buffer, "UTF-8");
            JSONArray items = new JSONArray(json);
            for (int i = 0; i < items.length(); i++) {


                try {
                    File root = new File(Environment.getExternalStorageDirectory(), "Notes_BerryCollegeApp");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, filename);
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(json);
                    writer.flush();
                    writer.close();
                    System.out.println("Andy sponser data done" + Environment.getExternalStorageDirectory());

                } catch (IOException e) {
                    e.printStackTrace();

                }
                try {


                    JSONObject object = items.getJSONObject(i);
                    GTCObject obj = (GTCObject) c.getConstructors()[0].newInstance();
                    obj.load(object);
                    list.add(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void loadAcademics() {
        try {
            academics = (ArrayList<Academic>) load("academic.json", Academic.class);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    ProgressDialog checkVersionProgress;
    public void checkDataVersion(final boolean withProgress, final Context ctx){
        if(CheckIsConnectedToInternet(ctx)){

            if(withProgress){
                checkVersionProgress = ParseDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");

            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
            //query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>(){

                @Override
                public void done(List<ParseObject> result,
                                 com.parse.ParseException arg1) {
                    if(withProgress){
                        checkVersionProgress.dismiss();
                    }
                    if(result != null){
                        if(result.size() > 0){
                            ParseObject  obj =  result.get(0);
                            float appVersion = obj.getNumber("AppVersion").floatValue();
                            float dataVersion = obj.getNumber("DataVersion").floatValue();

                            try {
                                String email_feedback= obj.getString("dialog_text");
                                String eula_string= obj.getString("eula");
                                String privacy_policy=obj.getString("privacy_policy");
                                SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
                                SharedPreferences.Editor editor = settings_new.edit();
                                editor.putString("dialog_text", email_feedback);
                                editor.putString("eula_text", eula_string);
                                editor.putString("privacy_policy", privacy_policy);

                                editor.commit();
                            } catch (Exception e) {
                                // TODO: handle exception
                            }



                            try {
                                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                                currentAppVersion =settings.getFloat("AppVersion",1f);
                                currentDataVersion = settings.getFloat("DataVersion",1f);


                                if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
                                    askDownload(dataVersion, ctx);
                                    currentAppVersion =appVersion;
                                    currentDataVersion = dataVersion;

                                }else{
                                    sharedDataManager.loadData();
                                    currentAppVersion =appVersion;
                                    currentDataVersion = dataVersion;


                                }
                            }
                            catch (Exception exception) {
                                askDownload(dataVersion, ctx);
                                currentAppVersion =appVersion;
                                currentDataVersion = dataVersion;
                                System.out.println("andy checking data version "+exception);
                            }
                            finally {

                            }

                        }
                    }else{
                        sharedDataManager.loadData();

                    }
                }
            });




//				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
//				    query2.setLimit(1);
//						query2.findInBackground(new FindCallback<ParseObject>(){
//
//							@Override
//							public void done(List<ParseObject> result,
//									com.parse.ParseException arg1) {
//								if(result != null){
//									ParseObject  obj =  result.get(0);
//							        String defaultstatement = obj.getString("DefaultStatement");
//							        String newstatement = obj.getString("NewStatement");
//							        boolean showstatement = obj.getBoolean("showStatement");
//
//							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
//							        try {
//							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//							        	SharedPreferences.Editor editor = settings.edit();
//							            editor.putString("DefaultStatement", defaultstatement);
//							            editor.putString("NewStatement", newstatement);
//							            editor.putBoolean("showStatement", showstatement);
//							            editor.commit();
//							        }
//							        catch (Exception exception) {
//
//							        }
//
//								}else{
//
//								}
//							}
//						});

            try {
                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                latestDataVersion =settings.getFloat("LatestDataVersion",1f);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }else{
            try {
                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                latestDataVersion =settings.getFloat("LatestDataVersion",1f);

            } catch (Exception e) {
                // TODO: handle exception
            }
            if(withProgress){
                ParseDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
            }
        }
    }
    ProgressDialog checkVersionProgress1;
    public void checkDataVersion_refresh(final boolean withProgress, final Context ctx){
        if(CheckIsConnectedToInternet(ctx)){

            if(withProgress){
                checkVersionProgress1 = ParseDataManager.sharedDataManager(ctx).showProgress(ctx, "Version Check", "Checking server for newer data version.");
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Version");
            //query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>(){

                @Override
                public void done(List<ParseObject> result,
                                 com.parse.ParseException arg1) {
                    if(withProgress){
                        checkVersionProgress1.dismiss();
                    }
                    if(result != null){
                        if(result.size() > 0){
                            ParseObject  obj =  result.get(0);
                            float appVersion = obj.getNumber("AppVersion").floatValue();
                            float dataVersion = obj.getNumber("DataVersion").floatValue();

                            try {
                                String email_feedback= obj.getString("dialog_text");
                                String eula_string= obj.getString("eula");
                                String privacy_policy=obj.getString("privacy_policy");
                                SharedPreferences settings_new = context.getSharedPreferences(context.getPackageName(), 0);
                                SharedPreferences.Editor editor = settings_new.edit();
                                editor.putString("dialog_text", email_feedback);
                                editor.putString("eula_text", eula_string);
                                editor.putString("privacy_policy", privacy_policy);

                                editor.commit();
                            } catch (Exception e) {
                                // TODO: handle exception
                            }


                            try {
                                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                                currentAppVersion =settings.getFloat("AppVersion",1f);
                                currentDataVersion = settings.getFloat("DataVersion",1f);
                                System.out.println("andy checking data version "+currentAppVersion +" "+currentDataVersion);

                                if(appVersion == currentAppVersion && dataVersion > currentDataVersion){
                                    askDownload(dataVersion, ctx);
                                    currentAppVersion =appVersion;
                                    currentDataVersion = dataVersion;

                                }else{

                                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(ctx);
                                    alertDialog2.setTitle("App is up to date");
                                    alertDialog2.setMessage("There is no new app data available at this time");
                                    alertDialog2.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });

                                    alertDialog2.show();
                                    sharedDataManager.loadData();
                                    currentAppVersion =appVersion;
                                    currentDataVersion = dataVersion;


                                }
                            }
                            catch (Exception exception) {
                                askDownload(dataVersion, ctx);
                                currentAppVersion =appVersion;
                                currentDataVersion = dataVersion;

                            }
                            finally {

                            }

                        }
                    }else{
                        sharedDataManager.loadData();

                    }
                }
            });




//				    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Statement");
//				    query2.setLimit(1);
//						query2.findInBackground(new FindCallback<ParseObject>(){
//
//							@Override
//							public void done(List<ParseObject> result,
//									com.parse.ParseException arg1) {
//								if(result != null){
//									ParseObject  obj =  result.get(0);
//							        String defaultstatement = obj.getString("DefaultStatement");
//							        String newstatement = obj.getString("NewStatement");
//							        boolean showstatement = obj.getBoolean("showStatement");
//
//							        System.out.println("Andy data sp "+defaultstatement+"  "+newstatement+" "+showstatement);
//							        try {
//							        	SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
//							        	SharedPreferences.Editor editor = settings.edit();
//							            editor.putString("DefaultStatement", defaultstatement);
//							            editor.putString("NewStatement", newstatement);
//							            editor.putBoolean("showStatement", showstatement);
//							            editor.commit();
//							        }
//							        catch (Exception exception) {
//
//							        }
//
//								}else{
//
//								}
//							}
//						});

            try {
                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                latestDataVersion =settings.getFloat("LatestDataVersion",1f);

            } catch (Exception e) {
                // TODO: handle exception
            }

        }else{
            try {
                SharedPreferences settings = ctx.getSharedPreferences(context.getPackageName(), 0);
                latestDataVersion =settings.getFloat("LatestDataVersion",1f);

            } catch (Exception e) {
                // TODO: handle exception
            }
            if(withProgress){
                ParseDataManager.sharedDataManager(ctx).showAlert(ctx, "Alert", "Sorry, Your device is not connected to internet");
            }
        }
    }

    public void tryUpdateVersion(float av, float dv) {
        SharedPreferences settings = context.getSharedPreferences(
                context.getPackageName(), 0);
        if (settings.getFloat("AppVersion", 0) <= 0
                && settings.getFloat("DataVersion", 0) <= 0) {
            currentAppVersion = av;
            currentDataVersion = dv;
            SharedPreferences.Editor editor = settings.edit();
            editor.putFloat("AppVersion", av);
            editor.putFloat("DataVersion", dv);
            editor.commit();
        }

    }

    public void askDownload(float dataVersion, final Context ctx){

        new AlertDialog.Builder(ctx)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Download Update")
                .setMessage("Version "+dataVersion + " is available.\nDownload now?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadData(ctx);

                    }

                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Builder builder = new Builder(ctx);
                        builder.setCancelable(true);
                        builder.setTitle("Download Later").setMessage("You can refresh the app content at any time from settings menu").setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });


                        ((Activity) ctx).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                builder.show();

                            }
                        });

                    }
                })
                .show();
    }


    public class AsyncDownloadData extends AsyncTask<Void, CharSequence, Void> {
        DownloadCompleteCallback callback;
        ProgressDialog dialog;
        Context context;

        public AsyncDownloadData(DownloadCompleteCallback cb, Context ctx) {
            callback = cb;
            context = ctx;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            if (ParseDataManager.showsDownloadActivity) {
                dialog = new ProgressDialog(context);
                dialog.setTitle("Downloading");
                dialog.setMessage("Checking Data Version");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }
        }

    }



}

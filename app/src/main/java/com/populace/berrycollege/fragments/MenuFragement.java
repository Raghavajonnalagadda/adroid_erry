package com.populace.berrycollege.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.populace.berrycollege.R;
import com.populace.berrycollege.activities.GlobalClass;
import com.populace.berrycollege.activities.HealthAndWellness;
import com.populace.berrycollege.activities.LoginScreen;
import com.populace.berrycollege.activities.MainActivity;
import com.populace.berrycollege.managers.BerrySession;
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

public class MenuFragement extends Fragment implements OnItemSelectedListener,OnClickListener
{
    BerrySession bs;
    private Context context;
    Activity ac;
    private String year, passWord;
    EditText firstName, lastName, e_mail ,password;
    Button save, logOut;
    List<String> categories;
    ImageView user_image;
    Spinner spinner;
    Bitmap	logo_bmp ;
    TextView healthText,version_info;
    Fragment menu_fragement;
    LinearLayout refresh;
    ImageView fb,twitter,youTube,pintrest,instagram;
    static float currentDataVersion=1.0f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_fragment, container, false);
        bs=new BerrySession(this.getActivity());
        context = getActivity();
        menu_fragement=this;
        ac=getActivity();
        firstName = (EditText) v.findViewById(R.id.first_name);
        lastName = (EditText) v.findViewById(R.id.last_name);
        e_mail = (EditText) v.findViewById(R.id.e_mail);
        user_image = (ImageView) v.findViewById(R.id.ivuser_image);
        spinner = (Spinner) v.findViewById(R.id.spinner1);
        password = (EditText) v.findViewById(R.id.password);
        version_info= (TextView)v.findViewById(R.id.version_info);
        save = (Button) v.findViewById(R.id.save);
        healthText= (TextView)v.findViewById(R.id.drawer_health_text);
        fb= (ImageView)v.findViewById(R.id.ivfacebook);
        twitter= (ImageView)v.findViewById(R.id.ivtwitter);
        youTube= (ImageView)v.findViewById(R.id.ivyoutube);
        pintrest= (ImageView)v.findViewById(R.id.ivpinterest);
        instagram= (ImageView)v.findViewById(R.id.ivinstagram);
        logOut = (Button) v.findViewById(R.id.logout);
        refresh = (LinearLayout) v.findViewById(R.id.layout_refresh);
        loadVersion();
        //   version_info.setText("VER " + loadDataVersion());
        version_info.setText("VER "+currentDataVersion);


        user_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        categories = new ArrayList<String>();
        categories.add("2014");
        categories.add("2015");
        categories.add("2016");
        categories.add("2017");
        categories.add("2018");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        userDetail();

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                String email = e_mail.getText().toString();
                String year = spinner.getSelectedItem().toString();
                String passWord = password.getText().toString();
//                String image = user_image.g().toString();
                if (fname.length() == 0) {
                    showAlertSimple("Please enter first name");
                    return;
                } else if (lname.length() == 0) {
                    showAlertSimple("Please enter last name");
                    return;
                } else if (year == null) {
                    showAlertSimple("Please select Year");
                    return;
                } else if (email.length() == 0) {
                    showAlertSimple("Please enter email");
                    return;


                } else if (!emailValidator(email.trim())) {
                    showAlertSimple("Please enter a valid email address");
                    return;
                } else if (!CheckIsConnectedToInternet(context)) {
                    showAlertSimple(GlobalClass.internet_message);
                    return;
                } else {
                    //      ParseUser.getCurrentUser().logOut();

                    if (logo_bmp == null) {
                        signupUserWithaoutImage(fname, lname, email, passWord, year);
                    } else {
                        signupUser(fname, lname, email, passWord, year);
                    }

                }


            }
        });
        logOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                ac.finish();
                String email = settings.getString("Fbemail", null);
                if (email != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginScreen.class);
                    startActivity(intent);
                }

            }

        });
        healthText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HealthAndWellness.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ParseDataManager.sharedDataManager(getActivity()).checkDataVersion_refresh(true, getActivity());
                //  ParseDataManager.sharedDataManager(getActivity()).checkDataVersion(true, getActivity());

            }
        });
        InitializeListeners();
        return v;


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
                ParseDataManager.sharedDataManager(ac).update_nikhil(email.trim(), password, fname, lname, year, imagefile, thumbfile, new IRegisterCallback() {

                    @Override
                    public void onRegister(final boolean success, final ParseException e, ParseUser user) {
//				Crashlytics.log(Log.INFO, "STS", "REgister callback called...");

                        if (MenuFragement.this != null) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    dialog.dismiss();

                                    if (success) {
                                        System.gc();
//                                        Intent myIntent = new Intent(ac, LoginScreen.class);
//                                        startActivity(myIntent);
                                        showToastMessage("You have updated your profile successfully");

//                                        logo_bmp=null;
                                        userDetail();


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

    public void signupUserWithaoutImage(final String fname,final String lname,final String email,final String password,final String year)
    {

        final ProgressDialog dialog = ParseDataManager.showProgress(ac, "Updating", "Updating...");
        ParseDataManager.sharedDataManager(ac).update_nikhil(email.trim(), password, fname, lname, year, null, null, new IRegisterCallback() {

            @Override
            public void onRegister(final boolean success, final ParseException e, ParseUser user) {
//				Crashlytics.log(Log.INFO, "STS", "REgister callback called...");

                if (MenuFragement.this != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            dialog.dismiss();

                            if (success) {
                                System.gc();
//                                        Intent myIntent = new Intent(ac, LoginScreen.class);
//                                        startActivity(myIntent);
                                showToastMessage("You have updated your profile successfully");

                                userDetail();


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




    public void userDetail() {
        if (CheckIsConnectedToInternet(getActivity())) {
            final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
            String email = settings.getString("Email", "");
            String password1 = settings.getString("Password", "");
            System.out.println("password" + password1);

            ParseUser.logInInBackground(email, password1, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e1) {

                    final ParseUser user = ParseUser.getCurrentUser();
                    ParseObject profileObj = null;
                    try {
                        profileObj = ((ParseObject) user.get("profile")).fetchIfNeeded();
                        String firstname = profileObj.getString("FirstName");
                        String lastname = profileObj.getString("LastName");
                        String email = profileObj.getString("user_email");
                        String year1 = profileObj.getString("study_year");
                        String image_name = (firstname + "_" + lastname);
                        System.out.println(image_name);
                        String passWord = user.getString("pwd");
                        setData(firstname, lastname, email, passWord, year1);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    ParseFile fl = (ParseFile) profileObj.get("thumbnail");
                    byte[] data = new byte[0];
                    try {
                        data = fl.getData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("LENGTH IS " + data.length);
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);
                    user_image.setImageBitmap(bmp);


//                    if (image_name != null) {
//                        if (bmp == null) {
//
//                            bmp = PIBitmapUtils.getBitmap(image_name, context);
//
//                            user_image.setImageBitmap(bmp);
//                            if (bmp == null) {
//                                bmp = PIBitmapUtils.getBitmap("no_photo", context);
//                                user_image.setImageBitmap(bmp);
//                                if (bmp == null) {
//                                    user_image.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        }
//
//                    }


                }
            });
        } else {
            final SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);
            String firstname = settings.getString("FbFirstName", "fName");
            String lastname = settings.getString("FbLastName", "lName");
            String email = settings.getString("Fbemail", "uEmail");
            String year1 = settings.getString("FbYear", "Year");
            String passWord = settings.getString("Password", "");
            String image_name = (firstname + "_" + lastname);


            if (image_name != null) {
                Bitmap bmp = null;
                if (bmp == null) {
                    bmp = PIBitmapUtils.getBitmap(image_name, context);

                    user_image.setImageBitmap(bmp);
                    if (bmp == null) {
                        bmp = PIBitmapUtils.getBitmap("no_photo", context);
                        user_image.setImageBitmap(bmp);
                        if (bmp == null) {
                            user_image.setVisibility(View.VISIBLE);
                        }
                    }
                }

                setData(firstname, lastname, email, passWord, year1);

            }

        }
    }

    void setData(String fname, String lname, String em, String pswd, String yr) {
        firstName.setText(fname);
        lastName.setText(lname);
        e_mail.setText(em);
        password.setText(pswd);
        int index = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(yr)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);

    }


    public void getUserCurrentImage()
    {
        SharedPreferences settings = context.getSharedPreferences(context.getPackageName(), 0);

        String firstname = settings.getString("FbFirstName", "NA");
        String lastname = settings.getString("FbLastName", "NA");
        String image_name = (firstname + "_" + lastname);
        System.out.println(image_name);
        if (image_name != null) {
            Bitmap bmp = null;
            if (bmp == null) {
                bmp = PIBitmapUtils.getBitmap(image_name, context);


                logo_bmp=bmp;
                if (bmp == null) {
                    bmp = PIBitmapUtils.getBitmap("no_photo", context);

                    logo_bmp=bmp;
                    if (bmp == null) {

                    }
                }
            }

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item


        year = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + year, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void showAlertSimple(final String  message)
    {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
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
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
    public void noInternet()
    {
        Toast.makeText(getActivity(), GlobalClass.internet_message,
                Toast.LENGTH_SHORT).show();
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
        ParseDataManager.photoFunctionalityLaunch(menu_fragement, imageUri);


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
    public void showToastMessage(String message)
    {
        Toast.makeText(ac,message,
                Toast.LENGTH_SHORT).show();

    }
    public void showOnImageView(Uri image_uri)
    {
//	    	Parse.enableLocalDatastore(this);
        Bitmap bitmap=null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , image_uri);
            logo_bmp=bitmap;
            user_image.setImageBitmap(bitmap);

        }
        catch (Exception e)
        {
            //handle exception
        }



    }
    private void InitializeListeners() {
        fb.setOnClickListener(this);
        twitter.setOnClickListener(this);
        pintrest.setOnClickListener(this);
        youTube.setOnClickListener(this);
        instagram.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivfacebook:
                if (CheckIsConnectedToInternet(getActivity()))
                {
                    String faceBookPackage = "com.facebook.katana";

                    String faceBookAppID = bs.getString(ParseDataManager.TAG_LINK_FACEBOOK);
                    String newString = faceBookAppID.replace("profile", "page");
                    //String faceBookAppID= "fb://page/53546673664";
                    System.out.println("fb string"+newString);
                    String faceBookURLBrowser ="https://www.facebook.com/pages/profile/53546673664";
                    Intent intent = getOpenLinkIntent(ac, faceBookPackage, newString, faceBookURLBrowser);
                    startActivity(intent);
                }
                else
                {
                    ParseDataManager.sharedDataManager(ac).noInternet(ac);
                }
                break;
            case R.id.ivtwitter:
                if (CheckIsConnectedToInternet(getActivity())) {
                    String twitterPackage = "com.twitter.android";
                    String twitterAppID =  bs.getString(ParseDataManager.TAG_LINK_TWITTER);
                    String newString =    twitterAppID.replace("twitter:/", "page");
                    String twitterURLBrowser = bs.getString(ParseDataManager.TAG_LINK_TWITTER);
                    Intent intent = getOpenLinkIntent(ac, twitterPackage, twitterAppID, twitterURLBrowser);
                    startActivity(intent);
                }
                else
                {
                    ParseDataManager.sharedDataManager(ac).noInternet(ac);
                }
                break;
            case R.id.ivpinterest:
                if (CheckIsConnectedToInternet(getActivity())) {
                    // String newString = faceBookAppID.replace("profile", "page");
                    String newString = bs.getString(ParseDataManager.TAG_LINK_PINTEREST);
                    String pinterest = newString.replace("pinterest://", "https://www.pinterest.com/");

//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pinterest));
//                    startActivity(browserIntent);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(pinterest));
                    startActivity(i);


                }
                else
                {
                    ParseDataManager.sharedDataManager(ac).noInternet(ac);

                }
                break;


            case R.id.ivinstagram:
                if (CheckIsConnectedToInternet(getActivity())) {
                    String instaAppID = bs.getString(ParseDataManager.TAG_LINK_INSTAGRAM);
                    String newString = instaAppID.replace("instagram://", "");
                    Uri uri = Uri.parse("http://instagram.com/_u/"+newString);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/"+newString)));
                    }

                }
                else
                {
                    ParseDataManager.sharedDataManager(ac).noInternet(ac);

                }
                break;

            case R.id.ivyoutube:
                if (CheckIsConnectedToInternet(getActivity())) {
                    String youtubePackage = "vnd.youtube:";
                    String youtubeAppID = bs.getString(ParseDataManager.TAG_LINK_YOU_TUBE);
                    String youtubeURLBrowser = bs.getString(ParseDataManager.TAG_LINK_YOU_TUBE);
                    Intent intent = getOpenLinkIntent(ac, youtubePackage, youtubeAppID, youtubeURLBrowser);
                    startActivity(intent);

//                  String url ="user/videoslusofona";
//                  watchYoutubeVideo(url);

                } else {
                    ParseDataManager.sharedDataManager(ac).noInternet(ac);

                }
                break;

        }
    }

    private Intent getOpenLinkIntent(Context context,String packageName,String urlAppID,String urlBrowser) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(urlAppID));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(urlBrowser));
        }
    }

    public float loadDataVersion()
    {
        float    dataversion=1f;
        SharedPreferences settings = ac.getSharedPreferences(ac.getPackageName(), 0);
        if (settings.contains("DataVersion"))
        {
            dataversion= settings.getFloat("DataVersion",1f);

        }

        return dataversion;
    }
    public void loadVersion() {
        SharedPreferences settings = getActivity().getSharedPreferences(
                getActivity().getPackageName(), 0);
        if (settings.contains("AppVersion") && settings.contains("DataVersion")) {
//			currentDataVersion = settings.getFloat("AppVersion", 1);
            currentDataVersion = settings.getFloat("DataVersion", 1.0f);

//			currentUpdateTime = settings.getLong("DateUpdated", 0);
        }
    }
}



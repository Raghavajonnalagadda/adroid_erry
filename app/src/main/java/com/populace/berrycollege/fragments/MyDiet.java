package com.populace.berrycollege.fragments;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.populace.berrycollege.R;
import com.populace.berrycollege.activities.Nutrition_Activity;
import com.populace.berrycollege.database.MydietDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import soundcloud.android.crop.util.Log;

public class MyDiet extends Fragment {
    public EditText total_txt, brkfast_txt,lunch_txt,dinner_txt,snacks_txt;
    LinearLayout mydiet_layout;

    public int brkfst, lunch,dinner,snacks,total=0,diner;
   // public String dateString;
    public String date;
    private int brkfaast,lunchOnFocusChange,dinnerOnFocusChange,snacksOnFocusChange,newbrkfst,newlnch,newdinner,newsnacks,newtotal;
    Button save;
    MydietDatabase db;
     Nutrition_Activity nutrition = new Nutrition_Activity();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1, Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver1, new IntentFilter("custom-event-name1"));
        View v = inflater.inflate(R.layout.fragment_my_diet, container1, false);
        brkfast_txt = (EditText) v.findViewById(R.id.edit_brkfast);
        lunch_txt = (EditText) v.findViewById(R.id.edit_lunch);
        dinner_txt = (EditText) v.findViewById(R.id.edit_dinner);
        snacks_txt = (EditText) v.findViewById(R.id.edit_snacks);
        total_txt = (EditText) v.findViewById(R.id.tv_total);
        mydiet_layout = (LinearLayout) v.findViewById(R.id.mydiet_layout);
       // date_txt = (TextView) v.findViewById(R.id.tvdate);
        save = (Button) v.findViewById(R.id.btn_save);
        db = new MydietDatabase(getActivity());


        brkfast_txt.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                total_txt.setText(addNumbers());


                try {
                    if (brkfast_txt.getText().toString() != null) {
                        total = Integer.parseInt(brkfast_txt.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    total = 0;
                }
            }

            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub



            }

        });

        lunch_txt.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                total_txt.setText(addNumbers());

                try {
                    if (lunch_txt.getText().toString() != null) {
                        total = Integer.parseInt(lunch_txt.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    total = 0;
                }


            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        dinner_txt.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                total_txt.setText(addNumbers());

                try {
                    if (dinner_txt.getText().toString() != null) {
                        diner = Integer.parseInt(dinner_txt.getText().toString());

                        total = Integer.parseInt(dinner_txt.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    total = 0;
                }


            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        snacks_txt.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                total_txt.setText(addNumbers());

                try {
                    if (snacks_txt.getText().toString() != null) {
                        total = Integer.parseInt(snacks_txt.getText().toString());
                    }
                } catch (NumberFormatException e) {
                    total = 0;
                }

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


            }
        });





        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(db.value_date==null) {

                    saveData();

                }
                else
                {
                    UpdateData();
                    db.getData();
                }
            }
            });


        return v;

    }

    private String addNumbers() {
        try {

            if (brkfast_txt.getText().toString() != "" && brkfast_txt.getText().length() > 0) {
                brkfst = Integer.parseInt(brkfast_txt.getText().toString());


            } else {
                brkfst = 0;
            }
            if (lunch_txt.getText().toString() != "" && lunch_txt.getText().length() > 0) {
                lunch = Integer.parseInt(lunch_txt.getText().toString());

            } else {
                lunch = 0;
            }
            if (dinner_txt.getText().toString() != "" && dinner_txt.getText().length() > 0) {
                dinner = Integer.parseInt(dinner_txt.getText().toString());


            } else {
                dinner = 0;
            }
            if (snacks_txt.getText().toString() != "" && snacks_txt.getText().length() > 0) {
                snacks = Integer.parseInt(snacks_txt.getText().toString());


            } else {
                snacks = 0;
            }


        }
        catch (NumberFormatException e)
        {
            System.out.println("Error ......");
        }
        return Integer.toString(brkfst + lunch + dinner + snacks);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println(" date when on onpause called " + Nutrition_Activity.dateStrng);
//        if(db.value_date==null) {
//
//            saveData();
//
//        }
//        else
//        {
//            UpdateData();
//            db.getData();
//        }
    }



    @Override
    public void onResume() {
        super.onResume();
        db.getData();
        ShowData();
    }

    public void saveData()
    {
        String date = Nutrition_Activity.dateStrng.replace(",", "");
        try {
            brkfaast = Integer.parseInt(brkfast_txt.getText().toString());
        } catch (NumberFormatException e) {
            brkfaast = 0;
        }
        try {
            lunchOnFocusChange = Integer.parseInt(lunch_txt.getText().toString());
        } catch (NumberFormatException e) {
            lunchOnFocusChange = 0;
        }
        try {
            dinnerOnFocusChange = Integer.parseInt(dinner_txt.getText().toString());
        } catch (NumberFormatException e) {
            dinnerOnFocusChange = 0;
        }
        try {
            snacksOnFocusChange = Integer.parseInt(snacks_txt.getText().toString());
        } catch (NumberFormatException e) {
            snacksOnFocusChange = 0;
        }
        try {
            total = Integer.parseInt(total_txt.getText().toString());
        }
        catch (NumberFormatException e) {
            total = 0;
        }
       db.insertData(/*dateString*/date, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
    }
 public void UpdateData()
 {
     String date = Nutrition_Activity.dateStrng.replace(",", "");
     System.out.println("My check date is: "+date);
    //int newbrkfst,newlnch,newdinner,newsnacks,newtotal;
     try {
         if (db.value_breakfast != Integer.parseInt(brkfast_txt.getText().toString())) {
              newbrkfst = Integer.parseInt(brkfast_txt.getText().toString());
            //db.updateData(/*dateString*/Nutrition_Activity.dateStrng, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
         }
         else
         {
             newbrkfst=Integer.parseInt(brkfast_txt.getText().toString());
         }
     }
     catch (NumberFormatException e) {
         newbrkfst =0;
     }
     try {

         if (db.value_lunch != Integer.parseInt(lunch_txt.getText().toString())) {
              newlnch = Integer.parseInt(lunch_txt.getText().toString());
            // db.updateData(/*dateString*/Nutrition_Activity.dateStrng, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
         }
         else
         {
             newlnch=Integer.parseInt(lunch_txt.getText().toString());
         }
     }
     catch (NumberFormatException e) {
         newlnch =0;
     }
     try {
         if (db.value_dinner != Integer.parseInt(dinner_txt.getText().toString())) {
              newdinner = Integer.parseInt(dinner_txt.getText().toString());
             // db.updateData(/*dateString*/Nutrition_Activity.dateStrng, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
         }
         else
         {
             newdinner=Integer.parseInt(dinner_txt.getText().toString());
         }
     }
     catch (NumberFormatException e) {
         newdinner =0;
     }
     try {

         if (db.value_snacks != Integer.parseInt(snacks_txt.getText().toString())) {
              newsnacks = Integer.parseInt(snacks_txt.getText().toString());
            // db.updateData(/*dateString*/Nutrition_Activity.dateStrng, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
         }
         else
         {
             newsnacks=Integer.parseInt(snacks_txt.getText().toString());
         }
     }
     catch (NumberFormatException e) {
         newsnacks=0;
     }
     try{
         if (db.value_total != Integer.parseInt(total_txt.getText().toString())) {
             newtotal = Integer.parseInt(total_txt.getText().toString());
            // db.updateData(/*dateString*/Nutrition_Activity.dateStrng, brkfaast, lunchOnFocusChange, dinnerOnFocusChange, snacksOnFocusChange, total);
         }
         else
         {
             newtotal=Integer.parseInt(total_txt.getText().toString());
         }
     }
     catch (NumberFormatException e) {
         newtotal=0;
     }
     db.updateData(/*dateString*/date, newbrkfst, newlnch, newdinner, newsnacks, newtotal);
 }


    public  void ShowData()
    {
        if (db.value_date != null)
        {
            System.out.println("Controll goes inside the Onresume");
            if (db.value_breakfast != 0)
            {
                brkfast_txt.setText("" + db.value_breakfast);

            }

            if (db.value_lunch != 0) {

                lunch_txt.setText("" + db.value_lunch);


            }

            if (db.value_dinner != 0) {

                dinner_txt.setText("" + db.value_dinner);

            }

            if (db.value_snacks != 0) {

                snacks_txt.setText("" + db.value_snacks);


            }

        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int indxVal = intent.getIntExtra("position",0);
            Toast.makeText(getActivity(),"message Recieved",Toast.LENGTH_LONG).show();
            if(db.value_date==null) {

                saveData();

            }
            else
            {
                UpdateData();
                db.getData();
            }

        }
    };

    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int indxVal = intent.getIntExtra("position1",0);
            Toast.makeText(getActivity(),"message1 Recieved",Toast.LENGTH_LONG).show();
            if(db.value_date==null) {

                saveData();

            } else
            {
                UpdateData();
                db.getData();
            }



    //       mydiet_layout.invalidate();
//           mydiet_layout.removeView(brkfast_txt);
//            mydiet_layout.addView(brkfast_txt);
//       //    mydiet_layout.invalidate();
//            mydiet_layout.refreshDrawableState();



        }
    };
    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter]
        // removeObserver:name:object:]
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                mMessageReceiver);
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
//                mMessageReceiver1);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
   //     Toast.makeText(getActivity(),"on detach called",Toast.LENGTH_LONG).show();
    }
}









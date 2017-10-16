package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;


public class Activities_Fragment extends Fragment implements OnClickListener {
    WebView activities;
    BerrySession bs;

    Button onCampus, offCampus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activities_, container1, false);
        onCampus = (Button) v.findViewById(R.id.btnOnCampus);
        offCampus = (Button) v.findViewById(R.id.btnOffCampus);
        onCampus.setSelected(true);
        offCampus.setSelected(false);
        getFragmentManager().beginTransaction().add(R.id.container_fragment_activities,new OnCampusFragment(),null).commit();

        InitializeListeners();
        return v;
    }



    private void InitializeListeners()
    {
        onCampus.setOnClickListener(this);
        offCampus.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOnCampus:
                onCampus.setPressed(true);
                onCampus.setSelected(true);
                offCampus.setSelected(false);
                // onCampus.setBuResource(R.drawable.physical_on_campus1);
                //offCampus.setImageResource(R.drawable.physical_off_campus1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_activities, new OnCampusFragment(), null).commit();
                break;

            case R.id.btnOffCampus:

                onCampus.setSelected(false);
                offCampus.setSelected(true);
                //onCampus.setImageResource(R.drawable.physical_on_campus1);
                //offCampus.setImageResource(R.drawable.physical_off_campus1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_activities, new OffCampusFragment(), null).commit();
                break;
        }
    }
}


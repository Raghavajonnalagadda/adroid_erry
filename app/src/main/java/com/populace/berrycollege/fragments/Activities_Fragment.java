package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;


public class Activities_Fragment extends Fragment implements OnClickListener {
    WebView activities;
    BerrySession bs;

    ImageView onCampus, offCampus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activities_, container1, false);
        onCampus = (ImageView) v.findViewById(R.id.btnOnCampus);
        offCampus = (ImageView) v.findViewById(R.id.btnOffCampus);
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

                onCampus.setImageResource(R.drawable.btntab_community1);
                offCampus.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_activities, new OnCampusFragment(), null).commit();
                break;

            case R.id.btnOffCampus:
                onCampus.setImageResource(R.drawable.btntab_community0);
                offCampus.setImageResource(R.drawable.btntab_challenges1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_activities, new OffCampusFragment(), null).commit();
                break;
        }
    }
}


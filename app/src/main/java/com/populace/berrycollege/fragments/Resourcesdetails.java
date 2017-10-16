package com.populace.berrycollege.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.BerrySession;


public class Resourcesdetails extends android.app.Fragment implements View.OnClickListener {
    WebView activities;
    BerrySession bs;

    Button onCampusResources, peerEducators;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resourcesdetails, container1, false);
        onCampusResources = (Button) v.findViewById(R.id.btnOnCampusResoureces);
        peerEducators = (Button) v.findViewById(R.id.btnPeerEducators);
        onCampusResources.setSelected(true);
        peerEducators.setSelected(false);
        getFragmentManager().beginTransaction().add(R.id.container_fragment_resourcesdetails, new ResourcesFragment(), null).commit();

        InitializeListeners();
        return v;
    }


    private void InitializeListeners() {
        onCampusResources.setOnClickListener(this);
        peerEducators.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOnCampusResoureces:
                //  onCampusResources.setPressed(true);
                onCampusResources.setSelected(true);
                peerEducators.setSelected(false);
                // onCampus.setBuResource(R.drawable.physical_on_campus1);
                //offCampus.setImageResource(R.drawable.physical_off_campus1);
                getFragmentManager().beginTransaction().add(R.id.container_fragment_resourcesdetails, new ResourcesFragment(), null).commit();
                break;

            case R.id.btnPeerEducators:

                onCampusResources.setSelected(false);
                peerEducators.setSelected(true);
                //onCampus.setImageResource(R.drawable.physical_on_campus1);
                //offCampus.setImageResource(R.drawable.physical_off_campus1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_resourcesdetails, new PeerExecutor(), null).commit();
                break;
        }
    }
}



package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.populace.berrycollege.R;


public class OnCampusFragment extends Fragment implements OnClickListener {
    ImageView onBikikng, onTrail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_on_campus, container, false);
        onBikikng = (ImageView) v.findViewById(R.id.btnOnByking);
        onTrail = (ImageView) v.findViewById(R.id.btnOnTrail);
        getFragmentManager().beginTransaction().add(R.id.container_fragment_oncampus, new OnBikingFragment(), null).commit();
        InitializeListeners();
        return v;
    }
        private void InitializeListeners()
        {

        onBikikng.setOnClickListener(this);
        onTrail.setOnClickListener(this);

        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOnByking:
                onBikikng.setImageResource(R.drawable.btntab_challenges1);
                onTrail.setImageResource(R.drawable.btntab_community0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_oncampus, new OnBikingFragment(), null).commit();
                break;

            case R.id.btnOnTrail:
                onBikikng.setImageResource(R.drawable.btntab_challenges0);
                onTrail.setImageResource(R.drawable.btntab_community1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_oncampus, new OnTrailFragment(), null).commit();
                break;
        }
    }
    }




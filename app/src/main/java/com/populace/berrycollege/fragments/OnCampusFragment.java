package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.populace.berrycollege.R;


public class OnCampusFragment extends Fragment implements OnClickListener {
    Button onBikikng, onTrail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_on_campus, container, false);
        onBikikng = (Button) v.findViewById(R.id.btnOnByking);
        onTrail = (Button) v.findViewById(R.id.btnOnTrail);
        onBikikng.setSelected(true);
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
                onBikikng.setSelected(true);
                onTrail.setSelected(false);
                //onBikikng.setImageResource(R.drawable.physical_on_biking);
                //onTrail.setImageResource(R.drawable.physical_on_trailmap);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_oncampus, new OnBikingFragment(), null).commit();
                break;

            case R.id.btnOnTrail:

                onBikikng.setSelected(false);
                onTrail.setSelected(true);
                // onBikikng.setImageResource(R.drawable.physical_on_biking);
                //onTrail.setImageResource(R.drawable.physical_on_trailmap);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_oncampus, new OnTrailFragment(), null).commit();
                break;
        }
    }
    }




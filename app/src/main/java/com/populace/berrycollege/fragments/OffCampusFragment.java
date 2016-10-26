package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.populace.berrycollege.R;


public class OffCampusFragment extends Fragment implements OnClickListener {

    ImageView races,river_rental,hiking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_off_campus, container, false);

        races = (ImageView) v.findViewById(R.id.btnRaces);
        river_rental = (ImageView) v.findViewById(R.id.btnRiverRental);
        hiking = (ImageView) v.findViewById(R.id.btnHiking);
        getFragmentManager().beginTransaction().add(R.id.container_fragment_offCampus,new OnRaces(),null).commit();
        InitializeListeners();
        return v;


    }

    private void InitializeListeners()
    {
        races.setOnClickListener(this);
        river_rental.setOnClickListener(this);
        hiking.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRaces:

                races.setImageResource(R.drawable.btntab_community1);
                river_rental.setImageResource(R.drawable.btntab_challenges0);
                hiking.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new OnRaces(), null).commit();
                break;

            case R.id.btnRiverRental:
                races.setImageResource(R.drawable.btntab_community0);
                river_rental.setImageResource(R.drawable.btntab_challenges1);
                hiking.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new RiverRentalFragment(), null).commit();
                break;

            case R.id.btnHiking:
                races.setImageResource(R.drawable.btntab_community0);
                river_rental.setImageResource(R.drawable.btntab_challenges0);
                hiking.setImageResource(R.drawable.btntab_challenges1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new HikingFragment(), null).commit();
                break;
        }
    }
}

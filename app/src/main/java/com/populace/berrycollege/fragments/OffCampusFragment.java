package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.populace.berrycollege.R;


public class OffCampusFragment extends Fragment implements OnClickListener {

    Button races, river_rental, hiking;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_off_campus, container, false);

        races = (Button) v.findViewById(R.id.btnRaces);
        river_rental = (Button) v.findViewById(R.id.btnRiverRental);
        hiking = (Button) v.findViewById(R.id.btnHiking);
        races.setSelected(true);

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
                races.setSelected(true);
                river_rental.setSelected(false);
                hiking.setSelected(false);

                // races.setImageResource(R.drawable.physical_races1);
                //river_rental.setImageResource(R.drawable.physical_river_rentals1);
                //hiking.setImageResource(R.drawable.physical_hiking1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new OnRaces(), null).commit();
                break;

            case R.id.btnRiverRental:

                races.setSelected(false);
                river_rental.setSelected(true);
                hiking.setSelected(false);
                //races.setImageResource(R.drawable.physical_races1);
                //river_rental.setImageResource(R.drawable.physical_river_rentals1);
                //hiking.setImageResource(R.drawable.physical_hiking1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new RiverRentalFragment(), null).commit();
                break;

            case R.id.btnHiking:

                races.setSelected(false);
                river_rental.setSelected(false);
                hiking.setSelected(true);
                //races.setImageResource(R.drawable.physical_races1);
                //river_rental.setImageResource(R.drawable.physical_river_rentals1);
                //hiking.setImageResource(R.drawable.physical_hiking1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_offCampus, new HikingFragment(), null).commit();
                break;
        }
    }
}

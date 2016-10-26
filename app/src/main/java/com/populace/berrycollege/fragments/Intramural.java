package com.populace.berrycollege.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.populace.berrycollege.R;


public class Intramural extends Fragment implements OnClickListener {

    ImageView schedule,register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intramural, container1, false);
        schedule = (ImageView) v.findViewById(R.id.btnSchedule);
        register = (ImageView) v.findViewById(R.id.btnRegister);
        getFragmentManager().beginTransaction().add(R.id.container_fragmen_intramural,new ScheduleFragment(),null).commit();

        InitializeListeners();
        return v;
    }
    private void InitializeListeners()
    {
        schedule.setOnClickListener(this);
        register.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSchedule:

                schedule.setImageResource(R.drawable.btntab_community1);
                register.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragmen_intramural, new ScheduleFragment(), null).commit();
                break;

            case R.id.btnRegister:
                schedule.setImageResource(R.drawable.btntab_community0);
                register.setImageResource(R.drawable.btntab_challenges1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragmen_intramural, new Register(), null).commit();
                break;
        }
    }
}

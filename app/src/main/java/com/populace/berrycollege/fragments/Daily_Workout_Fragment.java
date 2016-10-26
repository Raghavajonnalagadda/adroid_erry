package com.populace.berrycollege.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.populace.berrycollege.R;
import com.populace.berrycollege.managers.ParseDataManager;

public class Daily_Workout_Fragment extends Fragment implements View.OnClickListener {
   ImageView minutes,doorRoom,jumpRope;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container1,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily__workout_, container1, false);
        if (ParseDataManager.sharedDataManager(getActivity()).CheckIsConnectedToInternet(getActivity())) {
        minutes = (ImageView) v.findViewById(R.id.btnMinutes);
        doorRoom = (ImageView) v.findViewById(R.id.btnDoorRoom);
        jumpRope = (ImageView) v.findViewById(R.id.btnJumpRope);
        getFragmentManager().beginTransaction().add(R.id.container_fragment_dailyWorkout,new MinutesFragment(),null).commit();

        InitializeListeners();
    }
    else
    {
        ParseDataManager.sharedDataManager(getActivity()).noInternet(getActivity());
    }
        return v;
    }
    private void InitializeListeners()
    {
        minutes.setOnClickListener(this);
        doorRoom.setOnClickListener(this);
        jumpRope.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMinutes:

                minutes.setImageResource(R.drawable.btntab_community1);
                doorRoom.setImageResource(R.drawable.btntab_challenges0);
                jumpRope.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_dailyWorkout, new MinutesFragment(), null).commit();
                break;

            case R.id.btnDoorRoom:
                minutes.setImageResource(R.drawable.btntab_community0);
                doorRoom.setImageResource(R.drawable.btntab_challenges1);
                jumpRope.setImageResource(R.drawable.btntab_challenges0);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_dailyWorkout, new DoorRoomFragment(), null).commit();
                break;

            case R.id.btnJumpRope:
                minutes.setImageResource(R.drawable.btntab_community0);
                doorRoom.setImageResource(R.drawable.btntab_challenges0);
                jumpRope.setImageResource(R.drawable.btntab_challenges1);
                getFragmentManager().beginTransaction().replace(R.id.container_fragment_dailyWorkout, new JumpRope(), null).commit();
                break;
        }
    }
}

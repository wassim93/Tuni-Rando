package com.sim.tunisierando.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sim.tunisierando.R;
import com.sim.tunisierando.NewEventActivity;

import static com.sim.tunisierando.R.id.fab;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements View.OnClickListener {



    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_events, container, false);

        Fragment listEvFragment = new ListEventFragment();
        LoadFragment(listEvFragment);



        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case fab:
                Intent i = new Intent(getActivity(), NewEventActivity.class);
                startActivity(i);
                break;
        }
    }


    public void LoadFragment(Fragment fragment){
        FragmentManager fragmentManager = (getActivity().getSupportFragmentManager());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("init");
        fragmentTransaction.commit();
    }






}

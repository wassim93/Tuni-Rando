package com.sim.tunisierando.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sim.tunisierando.fragments.ArticlesFragment;
import com.sim.tunisierando.fragments.EventsFragment;
import com.sim.tunisierando.fragments.HomeFragment;

/**
 * Created by wassim on 13/11/2017.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    String [] titles = new String[]{"Acceuil","Evenement","Favoris"};
    private Fragment fragment;
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new HomeFragment();
            case 1:
                return  new EventsFragment();
            case 2:
                return  new ArticlesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}

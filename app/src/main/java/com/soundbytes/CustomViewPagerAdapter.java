package com.soundbytes;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Olumide on 10/3/2015.
 */
public class CustomViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<TitledFragment> fragments;

    public CustomViewPagerAdapter(FragmentManager fragManager, ArrayList<TitledFragment> fragments){
        super(fragManager);
        this.fragments = fragments;
    }

    @Override
    public TitledFragment getItem(int i){
        return fragments.get(i);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return getItem(position).getTitle();
    }

    @Override
    public int getCount(){
        return fragments.size();
    }
}

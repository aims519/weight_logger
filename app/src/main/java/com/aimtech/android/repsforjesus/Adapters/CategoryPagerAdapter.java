package com.aimtech.android.repsforjesus.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aimtech.android.repsforjesus.Fragments.ArmsFragment;
import com.aimtech.android.repsforjesus.Fragments.BackFragment;
import com.aimtech.android.repsforjesus.Fragments.ChestFragment;
import com.aimtech.android.repsforjesus.Fragments.LegsFragment;

/**
 * Created by Andy on 30/07/2016.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;
    private final String[] categoryTitleStrings = new String[]{"Chest","Back","Arms","Legs"};

    // Requires this emtpy constructor
    public CategoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChestFragment();
            case 1:
                return new BackFragment();
            case 2:
                return new ArmsFragment();
            case 3:
                return new LegsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Function to return the fragment title from the list above
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryTitleStrings[position];
    }
}

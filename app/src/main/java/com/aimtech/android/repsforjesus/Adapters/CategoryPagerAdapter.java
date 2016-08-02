package com.aimtech.android.repsforjesus.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aimtech.android.repsforjesus.Fragments.ExerciseListFragment;

/**
 * Created by Andy on 30/07/2016.
 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;
    private final String[] mCategoryTitleStrings = new String[]{"CHEST", "BACK", "ARMS", "LEGS"};
    //private final int[] mImageResIds = new int[]{R.drawable.ic_add_white_24dp,R.drawable.torso_24,R.drawable.torso_24,R.drawable.torso_24};

    //Context required for getDrawable below
    private Context mContext;

    // Requires this emtpy constructor
    public CategoryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ExerciseListFragment.newInstance("chest");
            case 1:
                return ExerciseListFragment.newInstance("back");
            case 2:
                return ExerciseListFragment.newInstance("arms");
            case 3:
                return ExerciseListFragment.newInstance("legs");
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

        return mCategoryTitleStrings[position];
    }

    @Override
    public int getItemPosition(Object object) {
        // always return position none so that fragments are refreshed when calling notifyDataSetChanged
        return POSITION_NONE;
    }
}

package com.aimtech.android.repsforjesus.Fragments;

import android.os.Bundle;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 01/08/2016.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

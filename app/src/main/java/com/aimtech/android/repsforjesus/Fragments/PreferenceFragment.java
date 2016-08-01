package com.aimtech.android.repsforjesus.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.Log;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 01/08/2016.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Register onPrefChange listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        // Update the summary of the max days preference
        EditTextPreference maxDaysPref = (EditTextPreference) getPreferenceScreen().findPreference("pref_max_days_before_warning");
        maxDaysPref.setSummary(maxDaysPref.getText());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Log.d("Shared Prefs","Key passed : " + s);
        Preference prefChanged = findPreference(s);
        if(prefChanged instanceof EditTextPreference && prefChanged.getKey().equals("pref_max_days_before_warning")){
            EditTextPreference maxDaysPref = (EditTextPreference) findPreference(s);
            maxDaysPref.setSummary(maxDaysPref.getText());
        }


    }
}

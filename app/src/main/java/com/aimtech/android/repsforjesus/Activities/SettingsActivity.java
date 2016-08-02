package com.aimtech.android.repsforjesus.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aimtech.android.repsforjesus.Fragments.PreferenceFragment;
import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 01/08/2016.
 * Activity to display PreferenceFragment
 */
public class SettingsActivity extends AppCompatActivity {

    // Load the preference fragment


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new PreferenceFragment())
                .commit();
    }
}

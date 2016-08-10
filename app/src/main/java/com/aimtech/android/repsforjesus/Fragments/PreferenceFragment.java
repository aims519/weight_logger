package com.aimtech.android.repsforjesus.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

import java.util.Locale;

/**
 * Created by Andy on 01/08/2016.
 * Displays a list of preferences
 * Implements the OnSharedPreferenceChangeListenter to update the UI after a settings change is made by the user
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    ExerciseDatabaseHelper mDbHelper;
    Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences to be displayed
        addPreferencesFromResource(R.xml.preferences);

        // Register onPrefChange listener so that the onSharedPreferenceChanged callback can be recieved
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        // Update the summary text of the 'max days' preference
        EditTextPreference maxDaysPref = (EditTextPreference) getPreferenceScreen().findPreference("pref_max_days_before_warning");
        maxDaysPref.setSummary(maxDaysPref.getText());

        mContext = getActivity().getApplicationContext();
    }

    // Unregister listener
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }

    // Register listener again
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        // When the "pref_max_changed_before_warning" setting is changed by the user, update the summary text so that
        // the user knows it's been updated.
        Log.d("Shared Prefs", "Key passed : " + s);
        Preference prefChanged = findPreference(s);


        if (prefChanged instanceof EditTextPreference && prefChanged.getKey().equals("pref_max_days_before_warning")) {
            EditTextPreference maxDaysPref = (EditTextPreference) findPreference(s);
            maxDaysPref.setSummary(maxDaysPref.getText());

        } else if (prefChanged instanceof ListPreference && prefChanged.getKey().equals("pref_units")) {
            Log.d("UnitPrefChanged", "To : " + ((ListPreference) prefChanged).getValue());

            mDbHelper = new ExerciseDatabaseHelper(mContext);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Return all database records in a cursor
            Cursor results = db.rawQuery("SELECT * FROM " + ExerciseDatabaseContract.ExerciseTable.TABLE_NAME, null);

            // Column indices used to easily retrieve data from cursor
            int idIndex = results.getColumnIndex(ExerciseDatabaseContract.ExerciseTable._ID);
            int nameIndex = results.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME);
            int currentWeightIndex = results.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT);
            int previousWeightIndex = results.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT);

            String currentExerciseName;

            ContentValues values;

            while (results.moveToNext()) {

                currentExerciseName = results.getString(nameIndex);

                values = new ContentValues();

                if (((ListPreference) prefChanged).getValue().equals(mContext.getString(R.string.units_kilograms))) {
                    // Convert from lb to kg

                    // Convert the current weight
                    Double newWeightKg = Double.parseDouble(results.getString(currentWeightIndex)) / 2.20462;
                    values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT, String.format(Locale.ENGLISH, "%.2f", newWeightKg));

                    // Convert the previous weight
                    if (results.getString(previousWeightIndex) != null) {
                        Double newPrevWeightKg = Double.parseDouble(results.getString(previousWeightIndex)) / 2.20462;
                        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT, String.format(Locale.ENGLISH, "%.2f", newPrevWeightKg));
                    }

                    // Update Database
                    db.update(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME, values, ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " = ?", new String[]{currentExerciseName});

                    // notify user
                    Toast.makeText(mContext, "Weights converted to kg", Toast.LENGTH_SHORT).show();


                } else if (((ListPreference) prefChanged).getValue().equals(mContext.getString(R.string.units_pounds))) {
                    // Convert from kg to lb

                    // Convert current weight
                    Double newWeightLb = Double.parseDouble(results.getString(currentWeightIndex)) * 2.20462;
                    values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT, String.format(Locale.ENGLISH, "%.2f", newWeightLb));

                    // Convert the previous weight
                    if (results.getString(previousWeightIndex) != null) {
                        Double newPrevWeightLb = Double.parseDouble(results.getString(previousWeightIndex)) * 2.20462;
                        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT, String.format(Locale.ENGLISH, "%.2f", newPrevWeightLb));
                    }


                    // Update database
                    db.update(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME, values, ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " = ?", new String[]{currentExerciseName});

                    // Notify user
                    Toast.makeText(mContext, "Weights converted to lb", Toast.LENGTH_SHORT).show();
                }

                values.clear();


            }


            // Clean up after update
            results.close();
            db.close();
            mDbHelper.close();


        }


    }
}

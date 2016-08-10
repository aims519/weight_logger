package com.aimtech.android.repsforjesus.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aimtech.android.repsforjesus.Model.Exercise;
import com.aimtech.android.repsforjesus.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andy on 30/07/2016.
 */
public class ExerciseListAdapter extends ArrayAdapter<Exercise> {

    private final String LOG_TAG = ExerciseListAdapter.class.getSimpleName();

    private SharedPreferences mPrefs;
    private Context mContext;

    public ExerciseListAdapter(Context context, List<Exercise> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mContext = getContext();

        // Check if existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            // Layout inflater turns an xml file into actual View objects
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_list_item, parent, false);
        }

        // Hook up views in the layout
        TextView exerciseNameTextView = (TextView) listItemView.findViewById(R.id.exerciseNameTextView);
        TextView currentWeightTextView = (TextView) listItemView.findViewById(R.id.currentWeightTextView);
        TextView weightUpWarningTextView = (TextView) listItemView.findViewById(R.id.weightUpWarningTextView);

        // Display Data
        Exercise currentExercise = getItem(position);
        exerciseNameTextView.setText(currentExercise.getName());

        // Checks preferences or which units to use
        String unitPref = mPrefs.getString(mContext.getString(R.string.pref_units_key),mContext.getString(R.string.pref_unit_default));

        if(unitPref.equals(mContext.getString(R.string.units_kilograms))){
            currentWeightTextView.setText(getContext().getString(R.string.item_current_weight_kg,String.valueOf(currentExercise.getCurrentWeight())));
        } else if (unitPref.equals(mContext.getString(R.string.units_pounds))){
            currentWeightTextView.setText(getContext().getString(R.string.item_current_weight_lb,String.valueOf(currentExercise.getCurrentWeight())));
        }



        // If user hasn't increased the weight in a week, display a message
        // Only do this if the shared preference for showing messages is true
        Boolean showOverdueMessagesPref = mPrefs.getBoolean(getContext().getString(R.string.pref_show_days_since_increase_key), Boolean.parseBoolean(getContext().getString(R.string.pref_show_days_since_increase_default)));
        String maxDaysBeforeMovingUpString = mPrefs.getString(getContext().getString(R.string.pref_days_before_warning_key), getContext().getString(R.string.pref_days_before_warning_default));
        int maxDaysInteger = Integer.parseInt(maxDaysBeforeMovingUpString);
        Log.d(LOG_TAG, "ShowOverdue : " + showOverdueMessagesPref + ". Days : " + String.valueOf(maxDaysInteger));

        if (showOverdueMessagesPref.equals(true)) {

            if (currentExercise.getDateLastUpdated() != null) {
                // Get today's date
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();

                // Get date last updated
                Date lastUpdated = currentExercise.getDateLastUpdated();

                // Calculate number of days passed
                long daysPassed = TimeUnit.DAYS.convert(now.getTime() - lastUpdated.getTime(), TimeUnit.MILLISECONDS);

                Log.d(LOG_TAG, currentExercise.getName() + ". Days passed since last update : " + daysPassed);

                // Check if last updated date is more than the specified number of days in preferences
                if (daysPassed >= maxDaysInteger && lastUpdated != null) {
                    weightUpWarningTextView.setText(getContext().getString(R.string.item_warning_message,daysPassed));
                } else {
                    weightUpWarningTextView.setText("");
                }

            } else {
                Log.d(LOG_TAG, "Not enough data for date comparison. " + currentExercise.getName() + " values : " + currentExercise.toString());
                weightUpWarningTextView.setText("");
            }


        } else {
            Log.d(LOG_TAG, "Suppress days overdue message");
        }


        return listItemView;
    }
}

package com.aimtech.android.repsforjesus.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aimtech.android.repsforjesus.Model.Exercise;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.Utility;

import java.util.List;

/**
 * Created by Andy on 30/07/2016.
 */
public class ExerciseListAdapter extends ArrayAdapter<Exercise> {

    public ExerciseListAdapter(Context context, List<Exercise> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            // Layout inflater turns an xml file into actual View objects
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_list_item, parent, false);
        }

        // Hook up views in the layout
        TextView exerciseNameTextView = (TextView) listItemView.findViewById(R.id.exerciseNameTextView);
        TextView currentWeightTextView = (TextView) listItemView.findViewById(R.id.currentWeightTextView);
        TextView previouslyTextView = (TextView) listItemView.findViewById(R.id.previouslyTextView);

        // Display Data
        Exercise currentExercise = getItem(position);
        exerciseNameTextView.setText(currentExercise.getName());
        currentWeightTextView.setText(String.valueOf(currentExercise.getCurrentWeight()) + " Kg");

        // If any previous data exists, display it


        if (currentExercise.getPreviousWeight() != null && currentExercise.getPreviousWeight() > 0 && currentExercise.getDateLastUpdated() != null) {
            // Format the Date
            String formattedDate = Utility.formatDateToString(currentExercise.getDateLastUpdated());

            previouslyTextView.setText("Previously " + currentExercise.getPreviousWeight() + " Kg on " + formattedDate);
        } else {
            Log.d("Previous Weight Null", "No Data for " + currentExercise.getName());
        }


        return listItemView;
    }
}

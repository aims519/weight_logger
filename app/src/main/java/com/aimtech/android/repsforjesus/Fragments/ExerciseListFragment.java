package com.aimtech.android.repsforjesus.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aimtech.android.repsforjesus.Adapters.ExerciseListAdapter;
import com.aimtech.android.repsforjesus.Dialogs.DeleteExerciseDialog;
import com.aimtech.android.repsforjesus.Dialogs.EditWeightDialog;
import com.aimtech.android.repsforjesus.Model.Exercise;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;
import com.aimtech.android.repsforjesus.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andy on 02/08/2016.
 */
public class ExerciseListFragment extends Fragment implements EditWeightDialog.EditWeightDialogListener {

    private final String LOG_TAG = ExerciseListFragment.class.getSimpleName();

    // variables for the list of exercises and the ListAdapter
    private ArrayList<Exercise> mExerciseList;
    private ExerciseListAdapter adapter;

    // Instance of SQL database helper
    ExerciseDatabaseHelper mDbHelper;

    // Exercise category, obtained from the Fragment arguments onCreateView
    private String mCategory;


    public ExerciseListFragment() {
        // Required empty public constructor
    }

    // New Instance method to set the required category for the fragment (chest,back,arms,legs)
    public static final ExerciseListFragment newInstance(String category){
        ExerciseListFragment newExerciseListFragment = new ExerciseListFragment();

        // store the category in a bundle and set as arguments on the new fragment
        Bundle bundle = new Bundle();
        bundle.putString("bundle_category_string",category);
        newExerciseListFragment.setArguments(bundle);

        return newExerciseListFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Grab the arguments from the newly created Fragment, and set the string variable
        mCategory = this.getArguments().getString("bundle_category_string");

        // Inflate the layout from XML file
        View rootView = inflater.inflate(R.layout.fragment_exercise_generic, container, false);

        // Set up the arrayList before query
        mExerciseList = new ArrayList<Exercise>();

        // Get a readable database
        mDbHelper = new ExerciseDatabaseHelper(getActivity());


        // Reset the database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //mDbHelper.onUpgrade(db,mDbHelper.DATABASE_VERSION,mDbHelper.DATABASE_VERSION);
        //mDbHelper.onCreate(db);

        //Read the list of exercises from the database and put them into an arrayList
        // Clears the array list first
        refreshExerciseList();

        // Set up adapter
        adapter = new ExerciseListAdapter(getContext(), mExerciseList);

        // Hook up ListView to adapter
        ListView exerciseListView = (ListView) rootView.findViewById(R.id.listViewExercisesGeneric);
        exerciseListView.setAdapter(adapter);


        // Set on long click listener for list items
        exerciseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Display the dialog
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();

                // Get name of current item to use in title
                Exercise currentItem = (Exercise) adapterView.getItemAtPosition(i);
                String dialogTitle = currentItem.getName();

                EditWeightDialog dialogFragment = EditWeightDialog.newInstance(dialogTitle);

                dialogFragment.setTargetFragment(ExerciseListFragment.this, 0);
                dialogFragment.show(fm, "fragment_dialog_edit_weight");

                return false;
            }
        });


        return rootView;
    }



    // Define what happens on save of a new weight. Method from dialog interface
    @Override
    public void onSaveNewWeight(String exerciseName, String newWeight) {

        // Get the current weight so that it can be transferred to previous weight
        String currentWeight = getCurrentWeightForExercise(exerciseName);

        // Get today's date
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String formattedDate = Utility.formatDateToString(now);

        // New values to put in to database. Only do this if dialog editText is not blank
        ContentValues values = new ContentValues();
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT, newWeight);
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT, currentWeight);
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_DATE_LAST_UPDATED, formattedDate);

        // Define selection so that only the correct exercise is updated
        String selection = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{exerciseName};

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Update
        db.update(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();

        // Refresh the ArrayList with the new data
        refreshExerciseList();

        // Notify adapter of change to data
        adapter.notifyDataSetChanged();

    }

    // Define what happens when the user deletes an exercise. Method from dialog interface
    @Override
    public void onExerciseDelete(String exerciseToDelete) {
        Log.d("Delete button","User has selected to delete an exercise :" + exerciseToDelete);
        // SHow the dialog to ask user to confirm
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        DeleteExerciseDialog dialogFragment = DeleteExerciseDialog.newInstance("Warning!",exerciseToDelete);

        dialogFragment.setTargetFragment(ExerciseListFragment.this, 100);
        dialogFragment.show(fm, "fragment_dialog_delete_exercise_warning");

    }

    public void refreshExerciseList() {
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Reset the arrayList
        mExerciseList.clear();

        //Define a projection (columns to use for query)
        String[] projection = {
                ExerciseDatabaseContract.ExerciseTable._ID,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_DATE_LAST_UPDATED
        };

        //Define a sort order. Read from preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrderPref = prefs.getString(getString(R.string.pref_sort_order_key),getString(R.string.pref_sort_order_default));
        String sortOrder;
        switch (sortOrderPref){
            case "alphabetical":
                sortOrder = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " ASC";
                break;
            case "order_entered":
                sortOrder = ExerciseDatabaseContract.ExerciseTable._ID + " ASC";
                break;
            default:
                sortOrder = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " ASC";
                break;
        }


        // Define a query, i.e return only rows with the specified category
        String selection = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY + " = ?";
        String[] selectionArgs = new String[]{mCategory};

        try {
            // Query the database and return the results in a cursor
            Cursor c = db.query(
                    ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            int idIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable._ID);
            int nameIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME);
            int cateroryIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY);
            int currentWeightIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT);
            int previousWeightIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT);
            int dateLastUpdatedIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_DATE_LAST_UPDATED);

            // print the results of the query
            c.moveToFirst();

            // Create an Exercise Object for each row returned in the query and add to arrayList
            while (c != null) {
                Log.i(LOG_TAG, "Load " + mCategory + " exercises. Cursor output : " + c.getString(idIndex) + ", " + c.getString(nameIndex) + ", " + c.getString(cateroryIndex) + ", " + c.getString(currentWeightIndex) + ", " + c.getString(previousWeightIndex) + ", " + c.getString(dateLastUpdatedIndex));

                // Deal with possible null values
                Double previousWeight;
                Date dateLastUpdated;
                if (c.getString(previousWeightIndex) != null) {
                    previousWeight = Double.parseDouble(c.getString(previousWeightIndex));
                } else {
                    previousWeight = null;
                }

                if (c.getString(dateLastUpdatedIndex) != null) {
                    // Parse String to Date
                    dateLastUpdated = Utility.parseStringToDate(c.getString(dateLastUpdatedIndex));
                } else {
                    dateLastUpdated = null;
                }

                mExerciseList.add(new Exercise(c.getString(nameIndex), Double.parseDouble(c.getString(currentWeightIndex)), previousWeight, dateLastUpdated));
                c.moveToNext();
            }

            // Close the cursor
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        // CLose the database
        db.close();

    }

    private String getCurrentWeightForExercise(String exerciseName) {
        String currentWeight = "";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define a projection (columns to use for query)
        String[] projection = {
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,
        };

        //Define a sort order
        String sortOrder = ExerciseDatabaseContract.ExerciseTable._ID + " ASC";

        // Define a query, i.e return only rows with the specified category
        String selection = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = new String[]{exerciseName};

        try {
            // Query the database and return the results in a cursor
            Cursor c = db.query(
                    ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            int currentWeightIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT);

            // print the results of the query
            c.moveToFirst();

            // Create an Exercise Object for each row returned in the query and add to arrayList
            while (c != null) {
                Log.i("Current Weight Returned", c.getString(currentWeightIndex));
                currentWeight = c.getString(currentWeightIndex);
                c.moveToNext();
            }

            // Close the cursor
            c.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        // CLose the database
        db.close();

        return currentWeight;


    }


}

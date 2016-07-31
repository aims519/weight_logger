package com.aimtech.android.repsforjesus.Fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aimtech.android.repsforjesus.Adapters.ExerciseListAdapter;
import com.aimtech.android.repsforjesus.Model.Exercise;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChestFragment extends Fragment {

    private ArrayList<Exercise> mExerciseList;
    private ExerciseListAdapter adapter;

    // Instance of SQL database helper
    ExerciseDatabaseHelper mDbHelper;


    public ChestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout from XML file
        View rootView = inflater.inflate(R.layout.fragment_chest,container,false);

        // Set up the list of Chest exercises
        mExerciseList = new ArrayList<Exercise>();
        mExerciseList.add(new Exercise("Bench Press",47.5,null,null));
        mExerciseList.add(new Exercise("Bench Press (Incline)",40.0,null,null));
        mExerciseList.add(new Exercise("Pectoral Fly",54.0,null,null));
        mExerciseList.add(new Exercise("Chest Press",45.0,null,null));

        // Set up adapter
        adapter = new ExerciseListAdapter(getContext(),mExerciseList);

        // Hook up ListView to adapter
        ListView exerciseListView = (ListView) rootView.findViewById(R.id.listViewChest);
        exerciseListView.setAdapter(adapter);

        // Get a writable database
        mDbHelper = new ExerciseDatabaseHelper(getActivity());


        // Insert/Query the database
        //insertDbTest();
        queryDbTest();


        return rootView;
    }

    public void queryDbTest(){
        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define a projection (columns to use for query)
        String[] projection = {
                ExerciseDatabaseContract.ExerciseTable._ID,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT,
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_DATE_LAST_UPDATED
        };

        //Define a sort order
        String sortOrder = ExerciseDatabaseContract.ExerciseTable._ID + " ASC";

        try{
            // Query the database and return the results in a cursor
            Cursor c = db.query(
                    ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                    projection,
                    null,
                    null,
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

            while ( c != null) {
                Log.i("Cursor Output", c.getString(idIndex) + ", " + c.getString(nameIndex) + ", " + c.getString(cateroryIndex) + ", " + c.getString(currentWeightIndex)+ ", " + c.getString(previousWeightIndex)+ ", " + c.getString(dateLastUpdatedIndex));
                c.moveToNext();
            }

            // Close the cursor
            c.close();




        }catch (Exception e){
            e.printStackTrace();
        }

        // CLose the database
        db.close();

    }

    public void insertDbTest(){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // INsert some content
        ContentValues values = new ContentValues();
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,"Bench Press (Incline)");
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY,"chest");
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,"40.0");

        //Insert() returns the primary key value of the new row
        long newRowId=db.insert(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,null,values);
        Log.i("Database Updated", "New Row ID : " + newRowId);

        // Close the database
        db.close();

    }

}

package com.aimtech.android.repsforjesus.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aimtech.android.repsforjesus.Adapters.ExerciseListAdapter;
import com.aimtech.android.repsforjesus.Model.Exercise;
import com.aimtech.android.repsforjesus.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChestFragment extends Fragment {

    private ArrayList<Exercise> mExerciseList;
    private ExerciseListAdapter adapter;


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

        //TODO Add the above to the database if they don't already exist

        return rootView;
    }

}

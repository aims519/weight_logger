package com.aimtech.android.repsforjesus.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aimtech.android.repsforjesus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArmsFragment extends Fragment {


    public ArmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_arms, container, false);
    }

}

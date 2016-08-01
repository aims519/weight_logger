package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 01/08/2016.
 */
public class NewExerciseDialog extends DialogFragment {

    // Empty Constructor
    public NewExerciseDialog() {}

    // Initialise Method
    public static NewExerciseDialog newInstance(String title){
        NewExerciseDialog frag = new NewExerciseDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Set the custom layout
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_new_exercise,null);



        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO save the new exercise to the database
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If user clicks cancel, do nothing
                dialogInterface.dismiss();
            }
        });

        // Set spinner options using an adapter
        Spinner spinner = (Spinner) view.findViewById(R.id.newExerciseCategorySpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.exercise_categories,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);




        // Make sure that the keyboard is shown when the dialog is created
        alertDialogBuilder.setView(view);
        AlertDialog newDialog = alertDialogBuilder.create();




        newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return newDialog;
    }

}

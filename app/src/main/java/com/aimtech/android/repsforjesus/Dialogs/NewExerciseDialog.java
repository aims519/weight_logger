package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 01/08/2016.
 */
public class NewExerciseDialog extends DialogFragment {

    private NewExerciseListener mListener;

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

        // Set spinner options using an adapter
        final Spinner spinner = (Spinner) view.findViewById(R.id.newExerciseCategorySpinner);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.exercise_categories,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(spinnerAdapter);


        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final EditText newExerciseNameEditText = (EditText) view.findViewById(R.id.newExerciseNameEditText);
                final EditText startingWeightEditText = (EditText) view.findViewById(R.id.startingWeightEditText);

                // Callback to MainActivity
                mListener.onSaveNewExercise(newExerciseNameEditText.getText().toString(),spinner.getSelectedItem().toString(),startingWeightEditText.getText().toString());
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If user clicks cancel, do nothing
                dialogInterface.dismiss();
            }
        });






        // Make sure that the keyboard is shown when the dialog is created
        alertDialogBuilder.setView(view);
        AlertDialog newDialog = alertDialogBuilder.create();




        newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return newDialog;
    }

    public interface NewExerciseListener{
        void onSaveNewExercise(String name,String category,String startingWeight);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // verify that the host activity implements the callback interface
        try {
            mListener = (NewExerciseListener) getActivity();
        } catch (ClassCastException e) {
            //The activity/fragment doesn't implement the interface. Throw exception
            throw new ClassCastException(context.toString() + " must implement NewExerciseListener");
        }
    }
}

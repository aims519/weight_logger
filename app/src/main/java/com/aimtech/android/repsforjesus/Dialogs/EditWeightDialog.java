package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 31/07/2016.
 */
public class EditWeightDialog extends DialogFragment {

    private EditText mNewWeightEditText;

    private EditWeightDialogListener mListener;

    private SharedPreferences mPrefs;

    // Required empty constructor
    public EditWeightDialog() {
    }

    public static EditWeightDialog newInstance(String title) {
        EditWeightDialog frag = new EditWeightDialog();
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
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_edit_weight, null);

        // Hook up delete button
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteExerciseButton);
        //Hook up EditText
        final EditText newWeightEditText = (EditText) view.findViewById(R.id.newWeightText);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Dismiss the dialog
                getDialog().dismiss();
                // Callback. Pass the name of the exercise to the fragment so that it knows which to delete
                // This is the title of the current dialog
                mListener.onExerciseDelete(title);
            }
        });


        // Display title bar instead of plain text
        TextView titleTextView = (TextView) view.findViewById(R.id.edit_weight_title_text_view);
        titleTextView.setText(title);

        // Set unit text by reading value from shared preferences
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String unitPref = mPrefs.getString(getContext().getString(R.string.pref_units_key),getContext().getString(R.string.pref_unit_default));
        TextView unitTextView = (TextView) view.findViewById(R.id.edit_weight_unit_textview);

        if(unitPref.equals(getContext().getString(R.string.units_kilograms))){
            unitTextView.setText(getString(R.string.units_kilograms));
        } else if (unitPref.equals(getContext().getString(R.string.units_pounds))){
            unitTextView.setText(getString(R.string.units_pounds));
        }


        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // CHeck to make sure a weight has been entered
                String newWeightEntered = newWeightEditText.getText().toString();
                if (newWeightEntered.isEmpty()) {
                    Toast.makeText(getContext(), "Invalid. Please enter a weight.", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(newWeightEntered) < 0) {
                    Toast.makeText(getContext(), "Invalid. Mass must be positive due to physics.", Toast.LENGTH_SHORT).show();
                } else {
                    // Send the positive button event back to the host fragment
                    Dialog f = (Dialog) dialogInterface;
                    mNewWeightEditText = (EditText) f.findViewById(R.id.newWeightText);
                    mListener.onSaveNewWeight(title, mNewWeightEditText.getText().toString());
                    getDialog().dismiss();
                }


            }
        });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If user clicks cancel, do nothing
                dialogInterface.dismiss();
            }
        });

        alertDialogBuilder.setView(view);
        // Make sure that the keyboard is shown when the dialog is created
        AlertDialog newDialog = alertDialogBuilder.create();
        newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return newDialog;
    }


    // Define the listener interface. This must be implemented in any activity/fragment that wants to receive data from the
    // AlertDialog. Sends a callback if a new weight is being saved, or an exercise is being deleted.
    public interface EditWeightDialogListener {
        void onSaveNewWeight(String exerciseName, String newWeight);

        void onExerciseDelete(String exerciseToDelete);
    }

    // Override the Fragment.onAttach method to instantiate the EditWeightDialogListener
    // onAttach is called immediately when a fragment becomes associated with an activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // verify that the host activity implements the interface so that callbacks can be recieved.
        try {
            mListener = (EditWeightDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            //The activity/fragment doesn't implement the interface. Throw exception
            throw new ClassCastException(context.toString() + " must implement EditWeightDialogListener");
        }
    }
}

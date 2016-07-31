package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.EditText;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 31/07/2016.
 */
public class EditWeightDialog extends DialogFragment {

    EditText mNewWeightEditText;

    EditWeightDialogListener mListener;

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
        alertDialogBuilder.setView(R.layout.fragment_edit_weight);



        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Send the positive button event back to the host fragment
                Dialog f = (Dialog) dialogInterface;
                mNewWeightEditText = (EditText) f.findViewById(R.id.newWeightText);
                mListener.onSaveNewWeight(title,mNewWeightEditText.getText().toString());
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
        AlertDialog newDialog = alertDialogBuilder.create();
        newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return newDialog;
    }

    // Define the listener interface. This must be implemented in any activity/fragment that wants to receive data from the
    // AlertDialog
    public interface EditWeightDialogListener{
        void onSaveNewWeight(String exerciseName,String newWeight);
    }

    // Override the Fragment.onAttach method to instantiate the EditWeightDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // verify that the host activity implements the callback interface
        try{
            mListener = (EditWeightDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            //The activity/fragment doesn't implement the interface. Throw exception
            throw new ClassCastException(context.toString() + " must implement EditWeightDialogListener");
        }
    }
}

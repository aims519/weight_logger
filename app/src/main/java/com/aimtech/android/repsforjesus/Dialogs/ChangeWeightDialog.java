package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.aimtech.android.repsforjesus.R;

/**
 * Created by Andy on 31/07/2016.
 */
public class ChangeWeightDialog extends DialogFragment {

    // Required empty constructor
    public ChangeWeightDialog() {
    }

    public static ChangeWeightDialog newInstance(String title) {
        ChangeWeightDialog frag = new ChangeWeightDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    /*
    // Inflate the custom dialog layout
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_weight,container,false);
    }
    */


    /*
    // Request focus to the EditText once the dialog is created, set title etc
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get edit text
        EditText editWeight = (EditText) view.findViewById(R.id.newWeightText);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        // SHow keyboard automatically and request focus
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        );
        editWeight.requestFocus();
    }
    */


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Set the custom layout
        alertDialogBuilder.setView(R.layout.fragment_edit_weight);


        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO save new weight to the database
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If user clicks cancel
                dialogInterface.dismiss();
            }
        });

        // Make sure that the keyboard is shown when the dialog is created
        AlertDialog newDialog = alertDialogBuilder.create();
        newDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return newDialog;
    }

}

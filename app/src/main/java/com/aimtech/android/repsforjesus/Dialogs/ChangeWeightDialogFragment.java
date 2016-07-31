package com.aimtech.android.repsforjesus.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Andy on 31/07/2016.
 */
public class ChangeWeightDialogFragment extends DialogFragment {

    // Required empty constructor
    public ChangeWeightDialogFragment() {
    }

    public static ChangeWeightDialogFragment newInstance(String title) {
        ChangeWeightDialogFragment frag = new ChangeWeightDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Change Weight");
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // if user clicks save
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // If user clicks cancel
                dialogInterface.dismiss();
            }
        });

        return alertDialogBuilder.create();




    }
}

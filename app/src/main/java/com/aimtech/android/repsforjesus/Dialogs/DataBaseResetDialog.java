package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Andy on 31/07/2016.
 */
public class DataBaseResetDialog extends DialogFragment{

    DatabaseResetListener mListener;

    //Required empty constructor
    public DataBaseResetDialog() {
    }

    public static DataBaseResetDialog newInstance(String title){
        DataBaseResetDialog frag = new DataBaseResetDialog();
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

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("This will permenantly erase all weight data. Are you sure you want to reset?");

        alertDialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Notifies main activity that a reset has been requested
                mListener.onDatabaseReset();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        });

        AlertDialog newDialog=  alertDialogBuilder.create();
        return newDialog;
    }

    public interface DatabaseResetListener {
        void onDatabaseReset();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // verify that the host activity implements the callback interface
        try{
            mListener = (DatabaseResetListener) getActivity();
        } catch (ClassCastException e) {
            //The activity/fragment doesn't implement the interface. Throw exception
            throw new ClassCastException(context.toString() + " must implement DatabaseResetListener");
        }
    }
}

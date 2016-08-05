package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.aimtech.android.repsforjesus.Activities.MainActivity;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

/**
 * Created by Andy on 01/08/2016.
 */
public class DeleteExerciseDialog extends DialogFragment {

    //Required empty constructor
    public DeleteExerciseDialog() {
    }

    public static DeleteExerciseDialog newInstance(String title, String exerciseName) {
        DeleteExerciseDialog frag = new DeleteExerciseDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("exerciseToDelete",exerciseName);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        final String exerciseToDelete = getArguments().getString("exerciseToDelete");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_warning_black_36dp);
        alertDialogBuilder.setMessage("Are you sure you want to delete the '" + exerciseToDelete + "' exercise?");

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get database
                ExerciseDatabaseHelper databaseHelper = new ExerciseDatabaseHelper(getContext());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                // WHERE clause
                String selection = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " = ?";
                String[] selectionArgs = new String[]{exerciseToDelete};

                db.delete(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                        selection,
                        selectionArgs);

                // Clean up
                db.close();
                databaseHelper.close();

                //Update the UI immediately
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.refreshUI();

                // Notify the user that the delete was successful
                Toast.makeText(getContext(),"'" + exerciseToDelete + "' has been deleted successfully",Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismiss as normal
            }
        });

        return alertDialogBuilder.create();
    }

}

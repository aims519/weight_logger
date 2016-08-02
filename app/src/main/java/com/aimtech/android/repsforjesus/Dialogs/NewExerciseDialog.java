package com.aimtech.android.repsforjesus.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

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


        //alertDialogBuilder.setTitle(title);
        TextView titleTextView = (TextView) view.findViewById(R.id.new_exercise_title_text_view);
        titleTextView.setText(title);

        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            // used to flag up a duplicate exercise when addding a new one
            Boolean existingResultFound;

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final EditText newExerciseNameEditText = (EditText) view.findViewById(R.id.newExerciseNameEditText);
                final EditText startingWeightEditText = (EditText) view.findViewById(R.id.startingWeightEditText);

                // Strings for Validating Input
                String newExerciseName = newExerciseNameEditText.getText().toString();
                String newExerciseStartingWeight = startingWeightEditText.getText().toString();

                // If nothing entered for a starting weight, default to zero
                if(newExerciseStartingWeight.isEmpty()){
                    newExerciseStartingWeight = "0.0";
                }

                //Database query to make sure new exercise doesn't exist yet
                ExerciseDatabaseHelper databaseHelper = new ExerciseDatabaseHelper(getContext());
                SQLiteDatabase db = databaseHelper.getReadableDatabase();

                //Define a projection (columns to use for query)
                String[] projection = {
                        ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,
                };

                //Define a sort order
                String sortOrder = ExerciseDatabaseContract.ExerciseTable._ID + " ASC";

                // Define a query, i.e return only rows with the name of the new exercise
                String selection = ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME+ " LIKE ?";
                String[] selectionArgs = new String[]{newExerciseName};

                try {
                    // Query the database and return the results in a cursor
                    Cursor c = db.query(
                            ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );

                    int nameIndex = c.getColumnIndex(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME);

                    // This will return false if the cursor is empty
                    existingResultFound = c.moveToFirst();

                    // Close the cursor
                    c.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }


                // Display a message to the user if data is not valid
                if(newExerciseName.isEmpty()){

                    Toast.makeText(getContext(),"Invalid. The exercise must have a name.",Toast.LENGTH_SHORT).show();

                } else if(existingResultFound){

                    // A duplicate has been found in the query above, notify the user
                    Toast.makeText(getContext(),"An exercise called '" + newExerciseName + "' already exists!",Toast.LENGTH_LONG).show();
                } else {

                    // Proceed with addition of exercise
                    // Callback to MainActivity
                    mListener.onSaveNewExercise(newExerciseName,spinner.getSelectedItem().toString(),newExerciseStartingWeight.toString());
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

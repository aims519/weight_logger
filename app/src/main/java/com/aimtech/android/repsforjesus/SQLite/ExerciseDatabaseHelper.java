package com.aimtech.android.repsforjesus.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Andy on 31/07/2016.
 */
public class ExerciseDatabaseHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ExerciseDb.db";

    // master list of default CHEST Exercises and categories
    String[] masterListChest = new String[]{
            "Bench Press",
            "Bench Press (Incline)",
            "Close Grip",
            "Pectoral Fly",
            "Peck Fly (Easy)",
            "Converging Chest Press"
    };

    // Master List of default BACK Exercises
    String[] masterListBack = new String[]{
            "Diverging Seated Row",
            "Face",
            "Sit Down Pull Down",
    };

    // Default Constructor
    public ExerciseDatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    // SQL code to create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + ExerciseDatabaseContract.ExerciseTable.TABLE_NAME + " (" +
                ExerciseDatabaseContract.ExerciseTable._ID + " INTEGER PRIMARY KEY, " +
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME + " TEXT," +
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY + " TEXT," +
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT + " TEXT," +
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_PREVIOUS_WEIGHT + " TEXT," +
                ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_DATE_LAST_UPDATED + " TEXT" +
                " )";
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

        // Insert the defualt lines into the newly created database
        insertDefaultLines(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // If upgraded, just delete the database and start again
        final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ExerciseDatabaseContract.ExerciseTable.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }


    // function to initialise all the default exercies with 0 current weights to start with
    private void insertDefaultLines(SQLiteDatabase db){

        // Insert default chest exercises
        for(int i =0;i<masterListChest.length;i++){
            ContentValues values = new ContentValues();
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,masterListChest[i]);
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "chest");
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,"0.0");

            //Insert() returns the primary key value of the new row
            long newRowId=db.insert(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,null,values);
            Log.i("Database Updated", "New Row ID : " + newRowId);
        }

        // Insert default Back exercises
        for(int i =0;i<masterListBack.length;i++){
            ContentValues values = new ContentValues();
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,masterListBack[i]);
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "back");
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT,"0.0");

            //Insert() returns the primary key value of the new row
            long newRowId=db.insert(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME,null,values);
            Log.i("Database Updated", "New Row ID : " + newRowId);
        }
    }



}

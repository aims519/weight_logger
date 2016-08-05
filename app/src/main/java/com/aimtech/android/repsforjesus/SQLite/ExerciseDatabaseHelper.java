package com.aimtech.android.repsforjesus.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andy on 31/07/2016.
 */
public class ExerciseDatabaseHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ExerciseDb.db";


    // Default Constructor
    public ExerciseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // If upgraded, just delete the database and start again
        final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ExerciseDatabaseContract.ExerciseTable.TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }




}

package com.aimtech.android.repsforjesus.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Andy on 31/07/2016.
 */
public class ExerciseDatabaseContract {

    // TO prevent accidentally instantiating the contract class, give it an empty constructor
    public ExerciseDatabaseContract(){};

    // Inner class that defines contents of Exercise table
    public static abstract class ExerciseTable implements BaseColumns{
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_CURRENT_WEIGHT = "current_weight";
        public static final String COLUMN_NAME_PREVIOUS_WEIGHT = "previous_weight";
        public static final String COLUMN_NAME_DATE_LAST_UPDATED = "date_last_updated";

    }

}

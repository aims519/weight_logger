package com.aimtech.android.repsforjesus.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aimtech.android.repsforjesus.Adapters.CategoryPagerAdapter;
import com.aimtech.android.repsforjesus.Dialogs.DataBaseResetDialog;
import com.aimtech.android.repsforjesus.Dialogs.NewExerciseDialog;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseContract;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

public class MainActivity extends AppCompatActivity implements DataBaseResetDialog.DatabaseResetListener,NewExerciseDialog.NewExerciseListener,SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private CategoryPagerAdapter mPagerAdapter;
    private ExerciseDatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load layout for MainActivity
        setContentView(R.layout.activity_main);

        // Register OnSharedPreferenceChance Listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("   Weight Log");


        // Load default preferences (Once only when the app is first installed)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);


        // Hook up ViewPager with adapter
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mViewPager.setOffscreenPageLimit(1);
        mPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(mPagerAdapter);
        // Always create Mainactivity with the first fragment selected in the ViewPager
        mViewPager.setCurrentItem(0);

        // Set up tab layout and link to the viewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

    }

    // OnResumeCallback to refresh data after preferences have been changed
    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    //Inflate the main menu from the menu_main.xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    // Respond to menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm;

        switch (item.getItemId()) {
            case R.id.menu_main_reset:
                // Open the DatabaseReset dialog fragment
                fm = getSupportFragmentManager();
                DataBaseResetDialog dialogFragment = DataBaseResetDialog.newInstance("Warning!");
                dialogFragment.show(fm, "database_reset_dialog");
                break;
            case R.id.main_menu_add_exercise:
                // Display the NewExercise dialog
                fm = getSupportFragmentManager();

                // Get the current category being viewed
                String currentCategory = mPagerAdapter.getPageTitle(mViewPager.getCurrentItem()).toString();

                NewExerciseDialog newExerciseDialogFragment = NewExerciseDialog.newInstance("New Exercise",currentCategory);
                newExerciseDialogFragment.show(fm, "fragment_dialog_new_exercise");

                //Callback from NewExerciseDialog will update the database if a positive response is received
                break;
            case R.id.main_menu_settings:
                // Launch the SettingsActivity
                Intent toSettings = new Intent(this,SettingsActivity.class);
                startActivity(toSettings);

            default:
                // Do nothing
                break;

        }
        return true;
    }

    // Callback for database reset (sent from DatabaseResetDialog, picked up by DatabaseResetListener)
    @Override
    public void onDatabaseReset() {
        //Reset the database if dialog response was positive
        databaseHelper = new ExerciseDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Reset the database by calling the onUpgrade method. Keep the version the same
        databaseHelper.onUpgrade(db, ExerciseDatabaseHelper.DATABASE_VERSION, ExerciseDatabaseHelper.DATABASE_VERSION);

        // Create the database afresh
        databaseHelper.onCreate(db);
        db.close();
        databaseHelper.close();

        //Refresh fragment UI
        // notifyDataSetChanged calls getItemPosition in the CategoryPagerAdapterClass
        // This always returns POSITION_NONE, which refreshes the fragment
        mPagerAdapter.notifyDataSetChanged();
    }

    // Callback for new exercise (Sent from NewExercise Dialog, picked up by NewExercise listener)
    @Override
    public void onSaveNewExercise(String name, String category, String startingWeight) {
        // Update the database
        databaseHelper = new ExerciseDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_NAME,name);

        // Choose correct category
        if(category.equals(getString(R.string.spinner_chest))){
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "chest");

        } else if (category.equals(getString(R.string.spinner_back))){
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "back");

        }else if (category.equals(getString(R.string.spinner_arms))){
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "arms");

        }else if (category.equals(getString(R.string.spinner_legs))){
            values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CATEGORY, "legs");
        }


        values.put(ExerciseDatabaseContract.ExerciseTable.COLUMN_NAME_CURRENT_WEIGHT, startingWeight);

        // Insert the new exercise into the database
        db.insert(ExerciseDatabaseContract.ExerciseTable.TABLE_NAME, null, values);
        Log.d(LOG_TAG,name + " exercise was successfully added to the database");

        // Notify the user that item has been successfully added using a Toast message
        Toast.makeText(this,"'" + name + "' exercise successfully added",Toast.LENGTH_SHORT).show();


        // Cleanup by closing the database and instance of ExerciseDatabaseHelper
        db.close();
        databaseHelper.close();

        //Refresh fragment UI
        // notifyDataSetChanged calls getItemPosition in the CategoryPagerAdapterClass
        // This always returns POSITION_NONE, which leads to a new instance of the fragment being created
        mPagerAdapter.notifyDataSetChanged();


    }

    // Function to refresh UI. Called by the DeleteExercise dialog after an item has been deleted
    public void refreshUI(){
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // If the sort order preference is changed, immediately refresh the UI
    }
}

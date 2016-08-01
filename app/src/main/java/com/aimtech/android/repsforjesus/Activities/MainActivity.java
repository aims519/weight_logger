package com.aimtech.android.repsforjesus.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aimtech.android.repsforjesus.Adapters.CategoryPagerAdapter;
import com.aimtech.android.repsforjesus.Dialogs.DataBaseResetDialog;
import com.aimtech.android.repsforjesus.R;
import com.aimtech.android.repsforjesus.SQLite.ExerciseDatabaseHelper;

public class MainActivity extends AppCompatActivity implements DataBaseResetDialog.DatabaseResetListener {

    private ViewPager mViewPager;
    private CategoryPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        // Hook up ViewPager with adapter
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        mPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        // Set up tab layout and link to the viewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    //Inflate the main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    // Respond to menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_reset:
                FragmentManager fm = getSupportFragmentManager();
                DataBaseResetDialog dialogFragment = DataBaseResetDialog.newInstance("Warning!");
                dialogFragment.show(fm, "database_reset_dialog");
                break;
            default:
                // Do nothing
                break;

        }
        return true;
    }

    // Callback for database reset (from Dialog)


    @Override
    public void onDatabaseReset() {
        //Reset the database if dialog response was positive
        ExerciseDatabaseHelper databaseHelper = new ExerciseDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Reset the database by calling the onUpgrade method. Keep the version the same
        databaseHelper.onUpgrade(db, ExerciseDatabaseHelper.DATABASE_VERSION, ExerciseDatabaseHelper.DATABASE_VERSION);

        // Create the database afresh
        databaseHelper.onCreate(db);
        db.close();

        //Refresh fragment UI
        // notifyDataSetChanged calls getItemPosition in the CategoryPagerAdapterClass
        // This always returns POSITION_NONE, which refreshes the fragment
        mPagerAdapter.notifyDataSetChanged();
    }
}

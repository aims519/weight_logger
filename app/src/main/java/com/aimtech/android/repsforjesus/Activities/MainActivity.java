package com.aimtech.android.repsforjesus.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aimtech.android.repsforjesus.Adapters.CategoryPagerAdapter;
import com.aimtech.android.repsforjesus.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        // Hook up ViewPager with adapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        CategoryPagerAdapter adapter = new CategoryPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        // Set up tab layout and link to the viewpager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}

package com.example.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.example.bakingapp.adapter.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager tmpPager = findViewById(R.id.pager);
        TabPagerAdapter tmpPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tmpPager.setAdapter(tmpPagerAdapter);

        TabLayout tmpTabLayout = findViewById(R.id.sliding_tabs);
        tmpTabLayout.setupWithViewPager(tmpPager);
    }
}

package com.mts.cocktailbuilder;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.mts.cocktailbuilder.fragments.MakeListFragment;
import com.mts.cocktailbuilder.mybar.MyBarFragment;
import com.mts.cocktailbuilder.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity  implements MyBarFragment.BarChangedNotifier {
    private TabLayout tabs;
    private ViewPager viewPager;
    private SectionsPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        adapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

    @Override
    public void onBarChange() {
        String tag = "android:switcher:" + R.id.view_pager + ":" +  1;
        MakeListFragment makeFrag = (MakeListFragment) getSupportFragmentManager().findFragmentByTag(tag);
        makeFrag.updateList();
    }
}
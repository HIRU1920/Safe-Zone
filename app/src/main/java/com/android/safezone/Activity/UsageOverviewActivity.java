package com.android.safezone.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.safezone.Adapter.ViewPagerAdapter;
import com.android.safezone.Fragment.AppLaunchesFragment;
import com.android.safezone.Fragment.InternetUsageFragment;
import com.android.safezone.Fragment.UsageOverviewFragment;
import com.android.safezone.R;
import com.google.android.material.tabs.TabLayout;

public class UsageOverviewActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_overview);

        TabLayout tabLayout = findViewById(R.id.stats_tab_layout);
        ViewPager viewPager = findViewById(R.id.stats_fragment_container);

        UsageOverviewFragment usageOverviewFragment = new UsageOverviewFragment();
        AppLaunchesFragment appLaunchesFragment = new AppLaunchesFragment();
        InternetUsageFragment internetUsageFragment = new InternetUsageFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(usageOverviewFragment, "Usage Overview");
        viewPagerAdapter.addFragment(appLaunchesFragment, "App Launches");
        viewPagerAdapter.addFragment(internetUsageFragment, "Internet Usage");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void finish(View v) {
        finish();
    }
}

package com.example.traffic_sign_detection.stream_and_data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.traffic_sign_detection.R;
import com.google.android.material.tabs.TabLayout;

public class StreamAndDataActivity extends AppCompatActivity {


    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_and_data);

        initViews();

    }

    private void initViews()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new StreamFragment(), "Stream");
        tabAdapter.addFragment(new DataFragment(), "Data");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

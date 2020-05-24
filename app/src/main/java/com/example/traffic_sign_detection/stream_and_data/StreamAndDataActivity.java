package com.example.traffic_sign_detection.stream_and_data;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.traffic_sign_detection.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Map;

public class StreamAndDataActivity extends AppCompatActivity {


    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_and_data);

        initViews();

        String user_email = getIntent().getStringExtra("user_email");
        System.out.println("User email yra" + user_email);

//        Bundle bundle = new Bundle();
//        bundle.putString("ID", "sessionId");
//// set Fragmentclass Arguments
//        MapFragment fragobj = new MapFragment();
//        fragobj.setArguments(bundle);

        Intent mapFragment = new Intent(getBaseContext(), MapFragment.class);
        mapFragment.putExtra("user_email", user_email);

    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new StreamFragment(), "Stream");
        tabAdapter.addFragment(new DataFragment(), "Data");
        tabAdapter.addFragment(new MapFragment(), "Map");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }
}

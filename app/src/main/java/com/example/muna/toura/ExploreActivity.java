package com.example.muna.toura;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class ExploreActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION ) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String [] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }

        // create the TabHost that will create the tabs
        final TabHost mainTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mainTabHost.setup();

        TabHost.TabSpec mainTabSpec = mainTabHost.newTabSpec("First Tab");
        mainTabSpec.setContent(R.id.first_tab);
        mainTabSpec.setIndicator("Explore");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Map");
        mainTabSpec.setContent(R.id.second_tab);
        mainTabSpec.setIndicator("Map");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Profile");
        mainTabSpec.setContent(R.id.third_tab);
        mainTabSpec.setIndicator("Profile");
        mainTabHost.addTab(mainTabSpec);

        mainTabHost.setCurrentTab(0);


        // on click for the Map Tab
        // on click for the Map Tab
        mainTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

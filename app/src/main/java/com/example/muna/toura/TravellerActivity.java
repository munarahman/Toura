package com.example.muna.toura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class TravellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller);

        // create the TabHost that will create the tabs
        final TabHost mainTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mainTabHost.setup();

        TabHost.TabSpec mainTabSpec = mainTabHost.newTabSpec("First Tab");
        mainTabSpec.setContent(R.id.first_tab);
        mainTabSpec.setIndicator("Explore");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Second Tab");
        mainTabSpec.setContent(R.id.second_tab);
        mainTabSpec.setIndicator("Guides");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Third Tab");
        mainTabSpec.setContent(R.id.third_tab);
        mainTabSpec.setIndicator("Map");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Forth Tab");
        mainTabSpec.setContent(R.id.forth_tab);
        mainTabSpec.setIndicator("Profile");
        mainTabHost.addTab(mainTabSpec);

        mainTabHost.setCurrentTab(3);

        // on click for the Map Tab
        mainTabHost.getTabWidget().getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), ExploreActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), TourGuideListActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                Intent intent = new Intent().setClass(getApplicationContext(), PublicSafetyMapActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), TravellerActivity.class);
                startActivity(intent);
            }
        });
    }
}

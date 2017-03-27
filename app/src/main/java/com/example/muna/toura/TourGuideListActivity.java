package com.example.muna.toura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TourGuideListActivity extends AppCompatActivity {

    private ListView tourGuideListView;
    private List<String> tourGuideList = new ArrayList<>(Arrays.asList(
            "Terry", "Mags", "Darsan", "Lindsey", "Marina", "Albert", "Steffi"));
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_list);

        setupTourGuideList();

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
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), TourGuideListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupTourGuideList() {
        tourGuideListView = (ListView) findViewById(R.id.tour_guide_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,
                tourGuideList);
        tourGuideListView.setAdapter(adapter);
        tourGuideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent tourGuideIntent = new Intent(TourGuideListActivity.this, TourGuideActivity.class);
                String tourGuide = tourGuideList.get((int) adapterView.getItemIdAtPosition(i));
                tourGuideIntent.putExtra("tourGuide", tourGuide);
                startActivity(tourGuideIntent);
            }
        });
    }
}

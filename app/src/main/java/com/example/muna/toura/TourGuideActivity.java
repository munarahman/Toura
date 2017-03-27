package com.example.muna.toura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TourGuideActivity extends AppCompatActivity {

    private String tourGuideName;
    private String[] tg1 = {"Terry", "Toronto", "English, Cantonese", "terry@mail.com",
            "Engineer", "Hiking, Bird Watching", "Kensington Market"};
    private String[] tg2 = {"Mags", "Toronto", "English, French, Cantonese", "mags@mail.com",
            "Data Scientist", "Acrobatics, Knitting", "Distillery District"};
    private String[] tg3 = {"Darsan", "Scarborough", "English, Hindi", "darsan@mail.com",
            "Doctor", "Eating chocolate", "Scarborough Bluffs"};
    private String[] tg4 = {"Lindsey", "Toronto", "English", "lindsey@mail.com",
            "Game Developer", "Kick Boxing, Music Remixes", "Centre Island"};
    private String[] tg5 = {"Marina", "Mississauga", "English, Arabic, French, Italian", "marina@mail.com",
            "Engineer", "Languages", "Gay Village"};
    private String[] tg6 = {"Albert", "North York", "English, Indonesian", "albert@mail.com",
            "Data Scientist", "Data visualization", "Pacific Mall"};
    private String[] tg7 = {"Steffi", "Toronto", "English, Malay, Mandarin, Spanish", "steffi@mail.com",
            "Engineer", "Yoga", "The Junction"};

    private Map<String, String[]> tourGuides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide);

        tourGuideName = (String) getIntent().getSerializableExtra("tourGuide");
        ((TextView) findViewById(R.id.tour_guide_name)).setText(tourGuideName);

        tourGuides = new HashMap<>();
        tourGuides.put(tg1[0], tg1);
        tourGuides.put(tg2[0], tg2);
        tourGuides.put(tg3[0], tg3);
        tourGuides.put(tg4[0], tg4);
        tourGuides.put(tg5[0], tg5);
        tourGuides.put(tg6[0], tg6);
        tourGuides.put(tg7[0], tg7);

        TextView offeredTour = (TextView) findViewById(R.id.offered_tours);

        if (tourGuides.containsKey(tourGuideName)) {
            final String[] tourGuide = tourGuides.get(tourGuideName);
            ((TextView) findViewById(R.id.from)).setText(tourGuide[1]);
            ((TextView) findViewById(R.id.language)).setText(tourGuide[2]);
            ((TextView) findViewById(R.id.email)).setText(tourGuide[3]);
            ((TextView) findViewById(R.id.profession)).setText(tourGuide[4]);
            ((TextView) findViewById(R.id.hobbies)).setText(tourGuide[5]);
            offeredTour.setText(Html.fromHtml("<u>" + tourGuide[6] + "</u>"));

            offeredTour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tourGuideIntent = new Intent(TourGuideActivity.this, TourActivity.class);
                    tourGuideIntent.putExtra("tour", tourGuide[6]);
                    startActivity(tourGuideIntent);
                }
            });
        }

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

        mainTabHost.setCurrentTab(1);

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
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                startActivity(intent);
            }
        });

        mainTabHost.getTabWidget().getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

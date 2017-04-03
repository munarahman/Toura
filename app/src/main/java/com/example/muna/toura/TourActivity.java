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

public class TourActivity extends AppCompatActivity {

    private String tourName;
    private String[] t1 = {"Kensington Market", "Terry", "Sunday", "2 hours", "College & Augusta",
    "Kensington Market offers a fascinating living-history tour of the immigrant populations who built Toronto. The closest we have to an Old World marketplace, you'll find an Arabian spice store nestled next to a Jewish grocery next to a Caribbean fruit stand beside a Chilean produce store."};
    private String[] t2 = {"Distillery District", "Mags", "Saturday & Sunday", "2 hours", "Mill St. & Parliament St.",
    "The Distillery District is a commercial and residential district in Toronto, Ontario, Canada. Located east of downtown, it contains numerous cafés, restaurants, and shops housed within heritage buildings of the former Gooderham and Worts Distillery."};
    private String[] t3 = {"Scarborough Bluffs", "Darsan", "Friday", "3 hours", "61 Under Cliff Dr.",
    "Forming much of the eastern portion of Toronto's waterfront, Scarborough Bluffs stands above the shoreline of Lake Ontario. At its highest point, the escarpment rises 90 metres (300 ft) above the coastline and spans a length of 15 kilometres (9.3 mi)."};
    private String[] t4 = {"Centre Island", "Lindsey", "Saturday", "2 hours", "Jack Layton Ferry Terminal",
    "Centre Island is Toronto’s island getaway, a reprieve from the hustle and bustle of the city without having to drive for hours. Just a short ferry ride from downtown, Centre Island is nestled between Ward’s Island and Hanlan’s Point. Visitors can rent bikes by the hour (standard, tandem and quad cycles) near the Pier to ride the many bike trails throughout the island."};
    private String[] t5 = {"Gay Village", "Marina", "Thursday to Sunday", "1 hour", "Yonge & Wellesley",
    "While the neighbourhood is home to the community centre, parks, bars, restaurants, and stores catering to the LGBT community (particularly along Church Street), it is also a historic community with Victorian houses and apartments dating back to the late 19th and early 20th century."};
    private String[] t6 = {"Pacific Mall", "Albert", "Saturday", "3 hours", "Mall Entrance (Steeles & Kennedy)",
    "Pacific Mall is surrounded by an existing shopping plaza, including the Market Village, and together they encompass over 500 stores and are served by both indoor and outdoor parking areas with over 1,500 parking spaces combined."};
    private String[] t7 = {"The Junction", "Steffi", "Saturday", "1 hour", "Dufferin & Keele",
    "The neighbourhood was previously an independent city called West Toronto, that was also its own federal electoral district until amalgamating with the city of Toronto in 1909"};

    private Map<String, String[]> tours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        tourName = (String) getIntent().getSerializableExtra("tour");
        ((TextView) findViewById(R.id.tour_name)).setText(tourName);

        tours = new HashMap<>();
        tours.put(t1[0], t1);
        tours.put(t2[0], t2);
        tours.put(t3[0], t3);
        tours.put(t4[0], t4);
        tours.put(t5[0], t5);
        tours.put(t6[0], t6);
        tours.put(t7[0], t7);

        TextView tourGuide = (TextView) findViewById(R.id.tour_guide);

        if (tours.containsKey(tourName)) {
            final String[] tour = tours.get(tourName);
            tourGuide.setText(Html.fromHtml("<u>" + tour[1] + "</u>"));
            ((TextView) findViewById(R.id.weekday)).setText(tour[2]);
            ((TextView) findViewById(R.id.duration)).setText(tour[3]);
            ((TextView) findViewById(R.id.meeting_place)).setText(tour[4]);
            ((TextView) findViewById(R.id.description)).setText(tour[5]);

            tourGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent tourGuideIntent = new Intent(TourActivity.this, TourGuideActivity.class);
                    tourGuideIntent.putExtra("tourGuide", tour[1]);
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

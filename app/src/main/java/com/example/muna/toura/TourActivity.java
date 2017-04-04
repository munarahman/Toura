package com.example.muna.toura;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.muna.toura.ServerTasks.PolylineAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TourActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String tourName;
    private String[] t1 = {"Kensington Market", "Terry", "Sunday", "2 hours", "College & Augusta",
    "Kensington Market offers a fascinating living-history tour of the immigrant populations who built Toronto. The closest we have to an Old World marketplace, you'll find an Arabian spice store nestled next to a Jewish grocery next to a Caribbean fruit stand beside a Chilean produce store.",
            "43.654524", "-79.401457"};
    private String[] t2 = {"Distillery District", "Mags", "Saturday & Sunday", "2 hours", "Mill St. & Parliament St.",
    "The Distillery District is a commercial and residential district in Toronto, Ontario, Canada. Located east of downtown, it contains numerous cafés, restaurants, and shops housed within heritage buildings of the former Gooderham and Worts Distillery.",
            "43.650305", "-79.35958"};
    private String[] t3 = {"Scarborough Bluffs", "Darsan", "Friday", "3 hours", "61 Under Cliff Dr.",
    "Forming much of the eastern portion of Toronto's waterfront, Scarborough Bluffs stands above the shoreline of Lake Ontario. At its highest point, the escarpment rises 90 metres (300 ft) above the coastline and spans a length of 15 kilometres (9.3 mi).",
            "43.706021", "-79.231634"};
    private String[] t4 = {"Centre Island", "Lindsey", "Saturday", "2 hours", "Jack Layton Ferry Terminal",
    "Centre Island is Toronto’s island getaway, a reprieve from the hustle and bustle of the city without having to drive for hours. Just a short ferry ride from downtown, Centre Island is nestled between Ward’s Island and Hanlan’s Point. Visitors can rent bikes by the hour (standard, tandem and quad cycles) near the Pier to ride the many bike trails throughout the island.",
            "43.618534", "-79.373833"};
    private String[] t5 = {"Gay Village", "Marina", "Thursday to Sunday", "1 hour", "Yonge & Wellesley",
    "While the neighbourhood is home to the community centre, parks, bars, restaurants, and stores catering to the LGBT community (particularly along Church Street), it is also a historic community with Victorian houses and apartments dating back to the late 19th and early 20th century.",
            "43.664542", "-79.381752"};
    private String[] t6 = {"Pacific Mall", "Albert", "Saturday", "3 hours", "Mall Entrance (Steeles & Kennedy)",
    "Pacific Mall is surrounded by an existing shopping plaza, including the Market Village, and together they encompass over 500 stores and are served by both indoor and outdoor parking areas with over 1,500 parking spaces combined.",
            "43.826005", "-79.306237"};
    private String[] t7 = {"The Junction", "Steffi", "Saturday", "1 hour", "Dufferin & Keele",
    "The neighbourhood was previously an independent city called West Toronto, that was also its own federal electoral district until amalgamating with the city of Toronto in 1909",
            "43.666612", "-79.468614"};

    private Map<String, String[]> tours;


    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    private void connectMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.tour_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(
                mLastLocation.getLatitude(), mLastLocation.getLongitude());

        if (tours.containsKey(tourName)) {
            final String[] tour = tours.get(tourName);
            location = new LatLng(Double.parseDouble(tour[6]),
                    Double.parseDouble(tour[7]));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));

        PolylineAsyncTask pat = new PolylineAsyncTask(this, new PolylineAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String serverResponse) {

            }
        });

        pat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                "&path=60.170880,24.942795|60.170879,24.942796|60.170877,24.942796");

//        Polyline line = mMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(43.654524,-79.401457), new LatLng(43.653373,-79.401562))
//                .width(5)
//                .color(Color.RED));

//        addHeatMap();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("onConnectionFailed: " + result);
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionSuspended: " + i);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        System.out.println("current location: " + mLastLocation);
        if (mLastLocation != null) {
            System.out.println(String.valueOf(mLastLocation.getLatitude()));
            System.out.println(String.valueOf(mLastLocation.getLongitude()));
        }
        connectMap();
    }


    private void removeHeatMap() {
        mOverlay.remove();
    }

}

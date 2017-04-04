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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    private String[] s1 = {"Kensington Market", "43.655142,-79.39931|43.654901,-79.400668|43.653584,-79.40043|43.652939,-79.401121|43.654862,-79.402007|43.657348,-79.403149"};
    private String[] s2 = {"Distillery District", "43.650305,-79.35958"};
    private String[] s3 = {"Scarborough Bluffs", "43.706021,-79.231634"};
    private String[] s4 = {"Centre Island", "43.618534,-79.373833"};
    private String[] s5 = {"Gay Village", "43.664542,-79.381752"};
    private String[] s6 = {"Pacific Mall", "43.826005,-79.306237"};
    private String[] s7 = {"The Junction", "43.666612,-79.468614"};

    private String[] n = {"Meeting Spot", "Blackbird Baking", "Tibetan Village Store", "Handlebar", "Jumbo Empanadas", "Ending Spot"};

    private Map<String, String[]> tours;
    private Map<String, String> stops;


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

        stops = new HashMap<>();
        stops.put(s1[0], s1[1]);
        stops.put(s2[0], s2[1]);
        stops.put(s3[0], s3[1]);
        stops.put(s4[0], s4[1]);
        stops.put(s5[0], s5[1]);
        stops.put(s6[0], s6[1]);
        stops.put(s7[0], s7[1]);

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
                Intent intent = new Intent().setClass(getApplicationContext(), TravellerActivity.class);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        PolylineAsyncTask pat = new PolylineAsyncTask(this, new PolylineAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(List<LatLng> serverResponse) {
                mMap.addPolyline(new PolylineOptions()
                        .addAll(serverResponse)
                        .width(12)
                        .color(Color.BLUE));
            }
        });

        pat.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, stops.get(tourName));

        addHeatMap();
        addMarkers();
    }

    private void addHeatMap() {
        ArrayList<LatLng> list;

        InputStream inputStream = getResources().openRawResource(R.raw.assault_tps);
        list = fileToList(inputStream);


        // TODO: figure out how to make a heatmap without any points (crashes now)
        if (list.isEmpty()) {
            list.add(new LatLng(50.0, 50.0));
        } else {

            // Create a heat map tile provider, passing it the latlngs of the police stations.
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

            // set the radius of each dot
            mProvider.setRadius(20);
        }
    }

    private void addMarkers() {
        System.out.println(stops.get(tourName));
        int i = 0;
        for (String coord : stops.get(tourName).split("\\|")) {
            String[] latlng = coord.split(",");
            LatLng stop = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));
            mMap.addMarker(new MarkerOptions().position(stop).title(n[i]));
            i++;
        }

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

    private ArrayList<LatLng> fileToList(InputStream stream) {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        String[] line;

        Scanner scanner = new Scanner(stream);

        // ignore the first line (they are titles)
        scanner.nextLine();

        while (scanner.hasNext()) {

            line = scanner.nextLine().split(",");

            list.add(new LatLng(
                    Double.parseDouble(line[1]),
                    Double.parseDouble(line[0])));
        }

        return list;
    }
}

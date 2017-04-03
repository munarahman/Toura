package com.example.muna.toura;

import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PublicSafetyMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_safety_map);

        // create the TabHost that will create the tabs
        final TabHost mainTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mainTabHost.setup();

        TabHost.TabSpec mainTabSpec = mainTabHost.newTabSpec("First Tab");
        mainTabSpec.setContent(R.id.webview_maps);
        mainTabSpec.setIndicator("Public Map");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Second Tab");
        mainTabSpec.setContent(R.id.user_maps);
        mainTabSpec.setIndicator("User Data Map");
        mainTabHost.addTab(mainTabSpec);

        mainTabHost.setCurrentTab(0);

        mainTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
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
                .findFragmentById(R.id.map);
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

        LatLng currentLatLng = new LatLng(
                mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f));

        addHeatMap();
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

    private void removeHeatMap() {
        mOverlay.remove();
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

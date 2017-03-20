package com.example.muna.toura;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class SafetyMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private Button addButton;
    private Button doneButton;
    private boolean isAddMode = false;
    private ArrayList<LatLng> addedAreas= new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_maps);

        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button doneButton = (Button) findViewById(R.id.done_button);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                System.out.println("HELLOO");
//                mMap.clear();

                Toast.makeText(getApplicationContext(), "Please select a starting destination.",
                        Toast.LENGTH_SHORT);

                isAddMode = true;

                doneButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
             }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<LatLng> list = null;

                isAddMode = false;

                doneButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);

                try {
                    list = readItems(R.raw.police_stations);
                    list.addAll(addedAreas);

                    // TODO: add the new combined list into Local Storage.


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Problem reading list of locations.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // get the search results from the auto-complete
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                System.out.println("Place: " + place.getName());
                LatLng selectedPlaceLatLng = place.getLatLng();

                mMap.addMarker(new MarkerOptions()
                        .position(selectedPlaceLatLng)
                        .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPlaceLatLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlaceLatLng, 12.0f));

                addButton.setVisibility(View.VISIBLE);


            }

            @Override
            public void onError(Status status) {
                System.out.println("An error occurred: " + status);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                System.out.println(latLng);
                if (isAddMode) {


                    mMap.addMarker(new MarkerOptions().position(latLng));
                    addedAreas.add(latLng);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        });

        addHeatMap();
    }

    private void addHeatMap() {
        ArrayList<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(R.raw.police_stations);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

        // set the radius of each dot
        mProvider.setRadius(75);
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }


}

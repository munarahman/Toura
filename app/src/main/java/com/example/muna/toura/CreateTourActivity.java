package com.example.muna.toura;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTourActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTerryRef = mRootRef.child("Terry");
    DatabaseReference mToursRef = mTerryRef.child("tours");

    private PlaceAutocompleteFragment autocompleteFragment;

    private String tourLocation;
    private EditText tourLength;
    private EditText tourDescription;

    private Button saveTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tourLength = (EditText) findViewById(R.id.tour_length);
        tourDescription = (EditText) findViewById(R.id.tour_description);

        saveTour = (Button) findViewById(R.id.save_tour);

        saveTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference newLocation = mToursRef.child(tourLocation);
                newLocation.child("length").setValue(tourLength.getText().toString());
                newLocation.child("tourDescription").setValue(tourDescription.getText().toString());
                finish();
            }
        });

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.google_places_autocomplete);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                tourLocation = (String) place.getName();
                System.out.println("Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred: " + status);
            }
        });
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println(result);
    }



    /*
    *
    * name
    * location
    * description
    * approximate length
    * photos
    * map
    *
    */
}

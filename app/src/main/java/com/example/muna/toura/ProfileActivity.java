package com.example.muna.toura;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 11;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mTerryRef = mRootRef.child("Terry");
    DatabaseReference mLanguage = mTerryRef.child("language");
    DatabaseReference mFrom = mTerryRef.child("from");
    DatabaseReference mHobbies = mTerryRef.child("hobbies");
    DatabaseReference mEmail = mTerryRef.child("email");
    DatabaseReference mProfession= mTerryRef.child("profession");

    private EditText profileLanguage;
    private EditText profileFrom;
    private EditText profileHobbies;
    private EditText profileEmail;
    private EditText profileProfession;

    private Button newTour;
    private Button doneEditing;
    private Button seeTours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION ) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) !=
                PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String [] {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder descriptionAlertDialog = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.email_dialog, null);
                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                descriptionAlertDialog.setView(dialogView)

                        // Setting Dialog Title
                        .setTitle("Contact Tour Guide")

                        .setMessage("Enter your message for the Tour Guide!")

                        // Setting Positive "Yes" Button
                        .setPositiveButton("Next",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.out.println(edt.getText().toString());
                                        dialog.cancel();

                                    }
                                })

                        // Setting Negative "NO" Button
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.cancel();
                                    }
                                })

                        .show();
            }
        });

        profileLanguage = (EditText) findViewById(R.id.profile_language);
        profileFrom = (EditText) findViewById(R.id.profile_from);
        profileHobbies = (EditText) findViewById(R.id.profile_hobbies);
        profileEmail = (EditText) findViewById(R.id.profile_email);
        profileProfession = (EditText) findViewById(R.id.profile_profession);

        newTour = (Button) findViewById(R.id.new_tour);
        doneEditing = (Button) findViewById(R.id.done_editing);
        seeTours = (Button) findViewById(R.id.see_tours);

        newTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createTourIntent = new Intent(getApplicationContext(), CreateTourActivity.class);
                startActivity(createTourIntent);
            }
        });

        doneEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditFields(false);
                mLanguage.setValue(profileLanguage.getText().toString());
                mFrom.setValue(profileFrom.getText().toString());
                mHobbies.setValue(profileHobbies.getText().toString());
                mEmail.setValue(profileEmail.getText().toString());
                mProfession.setValue(profileProfession.getText().toString());
            }
        });

        seeTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewTourIntent = new Intent(getApplicationContext(), ViewTourActivity.class);
                startActivity(viewTourIntent);
            }
        });

        // create the TabHost that will create the tabs
        final TabHost mainTabHost = (TabHost) findViewById(android.R.id.tabhost);
//        LocalActivityManager mLocalActivityManager = new LocalActivityManager(mActivity, false);
//        mLocalActivityManager.dispatchCreate(state); // state will be bundle your activity state which you get in onCreate
//        mainTabHost.setup(mLocalActivityManager);


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

        mainTabHost.setCurrentTab(2);

//        View view = LayoutInflater.from(this).inflate(R.layout.profile_icon,
//                mainTabHost.getTabWidget(), false);
//        ImageView imgtabF = (ImageView) view.findViewById(R.id.profile_icon);
//        imgtabF.setBackgroundResource(R.drawable.profile_icon);
//
//        mainTabSpec.setIndicator(view);
//        mainTabHost.addTab(mainTabSpec);


        // on click for the Map Tab
        mainTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_edit:
                toggleEditFields(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setTextFields(mLanguage, profileLanguage);
        setTextFields(mFrom, profileFrom);
        setTextFields(mEmail, profileEmail);
        setTextFields(mHobbies, profileHobbies);
        setTextFields(mProfession, profileProfession);
    }


    public void setTextFields(DatabaseReference fieldName, final EditText fieldValue) {
        fieldName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                fieldValue.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void toggleEditFields(boolean enabled) {
        profileLanguage.setEnabled(enabled);
        profileFrom.setEnabled(enabled);
        profileHobbies.setEnabled(enabled);
        profileEmail.setEnabled(enabled);
        profileProfession.setEnabled(enabled);

        if (enabled) {
            newTour.setVisibility(View.VISIBLE);
            doneEditing.setVisibility(View.VISIBLE);
            seeTours.setVisibility(View.GONE);
        } else {
            newTour.setVisibility(View.GONE);
            doneEditing.setVisibility(View.GONE);
            seeTours.setVisibility(View.VISIBLE);
        }
    }
}

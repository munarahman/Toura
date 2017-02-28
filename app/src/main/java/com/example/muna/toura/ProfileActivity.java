package com.example.muna.toura;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        TabHost mainTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mainTabHost.setup();

        TabHost.TabSpec mainTabSpec = mainTabHost.newTabSpec("First Tab");
        mainTabSpec.setContent(R.id.first_tab);
        mainTabSpec.setIndicator("Explore");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Second Tab");
        mainTabSpec.setContent(R.id.second_tab);
        mainTabSpec.setIndicator("Second Tab");
        mainTabHost.addTab(mainTabSpec);

        mainTabSpec = mainTabHost.newTabSpec("Profile");
        mainTabSpec.setContent(R.id.third_tab);
        mainTabSpec.setIndicator("Profile");
        mainTabHost.addTab(mainTabSpec);
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

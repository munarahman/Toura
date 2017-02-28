package com.example.muna.toura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // this is for the floating Email icon
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
//        mainTabSpec.setIndicator("Profile");

        View view = LayoutInflater.from(this).inflate(R.layout.profile_icon,
                mainTabHost.getTabWidget(), false);
        ImageView imgtabF = (ImageView) view.findViewById(R.id.profile_icon);
        imgtabF.setBackgroundResource(R.drawable.profile_icon);

        mainTabSpec.setIndicator(view);
        mainTabHost.addTab(mainTabSpec);


//        Intent intent;
//        tabhost = getTabHost();
//        TabHost.TabSpec tabspec;
//        intent = new Intent().setClass(getApplicationContext(),xxxxx.class);

//        tabspec = mainTabHost.newTabSpec("First");
//        View view = LayoutInflater.from(this).inflate(R.layout.profile_icon,
//                mainTabHost.getTabWidget(), false);
//        ImageView imgtabF = (ImageView) view.findViewById(R.id.profile_icon);
//        imgtabF.setBackgroundResource(R.drawable.profile_icon);
//
//        mainTabSpec.setIndicator(view);
//        mainTabSpec.setContent(intent);
//        tabhost.addTab(tabspec);

        mainTabHost.setCurrentTab(2);
        Context context = getApplicationContext();
        int color = ContextCompat.getColor(context, R.color.selected_tab);
        mainTabHost.getTabWidget().getChildAt(2).setBackgroundColor(color);

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                System.out.println(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

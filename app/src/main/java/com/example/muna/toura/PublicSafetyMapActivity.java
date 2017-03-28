package com.example.muna.toura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TabHost;

public class PublicSafetyMapActivity extends AppCompatActivity {

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

        WebView webView = (WebView) this.findViewById(R.id.public_maps_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://torontops.maps.arcgis.com/apps/PublicInformation/index.html?appid=b316d0be9dd14d8186c56f0757c1e4b8");


        mainTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setClass(getApplicationContext(), SafetyMapsActivity.class);
                startActivity(intent);
            }
        });
    }
}

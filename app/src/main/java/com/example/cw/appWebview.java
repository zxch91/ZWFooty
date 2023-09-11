package com.example.cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class appWebview extends baseDrawer implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient()); // set webview in the app

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript

        webView.loadUrl("file:///android_asset/index.html"); // load the webview into the app

    }

    @Override
    protected int getLayoutId() {
        return (R.layout.activity_webview); // get current layout
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //handle pressing on different potential screens on sidebar
        switch (id){
            case R.id.nav_item1:
                Intent intentNews = new Intent(getApplicationContext(), News.class);
                startActivity(intentNews);
                finish();
                break;
            case R.id.nav_item2:
                Intent intentLeague = new Intent(getApplicationContext(), leagueTable.class);
                startActivity(intentLeague);
                finish();
                break;
            case R.id.nav_item3:
                Intent intentFixtures = new Intent(getApplicationContext(), fixtures.class);
                startActivity(intentFixtures);
                finish();
                break;
            case R.id.nav_item4:
                Intent intentClub = new Intent(getApplicationContext(), myClub.class);
                startActivity(intentClub);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // added if potentially multiple pages on the webview so we can go back
        WebView webView = findViewById(R.id.webview);
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
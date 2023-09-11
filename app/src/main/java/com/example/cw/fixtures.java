package com.example.cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class fixtures extends baseDrawer implements NavigationView.OnNavigationItemSelectedListener {

    // Add this field to store a reference to your SQLiteOpenHelper
    private ZWFootyDB zwFootyDB;

    // Add this field to store a reference to the RecyclerView
    private RecyclerView fixtureRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize zwfootyDB
        zwFootyDB = new ZWFootyDB(this);

        // Initialize the fixtureRecyclerView field
        fixtureRecyclerView = findViewById(R.id.fixture_recycler_view);
        // fetch information for fixtures
        fetchFixtureData();
    }

    protected int getLayoutId() {
        return R.layout.activity_fixtures;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Handle switch to different screen

        switch (id) {
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
            case R.id.nav_item4:
                Intent intentClub = new Intent(getApplicationContext(), myClub.class);
                startActivity(intentClub);
                finish();
                break;
            case R.id.nav_item5:
                Intent intentWebView = new Intent(getApplicationContext(), appWebview.class);
                startActivity(intentWebView);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    // Fetch fixture data from the database and update the RecyclerView
    private void fetchFixtureData() {
            // uses the getfixturedata method from zwfootydb file
        List<Fixture> fixtureList = zwFootyDB.getFixtureData();
        populateFixtures(fixtureList);
    }

    private void populateFixtures(List<Fixture> fixtureList) {
        // populates the fixture recyclerview with the fixture results
        FixtureAdapter fixtureAdapter = new FixtureAdapter(fixtureList, this);
        fixtureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fixtureRecyclerView.setAdapter(fixtureAdapter);
    }

}

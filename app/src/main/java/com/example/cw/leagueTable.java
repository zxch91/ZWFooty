package com.example.cw;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class leagueTable extends baseDrawer implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // league on load is premier league
        saveDefaultLeague("PREMIERLEAGUE");

        TableLayout tableLayoutTop10 = findViewById(R.id.leaguetable_top10);
        TableLayout tableLayoutRemaining = findViewById(R.id.leaguetable_remaining);
        Button showMoreButton = findViewById(R.id.show_more_button);
        // only shows the first 10 teams in the league on loading
        tableLayoutRemaining.setVisibility(View.GONE);

        // gets an instance of the database for the league tables
        ZWFootyDB zwFootyDB = new ZWFootyDB(this);

        List<Pair<String, Pair<Integer, String>>> leagueData = zwFootyDB.getLeagueData(getDefaultLeague()); // gets the league information
        // populates the league tables
        populateLeagueTable(tableLayoutTop10, tableLayoutRemaining, leagueData);

        showMoreButton.setOnClickListener(v -> {
            if (tableLayoutRemaining.getVisibility() == View.GONE) {
                // if you click when it isnt showing all 20 teams - set them to visible
                tableLayoutRemaining.setVisibility(View.VISIBLE);
                for (int i = 0; i < tableLayoutRemaining.getChildCount(); i++) {
                    tableLayoutRemaining.getChildAt(i).setVisibility(View.VISIBLE);
                }
                showMoreButton.setText("Show Less");
            } else {
                // make it 10 teams again - minimising the league table
                tableLayoutRemaining.setVisibility(View.GONE);
                for (int i = 0; i < tableLayoutRemaining.getChildCount(); i++) {
                    tableLayoutRemaining.getChildAt(i).setVisibility(View.GONE);
                }
                showMoreButton.setText("Show More");
            }
        });


        // Hide all teams except the top 10 initially
        for (int i = 0; i < tableLayoutRemaining.getChildCount(); i++) {
            tableLayoutRemaining.getChildAt(i).setVisibility(View.GONE);
        }
        updateLeagueTable(getDefaultLeague());


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_league_table;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // handle sidebar choice of screen

        switch (id) {
            case R.id.nav_item1:
                Intent intentNews = new Intent(getApplicationContext(), News.class);
                startActivity(intentNews);
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

    private void populateLeagueTable(TableLayout tableLayoutTop10, TableLayout tableLayoutRemaining, List<Pair<String, Pair<Integer, String>>> leagueData) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < leagueData.size(); i++) {
            Pair<String, Pair<Integer, String>> data = leagueData.get(i);

            // Inflate the league_table_item layout
            View itemView = inflater.inflate(R.layout.league_table_item, null);

            // Set the team logo based on the team crest URL
            ImageView teamLogo = itemView.findViewById(R.id.team_logo);
            String crestUrl = data.second.second;

            // Load the team crest using Glide
            Glide.with(this)
                    .load(crestUrl)
                    .into(teamLogo);
            // Some teams do not have logos in the api so will be blank

            ImageButton starButton = itemView.findViewById(R.id.star_button);
            starButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTeamAsFavourite(data.first, starButton);
                }
            });
            isTeamFavorited(data.first, new OnTeamFavoritedListener() {
                @Override
                public void onTeamFavorited(boolean isFavorited) {
                    if (isFavorited) {
                        starButton.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {
                        starButton.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                }
            });

            // Set the team name and points
            TextView teamName = itemView.findViewById(R.id.team_name);
            teamName.setText(data.first);

            TextView teamPoints = itemView.findViewById(R.id.team_points);
            teamPoints.setText(String.valueOf(data.second.first));

            // Add the itemView to the appropriate TableLayout
            if (i < 10) {
                tableLayoutTop10.addView(itemView);
            } else {
                tableLayoutRemaining.addView(itemView);
            }
        }
    }


    private void saveDefaultLeague(String leagueName) {
        SharedPreferences sharedPreferences = getSharedPreferences("league_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("default_league", leagueName);
        editor.apply();
    }

    private String getDefaultLeague() {
        SharedPreferences sharedPreferences = getSharedPreferences("league_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("default_league", "PREMIERLEAGUE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_spinner_menu, menu);

        // Find the spinner menu item
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();

        // Set up the spinner adapter with the league names
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.leagues_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set an OnItemSelectedListener on the spinner to update the league table
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLeague = (String) parent.getItemAtPosition(position);
                updateLeagueTable(selectedLeague);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return true;
    }


    private void updateLeagueTable(String leagueName) {
        // Save the selected league as the default league
        saveDefaultLeague(leagueName);

        // Get the updated league data
        ZWFootyDB zwFootyDB = new ZWFootyDB(this);
        List<Pair<String, Pair<Integer, String>>> leagueData = zwFootyDB.getLeagueData(getDefaultLeague());

        // Clear the existing table views
        TableLayout tableLayoutTop10 = findViewById(R.id.leaguetable_top10);
        TableLayout tableLayoutRemaining = findViewById(R.id.leaguetable_remaining);
        tableLayoutTop10.removeAllViews();
        tableLayoutRemaining.removeAllViews();

        // Populate the table views with the updated league data
        populateLeagueTable(tableLayoutTop10, tableLayoutRemaining, leagueData);
    }

    private void setTeamAsFavourite(String teamName, ImageButton starButton) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userFavouritesRef = FirebaseDatabase.getInstance().getReference("user_favourites").child(userId).child(teamName);
            userFavouritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getValue().equals(true)) {
                        userFavouritesRef.setValue(false);
                        starButton.setImageResource(android.R.drawable.btn_star_big_off);




                    } else {
                        userFavouritesRef.setValue(true);
                        starButton.setImageResource(android.R.drawable.btn_star_big_on);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Failure adding favourite", Toast.LENGTH_SHORT).show();
        }
    }







    // callback interface to run asynchronous method
    public interface OnTeamFavoritedListener {
        void onTeamFavorited(boolean isFavorited);
    }


    private void isTeamFavorited(String teamName, OnTeamFavoritedListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userFavouritesRef = FirebaseDatabase.getInstance().getReference("user_favourites").child(userId);
            userFavouritesRef.child(teamName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) { // async method, requires interface
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.getValue() != null && (boolean) dataSnapshot.getValue()) {
                            listener.onTeamFavorited(true);
                        } else {
                            listener.onTeamFavorited(false);
                        }
                    } else {
                        listener.onTeamFavorited(false);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Failure ", Toast.LENGTH_SHORT).show();
        }
    }













}



package com.example.cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myClub extends baseDrawer implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private String userId;
    private RecyclerView favoriteTeamsRecyclerView;
    private FavoriteTeamAdapter favoriteTeamAdapter;
    private List<Team> favoriteTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("user_favourites");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Retrieve favorite teams
        retrieveFavoriteTeams();


    }


    protected int getLayoutId() {
        return R.layout.activity_my_club;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Handle your menu item clicks here

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

    private void retrieveFavoriteTeams() {
        favoriteTeams = new ArrayList<>();
        favoriteTeamAdapter = new FavoriteTeamAdapter(favoriteTeams, this);
        favoriteTeamsRecyclerView = findViewById(R.id.favorite_teams_recycler_view);
        favoriteTeamsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteTeamsRecyclerView.setAdapter(favoriteTeamAdapter);

        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteTeams.clear();
                for (DataSnapshot teamSnapshot : dataSnapshot.getChildren()) {
                    // if the club in our database is currently true - in the users favourite teams
                    if (teamSnapshot.getValue(Boolean.class))  {
                        String teamName = teamSnapshot.getKey();
                        favoriteTeams.add(new Team(teamName));
                    }
                }
                favoriteTeamAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
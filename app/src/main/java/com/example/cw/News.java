package com.example.cw;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class News extends baseDrawer implements MyAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseUser user;
    List<newsContent> news;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);

        auth = FirebaseAuth.getInstance(); // authenticates the user


        RecyclerView recyclerView = findViewById(R.id.recyclerview); // gets recyclerview to show the news articles

        news = new ArrayList<>();
        news.add(new newsContent("Ronaldoo", "Breaks Goal Scoring record in thiller vs Barcelona","Breaks Goal Scoring record in thiller vs Barcelona", R.drawable.ronaldo,1));
        news.add(new newsContent("Messi the greatest?", "Argentina win the world cup in an epic final","Argentina win the world cup in an epic final", R.drawable.messiworldcup,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), news); // puts the news articles inside the recyclerview
        myAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news; // gets the layout we are setting the screen to
    }

    @Override
    public void onItemClick(int position) {
        newsContent clickedNewsArticle = news.get(position);

        Intent intent = new Intent(News.this, newsArticleActivity.class);
        intent.putExtra("KEY_NEWS_ARTICLE", clickedNewsArticle);
        startActivity(intent); // navigates to the article the user has selected
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // handles what screen you go to when you click one on the sidebar
        switch (id){
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
}

package com.example.cw;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FetchStandingsService extends IntentService {
    private ZWFootyDB zwFootyDB;

    public FetchStandingsService() {
        super("FetchStandingsService");
        zwFootyDB = new ZWFootyDB(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            fetchPremierLeagueStandingsAndUpdate();
            fetchLaLigaStandingsAndUpdate(); // Fetch LaLiga standings
            fetchCompletedMatchesLastTwoWeeks();
        }
    }

    private void fetchPremierLeagueStandingsAndUpdate() {
        // Replace YOUR_API_KEY with your actual API key
        String apiKey = "1b05e18911004067ad284566ba69b83b";
        String url = "https://api.football-data.org/v2/competitions/2021/standings"; // Premier League standings endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Create a RequestQueue object to handle HTTP requests

        // Create a new JsonObjectRequest to fetch the JSON response from the API endpoint
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // JSON request body (in this case, there is no request body)
                 new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray standingsArray = response.getJSONArray("standings");
                    JSONObject standingsObject = standingsArray.getJSONObject(0); // Get the total standings
                    JSONArray teamStandings = standingsObject.getJSONArray("table");

                    // Open the database and delete any existing data
                    SQLiteDatabase db = zwFootyDB.getWritableDatabase();
                    db.delete("PREMIERLEAGUE", null, null);

                    for (int i = 0; i < teamStandings.length(); i++) { // loop through each standing and get the useful information
                        JSONObject team = teamStandings.getJSONObject(i);
                        String teamName = team.getJSONObject("team").getString("name");
                        String teamCrestUrl = team.getJSONObject("team").getString("crestUrl");
                        int points = team.getInt("points");

                        String insertQuery = "INSERT INTO PREMIERLEAGUE (team, points, crestUrl) VALUES ('" + teamName + "', " + points + ", '" + teamCrestUrl + "')";
                        db.execSQL(insertQuery); // insert each standing with information into database
                    }
                    // Close DB Connection
                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle any errors that occur during the API request
                error.printStackTrace();
            }
        }) {
            @Override
            // Override the getHeaders() method to include the API key as an HTTP header
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Auth-Token", apiKey);
                return headers;
            }
        };
        // Add the JsonObjectRequest to the RequestQueue to start the API request
        requestQueue.add(jsonObjectRequest);
    }

    private void fetchLaLigaStandingsAndUpdate() {

        // Replace YOUR_API_KEY with your actual API key
        String apiKey = "1b05e18911004067ad284566ba69b83b";

        // URL for LaLiga standings endpoint
        String url = "https://api.football-data.org/v2/competitions/2014/standings";

        // Create a RequestQueue object to handle HTTP requests
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a new JsonObjectRequest to fetch the JSON response from the API endpoint
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, // JSON request body (in this case, there is no request body)
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract the standings information from the JSON response
                            JSONArray standingsArray = response.getJSONArray("standings");
                            JSONObject standingsObject = standingsArray.getJSONObject(0); // Get the total standings
                            JSONArray teamStandings = standingsObject.getJSONArray("table");

                            // Open the database and delete any existing data
                            SQLiteDatabase db = zwFootyDB.getWritableDatabase();
                            db.delete("LALIGA", null, null);

                            // Loop through the team standings and insert each team's data into the database
                            for (int i = 0; i < teamStandings.length(); i++) {
                                JSONObject team = teamStandings.getJSONObject(i);
                                String teamName = team.getJSONObject("team").getString("name");
                                String teamCrestUrl = team.getJSONObject("team").getString("crestUrl");
                                int points = team.getInt("points");

                                // Construct a SQL query to insert the data into the database
                                String insertQuery = "INSERT INTO LALIGA (team, points, crestUrl) VALUES ('" + teamName + "', " + points + ", '" + teamCrestUrl + "')";
                                db.execSQL(insertQuery);
                            }

                            // Close the database connection
                            db.close();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the API request
                        error.printStackTrace();
                    }
                }
        ) {
            // Override the getHeaders() method to include the API key as an HTTP header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Auth-Token", apiKey);
                return headers;
            }
        };

        // Add the JsonObjectRequest to the RequestQueue to start the API request
        requestQueue.add(jsonObjectRequest);
    }


    private void fetchCompletedMatchesLastTwoWeeks() {
        // Replace YOUR_API_KEY with your actual API key
        String apiKey = "1b05e18911004067ad284566ba69b83b";

        // Calculate the date 2 weeks ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String twoWeeksAgoDate = sdf.format(calendar.getTime());

        calendar = Calendar.getInstance();
        String todayDate = sdf.format(calendar.getTime());
        // gets the fixture results in the last 2 weeks  for the premier league
        String url = "https://api.football-data.org/v2/competitions/PL/matches?status=FINISHED&dateFrom=" + twoWeeksAgoDate + "&dateTo=" + todayDate;

        // Create a RequestQueue object to handle HTTP requests
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Create a new JsonObjectRequest to fetch the JSON response from the API endpoint
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // finds all matches in the api call
                    JSONArray matchesArray = response.getJSONArray("matches");

                    // Open the database and delete any existing data
                    SQLiteDatabase db = zwFootyDB.getWritableDatabase();
                    db.delete("PREMIERLEAGUERESULTS", null, null);

                    for (int i = 0; i < matchesArray.length(); i++) {
                        // gets the information from each match
                        JSONObject match = matchesArray.getJSONObject(i);

                        String team1 = match.getJSONObject("homeTeam").getString("name");
                        String team2 = match.getJSONObject("awayTeam").getString("name");

                        int homeTeamGoals = match.getJSONObject("score").getJSONObject("fullTime").getInt("homeTeam");
                        int awayTeamGoals = match.getJSONObject("score").getJSONObject("fullTime").getInt("awayTeam");
                        String score = homeTeamGoals + "-" + awayTeamGoals;


                        // Inserts match information into database
                        String insertQuery = "INSERT INTO PREMIERLEAGUERESULTS (team1, team2, score) VALUES ('" + team1 + "', '" + team2 + "', '" +  score + "')";
                        db.execSQL(insertQuery);
                    }

                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Auth-Token", apiKey);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }









}

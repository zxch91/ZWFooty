package com.example.cw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.common.data.DataBuffer;

import java.util.ArrayList;
import java.util.List;

public class ZWFootyDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 26;

    public ZWFootyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creation of all tables here
        String CREATE_TABLE_PREMIER_LEAGUE = "CREATE TABLE PREMIERLEAGUE (" +
                "team TEXT PRIMARY KEY," +
                "points INTEGER NOT NULL," +
                "crestUrl TEXT);"; // Add this column
        db.execSQL(CREATE_TABLE_PREMIER_LEAGUE);
        // la liga table
        String CREATE_TABLE_LALIGA = "CREATE TABLE LALIGA (" +
                "team TEXT PRIMARY KEY," +
                "points INTEGER NOT NULL," +
                "crestUrl TEXT);"; // Add this column;
        db.execSQL(CREATE_TABLE_LALIGA);
        // premier league scorers table (not being used right now)
        String CREATE_TABLE_PREMIERLEAGUESCORERS = "CREATE TABLE PREMIERLEAGUESCORERS (" +
                "player TEXT PRIMARY KEY," +
                "goals INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_PREMIERLEAGUESCORERS);
        // la liga scorers table (not being used)
        String CREATE_TABLE_LALIGASCORERS = "CREATE TABLE LALIGASCORERS (" +
                "player TEXT PRIMARY KEY," +
                "goals INTEGER NOT NULL);";
        db.execSQL(CREATE_TABLE_LALIGASCORERS);
        // premier league results table
        String CREATE_TABLE_PREMIERLEAGUERESULTS = "CREATE TABLE PREMIERLEAGUERESULTS(" +
                "team1 TEXT NOT NULL," +
                "team2 TEXT NOT NULL," +
                "score TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE_PREMIERLEAGUERESULTS);
        // la liga results table
        String CREATE_TABLE_LALIGARESULTS = "CREATE TABLE LALIGARESULTS(" +
                "team1 TEXT NOT NULL," +
                "team2 TEXT NOT NULL," +
                "score TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE_LALIGARESULTS);

        // some data incase this is implemented (no api for it)
        String INSERT_PREMIERLEAGUE_SCORERS = "INSERT INTO PREMIERLEAGUESCORERS (player, goals) VALUES" +
                "('Haaland', 21)," +
                "('Rashford', 20)," +
                "('Saka', 18)";
        db.execSQL(INSERT_PREMIERLEAGUE_SCORERS);
        // was filler data before api calls for results was introduced
        String INSERT_PREMIERLEAGUE_RESULTS = "INSERT INTO PREMIERLEAGUERESULTS (team1, team2,  score) VALUES" +
                "('Man United','Man City','1-0')," +
                "('Arsenal','Man City','2-1')";
        db.execSQL(INSERT_PREMIERLEAGUE_RESULTS);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade logic here
        db.execSQL("DROP TABLE IF EXISTS PREMIERLEAGUE");
        db.execSQL("DROP TABLE IF EXISTS PREMIERLEAGUESCORERS");
        db.execSQL("DROP TABLE IF EXISTS PREMIERLEAGUERESULTS");
        db.execSQL("DROP TABLE IF EXISTS LALIGA");
        db.execSQL("DROP TABLE IF EXISTS LALIGASCORERS");
        db.execSQL("DROP TABLE IF EXISTS LALIGARESULTS");
        onCreate(db);
    }

    public List<Pair<String, Pair<Integer, String>>> getLeagueData(String league) {
        // allows for information from the league tables to be retrieved
        List<Pair<String, Pair<Integer, String>>> leagueData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // query to get all the information from given table
        Cursor cursor = db.rawQuery("SELECT * FROM " + league, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String team = cursor.getString(cursor.getColumnIndex("team"));
                @SuppressLint("Range") int points = cursor.getInt(cursor.getColumnIndex("points"));
                @SuppressLint("Range") String crestUrl = cursor.getString(cursor.getColumnIndex("crestUrl"));
                leagueData.add(new Pair<>(team, new Pair<>(points, crestUrl)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return leagueData;
    }


    public List<Fixture> getFixtureData() {
        // get fixtuee information
        List<Fixture> fixtureData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // get results from premier league table
        String selectQuery = "SELECT * FROM PREMIERLEAGUERESULTS";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String team1 = cursor.getString(cursor.getColumnIndex("team1"));
                @SuppressLint("Range") String team2 = cursor.getString(cursor.getColumnIndex("team2"));
                @SuppressLint("Range") String score = cursor.getString(cursor.getColumnIndex("score"));

                fixtureData.add(new Fixture(team1, team2, score));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return fixtureData;
    }










}



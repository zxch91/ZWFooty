package com.example.cw;

public class Fixture {
    private String team1;
    private String team2;
    private String score;
    private String scorers;

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScorers() {
        return scorers;
    }

    public void setScorers(String scorers) {
        this.scorers = scorers;
    }

    public Fixture(String team1, String team2, String score) {
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
        this.scorers = scorers;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getScore() {
        return score;
    }


}

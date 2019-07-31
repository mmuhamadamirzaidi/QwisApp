package com.mmuhamadamirzaidi.qwisapp.Model;

public class Ranking {

    private String Username;
    private long Score;

    public Ranking() {
    }

    public Ranking(String username, long score) {
        Username = username;
        Score = score;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public long getScore() {
        return Score;
    }

    public void setScore(long score) {
        Score = score;
    }
}

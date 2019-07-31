package com.mmuhamadamirzaidi.qwisapp.Model;

public class QuestionScore {

    private String QuestionScore, User, Score;

    public QuestionScore() {
    }

    public QuestionScore(String questionScore, String user, String score) {
        QuestionScore = questionScore;
        User = user;
        Score = score;
    }

    public String getQuestionScore() {
        return QuestionScore;
    }

    public void setQuestionScore(String questionScore) {
        QuestionScore = questionScore;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}

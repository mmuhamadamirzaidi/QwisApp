package com.mmuhamadamirzaidi.qwisapp.Model;

public class QuestionScore {

<<<<<<< HEAD
    private String QuestionScore, User, Score;
=======
    private String QuestionScore, User, Score, CategoryId, CategoryName;
>>>>>>> master

    public QuestionScore() {
    }

<<<<<<< HEAD
    public QuestionScore(String questionScore, String user, String score) {
=======
    public QuestionScore(String questionScore, String user, String score, String categoryId, String categoryName) {
>>>>>>> master
        QuestionScore = questionScore;
        User = user;
        Score = score;
        CategoryId = categoryId;
        CategoryName = categoryName;
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

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}

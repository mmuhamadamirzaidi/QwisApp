package com.mmuhamadamirzaidi.qwisapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Model.QuestionScore;

import java.io.IOException;

public class DoneGameActivity extends AppCompatActivity {

    private TextView ResultsTitle, ResultsSubtitle, ResultsScore, ResultQuestions;

    private Button ButtonResultsTryAgain;
    private Animation AnimationOne, AnimationTwo;

    private FirebaseDatabase database;
    private DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_game);

        //Text results view
        ResultsScore = findViewById(R.id.txtTotalScore);
        ResultQuestions = findViewById(R.id.txtTotalQuestion);

        //Text animation view
        ResultsTitle = findViewById(R.id.resultstitle);
        ResultsSubtitle = findViewById(R.id.resultssubtitle);

        //Button try again
        ButtonResultsTryAgain = findViewById(R.id.buttonresultstryagain);

        //Firebase
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        if (Build.VERSION.SDK_INT>=21) {
            setupWindowAnimations();
        }

        //Load animations
        AnimationOne = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_one);
        AnimationTwo = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_two);

        //Pass animations
        ResultsTitle.startAnimation(AnimationOne);
        ResultsSubtitle.startAnimation(AnimationOne);

        ButtonResultsTryAgain.startAnimation(AnimationTwo);

        ButtonResultsTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DoneGameActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoneGameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Get data from bundle and set to view
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            ResultsScore.setText(String.format("%d", score));
            ResultQuestions.setText(String.format("%d/%d", correctAnswer, totalQuestion));

            //Upload score to database
            question_score.child(String.format("%s_%s", Common.currentUser.getUsername(),
                    Common.categoryId))
                .setValue(new QuestionScore(String.format("%s_%s", Common.currentUser.getUsername(),
                        Common.categoryId),
                        Common.currentUser.getUsername(),
                        String.valueOf(score)));
        }
    }

    @TargetApi(21)
    private void setupWindowAnimations(){
        try {
            Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.activity_fade);
            getWindow().setExitTransition(fade);
        } catch (NoClassDefFoundError e){

        }
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    private RoundedBitmapDrawable getRoundedDrawable(String filename){
        try {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), getAssets().open(filename));
            dr.setCornerRadius(500);
            return dr;
        }
        catch (IOException e){
        }
        return null;
    }
}

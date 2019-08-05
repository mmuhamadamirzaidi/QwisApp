package com.mmuhamadamirzaidi.qwisapp.Student;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Interface.RankingCallBack;
import com.mmuhamadamirzaidi.qwisapp.Model.QuestionScore;
import com.mmuhamadamirzaidi.qwisapp.Model.Ranking;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.io.IOException;

public class StudentDoneGameActivity extends AppCompatActivity {

    private TextView ResultsTitle, ResultsSubtitle, ResultsScore, ResultQuestions;

    private Button ButtonResultsTryAgain;
    private Animation AnimationOne, AnimationTwo;

    private FirebaseDatabase database;
    private DatabaseReference question_score, rankingTable;
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_done_game);

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


        rankingTable = database.getReference("Ranking");

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
//                Toast.makeText(StudentDoneGameActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentDoneGameActivity.this, StudentHomeActivity.class);
                startActivity(intent);
                finish();

                //Implement callback
                updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>() {
                    @Override
                    public void callBack(Ranking ranking) {
                        //Update to ranking table
                        rankingTable.child(ranking.getUsername())
                                .setValue(ranking);
                        //After upload, sort Ranking table and show result
//                showRanking();
                    }
                });
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
                        String.valueOf(score),
                        Common.categoryId, Common.categoryName));
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

    private void updateScore(final String username, final RankingCallBack<Ranking> callback) {
        question_score.orderByChild("user").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(ques.getScore());
                        }
                        //After sum of all score, need to process sum variable here
                        //Firebase is async db, so if process outside, 'sum' value will be reset to 0
                        Ranking ranking = new Ranking(username, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

package com.mmuhamadamirzaidi.qwisapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Model.Category;
import com.mmuhamadamirzaidi.qwisapp.Model.Questions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartGameActivity extends AppCompatActivity {

    private TextView CategoryTitle, CategorySubtitle, CategoryTitleHeader, CategorySubtitleHeader;

    private Button ButtonCategoryPlayQuiz;
    private Animation AnimationOne, AnimationTwo, AnimationThree;
    private CircleImageView CategoryImageProfile, CategoryImageIcon;

    FirebaseDatabase database;
    DatabaseReference questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        //Firebase
        database = FirebaseDatabase.getInstance();
        questions = database.getReference("Questions");

        CategoryImageProfile = findViewById(R.id.categoryprofileimage);
        CategoryTitleHeader = findViewById(R.id.categorytitleheader);
        CategorySubtitleHeader = findViewById(R.id.categorysubtitleheader);

        CategoryImageIcon = findViewById(R.id.categoryimageicon);
        CategoryTitle = findViewById(R.id.resultstitle);
        CategorySubtitle = findViewById(R.id.resultssubtitle);
        ButtonCategoryPlayQuiz = findViewById(R.id.buttoncategoryplayquiz);

        //Get a value from previous page
        CategoryTitleHeader.setText(getIntent().getStringExtra("Name"));
        CategorySubtitleHeader.setText(getIntent().getStringExtra("Description"));

        CategoryTitle.setText(getIntent().getStringExtra("Name"));

        if (Build.VERSION.SDK_INT>=21) {
            setupWindowAnimations();
        }

        //Load animations
        AnimationOne = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_one);
        AnimationTwo = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_two);
        AnimationThree = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_three);

        //Pass animations
        CategoryImageIcon.startAnimation(AnimationOne);

        CategoryTitle.startAnimation(AnimationTwo);
        CategorySubtitle.startAnimation(AnimationTwo);

        ButtonCategoryPlayQuiz.startAnimation(AnimationThree);

        //Load questions once Play Quiz button clicked
        loadQuestions(Common.categoryId);

        ButtonCategoryPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(StartGameActivity.this, "Play Game!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StartGameActivity.this, PlayingGameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestions(String categoryId) {

        //Clear list if there're previous questions
        if (Common.ListQuestion.size() > 0)
            Common.ListQuestion.clear();

        questions.orderByChild("CategoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Questions ques = dataSnapshot1.getValue(Questions.class);
                    Common.ListQuestion.add(ques);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Collections.shuffle(Common.ListQuestion); //Shuffle questions to get random list questions
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
//            Log.d("imageHandling", e.toString());
        }
        return null;
    }
}

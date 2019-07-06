package com.mmuhamadamirzaidi.qwisapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartGameActivity extends AppCompatActivity {

    private TextView CategoryTitle, CategorySubtitle, CategoryTitleHeader;

    private Button ButtonCategoryPlayQuiz;
    private Animation AnimationOne, AnimationTwo, AnimationThree;
    private CircleImageView CategoryImageIcon, CategoryImageIconProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        CategoryImageIcon = findViewById(R.id.categoryimageicon);
        CategoryImageIconProfile = findViewById(R.id.categoryimageiconprofile);
        CategoryTitle = findViewById(R.id.categorytitle);
        CategorySubtitle = findViewById(R.id.categorysubtitle);
        CategoryTitleHeader = findViewById(R.id.categorytitleheader);
        ButtonCategoryPlayQuiz = findViewById(R.id.buttoncategoryplayquiz);

        //Get a value from previous page
        CategoryTitleHeader.setText(getIntent().getStringExtra("Name"));
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

        ButtonCategoryPlayQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartGameActivity.this, "Play Game!", Toast.LENGTH_SHORT).show();
            }
        });
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

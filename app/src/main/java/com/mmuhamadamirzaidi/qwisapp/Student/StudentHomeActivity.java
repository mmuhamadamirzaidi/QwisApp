package com.mmuhamadamirzaidi.qwisapp.Student;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
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

import com.mmuhamadamirzaidi.qwisapp.Data.QuizDBContract;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentHomeActivity extends AppCompatActivity {

    TextView nameuser, walletuser, mainmenus, pagetitle, pagesubtitle;

    Button btn_play_offline;
    Animation atg, atgtwo, atgthree;
    ImageView imageView3;

    private CircleImageView ProfileImage, Online, Instructions, Statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        if (Build.VERSION.SDK_INT>=21) {
            setupWindowAnimations();
        }

        atg = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_one);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_two);
        atgthree = AnimationUtils.loadAnimation(this, R.anim.animation_dashboard_three);

        nameuser = findViewById(R.id.nameuser);
        walletuser = findViewById(R.id.walletuser);

        imageView3 = findViewById(R.id.imageView3);

//        review = findViewById(R.id.review);
        mainmenus = findViewById(R.id.mainmenus);

        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);

        btn_play_offline = findViewById(R.id.btn_play_offline);

        btn_play_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(StudentHomeActivity.this, "Play Offline Mode", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(StudentHomeActivity.this, StudentQuizActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // pass an animation
        imageView3.startAnimation(atg);

        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);

        btn_play_offline.startAnimation(atgthree);

        //Main Menus Button
        ProfileImage = (CircleImageView) findViewById(R.id.imageView2);

        Online = (CircleImageView) findViewById(R.id.online);
        Instructions = (CircleImageView) findViewById(R.id.instructions);
        Statistics = (CircleImageView) findViewById(R.id.statistics);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StudentHomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
            }
        });

        Instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(StudentHomeActivity.this, "Instruction", Toast.LENGTH_SHORT).show();

                Intent instructionsIntent = new Intent(StudentHomeActivity.this, StudentInstructionActivity.class);
                startActivity(instructionsIntent);
            }
        });

        Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(StudentHomeActivity.this, "Online Mode", Toast.LENGTH_SHORT).show();

                Intent onlineIntent = new Intent(StudentHomeActivity.this, StudentCategoryActivity.class);
                startActivity(onlineIntent);
            }
        });

        Statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(StudentHomeActivity.this, "Statistics", Toast.LENGTH_SHORT).show();

                Cursor cursor = getContentResolver().query(QuizDBContract.QuizEntry.CONTENT_URI, new String[]{QuizDBContract.QuizEntry._ID},
                        null, null, null);
                int cursorCount = 0;
                if (cursor.moveToFirst()){
                    cursorCount = cursor.getCount();
                }
                if (cursorCount<=0){
                    Toast.makeText(StudentHomeActivity.this, R.string.no_stats, Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(StudentHomeActivity.this, StudentStatisticsActivity.class);
                    startActivity(intent);
                }
                cursor.close();
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

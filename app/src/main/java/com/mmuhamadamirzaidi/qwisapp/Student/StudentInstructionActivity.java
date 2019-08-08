package com.mmuhamadamirzaidi.qwisapp.Student;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Adapters.InstructionsViewPagerAdapter;
import com.mmuhamadamirzaidi.qwisapp.Model.ScreenItem;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;
import java.util.List;

public class StudentInstructionActivity extends AppCompatActivity {

    private ViewPager screenPager;
    InstructionsViewPagerAdapter instructionsViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnUnderstand;
    Animation btnAnim ;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_instruction);

        // init views
        btnNext = findViewById(R.id.btn_next);
        btnUnderstand = findViewById(R.id.btn_understand);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Start The Quiz","Tap the Play Quiz button to start the quiz. Every question provided with 40 seconds timer.",R.drawable.instructions_start));
        mList.add(new ScreenItem("Choose The Answer","Tap on the desired answer choice to advance the question. If the question not answered, you will be proceed to next question after the times up!",R.drawable.instructions_choose));
        mList.add(new ScreenItem("Score Many Points","Every correct answer rewards you with 1 mark. Think wisely, choose carefully. Goodluck!",R.drawable.instructions_score));
        mList.add(new ScreenItem("Warning!","If you leave the app while attempting the quiz, the current quiz will be ended!",R.drawable.instructions_warning));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        instructionsViewPagerAdapter = new InstructionsViewPagerAdapter(this, mList);
        screenPager.setAdapter(instructionsViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);


                }

                if (position == mList.size()-1) { // when we rech to the last screen

                    // TODO : show the GETSTARTED Button and hide the indicator and the next button

                    loadLastScreen();


                }



            }
        });

        // tablayout add change listener


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size()-1) {

                    loadLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        // Get Started button click listener

        btnUnderstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //open main activity

                Intent dashboardActivity = new Intent(getApplicationContext(), StudentHomeActivity.class);
                dashboardActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(dashboardActivity);
                finish();



            }
        });

        // skip button click listener

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnUnderstand.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnUnderstand.setAnimation(btnAnim);



    }
}

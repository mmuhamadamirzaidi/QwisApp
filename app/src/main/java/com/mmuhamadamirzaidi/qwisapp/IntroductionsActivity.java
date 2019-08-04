package com.mmuhamadamirzaidi.qwisapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Adapters.IntroViewPagerAdapter;
import com.mmuhamadamirzaidi.qwisapp.Model.ScreenItem;
import com.mmuhamadamirzaidi.qwisapp.Student.StudentHomeActivity;

import java.util.ArrayList;
import java.util.List;

public class IntroductionsActivity extends AppCompatActivity {
  private ViewPager screenPager;
  IntroViewPagerAdapter introViewPagerAdapter;
  TabLayout tabIndicator;
  Button btnNext;
  int position = 0;
  Button btnGetStarted;
  Animation btnAnim;
  TextView tvSkip;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_introductions);

    if (restorePrefData()) {

      Intent mainActivity = new Intent(getApplicationContext(), StudentHomeActivity.class);
      startActivity(mainActivity);
      finish();
    }

    // ini views
    btnNext = findViewById(R.id.btn_next);
    btnGetStarted = findViewById(R.id.btn_get_started);
    tabIndicator = findViewById(R.id.tab_indicator);
    btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
    tvSkip = findViewById(R.id.tv_skip);

    // fill list screen

    final List<ScreenItem> mList = new ArrayList<>();
    mList.add(new ScreenItem("Title 1", "Descriptions 1", R.drawable.introductions_intro));
    mList.add(new ScreenItem("Title 2", "Descriptions 2", R.drawable.introductions_components));
    mList.add(new ScreenItem("Title 3", "Descriptions 3. Goodluck!", R.drawable.introductions_instructions));

    // setup viewpager
    screenPager = findViewById(R.id.screen_viewpager);
    introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
    screenPager.setAdapter(introViewPagerAdapter);

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

        if (position == mList.size() - 1) { // when we rech to the last screen

          // TODO : show the GETSTARTED Button and hide the indicator and the next button

          loadLastScreen();
        }
      }
    });

    // tablayout add change listener


    tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {

        if (tab.getPosition() == mList.size() - 1) {

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

    btnGetStarted.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        //open main activity

        Intent dashboardActivity = new Intent(getApplicationContext(), AdminHomeActivity.class);
        startActivity(dashboardActivity);
        // also we need to save a boolean value to storage so next time when the user run the app
        // we could know that he is already checked the intro screen activity
        // i'm going to use shared preferences to that process
        savePrefsData();
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

  private boolean restorePrefData() {


    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
    Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpened", false);
    return isIntroActivityOpnendBefore;
  }

  private void savePrefsData() {

    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();
    editor.putBoolean("isIntroOpened", true);
    editor.commit();


  }

  // show the GETSTARTED Button and hide the indicator and the next button
  private void loadLastScreen() {

    btnNext.setVisibility(View.INVISIBLE);
    btnGetStarted.setVisibility(View.VISIBLE);
    tvSkip.setVisibility(View.INVISIBLE);
    tabIndicator.setVisibility(View.INVISIBLE);
    // TODO : ADD an animation the getstarted button
    // setup animation
    btnGetStarted.setAnimation(btnAnim);


  }
}
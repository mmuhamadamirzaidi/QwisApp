package com.mmuhamadamirzaidi.qwisapp.Lecturer;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mmuhamadamirzaidi.qwisapp.CategoryFragment;
import com.mmuhamadamirzaidi.qwisapp.ProfileFragment;
import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.RankingFragment;

public class LecturerHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_home);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFragment = null;

                switch (menuItem.getItemId())
                {
                    case R.id.action_category:
                        selectedFragment = CategoryFragment.newInstance();
                        break;

                    case R.id.action_ranking:
                        selectedFragment = RankingFragment.newInstance();
                        break;

                    case R.id.action_student:
                        selectedFragment = LecturerStudentFragment.newInstance();
                        break;

                    case R.id.action_post:
                        selectedFragment = LecturerPostFragment.newInstance();
                        break;

                    case R.id.action_profile:
                        selectedFragment = ProfileFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();

                return true;
            }
        });
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }
}

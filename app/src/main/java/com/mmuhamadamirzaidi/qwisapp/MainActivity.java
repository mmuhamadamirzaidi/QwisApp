package com.mmuhamadamirzaidi.qwisapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button CategoryMenu, RankingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CategoryMenu = (Button)findViewById(R.id.categoryMenu);
        RankingMenu = (Button)findViewById(R.id.rankingMenu);

        CategoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(MainActivity.this, Home.class);
                startActivity(categoryIntent);
            }
        });

        RankingMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Ranking page not available yet!", Toast.LENGTH_SHORT).show();

                Intent categoryIntent = new Intent(MainActivity.this, RankingFragment.class);
                startActivity(categoryIntent);
            }
        });

    }

//    private void SendUserToSetupActivity() {
//        Intent setupIntent = new Intent(MainActivity.this, SetupProfileActivity.class);
//        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(setupIntent);
//        finish();
//    }

    private void SendUserToSignInActivity() {

        Intent signinIntent = new Intent(MainActivity.this, SignInActivity.class);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signinIntent);
        finish();
    }
}
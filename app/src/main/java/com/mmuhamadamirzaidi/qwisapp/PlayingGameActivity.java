package com.mmuhamadamirzaidi.qwisapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;

public class PlayingGameActivity extends AppCompatActivity implements View.OnClickListener{

    final static long INTERVAL = 1000; //1 second
    final static long TIMEOUT = 11000; //11 second, must extra 1 second
    int progressValue = 0, downtime = 10;

    CountDownTimer mCountDown;

    int index = 0, score =0, thisQuestion = 0, totalQuestion = 0, correctAnswer;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnA, btnB, btnC, btnD;
    TextView txtScore, txtQuestionNum, question_text, countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_game);

        //Views
        countdown = (TextView)findViewById(R.id.countdown);
        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        question_text = (TextView)findViewById(R.id.question_text);
//        question_image =(ImageView)findViewById(R.id.question_image);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();
        if (index < totalQuestion) { //Still have questions in list
            //Choose correct answer
            score+=10;
            correctAnswer++;
            showQuestions(++index); //Next questions
        }
        else{
            //Choose wrong answer
            Intent intent = new Intent(this, DoneGameActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
        txtScore.setText(String.format("CURRENT SCORE : %d", score));
    }

    private void showQuestions(int index) {
        if (index < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("QUESTIONS : %d / %d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;
            downtime = 10;

//            if (Common.ListQuestion.get(index).getIsImageQuestion().equals("true")){
//                //If question is image
//                Picasso.get().load(Common.ListQuestion.get(index).getQuestion()).into(question_image);
//
//                question_image.setVisibility(View.VISIBLE);
//                question_text.setVisibility(View.INVISIBLE);
//            }
//            else{
//                question_text.setText(Common.ListQuestion.get(index).getQuestion());
//
//                question_image.setVisibility(View.INVISIBLE);
//                question_text.setVisibility(View.VISIBLE);
//            }
            if (Common.ListQuestion.get(index).getIsImageQuestion().equals("false")){
                question_text.setText(Common.ListQuestion.get(index).getQuestion());
            }
            btnA.setText(Common.ListQuestion.get(index).getAnswerA());
            btnB.setText(Common.ListQuestion.get(index).getAnswerB());
            btnC.setText(Common.ListQuestion.get(index).getAnswerC());
            btnD.setText(Common.ListQuestion.get(index).getAnswerD());

            mCountDown.start(); //Start timer countdown
        }
        else{
            //If it is final question
            Intent intent = new Intent(this, DoneGameActivity.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        totalQuestion = Common.ListQuestion.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long minisec) {
                progressBar.setProgress(progressValue);
                progressValue++;
                downtime--;
                countdown.setText(String.format("%d", downtime));
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestions(++index);
            }
        };
        showQuestions(index);
    }
}

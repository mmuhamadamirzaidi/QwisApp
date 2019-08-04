package com.mmuhamadamirzaidi.qwisapp.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.widget.TextView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.qwisapp.Adapters.CardAdapter;
import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.Scoring.QuestionScorer;
import com.mmuhamadamirzaidi.qwisapp.Scoring.QuizScorer;

import java.util.ArrayList;

public class StudentPostQuizActivity extends AppCompatActivity {

    private ArrayList<QuestionScorer> mQuestionScorers;
    private int mQuizSize;
    private QuizScorer mQuizScorer;
    private int mQuizNumber;

//    private Button btnFinishQuiz;

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;

    public static final String KEY_QUIZ_NUMBER = "questionScorers";
    public static final String KEY_QUIZ_SIZE = "quizSize";

    public static StudentPostQuizActivity getInstance(ArrayList<QuestionScorer> questionScorers){
        StudentPostQuizActivity studentPostQuizActivity = new StudentPostQuizActivity();
        studentPostQuizActivity.mQuestionScorers = questionScorers;
        return studentPostQuizActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post_quiz);

        mRecyclerView = (RecyclerView) findViewById(R.id.cards_listview);
//        mRecyclerView.setDividerHeight(Float.valueOf(getResources().getDimension(R.dimen.activity_vertical_margin)).intValue());
        Intent intent = getIntent();
        mQuizSize = intent.getIntExtra(KEY_QUIZ_SIZE, 10);
        mQuizNumber = intent.getIntExtra(KEY_QUIZ_NUMBER, 0);
        mQuizScorer = QuizScorer.getInstance(this, mQuizSize, mQuizNumber);
        mQuestionScorers = mQuizScorer.getQuestionScorers();
        try {
            TextView scoreView = (TextView) findViewById(R.id.scoreTextView);
            scoreView.setText(Integer.toString(mQuizScorer.scoreQuiz(mQuestionScorers)));
            scoreView.setTextSize(getResources().getDimension(R.dimen.score_textsize));
            ((TextView) findViewById(R.id.quizLengthTextView)).setText(Integer.toString(mQuestionScorers.size()));
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mCardAdapter = new CardAdapter(this, mQuestionScorers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        SnapHelper helper = new LinearSnapHelper();
        mRecyclerView.setAdapter(mCardAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        helper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        ((TextView)findViewById(R.id.timeright_textview)).setText(getString(R.string.format_seconds, mQuizScorer.getTimeAverageCorrect()));
        ((TextView)findViewById(R.id.timewrong_textview)).setText(getString(R.string.format_seconds, mQuizScorer.getTimeAverageWrong()));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SendUserToMainActivity();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(StudentPostQuizActivity.this, StudentHomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

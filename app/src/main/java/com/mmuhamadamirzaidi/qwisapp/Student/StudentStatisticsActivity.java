package com.mmuhamadamirzaidi.qwisapp.Student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.Statistics.CategoryStatisticsCalculator;
import com.mmuhamadamirzaidi.qwisapp.Statistics.OverallStatisticsCalculator;

import java.util.ArrayList;

public class StudentStatisticsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    Double totalQuizzes;
    Double highestScore;
    Double perfectQuizzes;
    Double averageScore;
    Double averageTimeRight;
    Double averageTimeWrong;

    Integer bestCategoryPercent;
    Integer worstCategoryPercent;
    String bestCategoryString;
    String worstCategoryString;

    LinearLayout catLinearLayout;

    TextView catTotalquestions;
    TextView catCorrectAnswers;
    TextView catPercentage;
    TextView catTimeoverall;
    TextView catTimecorrect;
    TextView catTimewrong;

    ArrayList<double[]> categoryScoreReports;


    private static final String KEY_TOTAL_QUIZZES = "totalQuizzes";
    private static final String KEY_HIGHEST_SCORE = "highestScore";
    private static final String KEY_PERFECT_QUIZZES = "perfectQuizzes";
    private static final String KEY_AVERAGE_SCORE = "averageScore";
    private static final String KEY_AVERAGE_TIME_RIGHT = "averageTimeRight";
    private static final String KEY_AVERAGE_TIME_WRONG = "averageTimeWrong";
    private static final String KEY_BEST_CATEGORY_PERCENT = "bestCategoryPercent";
    private static final String KEY_WORST_CATEGORY_PERCENT = "worstCategoryPercent";
    private static final String KEY_BEST_CATEGORY_STRING = "bestCategoryString";
    private static final String KEY_WORST_CATEGORY_STRING = "worstCategoryString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_statistics);

        if (savedInstanceState == null){
            ArrayList<Double> overallStats = OverallStatisticsCalculator.getOverallPerformanceAndAverages(this.getApplicationContext());
            ArrayList<Integer> bestAndWorstCategoryPercentages = CategoryStatisticsCalculator.getBestAndWorstCategoryPercentages(this.getApplicationContext());
            ArrayList<String> bestAndWorstCategoryStrings = CategoryStatisticsCalculator.getBestAndWorstCategoryStrings(this.getApplicationContext());
            totalQuizzes = overallStats.get(OverallStatisticsCalculator.TOTAL_NUMBER_QUIZZES_TAKEN);
            highestScore = overallStats.get(OverallStatisticsCalculator.HIGHEST_SCORE);
            perfectQuizzes = overallStats.get(OverallStatisticsCalculator.PERFECT_QUIZZES);
            averageScore = overallStats.get(OverallStatisticsCalculator.AVERAGE_PERCENTAGE_SCORE);
            averageTimeRight = overallStats.get(OverallStatisticsCalculator.AVERAGE_TIME_CORRECT_ANSWERS);
            averageTimeWrong = overallStats.get(OverallStatisticsCalculator.AVERAGE_TIME_WRONG_ANSWERS);

            bestCategoryPercent = bestAndWorstCategoryPercentages.get(CategoryStatisticsCalculator.BEST);
            worstCategoryPercent = bestAndWorstCategoryPercentages.get(CategoryStatisticsCalculator.WORST);
            bestCategoryString = bestAndWorstCategoryStrings.get(CategoryStatisticsCalculator.BEST);
            worstCategoryString = bestAndWorstCategoryStrings.get(CategoryStatisticsCalculator.WORST);


        } else {
            totalQuizzes = savedInstanceState.getDouble(KEY_TOTAL_QUIZZES);
            highestScore = savedInstanceState.getDouble(KEY_HIGHEST_SCORE);
            perfectQuizzes = savedInstanceState.getDouble(KEY_PERFECT_QUIZZES);
            averageScore = savedInstanceState.getDouble(KEY_AVERAGE_SCORE);
            averageTimeRight = savedInstanceState.getDouble(KEY_AVERAGE_TIME_RIGHT);
            averageTimeWrong = savedInstanceState.getDouble(KEY_AVERAGE_TIME_WRONG);

            bestCategoryPercent = savedInstanceState.getInt(KEY_BEST_CATEGORY_PERCENT);
            worstCategoryPercent = savedInstanceState.getInt(KEY_WORST_CATEGORY_PERCENT);
            bestCategoryString = savedInstanceState.getString(KEY_BEST_CATEGORY_STRING);
            worstCategoryString = savedInstanceState.getString(KEY_WORST_CATEGORY_STRING);
        }


        try {

            //Set text
            ((TextView) findViewById(R.id.total_quizzes_content)).setText(getString(R.string.format_integer, totalQuizzes));
            ((TextView) findViewById(R.id.highest_score_content)).setText(getString(R.string.format_integer, highestScore));
            ((TextView) findViewById(R.id.perfect_quizzes_content)).setText(getString(R.string.format_integer, perfectQuizzes));
            ((TextView) findViewById(R.id.average_score)).setText(getString(R.string.format_percent, averageScore));
            ((TextView) findViewById(R.id.average_time_right)).setText(getString(R.string.format_seconds, averageTimeRight));
            ((TextView) findViewById(R.id.average_time_wrong)).setText(getString(R.string.format_seconds, averageTimeWrong));
        } finally {
            categoryScoreReports = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(this, totalQuizzes.intValue());
        }

//        catLinearLayout = (LinearLayout)findViewById(R.id.category_stats_linearlayout);
//        catTotalquestions = (TextView)findViewById(R.id.total_questions_answered);
//        catCorrectAnswers = (TextView)findViewById(R.id.correctly_answered);
//        catPercentage = (TextView)findViewById(R.id.percentage_score);
//        catTimeoverall = (TextView)findViewById(R.id.average_time_overall);
//        catTimecorrect = (TextView)findViewById(R.id.average_time_right_category);
//        catTimewrong = (TextView)findViewById(R.id.average_time_wrong_category);
//
//        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner_category);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_names, R.layout.spinner_text);
//        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
//        categorySpinner.setAdapter(adapter);
//        categorySpinner.setOnItemSelectedListener(this);
//
//        ((TextView)findViewById(R.id.best_category)).setText(getString(R.string.format_best_and_worst_categories, bestCategoryString, bestCategoryPercent));
//        ((TextView)findViewById(R.id.worst_category)).setText(getString(R.string.format_best_and_worst_categories, worstCategoryString, worstCategoryPercent));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putDouble(KEY_TOTAL_QUIZZES, totalQuizzes);
        outState.putDouble(KEY_HIGHEST_SCORE, highestScore);
        outState.putDouble(KEY_PERFECT_QUIZZES, perfectQuizzes);
        outState.putDouble(KEY_AVERAGE_SCORE, averageScore);
        outState.putDouble(KEY_AVERAGE_TIME_RIGHT, averageTimeRight);
        outState.putDouble(KEY_AVERAGE_TIME_RIGHT, averageTimeWrong);
        outState.putInt(KEY_BEST_CATEGORY_PERCENT, bestCategoryPercent);
        outState.putInt(KEY_WORST_CATEGORY_PERCENT, worstCategoryPercent);
        outState.putString(KEY_BEST_CATEGORY_STRING, bestCategoryString);
        outState.putString(KEY_WORST_CATEGORY_STRING, worstCategoryString);
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (catLinearLayout==null){
//            catLinearLayout = (LinearLayout)findViewById(R.id.category_stats_linearlayout);
//            catTotalquestions = (TextView)findViewById(R.id.total_questions_answered);
//            catCorrectAnswers = (TextView)findViewById(R.id.correctly_answered);
//            catPercentage = (TextView)findViewById(R.id.percentage_score);
//            catTimeoverall = (TextView)findViewById(R.id.average_time_overall);
//            catTimecorrect = (TextView)findViewById(R.id.average_time_right_category);
//            catTimewrong = (TextView)findViewById(R.id.average_time_wrong_category);
        }

        if (catLinearLayout.getVisibility()==View.GONE){
            catLinearLayout.setVisibility(View.VISIBLE);
        }

        if (categoryScoreReports==null){
            categoryScoreReports = CategoryStatisticsCalculator.getCategoryPerformanceReports();
        }

        double[] thiscategory = categoryScoreReports.get(i);

        double totalAnswered = thiscategory[CategoryStatisticsCalculator.TOTAL_QUESTIONS_ANSWERED];
        if (totalAnswered>0){
            catTotalquestions.setText(getString(R.string.format_integer, totalAnswered));
            catCorrectAnswers.setText(getString(R.string.format_integer, thiscategory[CategoryStatisticsCalculator.TOTAL_QUESTIONS_CORRECT]));
            catPercentage.setText(getString(R.string.format_percent, thiscategory[CategoryStatisticsCalculator.PERCENTAGE]));
            catTimeoverall.setText(getString(R.string.format_seconds, thiscategory[CategoryStatisticsCalculator.AVERAGE_TIME_OVERALL]));
            double timecorrect = thiscategory[CategoryStatisticsCalculator.AVERAGE_TIME_CORRECT];
            double timewrong = thiscategory[CategoryStatisticsCalculator.AVERAGE_TIME_WRONG];
            if (timecorrect > 0) {
                catTimecorrect.setText(getString(R.string.format_seconds, timecorrect));
            } else {
                catTimecorrect.setText(getString(R.string.minus_sign));
            }
            if (timewrong > 0) {
                catTimewrong.setText(getString(R.string.format_seconds, timewrong));
            } else {
                catTimewrong.setText(getString(R.string.minus_sign));
            }
        } else {
            catTotalquestions.setText(getString(R.string.format_integer, totalAnswered));
            catCorrectAnswers.setText(getString(R.string.minus_sign));
            catPercentage.setText(getString(R.string.minus_sign));
            catTimeoverall.setText(getString(R.string.minus_sign));
            catTimecorrect.setText(getString(R.string.minus_sign));
            catTimewrong.setText(getString(R.string.minus_sign));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onPause() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onPause();
    }
}

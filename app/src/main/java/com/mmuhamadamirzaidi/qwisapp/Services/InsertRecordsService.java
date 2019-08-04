package com.mmuhamadamirzaidi.qwisapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;
import com.mmuhamadamirzaidi.qwisapp.Scoring.QuizScorer;
import com.mmuhamadamirzaidi.qwisapp.Statistics.CategoryStatisticsCalculator;

public class InsertRecordsService extends IntentService {

    private static final String LOG_TAG = "InsertRecordsService";
    private int mQuizNumber;
    private int mQuizSize;
    private QuizScorer mQuizScorer;

    public static final String EXTRA_SERVICE_QUIZ_NUMBER = "servicequiznumber";
    public static final String EXTRA_SERICE_QUIZ_SIZE = "servicequizsize";

    public InsertRecordsService() {
        super("InsertRecordsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mQuizNumber = intent.getIntExtra(EXTRA_SERVICE_QUIZ_NUMBER, QuizScorer.sQuizNumber);
        mQuizSize = intent.getIntExtra(EXTRA_SERICE_QUIZ_SIZE, 10);
        mQuizScorer = QuizScorer.getInstance(this.getApplicationContext(), mQuizSize, mQuizNumber);
        Uri uri = QuizScorer.createAndInsertQuizRecord(this.getApplicationContext(), mQuizScorer);
        int categories_updated = QuizScorer.createAndUpdateCategoryRecords(this.getApplicationContext(), mQuizScorer);

        if (uri==null || categories_updated< IndividualQuestion.categoryList.size()){
//            Log.d(LOG_TAG, "records insertion problem");
        }

        CategoryStatisticsCalculator.calculateCategoryPerformanceReports(this, mQuizNumber);
    }
}

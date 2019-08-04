package com.mmuhamadamirzaidi.qwisapp.Statistics;

import android.content.Context;
import android.database.Cursor;

import com.mmuhamadamirzaidi.qwisapp.Data.QuizDBContract;
import com.mmuhamadamirzaidi.qwisapp.Utility;

import java.util.ArrayList;

public class OverallStatisticsCalculator {
    private static final String LOG_TAG = "OverallStatsCalculator";

    private static final String[] CALCULATOR_COLUMNS = new String[]{
            QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE,
            QuizDBContract.QuizEntry.COLUMN_NAME_SCORE,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG
    };

    private static final int QUIZSIZE = 0;
    private static final int SCORE = 1;
    private static final int OVERALL_TIME = 2;
    private static final int TIME_CORRECT = 3;
    private static final int TIME_WRONG = 4;

    public static final int TOTAL_NUMBER_QUIZZES_TAKEN = 0;
    public static final int HIGHEST_SCORE = 1;
    public static final int HIGHEST_SCORE_OCCURRENCE = 6;
    public static final int PERFECT_QUIZZES = 2;
    public static final int AVERAGE_PERCENTAGE_SCORE = 3;
    public static final int AVERAGE_TIME_CORRECT_ANSWERS = 4;
    public static final int AVERAGE_TIME_WRONG_ANSWERS = 5;


    public static ArrayList<Double> getOverallPerformanceAndAverages(Context context){
        ArrayList<Double> returnList = new ArrayList<>(6);
        Cursor c = context.getContentResolver().query(QuizDBContract.QuizEntry.CONTENT_URI, CALCULATOR_COLUMNS, null, null, null);
        try {
            if (c != null) {
                int cursorcount = c.getCount();
                int[] quizsize_list = new int[cursorcount];
                int[] score_list = new int[cursorcount];
                double[] overalltime_list = new double[cursorcount];
                double[] timecorrect_list = new double[cursorcount];
                double[] timewrong_list = new double[cursorcount];
                if (c.moveToFirst()) {
                    do {
                        int currentposition = c.getPosition();
                        quizsize_list[currentposition] = c.getInt(QUIZSIZE);
                        score_list[currentposition] = c.getInt(SCORE);
                        overalltime_list[currentposition] = c.getDouble(OVERALL_TIME);
                        timecorrect_list[currentposition] = c.getDouble(TIME_CORRECT);
                        timewrong_list[currentposition] = c.getDouble(TIME_WRONG);
                    } while (c.moveToNext());
                }
                returnList.add(TOTAL_NUMBER_QUIZZES_TAKEN, Double.valueOf(cursorcount));
                ArrayList<Integer> maxArray = Utility.getMaxWithOccurrence(score_list);
                returnList.add(HIGHEST_SCORE, Double.valueOf(maxArray.get(Utility.INDEX_MAX)));
                returnList.add(PERFECT_QUIZZES, getNumberOfPerfectQuizzes(score_list, quizsize_list));
                try {
                    returnList.add(AVERAGE_PERCENTAGE_SCORE, Utility.getAveragePercentageFromListsOfActualAndTotal(score_list, quizsize_list));
                } catch (Exception e){
//                    Log.d(LOG_TAG, e.getMessage());
                }
                returnList.add(AVERAGE_TIME_CORRECT_ANSWERS, Utility.getAverageFromDoubleListWITHNegativeValueElimination(timecorrect_list));
                returnList.add(AVERAGE_TIME_WRONG_ANSWERS, Utility.getAverageFromDoubleListWITHNegativeValueElimination(timewrong_list));
                returnList.add(HIGHEST_SCORE_OCCURRENCE, Double.valueOf(maxArray.get(Utility.INDEX_OCCURRENCE)));
            }
        } finally {
            c.close();
        }
        return returnList;
    }

    static double getNumberOfPerfectQuizzes(int[] scorelist, int[] quizsizelist){
        double perfectCount = 0.0;

        if (scorelist.length!=quizsizelist.length){
            return -1;
        }

        for (int i = 0; i < quizsizelist.length; i++){
            if (scorelist[i] == quizsizelist[i]){
                perfectCount += 1;
            }
        }

        return perfectCount;
    }
}
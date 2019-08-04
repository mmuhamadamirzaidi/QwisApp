package com.mmuhamadamirzaidi.qwisapp.Statistics;

import android.content.Context;
import android.database.Cursor;

import com.mmuhamadamirzaidi.qwisapp.Data.QuizDBContract;
import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;
import com.mmuhamadamirzaidi.qwisapp.Utility;

import java.util.ArrayList;

public class CategoryStatisticsCalculator {
    private static final String LOG_TAG = "CategoryStatsCalculator";

    private static final String[] CATEGORY_COLUMNS = new String[]{
            QuizDBContract.CategoryEntry.COLUMN_NAME_NAME,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG
    };

    private static final int COL_NAME = 0;
    private static final int COL_QUESTIONS_ANSWERED = 1;
    private static final int COL_CORRECTLY_ANSWERED = 2;
    private static final int COL_TIME_OVERALL = 3;
    private static final int COL_TIME_CORRECT = 4;
    private static final int COL_TIME_WRONG = 5;

    public static int BEST = 0;
    public static int WORST = 1;

    public static ArrayList<double[]> getCategoryPerformanceReports() {
        return sCategoryPerformanceReports;
    }

    private static ArrayList<double[]> sCategoryPerformanceReports;
    private static int sNumQuizzes = 0;

    public static final int TOTAL_QUESTIONS_ANSWERED = 0;
    public static final int TOTAL_QUESTIONS_CORRECT = 1;
    public static final int PERCENTAGE = 2;
    public static final int AVERAGE_TIME_OVERALL = 3;
    public static final int AVERAGE_TIME_CORRECT = 4;
    public static final int AVERAGE_TIME_WRONG = 5;

    public static ArrayList<double[]> calculateCategoryPerformanceReports(Context context, int numberOfQuizzes){
        if (sNumQuizzes == numberOfQuizzes && sCategoryPerformanceReports !=null){
            return sCategoryPerformanceReports;
        }
        sNumQuizzes = numberOfQuizzes;
        Cursor c = context.getApplicationContext().getContentResolver().query(QuizDBContract.CategoryEntry.CONTENT_URI, CATEGORY_COLUMNS, null, null, null);

        if (c.moveToFirst()){
            sCategoryPerformanceReports = new ArrayList<>(IndividualQuestion.categoryList.size());
            do {
                double[] thisCategory = new double[6];
                int totalAnswered = c.getInt(COL_QUESTIONS_ANSWERED);
                if (totalAnswered == 0){
                    for (int i = 0; i < thisCategory.length; i++){
                        thisCategory[i] = 0;
                    }
                } else {
                    int correctAnswered = c.getInt(COL_CORRECTLY_ANSWERED);
                    thisCategory[TOTAL_QUESTIONS_ANSWERED] = totalAnswered;
                    thisCategory[TOTAL_QUESTIONS_CORRECT] = correctAnswered;
                    thisCategory[PERCENTAGE] = Utility.calculatePercentage(totalAnswered, correctAnswered);
                    thisCategory[AVERAGE_TIME_OVERALL] = (c.getDouble(COL_TIME_OVERALL)/totalAnswered);
                    if (correctAnswered > 0) {
                        thisCategory[AVERAGE_TIME_CORRECT] = (c.getDouble(COL_TIME_CORRECT) / correctAnswered);
                    } else {
                        thisCategory[AVERAGE_TIME_CORRECT] = -1;
                    }
                    if ((totalAnswered - correctAnswered) > 0){
                        thisCategory[AVERAGE_TIME_WRONG] = (c.getDouble(COL_TIME_WRONG) / (totalAnswered - correctAnswered));
                    } else {
                        thisCategory[AVERAGE_TIME_WRONG] = -1;
                    }
                }
                sCategoryPerformanceReports.add(c.getPosition(), thisCategory);
                c.moveToNext();
            } while (!c.isAfterLast());
        }


        return sCategoryPerformanceReports;
    }

    public static ArrayList<String> getBestAndWorstCategoryStrings(Context context){
        ArrayList<String> bestAndWorst = new ArrayList<>(2);
        Cursor c = context.getContentResolver().query(QuizDBContract.CategoryEntry.CONTENT_URI, CATEGORY_COLUMNS, null, null, QuizDBContract.CategoryEntry._ID + " ASC");
        double best = 0;
        double worst = 100;
        String bestName = null;
        String worstName = null;
        String commaSep = ", ";
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                int total = c.getInt(COL_QUESTIONS_ANSWERED);
//                Log.d("wtf" + Integer.toString(c.getPosition()),"total = " + Integer.toString(total));
                int correct = c.getInt(COL_CORRECTLY_ANSWERED);
//                Log.d("wtf","correct = " + Integer.toString(correct));
                double percentage = Utility.calculatePercentage(total,correct);
                if (percentage >= best){
                    if (percentage > best) {
                        bestName = c.getString(COL_NAME);
                        best = percentage;
                    } else if (percentage == best){
                        if (null != bestName){
                            bestName += (commaSep + c.getString(COL_NAME));
                        }
                    }
                } else if (percentage <= worst){
                    if (percentage < worst) {
                        worstName = c.getString(COL_NAME);
                        worst = percentage;
                    } else if (percentage == worst){
                        if (null != worstName){
                            worstName += (commaSep + c.getString(COL_NAME));
                        }
                    }
                }
                c.moveToNext();
            }
        }
        bestAndWorst.add(BEST, bestName);
        bestAndWorst.add(WORST, worstName);
        return bestAndWorst;
    }

    public static ArrayList<Integer> getBestAndWorstCategoryPercentages(Context context){
        ArrayList<Integer> bestAndWorst = new ArrayList<>(2);
        Cursor c = context.getContentResolver().query(QuizDBContract.CategoryEntry.CONTENT_URI, CATEGORY_COLUMNS, null, null, QuizDBContract.CategoryEntry._ID + " ASC");
        double best = 0;
        double worst = 100;
        String commaSep = ", ";
        if (c.moveToFirst()){
            while (!c.isAfterLast()){
                int total = c.getInt(COL_QUESTIONS_ANSWERED);
                int correct = c.getInt(COL_CORRECTLY_ANSWERED);
                double percentage = Utility.calculatePercentage(total,correct);
                if (percentage >= best){
                    best = percentage;
                } else if (percentage <= worst){
                    worst = percentage;
                }
                c.moveToNext();
            }
        }
        bestAndWorst.add(BEST, Double.valueOf(best).intValue());
        bestAndWorst.add(WORST, Double.valueOf(worst).intValue());
        return bestAndWorst;
    }
}
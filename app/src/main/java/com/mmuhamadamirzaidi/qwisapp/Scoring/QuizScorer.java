package com.mmuhamadamirzaidi.qwisapp.Scoring;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.mmuhamadamirzaidi.qwisapp.Data.QuizDBContract;
import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;
import com.mmuhamadamirzaidi.qwisapp.Utility;

import java.util.ArrayList;

import static com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion.categoryList;

public class QuizScorer {
    private static final String LOG_TAG = "QuizScorer";
    private static QuizScorer sQuizScorer;
    private Context mContext;
    private int size;
    public static int sQuizNumber = 0;
    private ArrayList<QuestionScorer> mQuestionScorers;
    private int currentQuestionCount;
    private ArrayList<int[]> mCategoryScoreReport;
    private ArrayList<int[]> mOverallTimeReport;
    private ArrayList<int[]> mCategoryTotalTimeReport;
    private ArrayList<double[]> mCategoryAverageTimeReport;

//    public ArrayList<int[]> getCategoryTotalTimeReport(){
//        if (mCategoryTotalTimeReport==null){
//            mCategoryTotalTimeReport = getCategoryTotalTimeReport();
//        }
//    }

    public static final int SCORES_TOTAL_CATEGORY_QUESTIONS = 0;
    public static final int SCORES_CORRECT_CATEGORY_ANSWERS = 1;

    public static final int TIMES_CORRECT_OVERALL = 0;
    public static final int TIMES_WRONG_OVERALL = 1;
    public static final int TIMES_OVERALL_BY_CATEGORY = 2;
    public static final int TIMES_CORRECT_BY_CATEGORY = 0;
    public static final int TIMES_WRONG_BY_CATEGORY = 1;

    public static final String[] categoryQueryProjection = new String[]{
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG,
            QuizDBContract.CategoryEntry.COLUMN_NAME_NAME
    };

    public static final int COL_TOTAL_QUESTIONS_ANSWERED = 0;
    public static final int COL_CORRECTLY_ANSWERED = 1;
    public static final int COL_TOTAL_TIME_OVERALL = 2;
    public static final int COL_TOTAL_TIME_CORRECT = 3;
    public static final int COL_TOTAL_TIME_WRONG = 4;

    private QuizScorer(Context context, int size, ArrayList<QuestionScorer> questionScorers) {
        this.mContext = context;
        this.size = size;
        this.mQuestionScorers = questionScorers;
    }

    private QuizScorer(int size, Context context) {
        this.size = size;
        this.mQuestionScorers = new ArrayList<QuestionScorer>(size);
        mContext = context;
    }

    //static factory method to get current context's instance of QuizScorer or construct one and return it
    // (destroys previous static instance)
    public static QuizScorer getInstance(Context context, int size, int quizNumber){
        if (sQuizScorer==null || sQuizNumber != quizNumber){
            sQuizScorer = new QuizScorer(size, context);
            sQuizScorer.currentQuestionCount = 0;
            sQuizNumber = quizNumber;
        }
        if (!context.equals(sQuizScorer.mContext)){
            sQuizScorer.mContext = context;
        }
        return sQuizScorer;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.mQuestionScorers = new ArrayList<QuestionScorer>(size);
    }

    public ArrayList<QuestionScorer> getQuestionScorers() {
        return mQuestionScorers;
    }

    public void setQuestionScorers(ArrayList<QuestionScorer> questionScorers) {
        mQuestionScorers = questionScorers;
    }

    //create content values for insertion into quizEntry table in database
    public static ContentValues createQuizRecordContentValues(Context context, QuizScorer thisScorer) throws Exception{
        if (thisScorer.size - thisScorer.currentQuestionCount > 1){
            throw new Exception("createQuizRecordContentValues() called before quiz completion");
        }
        ContentValues quizValues = null;
        try {
            int quizSize = thisScorer.size;
            int score = thisScorer.scoreQuiz(thisScorer.mQuestionScorers);
            double timeAverageOverall = thisScorer.getTimeAverageAllQuestions();
            double timeAverageCorrect = thisScorer.getTimeAverageCorrect();
            double timeAverageWrong = thisScorer.getTimeAverageWrong();
            quizValues = new ContentValues();
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE, quizSize);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_SCORE, score);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL, timeAverageOverall);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT, timeAverageCorrect);
            quizValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG, timeAverageWrong);
        } catch (Exception e){
//            Log.d(LOG_TAG, "Error at addquizrecord(): " + e.getMessage());
        }
        return quizValues;
    }

    //create an arraylist of contentvalues that correspond to the question categories, for updating category records
    private static ArrayList<ContentValues> createAllCategoryRecordContentValues(Context context, QuizScorer thisScorer) throws Exception{
        if (thisScorer.size - thisScorer.currentQuestionCount > 1){
            throw new Exception("createCategoryRecordContentValues() called before quiz completion");
        }
//        Log.d(LOG_TAG, "checkpoint 1");
        ArrayList<ContentValues> returnList = new ArrayList<>(categoryList.size());

        if (thisScorer.mCategoryTotalTimeReport==null){
            thisScorer.mCategoryTotalTimeReport = thisScorer.getCategoryTotalTimeReport();
//            Log.d(LOG_TAG, "checkpoint 1.0");
        }
        if (thisScorer.mCategoryScoreReport == null){
//            Log.d(LOG_TAG, "checkpoint 1.05");
            thisScorer.mCategoryScoreReport = thisScorer.getCategoryScoreReport();
//            Log.d(LOG_TAG, "checkpoint 1.1");
        }
        Cursor c;
        c = context.getContentResolver().query(QuizDBContract.CategoryEntry.CONTENT_URI, categoryQueryProjection, null, null, QuizDBContract.CategoryEntry._ID +" ASC");
//        Log.d(LOG_TAG, "checkpoint 2");
        try {
            if (c.moveToFirst()) {
                do {
                    int currentPosition = c.getPosition();

                    int totalAnswered = c.getInt(COL_TOTAL_QUESTIONS_ANSWERED);
                    int correctlyAnswered = c.getInt(COL_CORRECTLY_ANSWERED);
                    int totalTimeOverall = c.getInt(COL_TOTAL_TIME_OVERALL);
                    int totalTimeCorrect = c.getInt(COL_TOTAL_TIME_CORRECT);
                    int totalTimeWrong = c.getInt(COL_TOTAL_TIME_WRONG);
//                    Log.d(LOG_TAG, "checkpoint 3");

                    int newCorrectAnswer = correctlyAnswered + thisScorer.mCategoryScoreReport.get(SCORES_CORRECT_CATEGORY_ANSWERS)[currentPosition];
//                    Log.d(LOG_TAG + c.getString(5), "newCorrectAnswer = " + Integer.toString(newCorrectAnswer));
                    int newTotalAnswer = totalAnswered + thisScorer.mCategoryScoreReport.get(SCORES_TOTAL_CATEGORY_QUESTIONS)[currentPosition];
//                    Log.d(LOG_TAG + c.getString(5), "newTotalAnswer = " + Integer.toString(newTotalAnswer));
                    int newWrongAnswer = newTotalAnswer - newCorrectAnswer;
                    int newTotalTime = totalTimeOverall + thisScorer.mCategoryTotalTimeReport.get(TIMES_OVERALL_BY_CATEGORY)[currentPosition];
//                    Log.d(LOG_TAG + c.getString(5), "newTotalTime = " + Integer.toString(newTotalTime));
                    int newCorrectTime = totalTimeCorrect + thisScorer.mCategoryTotalTimeReport.get(TIMES_CORRECT_BY_CATEGORY)[currentPosition];
//                    Log.d(LOG_TAG + c.getString(5), "newCorrectTime = " + Integer.toString(newCorrectTime));
                    int newWrongTime = totalTimeWrong + thisScorer.mCategoryTotalTimeReport.get(TIMES_WRONG_BY_CATEGORY)[currentPosition];
//                    Log.d(LOG_TAG + c.getString(5), "newWrongTime = " + Integer.toString(newWrongTime));
//                    Log.d(LOG_TAG, "checkpoint 4");

                    ContentValues cv = new ContentValues();
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED, newTotalAnswer);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED, newCorrectAnswer);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL, newTotalTime);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT, newCorrectTime);
                    cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG, newWrongTime);
//                    Log.d(LOG_TAG, "checkpoint 5");

                    returnList.add(currentPosition, cv);
//                    Log.d(LOG_TAG, "checkpoint 6");
                    c.moveToNext();
                }
                while (c.getPosition()< categoryList.size());
            }
        } finally {
//            Log.d(LOG_TAG, "checkpoint 7");
//            Log.d(LOG_TAG, "content values array list is " + Integer.toString(returnList.size()));
            c.close();
        }
        return returnList;
    }

    @Nullable
    public static Uri createAndInsertQuizRecord(Context context, QuizScorer thisScorer){
        ContentValues contentValues = null;
        Uri retUri = null;
        try {
            contentValues = createQuizRecordContentValues(context, thisScorer);
            retUri  = context.getContentResolver().insert(QuizDBContract.QuizEntry.CONTENT_URI, contentValues);
        } catch (Exception e){
//            Log.d(LOG_TAG, "error while inserting quiz record: " + e);
        }
        return retUri;
    }

    public static int createAndUpdateCategoryRecords(Context context, QuizScorer thisScorer){
        int retInt = 0;
        try {
            ArrayList<ContentValues> contentValuesArrayList = createAllCategoryRecordContentValues(context, thisScorer);
            int i= 0;
            while (i<contentValuesArrayList.size()){
                try {
                    retInt += context.getContentResolver().update(QuizDBContract.CategoryEntry.buildUriCategoryIndex(i), contentValuesArrayList.get(i), null, null);
                } finally {
                    i+=1;
                }
            }
//            for (int i = 0; i < contentValuesArrayList.size(); i++){
//                retInt += context.getContentResolver().update(QuizDBContract.CategoryEntry.buildUriCategoryIndex(i), contentValuesArrayList.get(i), null, null);
//            }
        } catch (Exception e){
//            Log.d(LOG_TAG, "error while inserting category records: " + e);
        }
        return retInt;
    }

    //add a QuestionScorer to the member list of QuestionScorers with internal call of QuestionScorer constructor (no time taken)
    public void addQuestionScorer(int questionNumber, int category, int correctAnswer, int chosenAnswer){
        QuestionScorer questionScorer = new QuestionScorer(questionNumber, category, correctAnswer,chosenAnswer);
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    //add a QuestionScorer to the member list of QuestionScorers with internal call of QuestionScorer constructor (with time taken)
    public void addQuestionScorer(int questionNumber, int category, int timeTaken, int correctAnswer, int chosenAnswer){
        QuestionScorer questionScorer = new QuestionScorer(questionNumber, category, timeTaken, correctAnswer,chosenAnswer);
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    //add a pre-constructed QuestionScorer to the member list of QuestionScorers
    public void addQuestionScorer(QuestionScorer questionScorer){
        mQuestionScorers.add(currentQuestionCount,questionScorer);
        currentQuestionCount++;
    }

    public void addQuestionScorerList(ArrayList<QuestionScorer> questionScorers){
        mQuestionScorers.addAll(questionScorers);
        currentQuestionCount+= questionScorers.size();
    }

    //calculate and return the overall int score of the current quiz
    public int scoreQuiz(ArrayList<QuestionScorer> quizQuestions) throws Exception{
        int finalScore = 0;
        if (quizQuestions == null){
            throw new Exception("scorequiz() called with null arraylist");
        }
        for (int i = 0; i < quizQuestions.size(); i++){
            if (quizQuestions.get(i).getQuestionEvaluation()){
                finalScore ++;
            }
        }
        return finalScore;
    }

    //returns a category-wise breakdown of the current quiz,
    // with the first int[] providing the total number of questions from each category in the quiz,
    //  and the second int[] providing the correctly answered question from each category in the quiz.
    //  Refer to the respective indices using IndividualQuestions fields starting with 'CATEGORY_'
    public ArrayList<int[]> getCategoryScoreReport() throws NullPointerException{
        if (mQuestionScorers==null){
            throw new NullPointerException("getCategoryScoreReport() called with null mQuestionScorers");
        }
        mCategoryScoreReport = new ArrayList<>(2);
//        Log.d("getCategoryScoreReport", "checkpoint 1");
        ArrayList<String> categoryList = IndividualQuestion.categoryList;
        int numberOfCategories = categoryList.size();
//        Log.d("getCategoryScoreReport", "checkpoint 2");
        int[] totalCategoryQuestions = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] correctCategoryAnswers = Utility.returnArrayOfZeroInts(numberOfCategories);
//        Log.d("getCategoryScoreReport", "checkpoint 3");
        for (int i = 0; i < mQuestionScorers.size(); i++){
//            Log.d("getCategoryScoreReport", "checkpoint 4: i iteration " + Integer.toString(i));
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);
            for (int c = 0; c < numberOfCategories; c++){
                if (thisQuestionScorer.getCategory()==c){
//                    Log.d("getCategoryScoreReport", "checkpoint 4.1: c iteration " + Integer.toString(c) + " i iteration " + Integer.toString(i));
                    totalCategoryQuestions[c] = totalCategoryQuestions[c]+1;
//                    Log.d("getCategoryScoreReport", "checkpoint 4.2: c iteration " + Integer.toString(c) + " i iteration " + Integer.toString(i));
//                    Log.d("getCategoryScoreReport", Integer.toString(totalCategoryQuestions[c]) + " questions in category " + Integer.toString(c));
                    if (thisQuestionScorer.getQuestionEvaluation()){
                        correctCategoryAnswers[c] = correctCategoryAnswers[c]+1;
//                        Log.d("getCategoryScoreReport", "checkpoint 4.3: c iteration " + Integer.toString(c) + " i iteration " + Integer.toString(i));
//                        Log.d("getCategoryScoreReport", Integer.toString(correctCategoryAnswers[i]) + " correct answers in category " + Integer.toString(i));
                    }
                }
            }
        }
        mCategoryScoreReport.add(SCORES_TOTAL_CATEGORY_QUESTIONS, totalCategoryQuestions);
//        Log.d("getCategoryScoreReport", "totalCategoryQuestions length is " + totalCategoryQuestions.length);
        mCategoryScoreReport.add(SCORES_CORRECT_CATEGORY_ANSWERS, correctCategoryAnswers);
//        Log.d("getCategoryScoreReport", "correctCategoryAnswers length is " + correctCategoryAnswers.length);
        return mCategoryScoreReport;
    }


    //converts the output from the getCategoryScoreReport() into and arraylist of strings for presentation in the UI
    public ArrayList<String> getQuizCategoryScoreReportScoreStrings(ArrayList<int[]> scoreReportInt){
        int[] totalCategoryQuestions = scoreReportInt.get(SCORES_TOTAL_CATEGORY_QUESTIONS);
        int[] correctCategoryAnswers = scoreReportInt.get(SCORES_CORRECT_CATEGORY_ANSWERS);
        int numberOfCategories = totalCategoryQuestions.length;
        ArrayList<String> returnList = new ArrayList<>(numberOfCategories);
        for (int i = 0; i < numberOfCategories; i++){
            returnList.add(Utility.returnOUTOFString(correctCategoryAnswers[i],totalCategoryQuestions[i]));
        }
        return returnList;
    }


    public ArrayList<int[]> getOverallTimeReport(){
        mOverallTimeReport = new ArrayList<>(2);
        int[] correctOverallTimes = new int[mQuestionScorers.size()];
        int[] wrongOverallTimes = new int[mQuestionScorers.size()];
        for (int i = 0; i < mQuestionScorers.size(); i++){
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);

            if (thisQuestionScorer.getQuestionEvaluation()){
                correctOverallTimes[i] = thisQuestionScorer.getTimeTaken();
                wrongOverallTimes[i] = -1;
            } else {
                wrongOverallTimes[i] = thisQuestionScorer.getTimeTaken();
                correctOverallTimes[i]=-1;
            }
//            Log.d(LOG_TAG + "overallTime " + Integer.toString(i), Double.toString(correctOverallTimes[i]));
        }
        mOverallTimeReport.add(TIMES_CORRECT_OVERALL,correctOverallTimes);
        mOverallTimeReport.add(TIMES_WRONG_OVERALL, wrongOverallTimes);
        return mOverallTimeReport;
    }

    public double getTimeAverageCorrect(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] correctAnswerTimes = mOverallTimeReport.get(TIMES_CORRECT_OVERALL);
        return Utility.getAverageFromIntListWITHNegativeValueElimination(correctAnswerTimes);
    }

    public double getTimeAverageWrong(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] wrongAnswerTimes = mOverallTimeReport.get(TIMES_WRONG_OVERALL);
        return Utility.getAverageFromIntListWITHNegativeValueElimination(wrongAnswerTimes);
    }

    public double getTimeAverageAllQuestions(){
        if (mOverallTimeReport ==null) {
            mOverallTimeReport = getOverallTimeReport();
        }
        int[] wrongAnswerTimes = mOverallTimeReport.get(TIMES_WRONG_OVERALL);
        int[] correctAnswerTimes = getOverallTimeReport().get(TIMES_CORRECT_OVERALL);
        return Utility.getAverageFromCOMPLEMENTARYIntListsWITHNegativeValueElimination(wrongAnswerTimes,correctAnswerTimes);
    }


    public ArrayList<int[]> getCategoryTotalTimeReport(){
        ArrayList<int[]> returnList = new ArrayList<>(2);
        int numberOfCategories = categoryList.size();
        int[] overallCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] correctCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        int[] wrongCategoryTimes = Utility.returnArrayOfZeroInts(numberOfCategories);
        for (int i = 0; i < mQuestionScorers.size(); i++){
            QuestionScorer thisQuestionScorer = mQuestionScorers.get(i);

//            Log.d(LOG_TAG + "overallTime " + Integer.toString(i), Double.toString(correctOverallTimes[i]));

            for (int c = 0; c < numberOfCategories; c++){
                if (thisQuestionScorer.getCategory()==c){
                    overallCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
                    if (thisQuestionScorer.getQuestionEvaluation()){
                        correctCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
//                        Log.d(LOG_TAG + "correctTime " + Integer.toString(c), Double.toString(correctCategoryTimes[c]));
                    } else {
                        wrongCategoryTimes[c] += thisQuestionScorer.getTimeTaken();
//                        Log.d(LOG_TAG + "wrongTime " + Integer.toString(c), Double.toString(wrongCategoryTimes[c]));
                    }
                }
            }
        }
        returnList.add(TIMES_CORRECT_BY_CATEGORY,correctCategoryTimes);
        returnList.add(TIMES_WRONG_BY_CATEGORY, wrongCategoryTimes);
        returnList.add(TIMES_OVERALL_BY_CATEGORY,overallCategoryTimes);
        mCategoryTotalTimeReport = returnList;
        return returnList;
    }

    public ArrayList<double[]> getCategoryAverageTimeReport(){
        ArrayList<double[]> returnList = new ArrayList<>(2);
        if (mCategoryTotalTimeReport==null){
            mCategoryTotalTimeReport = getCategoryTotalTimeReport();
        }
        if (mCategoryScoreReport == null){
            mCategoryScoreReport = getCategoryScoreReport();
        }
        int[] correctAnswerTimes = mCategoryTotalTimeReport.get(TIMES_CORRECT_BY_CATEGORY);
        int[] wrongAnswerTimes = mCategoryTotalTimeReport.get(TIMES_WRONG_BY_CATEGORY);
        int[] totalAnswerInstances = mCategoryScoreReport.get(SCORES_TOTAL_CATEGORY_QUESTIONS);
        int[] correctAnswerInstances = mCategoryScoreReport.get(SCORES_CORRECT_CATEGORY_ANSWERS);

        double[] correctAnswerAverages = Utility.returnArrayOfZeroDoubles(correctAnswerTimes.length);
        double[] wrongAnswerAverages = Utility.returnArrayOfZeroDoubles(wrongAnswerTimes.length);
        double[] overallAnswerAverages = Utility.returnArrayOfZeroDoubles(correctAnswerTimes.length);

        for (int i = 0; i<correctAnswerTimes.length; i++){
            if (correctAnswerInstances[i]>0){
                correctAnswerAverages[i] = correctAnswerTimes[i]/correctAnswerInstances[i];
            }
            if (totalAnswerInstances[i]-correctAnswerInstances[i]>0){
                wrongAnswerAverages[i] = wrongAnswerTimes[i]/(totalAnswerInstances[i]-correctAnswerInstances[i]);
            }
            if (totalAnswerInstances[i]>0){
                overallAnswerAverages[i] = (correctAnswerTimes[i] + wrongAnswerTimes[i])/totalAnswerInstances[i];
            }
        }
        returnList.add(TIMES_CORRECT_BY_CATEGORY,correctAnswerAverages);
        returnList.add(TIMES_WRONG_BY_CATEGORY, wrongAnswerAverages);
        returnList.add(TIMES_OVERALL_BY_CATEGORY, overallAnswerAverages);
        mCategoryAverageTimeReport = returnList;
        return mCategoryAverageTimeReport;
    }

    public double getTimeAverageCORRECTForCategory(int category){
        double returnDouble = 0.00;
        if (mCategoryAverageTimeReport==null){
            mCategoryAverageTimeReport = getCategoryAverageTimeReport();
        }
        try {
            returnDouble = mCategoryAverageTimeReport.get(TIMES_CORRECT_BY_CATEGORY)[category];
        } catch (IndexOutOfBoundsException e){
//            Log.d(LOG_TAG,"Exception in getTimeAverageCORRECTForCategory: " + e.getMessage());
        }
        return returnDouble;
    }

    public double getTimeAverageWRONGForCategory(int category){
        double returnDouble = 0.00;
        if (mCategoryAverageTimeReport==null){
            mCategoryAverageTimeReport = getCategoryAverageTimeReport();
        }
        try {
            returnDouble = mCategoryAverageTimeReport.get(TIMES_WRONG_BY_CATEGORY)[category];
        } catch (IndexOutOfBoundsException e){
//            Log.d(LOG_TAG,"Exception in getTimeAverageWRONGForCategory: " + e.getMessage());
        }
        return returnDouble;
    }

    public double getTimeAverageOVERALLForCategory(int category){
        double returnDouble = 0.00;
        if (mCategoryAverageTimeReport==null){
            mCategoryAverageTimeReport = getCategoryAverageTimeReport();
        }
        try {
            returnDouble = mCategoryAverageTimeReport.get(TIMES_OVERALL_BY_CATEGORY)[category];
        } catch (IndexOutOfBoundsException e){
//            Log.d(LOG_TAG,"Exception in getTimeAverageOVERALLForCategory: " + e.getMessage());
        }
        return returnDouble;
    }
}
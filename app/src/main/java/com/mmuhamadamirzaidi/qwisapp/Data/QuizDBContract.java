package com.mmuhamadamirzaidi.qwisapp.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;

public class QuizDBContract {
    public static final String CONTENT_AUTHORITY = "com.mmuhamadamirzaidi.qwisapp.Data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public QuizDBContract() {
    }


    public static abstract class QuizEntry implements BaseColumns {
        public static final String TABLE_NAME = "quizEntry";
        public static final String COLUMN_NAME_QUIZ_SIZE = "quizSize";
        public static final String COLUMN_NAME_SCORE = "quizScore";
        public static final String COLUMN_NAME_AVERAGE_TIME_OVERALL = "quizTimeOverall";
        public static final String COLUMN_NAME_AVERAGE_TIME_CORRECT = "quizTimeCorrect";
        public static final String COLUMN_NAME_AVERAGE_TIME_WRONG = "quizTimeWrong";

        public static final String PATH_QUIZ = "quiz";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUIZ).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;

        //The following 5 methods are to build URIs for various selections of data in the quiz table. These URIs will be recognized by the content provider to look up desired data
        public static Uri buildUriQuizId(long id){
            Uri uri = CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
////            Log.d("testCategoryId",uri.toString());
            return uri;
        }

        public static Uri buildUriQuizScoreForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_SCORE).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriQuizTimeOverallForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_AVERAGE_TIME_OVERALL).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriQuizTimeCorrectForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_AVERAGE_TIME_CORRECT).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriQuizTimeWrongForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_AVERAGE_TIME_WRONG).appendPath(_ID).appendPath(Long.toString(id)).build();
        }
    }

    public static abstract class CategoryEntry implements BaseColumns{
        public static final String TABLE_NAME = "categoryEntry";
        public static final String COLUMN_NAME_NAME = "categoryName";
        public static final String COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED = "categoryTotalQuestions";
        public static final String COLUMN_NAME_CORRECTLY_ANSWERED = "categoryCorrectlyAnswered";
        public static final String COLUMN_NAME_TOTAL_TIME_OVERALL = "categoryTimeOverall";
        public static final String COLUMN_NAME_TOTAL_TIME_CORRECT = "categoryTimeCorrect";
        public static final String COLUMN_NAME_TOTAL_TIME_WRONG = "categoryTimeWrong";

        public static final String PATH_CATEGORY = "category";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        public static Uri buildUriCategoryId(long id){
            Uri uri = CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
////            Log.d("testCategoryId",uri.toString());
            return uri;
        }

        //The following 7 methods are to build URIs for various selections of data in the quiz table. These URIs will be recognized by the content provider to look up desired data
        public static Uri buildUriCategoryIndex(int index){
            Uri uri = CONTENT_URI.buildUpon().appendPath(Integer.toString(index + 1)).build();
////            Log.d("testCategoryId",uri.toString());
            return uri;
        }

        public static Uri buildUriCategoryName(String name){
            int categoryId = (IndividualQuestion.categoryList.indexOf(name))+1;
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(categoryId)).build();
        }

        public static Uri buildUriCategoryTotalQuestionsAnsweredForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriCategoryCorrectlyAnsweredForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_CORRECTLY_ANSWERED).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriCategoryTimeOverallForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_TOTAL_TIME_OVERALL).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriCategoryTimeCorrectForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_TOTAL_TIME_CORRECT).appendPath(_ID).appendPath(Long.toString(id)).build();
        }

        public static Uri buildUriCategoryTimeWrongForId(long id){
            return CONTENT_URI.buildUpon().appendPath(COLUMN_NAME_TOTAL_TIME_WRONG).appendPath(_ID).appendPath(Long.toString(id)).build();
        }
    }
}
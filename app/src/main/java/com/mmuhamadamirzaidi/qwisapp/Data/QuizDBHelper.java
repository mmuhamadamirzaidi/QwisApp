package com.mmuhamadamirzaidi.qwisapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;

import java.util.ArrayList;

public class QuizDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "QuizRecords.db";
    private static QuizDBHelper sQuizDBHelper;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String PRIMARY_KEY_CONSTRAINT = " PRIMARY KEY";
    private static final String UNIQUE_CONSTRAINT = " UNIQUE";
    private static final String NOT_NULL_CONSTRAINT = " NOT NULL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_QUIZ_ENTRIES =
            "CREATE TABLE " + QuizDBContract.QuizEntry.TABLE_NAME + " (" +
                    QuizDBContract.QuizEntry._ID + INTEGER_TYPE + PRIMARY_KEY_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE + INTEGER_TYPE + NOT_NULL_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.QuizEntry.COLUMN_NAME_SCORE + INTEGER_TYPE + NOT_NULL_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL + REAL_TYPE + NOT_NULL_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT + REAL_TYPE + NOT_NULL_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG + REAL_TYPE + NOT_NULL_CONSTRAINT + " )";

    private static final String SQL_CREATE_CATEGORY_ENTRIES =
            "CREATE TABLE " + QuizDBContract.CategoryEntry.TABLE_NAME + " (" +
                    QuizDBContract.QuizEntry._ID + INTEGER_TYPE + PRIMARY_KEY_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_NAME + TEXT_TYPE + UNIQUE_CONSTRAINT + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED + INTEGER_TYPE + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED + INTEGER_TYPE + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL + INTEGER_TYPE + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT + INTEGER_TYPE + COMMA_SEP +
                    QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_QUIZ_ENTRIES =
            "DROP TABLE IF EXISTS " + QuizDBContract.QuizEntry.TABLE_NAME;

    private static final String SQL_DELETE_CATEGORY_ENTRIES =
            "DROP TABLE IF EXISTS " + QuizDBContract.CategoryEntry.TABLE_NAME;

    private QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static QuizDBHelper getInstance(Context context){
        if (sQuizDBHelper == null){
            sQuizDBHelper = new QuizDBHelper(context.getApplicationContext());
        }
        return sQuizDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUIZ_ENTRIES);
        db.execSQL(SQL_CREATE_CATEGORY_ENTRIES);
        ArrayList<ContentValues> initialCategoryValues = initialCategoryContentValues();
        for (int i=0; i<initialCategoryValues.size(); i++){
            db.insert(QuizDBContract.CategoryEntry.TABLE_NAME, null, initialCategoryValues.get(i));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_QUIZ_ENTRIES);
        db.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private ArrayList<ContentValues> initialCategoryContentValues(){
        ArrayList<String> categoryList = IndividualQuestion.categoryList;
        ArrayList<ContentValues> returnList = new ArrayList<>(categoryList.size());
        for (int i = 0; i< categoryList.size(); i++){
            ContentValues cv = new ContentValues();
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_NAME, categoryList.get(i));
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED, 0);
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED, 0);
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL, 0);
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT, 0);
            cv.put(QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG, 0);
            returnList.add(i,cv);
        }
        return returnList;
    }
}

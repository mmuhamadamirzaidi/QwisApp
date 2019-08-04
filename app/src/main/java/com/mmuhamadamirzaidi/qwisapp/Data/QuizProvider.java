package com.mmuhamadamirzaidi.qwisapp.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

public class QuizProvider extends ContentProvider {

    private static final String LOG_TAG = "QuizProvider";
    private QuizDBHelper mQuizDBHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static final int QUIZ_ALL = 99;
    static final int QUIZ_ID = 100;
    static final int QUIZ_SCORE_FOR_ID = 101;
    static final int QUIZ_TIME_OVERALL_FOR_ID = 102;
    static final int QUIZ_TIME_CORRECT_FOR_ID = 103;
    static final int QUIZ_TIME_WRONG_FOR_ID = 104;

    static final int CATEGORY_ALL = 199;
    static final int CATEGORY_ID = 200;
    static final int CATEGORY_NAME = 201;
    static final int CATEGORY_TOTAL_ANSWERED_FOR_ID = 202;
    static final int CATEGORY_CORRECTLY_ANSWERED_FOR_ID = 203;
    static final int CATEGORY_TIME_OVERALL_FOR_ID = 204;
    static final int CATEGORY_TIME_CORRECT_FOR_ID = 205;
    static final int CATEGORY_TIME_WRONG_FOR_ID = 206;

    private static final String sQuizIdSelection = QuizDBContract.QuizEntry.TABLE_NAME + "." + QuizDBContract.QuizEntry._ID + "=?";

    private static final String sCategoryIdSelection = QuizDBContract.CategoryEntry.TABLE_NAME + "." + QuizDBContract.CategoryEntry._ID + "=?";
    private static final String sCategoryNameSelection = QuizDBContract.CategoryEntry.TABLE_NAME + "." + QuizDBContract.CategoryEntry.COLUMN_NAME_NAME + "=?";

    private String[] getIDParamFromNonIDQueries(Uri uri){
        return new String[]{uri.getPathSegments().get(3)};
    }

    private String[] getIDFromIDQuery(Uri uri){
        return new String[]{uri.getPathSegments().get(1)};
    }

    private String[] getNameFromCategoryNameQuery(Uri uri){
        return new String[]{uri.getPathSegments().get(2)};
    }

    static{
        sUriMatcher.addURI(QuizDBContract.CONTENT_AUTHORITY,
                QuizDBContract.QuizEntry.PATH_QUIZ,
                QUIZ_ALL);
        sUriMatcher.addURI(QuizDBContract.CONTENT_AUTHORITY,
                QuizDBContract.QuizEntry.PATH_QUIZ + "/#",
                QUIZ_ID);
        sUriMatcher.addURI(QuizDBContract.CONTENT_AUTHORITY,
                QuizDBContract.CategoryEntry.PATH_CATEGORY,
                CATEGORY_ALL);
        sUriMatcher.addURI(QuizDBContract.CONTENT_AUTHORITY,
                QuizDBContract.CategoryEntry.PATH_CATEGORY + "/#",
                CATEGORY_ID);
        sUriMatcher.addURI(QuizDBContract.CONTENT_AUTHORITY,
                QuizDBContract.CategoryEntry.PATH_CATEGORY + "/" + QuizDBContract.CategoryEntry.COLUMN_NAME_NAME + "/*",
                CATEGORY_NAME);
    }

    @Override
    public boolean onCreate() {
        mQuizDBHelper = QuizDBHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor retCursor = null;
        SQLiteDatabase quizDb = mQuizDBHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case QUIZ_ALL:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, strings, s, strings1, null,null, s1);
                break;
            }
            case QUIZ_ID:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, strings, sQuizIdSelection, getIDFromIDQuery(uri), null, null, s1);
                break;
            }
            case CATEGORY_ALL:{
                retCursor = quizDb.query(QuizDBContract.CategoryEntry.TABLE_NAME, strings, s, strings1, null,null, s1);
                break;
            }
            case CATEGORY_ID:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, strings, sCategoryIdSelection, getIDFromIDQuery(uri), null, null, s1);
                break;
            }
            case CATEGORY_NAME:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, strings, sCategoryNameSelection, getNameFromCategoryNameQuery(uri), null, null, s1);
                break;
            }
            default: {
                retCursor = null;
                break;
            }
        }
        return retCursor;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        Cursor retCursor = null;
        SQLiteDatabase quizDb = mQuizDBHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case QUIZ_ALL:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, projection, selection, selectionArgs, null,null, sortOrder);
                break;
            }
            case QUIZ_ID:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, projection, sQuizIdSelection, getIDFromIDQuery(uri), null, null, sortOrder);
                break;
            }
            case CATEGORY_ALL:{
                retCursor = quizDb.query(QuizDBContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null,null, sortOrder);
                break;
            }
            case CATEGORY_ID:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, projection, sCategoryIdSelection, getIDFromIDQuery(uri), null, null, sortOrder);
                break;
            }
            case CATEGORY_NAME:{
                retCursor = quizDb.query(QuizDBContract.QuizEntry.TABLE_NAME, projection, sCategoryNameSelection, getNameFromCategoryNameQuery(uri), null, null, sortOrder);
                break;
            }
            default: {
                retCursor = super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal);
                break;
            }
        }
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (contentValues.equals(null)){
            return null;
        }
        Uri retUri = null;
        SQLiteDatabase quizDb = mQuizDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case QUIZ_ALL:{
                if (null==contentValues.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL)){
                    contentValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL, 10);
                }
                if (null==contentValues.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT)){
                    contentValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT, 10);
                }
                if (null==contentValues.getAsInteger(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG)){
                    contentValues.put(QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG, 10);
                }
                try {
                    long _id = quizDb.insertOrThrow(QuizDBContract.QuizEntry.TABLE_NAME, null, contentValues);
                    if (_id>0) {
                        retUri = QuizDBContract.QuizEntry.buildUriQuizId(_id);
                        getContext().getContentResolver().notifyChange(uri, null);
                    } else {
                        retUri = null;
                        throw new android.database.SQLException("Failed to insert row into " + uri + ": returned id of " + Long.toString(_id));
                    }
                } catch (SQLException e){
//                    Log.d(LOG_TAG, e.getMessage());
                }
                break;
            }
            case CATEGORY_ALL:{
                try {
                    long _id = quizDb.insertOrThrow(QuizDBContract.CategoryEntry.TABLE_NAME, null, contentValues);
                    if (_id>0) {
                        retUri = QuizDBContract.CategoryEntry.buildUriCategoryId(_id);
                        getContext().getContentResolver().notifyChange(uri, null);
                    } else {
                        retUri = null;
                        throw new android.database.SQLException("Failed to insert row into " + uri + ": returned id of " + Long.toString(_id));
                    }
                } catch (SQLException e){
//                    Log.d(LOG_TAG, e.getMessage());
                }
                break;
            }
            case CATEGORY_NAME:{
                try {
                    long _id = quizDb.insertOrThrow(QuizDBContract.CategoryEntry.TABLE_NAME, null, contentValues);
                    if (_id>0) {
                        retUri = QuizDBContract.CategoryEntry.buildUriCategoryId(_id);
                        getContext().getContentResolver().notifyChange(uri, null);
                    } else {
                        retUri = null;
                        throw new android.database.SQLException("Failed to insert row into " + uri + ": returned id of " + Long.toString(_id));
                    }
                } catch (SQLException e){
//                    Log.d(LOG_TAG, e.getMessage());
                }
                break;
            }
            default:
                return null;
        }
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.equals(null)){
            return 0;
        }
        int retInt;
        SQLiteDatabase quizDB = mQuizDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case CATEGORY_ID:{
                retInt=quizDB.update(QuizDBContract.CategoryEntry.TABLE_NAME,contentValues,sCategoryIdSelection,getIDFromIDQuery(uri));
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            case CATEGORY_NAME:{
                retInt=quizDB.update(QuizDBContract.CategoryEntry.TABLE_NAME,contentValues,sCategoryNameSelection,getNameFromCategoryNameQuery(uri));
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            default:
                retInt=0;
        }
        return retInt;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        //no delete ability or requirement right now. Will implement if requested
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case QUIZ_ALL:
                return QuizDBContract.QuizEntry.CONTENT_TYPE;
            case QUIZ_ID:
                return QuizDBContract.QuizEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_ALL:
                return QuizDBContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_ID:
                return QuizDBContract.QuizEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_NAME:
                return QuizDBContract.QuizEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}

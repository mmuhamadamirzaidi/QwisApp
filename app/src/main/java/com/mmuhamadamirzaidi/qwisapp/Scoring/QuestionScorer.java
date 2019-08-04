package com.mmuhamadamirzaidi.qwisapp.Scoring;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionScorer implements Parcelable {

    private int mQuestionNumber;
    private int mCategory;
    private int timeTaken;
    private int mCorrectAnswer;
    private int mChosenAnswer;
    private boolean mCorrect;

    public static final int NO_ANSWER = -1;

    public QuestionScorer(int questionNumber, int category, int correctAnswer, int chosenAnswer){
        this.mQuestionNumber = questionNumber;
        this.mCategory = category;
        this.mCorrectAnswer = correctAnswer;
        this.mChosenAnswer = chosenAnswer;
        this.mCorrect = (correctAnswer==chosenAnswer);
        this.timeTaken = 10;
    }

    public QuestionScorer(int questionNumber, int category, int timeTaken, int correctAnswer, int chosenAnswer){
        this.mQuestionNumber = questionNumber;
        this.mCategory = category;
        this.mCorrectAnswer = correctAnswer;
        this.mChosenAnswer = chosenAnswer;
        this.timeTaken = timeTaken;
        this.mCorrect = (correctAnswer==chosenAnswer);
    }

    public QuestionScorer(Parcel in){
        this.mQuestionNumber = in.readInt();
        this.mCategory = in.readInt();
        this.timeTaken = in.readInt();
        this.mCorrectAnswer = in.readInt();
        this.mChosenAnswer = in.readInt();
        boolean[] correct = new boolean[1];
        in.readBooleanArray(correct);
        this.mCorrect = correct[0];
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mQuestionNumber);
        parcel.writeInt(mCategory);
        parcel.writeInt(timeTaken);
        parcel.writeInt(mCorrectAnswer);
        parcel.writeInt(mChosenAnswer);
        boolean[] correct = new boolean[1];
        correct[0] = mCorrect;
        parcel.writeBooleanArray(correct);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator(){
        @Override
        public QuestionScorer createFromParcel(Parcel parcel) {
            return new QuestionScorer(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new QuestionScorer[i];
        }
    };

    public int getChosenAnswer() {
        return mChosenAnswer;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }

    public boolean getQuestionEvaluation(){
        return mCorrect;
    }

    public int getCategory(){
        return mCategory;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }
}

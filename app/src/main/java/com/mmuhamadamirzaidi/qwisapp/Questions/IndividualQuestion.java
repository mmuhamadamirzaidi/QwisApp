package com.mmuhamadamirzaidi.qwisapp.Questions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class IndividualQuestion implements Parcelable {

    public int category;
    public String question;
    public String[] choicesList;
    public int correctAnswer;
    public int questionNumber;

    public static final int CATEGORY_GENERAL = 0;
    public static final int CATEGORY_SCIENCE = 1;
    public static final int CATEGORY_WORLD = 2;
    public static final int CATEGORY_HISTORY = 3;
    public static final int CATEGORY_ENTERTAINMENT = 4;
    public static final int CATEGORY_SPORTS = 5;

    public static final ArrayList<String> categoryList = new ArrayList<String>(Arrays.asList("general","science","world","history","entertainment","sports"));

    public static ArrayList<String> getCategoryList() {
        return categoryList;
    }

    //    private static void createCategoryList(){
//        if (categoryList == null){
//            categoryList =  new ArrayList<String>(5);
//            categoryList.add(0,"general");
//            categoryList.add(1,"science");
//            categoryList.add(2,"world");
//            categoryList.add(3,"science");
//            categoryList.add(4,"sports");
//        }
//    }


//    public IndividualQuestion(int category, String question, String[] choicesList, int correctAnswer) {
//        this.category = category;
//        this.question = question;
//        this.choicesList = choicesList;
//        this.correctAnswer = correctAnswer;
//    }
//
//    public IndividualQuestion(String category, String question, String[] answersList, int correctAnswer) {
//        this.category = categoryList.indexOf(category);
//        this.question = question;
//        this.choicesList = answersList;
//        this.correctAnswer = correctAnswer;
//    }

    public IndividualQuestion(int questionNumber, String category, String question, String[] answersList, int correctAnswer) {
        this.questionNumber = questionNumber;
        this.category = categoryList.indexOf(category);
        this.question = question;
        this.choicesList = answersList;
        this.correctAnswer = correctAnswer;
    }

    public IndividualQuestion(Parcel parcel){
        this.questionNumber = parcel.readInt();
        this.category = parcel.readInt();
        this.question = parcel.readString();
        String[] choicesList = new String[4];
        parcel.readStringArray(choicesList);
        this.choicesList = choicesList;
        this.correctAnswer = parcel.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(questionNumber);
        parcel.writeInt(category);
        parcel.writeString(question);
        parcel.writeStringArray(choicesList);
        parcel.writeInt(correctAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new IndividualQuestion(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new IndividualQuestion[i];
        }
    };
}

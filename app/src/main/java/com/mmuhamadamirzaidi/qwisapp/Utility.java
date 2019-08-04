package com.mmuhamadamirzaidi.qwisapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mmuhamadamirzaidi.qwisapp.Questions.IndividualQuestion;

import java.util.ArrayList;

public class Utility {
    public static final String LOG_TAG = "Utility";
    public static final int INDEX_MAX = 0;
    public static final int INDEX_OCCURRENCE = 1;
    public static final int AVERAGE_VALUE_WHEN_NO_ELEMENTS_IN_LIST = -1;

    public static double calculatePercentage(int total, int actual){
        if (total != 0){
//            Log.d(LOG_TAG + "calculPercentage", "Percentage of " + Integer.toString(actual) + " out of " + Integer.toString(total) + " is " + String.valueOf((Double.valueOf(actual)/Double.valueOf(total)) * 100));
            return (Double.valueOf(actual)/Double.valueOf(total)) * 100;
        } else {
            if (actual == 0){
                return 0;
            }
            return -1;
        }
    }

    public static int[] returnArrayOfZeroInts(int size){
        int[] returnArray = new int[size];
        for (int i = 0; i < size; i++){
            returnArray[i] = 0;
        }
        return returnArray;
    }

    public static double[] returnArrayOfZeroDoubles(int size){
        double[] returnArray = new double[size];
        for (int i = 0; i < size; i++){
            returnArray[i] = 0.00;
        }
        return returnArray;
    }

    public static String[] getCategoryNameList(){
        return (String[]) IndividualQuestion.categoryList.toArray();
    }

    public static String returnOUTOFString(int correct, int total){
        return Integer.toString(correct) + "/" + Integer.toString(total);
    }

    //get average of all non-negative elements in an int[]
    public static double getAverageFromIntListWITHNegativeValueElimination(int[] thisList){
        double returnDouble = 0.00;
        int totalValues = thisList.length;
        int validValues = totalValues;
        for (int i = 0; i< totalValues; i++){
            if (thisList[i]>=0) {
                returnDouble += thisList[i];
            } else {
                validValues-=1;
            }
        }
        if (returnDouble == 0.00){
            return AVERAGE_VALUE_WHEN_NO_ELEMENTS_IN_LIST;
        }
        return (returnDouble/validValues);
    }

    //get average of all non-negative elements in an int[]
    public static double getAverageFromDoubleListWITHNegativeValueElimination(double[] thisList){
        double returnDouble = 0.00;
        int totalValues = thisList.length;
        int validValues = totalValues;
        for (int i = 0; i< totalValues; i++){
            if (thisList[i]>=0) {
//                Log.d(LOG_TAG, "adding this value to doubleList calculation: " + Double.toString(thisList[i]));
                returnDouble += thisList[i];
            } else {
                validValues-=1;
            }
        }
        if (returnDouble == 0.00){
            return AVERAGE_VALUE_WHEN_NO_ELEMENTS_IN_LIST;
        }
        return (returnDouble/validValues);
    }

    //get average of all non-negative elements in a group of two int[] that complement each other to form a complete data set
    public static double getAverageFromCOMPLEMENTARYIntListsWITHNegativeValueElimination(int[] first, int[] second){
        int[] finalArray = new int[first.length];
        for (int i = 0; i< finalArray.length; i++){
            if (first[i]>=0){
                finalArray[i] = first[i];
            } else if (second[i]>=0){
                finalArray[i] = second[i];
            }
        }
        return getAverageFromIntListWITHNegativeValueElimination(finalArray);
    }

    //get average percentage score from two lists, one holding the partial amounts and one holding the respective total amounts.
    public static double getAveragePercentageFromListsOfActualAndTotal(int[] actual, int[] total) throws Exception{
        if (actual.length != total.length){
            throw new Exception("getAveragePercentageFromListsOfActualAndTotal called on lists with different lengths");
        }
        double actualSum = 0;
        double totalSum = 0;
        for (int i=0; i < actual.length; i++){
            actualSum += actual[i];
            totalSum += total[i];
        }
        return (actualSum/totalSum) * 100;
    }

    //get the max value of an int[] with the occurrence
    public static ArrayList<Integer> getMaxWithOccurrence(int[] thisList){
        int max = Integer.MIN_VALUE;
        int occurance = 0;
        for (int i=0; i< thisList.length; i++){
            int thisInt = thisList[i];
            if (thisInt > max){
                max = thisInt;
                occurance = 1;
            } else if (thisInt == max){
                occurance += 1;
            }
        }
        ArrayList<Integer> returnList = new ArrayList<>(2);
        returnList.add(INDEX_MAX, max);
        returnList.add(INDEX_OCCURRENCE, occurance);
        return returnList;
    }
}
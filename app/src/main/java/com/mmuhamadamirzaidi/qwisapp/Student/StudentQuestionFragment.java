package com.mmuhamadamirzaidi.qwisapp.Student;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Questions.QuestionsHandling;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentQuestionFragment extends Fragment {


    private ArrayList<String> mCurrentDisplayQuestion;

    private TextView mQuestionView;
    private Button mChoice1TextView;
    private Button mChoice2TextView;
    private Button mChoice3TextView;
    private Button mChoice4TextView;

    public static Fragment getInstance(ArrayList<String> currentQuestion){
        StudentQuestionFragment fragmentQuestion = new StudentQuestionFragment();
        fragmentQuestion.mCurrentDisplayQuestion = currentQuestion;
        return fragmentQuestion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_student_question, container, false);

        mQuestionView = (TextView)rootview.findViewById(R.id.question_textview);
        mChoice1TextView = (Button)rootview.findViewById(R.id.choice1);
        mChoice2TextView = (Button)rootview.findViewById(R.id.choice2);
        mChoice3TextView = (Button)rootview.findViewById(R.id.choice3);
        mChoice4TextView = (Button)rootview.findViewById(R.id.choice4);


        setAndUpdateTextViews();

        return rootview;
    }

    private void setAndUpdateTextViews(){
        mQuestionView.setText(mCurrentDisplayQuestion.get(QuestionsHandling.INDEX_QUESTION));
        mChoice1TextView.setText(mCurrentDisplayQuestion.get(QuestionsHandling.INDEX_CHOICE_1));
        mChoice2TextView.setText(mCurrentDisplayQuestion.get(QuestionsHandling.INDEX_CHOICE_2));
        mChoice3TextView.setText(mCurrentDisplayQuestion.get(QuestionsHandling.INDEX_CHOICE_3));
        mChoice4TextView.setText(mCurrentDisplayQuestion.get(QuestionsHandling.INDEX_CHOICE_4));
    }

//    private void submitAnswer(int choiceIndex){
//        ((ActivityQuiz)getActivity()).addQuestionScorer(choiceIndex);
//    }

}

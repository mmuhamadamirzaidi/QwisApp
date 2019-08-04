package com.mmuhamadamirzaidi.qwisapp.Student;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Questions.QuestionsHandling;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentQuestionHoneycombFragment extends android.app.Fragment {

    private ArrayList<String> mCurrentDisplayQuestion;

    private TextView mQuestionView;
    private TextView mChoice1TextView;
    private TextView mChoice2TextView;
    private TextView mChoice3TextView;
    private TextView mChoice4TextView;

    public static android.app.Fragment getInstance(ArrayList<String> currentQuestion){
        StudentQuestionHoneycombFragment fragmentQuestion = new StudentQuestionHoneycombFragment();
        fragmentQuestion.mCurrentDisplayQuestion = currentQuestion;
        return fragmentQuestion;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_student_question, container, false);

        mQuestionView = (TextView)rootview.findViewById(R.id.question_textview);
        mChoice1TextView = (TextView)rootview.findViewById(R.id.choice1);
        mChoice2TextView = (TextView)rootview.findViewById(R.id.choice2);
        mChoice3TextView = (TextView)rootview.findViewById(R.id.choice3);
        mChoice4TextView = (TextView)rootview.findViewById(R.id.choice4);
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
}

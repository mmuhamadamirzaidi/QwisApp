package com.mmuhamadamirzaidi.qwisapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mmuhamadamirzaidi.qwisapp.Model.QuestionScore;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.ScoreDetailViewHolder;

public class ScoreDetailActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView listScore;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    String viewUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        //View
        listScore =(RecyclerView)findViewById(R.id.listScore);
//        listScore.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listScore.setLayoutManager(layoutManager);

        if (getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if(!viewUser.isEmpty())
            loadScoreDetail(viewUser);

    }

    private void loadScoreDetail(String viewUser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(QuestionScore.class, R.layout.item_score, ScoreDetailViewHolder.class, question_score.orderByChild("user").equalTo(viewUser)) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {

                viewHolder.IconImage.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_transition_animation));
                viewHolder.Container.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_scale_animation));

                viewHolder.txt_name.setText(model.getCategoryName());
                viewHolder.txt_score.setText(model.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        listScore.setAdapter(adapter);
    }
}

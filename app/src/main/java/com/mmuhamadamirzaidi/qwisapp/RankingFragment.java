package com.mmuhamadamirzaidi.qwisapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.Interface.RankingCallBack;
import com.mmuhamadamirzaidi.qwisapp.Model.QuestionScore;
import com.mmuhamadamirzaidi.qwisapp.Model.Ranking;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.RankingViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    View myFragment;

    RecyclerView listRanking;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTable;
    int sum = 0;

    public static RankingFragment newInstance() {
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTable = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking, container, false);

        //Init view
        listRanking = (RecyclerView) myFragment.findViewById(R.id.listRanking);
        layoutManager = new LinearLayoutManager(getActivity());
//        listRanking.setHasFixedSize(true); //Need to remove if using Firebase Recycler Adapter

        //OrderByChild method sort with ascending, so, need to reverse Recycler data
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        listRanking.setLayoutManager(layoutManager);

//        //Implement callback
//        updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>() {
//            @Override
//            public void callBack(Ranking ranking) {
//                //Update to ranking table
//                rankingTable.child(ranking.getUsername())
//                        .setValue(ranking);
//                //After upload, sort Ranking table and show result
////                showRanking();
//            }
//        });

        //Set adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class, R.layout.item_ranking, RankingViewHolder.class, rankingTable.orderByChild("score")) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {

                viewHolder.IconImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));
                viewHolder.Container.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_animation));

                viewHolder.txt_name.setText(model.getUsername());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                //Fix crash when click to item
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetailActivity.class);
                        scoreDetail.putExtra("viewUser", model.getUsername());
                        startActivity(scoreDetail);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listRanking.setAdapter(adapter);

        return myFragment;
    }

    private void showRanking() {
        //Print log to show
        rankingTable.orderByChild("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Ranking local = data.getValue(Ranking.class);
                    Log.d("DEBUG", local.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateScore(final String username, final RankingCallBack<Ranking> callback) {
        questionScore.orderByChild("user").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(ques.getScore());
                        }
                        //After sum of all score, need to process sum variable here
                        //Firebase is async db, so if process outside, 'sum' value will be reset to 0
                        Ranking ranking = new Ranking(username, sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}

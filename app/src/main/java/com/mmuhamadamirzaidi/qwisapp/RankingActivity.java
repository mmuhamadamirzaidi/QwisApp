package com.mmuhamadamirzaidi.qwisapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Interface.RankingCallBack;
import com.mmuhamadamirzaidi.qwisapp.Model.QuestionScore;
import com.mmuhamadamirzaidi.qwisapp.Model.Ranking;
import com.mmuhamadamirzaidi.qwisapp.ViewHolder.RankingViewHolder;

public class RankingActivity extends AppCompatActivity {

    TextView categorypage, subcategorypage, endpage;
    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTable;
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

//        categorypage = findViewById(R.id.categorypage);
//        subcategorypage = findViewById(R.id.subcategorypage);
        endpage = findViewById(R.id.endpage);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTable = database.getReference("Ranking");

        //Init view
        rankingList = (RecyclerView)findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(this);
        rankingList.setHasFixedSize(true);

        //Order by childsort with ascending, so, need to reverse Recycler data
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        //Implement callback
        updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //Update to ranking table
                rankingTable.child(ranking.getUsername())
                        .setValue(ranking);
                //After upload, sort Ranking table and show result
//                showRanking();
            }
        });

        //Set adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(Ranking.class, R.layout.item_ranking, RankingViewHolder.class, rankingTable.orderByChild("score")) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, Ranking model, int position) {

//                viewHolder.txt_name.setText(model.getUsername());
//                viewHolder.txt_score.setText(model.getScore());

                //Fix crash when click to item
//                viewHolder.setItemClickListener(new ItemClickListener);
            }
        };
    }

    private void showRanking() {
        //Print log to show
        rankingTable.orderByChild("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
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

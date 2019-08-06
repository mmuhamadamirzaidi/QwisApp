package com.mmuhamadamirzaidi.qwisapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmuhamadamirzaidi.qwisapp.Model.Post;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;

public class StudentPostAdapter extends RecyclerView.Adapter<StudentPostAdapter.MyViewHolder> {

    Context context;
    ArrayList<Post> myPost;

    public StudentPostAdapter(Context c, ArrayList<Post> p) {
        context = c;
        myPost = p;
    }


    @NonNull
    @Override
    public StudentPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StudentPostAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_student_post,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentPostAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

        myViewHolder.titlepost.setText(myPost.get(i).getTitlepost());
        myViewHolder.descpost.setText(myPost.get(i).getDescpost());

        final String getTitlePost = myPost.get(i).getTitlepost();
        final String getDescPost = myPost.get(i).getDescpost();
        final String getKeyPost = myPost.get(i).getKeypost();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, ""+getTitlePost, Toast.LENGTH_SHORT).show();

//                Intent editPost = new Intent(context, LecturerUpdatePostActivity.class);
//                editPost.putExtra("titlepost", getTitlePost);
//                editPost.putExtra("descpost", getDescPost);
//                editPost.putExtra("keypost", getKeyPost);
//                context.startActivity(editPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPost.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titlepost, descpost, keypost;
        RelativeLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titlepost = (TextView) itemView.findViewById(R.id.titlepost);
            descpost = (TextView) itemView.findViewById(R.id.descpost);

            container = itemView.findViewById(R.id.container);
        }
    }

}

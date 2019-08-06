package com.mmuhamadamirzaidi.qwisapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mmuhamadamirzaidi.qwisapp.Model.Post;
import com.mmuhamadamirzaidi.qwisapp.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>  {

    Context context;
    ArrayList<Post> myPost;

    public PostAdapter(Context c, ArrayList<Post> p) {
        context = c;
        myPost = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.categoryIconImage.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        myViewHolder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

        myViewHolder.titlepost.setText(myPost.get(i).getTitlepost());
        myViewHolder.descpost.setText(myPost.get(i).getDescdpost());

        final String getTitlePost = myPost.get(i).getTitlepost();
        final String getDescPost = myPost.get(i).getDescdpost();
        final String getKeyPost = myPost.get(i).getKeypost();

//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent editMemo = new Intent(context, UpdateMemoActivity.class);
//                editMemo.putExtra("titlepost", getTitlePost);
//                editMemo.putExtra("descpost", getDescPost);
//                editMemo.putExtra("keypost", getKeyPost);
//                context.startActivity(editMemo);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return myPost.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titlepost, descpost, keypost;
        ImageView categoryIconImage;
        RelativeLayout container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titlepost = (TextView) itemView.findViewById(R.id.titlepost);
            descpost = (TextView) itemView.findViewById(R.id.descpost);

            container = itemView.findViewById(R.id.container);
            categoryIconImage = itemView.findViewById(R.id.categoryIconImage);
        }
    }
}

package com.mmuhamadamirzaidi.qwisapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mmuhamadamirzaidi.qwisapp.Common.Common;
import com.mmuhamadamirzaidi.qwisapp.Model.Category;
import com.mmuhamadamirzaidi.qwisapp.R;
import com.mmuhamadamirzaidi.qwisapp.StartGameActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

    Context context;
    ArrayList<Category> category;

    public CategoryAdapter(Context ctx, ArrayList<Category> ctg) {
        context = ctx;
        category = ctg;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {


        myViewHolder.IconImage.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        myViewHolder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        myViewHolder.Description.setText(category.get(i).getDescription());
        Picasso.get().load(category.get(i).getIconImage()).into(myViewHolder.IconImage);
        Picasso.get().load(category.get(i).getImage()).into(myViewHolder.Image);
        myViewHolder.Name.setText(category.get(i).getName());

        final String getDescriptionCategory = category.get(i).getDescription();
        final String getIconImageCategory = category.get(i).getIconImage();
        final String getImageCategory = category.get(i).getImage();
        final String getNameCategory = category.get(i).getName();
//        final String getKeyCategory = category.get(i).getKey();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGame = new Intent(context, StartGameActivity.class);

//                startGame.putExtra("IconImage", getIconImageCategory);
//                startGame.putExtra("Icon", getImageCategory);

//                Common.categoryId = ;
                startGame.putExtra("Description", getDescriptionCategory);
                startGame.putExtra("Name", getNameCategory);
//                Common.categoryId = myViewHolder.getAdapterPosition();

//                Common.categoryId = myViewHolder.setOnClickListener(view -> delete(viewHolder.getAdapterPosition()));
//                startGame.putExtra("Key", getKeyCategory);
//                startGame.putExtra("Key", getKeyCategory);
//                editMemo.putExtra("titledoes", getTitleDoes);
//                editMemo.putExtra("descdoes", getDescDoes);
//                editMemo.putExtra("datedoes", getDateDoes);
//                editMemo.putExtra("keydoes", getKeyDoes);
                context.startActivity(startGame);
//                Toast.makeText(context, "Category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Name, Description, Key;
        ImageView Image, IconImage;
        RelativeLayout Container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = (TextView) itemView.findViewById(R.id.categoryName);
            Description = (TextView) itemView.findViewById(R.id.categoryDescription);

            Container = itemView.findViewById(R.id.container);
            IconImage = itemView.findViewById(R.id.categoryIconImage);
            Image = itemView.findViewById(R.id.categoryImage);
        }
    }
}

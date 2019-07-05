package com.mmuhamadamirzaidi.qwisapp.Adapters;

import android.content.Context;
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

import com.mmuhamadamirzaidi.qwisapp.Model.Category;
import com.mmuhamadamirzaidi.qwisapp.R;
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

        myViewHolder.Name.setText(category.get(i).getName());
        myViewHolder.Description.setText(category.get(i).getDescription());
        Picasso.get().load(category.get(i).getIconImage()).into(myViewHolder.IconImage);
        Picasso.get().load(category.get(i).getImage()).into(myViewHolder.Image);

        final String getNameCategory = category.get(i).getName();
        final String getDescriptionCategory = category.get(i).getDescription();
        final String getImageCategory = category.get(i).getImage();
        final String getIconImageCategory = category.get(i).getIconImage();
        final String getKeyCategory = category.get(i).getKey();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent editMemo = new Intent(context, UpdateMemoActivity.class);
//                editMemo.putExtra("titledoes", getTitleDoes);
//                editMemo.putExtra("descdoes", getDescDoes);
//                editMemo.putExtra("datedoes", getDateDoes);
//                editMemo.putExtra("keydoes", getKeyDoes);
//                context.startActivity(editMemo);
                Toast.makeText(context, "Category", Toast.LENGTH_SHORT).show();
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

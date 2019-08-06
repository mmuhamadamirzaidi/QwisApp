package com.mmuhamadamirzaidi.qwisapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Name, Description;
    public ImageView IconImage;
    public RelativeLayout Container;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        Container = itemView.findViewById(R.id.container);

        Name = (TextView) itemView.findViewById(R.id.categoryName);
        Description = (TextView) itemView.findViewById(R.id.categoryDescription);

        Container = itemView.findViewById(R.id.container);
        IconImage = itemView.findViewById(R.id.categoryIconImage);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}

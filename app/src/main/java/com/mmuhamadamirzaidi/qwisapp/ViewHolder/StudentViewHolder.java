package com.mmuhamadamirzaidi.qwisapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Interface.ItemClickListener;
import com.mmuhamadamirzaidi.qwisapp.R;

public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView txt_name, txt_email;
    private ItemClickListener itemClickListener;

    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_name = (TextView)itemView.findViewById(R.id.txt_name);
        txt_email = (TextView)itemView.findViewById(R.id.txt_email);

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

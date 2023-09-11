package com.example.cw;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageview;
    TextView articleName;
    TextView articleDescription;
    TextView articleContent;
    MyAdapter.OnItemClickListener onItemClickListener;

    public MyViewHolder(@NonNull View itemView, MyAdapter.OnItemClickListener onItemClickListener) {
        // viewholder for the news articles
        super(itemView);
        imageview = itemView.findViewById(R.id.imageview);
        articleName = itemView.findViewById(R.id.articleName);
        articleDescription = itemView.findViewById(R.id.articleDescription);
        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // goes onto the article of choice
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

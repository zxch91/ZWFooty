package com.example.cw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for a single comment and creates a new CommentViewHolder to hold the view
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.authorTextView.setText(comment.getAuthor());
        holder.contentTextView.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        // Returns the number of comments in the list
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder class that holds the author and content
        TextView authorTextView;
        TextView contentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            // Sets the author and content TextViews
            super(itemView);
            authorTextView = itemView.findViewById(R.id.comment_author);
            contentTextView = itemView.findViewById(R.id.comment_content);
        }
    }
}


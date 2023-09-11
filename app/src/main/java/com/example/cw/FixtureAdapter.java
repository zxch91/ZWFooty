package com.example.cw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {

    private List<Fixture> fixtures;
    private Context context;

    public FixtureAdapter(List<Fixture> fixtures, Context context) {
        this.fixtures = fixtures;
        this.context = context;
    }

    @NonNull
    @Override
    public FixtureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single fixture item and create a new FixtureViewHolder to hold the view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fixture_item, parent, false);
        return new FixtureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FixtureViewHolder holder, int position) {
        // Get the fixture at the given position in the list and set the team names and score TextViews in the ViewHolder
        Fixture fixture = fixtures.get(position);
        holder.team1.setText(fixture.getTeam1());
        holder.score.setText(fixture.getScore());
        holder.team2.setText(fixture.getTeam2());
    }

    @Override
    public int getItemCount() {
        // Return the number of fixtures in the list
        return fixtures.size();
    }

    public class FixtureViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder class that holds the team names, score, and down arrow ImageButton
        TextView team1, score, team2;
        ImageButton downArrow;

        public FixtureViewHolder(@NonNull View itemView) {
            // Set the team names and score TextViews and down arrow ImageButton
            super(itemView);
            team1 = itemView.findViewById(R.id.team1_name);
            score = itemView.findViewById(R.id.team_score);
            team2 = itemView.findViewById(R.id.team2_name);

        }

    }
}

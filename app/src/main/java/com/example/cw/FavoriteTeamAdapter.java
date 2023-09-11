package com.example.cw;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteTeamAdapter extends RecyclerView.Adapter<FavoriteTeamAdapter.TeamViewHolder> {
    private final Context context;
    private List<Team> teams;

    public FavoriteTeamAdapter(List<Team> teams, Context context) {
        this.teams = teams;
        this.context = context;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single favorite team and create a new TeamViewHolder to hold the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        // Get the team at the given position in the list and set the team name TextView in the ViewHolder
        Team team = teams.get(position);
        holder.teamName.setText(team.getTeamName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            // Set an OnClickListener for the ViewHolder's item view that opens a Google search for the team name when clicked
            @Override
            public void onClick(View view) {
                String teamName = team.getTeamName();
                String searchQuery = "https://www.google.com/search?q=" + Uri.encode(String.valueOf(teamName));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchQuery));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of favorite teams in the list
        return teams.size();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder class that holds the team name
        TextView teamName;

        public TeamViewHolder(@NonNull View itemView) {
            // Set the team name TextView
            super(itemView);
            teamName = itemView.findViewById(R.id.team_name);
        }


    }
}


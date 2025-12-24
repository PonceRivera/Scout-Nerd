package com.scoutnerd.app.ui.team;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scoutnerd.app.R;
import com.scoutnerd.app.data.local.entity.TeamEntity;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<TeamEntity> mTeams;

    public void setTeams(List<TeamEntity> teams) {
        mTeams = teams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        if (mTeams != null) {
            TeamEntity current = mTeams.get(position);
            holder.numberView.setText(String.valueOf(current.teamNumber));
            holder.nicknameView.setText(current.nickname);
            holder.locationView.setText(current.city);
        }
    }

    @Override
    public int getItemCount() {
        return mTeams != null ? mTeams.size() : 0;
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        final TextView numberView;
        final TextView nicknameView;
        final TextView locationView;

        TeamViewHolder(View itemView) {
            super(itemView);
            numberView = itemView.findViewById(R.id.text_team_number);
            nicknameView = itemView.findViewById(R.id.text_team_nickname);
            locationView = itemView.findViewById(R.id.text_team_location);
        }
    }
}

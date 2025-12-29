package com.scoutnerd.app.ui.analytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scoutnerd.app.R;
import com.scoutnerd.app.data.local.dao.MatchResultDao;
import java.util.ArrayList;
import java.util.List;

public class TeamAnalyticsAdapter extends RecyclerView.Adapter<TeamAnalyticsAdapter.ViewHolder> {

    private List<MatchResultDao.TeamMatchCount> mData = new ArrayList<>();
    private final OnTeamClickListener mListener;

    public interface OnTeamClickListener {
        void onTeamClick(int teamNumber);
    }

    public TeamAnalyticsAdapter(OnTeamClickListener listener) {
        mListener = listener;
    }

    public void setData(List<MatchResultDao.TeamMatchCount> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchResultDao.TeamMatchCount item = mData.get(position);
        holder.text1.setText("Team " + item.teamNumber);
        holder.text2.setText(item.matchCount + " Matches Played");

        holder.text1.setTextColor(android.graphics.Color.WHITE);
        holder.text2.setTextColor(android.graphics.Color.LTGRAY);

        holder.itemView.setOnClickListener(v -> mListener.onTeamClick(item.teamNumber));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}

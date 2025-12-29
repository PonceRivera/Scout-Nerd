package com.scoutnerd.app.ui.analytics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.scoutnerd.app.R;

public class AnalyticsFragment extends Fragment {
    private AnalyticsViewModel mViewModel;
    private androidx.recyclerview.widget.RecyclerView mRecyclerView;
    private TeamAnalyticsAdapter mAdapter;
    private android.widget.TextView mTotalMatchesText;
    private android.widget.TextView mAvgScoreText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTotalMatchesText = view.findViewById(R.id.text_total_matches);
        mAvgScoreText = view.findViewById(R.id.text_avg_score);

        mRecyclerView = view.findViewById(R.id.recycler_team_analytics);
        mRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

        mAdapter = new TeamAnalyticsAdapter(teamNumber -> {
            // TODO: Navigate to Team Detail
            android.widget.Toast.makeText(getContext(), "Clicked Team " + teamNumber, android.widget.Toast.LENGTH_SHORT)
                    .show();
            // Bundle args = new Bundle();
            // args.putInt("team_number", teamNumber);
            // Navigation.findNavController(view).navigate(R.id.to_team_detail, args);
        });
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new androidx.lifecycle.ViewModelProvider(this).get(AnalyticsViewModel.class);

        // TODO: Use real event key from settings
        String eventKey = "2025txho";

        // Observe Match Counts (General Activity)
        mViewModel.getTeamMatchCounts(eventKey).observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                mAdapter.setData(stats);

                int totalMatches = 0;
                for (com.scoutnerd.app.data.local.dao.MatchResultDao.TeamMatchCount stat : stats) {
                    totalMatches += stat.matchCount;
                }
                mTotalMatchesText.setText(String.valueOf(totalMatches / 6)); // Approx matches
            } else {
                mTotalMatchesText.setText("0");
            }
        });

        // Observe Real Average Score
        mViewModel.getEventAvgScore(eventKey).observe(getViewLifecycleOwner(), score -> {
            if (score != null) {
                if (mAvgScoreText != null)
                    mAvgScoreText.setText(String.format("%.1f", score));
            } else {
                if (mAvgScoreText != null)
                    mAvgScoreText.setText("0.0");
            }
        });
    }
}

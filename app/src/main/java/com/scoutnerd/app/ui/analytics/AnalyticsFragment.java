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
    private android.widget.TextView mTopTeamsText;
    private android.widget.TextView mTotalMatchesText;
    private android.widget.TextView mAvgScoreText; // Placeholder for now

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTopTeamsText = view.findViewById(R.id.text_top_teams); // Need to add ID to XML
        mTotalMatchesText = view.findViewById(R.id.text_total_matches); // Need to add ID to XML
        mAvgScoreText = view.findViewById(R.id.text_avg_score); // Need to add ID to XML

        mViewModel = new androidx.lifecycle.ViewModelProvider(this).get(AnalyticsViewModel.class);

        // TODO: Use real event key from settings
        String eventKey = "2025txho";

        // Observe Match Counts (General Activity)
        mViewModel.getTeamMatchCounts(eventKey).observe(getViewLifecycleOwner(), stats -> {
            if (stats != null && !stats.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                int totalMatches = 0;

                // Show Top 5 most active teams
                for (int i = 0; i < Math.min(stats.size(), 5); i++) {
                    com.scoutnerd.app.data.local.dao.MatchResultDao.TeamMatchCount stat = stats.get(i);
                    sb.append(String.format("%d. Team %d (%d matches)\n", i + 1, stat.teamNumber, stat.matchCount));
                    totalMatches += stat.matchCount; // Just a sum of team-matches
                }

                mTopTeamsText.setText(sb.toString());
                mTotalMatchesText.setText(String.valueOf(totalMatches / 6)); // Approx matches
            } else {
                mTopTeamsText.setText("No data collected yet.");
                mTotalMatchesText.setText("0");
            }
        });

        // Observe a specific metric (e.g. Coral Level 4 - ID 13 from defaults)
        // Note: Metric IDs might vary if auto-generated, ideally look up by name.
        // Assuming ID 13 is "Coral Level 4" from ScoutRepository defaults.
        mViewModel.getAvgStat(eventKey, 13).observe(getViewLifecycleOwner(), stats -> {
            if (stats != null && !stats.isEmpty()) {
                float globalAvg = 0;
                for (com.scoutnerd.app.data.local.dao.MatchResultDao.TeamStat s : stats) {
                    globalAvg += s.avgValue;
                }
                globalAvg /= stats.size();
                if (mAvgScoreText != null)
                    mAvgScoreText.setText(String.format("%.1f", globalAvg));
            }
        });
    }
}

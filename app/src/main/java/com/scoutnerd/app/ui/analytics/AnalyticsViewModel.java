package com.scoutnerd.app.ui.analytics;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.scoutnerd.app.data.local.AppDatabase;
import com.scoutnerd.app.data.local.dao.MatchResultDao;
import com.scoutnerd.app.data.repository.ScoutRepository; // If needed, or access DB directly via helper
import com.scoutnerd.app.utils.AppExecutors;
import java.util.List;

public class AnalyticsViewModel extends AndroidViewModel {

    private final MatchResultDao mDao;
    // Hardcoded for demo: "Coral Level 4" metric ID usually is generated.
    // In real app we'd look up metric ID by name first.
    // For now we will fetch match counts as a general stat.

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);
        mDao = AppDatabase.getDatabase(application).matchResultDao();
    }

    // Example stat: Number of matches played per team
    public LiveData<List<MatchResultDao.TeamMatchCount>> getTeamMatchCounts(String eventKey) {
        return mDao.getTeamMatchCounts(eventKey);
    }

    // Example stat: Average Score (Assuming metric ID 1 is total score or similar,
    // but we'll use a placeholder logic if ID doesn't exist yet)
    // Let's expose a method that fragments can call with a specific metric ID
    // Let's expose a method that fragments can call with a specific metric ID
    public LiveData<List<MatchResultDao.TeamStat>> getAvgStat(String eventKey, long metricId) {
        return mDao.getAverageMetricStats(eventKey, metricId);
    }

    public LiveData<Float> getEventAvgScore(String eventKey) {
        return mDao.getEventAverageScore(eventKey);
    }
}

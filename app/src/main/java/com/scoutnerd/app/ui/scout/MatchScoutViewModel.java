package com.scoutnerd.app.ui.scout;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.scoutnerd.app.data.local.AppDatabase;
import com.scoutnerd.app.data.local.entity.MatchResultEntity;
import com.scoutnerd.app.data.local.entity.MetricEntity;
import com.scoutnerd.app.data.repository.ScoutRepository;
import com.scoutnerd.app.utils.AppExecutors;
import java.util.List;

public class MatchScoutViewModel extends AndroidViewModel {

    private final ScoutRepository mRepository;
    private final LiveData<List<MetricEntity>> mMetrics;

    public MatchScoutViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        AppExecutors executors = AppExecutors.getInstance();
        mRepository = ScoutRepository.getInstance(database, executors);

        // Ensure defaults exist
        mRepository.initializeDefaults();

        mMetrics = mRepository.getActiveMetrics();
    }

    public LiveData<List<MetricEntity>> getMetrics() {
        return mMetrics;
    }

    public LiveData<List<com.scoutnerd.app.data.local.entity.EventEntity>> getEvents(int year) {
        return mRepository.getEvents(year);
    }

    public void saveMatch(int matchNumber, int teamNumber, String eventKey, List<MatchResultEntity> results) {
        // In a real app, process the list. For now, we save one by one or batch if
        // Repository supported it.
        // Repository supports single save for simplicity, let's just loop (not
        // efficient but fine for prototype)
        for (MatchResultEntity result : results) {
            mRepository.saveMatchResult(result);
        }
    }
}

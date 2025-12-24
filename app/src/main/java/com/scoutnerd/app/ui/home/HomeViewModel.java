package com.scoutnerd.app.ui.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.scoutnerd.app.data.local.AppDatabase;
import com.scoutnerd.app.data.local.entity.EventEntity;
import com.scoutnerd.app.data.repository.ScoutRepository;
import com.scoutnerd.app.utils.AppExecutors;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final ScoutRepository mRepository;
    private final LiveData<List<EventEntity>> mEvents;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        // In a real app, use Dependency Injection (Hilt)
        AppDatabase database = AppDatabase.getDatabase(application);
        AppExecutors executors = AppExecutors.getInstance();
        mRepository = ScoutRepository.getInstance(database, executors);

        // Hardcoded year for testing
        mEvents = mRepository.getEvents(2025);
    }

    public LiveData<List<EventEntity>> getEvents() {
        return mEvents;
    }
}

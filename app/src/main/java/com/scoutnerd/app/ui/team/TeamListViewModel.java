package com.scoutnerd.app.ui.team;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.scoutnerd.app.data.local.AppDatabase;
import com.scoutnerd.app.data.local.entity.TeamEntity;
import com.scoutnerd.app.data.repository.ScoutRepository;
import com.scoutnerd.app.utils.AppExecutors;
import java.util.List;

public class TeamListViewModel extends AndroidViewModel {

    private final ScoutRepository mRepository;
    private final MutableLiveData<String> mEventKey = new MutableLiveData<>();
    private final LiveData<List<TeamEntity>> mTeams;

    public TeamListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ScoutRepository.getInstance(
                AppDatabase.getDatabase(application),
                AppExecutors.getInstance());

        mTeams = Transformations.switchMap(mEventKey, key -> {
            if (key == null) {
                return new MutableLiveData<>();
            }
            return mRepository.getTeamsAtEvent(key);
        });
    }

    public void setEventKey(String eventKey) {
        if (mEventKey.getValue() == null || !mEventKey.getValue().equals(eventKey)) {
            mEventKey.setValue(eventKey);
        }
    }

    public LiveData<List<TeamEntity>> getTeams() {
        return mTeams;
    }
}

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
    private final MutableLiveData<String> mSearchQuery = new MutableLiveData<>("");
    private final LiveData<List<TeamEntity>> mFilteredTeams;

    public TeamListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ScoutRepository.getInstance(
                AppDatabase.getDatabase(application),
                AppExecutors.getInstance());

        // Load specific event teams
        LiveData<List<TeamEntity>> allTeams = Transformations.switchMap(mEventKey, key -> {
            if (key == null) {
                return new MutableLiveData<>();
            }
            return mRepository.getTeamsAtEvent(key);
        });

        // Filter whenever query or list changes
        mFilteredTeams = Transformations.switchMap(allTeams, teams -> {
            return Transformations.map(mSearchQuery, query -> {
                if (query == null || query.isEmpty()) {
                    return teams;
                }
                List<TeamEntity> filtered = new java.util.ArrayList<>();
                for (TeamEntity team : teams) {
                    if (String.valueOf(team.teamNumber).contains(query) ||
                            (team.nickname != null && team.nickname.toLowerCase().contains(query.toLowerCase()))) {
                        filtered.add(team);
                    }
                }
                return filtered;
            });
        });
    }

    public void setEventKey(String eventKey) {
        if (mEventKey.getValue() == null || !mEventKey.getValue().equals(eventKey)) {
            mEventKey.setValue(eventKey);
        }
    }

    public void setSearchQuery(String query) {
        mSearchQuery.setValue(query);
    }

    public LiveData<List<TeamEntity>> getTeams() {
        return mFilteredTeams;
    }
}

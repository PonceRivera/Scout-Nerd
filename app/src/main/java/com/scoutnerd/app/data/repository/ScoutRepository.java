package com.scoutnerd.app.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import com.scoutnerd.app.data.api.RetrofitClient;
import com.scoutnerd.app.data.api.TbaApiService;
import com.scoutnerd.app.data.api.model.ApiEvent;
import com.scoutnerd.app.data.local.AppDatabase;
import com.scoutnerd.app.data.local.entity.EventEntity;
import com.scoutnerd.app.data.local.entity.EventTeamJoin;
import com.scoutnerd.app.data.local.entity.TeamEntity;
import com.scoutnerd.app.utils.AppExecutors;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoutRepository {

    private static ScoutRepository sInstance;
    private final AppDatabase mDb;
    private final TbaApiService mApiService;
    private final AppExecutors mExecutors;

    private ScoutRepository(AppDatabase database, AppExecutors executors) {
        mDb = database;
        mExecutors = executors;
        mApiService = RetrofitClient.getApiService();
    }

    public static synchronized ScoutRepository getInstance(AppDatabase database, AppExecutors executors) {
        if (sInstance == null) {
            sInstance = new ScoutRepository(database, executors);
        }
        return sInstance;
    }

    // Logic: Return local data, but trigger a network refresh
    public LiveData<List<EventEntity>> getEvents(int year) {
        refreshEvents(year);
        return mDb.eventDao().getAllEvents();
    }

    public LiveData<List<TeamEntity>> getTeamsAtEvent(String eventKey) {
        refreshTeamsAtEvent(eventKey);
        return mDb.teamDao().getTeamsForEvent(eventKey);
    }

    private void refreshEvents(int year) {
        mApiService.getEvents(year).enqueue(new Callback<List<ApiEvent>>() {
            @Override
            public void onResponse(@NonNull Call<List<ApiEvent>> call, @NonNull Response<List<ApiEvent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mExecutors.diskIO().execute(() -> {
                        List<EventEntity> entities = new ArrayList<>();
                        for (ApiEvent apiEvent : response.body()) {
                            // Basic mapping - can be moved to a mapper class
                            entities.add(new EventEntity(
                                    apiEvent.key,
                                    apiEvent.name,
                                    apiEvent.eventCode,
                                    apiEvent.startDate,
                                    apiEvent.city));
                        }
                        mDb.eventDao().insertEvents(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApiEvent>> call, @NonNull Throwable t) {
                // Handle error or just rely on local data
            }
        });
    }

    private void refreshTeamsAtEvent(String eventKey) {
        mApiService.getTeamsAtEvent(eventKey).enqueue(new Callback<List<com.scoutnerd.app.data.api.model.ApiTeam>>() {
            @Override
            public void onResponse(@NonNull Call<List<com.scoutnerd.app.data.api.model.ApiTeam>> call,
                    @NonNull Response<List<com.scoutnerd.app.data.api.model.ApiTeam>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mExecutors.diskIO().execute(() -> {
                        List<TeamEntity> teams = new ArrayList<>();
                        List<EventTeamJoin> joins = new ArrayList<>();

                        for (com.scoutnerd.app.data.api.model.ApiTeam apiTeam : response.body()) {
                            teams.add(new TeamEntity(
                                    apiTeam.key,
                                    apiTeam.teamNumber,
                                    apiTeam.nickname,
                                    apiTeam.name,
                                    apiTeam.city));
                            joins.add(new EventTeamJoin(eventKey, apiTeam.key));
                        }

                        mDb.teamDao().insertTeams(teams);
                        mDb.teamDao().insertEventTeamJoins(joins);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<com.scoutnerd.app.data.api.model.ApiTeam>> call, @NonNull Throwable t) {
                // Log error
            }
        });
    }
}

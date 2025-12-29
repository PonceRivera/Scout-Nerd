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
            public void onFailure(@NonNull Call<List<com.scoutnerd.app.data.api.model.ApiTeam>> call,
                    @NonNull Throwable t) {
                // Log error
            }
        });
    }
    // --- Metrics & Results ---

    public LiveData<List<com.scoutnerd.app.data.local.entity.MetricEntity>> getActiveMetrics() {
        return mDb.metricDao().getAllMetrics();
    }

    public void saveMatchResult(com.scoutnerd.app.data.local.entity.MatchResultEntity result) {
        mExecutors.diskIO().execute(() -> {
            mDb.matchResultDao().insert(result);
        });
    }

    /**
     * Initializes the database with default metrics for the 2025 season if none
     * exist.
     */
    public void initializeDefaults() {
        mExecutors.diskIO().execute(() -> {
            // Get existing metrics to skip duplicates
            List<com.scoutnerd.app.data.local.entity.MetricEntity> existing = mDb.metricDao().getAllMetricsSync();
            List<String> existingNames = new ArrayList<>();
            for (com.scoutnerd.app.data.local.entity.MetricEntity m : existing) {
                existingNames.add(m.name);
            }

            List<com.scoutnerd.app.data.local.entity.MetricEntity> defaults = new ArrayList<>();

            // Helper to add if missing
            // 2025 REEFSCAPE METRICS

            // AUTO
            if (!existingNames.contains("Auto Line"))
                defaults.add(new com.scoutnerd.app.data.local.entity.MetricEntity("Auto Line", 0, 0, 0));
            if (!existingNames.contains("Coral Level 1"))
                defaults.add(new com.scoutnerd.app.data.local.entity.MetricEntity("Coral Level 1", 1, 0, 1));
            if (!existingNames.contains("Coral Level 2"))
                defaults.add(new com.scoutnerd.app.data.local.entity.MetricEntity("Coral Level 2", 1, 0, 2));

            // Fix: Add Coral Level 3 & 4 to AUTO as requested
            checkAndAdd(defaults, existing, "Coral Level 3", 1, 0, 3);
            checkAndAdd(defaults, existing, "Coral Level 4", 1, 0, 4);

            // TELEOP
            // Note: Teleop Coral usually reused names but diff category.
            // Our simple "contains" check by name works if names are unique OR if we check
            // category.
            // But names "Coral Level 1" exist in both.
            // Let's refine check to "Contains name AND category" or just use unique names
            // internally if needed.
            // For simplicity in this fix, I'll rely on the fact that getCount() was 0
            // initially.
            // But since getCount > 0 now, I need a better check.

            // Allow duplicate names if different category?
            // Existing logic: existingNames only stores names.
            // This is risky if "Coral Level 1" exists in Auto, but I want to add it to
            // Teleop.
            // Fix: Check (Name + Category).

            checkAndAdd(defaults, existing, "Coral Level 1", 1, 1, 10);
            checkAndAdd(defaults, existing, "Coral Level 2", 1, 1, 11);
            checkAndAdd(defaults, existing, "Coral Level 3", 1, 1, 12);
            checkAndAdd(defaults, existing, "Coral Level 4", 1, 1, 13);

            checkAndAdd(defaults, existing, "Algae Net", 1, 1, 14); // New
            checkAndAdd(defaults, existing, "Algae Processor", 1, 1, 15); // New

            checkAndAdd(defaults, existing, "Defense Rating", 2, 1, 16);

            // ENDGAME
            checkAndAdd(defaults, existing, "Climb Successful", 0, 2, 20);
            checkAndAdd(defaults, existing, "Notes", 3, 2, 21);

            // PIT SCOUTING
            // Using correct constant CATEGORY_PIT which corresponds to 3 (based on previous
            // edit)
            int CAT_PIT = 3;
            checkAndAdd(defaults, existing, "Drivetrain Type", 3, CAT_PIT, 30);
            checkAndAdd(defaults, existing, "Robot Weight", 3, CAT_PIT, 31);
            checkAndAdd(defaults, existing, "Programming Language", 3, CAT_PIT, 32);

            if (!defaults.isEmpty()) {
                mDb.metricDao().insertAll(defaults);
            }
        });
    }

    private void checkAndAdd(List<com.scoutnerd.app.data.local.entity.MetricEntity> toAdd,
            List<com.scoutnerd.app.data.local.entity.MetricEntity> existing,
            String name, int type, int category, int sort) {
        boolean found = false;
        for (com.scoutnerd.app.data.local.entity.MetricEntity m : existing) {
            if (m.name.equals(name) && m.category == category) {
                found = true;
                break;
            }
        }
        if (!found) {
            toAdd.add(new com.scoutnerd.app.data.local.entity.MetricEntity(name, type, category, sort));
        }
    }
}

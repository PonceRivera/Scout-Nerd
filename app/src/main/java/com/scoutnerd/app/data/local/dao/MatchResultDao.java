package com.scoutnerd.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.scoutnerd.app.data.local.entity.MatchResultEntity;
import java.util.List;

@Dao
public interface MatchResultDao {
    @Insert
    void insert(MatchResultEntity result);

    @Query("SELECT * FROM match_results WHERE eventKey = :eventKey AND teamNumber = :teamNumber")
    LiveData<List<MatchResultEntity>> getResultsForTeam(String eventKey, int teamNumber);

    @Query("SELECT * FROM match_results WHERE eventKey = :eventKey")
    List<MatchResultEntity> getAllResultsForEvent(String eventKey);

    // Analytics Queries
    // Get average of a specific numeric metric per team, ordered by average desc
    @Query("SELECT teamNumber, AVG(CAST(value AS FLOAT)) as avgValue " +
            "FROM match_results " +
            "WHERE eventKey = :eventKey AND metricId = :metricId " +
            "GROUP BY teamNumber " +
            "ORDER BY avgValue DESC")
    LiveData<List<TeamStat>> getAverageMetricStats(String eventKey, long metricId);

    // Get total matches played per team
    @Query("SELECT teamNumber, COUNT(DISTINCT matchNumber) as matchCount " +
            "FROM match_results " +
            "WHERE eventKey = :eventKey " +
            "GROUP BY teamNumber " +
            "ORDER BY matchCount DESC")
    LiveData<List<TeamMatchCount>> getTeamMatchCounts(String eventKey);

    // Helper classes for query results
    class TeamStat {
        public int teamNumber;
        public float avgValue;
    }

    class TeamMatchCount {
        public int teamNumber;
        public int matchCount;
    }
}

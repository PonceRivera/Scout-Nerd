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

    // Calculate Average Score per match (across all teams)
    // Formula: Sum(Value * Weight) / Count(Matches)
    // Simplified: We calculate the score for each match-team entry, then average
    // those scores.
    // Weights: AutoLine=3, Coral1=2, Coral2=3, Coral3=4, Coral4=5, Climb=10,
    // Algae=4
    @Query("SELECT AVG(match_score) FROM (" +
            "  SELECT SUM(" +
            "    CASE " +
            "      WHEN m.name = 'Auto Line' AND r.value = 'true' THEN 3 " +
            "      WHEN m.name = 'Climb Successful' AND r.value = 'true' THEN 10 " +
            "      WHEN m.name LIKE 'Coral Level 1' THEN 2 * CAST(r.value AS INTEGER) " +
            "      WHEN m.name LIKE 'Coral Level 2' THEN 3 * CAST(r.value AS INTEGER) " +
            "      WHEN m.name LIKE 'Coral Level 3' THEN 4 * CAST(r.value AS INTEGER) " +
            "      WHEN m.name LIKE 'Coral Level 4' THEN 5 * CAST(r.value AS INTEGER) " +
            "      WHEN m.name LIKE 'Algae%' THEN 4 * CAST(r.value AS INTEGER) " +
            "      ELSE 0 " +
            "    END" +
            "  ) as match_score " +
            "  FROM match_results r " +
            "  JOIN metrics m ON r.metricId = m.id " +
            "  WHERE r.eventKey = :eventKey " +
            "  GROUP BY r.matchNumber, r.teamNumber" +
            ")")
    LiveData<Float> getEventAverageScore(String eventKey);
}

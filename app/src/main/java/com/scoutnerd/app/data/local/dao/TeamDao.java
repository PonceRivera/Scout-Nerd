package com.scoutnerd.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.scoutnerd.app.data.local.entity.EventTeamJoin;
import com.scoutnerd.app.data.local.entity.TeamEntity;
import java.util.List;

@Dao
public interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTeams(List<TeamEntity> teams);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEventTeamJoins(List<EventTeamJoin> joins);

    @Transaction
    @Query("SELECT * FROM teams INNER JOIN event_teams ON teams.`key` = event_teams.teamKey WHERE event_teams.eventKey = :eventKey ORDER BY teams.teamNumber ASC")
    LiveData<List<TeamEntity>> getTeamsForEvent(String eventKey);
}
